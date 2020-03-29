package com.school.article.service;


import com.school.article.dao.ArticleDao;
import com.school.article.pojo.Article;
import com.school.article.pojo.ArticleAndUser;
import com.school.friend.pojo.Friendactivity;
import com.school.user.dao.UserDao;
import com.school.user.pojo.User;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import util.DateFormatUtil;
import util.IdWorker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 服务层
 * 
 * @author Administrator
 *
 */
@Service
public class ArticleService {

	@Autowired
	private ArticleDao articleDao;
	
	@Autowired
	private IdWorker idWorker;

	@Autowired
	private UserDao userDao;

	@Autowired private
	RedisTemplate redisTemplate;


	/**
	 * 查询全部列表通过用户id
	 * @return
	 */
	public List<Article> findAllByUserid(String userid) {
		return articleDao.findByUserid(userid);
	}

	/**
	 * 首页文章遍历
	 * @return
	 */
	public List<ArticleAndUser> findIndexArticles(){
		List<Article> all = articleDao.findAll();
		ArrayList<ArticleAndUser> articleAndUsers = new ArrayList<>();
		for (Article article:all){
			String userid = article.getUserid();
			User user = userDao.findById(userid).get();
			user.setPassword(null);
			user.setBirthday(null);
			ArticleAndUser articleAndUser = new ArticleAndUser();
			articleAndUser.setArticle(article);
			articleAndUser.setUser(user);
			articleAndUsers.add(articleAndUser);
			articleAndUser.setTime(DateFormatUtil.DateToString(article.getCreatetime()));
		}
		return articleAndUsers;
	}

	public ArticleAndUser finddetailArticleById(String id){
		Article article = articleDao.findById(id).get();
		String userid = article.getUserid();
		User user = userDao.findById(userid).get();
		user.setPassword(null);

		ArticleAndUser articleAndUser = new ArticleAndUser();
		articleAndUser.setUser(user);
		articleAndUser.setArticle(article);

		return articleAndUser;
	}

	/**
	 * 查询全部列表
	 * @return
	 */
	public List<Article> findAll() {
		return articleDao.findAll();
	}

	
	/**
	 * 条件查询+分页
	 * @param whereMap
	 * @param page
	 * @param size
	 * @return
	 */
	public Page<Article> findSearch(Map whereMap, int page, int size) {
		Specification<Article> specification = createSpecification(whereMap);
		PageRequest pageRequest =  PageRequest.of(page-1, size);
		return articleDao.findAll(specification, pageRequest);
	}

	
	/**
	 * 条件查询
	 * @param whereMap
	 * @return
	 */
	public List<Article> findSearch(Map whereMap) {
		Specification<Article> specification = createSpecification(whereMap);
		return articleDao.findAll(specification);
	}

	/**
	 * 根据ID查询实体
	 * @param id
	 * @return
	 */
	public Article findById(String id) {
		//从缓存中提取
		Article article= (Article)redisTemplate.opsForValue().get("article_"+id);
		// 如果缓存没有则到数据库查询并放入缓存
		if(article==null){
			article = articleDao.findById(id).get();
			redisTemplate.opsForValue().set("article_"+id,article,1,TimeUnit.DAYS);
		}
		return article;
	}

	/**
	 * 增加
	 * @param article
	 */
	public void add(Article article) {
		article.setId( idWorker.nextId()+"" );
		article.setThumbup(0); //点赞数
		article.setVisits(0);//点赞数
		article.setComment(0);//评论数
		article.setUrl("boke-detail.html?articleId="+article.getId());
		article.setCreatetime(new Date());
		article.setTime(DateFormatUtil.DateToString(new Date()));
		article.setUpdatetime(new Date());
		System.out.println("正在保存文章");
		articleDao.save(article);
	}

	/**
	 * 添加点赞
	 * @param articleid
	 */
	@Transactional
	public void addThumbup(String articleid) {
		System.out.println("addThump article service");
		articleDao.updateThumbup(articleid);
	}
	/**
	 * 修改
	 * @param article
	 */
	public void update(Article article) {
		redisTemplate.delete( "article_" + article.getId() );//删除缓存
		articleDao.save(article);
	}

	/**
	 * 删除
	 * @param id
	 */
	public void deleteById(String id) {
		redisTemplate.delete( "article_" + id );//删除缓存
		articleDao.deleteById(id);
	}

	/**
	 * 文章审核
	 * @param id
	 */
	@Transactional
	public void examine(String id){
		articleDao.examine(id);
	}

	/**
	 * 文章点赞
	 * @param id
	 * @return
	 */
	@Transactional
	public int updateThumbup(String id){
		return articleDao.updateThumbup(id);
	}

	/**
	 * 动态条件构建
	 * @param searchMap
	 * @return
	 */
	private Specification<Article> createSpecification(Map searchMap) {

		return new Specification<Article>() {

			@Override
			public Predicate toPredicate(Root<Article> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicateList = new ArrayList<Predicate>();
                // ID
                if (searchMap.get("id")!=null && !"".equals(searchMap.get("id"))) {
                	predicateList.add(cb.like(root.get("id").as(String.class), "%"+(String)searchMap.get("id")+"%"));
                }
                // 专栏ID
                if (searchMap.get("columnid")!=null && !"".equals(searchMap.get("columnid"))) {
                	predicateList.add(cb.like(root.get("columnid").as(String.class), "%"+(String)searchMap.get("columnid")+"%"));
                }
                // 用户ID
                if (searchMap.get("userid")!=null && !"".equals(searchMap.get("userid"))) {
                	predicateList.add(cb.like(root.get("userid").as(String.class), "%"+(String)searchMap.get("userid")+"%"));
                }
                // 标题
                if (searchMap.get("title")!=null && !"".equals(searchMap.get("title"))) {
                	predicateList.add(cb.like(root.get("title").as(String.class), "%"+(String)searchMap.get("title")+"%"));
                }
                // 文章正文
                if (searchMap.get("content")!=null && !"".equals(searchMap.get("content"))) {
                	predicateList.add(cb.like(root.get("content").as(String.class), "%"+(String)searchMap.get("content")+"%"));
                }
                // 文章封面
                if (searchMap.get("image")!=null && !"".equals(searchMap.get("image"))) {
                	predicateList.add(cb.like(root.get("image").as(String.class), "%"+(String)searchMap.get("image")+"%"));
                }
                // 是否公开
                if (searchMap.get("ispublic")!=null && !"".equals(searchMap.get("ispublic"))) {
                	predicateList.add(cb.like(root.get("ispublic").as(String.class), "%"+(String)searchMap.get("ispublic")+"%"));
                }
                // 是否置顶
                if (searchMap.get("istop")!=null && !"".equals(searchMap.get("istop"))) {
                	predicateList.add(cb.like(root.get("istop").as(String.class), "%"+(String)searchMap.get("istop")+"%"));
                }
                // 审核状态
                if (searchMap.get("state")!=null && !"".equals(searchMap.get("state"))) {
                	predicateList.add(cb.like(root.get("state").as(String.class), "%"+(String)searchMap.get("state")+"%"));
                }
                // 所属频道
                if (searchMap.get("channelid")!=null && !"".equals(searchMap.get("channelid"))) {
                	predicateList.add(cb.like(root.get("channelid").as(String.class), "%"+(String)searchMap.get("channelid")+"%"));
                }
                // URL
                if (searchMap.get("url")!=null && !"".equals(searchMap.get("url"))) {
                	predicateList.add(cb.like(root.get("url").as(String.class), "%"+(String)searchMap.get("url")+"%"));
                }
                // 类型
                if (searchMap.get("type")!=null && !"".equals(searchMap.get("type"))) {
                	predicateList.add(cb.like(root.get("type").as(String.class), "%"+(String)searchMap.get("type")+"%"));
                }
				
				return cb.and( predicateList.toArray(new Predicate[predicateList.size()]));

			}
		};

	}

}
