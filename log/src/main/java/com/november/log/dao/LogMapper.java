package com.november.log.dao;

import com.november.log.model.Log;
import com.november.log.model.LogType;
import com.november.log.model.LogWithBLOBs;

import java.util.List;

public interface LogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LogWithBLOBs record);

    int insertSelective(LogWithBLOBs record);

    LogWithBLOBs selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LogWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(LogWithBLOBs record);

    int updateByPrimaryKey(Log record);

    List<LogType> getLogTypeList();
}