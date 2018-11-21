package com.november.space.service;

import com.november.space.model.Space;
import com.november.space.param.SpaceParam;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("spaceService")
public interface SpaceService {
    int deleteByPrimaryKey(Integer id);//根据ID删除

    int insert(SpaceParam record);//添加空间

    List<Space> selectList();//查询空间

    int updateByPrimaryKey(SpaceParam record);//修改空间


  /*  SpaceParam selectByPrimaryKey(@Param("id") Integer id);

    int updateByPrimaryKeySelective(SpaceParam record);

    int updateByPrimaryKey(SpaceParam record);*/
}
