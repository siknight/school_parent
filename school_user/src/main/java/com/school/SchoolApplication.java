package com.school;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import util.IdWorker;
import util.JwtUtil;

@SpringBootApplication
public class SchoolApplication {

	public static void main(String[] args) {
		SpringApplication.run(SchoolApplication.class, args);
	}
	//生成id
	@Bean
	public IdWorker idWorkker(){
		return new IdWorker(1, 1);
	}
//	密码加密
	@Bean
	public BCryptPasswordEncoder bcryptPasswordEncoder(){
		return new BCryptPasswordEncoder();
	}
	//生成token
	@Bean
	public JwtUtil jwtUtil(){
		return new util.JwtUtil();
	}

}
