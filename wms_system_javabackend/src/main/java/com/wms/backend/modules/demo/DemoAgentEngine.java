package com.wms.backend.modules.demo;

import static com.wms.backend.modules.demo.DemoJson.*;
import com.wms.backend.modules.demo.DemoAuth.Actor;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;
import org.springframework.stereotype.Service;

@Service
public class DemoAgentEngine {
    private final DeepSeekClient client;private final DemoRepository repo;private final DemoAuth auth;
    public DemoAgentEngine(DeepSeekClient client,DemoRepository repo,DemoAuth auth){this.client=client;this.repo=repo;this.auth=auth;}
    public static Map<String,Object> prop(String type,String description){return map("type",type,"description",description);}
    public static Map<String,Object> tool(String name,String description,Map<String,Object> properties,String... required){return map("type","function","function",map("name",name,"description",description,"parameters",map("type","object","properties",properties,"required",Arrays.asList(required),"additionalProperties",false)));}
    public static Map<String,Object> stringArray(String description){return map("type","array","description",description,"items",map("type","string"));}
    public static Map<String,Object> choice(String description,String... values){return map("type","string","description",description,"enum",Arrays.asList(values));}
    public static Map<String,Object> present(){return tool("present_results","选择已经取得的 evidence_id 交付结果；卡片和所有数字由服务端生成。不要重复调用查询。",map("evidence_ids",stringArray("本轮工具结果中的 evidence_id，例如 E1")),"evidence_ids");}
    public static Map<String,Object> clarify(){return tool("ask_clarification","缺少关键工厂、期间、设备或动作时集中追问。只问必要问题，不假定未知业务事实。",map("question",prop("string","给用户的简短中文问题")),"question");}
    private void active(Actor a,String runId,long deadline){auth.live(a);String state=str(repo.get(runId),"state");if(!state.equals("running"))throw bad("CANCELLED","任务已停止");if(System.currentTimeMillis()>deadline)throw bad("TIMEOUT","执行时间预算已耗尽");}
    private void validateArgs(Map<String,Object> args,Map<String,Object> definition){Map<String,Object> schema=obj(obj(definition.get("function")).get("parameters")),properties=obj(schema.get("properties"));
        for(String required:strings(schema.get("required")))require(args.containsKey(required),"工具缺少必填参数："+required);
        for(Map.Entry<String,Object> e:args.entrySet()){require(properties.containsKey(e.getKey()),"工具包含未允许参数："+e.getKey());Map<String,Object> spec=obj(properties.get(e.getKey()));Object value=e.getValue();String type=str(spec,"type");require(value!=null,"工具参数不能为 null");if(type.equals("string"))require(value instanceof String&&((String)value).length()<=2000,"工具文本参数无效");else if(type.equals("integer"))require(value instanceof Number&&((Number)value).doubleValue()==((Number)value).intValue(),"工具整数参数无效");else if(type.equals("array")){require(value instanceof List&&((List<?>)value).size()<=20,"工具列表参数无效");for(Object x:(List<?>)value)require(x instanceof String&&((String)x).length()<=100,"工具列表元素无效");}if(spec.containsKey("enum"))require(strings(spec.get("enum")).contains(String.valueOf(value)),"工具枚举参数无效");}
    }
    public Map<String,Object> execute(Actor actor,String runId,String role,List<Map<String,Object>> messages,List<Map<String,Object>> tools,Function<Map<String,Object>,Map<String,Object>> handler,long deadline){
        Map<String,Map<String,Object>> allowed=new LinkedHashMap<>();for(Map<String,Object> t:tools)allowed.put(str(obj(t.get("function")),"name"),t);
        Map<String,Map<String,Object>> evidence=new LinkedHashMap<>();Set<String> called=new HashSet<>();int modelUsed=0,toolUsed=0;
        while(modelUsed<3){
            active(actor,runId,deadline);long start=System.currentTimeMillis();repo.trace(runId,role,"DeepSeek","running",map("model",client.status().get("model"),"call",modelUsed+1));
            Map<String,Object> response=client.complete(messages,tools,deadline,3-modelUsed);modelUsed+=num(response,"request_attempts",1);
            active(actor,runId,deadline);repo.trace(runId,role,"DeepSeek","success",map("response_id",response.get("id"),"model",response.get("model"),"usage",response.get("usage"),"elapsed_ms",System.currentTimeMillis()-start,"real_model",true));
            List<Map<String,Object>> choices=rows(response.get("choices"));require(!choices.isEmpty(),"模型没有返回有效结果");Map<String,Object> message=obj(choices.get(0).get("message"));List<Map<String,Object>> calls=rows(message.get("tool_calls"));
            if(calls.isEmpty())throw bad("UNGROUNDED_RESPONSE","模型未提供可核验的工具调用，本次未生成业务回答，请重试或补充范围");
            require(toolUsed+calls.size()<=4,"工具调用次数超过本轮预算");
            // Persist only public protocol fields, never hidden reasoning.
            messages.add(map("role","assistant","content",message.get("content"),"tool_calls",calls));
            for(Map<String,Object> call:calls){
                active(actor,runId,deadline);toolUsed++;Map<String,Object> fn=obj(call.get("function"));String name=str(fn,"name");require(allowed.containsKey(name),"模型请求了未注册工具");Map<String,Object> args=parse(str(fn,"arguments"));validateArgs(args,allowed.get(name));
                String fingerprint=name+json(new TreeMap<>(args));require(called.add(fingerprint),"模型重复请求相同工具，已停止循环");
                if(name.equals("ask_clarification")){require(calls.size()==1,"澄清不能与业务执行同时提交");return map("state","waiting_clarification","question",required(args,"question",500),"cards",new ArrayList<>(),"messages",messages);}
                if(name.equals("present_results")){
                    require(calls.size()==1,"结果交付须独立调用");List<String> ids=strings(args.get("evidence_ids"));require(!ids.isEmpty(),"交付结果缺少来源");List<Map<String,Object>> cards=new ArrayList<>();for(String id:ids){require(evidence.containsKey(id),"模型引用了不存在的结果来源");if(!cards.contains(evidence.get(id)))cards.add(evidence.get(id));}
                    messages.add(map("role","tool","tool_call_id",call.get("id"),"content",json(map("presented",ids))));
                    repo.trace(runId,role,name,"success",map("evidence_ids",ids));boolean confirmation=cards.stream().anyMatch(c->str(c,"type").equals("confirmation"));return map("state",confirmation?"confirmation_pending":"success","cards",cards,"messages",messages,"model_calls",modelUsed);
                }
                repo.trace(runId,role,name,"running",map("argument_keys",new ArrayList<>(args.keySet())));
                Map<String,Object> result=handler.apply(map("name",name,"args",args));active(actor,runId,deadline);String evidenceId="E"+(evidence.size()+1);result.put("evidence_id",evidenceId);
                String serialized=json(result);require(serialized.getBytes(StandardCharsets.UTF_8).length<=65536,"工具结果过大，请缩小查询范围");evidence.put(evidenceId,result);
                messages.add(map("role","tool","tool_call_id",call.get("id"),"content",serialized));repo.trace(runId,role,name,"success",map("evidence_id",evidenceId,"source_ref",result.get("source_ref"),"total",result.get("total"),"type",result.get("type")));
            }
        }throw bad("BUDGET_EXCEEDED","模型调用次数耗尽，任务未完成");
    }
}
