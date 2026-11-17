package com.wms.backend.modules.demo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wms.backend.common.exception.BusinessException;
import java.time.*;
import java.util.*;

public final class DemoJson {
    public static final ObjectMapper JSON = new ObjectMapper();
    public static final ZoneId ZONE = ZoneId.of("Asia/Shanghai");
    private DemoJson() {}
    public static Map<String,Object> map(Object... pairs) {
        Map<String,Object> m = new LinkedHashMap<>();
        for (int i=0;i<pairs.length;i+=2) m.put((String)pairs[i],pairs[i+1]);
        return m;
    }
    public static String str(Map<String,Object> m,String k) { Object v=m.get(k);return v==null?"":String.valueOf(v).trim(); }
    public static int num(Map<String,Object> m,String k,int fallback) { try{return Integer.parseInt(str(m,k));}catch(Exception e){return fallback;} }
    @SuppressWarnings("unchecked") public static Map<String,Object> obj(Object v) { return v instanceof Map?(Map<String,Object>)v:new LinkedHashMap<>(); }
    @SuppressWarnings("unchecked") public static List<Map<String,Object>> rows(Object v) {return v instanceof List?(List<Map<String,Object>>)v:new ArrayList<>();}
    public static List<String> strings(Object v) {List<String> out=new ArrayList<>();if(v instanceof List)for(Object x:(List<?>)v)out.add(String.valueOf(x));return out;}
    public static String json(Object o) {try{return JSON.writeValueAsString(o);}catch(Exception e){throw new IllegalStateException(e);}}
    public static Map<String,Object> parse(String s) {try{return JSON.readValue(s,new TypeReference<Map<String,Object>>(){});}catch(Exception e){throw bad("INVALID_JSON","参数必须是合法 JSON 对象");}}
    public static String id(String prefix) {return prefix+"-"+UUID.randomUUID().toString().replace("-","").substring(0,16);}
    public static String now() {return OffsetDateTime.now(ZONE).toString();}
    public static BusinessException bad(String code,String message) {return new BusinessException(code,message);}
    public static void require(boolean ok,String message) {if(!ok)throw bad("VALIDATION_ERROR",message);}
    public static String required(Map<String,Object> m,String key,int max) {String s=str(m,key);require(!s.isEmpty()&&s.length()<=max,"字段 "+key+" 必填且不能超过 "+max+" 字符");return s;}
    public static LocalDate date(String value) {try{return LocalDate.parse(value);}catch(Exception e){throw bad("INVALID_DATE","日期须为有效的 YYYY-MM-DD");}}
    public static YearMonth month(String value) {try{return YearMonth.parse(value);}catch(Exception e){throw bad("INVALID_PERIOD","月份须为 YYYY-MM");}}
}
