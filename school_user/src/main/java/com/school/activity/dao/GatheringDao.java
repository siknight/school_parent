package com.school.activity.dao;


import com.school.activity.pojo.Gathering;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface GatheringDao extends JpaRepository<Gathering,String>,JpaSpecificationExecutor<Gathering>{

    public List<Gathering> findByNameLikeOrAddressLikeOrDetailLikeOrSponsorLike(String name,String address,String detail,String sponsor);

    public List<Gathering> findTop2ByStateOrderByEndtimeDesc(String state);

//    @Override
//    List<Gathering> findAll(Sort sort);
}
