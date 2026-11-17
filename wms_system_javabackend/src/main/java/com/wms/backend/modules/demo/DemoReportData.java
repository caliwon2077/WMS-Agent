package com.wms.backend.modules.demo;

import static com.wms.backend.modules.demo.DemoJson.*;
import com.wms.backend.modules.demo.DemoAuth.Actor;
import java.math.*;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class DemoReportData {
    private final DemoRepository repo;private final DemoAuth auth;
    public DemoReportData(DemoRepository repo,DemoAuth auth){this.repo=repo;this.auth=auth;}
    public Map<String,Object> latestSnapshot(Actor a,String factory,String period){auth.scope(a,factory);List<Map<String,Object>> rr=repo.all("snapshot").stream().filter(r->factory.equals(str(r,"factory"))&&period.equals(str(r,"period"))&&str(r,"state").equals("confirmed")).collect(Collectors.toList());return rr.isEmpty()?null:rr.get(rr.size()-1);}
    public Map<String,Object> source(Actor a,String id){Map<String,Object> r=repo.get(id);auth.scope(a,str(r,"factory"));require(Arrays.asList("snapshot","import","event","asset","plan","energy").contains(str(r,"kind")),"不支持此来源类型");return r;}
    public Map<String,Object> frozenPlanMetrics(Actor a,String factory,String period){
        auth.scope(a,factory);LocalDate end=month(period).atEndOfMonth(),start=month(period).atDay(1);int due=0,done=0,onTime=0,overdue=0;Map<String,Object> types=map("零星维修",0,"大修",0,"巡检",0);
        for(Map<String,Object> p:repo.all("plan")){
            if(!factory.equals(str(p,"factory")))continue;String created=str(p,"effective_created");if(!created.isEmpty()&&date(created).isAfter(end))continue;
            String state="待执行";List<Map<String,Object>> history=rows(p.get("history"));for(Map<String,Object> event:history){String at=str(event,"at");if(at.length()>=10&&!date(at.substring(0,10)).isAfter(end))state=str(event,"state");}
            if(history.isEmpty())state=str(p,"state");LocalDate deadline=date(str(p,"deadline"));if(state.equals("已取消"))continue;
            if(!deadline.isAfter(end)&&!state.equals("已完成"))overdue++;
            if(deadline.isBefore(start)||deadline.isAfter(end))continue;due++;String type=str(p,"type");types.put(type,num(types,type,0)+1);
            if(state.equals("已完成")){done++;String completed=str(p,"completed_at");if(!completed.isEmpty()&&!date(completed).isAfter(deadline))onTime++;}
        }
        return map("due",due,"completed",done,"on_time",onTime,"overdue",overdue,"completion_rate",due==0?null:BigDecimal.valueOf(done*100.0/due).setScale(2,RoundingMode.HALF_UP),"by_type",types,"period",period,"cutoff",end.toString(),"source_ref","plans:as-of:"+factory+":"+period);
    }
    public Map<String,Object> collect(Actor a,String factory,String period,String template,String snapshotId,Map<String,Object> planMetrics){
        auth.scope(a,factory);require(Arrays.asList("V1","V2").contains(template),"模板版本无效");
        if(snapshotId.isEmpty())return map("type","factory_report","factory",factory,"period",period,"template",template,"status","needs_input","missing",Arrays.asList("缺少 "+period+" 已确认月度盘点，请上传并确认"),"source_ref","missing:"+factory,"synthetic",true);
        Map<String,Object> snapshot=source(a,snapshotId);require(factory.equals(str(snapshot,"factory"))&&period.equals(str(snapshot,"period"))&&str(snapshot,"kind").equals("snapshot"),"来源范围不匹配");
        List<Map<String,Object>> rr=rows(snapshot.get("rows"));int total=rr.size(),process=0,other=0,unclassified=0,stopped=0;for(Map<String,Object> r:rr){String c=str(r,"classification");if(c.equals("工艺设备"))process++;else if(c.equals("非工艺设备"))other++;else unclassified++;if(str(r,"equipment_state").equals("已停机"))stopped++;}
        Map<String,Object> metrics=map("total",total,"process",process,"nonprocess",other,"unclassified",unclassified,"process_share",total==0?null:BigDecimal.valueOf(process*100.0/total).setScale(2,RoundingMode.HALF_UP));if(template.equals("V2"))metrics.put("stopped",stopped);
        Map<String,Object> prev=latestSnapshot(a,factory,month(period).minusMonths(1).toString());List<Map<String,Object>> differences=new ArrayList<>();if(prev!=null){Map<String,Map<String,Object>> old=new HashMap<>(),current=new HashMap<>();for(Map<String,Object> r:rows(prev.get("rows")))old.put(str(r,"id"),r);for(Map<String,Object> r:rr)current.put(str(r,"id"),r);
            for(String id:old.keySet())if(!current.containsKey(id))differences.add(map("asset_id",id,"kind","疑似缺失","confirmed",false));
            for(String id:current.keySet()){if(!old.containsKey(id))differences.add(map("asset_id",id,"kind","疑似新增","confirmed",false));else if(!str(old.get(id),"equipment_state").equals(str(current.get(id),"equipment_state")))differences.add(map("asset_id",id,"kind","状态差异","before",old.get(id).get("equipment_state"),"after",current.get(id).get("equipment_state"),"confirmed",false));}
        }
        List<Map<String,Object>> events=repo.all("event").stream().filter(e->factory.equals(str(e,"factory"))&&str(e,"action").equals("equipment_state_changed")&&str(obj(e.get("after")),"effective_date").startsWith(period)).collect(Collectors.toList());
        return map("type","factory_report","factory",factory,"period",period,"cutoff",snapshot.get("cutoff"),"template",template,"status","success","metrics",metrics,"plans",planMetrics,"differences",differences,"confirmed_events",events,"source_ref",snapshotId,"data_version",snapshot.get("version"),"mapping_version",snapshot.get("mapping_version"),"synthetic",true);
    }
    public Map<String,Object> aggregate(List<Map<String,Object>> reports,List<String> requested,String period,String template){
        int total=0,process=0,other=0,pending=0,stopped=0,due=0,done=0,overdue=0;Set<String> accepted=new HashSet<>();
        for(Map<String,Object> r:reports)if(str(r,"status").equals("success")&&requested.contains(str(r,"factory"))&&accepted.add(str(r,"factory"))){Map<String,Object> m=obj(r.get("metrics"));total+=num(m,"total",0);process+=num(m,"process",0);other+=num(m,"nonprocess",0);pending+=num(m,"unclassified",0);stopped+=num(m,"stopped",0);Map<String,Object> p=obj(r.get("plans"));due+=num(p,"due",0);done+=num(p,"completed",0);overdue+=num(p,"overdue",0);}
        List<String> missing=new ArrayList<>(requested);missing.removeAll(accepted);Map<String,Object> metrics=map("total",total,"process",process,"nonprocess",other,"unclassified",pending,"process_share",total==0?null:BigDecimal.valueOf(process*100.0/total).setScale(2,RoundingMode.HALF_UP),"due",due,"completed",done,"overdue",overdue,"completion_rate",due==0?null:BigDecimal.valueOf(done*100.0/due).setScale(2,RoundingMode.HALF_UP));if(template.equals("V2"))metrics.put("stopped",stopped);
        return map("type","monthly_report","title",period+" 资产与检维修月报","period",period,"cutoff",month(period).atEndOfMonth().toString(),"template",template,"requested_factories",requested,"covered_factories",new ArrayList<>(accepted),"coverage",accepted.size()+"/"+requested.size(),"organization_coverage",requested.size()+"/12","missing_factories",missing,"metrics",metrics,"factories",reports,"report_state","draft","synthetic",true);
    }
    public Map<String,Object> energy(Actor a,Map<String,Object> p){
        String factory=required(p,"factory_id",3);auth.scope(a,factory);require(!factory.equals("ALL"),"能耗分析需要明确一个工厂");String start=required(p,"start",10),end=required(p,"end",10),cs=str(p,"compare_start"),ce=str(p,"compare_end");require(!date(start).isAfter(date(end)),"统计起止日期无效");
        if(!cs.isEmpty()||!ce.isEmpty()){require(!cs.isEmpty()&&!ce.isEmpty(),"对比期间需要完整起止日期");require(java.time.temporal.ChronoUnit.DAYS.between(date(cs),date(ce))==java.time.temporal.ChronoUnit.DAYS.between(date(start),date(end)),"仅支持相同长度期间对比");}
        List<Map<String,Object>> meters=repo.all("energy").stream().filter(r->factory.equals(str(r,"factory"))&&(str(p,"asset_id").isEmpty()||str(p,"asset_id").equals(str(r,"asset_id")))).collect(Collectors.toList());Double current=null,previous=null;String asset="",source="";
        for(Map<String,Object> meter:meters){asset=str(meter,"asset_name");source=str(meter,"id");for(Map<String,Object> period:rows(meter.get("periods"))){if(start.equals(str(period,"start"))&&end.equals(str(period,"end")))current=Double.valueOf(str(period,"value"));if(cs.equals(str(period,"start"))&&ce.equals(str(period,"end")))previous=Double.valueOf(str(period,"value"));}}
        Object change=current==null||previous==null||previous==0?null:BigDecimal.valueOf((current-previous)*100.0/previous).setScale(2,RoundingMode.HALF_UP);
        return map("type","energy","title","空调模拟能耗对比","factory_id",factory,"asset_name",asset,"start",start,"end",end,"compare_start",cs,"compare_end",ce,"current",current,"previous",previous,"change_percent",change,"unit","kWh","source_ref",source,"synthetic",true,"notice",current==null?"当前期间没有模拟计量数据；缺值不记作 0":previous==null?"缺少同口径对比期间，无法计算变化率":previous==0?"基期为 0，变化率不适用":"同一模拟计量点、相同长度期间；不推断用电变化原因");
    }
}
