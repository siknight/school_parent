package com.school.problem.dao;

import com.school.article.pojo.FavArticle;
import com.school.problem.pojo.FavProblem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface FavProblemDao extends JpaRepository<FavProblem,String>,JpaSpecificationExecutor<FavArticle>{

    public int countByProblemid(String problemid);

    public int countByUseridAndProblemid(String userid, String problemid);

    public List<FavProblem> findByUserid(String userid);


//    public void deleteAllByArticleid(String articleid);


}
