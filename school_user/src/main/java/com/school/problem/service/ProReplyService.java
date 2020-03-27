package com.school.problem.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;


import com.school.article.pojo.ArticleReply;
import com.school.article.pojo.ArticleReplyUser;
import com.school.problem.dao.ProReplyDao;
import com.school.problem.pojo.ProReplyUser;
import com.school.problem.pojo.ProblemReply;
import com.school.user.dao.UserDao;
import com.school.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import util.IdWorker;



/**
 * 服务层
 * 
 * @author Administrator
 *
 */
@Service
@SuppressWarnings("all")
public class ProReplyService {

	@Autowired
	private ProReplyDao replyDao;
	
	@Autowired
	private IdWorker idWorker;

	@Autowired
	private UserDao userDao;

	public List<ProReplyUser> findAllReplyByProblemid(String problemid) {
		List<ProblemReply> allByArticleid = replyDao.findByProblemid(problemid); //通过问题id获取到评论id
		ArrayList<ProReplyUser> allProReplyUsers = new ArrayList<>();
		for (ProblemReply problemReply : allByArticleid) {
			String userid = problemReply.getUserid();  //通过评论id，获取到用户id
			System.out.println("userid=" + userid);
			User user = userDao.findUserById(userid);
			user.setPassword(null);
			ProReplyUser proReplyUser = new ProReplyUser();
			proReplyUser.setUser(user);
			proReplyUser.setProblemReply(problemReply);
			allProReplyUsers.add(proReplyUser);
		}
		return  allProReplyUsers;
	}

	/**
	 * 查询全部列表
	 * @return
	 */
	public List<ProblemReply> findAll() {
		return replyDao.findAll();
	}

	
	/**
	 * 条件查询+分页
	 * @param whereMap
	 * @param page
	 * @param size
	 * @return
	 */
	public Page<ProblemReply> findSearch(Map whereMap, int page, int size) {
		Specification<ProblemReply> specification = createSpecification(whereMap);
		PageRequest pageRequest =  PageRequest.of(page-1, size);
		return replyDao.findAll(specification, pageRequest);
	}

	
	/**
	 * 条件查询
	 * @param whereMap
	 * @return
	 */
	public List<ProblemReply> findSearch(Map whereMap) {
		Specification<ProblemReply> specification = createSpecification(whereMap);
		return replyDao.findAll(specification);
	}

	/**
	 * 根据ID查询实体
	 * @param id
	 * @return
	 */
	public ProblemReply findById(String id) {
		return replyDao.findById(id).get();
	}

	/**
	 * 增加
	 * @param reply
	 */
	public void add(ProblemReply reply) {
		reply.setId( idWorker.nextId()+"" );
		replyDao.save(reply);
	}

	/**
	 * 修改
	 * @param reply
	 */
	public void update(ProblemReply reply) {
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
	private Specification<ProblemReply> createSpecification(Map searchMap) {

		return new Specification<ProblemReply>() {

			@Override
			public Predicate toPredicate(Root<ProblemReply> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
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
