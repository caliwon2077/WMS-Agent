package com.wms.backend.modules.demo;

import static com.wms.backend.modules.demo.DemoJson.*;
import com.wms.backend.modules.demo.DemoAuth.Actor;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class DemoPlanService {
    private final DemoRepository repo;private final DemoAuth auth;private final DemoAssetService assets;
    public DemoPlanService(DemoRepository r,DemoAuth a,DemoAssetService assets){repo=r;auth=a;this.assets=assets;}
    public Map<String,Object> get(Actor a,String id){Map<String,Object> r=repo.get(id);require("plan".equals(str(r,"kind")),"请选择作业计划");auth.scope(a,str(r,"factory"));return r;}
    public Map<String,Object> search(Actor a,Map<String,Object> p){String f=str(p,"factory_id");if(f.isEmpty())f=a.factory;auth.scope(a,f);final String factory=f;String status=str(p,"status"),q=str(p,"query");if(!str(p,"start").isEmpty())date(str(p,"start"));if(!str(p,"end").isEmpty())date(str(p,"end"));List<Map<String,Object>> rr=repo.all("plan").stream().filter(r->factory.equals("ALL")||factory.equals(str(r,"factory"))).filter(r->(str(r,"id")+str(r,"title")+str(r,"asset_name")).contains(q)).filter(r->str(p,"type").isEmpty()||str(p,"type").equals(str(r,"type"))).filter(r->str(p,"start").isEmpty()||str(r,"deadline").compareTo(str(p,"start"))>=0).filter(r->str(p,"end").isEmpty()||str(r,"deadline").compareTo(str(p,"end"))<=0).filter(r->{if(status.equals("未完成"))return !Arrays.asList("已完成","已取消").contains(str(r,"state"));if(status.equals("逾期"))return overdue(r);return status.isEmpty()||status.equals(str(r,"state"));}).collect(Collectors.toList());
        int page=Math.max(1,num(p,"page",1)),from=Math.min(rr.size(),(page-1)*50),to=Math.min(rr.size(),from+50);return map("type","plans","title","作业计划","factory_id",factory,"records",rr.subList(from,to),"total",rr.size(),"uncompleted_count",rr.stream().filter(r->!Arrays.asList("已完成","已取消").contains(str(r,"state"))).count(),"page",page,"has_more",to<rr.size(),"cutoff",now(),"source_ref","plans:current","synthetic",true);}
    private boolean overdue(Map<String,Object> r){return !Arrays.asList("已完成","已取消").contains(str(r,"state"))&&date(str(r,"deadline")).isBefore(LocalDate.now(ZONE));}
    private void writable(Actor a,Map<String,Object> plan){auth.scope(a,str(plan,"factory"));if("TECH".equals(a.role)&&!a.id.equals(str(plan,"assignee")))throw bad("FORBIDDEN","只能更新分配给自己的计划");}
    public Map<String,Object> propose(Actor a,Map<String,Object> p){
        String action=required(p,"action",20);require(Arrays.asList("create","update").contains(action),"仅支持新建或更新计划草稿");Map<String,Object> before=null,after=new LinkedHashMap<>();String factory;
        if(action.equals("create")){
            require(!a.role.equals("TECH"),"维修账号只能更新已分配的计划");Map<String,Object> asset=assets.get(a,required(p,"asset_id",100));require(!str(asset,"state").equals("void"),"设备已作废");factory=str(asset,"factory");auth.stock(a,factory);after.putAll(map("asset_id",asset.get("id"),"asset_name",asset.get("name"),"type",required(p,"type",20),"title",required(p,"title",100),"deadline",required(p,"deadline",10),"assignee",required(p,"assignee",40),"state","待执行","note",str(p,"note"),"result","","completed_at","","synthetic",true,"effective_created",LocalDate.now(ZONE).toString()));
        }else{
            before=get(a,required(p,"task_id",100));writable(a,before);factory=str(before,"factory");after.putAll(before);
            for(String key:Arrays.asList("state","note","result","completed_at","deadline","assignee","cancel_reason"))if(p.containsKey(key))after.put(key,p.get(key));
            if(!str(after,"deadline").equals(str(before,"deadline"))||!str(after,"assignee").equals(str(before,"assignee"))||str(after,"state").equals("已取消"))auth.hq(a);
            String old=str(before,"state"),next=str(after,"state");Map<String,List<String>> allowed=new HashMap<>();allowed.put("待执行",Arrays.asList("待执行","进行中","受阻","已取消"));allowed.put("进行中",Arrays.asList("进行中","受阻","已完成","已取消"));allowed.put("受阻",Arrays.asList("受阻","进行中","已取消"));
            require(allowed.containsKey(old)&&allowed.get(old).contains(next),"不允许此状态转换；已完成和已取消任务不直接重开");
        }
        require(Arrays.asList("零星维修","大修","巡检").contains(str(after,"type")),"作业类型无效");date(required(after,"deadline",10));require(str(after,"note").length()<=1000&&str(after,"result").length()<=1000,"说明过长");
        String assignee=str(after,"assignee");require(repo.jdbc().queryForObject("SELECT COUNT(*) FROM demo_users WHERE id=? AND active=1 AND (factory=? OR role='HQ')",Integer.class,assignee,factory)>0,"责任人须属于该工厂");
        if(str(after,"state").equals("已完成")){required(after,"result",1000);LocalDate done=date(required(after,"completed_at",10));require(!done.isAfter(LocalDate.now(ZONE)),"完成日期不能是未来");if(str(after,"type").equals("巡检"))require(str(after,"result").startsWith("正常")||str(after,"result").startsWith("异常")||str(after,"result").startsWith("待确认"),"巡检结果须明确正常、异常或待确认");}
        if(str(after,"state").equals("已取消"))required(after,"cancel_reason",500);
        Map<String,Object> card=repo.add("confirmation",id("CF"),factory,a.id,"pending",map("action",action,"before",before,"after",after,"actor_revision",a.revision,"expires_at",OffsetDateTime.now(ZONE).plusMinutes(5).toString(),"source_run",str(p,"source_run"),"synthetic",true));
        return map("type","confirmation","title","请确认业务变更","confirmation",card,"source_ref",card.get("id"),"synthetic",true);
    }
    public Map<String,Object> confirm(Actor a,String id,boolean cancel){return repo.tx(()->{
        Map<String,Object> card=repo.get(id);require("confirmation".equals(str(card,"kind")),"不是业务确认卡");auth.scope(a,str(card,"factory"));require(a.id.equals(str(card,"owner"))&&a.revision==num(card,"actor_revision",-1),"确认人或授权已变化");
        if(str(card,"state").equals("committed"))return obj(card.get("saved_result"));require(str(card,"state").equals("pending"),"确认卡已失效");require(OffsetDateTime.parse(str(card,"expires_at")).isAfter(OffsetDateTime.now(ZONE)),"确认卡已过期，请重新生成");
        if(cancel){card.put("state","cancelled");repo.save(card);finishRun(card,"cancelled",null);return map("cancelled",true);}
        Map<String,Object> next=obj(card.get("after")),before=obj(card.get("before")),saved;
        if(str(card,"action").equals("create")){auth.stock(a,str(card,"factory"));Map<String,Object> asset=assets.get(a,str(next,"asset_id"));require(!str(asset,"state").equals("void"),"设备已作废，请重新确认");next.put("history",Arrays.asList(map("at",now(),"state","待执行")));saved=repo.add("plan",id("WX"),str(card,"factory"),a.id,"待执行",next);}
        else{Map<String,Object> current=get(a,str(before,"id"));writable(a,current);require(num(current,"version",0)==num(before,"version",-1),"计划已变化，请重新生成确认卡");if(!str(current,"deadline").equals(str(next,"deadline"))||!str(current,"assignee").equals(str(next,"assignee"))||str(next,"state").equals("已取消"))auth.hq(a);List<Map<String,Object>> history=new ArrayList<>(rows(current.get("history")));history.add(map("at",now(),"state",next.get("state"),"result",next.get("result")));next.put("history",history);saved=repo.save(next);}
        repo.event(str(card,"factory"),a.id,"confirm_plan_change",str(saved,"id"),before,saved);card.put("state","committed");card.put("saved_result",saved);repo.save(card);finishRun(card,"success",saved);return repo.get(str(saved,"id"));
    });}
    private void finishRun(Map<String,Object> card,String state,Map<String,Object> result){
        String id=str(card,"source_run");if(id.isEmpty())return;Map<String,Object> run=repo.find(id);
        if(run==null||!str(run,"state").equals("confirmation_pending"))return;
        run.put("state",state);run.put("business_result",result);run.put("finished_at",now());repo.save(run);
        repo.trace(id,"人员确认","confirm_plan_change",state,map("confirmation_id",card.get("id"),"target",result==null?null:result.get("id")));
    }
}
