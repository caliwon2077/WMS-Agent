"""Real API acceptance against an isolated SQLite copy; synthetic fixtures only."""
from pathlib import Path
import csv, io, json, os, shutil, socket, sqlite3, subprocess, tempfile, time, urllib.request, urllib.error, uuid

ROOT = Path(__file__).resolve().parents[2]
OUT = ROOT / 'project/files/releases/V1.0.0/evidence'
OUT.mkdir(parents=True, exist_ok=True)
TMP = Path(tempfile.mkdtemp(prefix='wms-demo-acceptance-'))
DATABASE = TMP / 'demo.sqlite'
with socket.socket() as listener:
    listener.bind(('127.0.0.1', 0)); PORT = listener.getsockname()[1]
BASE = f'http://127.0.0.1:{PORT}/api/v1/demo'
LOCAL_JAVA = ROOT / '.tools/jdk-17.0.21+9/Contents/Home/bin/java'
JAVA = str(LOCAL_JAVA) if LOCAL_JAVA.is_file() else (str(Path(os.environ['JAVA_HOME'])/'bin/java') if os.environ.get('JAVA_HOME') else shutil.which('java'))
if not JAVA: raise SystemExit('Java is required; install JDK 17 or set JAVA_HOME')
JAR = ROOT / 'wms_system_javabackend/target/wms-system-backend-1.0.0.jar'
checks, runs, process = [], [], None
token = ''
log = (TMP / 'server.log').open('w')

def start():
    global process
    process = subprocess.Popen([str(JAVA), '-jar', str(JAR), '--server.address=127.0.0.1', f'--server.port={PORT}',
        f'--app.database.path={DATABASE}', f'--app.demo.config-path={ROOT}/project/local/deepseek.env'], cwd=ROOT, stdout=log, stderr=subprocess.STDOUT)
    for _ in range(80):
        try:
            with urllib.request.urlopen(f'http://127.0.0.1:{PORT}/api/v1/system/health', timeout=1): return
        except Exception:
            if process.poll() is not None: raise AssertionError('Test server failed; see isolated log')
            time.sleep(.25)
    raise AssertionError('Test server did not become ready')

def stop():
    if process and process.poll() is None:
        process.terminate()
        try: process.wait(timeout=12)
        except subprocess.TimeoutExpired: process.kill(); process.wait(timeout=5)

def request(path, body=None, bearer=None, raw=False, method=None, content_type=None):
    headers = {'Authorization': 'Bearer ' + (token if bearer is None else bearer)}
    if body is not None:
        if not isinstance(body, bytes): body = json.dumps(body).encode(); content_type = 'application/json'
        headers['Content-Type'] = content_type
    req = urllib.request.Request(BASE+path, body, headers, method=method or ('POST' if body is not None else 'GET'))
    try:
        with urllib.request.urlopen(req, timeout=15) as response:
            data = response.read()
            if raw: return response.status, data
            payload = json.loads(data)
            assert payload['success'], payload
            return payload['data']
    except urllib.error.HTTPError as e:
        return {'http_error': e.code, 'payload': json.loads(e.read())}

def check(name, condition, detail=None):
    record = {'name': name, 'passed': bool(condition), 'detail': detail}
    checks.append(record); print(json.dumps(record, ensure_ascii=False), flush=True)
    if not condition: raise AssertionError(name)

def login(user='admin'):
    result = request('/login', {'username': user, 'password': 'Demo@2026'}, bearer='')
    assert 'token' in result, result
    return result['token']

def poll(run, expected=None):
    deadline = time.monotonic()+190
    while time.monotonic() < deadline:
        run = request('/runs/'+run['id'])
        if run['state'] not in ['queued','running']:
            runs.append(run)
            if expected: check('run terminal '+run['id'], run['state'] in expected, {'state':run['state'], 'error':run.get('error')})
            return run
        time.sleep(.3)
    raise AssertionError('Run timeout')

def chat(message, factory='ALL', conversation=''):
    return poll(request('/chat', {'message':message,'factory_id':factory,'conversation_id':conversation}))

def upload(filename):
    boundary = 'WMS' + uuid.uuid4().hex
    content = b''
    for key,value in {'factory':'F03','purpose':'snapshot','period':'2026-10','synthetic':'true'}.items():
        content += f'--{boundary}\r\nContent-Disposition: form-data; name="{key}"\r\n\r\n{value}\r\n'.encode()
    file = ROOT/'project/files/releases/V1.0.0/fixtures'/filename
    content += f'--{boundary}\r\nContent-Disposition: form-data; name="file"; filename="{filename}"\r\nContent-Type: text/csv\r\n\r\n'.encode()+file.read_bytes()+f'\r\n--{boundary}--\r\n'.encode()
    return request('/imports/preview', content, content_type='multipart/form-data; boundary='+boundary)

try:
    start(); token = login()
    boot = request('/bootstrap'); check('official model configured', boot['model']['configured'] and boot['model']['model']=='deepseek-v4-pro')
    check('12 factories', len(boot['factories'])==12)
    check('432 synthetic assets', request('/assets?factory_id=ALL')['total']==432)
    stock = login('factory01')
    check('cross-factory aggregate rejected', request('/assets?factory_id=F02', bearer=stock).get('http_error')==403)
    check('cross-factory source rejected', request('/sources/AS-F02-0025', bearer=stock).get('http_error')==403)
    request('/logout', {}, bearer=stock); check('logout invalidates token', request('/bootstrap', bearer=stock).get('http_error')==401)
    ambiguous = chat('查一下12厂的资产状态')
    check('ambiguous 12 factory asks clarification', ambiguous['state']=='waiting_clarification', ambiguous.get('question'))
    for iteration in range(3):
        template = 'V1' if iteration==1 else 'V2'
        job = request('/reports', {'factory_ids':['F01','F02','F03'],'period':'2026-10','template':template,'request_key':uuid.uuid4().hex})
        report = poll(job, ['partial'] if iteration==0 else ['success'])
        if iteration==0:
            check('missing factory only counts 72', report['report']['metrics']['total']==72 and report['report']['missing_factories']==['F03'])
            initial_children = {c['factory']:c['id'] for c in report['children'] if c['state']=='success'}
            bad = upload('F03_C_缺少运行状态_错误样例.csv'); check('bad layout has zero business write', bad['state']=='invalid')
            check('invalid batch cannot commit', request('/imports/'+bad['id']+'/confirm',{}).get('http_error')==400)
            good = upload('F03_C_2026-10_模拟盘点.csv'); check('C mapping preview', good['state']=='preview' and good['row_count']==36)
            check('preview does not create snapshot', not [s for s in request('/snapshots') if s['factory']=='F03' and s['period']=='2026-10'])
            saved = request('/imports/'+good['id']+'/confirm',{}); repeated = request('/imports/'+good['id']+'/confirm',{})
            check('snapshot confirmation idempotent', saved['snapshot_id']==repeated['snapshot_id'])
            check('snapshot leaves master asset count unchanged', request('/assets?factory_id=F03')['total']==36)
            report = poll(request('/runs/'+job['id']+'/retry',{}), ['success'])
            kept = {c['factory']:c['id'] for c in report['children'] if c['state']=='success' and c['factory'] in ['F01','F02']}
            check('only incomplete factory retried', kept==initial_children and len(report['children'])==4)
        metrics = report['report']['metrics']
        check(f'round {iteration+1} actual main plus factory agents', all(any(t['action']=='DeepSeek' and t['state']=='success' and t['detail'].get('real_model') for t in c['trace']) for c in report['children'] if c['state']=='success'))
        check(f'round {iteration+1} report totals', metrics['total']==108 and metrics['process']==72 and (metrics.get('stopped')==18 if template=='V2' else 'stopped' not in metrics))
        status, content = request('/runs/'+report['id']+'/export',raw=True)
        exported = list(csv.reader(io.StringIO(content.decode('utf-8-sig'))))
        check(f'round {iteration+1} CSV export', status==200 and len(exported)==5 and exported[-1][7]=='108' and exported[-1][0]=='合成演示数据')
        (OUT/f'monthly-{template}-round{iteration+1}.csv').write_bytes(content)
        saved_report = request('/runs/'+report['id']+'/confirm',{}); check('report version confirmation',saved_report['report_state']=='confirmed')
        check('confirmed report cannot retry or overwrite',request('/runs/'+report['id']+'/retry',{}).get('http_error')==400)
        query_run = chat('查看二厂所有类型未完成的作业计划')
        check(f'round {iteration+1} real plan query',query_run['state']=='success' and any(c['type']=='plans' for c in query_run['cards']))
        task, target = [('WX-02-001','进行中'),('WX-02-002','受阻'),('WX-02-003','进行中')][iteration]
        before = request('/plans/'+task)
        update_run = chat(f'把计划 {task} 改为{target}，备注维修人员已到场',conversation=query_run['conversation_id'])
        check(f'round {iteration+1} real proposal',update_run['state']=='confirmation_pending',update_run.get('error'))
        card = next(c['confirmation'] for c in update_run['cards'] if c['type']=='confirmation')
        unchanged = request('/plans/'+task); check('confirmation before zero writes',unchanged['state']==before['state'] and unchanged['version']==before['version'])
        changed = request('/confirmations/'+card['id']+'/confirm',{}); duplicate = request('/confirmations/'+card['id']+'/confirm',{})
        check('confirmation save and read back once',changed['state']==target and changed['version']==before['version']+1 and duplicate['version']==changed['version'] and request('/plans/'+task)['note']=='维修人员已到场')
        check('run state follows confirmed business result',request('/runs/'+update_run['id'])['state']=='success')
        energy = chat('分析二厂空调2026年11月1日至16日模拟用电，与2026年10月1日至16日对比')
        energy_card = next((c for c in energy.get('cards',[]) if c['type']=='energy'),{})
        check(f'round {iteration+1} actual energy analysis',energy['state']=='success' and energy_card.get('current')==120 and energy_card.get('previous')==100 and energy_card.get('change_percent')==20 and energy_card.get('synthetic'),energy.get('error'))
    plan_before_restart = request('/plans/WX-02-001')
    with sqlite3.connect(DATABASE) as connection:
        connection.execute("INSERT INTO demo_records VALUES (?,?,?,?,?,?,?,?,?)",('INTERRUPTED-QA','run','ALL','admin',1,'running',json.dumps({'run_type':'chat','synthetic':True}),'2026-11-17T10:00:00+08:00','2026-11-17T10:00:00+08:00'))
    stop(); start(); token=login()
    check('restart preserves assets',request('/assets?factory_id=ALL')['total']==432)
    check('restart preserves confirmed plan without replay',request('/plans/WX-02-001')['version']==plan_before_restart['version'])
    check('injected interrupted run is explicit after restart',request('/runs/INTERRUPTED-QA')['state']=='interrupted')
finally:
    stop(); log.close()
    evidence={'date':time.strftime('%Y-%m-%d'),'version':'V1.0.0','scope':'isolated database; official DeepSeek real requests; restart state uses injected in-flight record','checks':checks,'passed':sum(c['passed'] for c in checks),'failed':sum(not c['passed'] for c in checks),'run_evidence':runs,'temporary_database':str(DATABASE)}
    (OUT/'real-api-acceptance.json').write_text(json.dumps(evidence,ensure_ascii=False,indent=2)+'\n')
    print(json.dumps({'passed':evidence['passed'],'failed':evidence['failed'],'evidence':str(OUT/'real-api-acceptance.json')},ensure_ascii=False),flush=True)
