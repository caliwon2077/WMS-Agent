package com.wms.backend.modules.demo;

import static com.wms.backend.modules.demo.DemoJson.*;
import com.wms.backend.modules.demo.DemoAuth.Actor;
import com.wms.backend.common.api.ApiResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1/demo")
public class DemoController {
    private final DemoRepository repo;private final DemoAuth auth;private final DemoAssetService assets;private final DemoPlanService plans;private final DemoReportData reports;private final DemoAgentService agent;private final DeepSeekClient client;
    public DemoController(DemoRepository r,DemoAuth a,DemoAssetService as,DemoPlanService p,DemoReportData d,DemoAgentService ag,DeepSeekClient c){repo=r;auth=a;assets=as;plans=p;reports=d;agent=ag;client=c;}
    private Actor actor(HttpServletRequest req){return (Actor)req.getAttribute("actor");}
    private <T>ApiResponse<T> ok(T data){return ApiResponse.success(data);}
    @PostMapping("/login") public ApiResponse<?> login(@RequestBody Map<String,Object> p){return ok(auth.login(required(p,"username",40),required(p,"password",200)));}
    @PostMapping("/logout") public ApiResponse<?> logout(HttpServletRequest r){auth.logout(r.getHeader("Authorization"));return ok(map("logged_out",true));}
    @GetMapping("/bootstrap") public ApiResponse<?> bootstrap(HttpServletRequest r){Actor a=actor(r);return ok(map("version","V1.0.0","prd_version","V4.1","user",a.view(),"today",LocalDate.now(ZONE).toString(),"factories",assets.factories(a),"users",auth.users(a),"model",client.status(),"synthetic",true,"report_factories",Arrays.asList("F01","F02","F03"),"templates",Arrays.asList(map("id","V1","name","资产与作业汇总"),map("id","V2","name","增加停机设备数"))));}
    @PostMapping("/users/{id}/revoke") public ApiResponse<?> revoke(HttpServletRequest r,@PathVariable String id){auth.revoke(actor(r),id);return ok(map("revoked",true));}
    @GetMapping("/assets") public ApiResponse<?> assets(HttpServletRequest r,@RequestParam Map<String,String> params){return ok(assets.search(actor(r),new LinkedHashMap<>(params)));}
    @GetMapping("/assets/{id}") public ApiResponse<?> asset(HttpServletRequest r,@PathVariable String id){return ok(assets.get(actor(r),id));}
    @GetMapping("/assets/{id}/history") public ApiResponse<?> history(HttpServletRequest r,@PathVariable String id){return ok(assets.history(actor(r),id));}
    @PostMapping("/assets/classify") public ApiResponse<?> classify(HttpServletRequest r,@RequestBody Map<String,Object> p){return ok(assets.classify(actor(r),p));}
    @GetMapping("/imports") public ApiResponse<?> imports(HttpServletRequest r){Actor a=actor(r);List<Map<String,Object>> out=repo.all("import").stream().filter(x->a.hq()||a.factory.equals(str(x,"factory"))).collect(Collectors.toList());for(Map<String,Object> row:out)row.remove("rows");Collections.reverse(out);return ok(out);}
    @PostMapping("/imports/preview") public ApiResponse<?> preview(HttpServletRequest r,@RequestParam("file") MultipartFile file,@RequestParam String factory,@RequestParam String purpose,@RequestParam(defaultValue="")String period,@RequestParam boolean synthetic)throws Exception{require(synthetic,"Demo 仅接收合成演示文件，请确认数据来源");String filename=file.getOriginalFilename();require(filename!=null&&filename.length()<=150&&!filename.contains("/")&&!filename.contains("\\"),"文件名无效");return ok(assets.preview(actor(r),factory,purpose,period,filename,file.getBytes()));}
    @GetMapping("/imports/{id}") public ApiResponse<?> batch(HttpServletRequest r,@PathVariable String id){return ok(assets.batch(actor(r),id));}
    @PostMapping("/imports/{id}/confirm") public ApiResponse<?> importConfirm(HttpServletRequest r,@PathVariable String id){return ok(assets.commit(actor(r),id));}
    @PostMapping("/imports/{id}/void") public ApiResponse<?> importVoid(HttpServletRequest r,@PathVariable String id){return ok(assets.voidBatch(actor(r),id));}
    @GetMapping("/imports/{id}/file") public ResponseEntity<byte[]> original(HttpServletRequest r,@PathVariable String id){assets.batch(actor(r),id);Map<String,Object> f=repo.jdbc().queryForMap("SELECT * FROM demo_uploads WHERE id=?",id);return download((byte[])f.get("bytes"),str(f,"filename"),"application/octet-stream");}
    @GetMapping("/snapshots") public ApiResponse<?> snapshots(HttpServletRequest r){Actor a=actor(r);List<Map<String,Object>> rr=repo.all("snapshot").stream().filter(x->a.hq()||a.factory.equals(str(x,"factory"))).collect(Collectors.toList());for(Map<String,Object> x:rr){x.put("row_count",rows(x.get("rows")).size());x.remove("rows");}return ok(rr);}
    @GetMapping("/sources/{id}") public ApiResponse<?> source(HttpServletRequest r,@PathVariable String id){return ok(reports.source(actor(r),id));}
    @GetMapping("/plans") public ApiResponse<?> plans(HttpServletRequest r,@RequestParam Map<String,String> params){return ok(plans.search(actor(r),new LinkedHashMap<>(params)));}
    @GetMapping("/plans/{id}") public ApiResponse<?> plan(HttpServletRequest r,@PathVariable String id){return ok(plans.get(actor(r),id));}
    @PostMapping("/plans/propose") public ApiResponse<?> propose(HttpServletRequest r,@RequestBody Map<String,Object> p){p.remove("source_run");return ok(plans.propose(actor(r),p));}
    @PostMapping("/confirmations/{id}/confirm") public ApiResponse<?> confirm(HttpServletRequest r,@PathVariable String id){return ok(plans.confirm(actor(r),id,false));}
    @PostMapping("/confirmations/{id}/cancel") public ApiResponse<?> cancelConfirm(HttpServletRequest r,@PathVariable String id){return ok(plans.confirm(actor(r),id,true));}
    @GetMapping("/approvals") public ApiResponse<?> approvals(HttpServletRequest r){return ok(assets.approvals(actor(r)));}
    @PostMapping("/approvals") public ApiResponse<?> approval(HttpServletRequest r,@RequestBody Map<String,Object> p){return ok(assets.approval(actor(r),p));}
    @GetMapping("/energy") public ApiResponse<?> energy(HttpServletRequest r,@RequestParam Map<String,String> p){return ok(reports.energy(actor(r),new LinkedHashMap<>(p)));}
    @PostMapping("/chat") public ApiResponse<?> chat(HttpServletRequest r,@RequestBody Map<String,Object> p){return ok(agent.chat(actor(r),p));}
    @GetMapping("/runs") public ApiResponse<?> runs(HttpServletRequest r){return ok(agent.list(actor(r)));}
    @GetMapping("/runs/{id}") public ApiResponse<?> run(HttpServletRequest r,@PathVariable String id){return ok(agent.get(actor(r),id));}
    @PostMapping("/runs/{id}/cancel") public ApiResponse<?> cancel(HttpServletRequest r,@PathVariable String id){return ok(agent.cancel(actor(r),id));}
    @PostMapping("/reports") public ApiResponse<?> report(HttpServletRequest r,@RequestBody Map<String,Object> p){return ok(agent.startReport(actor(r),p));}
    @PostMapping("/runs/{id}/retry") public ApiResponse<?> retry(HttpServletRequest r,@PathVariable String id){return ok(agent.retry(actor(r),id));}
    @PostMapping("/runs/{id}/confirm") public ApiResponse<?> confirmReport(HttpServletRequest r,@PathVariable String id){return ok(agent.confirmReport(actor(r),id));}
    @GetMapping("/runs/{id}/export") public ResponseEntity<byte[]> exportReport(HttpServletRequest req,@PathVariable String id){Map<String,Object> r=agent.get(actor(req),id),report=obj(r.get("report"));require(!report.isEmpty(),"报告尚未生成");StringBuilder csv=new StringBuilder("\uFEFF");csv.append(csvRow("数据来源","期间","截止","模板","报告状态","工厂","处理状态","全部设备","工艺设备","非工艺设备","待分类","停机设备","来源"));
        for(Map<String,Object> f:rows(report.get("factories"))){Map<String,Object> m=obj(f.get("metrics"));csv.append(csvRow("合成演示数据",r.get("period"),report.get("cutoff"),r.get("template"),r.get("report_state"),f.get("factory"),f.get("status"),m.get("total"),m.get("process"),m.get("nonprocess"),m.get("unclassified"),m.get("stopped"),f.get("source_ref")));}
        Map<String,Object> m=obj(report.get("metrics"));csv.append(csvRow("合成演示数据",r.get("period"),report.get("cutoff"),r.get("template"),r.get("report_state"),"已覆盖工厂合计",report.get("coverage"),m.get("total"),m.get("process"),m.get("nonprocess"),m.get("unclassified"),m.get("stopped"),id));return download(csv.toString().getBytes(StandardCharsets.UTF_8),"WMS_"+r.get("period")+"_"+r.get("template")+"_模拟月报.csv","text/csv;charset=UTF-8");}
    @GetMapping("/energy/export") public ResponseEntity<byte[]> exportEnergy(HttpServletRequest req,@RequestParam Map<String,String> p){Map<String,Object> r=reports.energy(actor(req),new LinkedHashMap<>(p));String csv="\uFEFF"+csvRow("数据来源","工厂","起始","截止","用电量","单位","对比起始","对比截止","对比用电量","变化率%","说明")+csvRow("模拟能耗",r.get("factory_id"),r.get("start"),r.get("end"),r.get("current"),"kWh",r.get("compare_start"),r.get("compare_end"),r.get("previous"),r.get("change_percent"),r.get("notice"));return download(csv.getBytes(StandardCharsets.UTF_8),"空调模拟能耗.csv","text/csv;charset=UTF-8");}
    @GetMapping("/fixtures/{key}") public ResponseEntity<byte[]> fixture(@PathVariable String key)throws Exception{Map<String,String> files=new HashMap<>();files.put("a","F01_A_2026-10_模拟盘点.csv");files.put("b","F02_B_2026-10_模拟盘点.csv");files.put("c","F03_C_2026-10_模拟盘点.csv");files.put("bad","F03_C_缺少运行状态_错误样例.csv");files.put("template","设备导入模板.xlsx");files.put("csv","设备导入空模板.csv");require(files.containsKey(key),"演示样例不存在");String name=files.get(key);return download(StreamUtils.copyToByteArray(new ClassPathResource("demo/files/"+name).getInputStream()),name,"application/octet-stream");}
    private ResponseEntity<byte[]> download(byte[] bytes,String filename,String type){return ResponseEntity.ok().contentType(MediaType.parseMediaType(type)).header(HttpHeaders.CONTENT_DISPOSITION,ContentDisposition.attachment().filename(filename,StandardCharsets.UTF_8).build().toString()).body(bytes);}
    private String csvRow(Object... values){List<String> cells=new ArrayList<>();for(Object value:values){String s=value==null?"":String.valueOf(value);if(s.startsWith("=")||s.startsWith("+")||s.startsWith("@")||s.startsWith("-"))s="'"+s;cells.add("\""+s.replace("\"","\"\"")+"\"");}return String.join(",",cells)+"\r\n";}
}
