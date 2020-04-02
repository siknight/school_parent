package com.school.article.controller;


import com.school.article.pojo.Article;
import com.school.article.service.ArticleService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 控制器层
 * @author Administrator
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/article")
public class ArticleController {

	@Autowired
	private ArticleService articleService;
	//用于验证token
	@Autowired
	private HttpServletRequest request;

	@Autowired
	private RedisTemplate redisTemplate;

	/**
	 * 查找用户发布的交友活动
	 * @return
	 */
	@RequestMapping(value = "/hisid/{hisid}",method= RequestMethod.GET)
	public Result findFriendActivityByhisid(@PathVariable String hisid){
		return  new Result(true,StatusCode.OK,"查询成功",articleService.findAllByUserid(hisid));
	}


	/**
	 * 查找用户发布的交友活动
	 * @return
	 */
	@RequestMapping(value = "/all/userid",method= RequestMethod.GET)
	public Result findFriendActivityByUserid(){
		//判断是否有权限访问
		Claims claims=(Claims) request.getAttribute("user_claims");
		if(claims==null){
			return new Result(true,StatusCode.ACCESSERROR,"无权访问");
		}
		System.out.println("acall="+claims.getId());
		return  new Result(true,StatusCode.OK,"查询成功",articleService.findAllByUserid(claims.getId()));
	}

	/**
	 * 查找用户发布的交友活动
	 * @return
	 */
	@RequestMapping(value = "/column/{columnid}",method= RequestMethod.GET)
	public Result findAllByColumnid(@PathVariable String columnid){


		return  new Result(true,StatusCode.OK,"查询成功",articleService.findIndexColumnArticles(columnid));
	}



	@RequestMapping(value = "/findDetailArticle/{articleId}",method= RequestMethod.GET)
	public Result finddetailArticlesById(@PathVariable("articleId") String articleId){
		return new Result(true,StatusCode.OK,"查询成功",articleService.finddetailArticleById(articleId));
	}

	@RequestMapping(value = "/findIndexArticle",method= RequestMethod.GET)
	public Result findIndexArticles(){
		System.out.println("findIndexArticles 执行了");
		return new Result(true,StatusCode.OK,"查询成功",articleService.findIndexArticles());
	}

	/**
	 * 查询全部数据
	 * @return
	 */
	@RequestMapping(method= RequestMethod.GET)
	public Result findAll(){
		return new Result(true,StatusCode.OK,"查询成功",articleService.findAll());
	}
	
	/**
	 * 根据ID查询
	 * @param id ID
	 * @return
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.GET)
	public Result findById(@PathVariable String id){
		return new Result(true,StatusCode.OK,"查询成功",articleService.findById(id));
	}

	/**
	 * 查询
	 * @return 分页结果
	 */
	@RequestMapping(value="/search/{searchContent}",method=RequestMethod.GET)
	public Result findSearch(@PathVariable String searchContent){
		System.out.println("searchContent="+searchContent);
//		System.out.println("search result"+articleService.findSearchIndexArticles(searchContent));
		return  new Result(true,StatusCode.OK,"查询成功",
				articleService.findSearchIndexArticles(searchContent));
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
		Page<Article> pageList = articleService.findSearch(searchMap, page, size);
		return  new Result(true,StatusCode.OK,"查询成功",  new PageResult<Article>(pageList.getTotalElements(), pageList.getContent()) );
	}

	/**
     * 根据条件查询
     * @param searchMap
     * @return
     */
    @RequestMapping(value="/search",method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap){
        return new Result(true,StatusCode.OK,"查询成功",articleService.findSearch(searchMap));
    }
	
	/**
	 * 增加
	 * @param article
	 */
	@RequestMapping(method=RequestMethod.POST)
	public Result add(@RequestBody Article article ){
		//判断是否有权限访问
		Claims claims=(Claims) request.getAttribute("user_claims");
		if(claims==null){
			return new Result(true,StatusCode.ACCESSERROR,"无权访问");
		}
		//claims.getId()是为了获取token里存的用户id
		article.setUserid(claims.getId());
		System.out.println(article);
		articleService.add(article);
		return new Result(true,StatusCode.OK,"增加成功");
	}


	/**
	 * 吐槽点赞
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/thumbup", method = RequestMethod.POST)
	public Result updateThumbup( String id){
		System.out.println("updateThumbup...1");
		//判断是否有权限访问
		Claims claims=(Claims) request.getAttribute("user_claims");

		System.out.println("claims11="+claims);
		if(claims==null){
			return new Result(true,StatusCode.ACCESSERROR,"无权访问");
		}
		String userid = claims.getId();
		System.out.println("updateThumbup...2");
		if(redisTemplate.opsForValue().get("thumbup_article"+userid+"_"+ id)!=null){
			return new Result(false,StatusCode.REPERROR,"你已经点过赞了");
		}
		articleService.addThumbup(id);
		redisTemplate.opsForValue().set( "thumbup_article"+userid+"_"+ id,"1");
		System.out.println("updateThumbup...3");
		return  new Result(true,StatusCode.OK,"点赞成功");
	}


	/**
	 * 修改
	 * @param article
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.PUT)
	public Result update(@RequestBody Article article, @PathVariable String id ){
		article.setId(id);
		articleService.update(article);		
		return new Result(true,StatusCode.OK,"修改成功");
	}
	
	/**
	 * 删除
	 * @param id
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.DELETE)
	public Result delete(@PathVariable String id ){
		articleService.deleteById(id);
		return new Result(true,StatusCode.OK,"删除成功");
	}

	/**
	 * 审核
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/examine/{id}",method=RequestMethod.PUT)
	public Result examine(@PathVariable String id){
		articleService.examine(id);
		return new Result(true, StatusCode.OK, "审核成功！");
	}

	
}
