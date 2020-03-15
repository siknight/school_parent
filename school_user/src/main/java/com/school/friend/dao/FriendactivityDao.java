package com.school.friend.dao;

import com.school.friend.pojo.Friendactivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface FriendactivityDao extends JpaRepository<Friendactivity,String>,JpaSpecificationExecutor<Friendactivity>{
	
}
