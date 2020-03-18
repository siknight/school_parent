package com.school.friend.pojo;

import com.school.user.pojo.User;

public class UserFriendActivity {

    private User user;
    private Friendactivity friendactivity;
    private Friend friend;

    public Friend getFriend() {
        return friend;
    }

    public void setFriend(Friend friend) {
        this.friend = friend;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Friendactivity getFriendactivity() {
        return friendactivity;
    }

    public void setFriendactivity(Friendactivity friendactivity) {
        this.friendactivity = friendactivity;
    }
}
