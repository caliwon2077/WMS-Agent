package com.wms.backend.modules.demo;

import static com.wms.backend.modules.demo.DemoJson.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.*;

@Component
public class DeepSeekClient {
    private final String configPath,environmentKey,environmentModel,endpoint;
    public DeepSeekClient(@Value("${app.demo.config-path:../project/local/deepseek.env}")String path,@Value("${DEEPSEEK_API_KEY:}")String key,@Value("${DEEPSEEK_MODEL:}")String model,@Value("${app.demo.deepseek-url:https://api.deepseek.com/chat/completions}")String endpoint){this.configPath=path;this.environmentKey=key;this.environmentModel=model;this.endpoint=endpoint;}
    private Map<String,String> config(){
        Map<String,String> out=new HashMap<>();out.put("DEEPSEEK_MODEL","deepseek-v4-pro");
        try{for(String line:Files.readAllLines(Paths.get(configPath),StandardCharsets.UTF_8)){if(line.trim().startsWith("#")||!line.contains("="))continue;String[] p=line.split("=",2);String v=p[1].trim();if(v.length()>1&&((v.startsWith("\"")&&v.endsWith("\""))||(v.startsWith("'")&&v.endsWith("'"))))v=v.substring(1,v.length()-1);out.put(p[0].trim(),v);}}catch(Exception ignored){}
        if(!environmentKey.isEmpty())out.put("DEEPSEEK_API_KEY",environmentKey);if(!environmentModel.isEmpty())out.put("DEEPSEEK_MODEL",environmentModel);return out;
    }
    public Map<String,Object> status(){Map<String,String> c=config();return map("provider","DeepSeek 官方 API","model",c.get("DEEPSEEK_MODEL"),"configured",!c.getOrDefault("DEEPSEEK_API_KEY","").isEmpty(),"synthetic_only",true,"thinking","disabled");}
    public Map<String,Object> complete(List<Map<String,Object>> messages,List<Map<String,Object>> tools,long deadline,int attemptBudget){
        Map<String,String> c=config();String key=c.getOrDefault("DEEPSEEK_API_KEY","");if(key.isEmpty())throw bad("MODEL_NOT_CONFIGURED","DeepSeek 密钥未配置，请由管理员配置本机凭据；人工查询仍可使用");
        if(System.currentTimeMillis()>=deadline)throw bad("TIMEOUT","任务已超过执行时间预算");
        require(json(messages).length()<=64000,"对话上下文过长，请开启新对话");
        Map<String,Object> body=map("model",c.get("DEEPSEEK_MODEL"),"messages",messages,"tools",tools,"tool_choice","required","thinking",map("type","disabled"),"temperature",0,"max_tokens",1200);
        HttpHeaders headers=new HttpHeaders();headers.setContentType(MediaType.APPLICATION_JSON);headers.setBearerAuth(key);
        for(int attempt=0;attempt<Math.min(2,attemptBudget);attempt++){
            int remaining=(int)Math.min(45000,deadline-System.currentTimeMillis());if(remaining<=0)throw bad("TIMEOUT","任务已超过执行时间预算");
            SimpleClientHttpRequestFactory factory=new SimpleClientHttpRequestFactory();factory.setConnectTimeout(Math.min(8000,remaining));factory.setReadTimeout(remaining);
            try{ResponseEntity<String> response=new RestTemplate(factory).exchange(endpoint,HttpMethod.POST,new HttpEntity<>(json(body),headers),String.class);Map<String,Object> result=parse(response.getBody());result.put("request_attempts",attempt+1);return result;}
            catch(HttpStatusCodeException e){int status=e.getRawStatusCode();if(attempt==0&&attemptBudget>1&&(status==429||status>=500)&&deadline-System.currentTimeMillis()>3000){pause();continue;}String message=status==401?"DeepSeek 认证失败，请检查服务端密钥":status==402?"DeepSeek 账户余额不足":status==429?"DeepSeek 请求限流，请稍后重试":status==400||status==422?"DeepSeek 请求参数不兼容，请查看模型配置":"DeepSeek 服务暂不可用（HTTP "+status+"）";throw bad("MODEL_HTTP_"+status,message);}
            catch(ResourceAccessException e){if(attempt==0&&attemptBudget>1&&deadline-System.currentTimeMillis()>5000){pause();continue;}throw bad("MODEL_NETWORK","无法连接 DeepSeek 或请求超时；请检查网络，人工查询仍可使用");}
        }throw bad("MODEL_UNAVAILABLE","DeepSeek 暂不可用");
    }
    private void pause(){try{Thread.sleep(600);}catch(InterruptedException e){Thread.currentThread().interrupt();throw bad("CANCELLED","任务已取消");}}
}
