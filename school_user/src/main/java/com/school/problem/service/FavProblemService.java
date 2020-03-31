package com.school.problem.service;



import com.school.article.pojo.FavArticle;
import com.school.problem.dao.FavProblemDao;
import com.school.problem.dao.ProblemDao;
import com.school.problem.pojo.FavProblem;
import com.school.problem.pojo.Problem;
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

/**
 * 服务层
 * 
 * @author Administrator
 *
 */
@Service
public class FavProblemService {

	@Autowired
	private FavProblemDao favDao;
	
	@Autowired
	private IdWorker idWorker;

	@Autowired
	private ProblemDao problemDao;


	/**
	 * 取消收藏
	 */
	@Transactional
	public void deleteByProidAndUserid(String proid ,String userid){
		favDao.deleteByProblemidAndUserid(proid,userid);
	}

	/**
	 * 查找收藏的问题
	 * @param userid
	 * @return
	 */
	public List<Problem> findAllfavProblemsByUserid(String userid){
		//获取该用户收藏的所有记录
		List<FavProblem> favArticles = favDao.findByUserid(userid);
		//用于存储查询出来的文章
		List<Problem> problems = new ArrayList<>();
		for (FavProblem favProblem:favArticles){
			//获取改用户收藏的文章id
			String problemid = favProblem.getProblemid();
			System.out.println("problemid="+problemid);
			//通过该文章id查找

//			Problem problem = problemDao.findByIdAndUserid(problemid,userid);
			Problem problem =problemDao.findProblemById(problemid);
			if (problem!=null){
				problems.add(problem);
			}

		}
		return  problems;
	}



	public int findByUseridAndProblemid(String userid,String problemid){
		return  favDao.countByUseridAndProblemid(userid,problemid);
	}

	public int countByProblemid(String problemid){
		return  favDao.countByProblemid(problemid);
	}


	/**
	 * 增加
	 * @param favProblem
	 */
	public void add(FavProblem favProblem) {
		favProblem.setId( idWorker.nextId()+"" );
		favDao.save(favProblem);
	}
	/**
	 * 查询全部列表
	 * @return
	 */
	public List<FavProblem> findAll() {
		return favDao.findAll();
	}

	

	

	/**
	 * 根据ID查询实体
	 * @param id
	 * @return
	 */
	public FavProblem findById(String id) {
		return favDao.findById(id).get();
	}





	/**
	 * 删除
	 * @param id
	 */
	public void deleteById(String id) {
		favDao.deleteById(id);
	}


}
