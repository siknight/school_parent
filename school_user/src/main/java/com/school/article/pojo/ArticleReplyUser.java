package com.school.article.pojo;

import com.school.user.pojo.User;

public class ArticleReplyUser {

    private ArticleReply articleReply;
    private User user;

    public ArticleReply getArticleReply() {
        return articleReply;
    }

    public void setArticleReply(ArticleReply articleReply) {
        this.articleReply = articleReply;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
