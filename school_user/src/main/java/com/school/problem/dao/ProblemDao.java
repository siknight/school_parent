package com.school.problem.dao;

import com.school.article.pojo.Article;
import com.school.problem.pojo.Problem;
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
public interface ProblemDao extends JpaRepository<Problem,String>,JpaSpecificationExecutor<Problem>{
    /**
     * 通过用户查找所有提问
     * @param userid
     * @return
     */
    public List<Problem> findByUserid(String userid);


    /**
     * 问题点赞
     * @param id
     * @return
     */
    @Modifying
    @Query("update Problem a set thumbup=thumbup+1 where id=?1")
    public int updateThumbup(String id);

    public List<Problem> findByTitleLike(String searchcontet);
    public Problem findProblemById(String id);


    public Problem findByIdAndUserid(String id,String userid);

}
