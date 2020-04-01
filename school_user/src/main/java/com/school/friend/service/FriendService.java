package com.school.friend.service;


import com.school.friend.dao.FriendDao;
import com.school.friend.dao.NoFriendDao;
import com.school.friend.pojo.Friend;
import com.school.friend.pojo.NoFriend;
import com.school.user.dao.UserDao;

import com.school.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
     * 查找关注的用户
     * @param userid
     * @return
     */
    public List<User> findAllFriendsByUserid(String userid){
        //获取该用户收藏的所有记录
        List<Friend> friends = friendDao.findByUserid(userid);
        //用于存储查询出来的文章
        List<User> users = new ArrayList<>();
        for (Friend friend:friends){
            //获取每个用户收藏的文章id
            String friendid = friend.getFriendid();
            System.out.println("friendid="+friendid);
            //通过该文章id查找
            User user= userClient.findUserById(friendid);

            System.out.println("service user="+user);
            if (user!=null){
//                user.setPassword(null);
//                user.setUpdatedate(null);
//                user.setBirthday(null);
                users.add(user);
            }
        }
        return  users;
    }

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


        Friend friend=new Friend();
        friend.setUserid(userid);
        friend.setFriendid(friendid);
        friend.setIslike("0");
        friendDao.save(friend);


        return 1;
    }


}
