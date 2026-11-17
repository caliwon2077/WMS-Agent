package com.wms.backend.modules.demo;

import static com.wms.backend.modules.demo.DemoJson.*;
import java.util.*;
import javax.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class DemoSeeder {
    private final DemoRepository repo;
    public DemoSeeder(DemoRepository repo){this.repo=repo;}
    @PostConstruct public void initialize()throws Exception {
        if(repo.find("SEED-V0.1.0")!=null)return;
        Map<String,Object> seed=JSON.readValue(new ClassPathResource("demo/seed-v0.1.0.json").getInputStream(),Map.class);
        repo.tx(()->{
            for(Map<String,Object> f:rows(seed.get("factories")))repo.add("factory",str(f,"id"),str(f,"id"),"admin","active",f);
            for(Map<String,Object> a:rows(seed.get("assets")))repo.add("asset",str(a,"id"),str(a,"factory"),"admin","active",a);
            for(int n=1;n<=12;n++){
                String factory=String.format("F%02d",n);List<Map<String,Object>> assets=new ArrayList<>();
                for(Map<String,Object> a:repo.all("asset"))if(factory.equals(str(a,"factory")))assets.add(a);
                String ac=str(assets.get(24),"id");
                for(int j=0;j<6;j++){
                    String task=String.format("WX-%02d-%03d",n,j+1);String state=Arrays.asList("待执行","进行中","受阻","已完成","已取消","待执行").get(j);
                    Map<String,Object> a=assets.get(j==0?24:j);
                    repo.add("plan",task,factory,"admin",state,map("asset_id",a.get("id"),"asset_name",a.get("name"),"type",Arrays.asList("零星维修","大修","巡检").get(j%3),"title",Arrays.asList("空调滤网清洗","主设备年度大修","生产线运行巡检","传动机构检修","停用设备复核","包装线点检").get(j),"assignee",n==2?"tech02":String.format("factory%02d",n),"deadline",j<4?"2026-10-25":"2026-11-20","note",j==2?"等待备件到场":"演示计划","result",j==3?"完成检修并试运行正常":"","completed_at",j==3?"2026-10-24":"","cancel_reason",j==4?"演示：生产安排调整":"","effective_created","2026-10-01","history",Arrays.asList(map("at","2026-10-01T08:00:00+08:00","state","待执行"),map("at",j==3?"2026-10-24T16:00:00+08:00":"2026-10-12T08:00:00+08:00","state",state)),"synthetic",true));
                }
                for(String period:Arrays.asList("2026-09","2026-10")){
                    if(period.equals("2026-10")&&n==3)continue;
                    List<Map<String,Object>> snapshot=new ArrayList<>();
                    for(Map<String,Object> a:assets)snapshot.add(new LinkedHashMap<>(a));
                    if(period.equals("2026-09"))snapshot.get(5).put("equipment_state","在用");
                    repo.add("snapshot","SN-"+factory+"-"+period,factory,"admin","confirmed",map("period",period,"cutoff",month(period).atEndOfMonth().toString(),"rows",snapshot,"mapping_version","A-1","source_ref","SEED-V0.1.0","synthetic",true));
                }
                repo.event(factory,"admin","equipment_state_changed",str(assets.get(5),"id"),map("equipment_state","在用"),map("equipment_state",str(assets.get(5),"equipment_state"),"effective_date","2026-10-10","synthetic",true));
                repo.add("energy","EN-"+factory,factory,"admin","active",map("asset_id",ac,"asset_name",assets.get(24).get("name"),"meter","模拟专用计量点","unit","kWh","periods",Arrays.asList(map("start","2026-10-01","end","2026-10-16","value",100),map("start","2026-11-01","end","2026-11-16","value",120)),"synthetic",true));
            }
            repo.add("meta","SEED-V0.1.0","ALL","admin","ready",map("synthetic",true,"version_label","V0.1.0"));return null;
        });
    }
}
