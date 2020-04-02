package com.school.friend.dao;

import com.school.friend.pojo.Friendactivity;
import com.school.problem.pojo.Problem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;


/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface FriendactivityDao extends JpaRepository<Friendactivity,String>,JpaSpecificationExecutor<Friendactivity>{
    /**
     * 通过用户查找所有自己发布的活动
     * @param userid
     * @return
     */
    public List<Friendactivity> findByUserid(String userid);

    public List<Friendactivity> findByActivitynameLike(String searchContent);
}
