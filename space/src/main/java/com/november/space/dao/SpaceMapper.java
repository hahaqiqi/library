package com.november.space.dao;


import com.november.space.model.Space;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLClientInfoException;
import java.util.List;

public interface SpaceMapper {

    int deleteByPrimaryKey(List<Integer> listid);//根据ID删除

    int insert(Space record);//添加父空间

    Space selectByPrimaryKey(@Param("id") Integer id);//查询空间

    int selectByparentid(@Param("parentid") Integer parentid, @Param("parentName") String parentName);//查询空间

    List<Space> selectList(@Param("parentId") Integer parentId);//查询空间

    int updateByPrimaryKeySelective(Space record);//修改空间

//    List<Space> selectIdandParentId(@Param("parentid") Integer parentid);

    int Movespace(@Param("id") Integer id,@Param("parentId") Integer pid);//移动节点

    List<Space> selectSpaceBook(List<Integer> Spaceid);

}