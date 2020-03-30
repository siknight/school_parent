package com.school.user.controller;

import com.school.user.pojo.User;
import com.school.user.service.UserService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import util.FileUtil;
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
	 * 修改头像
	 * @param
	 * @return
	 */
	@RequestMapping(value="/avatar",method=RequestMethod.POST)
	public Result updateImg(@RequestPart("uploadFile") MultipartFile uploadFile) {
		//判断是否有权限访问
		Claims claims=(Claims) request.getAttribute("user_claims");
		if(claims==null){
			return new Result(true,StatusCode.ACCESSERROR,"无权访问");
		}
		//上传的文件存放在一个绝对路径里
		String tempFileName = FileUtil.FileUpload(uploadFile,"D:\\imageschool\\userimages\\","user");
		//claims.getId()是为了获取token里存的用户id
		String userId = claims.getId(); //获取到userid
		//数据库存的相对路径和物理路径进行映射
		userService.updateImg(userId,"/userimage/"+tempFileName);
		HashMap<String, Object> map = new HashMap<>();
		map.put("avatar","/userimage/"+tempFileName);
		return new Result(true,StatusCode.OK,"修改成功",map);
	}

	/**
	 * 发送短信
	 * @param mobile
	 * @return
	 */
	@RequestMapping(value="/sendsms",method=RequestMethod.POST)
	public void sendsms(@RequestParam("mobile") String mobile ){
		System.out.println("controller mobile="+mobile);
		userService.sendSms(mobile);
//		return new Result(true,StatusCode.OK,"发送成功");  //这里不注掉的话会抛异常
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
	 * 他人根据ID查询
	 * @return
	 */
	@RequestMapping(value="/hisid/{hisid}",method= RequestMethod.GET)
	public Result findByhisId(@PathVariable String hisid){

		User user = userService.findById(hisid);
		user.setPassword(null);
		user.setUpdatedate(null);
		user.setRegdate(null);
		return new Result(true,StatusCode.OK,"查询成功",user);
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
		User user = userService.findById(userId);
		user.setPassword(null);
		return new Result(true,StatusCode.OK,"查询成功",user);
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
			String qqurl="tencent://message/?uin="+user.getMyqq()+"&Menu=yes&Service=300&sigT=42a1e5347953b64c5ff3980f8a6e644d4b31456cb0b6ac6b27663a3c4dd0f4aa14a543b1716f9d45";
			userDB.setQqurl(qqurl);
		}else if("school".equals(updateType)){ //修改学校
			userDB.setMyschool(user.getMyschool());
		}else if("professional".equals(updateType)){  //修改学院
			userDB.setMyprofessional(user.getMyprofessional());
		}else if ("grade".equals(updateType)){
			userDB.setMygrade(user.getMygrade());
		}else if ("personality".equals(updateType)){
			userDB.setPersonality(user.getPersonality());
			System.out.println("修改个人简介");
		}else if ("all".equals(updateType)){
//			userDB
		}
		System.out.println("正在修改");
		userService.update(userDB);
		//将密码置空返回出去
		BeanUtils.copyProperties(userDB,user);
		user.setPassword(null);
		System.out.println(user==userDB);

		return new Result(true,StatusCode.OK,"修改成功",user);
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
