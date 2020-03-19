package userweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableEurekaClient
public class UserWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserWebApplication.class,args);
    }


}
