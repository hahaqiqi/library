package com.november.space.service;

import com.november.space.model.Space;
import com.november.space.param.SpaceParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("spaceService")
public interface SpaceService {
//    int deleteByPrimaryKey(@Param("id") Integer id);

    int insert(SpaceParam record);

    List<Space> selectList();

    int updateByPrimaryKey(SpaceParam record);


  /*  SpaceParam selectByPrimaryKey(@Param("id") Integer id);

    int updateByPrimaryKeySelective(SpaceParam record);

    int updateByPrimaryKey(SpaceParam record);*/
}
