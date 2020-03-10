package com.school.user.controller;

import com.school.user.pojo.User;
import com.school.user.service.UserService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 控制器层
 * @author Administrator
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	BCryptPasswordEncoder encoder;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private JwtUtil jwtUtil;

	/**
	 * 发送短信
	 * @param mobile
	 * @return
	 */
	@RequestMapping(value="/sendsms/{mobile}",method=RequestMethod.POST)
	public Result sendsms(@PathVariable String mobile ){
		userService.sendSms(mobile);
		return new Result(true,StatusCode.OK,"发送成功");
	}

	/*
	 * 注册 添加用户
	 * @param user
	 * @param code
	 * @return
	 */
	@RequestMapping(value="/register/{code}",method=RequestMethod.POST)
	public Result register(@RequestBody User user , @PathVariable String code){
		userService.add(user,code);
		return new Result(true,StatusCode.OK,"注册成功");

	}

	@RequestMapping(value="/pass/{code}",method=RequestMethod.PUT)
	public Result updatePass(@RequestBody User user , @PathVariable String code){
		userService.updatePass(user,code);
		return new Result(true,StatusCode.OK,"密码修改成功，请登录");

	}
	
	/**
	 * 查询全部数据
	 * @return
	 */
	@RequestMapping(method= RequestMethod.GET)
	public Result findAll(){
		return new Result(true,StatusCode.OK,"查询成功",userService.findAll());
	}
	
	/**
	 * 根据ID查询
	 * @return
	 */
	@RequestMapping(value="/findById",method= RequestMethod.GET)
	public Result findById(){
		//判断是否有权限访问
		Claims claims=(Claims) request.getAttribute("user_claims");
		if(claims==null){
			return new Result(true,StatusCode.ACCESSERROR,"无权访问");
		}
		//claims.getId()是为了获取token里存的用户id
		String userId = claims.getId(); //获取到userid
		return new Result(true,StatusCode.OK,"查询成功",userService.findById(userId));

	}

	/**
	 * 跟新粉丝数
	 * @param userid
	 * @param x
	 */
	@RequestMapping(value="/incfans/{userid}/{x}",method= RequestMethod.POST)
	public void incFanscount(String userid,int x){
		userService.incFanscount(userid,x);
	}

	/**
	 * 跟新关注数
	 * @param userid
	 * @param x
	 */
	public void incFollowcount(@PathVariable String userid,@PathVariable int x){
		userService.incFollowcount(userid,x);
	}

	/**
	 * 用户登陆
	 * @param mobile
	 * @param password
	 * @return
	 */
	@RequestMapping(value="/login",method=RequestMethod.POST)
	public Result login(@RequestParam("mobile") String mobile,@RequestParam("password") String password){
		System.out.println("mobile="+mobile+",password="+password);
		User user = userService.findByMobileAndPassword(mobile,password);
		if(user!=null){
			String token = jwtUtil.createJWT(user.getId(), user.getNickname(), "user");
			Map map=new HashMap();
			map.put("token",token);  //里面包含id，nickname
			map.put("userId",user.getId());   //id
			map.put("name",user.getNickname());//昵称
			map.put("avatar",user.getAvatar());//头像
			return new Result(true,StatusCode.OK,"登陆成功",map);
		}else {
			return new Result(false,StatusCode.LOGINERROR,"用户名或密码错误");
		}

	}


	/**
	 * 分页+多条件查询
	 * @param searchMap 查询条件封装
	 * @param page 页码
	 * @param size 页大小
	 * @return 分页结果
	 */
	@RequestMapping(value="/search/{page}/{size}",method=RequestMethod.POST)
	public Result findSearch(@RequestBody Map searchMap , @PathVariable int page, @PathVariable int size){
//		Page<User> pageList = userService.findSearch(searchMap, page, size);
//		return  new Result(true,StatusCode.OK,"查询成功",  new PageResult<User>(pageList.getTotalElements(), pageList.getContent()) );
		return  null;
	}

	/**
     * 根据条件查询
     * @param searchMap
     * @return
     */
    @RequestMapping(value="/search",method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap){
        return new Result(true,StatusCode.OK,"查询成功",userService.findSearch(searchMap));
    }
	
	/**
	 * 增加
	 * @param user
	 */
	@RequestMapping(method=RequestMethod.POST)
	public Result add(@RequestBody User user  ){
//		userService.add(user);
//		return new Result(true,StatusCode.OK,"增加成功");
		return null;
	}
	
	/**
	 * 修改 根据条件
	 * @param user
	 */
	@RequestMapping(value="/{updateType}",method= RequestMethod.PUT)
	@Transactional
	public Result update(@RequestBody User user, @PathVariable String updateType ){
  //判断是否有权限访问
		Claims claims=(Claims) request.getAttribute("user_claims");
		if(claims==null){
			return new Result(true,StatusCode.ACCESSERROR,"无权访问");
		}
		//claims.getId()是为了获取token里存的用户id
		String userId = claims.getId(); //获取到userid
		User userDB = userService.findById(userId); //通过id查询数据库里的用户
		//判断类型
		if ("qq".equals(updateType)){
			userDB.setMyqq(user.getMyqq());
		}else if("school".equals(updateType)){ //修改学校
			userDB.setMyschool(user.getMyschool());
		}else if("professional".equals(updateType)){  //修改学院
			userDB.setMyprofessional(user.getMyprofessional());
		}else if ("grade".equals(updateType)){
			userDB.setMygrade(user.getMygrade());
		}else if ("all".equals(updateType)){
//			userDB
		}
		System.out.println("正在修改");
		userService.update(userDB);
		return new Result(true,StatusCode.OK,"修改成功",userDB);
	}
	
	/**
	 * 删除
	 * @param id
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.DELETE)
	public Result delete(@PathVariable String id ){
		Claims claims=(Claims) request.getAttribute("admin_claims");
		if(claims==null){
			return new Result(true,StatusCode.ACCESSERROR,"无权访问");
		}

		userService.deleteById(id);
		return new Result(true,StatusCode.OK,"删除成功");
	}
	
}
