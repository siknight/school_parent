import com.school.SchoolApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SchoolApplication.class)
public class Test01 {

    @Autowired
    RedisTemplate redisTemplate;
    @Test
    public void test01(){
        redisTemplate.opsForValue().set("01","我是java爱好者",5,TimeUnit.MINUTES);
        System.out.println("存完毕");
    }

    @Test
    public void test02(){
        System.out.println(redisTemplate.opsForValue().get("01"));
        System.out.println("----------------");
        Object o = redisTemplate.opsForValue().get("smscode2632492263@qq");
//        Object o = redisTemplate.opsForValue().get("smscode");
        System.out.println("2生成时发送的code："+o);
        System.out.println("---3333-----");
        Object o2 = redisTemplate.opsForValue().get("3333");
        System.out.println("3333生成时发送的code："+o2);
        System.out.println("取完毕");
    }


}
