package com.school.article.service;


import com.school.article.dao.ReplyDao;
import com.school.article.pojo.ArticleReply;
import com.school.article.pojo.ArticleReplyUser;
import com.school.user.dao.UserDao;
import com.school.user.pojo.User;
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
import java.util.*;

/**
 * 服务层
 * 
 * @author Administrator
 *
 */
@Service
public class ReplyService {

	@Autowired
	private ReplyDao replyDao;
	
	@Autowired
	private IdWorker idWorker;
	@Autowired
	private UserDao userDao;

	public List<ArticleReplyUser> findAllReplyByArticleid(String articleid) {
		List<ArticleReply> allByArticleid = replyDao.findByArticleid(articleid); //通过文章id获取到评论id
		ArrayList<ArticleReplyUser> articleReplyUsers = new ArrayList<>();
		for (ArticleReply articleReply :allByArticleid){
			String userid = articleReply.getUserid();  //通过评论id，获取到用户id
			System.out.println("userid="+userid);
			User user = userDao.findUserById(userid);
			user.setPassword(null);
			ArticleReplyUser articleReplyUser = new ArticleReplyUser();
			articleReplyUser.setArticleReply(articleReply);
			articleReplyUser.setUser(user);
			articleReplyUsers.add(articleReplyUser);
		}
		return articleReplyUsers;
	}

	/**
	 * 查询全部列表
	 * @return
	 */
	public List<ArticleReply> findAll() {
		return replyDao.findAll();
	}

	
	/**
	 * 条件查询+分页
	 * @param whereMap
	 * @param page
	 * @param size
	 * @return
	 */
	public Page<ArticleReply> findSearch(Map whereMap, int page, int size) {
		Specification<ArticleReply> specification = createSpecification(whereMap);
		PageRequest pageRequest =  PageRequest.of(page-1, size);
		return replyDao.findAll(specification, pageRequest);
	}

	
	/**
	 * 条件查询
	 * @param whereMap
	 * @return
	 */
	public List<ArticleReply> findSearch(Map whereMap) {
		Specification<ArticleReply> specification = createSpecification(whereMap);
		return replyDao.findAll(specification);
	}

	/**
	 * 根据ID查询实体
	 * @param id
	 * @return
	 */
	public ArticleReply findById(String id) {
		return replyDao.findById(id).get();
	}

	/**
	 * 增加
	 * @param reply
	 */
	public void add(ArticleReply reply) {
		reply.setId( idWorker.nextId()+"" );
		replyDao.save(reply);
	}

	/**
	 * 修改
	 * @param reply
	 */
	public void update(ArticleReply reply) {
		replyDao.save(reply);
	}

	/**
	 * 删除
	 * @param id
	 */
	public void deleteById(String id) {
		replyDao.deleteById(id);
	}




	/**
	 * 动态条件构建
	 * @param searchMap
	 * @return
	 */
	private Specification<ArticleReply> createSpecification(Map searchMap) {

		return new Specification<ArticleReply>() {

			@Override
			public Predicate toPredicate(Root<ArticleReply> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicateList = new ArrayList<Predicate>();
                // 编号
                if (searchMap.get("id")!=null && !"".equals(searchMap.get("id"))) {
                	predicateList.add(cb.like(root.get("id").as(String.class), "%"+(String)searchMap.get("id")+"%"));
                }
                // 问题ID
                if (searchMap.get("problemid")!=null && !"".equals(searchMap.get("problemid"))) {
                	predicateList.add(cb.like(root.get("problemid").as(String.class), "%"+(String)searchMap.get("problemid")+"%"));
                }
                // 回答内容
                if (searchMap.get("content")!=null && !"".equals(searchMap.get("content"))) {
                	predicateList.add(cb.like(root.get("content").as(String.class), "%"+(String)searchMap.get("content")+"%"));
                }
                // 回答人ID
                if (searchMap.get("userid")!=null && !"".equals(searchMap.get("userid"))) {
                	predicateList.add(cb.like(root.get("userid").as(String.class), "%"+(String)searchMap.get("userid")+"%"));
                }
                // 回答人昵称
                if (searchMap.get("nickname")!=null && !"".equals(searchMap.get("nickname"))) {
                	predicateList.add(cb.like(root.get("nickname").as(String.class), "%"+(String)searchMap.get("nickname")+"%"));
                }
				
				return cb.and( predicateList.toArray(new Predicate[predicateList.size()]));

			}
		};

	}

}
