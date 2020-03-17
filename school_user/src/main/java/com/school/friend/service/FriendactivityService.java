package com.school.friend.service;

import java.util.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import com.school.friend.dao.FriendactivityDao;
import com.school.friend.pojo.Friendactivity;
import com.school.friend.pojo.UserFriendActivity;
import com.school.user.dao.UserDao;
import com.school.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
public class FriendactivityService {

	@Autowired
	private FriendactivityDao friendactivityDao;
	
	@Autowired
	private IdWorker idWorker;

	@Autowired
	private UserDao userDao;



	/**
	 * 查询全部列表
	 * @return
	 */
	public List<UserFriendActivity> findAllUserFriendActivity() {
		List<Friendactivity> acAll = friendactivityDao.findAll();
		ArrayList<UserFriendActivity> ufList = new ArrayList<>();

		for (Friendactivity ac:acAll){
			String userid = ac.getUserid();
			User user = userDao.findById(userid).get();
			user.setPassword(null);
			UserFriendActivity userFriendActivity = new UserFriendActivity();
			userFriendActivity.setUser(user);
			userFriendActivity.setFriendactivity(ac);
			ufList.add(userFriendActivity);
		}
		return ufList;
	}


	/**
	 * 查询全部列表
	 * @return
	 */
	public List<Friendactivity> findAll() {
		return friendactivityDao.findAll();
	}

	
	/**
	 * 条件查询+分页
	 * @param whereMap
	 * @param page
	 * @param size
	 * @return
	 */
	public Page<Friendactivity> findSearch(Map whereMap, int page, int size) {
		Specification<Friendactivity> specification = createSpecification(whereMap);
		PageRequest pageRequest =  PageRequest.of(page-1, size);
		return friendactivityDao.findAll(specification, pageRequest);
	}

	
	/**
	 * 条件查询
	 * @param whereMap
	 * @return
	 */
	public List<Friendactivity> findSearch(Map whereMap) {
		Specification<Friendactivity> specification = createSpecification(whereMap);
		return friendactivityDao.findAll(specification);
	}

	/**
	 * 根据ID查询实体
	 * @param id
	 * @return
	 */
	public Friendactivity findById(String id) {
		return friendactivityDao.findById(id).get();
	}

	/**
	 * 增加
	 * @param friendactivity
	 */
	public void add(Friendactivity friendactivity) {
		friendactivity.setId( idWorker.nextId()+"" );

		friendactivityDao.save(friendactivity);
	}

	/**
	 * 修改
	 * @param friendactivity
	 */
	public void update(Friendactivity friendactivity) {
		friendactivityDao.save(friendactivity);
	}

	/**
	 * 删除
	 * @param id
	 */
	public void deleteById(String id) {
		friendactivityDao.deleteById(id);
	}

	/**
	 * 动态条件构建
	 * @param searchMap
	 * @return
	 */
	private Specification<Friendactivity> createSpecification(Map searchMap) {

		return new Specification<Friendactivity>() {

			@Override
			public Predicate toPredicate(Root<Friendactivity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicateList = new ArrayList<Predicate>();
                // 
                if (searchMap.get("id")!=null && !"".equals(searchMap.get("id"))) {
                	predicateList.add(cb.like(root.get("id").as(String.class), "%"+(String)searchMap.get("id")+"%"));
                }
                // 
                if (searchMap.get("activityName")!=null && !"".equals(searchMap.get("activityName"))) {
                	predicateList.add(cb.like(root.get("activityName").as(String.class), "%"+(String)searchMap.get("activityName")+"%"));
                }
                // 
                if (searchMap.get("userid")!=null && !"".equals(searchMap.get("userid"))) {
                	predicateList.add(cb.like(root.get("userid").as(String.class), "%"+(String)searchMap.get("userid")+"%"));
                }
				
				return cb.and( predicateList.toArray(new Predicate[predicateList.size()]));

			}
		};

	}

}