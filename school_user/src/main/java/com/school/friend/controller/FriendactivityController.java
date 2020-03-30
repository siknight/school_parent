package com.school.friend.controller;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.school.friend.pojo.Friendactivity;
import com.school.friend.service.FriendactivityService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import util.DateFormatUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * 控制器层
 * @author Administrator
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/friendactivity")
public class FriendactivityController {

	@Autowired
	private FriendactivityService friendactivityService;

	//用于验证token
	@Autowired
	private HttpServletRequest request;

	@RequestMapping(value = "/hisid/{hisid}",method= RequestMethod.GET)
	public Result findFriendActivityByhisid(String hisid){
		//判断是否有权限访问
		System.out.println("fa hisid");
		return  new Result(true,StatusCode.OK,"查询成功",friendactivityService.findAllByUserid(hisid));
	}


	@RequestMapping(value = "/all/userid",method= RequestMethod.GET)
	public Result findFriendActivityByUserid(){
		//判断是否有权限访问
		Claims claims=(Claims) request.getAttribute("user_claims");
		if(claims==null){
			return new Result(true,StatusCode.ACCESSERROR,"无权访问");
		}
		System.out.println("acall="+claims.getId());
		return  new Result(true,StatusCode.OK,"查询成功",friendactivityService.findAllByUserid(claims.getId()));
	}

	@RequestMapping(value = "/all/ufa",method= RequestMethod.GET)
	public Result findAllUserFriendActivity(){

		return  new Result(true,StatusCode.OK,"查询成功",friendactivityService.findAllUserFriendActivity());
	}

	/**
	 * 查询全部数据
	 * @return
	 */
	@RequestMapping(method= RequestMethod.GET)
	public Result findAll(){
		return new Result(true,StatusCode.OK,"查询成功",friendactivityService.findAll());
	}
	
	/**
	 * 根据ID查询
	 * @param id ID
	 * @return
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.GET)
	public Result findById(@PathVariable String id){
		return new Result(true,StatusCode.OK,"查询成功",friendactivityService.findById(id));
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
		Page<Friendactivity> pageList = friendactivityService.findSearch(searchMap, page, size);
		return  new Result(true,StatusCode.OK,"查询成功",  new PageResult<Friendactivity>(pageList.getTotalElements(), pageList.getContent()) );
	}

	/**
     * 根据条件查询
     * @param searchMap
     * @return
     */
    @RequestMapping(value="/search",method = RequestMethod.POST)
    public Result findSearch( @RequestBody Map searchMap){
        return new Result(true,StatusCode.OK,"查询成功",friendactivityService.findSearch(searchMap));
    }
	
	/**
	 * 增加
	 * @param friendactivity
	 */
	@RequestMapping(method=RequestMethod.POST)
	public Result add(@RequestBody Friendactivity friendactivity  ){
		//判断是否有权限访问
		Claims claims=(Claims) request.getAttribute("user_claims");
		if(claims==null){
			return new Result(true,StatusCode.ACCESSERROR,"无权访问");
		}
		System.out.println("friactivity="+friendactivity.getActivityname());
		friendactivity.setUserid(claims.getId());
		friendactivity.setCreatetime(DateFormatUtil.DateToString(new Date()));
		friendactivity.setUpdatetime(new Date());
		System.out.println("fa="+friendactivity);
		friendactivityService.add(friendactivity);
		return new Result(true,StatusCode.OK,"增加成功");
	}
	
	/**
	 * 修改
	 * @param friendactivity
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.PUT)
	public Result update(@RequestBody Friendactivity friendactivity, @PathVariable String id ){
		friendactivity.setId(id);
		friendactivityService.update(friendactivity);		
		return new Result(true,StatusCode.OK,"修改成功");
	}
	
	/**
	 * 删除
	 * @param id
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.DELETE)
	public Result delete(@PathVariable String id ){
		//判断是否有权限访问
		Claims claims=(Claims) request.getAttribute("user_claims");
		if(claims==null){
			return new Result(true,StatusCode.ACCESSERROR,"无权访问");
		}
		friendactivityService.deleteById(id);
		return new Result(true,StatusCode.OK,"删除成功");
	}
	
}
