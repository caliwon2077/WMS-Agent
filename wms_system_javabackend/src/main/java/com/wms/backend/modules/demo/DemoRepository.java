package com.wms.backend.modules.demo;

import static com.wms.backend.modules.demo.DemoJson.*;
import java.util.*;
import java.util.function.Supplier;
import javax.annotation.PostConstruct;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.transaction.PlatformTransactionManager;

/** Isolated, versioned demo domain. JSON payloads retain source schemas; indexed columns enforce identity. */
@Repository
public class DemoRepository {
    final JdbcTemplate jdbc;
    private final TransactionTemplate transaction;
    public DemoRepository(JdbcTemplate jdbc,PlatformTransactionManager manager) {this.jdbc=jdbc;this.transaction=new TransactionTemplate(manager);}
    public JdbcTemplate jdbc() { return jdbc; }
    @PostConstruct public void initialize() {
        jdbc.execute("CREATE TABLE IF NOT EXISTS demo_records (id TEXT PRIMARY KEY, kind TEXT NOT NULL, factory TEXT NOT NULL, owner TEXT NOT NULL, version INTEGER NOT NULL, state TEXT NOT NULL, payload TEXT NOT NULL, created_at TEXT NOT NULL, updated_at TEXT NOT NULL)");
        jdbc.execute("CREATE INDEX IF NOT EXISTS demo_records_kind_scope ON demo_records(kind,factory,state)");
        jdbc.execute("CREATE UNIQUE INDEX IF NOT EXISTS demo_asset_code ON demo_records(factory,json_extract(payload,'$.code')) WHERE kind='asset' AND state!='void'");
        jdbc.execute("CREATE TABLE IF NOT EXISTS demo_users (id TEXT PRIMARY KEY, name TEXT NOT NULL, role TEXT NOT NULL, factory TEXT NOT NULL, password_hash TEXT NOT NULL, salt TEXT NOT NULL, revision INTEGER NOT NULL DEFAULT 1, active INTEGER NOT NULL DEFAULT 1)");
        jdbc.execute("CREATE TABLE IF NOT EXISTS demo_sessions (token_hash TEXT PRIMARY KEY, user_id TEXT NOT NULL, revision INTEGER NOT NULL, expires_at TEXT NOT NULL)");
        jdbc.execute("CREATE TABLE IF NOT EXISTS demo_uploads (id TEXT PRIMARY KEY, filename TEXT NOT NULL, bytes BLOB NOT NULL)");
        jdbc.execute("CREATE TABLE IF NOT EXISTS demo_trace (id INTEGER PRIMARY KEY AUTOINCREMENT, run_id TEXT NOT NULL, role TEXT NOT NULL, action TEXT NOT NULL, state TEXT NOT NULL, detail TEXT NOT NULL, created_at TEXT NOT NULL)");
        // A process restart cannot resume an in-flight LLM call or replay a confirmation.
        jdbc.update("UPDATE demo_records SET state='interrupted',updated_at=? WHERE kind='run' AND state IN ('queued','running')",now());
        jdbc.update("DELETE FROM demo_trace WHERE created_at < ?",java.time.OffsetDateTime.now(ZONE).minusDays(7).toString());
        jdbc.update("DELETE FROM demo_sessions WHERE expires_at < ?",now());
    }
    public <T>T tx(Supplier<T> work) {return transaction.execute(status->work.get());}
    private Map<String,Object> decode(Map<String,Object> row) {
        Map<String,Object> m=parse(String.valueOf(row.get("payload")));
        for(String key:Arrays.asList("id","kind","factory","owner","version","state","created_at","updated_at"))m.put(key,row.get(key));
        return m;
    }
    public Map<String,Object> get(String id) {List<Map<String,Object>> rows=jdbc.queryForList("SELECT * FROM demo_records WHERE id=?",id);if(rows.isEmpty())throw bad("NOT_FOUND","记录不存在或不可用");return decode(rows.get(0));}
    public Map<String,Object> find(String id) {try{return get(id);}catch(com.wms.backend.common.exception.BusinessException e){return null;}}
    public List<Map<String,Object>> all(String kind) {List<Map<String,Object>> out=new ArrayList<>();for(Map<String,Object> row:jdbc.queryForList("SELECT * FROM demo_records WHERE kind=? ORDER BY created_at,id",kind))out.add(decode(row));return out;}
    public Map<String,Object> add(String kind,String id,String factory,String owner,String state,Map<String,Object> payload) {
        String time=now();jdbc.update("INSERT INTO demo_records VALUES (?,?,?,?,1,?,?,?,?)",id,kind,factory,owner,state,json(payload),time,time);return get(id);
    }
    public Map<String,Object> save(Map<String,Object> row) {
        int v=num(row,"version",0);
        Map<String,Object> payload=new LinkedHashMap<>(row);payload.remove("trace");payload.remove("children");
        int n=jdbc.update("UPDATE demo_records SET payload=?,state=?,version=version+1,updated_at=? WHERE id=? AND version=?",json(payload),str(row,"state"),now(),str(row,"id"),v);
        if(n!=1)throw bad("VERSION_CONFLICT","记录已被其他操作更新，请刷新后重新确认");return get(str(row,"id"));
    }
    public void event(String factory,String owner,String action,String target,Object before,Object after) {
        add("event",id("EV"),factory,owner,"recorded",map("action",action,"target",target,"before",before,"after",after,"synthetic",true));
    }
    public void trace(String run,String role,String action,String state,Map<String,Object> detail) {
        jdbc.update("INSERT INTO demo_trace(run_id,role,action,state,detail,created_at) VALUES(?,?,?,?,?,?)",run,role,action,state,json(detail),now());
    }
    public List<Map<String,Object>> traces(String run) {List<Map<String,Object>> out=jdbc.queryForList("SELECT role,action,state,detail,created_at FROM demo_trace WHERE run_id=? ORDER BY id",run);for(Map<String,Object> r:out)r.put("detail",parse(str(r,"detail")));return out;}
}
