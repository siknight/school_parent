package com.school.problem.pojo;

import com.school.user.pojo.User;

public class ProReplyUser {
    private  ProblemReply problemReply;

    private User user;

    public ProblemReply getProblemReply() {
        return problemReply;
    }

    public void setProblemReply(ProblemReply problemReply) {
        this.problemReply = problemReply;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
