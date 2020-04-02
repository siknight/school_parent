package com.school.article.service;


import com.school.article.dao.ArticleDao;
import com.school.article.dao.FavArticleDao;
import com.school.article.pojo.Article;
import com.school.article.pojo.ArticleAndUser;
import com.school.friend.pojo.Friendactivity;
import com.school.user.dao.UserDao;
import com.school.user.pojo.User;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
@SuppressWarnings("all")
public class ArticleService {

	@Autowired
	private ArticleDao articleDao;
	
	@Autowired
	private IdWorker idWorker;

	@Autowired
	private UserDao userDao;

	@Autowired private
	RedisTemplate redisTemplate;
	@Autowired
	private FavArticleDao favArticleDao;


	/**
	 * 查询全部列表通过用户id
	 * @return
	 */
	public List<Article> findAllByUserid(String userid) {
		return articleDao.findByUserid(userid);
	}

	/**
	 * 首页column文章遍历,包含有用户信息
	 * @return
	 */
	public List<ArticleAndUser> findIndexColumnArticles(String id){
		List<Article> all = articleDao.findByColumnid(id);
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


	/**
	 * 首页文章遍历,包含有用户信息
	 * @return
	 */
	public List<ArticleAndUser> findIndexArticles(){
		Sort sort = new Sort(Sort.Direction.DESC,"updatetime");
		List<Article> all = articleDao.findAll(sort);
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


	/**
	 * 首页    搜索                文章遍历,包含有用户信息
	 * @return
	 */
	public List<ArticleAndUser> findSearchIndexArticles(String searchContent){
		List<Article> all = articleDao.findByTitleLikeOrContentLike("%"+searchContent+"%","%"+searchContent+"%");
		System.out.println("all="+all);
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


	/**
	 * 文章详情页
	 * @param id
	 * @return
	 */
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
//		//删除收藏全部通过文章id
//		favArticleDao.deleteAllByArticleid(id);

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

                // 标题
                if (searchMap.get("title")!=null && !"".equals(searchMap.get("title"))) {
                	predicateList.add(cb.like(root.get("title").as(String.class), "%"+(String)searchMap.get("title")+"%"));
                }
                // 文章正文
				 if (searchMap.get("content")!=null && !"".equals(searchMap.get("content"))) {
                	predicateList.add(cb.like(root.get("content").as(String.class), "%"+(String)searchMap.get("content")+"%"));
                }

				
				return cb.and( predicateList.toArray(new Predicate[predicateList.size()]));

			}
		};

	}

}
