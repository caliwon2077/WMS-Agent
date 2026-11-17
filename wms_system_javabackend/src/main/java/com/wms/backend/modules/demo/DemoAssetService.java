package com.wms.backend.modules.demo;

import static com.wms.backend.modules.demo.DemoJson.*;
import com.wms.backend.modules.demo.DemoAuth.Actor;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class DemoAssetService {
    public static final List<String> KEYS=Arrays.asList("code","name","support","priority","line","department","enabled_date","value","condition","manufactured_month","equipment_state","category","classification","source");
    public static final List<String> HEADERS=Arrays.asList("设备编码","资产名称","支持状态","运行设备主次","生产线","使用部门","启用日期","原值（元）","完好状态","出厂年月","设备状态","资产类别","设备分类","资产来源");
    public static final List<String> ALIASES=Arrays.asList("设备编号","设备名称","支持情况","主次","产线","部门","投用日期","原值","完好情况","制造月份","运行状态","设备类别","用途分类","来源");
    private final DemoRepository repo;private final DemoAuth auth;
    public DemoAssetService(DemoRepository repo,DemoAuth auth){this.repo=repo;this.auth=auth;}
    public List<Map<String,Object>> factories(Actor a){return repo.all("factory").stream().filter(f->a.hq()||a.factory.equals(str(f,"id"))).collect(Collectors.toList());}
    public List<Map<String,Object>> records(Actor a,String factory){auth.scope(a,factory);return repo.all("asset").stream().filter(r->!"void".equals(str(r,"state"))&&(factory.equals("ALL")||factory.equals(str(r,"factory")))).collect(Collectors.toList());}
    public Map<String,Object> get(Actor a,String id){Map<String,Object> r=repo.get(id);require("asset".equals(str(r,"kind")),"请选择设备记录");auth.scope(a,str(r,"factory"));return r;}
    public Map<String,Object> search(Actor a,Map<String,Object> p){
        String factory=str(p,"factory_id");if(factory.isEmpty())factory=a.factory;
        String q=str(p,"query").toLowerCase(Locale.ROOT);List<Map<String,Object>> rr=records(a,factory).stream().filter(r->(str(r,"code")+str(r,"name")).toLowerCase(Locale.ROOT).contains(q)).filter(r->Arrays.asList("line","classification","importance","equipment_state").stream().allMatch(k->str(p,k).isEmpty()||str(p,k).equals(str(r,k)))).sorted(Comparator.comparing(r->str(r,"factory")+str(r,"code")+str(r,"id"))).collect(Collectors.toList());
        int page=Math.max(1,num(p,"page",1)),size=Math.max(1,Math.min(100,num(p,"page_size",50)));int from=Math.min(rr.size(),(page-1)*size),to=Math.min(rr.size(),from+size);
        List<Map<String,Object>> process=rr.stream().filter(r->str(r,"classification").equals("工艺设备")).collect(Collectors.toList());int pf=Math.min(process.size(),(page-1)*size);
        return map("type","assets","title","资产查询","factory_id",factory,"records",rr.subList(from,to),"process_records",process.subList(pf,Math.min(process.size(),pf+size)),"total",rr.size(),"process_count",process.size(),"nonprocess_count",rr.stream().filter(r->str(r,"classification").equals("非工艺设备")).count(),"unclassified_count",rr.stream().filter(r->str(r,"classification").equals("待分类")).count(),"page",page,"page_size",size,"has_more",to<rr.size(),"source_ref","assets:current","cutoff",now(),"synthetic",true);
    }
    private void validate(Map<String,Object> r,String factory){
        required(r,"code",64);required(r,"name",100);
        for(String k:KEYS){String v=str(r,k);require(v.length()<=100,"字段 "+k+" 过长");require(v.isEmpty()||!"=+@".contains(v.substring(0,1)),"不接受公式前缀");if(!k.equals("value"))require(!v.startsWith("-"),"不接受公式前缀");}
        for(String[] e:new String[][]{{"priority","主","次","待确认"},{"condition","完好","异常","待确认"},{"equipment_state","在用","已停机","待确认"},{"classification","工艺设备","非工艺设备","待分类"}}){String v=str(r,e[0]);if(v.isEmpty()){v=e[e.length-1];r.put(e[0],v);}require(Arrays.asList(e).subList(1,e.length).contains(v),"字段 "+e[0]+" 的值不在允许范围");}
        String line=str(r,"line");require(line.isEmpty()||strings(repo.get(factory).get("lines")).contains(line),"生产线不属于所选工厂");
        String date=str(r,"enabled_date");if(!date.isEmpty()){if(date.matches("\\d+(\\.0+)?")){int days=new BigDecimal(date).intValueExact();require(days>0&&days<100000,"Excel 日期超出范围");date=LocalDate.of(1899,12,30).plusDays(days).toString();r.put("enabled_date",date);}date(date);}
        if(!str(r,"manufactured_month").isEmpty())month(str(r,"manufactured_month"));
        String value=str(r,"value");require(value.isEmpty()||value.matches("\\d+(\\.\\d{1,2})?"),"原值须为非负金额，最多两位小数");if(!value.isEmpty())require(new BigDecimal(value).compareTo(new BigDecimal("999999999999.99"))<=0,"原值超出范围");
    }
    public Map<String,Object> preview(Actor a,String factory,String purpose,String period,String filename,byte[] bytes){
        auth.stock(a,factory);require(!factory.equals("ALL"),"上传前请选择一个工厂");require(Arrays.asList("assets","snapshot").contains(purpose),"请选择导入用途");if(purpose.equals("snapshot"))month(period);
        List<List<String>> table=DemoFileReader.read(filename,bytes);List<String> header=table.get(0);List<String> mapping=new ArrayList<>();List<Map<String,Object>> errors=new ArrayList<>(),warnings=new ArrayList<>(),normalized=new ArrayList<>();Set<String> seen=new HashSet<>();
        String format="unknown";if(header.equals(HEADERS))format="A";else if(header.equals(KEYS))format="B";else if(header.size()==14&&new HashSet<>(header).equals(new HashSet<>(ALIASES)))format="C";
        for(String h:header){int index=HEADERS.indexOf(h);if(index<0)index=KEYS.indexOf(h);if(index<0)index=ALIASES.indexOf(h);mapping.add(index<0?"":KEYS.get(index));}
        if(format.equals("unknown"))errors.add(map("row",1,"column","表头","message","表头不属于已支持的 A/B/C 格式，请检查缺列、重复列、额外列和顺序"));
        Map<String,Map<String,Object>> existing=new HashMap<>();for(Map<String,Object> r:records(a,factory))existing.put(str(r,"code"),r);
        if(errors.isEmpty())for(int i=1;i<table.size();i++){
            List<String> values=table.get(i);if(values.stream().allMatch(String::isEmpty))continue;Map<String,Object> r=new LinkedHashMap<>();
            try{
                require(values.size()<=14,"该行列数多于表头");for(int k=0;k<14;k++)r.put(mapping.get(k),k<values.size()?values.get(k):"");
                if(str(r,"equipment_state").equals("在使")){r.put("equipment_state","在用");warnings.add(map("row",i+1,"message","“在使”按已批准词典映射为“在用”"));}
                validate(r,factory);String code=str(r,"code");require(seen.add(code),"文件内设备编码重复");Map<String,Object> asset=existing.get(code);
                if(purpose.equals("assets"))require(asset==null,"同厂已有有效设备编码，请使用月度盘点用途");
                else{require(asset!=null,"设备编码未建档，请先核对设备身份");r.put("asset_id",asset.get("id"));r.put("asset_version",asset.get("version"));}
                if(str(r,"classification").equals("工艺设备")&&str(r,"line").isEmpty())warnings.add(map("row",i+1,"message","工艺设备缺少生产线"));
                r.put("source_row",i+1);normalized.add(r);
            }catch(Exception ex){errors.add(map("row",i+1,"column","数据","message",ex.getMessage()));}
        }
        String id=id("IM");long rowCount=table.subList(1,table.size()).stream().filter(r->!r.stream().allMatch(String::isEmpty)).count();Map<String,Object> payload=map("filename",filename,"purpose",purpose,"period",period,"format",format,"mapping_version",format+"-1","headers",header,"mapping",mapping,"rows",normalized,"row_count",rowCount,"errors",errors,"warnings",warnings,"file_hash",DemoAuth.hash(Base64.getEncoder().encodeToString(bytes)),"synthetic",true);
        return repo.tx(()->{Map<String,Object> batch=repo.add("import",id,factory,a.id,errors.isEmpty()?"preview":"invalid",payload);repo.jdbc().update("INSERT INTO demo_uploads VALUES(?,?,?)",id,filename,bytes);return batch;});
    }
    public Map<String,Object> batch(Actor a,String id){Map<String,Object> r=repo.get(id);require("import".equals(str(r,"kind")),"不是导入批次");auth.scope(a,str(r,"factory"));return r;}
    public Map<String,Object> commit(Actor a,String id){return repo.tx(()->{
        Map<String,Object> b=batch(a,id);auth.stock(a,str(b,"factory"));require(a.id.equals(str(b,"owner")),"须由上传人确认批次");if(str(b,"state").equals("confirmed"))return b;require(str(b,"state").equals("preview"),"存在错误的批次不能提交");
        String f=str(b,"factory"),purpose=str(b,"purpose");Map<String,Map<String,Object>> existing=new HashMap<>();for(Map<String,Object> r:records(a,f))existing.put(str(r,"code"),r);
        List<Map<String,Object>> output=new ArrayList<>();
        for(Map<String,Object> r:rows(b.get("rows"))){validate(r,f);Map<String,Object> prior=existing.get(str(r,"code"));if(purpose.equals("assets")){require(prior==null,"预检后出现重复编码，请重新预检");Map<String,Object> p=new LinkedHashMap<>(r);p.put("batch_id",id);p.put("importance","待评定");p.put("synthetic",true);output.add(repo.add("asset",id("AS"),f,a.id,"active",p));}
            else{require(prior!=null&&num(prior,"version",-1)==num(r,"asset_version",-2),"档案已变化，请重新上传预检");Map<String,Object> p=new LinkedHashMap<>(r);p.put("id",prior.get("id"));p.put("factory",f);p.put("synthetic",true);output.add(p);}}
        if(purpose.equals("snapshot")){String snapshotId=id("SN");repo.add("snapshot",snapshotId,f,a.id,"confirmed",map("period",b.get("period"),"cutoff",month(str(b,"period")).atEndOfMonth().toString(),"rows",output,"mapping_version",b.get("mapping_version"),"source_ref",id,"synthetic",true));b.put("snapshot_id",snapshotId);}
        b.put("state","confirmed");b.put("committed_count",output.size());b=repo.save(b);repo.event(f,a.id,"confirm_import",id,null,map("purpose",purpose,"count",output.size()));return b;
    });}
    public Map<String,Object> voidBatch(Actor a,String id){return repo.tx(()->{
        auth.hq(a);Map<String,Object> b=batch(a,id);require(str(b,"state").equals("confirmed")&&str(b,"purpose").equals("assets"),"仅能撤销已确认的新增档案批次");List<Map<String,Object>> targets=records(a,str(b,"factory")).stream().filter(r->id.equals(str(r,"batch_id"))).collect(Collectors.toList());Set<String> ids=targets.stream().map(r->str(r,"id")).collect(Collectors.toSet());
        for(Map<String,Object> r:targets)require(num(r,"version",0)==1,"批次设备已有后续修改，不能整批撤销");
        for(String kind:Arrays.asList("plan","approval","snapshot"))for(Map<String,Object> r:repo.all(kind)){if(kind.equals("snapshot")){for(Map<String,Object> x:rows(r.get("rows")))require(!ids.contains(str(x,"id")),"批次设备已有月报快照引用");}else require(!ids.contains(str(r,"asset_id")),"批次设备已有业务引用");}
        for(Map<String,Object> r:targets){r.put("state","void");repo.save(r);}b.put("state","void");repo.save(b);repo.event(str(b,"factory"),a.id,"void_import",id,null,map("count",targets.size()));return map("voided",targets.size());
    });}
    public Map<String,Object> classify(Actor a,Map<String,Object> body){return repo.tx(()->{
        String reason=required(body,"reason",500),classification=required(body,"classification",20),importance=str(body,"importance");require(Arrays.asList("工艺设备","非工艺设备","待分类").contains(classification),"设备用途分类无效");require(importance.isEmpty()||Arrays.asList("关键","重要","一般","待评定").contains(importance),"重要度无效");List<Map<String,Object>> rr=rows(body.get("assets"));require(!rr.isEmpty()&&rr.size()<=1000,"请选择 1 至 1000 条设备");Set<String> seen=new HashSet<>();
        for(Map<String,Object> requested:rr){Map<String,Object> r=get(a,required(requested,"id",100));require(seen.add(str(r,"id")),"重复设备");auth.stock(a,str(r,"factory"));require(!str(r,"state").equals("void"),"设备已作废");require(num(r,"version",0)==num(requested,"version",-1),"设备版本已变更，请刷新");Map<String,Object> before=new LinkedHashMap<>(r);r.put("classification",classification);if(!importance.isEmpty())r.put("importance",importance);repo.save(r);repo.event(str(r,"factory"),a.id,"classify",str(r,"id"),before,map("classification",classification,"importance",importance,"reason",reason));}return map("updated",rr.size());
    });}
    public Map<String,Object> history(Actor a,String id){Map<String,Object> r=get(a,id);List<Map<String,Object>> events=repo.all("event").stream().filter(e->id.equals(str(e,"target"))).collect(Collectors.toList());List<Map<String,Object>> snapshots=new ArrayList<>();for(Map<String,Object> s:repo.all("snapshot"))if(str(s,"factory").equals(str(r,"factory")))for(Map<String,Object> item:rows(s.get("rows")))if(id.equals(str(item,"id")))snapshots.add(map("source_ref",s.get("id"),"period",s.get("period"),"equipment_state",item.get("equipment_state"),"classification",item.get("classification")));return map("asset",r,"events",events,"snapshots",snapshots);}
    public Map<String,Object> approval(Actor a,Map<String,Object> p){Map<String,Object> asset=get(a,required(p,"asset_id",100));require(!str(asset,"state").equals("void"),"设备已作废");String requestKey=required(p,"request_key",100),id="AP-"+DemoAuth.hash(a.id+requestKey).replaceAll("[^a-zA-Z0-9]","");return repo.tx(()->{Map<String,Object> found=repo.find(id);if(found!=null)return found;Map<String,Object> result=repo.add("approval",id,str(asset,"factory"),a.id,"待处理",map("asset_id",asset.get("id"),"asset_name",asset.get("name"),"description",required(p,"description",500),"applicant",a.name,"synthetic",true));repo.event(str(asset,"factory"),a.id,"submit_approval_example",id,null,map("state","待处理"));return result;});}
    public List<Map<String,Object>> approvals(Actor a){return repo.all("approval").stream().filter(r->a.hq()||a.id.equals(str(r,"owner"))).collect(Collectors.toList());}
}
