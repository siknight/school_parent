package com.school.problem.pojo;

import com.school.user.pojo.User;

public class ProblemUser {
    private Problem problem;
    private User user;

    public Problem getProblem() {
        return problem;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
