package com.school.problem.service;



import com.school.article.pojo.FavArticle;
import com.school.problem.dao.FavProblemDao;
import com.school.problem.pojo.FavProblem;
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
