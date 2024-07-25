package org.keshe.keshe.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.keshe.keshe.utills.JwtUtil;
import org.keshe.keshe.utills.ThreadLocalUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        //令牌验证
        String token = request.getHeader("Authorization");
        //验证token
        try {
            Map<String,Object> claims = JwtUtil.parseToken(token);

            //把数据存到ThreadLocal中
            ThreadLocalUtil.set(claims);
            return true;
        } catch (Exception e) {
            //状态响应码为114541
            response.setStatus(401);
            return false;
        }
    }
    @Override
    public void afterCompletion(HttpServletRequest request,HttpServletResponse response,Object handler,Exception ex) throws Exception{
        //清空
        ThreadLocalUtil.remove();
    }
}





















