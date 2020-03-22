package com.school.TokenVeriryController;

import com.school.user.pojo.Admin;
import com.school.user.pojo.User;
import com.school.user.service.AdminService;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;



@CrossOrigin
@RequestMapping("/token")
@RestController
@SuppressWarnings("all")
public class TokenController {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    AdminService adminService;

    @RequestMapping(value="/tokenVerify",method= RequestMethod.GET)
    public Result tokenVerify(HttpServletRequest request){

        String authHeader = request.getHeader("Authorization");  //如果没有token，他就没有claims
        System.out.println("authheader:"+authHeader);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            System.out.println("admin 验证");
            final String token = authHeader.substring(7);
            Claims claims = jwtUtil.parseJWT(token);
            if("user".equals(claims.get("roles"))){//如果是用户
                System.out.println("jwtfilter admin 验证");
                return new Result(true,StatusCode.OK,"验证成功");
            }
        }
        //无权访问
        return new Result(true,StatusCode.ACCESSERROR,"无权访问");

    }

    /**
     * 登录校验
     * 根据token查询
     * @param
     * @return
     */
    @RequestMapping(value="/admintoken",method= RequestMethod.GET)
    public Result findByToken(HttpServletRequest request){
        System.out.println("admin经过了拦截器");
        String authHeader = request.getHeader("Authorization");  //如果没有token，他就没有claims
        System.out.println("admin authheader:"+authHeader);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            System.out.println("admin 验证");
            final String token = authHeader.substring(7);
            Claims claims = jwtUtil.parseJWT(token);
            if("admin".equals(claims.get("roles"))){//如果是用户
                System.out.println("jwtfilter user 验证");
                Admin admin = adminService.findById(claims.getId());
                admin.setPassword(null);
                return new Result(true,StatusCode.OK,"验证成功" ,admin);
            }
        }
        //无权访问
        return new Result(true,StatusCode.ACCESSERROR,"无权访问");
    }
}
