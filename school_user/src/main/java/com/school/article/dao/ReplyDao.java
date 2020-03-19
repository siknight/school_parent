package com.school.article.dao;


import com.school.article.pojo.ArticleReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface ReplyDao extends JpaRepository<ArticleReply,String>,JpaSpecificationExecutor<ArticleReply>{
   //通过文章id查询评论
   public List<ArticleReply> findByArticleid(String articleid);


}
