package com.november.log.dao;

import com.november.log.model.Log;
import com.november.log.model.LogType;
import com.november.log.model.LogWithBLOBs;

import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface LogMapper {
    int deleteByPrimaryKey(@Param("id") Integer id);

    int insert(LogWithBLOBs record);

    int insertSelective(LogWithBLOBs record);

    LogWithBLOBs selectByPrimaryKey(@Param("id") Integer id);

    int updateByPrimaryKeySelective(LogWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(LogWithBLOBs record);

    int updateByPrimaryKey(Log record);

    List<LogType> getLogTypeList();

    List<String> getLogDateList(@Param("startDate") String startDate,@Param("endDate") String endDate);

    List<LogWithBLOBs> getLogListByOperTime(@Param("date") String startDate,@Param("page") int page,@Param("limit") int limit);

    int getCountByOperTime(@Param("date") String date);
}