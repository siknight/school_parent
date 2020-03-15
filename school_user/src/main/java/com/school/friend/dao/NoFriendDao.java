package com.school.friend.dao;

import com.school.friend.pojo.NoFriend;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 非好友数据访问层
 */
public interface NoFriendDao extends JpaRepository<NoFriend,String> {



}
