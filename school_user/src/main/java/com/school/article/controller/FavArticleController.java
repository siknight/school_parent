package com.school.article.controller;


import com.school.article.pojo.FavArticle;
import com.school.article.service.FavArticleService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 控制器层
 * @author Administrator
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/favArticle")
public class FavArticleController {

	@Autowired
	private FavArticleService favService;

	//用于验证token
	@Autowired
	private HttpServletRequest request;

	/**
	 * 根据文章ID查询
	 * @param
	 * @return
	 */
	@RequestMapping(value="/favcount/{favid}",method= RequestMethod.GET)
	public Result countByArticleid(@PathVariable String favid){
		return new Result(true,StatusCode.OK,"查询成功",favService.countByArticleid(favid));
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
	 * 查询全部收藏数据
	 * @return
	 */
	@RequestMapping(value = "/findfav",method= RequestMethod.GET)
	public Result findAllfavArticlesByUserid(){
		//判断是否有权限访问
		Claims claims=(Claims) request.getAttribute("user_claims");
		System.out.println("claims11="+claims);
		if(claims==null){
			return new Result(true,StatusCode.ACCESSERROR,"无权访问");
		}
//		System.out.println("fav article="+favService.findAllfavArticlesByUserid(claims.getId()));
		return new Result(true,StatusCode.OK,"查询成功",
				favService.findAllfavArticlesByUserid(claims.getId()));
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
	 * 分页+多条件查询
	 * @param searchMap 查询条件封装
	 * @param page 页码
	 * @param size 页大小
	 * @return 分页结果
	 */
	@RequestMapping(value="/search/{page}/{size}",method=RequestMethod.POST)
	public Result findSearch(@RequestBody Map searchMap , @PathVariable int page, @PathVariable int size){
		Page<FavArticle> pageList = favService.findSearch(searchMap, page, size);
		return  new Result(true,StatusCode.OK,"查询成功",  new PageResult<FavArticle>(pageList.getTotalElements(), pageList.getContent()) );
	}

	/**
     * 根据条件查询
     * @param searchMap
     * @return
     */
    @RequestMapping(value="/search",method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap){
        return new Result(true,StatusCode.OK,"查询成功",favService.findSearch(searchMap));
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

		int fav = favService.findByUseridAndArticleid(claims.getId(),favid);
		System.out.println("fav="+fav);
		if (fav>0){
			return new Result(true,StatusCode.REPERROR,"你已经收藏过了");
		}
		FavArticle favArticle = new FavArticle();
		favArticle.setUserid(claims.getId());
		favArticle.setArticleid(favid);
		favService.add(favArticle);
		return new Result(true,StatusCode.OK,"增加成功");
	}
	
	/**
	 * 修改
	 * @param channel
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.PUT)
	public Result update(@RequestBody FavArticle channel, @PathVariable String id ){
		channel.setId(id);
		favService.update(channel);
		return new Result(true,StatusCode.OK,"修改成功");
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
