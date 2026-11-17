package com.wms.backend.modules.demo;

import com.wms.backend.common.api.ApiResponse;
import com.wms.backend.common.exception.BusinessException;
import javax.servlet.http.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class DemoWebConfig implements WebMvcConfigurer {
    private final DemoAuth auth;private final boolean enabled;
    public DemoWebConfig(DemoAuth auth,@Value("${app.demo.enabled:true}")boolean enabled){this.auth=auth;this.enabled=enabled;}
    @Override public void addInterceptors(InterceptorRegistry registry){registry.addInterceptor(new HandlerInterceptor(){
        @Override public boolean preHandle(HttpServletRequest req,HttpServletResponse res,Object handler)throws Exception{
            String path=req.getServletPath();
            if("OPTIONS".equals(req.getMethod())||path.equals("/error")||path.equals("/v1/system/health"))return true;
            if(!path.startsWith("/v1/demo/")){
                if(!enabled)return true;
                res.setStatus(410);res.setContentType("application/json;charset=UTF-8");res.getWriter().write(DemoJson.json(ApiResponse.fail("LEGACY_DISABLED","旧接口未纳入本次 Demo，已关闭")));return false;
            }
            if(path.equals("/v1/demo/login"))return true;
            try{req.setAttribute("actor",auth.authenticate(req.getHeader("Authorization")));return true;}
            catch(BusinessException e){res.setStatus(401);res.setContentType("application/json;charset=UTF-8");res.getWriter().write(DemoJson.json(ApiResponse.fail(e.getCode(),e.getMessage())));return false;}
        }
    }).addPathPatterns("/**");}
}
