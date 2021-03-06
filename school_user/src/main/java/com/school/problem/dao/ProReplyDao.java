package com.school.problem.dao;

import com.school.problem.pojo.ProblemReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;


/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface ProReplyDao extends JpaRepository<ProblemReply,String>,JpaSpecificationExecutor<ProblemReply>{

    public List<ProblemReply> findByProblemid(String problemid);
}
