"""Official API preflight. Never prints credentials or full provider responses."""
from pathlib import Path
import json, urllib.request, urllib.error, time

root = Path(__file__).resolve().parents[2]
config = {}
for line in (root / 'project/local/deepseek.env').read_text().splitlines():
    if '=' in line and not line.lstrip().startswith('#'):
        key, value = line.split('=', 1)
        config[key.strip()] = value.strip().strip('"').strip("'")
credential = config.get('DEEPSEEK_API_KEY', '')
if not credential:
    raise SystemExit('DeepSeek credential not configured')
messages = [{'role': 'user', 'content': 'Call get_demo_status to check the synthetic WMS demo. Then acknowledge the result.'}]
payload = {
    'model': config.get('DEEPSEEK_MODEL', 'deepseek-v4-pro'),
    'thinking': {'type': 'disabled'}, 'messages': messages,
    'tools': [{'type': 'function', 'function': {
        'name': 'get_demo_status', 'description': 'Return synthetic demo readiness',
        'parameters': {'type': 'object', 'properties': {}, 'additionalProperties': False}}}],
    'max_tokens': 256, 'temperature': 0,
}
started = time.time()
def call():
    request = urllib.request.Request('https://api.deepseek.com/chat/completions', json.dumps(payload).encode(),
        {'Content-Type': 'application/json', 'Authorization': 'Bearer ' + credential})
    with urllib.request.urlopen(request, timeout=45) as response:
        return json.load(response)
try:
    first = call()
    message = first['choices'][0]['message']
    calls = message.get('tool_calls', [])
    assert len(calls) == 1 and calls[0]['function']['name'] == 'get_demo_status', 'Expected registered tool call'
    messages.append(message)
    messages.append({'role': 'tool', 'tool_call_id': calls[0]['id'], 'content': json.dumps({'synthetic': True, 'factories': 12, 'status': 'development'})})
    second = call()
    evidence = {'stage': 'official-provider-tool-roundtrip', 'model': first.get('model'),
        'response_ids': [first.get('id'), second.get('id')], 'elapsed_seconds': round(time.time()-started, 2),
        'usage': [first.get('usage'), second.get('usage')], 'tool': 'get_demo_status',
        'tool_result_returned': True, 'product_business_acceptance': False}
    folder = root / 'project/files/releases/V1.0.0/evidence'
    folder.mkdir(exist_ok=True, parents=True)
    (folder/'deepseek-preflight.json').write_text(json.dumps(evidence, ensure_ascii=False, indent=2)+'\n')
    print(json.dumps(evidence, ensure_ascii=False))
except urllib.error.HTTPError as error:
    print('Provider HTTP status:', error.code)
    raise SystemExit(1)
