package com.school.friend.controller;


import com.school.friend.service.FriendService;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/friend")
public class FriendController {
    @Autowired
    private FriendService friendService;

    @Autowired
    private HttpServletRequest request;



    @RequestMapping(value="/like/{friendid}/{type}",method= RequestMethod.PUT)
    public Result addFriend(@PathVariable String friendid , @PathVariable String type){
        Claims claims=(Claims)request.getAttribute("user_claims");
        if(claims==null){
            return new Result(false, StatusCode.ACCESSERROR,"无权访问");
        }
        //如果是喜欢
        if(type.equals("1")){
            //判断是否已经添加过了，如果=0就表示添加过了，如果不是0就添加一个
            if(friendService.addFriend(claims.getId(),friendid)==0){

                return new Result(false, StatusCode.REPERROR,"已经添加此好友");
            }
        }else {
            //不喜欢
            friendService.addNoFriend(claims.getId(),friendid);//向不喜欢 列表中添加记录

        }

        return new Result(true, StatusCode.OK, "操作成功");
    }



    @RequestMapping(value="/{friendid}",method=RequestMethod.DELETE)
    public Result remove(@PathVariable String friendid){
        Claims claims=(Claims)request.getAttribute("user_claims");
        if(claims==null){
            return new Result(false, StatusCode.ACCESSERROR,"无权访问");
        }
        friendService.deleteFriend(claims.getId(), friendid);
        return new Result(true, StatusCode.OK, "删除成功");

    }
}
