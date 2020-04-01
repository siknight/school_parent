package com.school.user.dao;

import com.school.user.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface UserDao extends JpaRepository<User,String>,JpaSpecificationExecutor<User>{
    //通过账号查询
    public User findByMobile(String mobile);


    public User findUserById(String id);




    /**
     * 修改密码
     * @param mobile
     * @param password
     */
    @Modifying
    @Query("update User u set u.password=?2 where u.mobile=?1")
    public void updatePass(String mobile, String password);


    /**
     * 修改头像
     * @param id
     * @param avatar
     */
    @Modifying
    @Query("update User u set u.avatar=?2 where u.id=?1")
    public void updateImg(String id, String avatar);

    /**
     * 跟新粉丝数
     * @param userid
     * @param x
     */
    @Modifying
    @Query("update User u set u.fanscount=u.fanscount+?2 where u.id=?1")
    public void incFanscount(String userid, int x);

    /**
     * 更新关注数
     * @param userid
     * @param x
     */
    @Modifying
    @Query("update User u set u.followcount=u.followcount+?2 where u.id=?1")
    public void incFollowcount(String userid, int x);
}
