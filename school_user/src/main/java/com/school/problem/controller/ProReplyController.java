package com.school.problem.controller;
import java.util.List;
import java.util.Map;

import com.school.problem.pojo.ProblemReply;
import com.school.problem.service.ProReplyService;
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

import javax.servlet.http.HttpServletRequest;

/**
 * 控制器层
 * @author Administrator
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/proreply")
public class ProReplyController {

	@Autowired
	private ProReplyService replyService;

	@Autowired
	private HttpServletRequest request;


	/**
	 * 通过文章id查询所有评论
	 * @param problemid
	 * @return
	 */
	@RequestMapping(value="/problem/{problemid}",method= RequestMethod.GET)
	public Result findAllByArticleid(@PathVariable String problemid){
		System.out.println("articleid="+problemid);
		return new Result(true,StatusCode.OK,"查询成功",replyService.findAllReplyByProblemid(problemid));
	}


	/**
	 * 查询全部数据
	 * @return
	 */
	@RequestMapping(method= RequestMethod.GET)
	public Result findAll(){
		return new Result(true,StatusCode.OK,"查询成功",replyService.findAll());
	}
	
	/**
	 * 根据ID查询
	 * @param id ID
	 * @return
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.GET)
	public Result findById(@PathVariable String id){
		return new Result(true,StatusCode.OK,"查询成功",replyService.findById(id));
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
		Page<ProblemReply> pageList = replyService.findSearch(searchMap, page, size);
		return  new Result(true,StatusCode.OK,"查询成功",  new PageResult<ProblemReply>(pageList.getTotalElements(), pageList.getContent()) );
	}

	/**
     * 根据条件查询
     * @param searchMap
     * @return
     */
    @RequestMapping(value="/search",method = RequestMethod.POST)
    public Result findSearch( @RequestBody Map searchMap){
        return new Result(true,StatusCode.OK,"查询成功",replyService.findSearch(searchMap));
    }
	
	/**
	 * 增加
	 * @param reply
	 */
	@RequestMapping(method=RequestMethod.POST)
	public Result add(@RequestBody ProblemReply reply  ){
		//判断是否有权限访问
		Claims claims=(Claims) request.getAttribute("user_claims");
		if(claims==null){
			return new Result(true,StatusCode.ACCESSERROR,"无权访问");
		}
		//claims.getId()是为了获取token里存的用户id
		reply.setUserid(claims.getId());
		replyService.add(reply);
		return new Result(true,StatusCode.OK,"增加成功");
	}
	
	/**
	 * 修改
	 * @param reply
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.PUT)
	public Result update(@RequestBody ProblemReply reply, @PathVariable String id ){
		reply.setId(id);
		replyService.update(reply);		
		return new Result(true,StatusCode.OK,"修改成功");
	}
	
	/**
	 * 删除
	 * @param id
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.DELETE)
	public Result delete(@PathVariable String id ){
		replyService.deleteById(id);
		return new Result(true,StatusCode.OK,"删除成功");
	}
	
}
