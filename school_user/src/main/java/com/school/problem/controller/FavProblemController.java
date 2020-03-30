package com.school.problem.controller;



import com.school.problem.pojo.FavProblem;
import com.school.problem.service.FavProblemService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 控制器层
 * @author Administrator
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/favProblem")
public class FavProblemController {

	@Autowired
	private FavProblemService favService;

	//用于验证token
	@Autowired
	private HttpServletRequest request;

	/**
	 * 根据问题ID查询
	 * @param
	 * @return
	 */
	@RequestMapping(value="/cancelall/{proid}",method= RequestMethod.DELETE)
	public Result cancelByProid(@PathVariable String proid){
		System.out.println("cancelall");
		//判断是否有权限访问
		Claims claims=(Claims) request.getAttribute("user_claims");
		System.out.println("claims11="+claims);
		if(claims==null){
			return new Result(true,StatusCode.ACCESSERROR,"无权访问");
		}
		favService.deleteByProidAndUserid(proid,claims.getId());
		return new Result(true,StatusCode.OK,"取消成功");
	}



	/**
	 * 查询全部收藏数据
	 * @return
	 */
	@RequestMapping(value = "/findfav",method= RequestMethod.GET)
	public Result findAllfavProblemsByUserid(){
		//判断是否有权限访问
		Claims claims=(Claims) request.getAttribute("user_claims");
		System.out.println("claims11="+claims);
		if(claims==null){
			return new Result(true,StatusCode.ACCESSERROR,"无权访问");
		}
//		System.out.println("fav article="+favService.findAllfavProblemsByUserid(claims.getId()));
		return new Result(true,StatusCode.OK,"查询成功",
				favService.findAllfavProblemsByUserid(claims.getId()));
	}


	/**
	 * 根据文章ID查询
	 * @param
	 * @return
	 */
	@RequestMapping(value="/favcount/{favid}",method= RequestMethod.GET)
	public Result countByProblemid(@PathVariable String favid){
		return new Result(true,StatusCode.OK,"查询成功",favService.countByProblemid(favid));
	}
	/**
	 * 查询全部数据
	 * @return
	 */
	@RequestMapping(method= RequestMethod.GET)
	public Result findAll(){
		return new Result(true,StatusCode.OK,"查询成功",favService.findAll());
	}
	
	/**
	 * 根据ID查询
	 * @param id ID
	 * @return
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.GET)
	public Result findById(@PathVariable String id){
		return new Result(true,StatusCode.OK,"查询成功",favService.findById(id));
	}




	
	/**
	 * 收藏
	 * @param
	 */
	@RequestMapping(value = "/fav",method=RequestMethod.POST)
	public Result addFav(String favid){
		//判断是否有权限访问
		Claims claims=(Claims) request.getAttribute("user_claims");
		System.out.println("claims11="+claims);
		if(claims==null){
			return new Result(true,StatusCode.ACCESSERROR,"无权访问");
		}

		int fav = favService.findByUseridAndProblemid(claims.getId(),favid);
		System.out.println("fav="+fav);
		if (fav>0){
			return new Result(true,StatusCode.REPERROR,"你已经收藏过了");
		}
		FavProblem favProblem = new FavProblem();
		favProblem.setUserid(claims.getId());
		favProblem.setProblemid(favid);
		favService.add(favProblem);
		return new Result(true,StatusCode.OK,"增加成功");
	}
	

	
	/**
	 * 删除
	 * @param id
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.DELETE)
	public Result delete(@PathVariable String id ){
		favService.deleteById(id);
		return new Result(true,StatusCode.OK,"删除成功");
	}
	
}
