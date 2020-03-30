package com.school.article.dao;

import com.school.article.pojo.FavArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface FavArticleDao extends JpaRepository<FavArticle,String>,JpaSpecificationExecutor<FavArticle>{

    public int countByArticleid(String articleid);

    public int countByUseridAndArticleid(String userid,String articleid);

    public List<FavArticle> findByUserid(String userid);

    public  void deleteByArticleidAndUserid(String articleid,String userid);

}
