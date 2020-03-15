package com.school.friend.dao;


import com.school.friend.pojo.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface FriendDao extends JpaRepository<Friend,String> {
    /**
     * 根据用户ID与被关注用户ID查询记录个数
     * @param userid
     * @param friendid
     * @return
     */
    @Query("select count(f) from Friend f where f.userid=?1 and f.friendid=?2")
    public int selectCount(String userid, String friendid);

    /**
     * 喜欢
     * @param userid
     * @param friendid
     * @param islike
     */
    @Modifying
    @Query("update Friend f set f.islike=?3 where f.userid=?1 and f.friendid=?2")
    public void updateLike(String userid, String friendid, String islike);

    /**
     * 删除喜欢
     * @param userid
     * @param friendid
     */
    public void deleteFriend(String userid, String friendid);
}
