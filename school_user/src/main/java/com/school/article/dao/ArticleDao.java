package com.school.article.dao;

import com.school.article.pojo.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface ArticleDao extends JpaRepository<Article,String>,JpaSpecificationExecutor<Article>{

    /**
     * 文章审核
     * @param id
     */
    @Modifying
    @Query("update Article set state='1' where id=?1")
    public void examine(String id);

    /**
     * 文章点赞
     * @param id
     * @return
     */
    @Modifying
    @Query("update Article  set thumbup=thumbup+1 where id=?1")
    public int updateThumbup(String id);

    /**
     * 通过用户查找所有文章
     * @param userid
     * @return
     */
    public List<Article> findByUserid(String userid);


}
