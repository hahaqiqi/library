package com.november.log.service;

import com.november.log.Param.LogParam;
import com.november.log.model.LogWithBLOBs;

import java.util.Date;
import java.util.List;

/**
 * @author skrT
 * @create 2018/11/27 10:42
 */
public interface LogService {

    public void saveLog(LogParam param);

    public List<String> getDateList();

    public List<LogWithBLOBs> getAllByOperTime(String date,int page,int limit);

    public int getCountByOperTime(String date);
}
