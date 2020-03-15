package com.school.friend.service;

import com.school.friend.dao.FriendDao;
import com.school.friend.dao.NoFriendDao;
import com.school.friend.pojo.Friend;
import com.school.friend.pojo.NoFriend;
import com.school.user.dao.UserDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FriendService {
    @Autowired
    private FriendDao friendDao;

    @Autowired
    private NoFriendDao noFriendDao;

    @Autowired
    private UserDao userClient;

    /**
     * 加入黑名单(非朋友列表)
     * @param userid
     * @param friendid
     */
    public void addNoFriend(String userid,String friendid){
        NoFriend noFriend=new NoFriend();
        noFriend.setUserid(userid);
        noFriend.setFriendid(friendid);
        noFriendDao.save(noFriend);
    }

    /**
     * 删除好友
     * @param userid
     * @param friendid
     */
    public void deleteFriend(String userid,String friendid){
        friendDao.deleteFriendByUseridAndFriendid(userid,friendid);
        friendDao.updateLike(friendid,userid,"0");
        addNoFriend(userid,friendid);//向不喜欢表中添加记录
        userClient.incFollowcount(userid,-1);//减少自己的关注数
        userClient.incFanscount(friendid,-1);//减少对方的粉丝数
    }
    /**
     * 添加喜欢
     * @param userid
     * @param friendid
     * @return
     */
    public int addFriend(String userid,String friendid){
        //判断如果用户已经添加了这个好友，则不进行任何操作,返回0
        if(friendDao.selectCount(userid, friendid)>0){  //查询是否有一个
            return 0;
        }

        Friend friend=new Friend();
        friend.setUserid(userid);
        friend.setFriendid(friendid);
        friend.setIslike("0");
        friendDao.save(friend);

        userClient.incFollowcount(userid,1);//增加自己的关注数
        userClient.incFanscount(friendid,1);//增加对方的粉丝数

        //判断对方是否喜欢你，如果喜欢，将islike设置为1
        if(friendDao.selectCount( friendid,userid)>0){
            friendDao.updateLike(userid,friendid,"1");
            friendDao.updateLike(friendid,userid,"1");
        }

        return 1;
    }


}
