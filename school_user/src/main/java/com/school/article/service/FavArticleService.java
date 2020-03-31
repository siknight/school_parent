package com.school.article.service;


import com.school.article.dao.ArticleDao;
import com.school.article.dao.FavArticleDao;
import com.school.article.pojo.Article;
import com.school.article.pojo.FavArticle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import util.IdWorker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 服务层
 * 
 * @author Administrator
 *
 */
@Service
public class FavArticleService {

	@Autowired
	private FavArticleDao favDao;
	
	@Autowired
	private IdWorker idWorker;
	@Autowired
	private ArticleDao articleDao;

	/**
	 * 取消收藏
	 */
	@Transactional
	public void deleteByarticleidAndUserid(String articleid ,String userid){
		favDao.deleteByArticleidAndUserid(articleid,userid);
	}
	/**
	 * 查找收藏
	 * @param userid
	 * @param articleid
	 * @return
	 */
	public int findByUseridAndArticleid(String userid,String articleid){
		return  favDao.countByUseridAndArticleid(userid,articleid);
	}

	public int countByArticleid(String articleid){
		return  favDao.countByArticleid(articleid);
	}

	/**
	 * 查找收藏的文章
	 * @param userid
	 * @return
	 */
	public List<Article> findAllfavArticlesByUserid(String userid){
		//获取该用户收藏的所有记录
		List<FavArticle> favArticles = favDao.findByUserid(userid);
		//用于存储查询出来的文章
		List<Article> articles = new ArrayList<>();
		for (FavArticle favArticle:favArticles){
			//获取每个用户收藏的文章id
			String articleid = favArticle.getArticleid();
			System.out.println("articleid="+articleid);
			//通过该文章id查找
			Article article =articleDao.findArticleById(articleid);
			System.out.println("service article="+article);
			if (article!=null){
				articles.add(article);
			}

		}
		return  articles;
	}

	/**
	 * 查询全部列表
	 * @return
	 */
	public List<FavArticle> findAll() {
		return favDao.findAll();
	}

	
	/**
	 * 条件查询+分页
	 * @param whereMap
	 * @param page
	 * @param size
	 * @return
	 */
	public Page<FavArticle> findSearch(Map whereMap, int page, int size) {
		Specification<FavArticle> specification = createSpecification(whereMap);
		PageRequest pageRequest =  PageRequest.of(page-1, size);
		return favDao.findAll(specification, pageRequest);
	}

	
	/**
	 * 条件查询
	 * @param whereMap
	 * @return
	 */
	public List<FavArticle> findSearch(Map whereMap) {
		Specification<FavArticle> specification = createSpecification(whereMap);
		return favDao.findAll(specification);
	}

	/**
	 * 根据ID查询实体
	 * @param id
	 * @return
	 */
	public FavArticle findById(String id) {
		return favDao.findById(id).get();
	}

	/**
	 * 增加
	 * @param channel
	 */
	public void add(FavArticle channel) {
		channel.setId( idWorker.nextId()+"" );
		favDao.save(channel);
	}

	/**
	 * 修改
	 * @param channel
	 */
	public void update(FavArticle channel) {
		favDao.save(channel);
	}

	/**
	 * 删除
	 * @param id
	 */
	public void deleteById(String id) {
		favDao.deleteById(id);
	}

	/**
	 * 动态条件构建
	 * @param searchMap
	 * @return
	 */
	private Specification<FavArticle> createSpecification(Map searchMap) {

		return new Specification<FavArticle>() {

			@Override
			public Predicate toPredicate(Root<FavArticle> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicateList = new ArrayList<Predicate>();
                // ID
                if (searchMap.get("id")!=null && !"".equals(searchMap.get("id"))) {
                	predicateList.add(cb.like(root.get("id").as(String.class), "%"+(String)searchMap.get("id")+"%"));
                }
                // 频道名称
                if (searchMap.get("name")!=null && !"".equals(searchMap.get("name"))) {
                	predicateList.add(cb.like(root.get("name").as(String.class), "%"+(String)searchMap.get("name")+"%"));
                }
                // 状态
                if (searchMap.get("state")!=null && !"".equals(searchMap.get("state"))) {
                	predicateList.add(cb.like(root.get("state").as(String.class), "%"+(String)searchMap.get("state")+"%"));
                }
				
				return cb.and( predicateList.toArray(new Predicate[predicateList.size()]));

			}
		};

	}

}
