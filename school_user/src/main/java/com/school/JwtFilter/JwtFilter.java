package com.school.JwtFilter;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@SuppressWarnings("all")
public class JwtFilter extends HandlerInterceptorAdapter {
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("经过了拦截器");
        String authHeader = request.getHeader("Authorization");  //如果没有token，他就没有claims

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            final String token = authHeader.substring(7); // The part after "Bearer "
            System.out.println("token="+token);
            //获取Claims
            Claims claims = jwtUtil.parseJWT(token);
            if (claims != null) {
                if("admin".equals(claims.get("roles"))) {//如果是管理员
                    System.out.println("jwtfilter admin");
                    request.setAttribute("admin_claims", claims);
                }

                if("user".equals(claims.get("roles"))){//如果是用户
                    System.out.println("jwtfilter user");
                    request.setAttribute("user_claims", claims);
                }
            }else {  //claims为空
                throw new Exception("你还没有登录");
            }

        }

        return true;
    }
}
