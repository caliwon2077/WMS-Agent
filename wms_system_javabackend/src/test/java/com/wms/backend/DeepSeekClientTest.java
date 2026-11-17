package com.wms.backend;

import static com.wms.backend.modules.demo.DemoJson.*;
import static org.junit.jupiter.api.Assertions.*;
import com.wms.backend.modules.demo.DeepSeekClient;
import com.wms.backend.common.exception.BusinessException;
import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.util.StreamUtils;

class DeepSeekClientTest {
    @ParameterizedTest @ValueSource(ints={400,401,402,422})
    void permanentProviderErrorsDoNotRetryOrLeakResponse(int status)throws Exception{
        AtomicInteger count=new AtomicInteger();HttpServer server=HttpServer.create(new InetSocketAddress("127.0.0.1",0),0);
        server.createContext("/chat",exchange->{count.incrementAndGet();byte[] body="PRIVATE_PROVIDER_RESPONSE".getBytes(StandardCharsets.UTF_8);exchange.sendResponseHeaders(status,body.length);exchange.getResponseBody().write(body);exchange.close();});server.start();
        try{DeepSeekClient client=new DeepSeekClient("/nonexistent/wms-test", "synthetic-test-key", "deepseek-v4-pro", "http://127.0.0.1:"+server.getAddress().getPort()+"/chat");BusinessException e=assertThrows(BusinessException.class,()->client.complete(new ArrayList<>(),new ArrayList<>(),System.currentTimeMillis()+5000,3));assertEquals("MODEL_HTTP_"+status,e.getCode());assertFalse(e.getMessage().contains("PRIVATE_PROVIDER_RESPONSE"));assertEquals(1,count.get());}finally{server.stop(0);}
    }
    @Test void transientErrorRetriesOnceWithinBudgetAndUsesRequiredNonThinkingTools()throws Exception{
        AtomicInteger count=new AtomicInteger();List<Map<String,Object>> requests=new ArrayList<>();HttpServer server=HttpServer.create(new InetSocketAddress("127.0.0.1",0),0);
        server.createContext("/chat",exchange->{requests.add(parse(StreamUtils.copyToString(exchange.getRequestBody(),StandardCharsets.UTF_8)));int status=count.incrementAndGet()==1?429:200;byte[] body=json(map("id","injected-provider-retry")).getBytes(StandardCharsets.UTF_8);exchange.sendResponseHeaders(status,body.length);exchange.getResponseBody().write(body);exchange.close();});server.start();
        try{DeepSeekClient client=new DeepSeekClient("/nonexistent/wms-test","synthetic-test-key","deepseek-v4-pro","http://127.0.0.1:"+server.getAddress().getPort()+"/chat");Map<String,Object> result=client.complete(new ArrayList<>(),new ArrayList<>(),System.currentTimeMillis()+10000,3);assertEquals(2,count.get());assertEquals(2,result.get("request_attempts"));assertEquals("required",requests.get(0).get("tool_choice"));assertEquals("disabled",obj(requests.get(0).get("thinking")).get("type"));}finally{server.stop(0);}
    }
    @Test void finalAttemptDoesNotExceedModelBudget()throws Exception{
        AtomicInteger count=new AtomicInteger();HttpServer server=HttpServer.create(new InetSocketAddress("127.0.0.1",0),0);server.createContext("/chat",exchange->{count.incrementAndGet();exchange.sendResponseHeaders(503,-1);exchange.close();});server.start();try{DeepSeekClient client=new DeepSeekClient("/nonexistent/wms-test","synthetic-test-key","deepseek-v4-pro","http://127.0.0.1:"+server.getAddress().getPort()+"/chat");assertThrows(BusinessException.class,()->client.complete(new ArrayList<>(),new ArrayList<>(),System.currentTimeMillis()+5000,1));assertEquals(1,count.get());}finally{server.stop(0);}
    }
    @Test void missingCredentialCannotBecomeFakeSuccess()throws Exception{Path p=Files.createTempFile("empty-wms-config-",".env");DeepSeekClient client=new DeepSeekClient(p.toString(),"","deepseek-v4-pro","http://127.0.0.1:1");assertEquals(false,client.status().get("configured"));BusinessException e=assertThrows(BusinessException.class,()->client.complete(new ArrayList<>(),new ArrayList<>(),System.currentTimeMillis()+1000,3));assertEquals("MODEL_NOT_CONFIGURED",e.getCode());Files.delete(p);}
}
