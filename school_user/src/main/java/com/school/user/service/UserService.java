package com.school.user.service;

import com.school.email.MailService;
import com.school.user.dao.UserDao;
import com.school.user.pojo.User;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import util.IdWorker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 服务层
 * 
 * @author Administrator
 *
 */
@Service

public class UserService {

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private IdWorker idWorker;

	@Autowired
	private RedisTemplate redisTemplate;


	@Autowired
	private MailService mailService;


	@Autowired
	BCryptPasswordEncoder encoder;

	/**
	 * 修改头像
	 * @param userid  用户id
	 * @param avatar  用户头像
	 */
	@Transactional
	public void updateImg(String userid, String avatar) {
		userDao.updateImg(userid,avatar);
	}

	/**
	 * 查询全部列表
	 * @return
	 */
	public List<User> findAll() {
		return userDao.findAll();
	}

	/**
	 * 发送短信验证码
	 * @param mobile
	 */
	public void sendSms(String mobile){

		//1.生成6位短信验证码
		Random random=new Random();
		int max=999999;//最大数
		int min=100000;//最小数
		int code = random.nextInt(max);//随机生成
		if(code<min){
			code=code+min;
		}
		System.out.println(mobile+"收到验证码是："+code);
		//2.将验证码放入redis
		redisTemplate.opsForValue().set("smscode_"+mobile, code+"" ,5, TimeUnit.MINUTES);//五分钟过期


		//3.发送出去
		mailService.sendSimpleMail(mobile,"校园社交验证码","尊敬的用户您好,你的验证码为"+code+",五分钟内有效");

	}

	/**
	 * 修改密码
	 * @param user
	 * @param code
	 */
	@Transactional
	public void updatePass(User user,String code) {
		System.out.println("user="+user.getMobile());

        //判断验证码是否正确
		String syscode = (String)redisTemplate.opsForValue().get("smscode_" + user.getMobile());
		System.out.println("syscode redis="+syscode);
		//提取系统正确的验证码
		if(syscode==null){
			throw new RuntimeException("请点击获取短信验证码");
		}

		if(!syscode.equals(code)){
			throw new RuntimeException("验证码输入不正确");
		}
		User byMobile = userDao.findByMobile(user.getMobile());
		if (byMobile==null){
			throw new RuntimeException("该邮箱尚未注册");
		}

		//密码加密
		String newpassword = encoder.encode(user.getPassword());//加密后的密码
		user.setPassword(newpassword);

		userDao.updatePass(user.getMobile(),newpassword);

	}

	/**
	 * 添加用户
	 * @param user
	 * @param code
	 */
	public void add(User user,String code) {
		//判断验证码是否正确
		String syscode = (String)redisTemplate.opsForValue().get("smscode_" + user.getMobile());
		System.out.println("syscode redis="+syscode);
		//提取系统正确的验证码
		if(syscode==null){
			throw new RuntimeException("请点击获取短信验证码");
		}

		if(!syscode.equals(code)){
			throw new RuntimeException("验证码输入不正确");
		}

		User byMobile = userDao.findByMobile(user.getMobile());
		if (byMobile!=null){
			throw new RuntimeException("该邮箱已被注册");
		}

		user.setId( idWorker.nextId()+"" );
		user.setFollowcount(0);//关注数
		user.setFanscount(0);//粉丝数
		user.setOnline(0L);//在线时长
		user.setRegdate(new Date());//注册日期
		user.setUpdatedate(new Date());//更新日期
		user.setLastdate(new Date());//最后登陆日期
		user.setAvatar("./img/widget-dating1.png");  //默认值
		//密码加密
		String newpassword = encoder.encode(user.getPassword());//加密后的密码
		user.setPassword(newpassword);
		//密码加密
		userDao.save(user);
	}

	/**
	 * 登录
	 * @param mobile
	 * @param password
	 * @return
	 */
	public User findByMobileAndPassword(String mobile,String password){
		User user = userDao.findByMobile(mobile);
		if (user!=null&&encoder.matches(password,user.getPassword())){
			return user;
		}else if (user==null){
			throw new RuntimeException("该账户不存在");
		}else{
			throw new RuntimeException("密码错误");
		}

	}
	
	/**
	 * 条件查询+分页
	 * @param whereMap
	 * @param page
	 * @param size
	 * @return
	 */
	public Page<User> findSearch(Map whereMap, int page, int size) {
		Specification<User> specification = createSpecification(whereMap);
		PageRequest pageRequest =  PageRequest.of(page-1, size);
		return userDao.findAll(specification, pageRequest);
	}

	
	/**
	 * 条件查询
	 * @param whereMap
	 * @return
	 */
	public List<User> findSearch(Map whereMap) {
		Specification<User> specification = createSpecification(whereMap);
		return userDao.findAll(specification);
	}

	/**
	 * 跟新粉丝数
	 * @param userid
	 * @param x
	 */
    @Transactional
	public void incFanscount(String userid,int x){
		userDao.incFanscount(userid,x);
	}

	@Transactional
	public void incFollowcount(String userid,int x){
    	userDao.incFollowcount(userid,x);
	}

	/**
	 * 根据ID查询实体
	 * @param id
	 * @return
	 */
	public User findById(String id) {
		return userDao.findById(id).get();
	}



	/**
	 * 修改
	 * @param user
	 */
	public void update(User user) {

		userDao.save(user);
	}

	/**
	 * 删除
	 * @param id
	 */
	public void deleteById(String id) {
		userDao.deleteById(id);
	}

	/**
	 * 动态条件构建
	 * @param searchMap
	 * @return
	 */
	private Specification<User> createSpecification(Map searchMap) {

		return new Specification<User>() {

			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicateList = new ArrayList<Predicate>();
                // ID
                if (searchMap.get("id")!=null && !"".equals(searchMap.get("id"))) {
                	predicateList.add(cb.like(root.get("id").as(String.class), "%"+(String)searchMap.get("id")+"%"));
                }
                // 手机号码
                if (searchMap.get("mobile")!=null && !"".equals(searchMap.get("mobile"))) {
                	predicateList.add(cb.like(root.get("mobile").as(String.class), "%"+(String)searchMap.get("mobile")+"%"));
                }
                // 密码
                if (searchMap.get("password")!=null && !"".equals(searchMap.get("password"))) {
                	predicateList.add(cb.like(root.get("password").as(String.class), "%"+(String)searchMap.get("password")+"%"));
                }
                // 昵称
                if (searchMap.get("nickname")!=null && !"".equals(searchMap.get("nickname"))) {
                	predicateList.add(cb.like(root.get("nickname").as(String.class), "%"+(String)searchMap.get("nickname")+"%"));
                }
                // 性别
                if (searchMap.get("sex")!=null && !"".equals(searchMap.get("sex"))) {
                	predicateList.add(cb.like(root.get("sex").as(String.class), "%"+(String)searchMap.get("sex")+"%"));
                }
                // 头像
                if (searchMap.get("avatar")!=null && !"".equals(searchMap.get("avatar"))) {
                	predicateList.add(cb.like(root.get("avatar").as(String.class), "%"+(String)searchMap.get("avatar")+"%"));
                }
                // E-Mail
                if (searchMap.get("email")!=null && !"".equals(searchMap.get("email"))) {
                	predicateList.add(cb.like(root.get("email").as(String.class), "%"+(String)searchMap.get("email")+"%"));
                }
                // 兴趣
                if (searchMap.get("interest")!=null && !"".equals(searchMap.get("interest"))) {
                	predicateList.add(cb.like(root.get("interest").as(String.class), "%"+(String)searchMap.get("interest")+"%"));
                }
                // 个性
                if (searchMap.get("personality")!=null && !"".equals(searchMap.get("personality"))) {
                	predicateList.add(cb.like(root.get("personality").as(String.class), "%"+(String)searchMap.get("personality")+"%"));
                }
				
				return cb.and( predicateList.toArray(new Predicate[predicateList.size()]));

			}
		};

	}

}

/*
*
  mail:
    host: smtp.qq.com 	#邮箱服务器地址
    username: 1786678583@qq.com  #邮箱账号
    password: ihypidhglruvbigd	#邮箱密码
    default-encoding: utf-8	#默认编码
    --------
    spring.mail.host=smtp.163.com
//spring.mail.port=587 这是qq的端口，网易不需要
spring.mail.username=hufei1639670695@163.com
spring.mail.password=merryhappy123456
spring.mail.default-encoding=utf-8
spring.mail.properties.mail.smtp.socketFactory.class=javax.net.ssl.SSLSocketFactory
//将发送邮件得过程打印在控制台
spring.mail.properties.mail.debug=true//打印日志

 */
