package com.school.exception;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一异常处理类
 */
@ControllerAdvice
public class JWTExceptionHandler {
	
    @ExceptionHandler(value = MalformedJwtException.class)
    @ResponseBody
    public Result error(Exception e){
//        e.printStackTrace(); //
        System.out.println("jwt出异常了");
        return new Result(false, StatusCode.ERROR, "jwt出异常了");
    }
}
