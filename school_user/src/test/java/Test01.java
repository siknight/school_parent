import com.school.SchoolApplication;
import com.school.article.dao.ReplyDao;
import com.school.article.pojo.ArticleReply;
import com.school.article.pojo.ArticleReplyUser;
import com.school.article.service.ReplyService;
import com.school.user.dao.UserDao;
import com.school.user.pojo.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SchoolApplication.class)
public class Test01 {

    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    ReplyDao replyDao;
    @Autowired
    UserDao userDao;

    @Autowired
    ReplyService service;

    @Test
    public void test06(){
        List<ArticleReplyUser> allReplyByArticleid = service.findAllReplyByArticleid("1240211588263317504");
        System.out.println(allReplyByArticleid);
//        String userid="1234380501851181056";
//        User user = userDao.findUserById(userid);
//        User user = userDao.findById("1234380501851181056").get();
//        System.out.println(user);
    }

    @Test
    public void test05(){
        List<ArticleReply> byArticleid = replyDao.findByArticleid("1240211588263317504");
        System.out.println(byArticleid);
    }
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
