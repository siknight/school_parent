package com.school.problem.service;

import java.util.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import com.school.article.pojo.Article;
import com.school.article.service.ReplyService;
import com.school.problem.dao.ProblemDao;
import com.school.problem.pojo.Problem;
import com.school.problem.pojo.ProblemReply;
import com.school.problem.pojo.ProblemUser;
import com.school.user.dao.UserDao;
import com.school.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import util.DateFormatUtil;
import util.IdWorker;


/**
 * 服务层
 * 
 * @author Administrator
 *
 */
@Service
@SuppressWarnings("all")
public class ProblemService {

	@Autowired
	private ProblemDao problemDao;
	
	@Autowired
	private IdWorker idWorker;

	@Autowired
	private UserDao userDao;



	/**
	 * 查询全部列表通过用户id
	 * @return
	 */
	public List<Problem> findAllByUserid(String userid) {
		return problemDao.findByUserid(userid);
	}

	@Autowired
	private ReplyService replyService;

	public ProblemUser findByProblemId(String id){
		Problem problem = problemDao.findById(id).get();
		String userid = problem.getUserid();
		User user = userDao.findUserById(userid);
		ProblemUser problemUser = new ProblemUser();
		problemUser.setProblem(problem);
		problemUser.setUser(user);
		return  problemUser;
	}

	/**
	 * 查询全部列表
	 * @return
	 */
	public List<Problem> findAll() {
		return problemDao.findAll();
	}

	/**
	 * 查询全部列表
	 * @return
	 */
	public List<ProblemUser> findAllProblemAndUser() {
		Sort sort = new Sort(Sort.Direction.DESC,"updatetime");
		List<Problem> allProblems = problemDao.findAll(sort);
		ArrayList<ProblemUser> problemUsers = new ArrayList<>();
		for (Problem problem : allProblems){
			String userid = problem.getUserid();
			User user = userDao.findById(userid).get();
			ProblemUser problemUser = new ProblemUser();
			problemUser.setUser(user);
			problemUser.setProblem(problem);
			problemUsers.add(problemUser);
		}
		return problemUsers;
	}

	/**
	 * 查询全部列表  搜索
	 * @return
	 */
	public List<ProblemUser> findAllSearchProblemAndUser(String searchContent) {
		List<Problem> allProblems = problemDao.findByTitleLike("%" + searchContent + "%");
//		List<Problem> allProblems = problemDao.findAll();
		ArrayList<ProblemUser> problemUsers = new ArrayList<>();
		for (Problem problem : allProblems){
			String userid = problem.getUserid();
			User user = userDao.findById(userid).get();
			ProblemUser problemUser = new ProblemUser();
			problemUser.setUser(user);
			problemUser.setProblem(problem);
			problemUsers.add(problemUser);
		}
		return problemUsers;
	}



	/**
	 * 条件查询+分页
	 * @param whereMap
	 * @param page
	 * @param size
	 * @return
	 */
	public Page<Problem> findSearch(Map whereMap, int page, int size) {
		Specification<Problem> specification = createSpecification(whereMap);
		PageRequest pageRequest =  PageRequest.of(page-1, size);
		return problemDao.findAll(specification, pageRequest);
	}

	
	/**
	 * 条件查询
	 * @param whereMap
	 * @return
	 */
	public List<Problem> findSearch(Map whereMap) {
		Specification<Problem> specification = createSpecification(whereMap);
		return problemDao.findAll(specification);
	}

	/**
	 * 根据ID查询实体
	 * @param id
	 * @return
	 */
	public Problem findById(String id) {
		return problemDao.findById(id).get();
	}

	/**
	 * 增加
	 * @param problem
	 */
	public void add(Problem problem) {
		 long idwork = idWorker.nextId();
		problem.setId( idwork+"" );
		problem.setCreatetime(DateFormatUtil.DateToString(new Date()));
		problem.setThumbup(0);
		problem.setUpdatetime(new Date());
		problem.setUrl("qa-detail.html?qaid="+idwork);
		problemDao.save(problem);
	}

	/**
	 * 增加点赞
	 * @param problemid
	 */
	@Transactional
	public void addThump(String problemid) {
		System.out.println("addThump service");
		problemDao.updateThumbup(problemid);
	}

	/**
	 * 修改
	 * @param problem
	 */
	public void update(Problem problem) {
		problemDao.save(problem);
	}

	/**
	 * 删除
	 * @param id
	 */
	public void deleteById(String id) {
		problemDao.deleteById(id);
	}

	/**
	 * 动态条件构建
	 * @param searchMap
	 * @return
	 */
	private Specification<Problem> createSpecification(Map searchMap) {

		return new Specification<Problem>() {

			@Override
			public Predicate toPredicate(Root<Problem> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicateList = new ArrayList<Predicate>();
                // ID
                if (searchMap.get("id")!=null && !"".equals(searchMap.get("id"))) {
                	predicateList.add(cb.like(root.get("id").as(String.class), "%"+(String)searchMap.get("id")+"%"));
                }
                // 标题
                if (searchMap.get("title")!=null && !"".equals(searchMap.get("title"))) {
                	predicateList.add(cb.like(root.get("title").as(String.class), "%"+(String)searchMap.get("title")+"%"));
                }
                // 内容
                if (searchMap.get("content")!=null && !"".equals(searchMap.get("content"))) {
                	predicateList.add(cb.like(root.get("content").as(String.class), "%"+(String)searchMap.get("content")+"%"));
                }
                // 用户ID
                if (searchMap.get("userid")!=null && !"".equals(searchMap.get("userid"))) {
                	predicateList.add(cb.like(root.get("userid").as(String.class), "%"+(String)searchMap.get("userid")+"%"));
                }
                // 昵称
                if (searchMap.get("nickname")!=null && !"".equals(searchMap.get("nickname"))) {
                	predicateList.add(cb.like(root.get("nickname").as(String.class), "%"+(String)searchMap.get("nickname")+"%"));
                }
                // 是否解决
                if (searchMap.get("solve")!=null && !"".equals(searchMap.get("solve"))) {
                	predicateList.add(cb.like(root.get("solve").as(String.class), "%"+(String)searchMap.get("solve")+"%"));
                }
                // 回复人昵称
                if (searchMap.get("replyname")!=null && !"".equals(searchMap.get("replyname"))) {
                	predicateList.add(cb.like(root.get("replyname").as(String.class), "%"+(String)searchMap.get("replyname")+"%"));
                }
				
				return cb.and( predicateList.toArray(new Predicate[predicateList.size()]));

			}
		};

	}

}
