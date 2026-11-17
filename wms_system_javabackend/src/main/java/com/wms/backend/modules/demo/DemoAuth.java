package com.wms.backend.modules.demo;

import static com.wms.backend.modules.demo.DemoJson.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.time.OffsetDateTime;
import java.util.*;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
public class DemoAuth {
    public static class Actor {
        public final String id,name,role,factory; public final int revision;
        Actor(Map<String,Object> row){id=str(row,"id");name=str(row,"name");role=str(row,"role");factory=str(row,"factory");revision=num(row,"revision",1);}
        public boolean hq(){return "HQ".equals(role);}
        public Map<String,Object> view(){return map("id",id,"name",name,"role",role,"factory",factory);}
    }
    private final DemoRepository repo;
    public DemoAuth(DemoRepository repo){this.repo=repo;}
    @PostConstruct void seed(){
        seedUser("admin","总部 · 机动能源部","HQ","ALL");
        for(int n=1;n<=12;n++)seedUser(String.format("factory%02d",n),String.format("%02d厂 · 资产员",n),"STOCK",String.format("F%02d",n));
        seedUser("tech02","二厂 · 维修人员","TECH","F02");
    }
    private void seedUser(String id,String name,String role,String factory){
        if(repo.jdbc().queryForObject("SELECT COUNT(*) FROM demo_users WHERE id=?",Integer.class,id)>0)return;
        byte[] bytes=new byte[16];new SecureRandom().nextBytes(bytes);String salt=Base64.getEncoder().encodeToString(bytes);
        repo.jdbc().update("INSERT INTO demo_users(id,name,role,factory,password_hash,salt) VALUES(?,?,?,?,?,?)",id,name,role,factory,password("Demo@2026",salt),salt);
    }
    private String password(String value,String salt){try{return Base64.getEncoder().encodeToString(SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(new PBEKeySpec(value.toCharArray(),Base64.getDecoder().decode(salt),60000,256)).getEncoded());}catch(Exception e){throw new IllegalStateException(e);}}
    public static String hash(String value){try{return Base64.getEncoder().encodeToString(MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.UTF_8)));}catch(Exception e){throw new IllegalStateException(e);}}
    public Map<String,Object> login(String id,String pass){
        require(id.length()<=40&&pass.length()<=200,"账号或密码过长");
        List<Map<String,Object>> rows=repo.jdbc().queryForList("SELECT * FROM demo_users WHERE id=? AND active=1",id);
        if(rows.isEmpty()||!MessageDigest.isEqual(password(pass,str(rows.get(0),"salt")).getBytes(StandardCharsets.UTF_8),str(rows.get(0),"password_hash").getBytes(StandardCharsets.UTF_8)))throw bad("LOGIN_FAILED","账号或密码不正确");
        Actor a=new Actor(rows.get(0));String token=UUID.randomUUID().toString()+UUID.randomUUID();
        repo.jdbc().update("INSERT INTO demo_sessions VALUES(?,?,?,?)",hash(token),a.id,a.revision,OffsetDateTime.now(ZONE).plusHours(8).toString());
        return map("token",token,"user",a.view());
    }
    public Actor authenticate(String header){
        if(header==null||!header.startsWith("Bearer "))throw bad("UNAUTHORIZED","请先登录");
        List<Map<String,Object>> rows=repo.jdbc().queryForList("SELECT u.* FROM demo_sessions s JOIN demo_users u ON u.id=s.user_id WHERE s.token_hash=? AND s.expires_at>? AND s.revision=u.revision AND u.active=1",hash(header.substring(7)),now());
        if(rows.isEmpty())throw bad("UNAUTHORIZED","会话已失效，请重新登录");return new Actor(rows.get(0));
    }
    public void live(Actor a){Integer n=repo.jdbc().queryForObject("SELECT COUNT(*) FROM demo_users WHERE id=? AND revision=? AND active=1",Integer.class,a.id,a.revision);if(n!=1)throw bad("FORBIDDEN","授权已变更，请重新登录");}
    public void scope(Actor a,String factory){live(a);require(factory.matches("F(0[1-9]|1[0-2])")||factory.equals("ALL"),"请选择有效工厂");if(!a.hq()&&!a.factory.equals(factory))throw bad("FORBIDDEN","无权访问该工厂范围");}
    public void stock(Actor a,String factory){scope(a,factory);if("TECH".equals(a.role))throw bad("FORBIDDEN","当前账号无资产维护权限");}
    public void hq(Actor a){live(a);if(!a.hq())throw bad("FORBIDDEN","此操作仅限总部管理员");}
    public void logout(String header){if(header!=null&&header.startsWith("Bearer "))repo.jdbc().update("DELETE FROM demo_sessions WHERE token_hash=?",hash(header.substring(7)));}
    public void revoke(Actor admin,String id){hq(admin);require(!admin.id.equals(id),"不能在本次操作撤销自己的会话");repo.jdbc().update("UPDATE demo_users SET revision=revision+1 WHERE id=?",id);}
    public List<Map<String,Object>> users(Actor a){return repo.jdbc().queryForList("SELECT id,name,role,factory FROM demo_users WHERE active=1 AND (factory=? OR ?='HQ')",a.factory,a.role);}
}
