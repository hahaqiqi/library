package com.november.log.service;

import com.google.common.collect.Lists;
import com.november.common.RequestHolder;
import com.november.exception.ParamException;
import com.november.log.Param.LogParam;
import com.november.log.dao.LogMapper;
import com.november.log.model.LogType;
import com.november.log.model.LogWithBLOBs;
import com.november.util.TimeUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author skrT
 * @create 2018/11/27 10:42
 */
@Service("logService")
public class LogServiceImpl implements LogService {

    @Resource
    private LogMapper logMapper;

    @Override
    public void saveLog(LogParam param) {
        LogWithBLOBs log = new LogWithBLOBs();
        log.setType(param.getType());
        log.setTargetid(param.getTargetId());
        log.setOperateTime(new Date());
        if(param.getOldValue() != null){
            log.setOldValue(param.getOldValue());
        }
        if(param.getNewValue() != null){
            log.setNewValue(param.getNewValue());
        }
        if(param.getRemark() != null){
            log.setRemark(param.getRemark());
        }
        if(RequestHolder.getCurrentAdmin() != null){
            log.setOperator(RequestHolder.getCurrentAdmin().getAdminCode());
        }else{
            log.setOperator("admin");
        }
        logMapper.insertSelective(log);
    }

    @Override
    public List<String> getDateList(String date) {
        if(StringUtils.isBlank(date)){
            List<String> logDateList = logMapper.getLogDateList(null,null);
            return logDateList;
        }
        if(date.indexOf(' ') > 0){
            String[] dates = StringUtils.split(date," ");
            if(dates[0].equals(dates[1])){
                List<String> logDateList = logMapper.getLogDateList(dates[0],null);
                return logDateList;
            }
            List<String> logDateList = logMapper.getLogDateList(dates[0],dates[1]);
            return logDateList;
        }
        return null;
    }

    @Override
    public List<LogWithBLOBs> getAllByOperTime(String date,int page,int limit) {
        page = (page-1) * limit;
        if(StringUtils.isBlank(date)){
            throw new ParamException("日期不能为空");
        }
        List<LogWithBLOBs> logList = logMapper.getLogListByOperTime(date, page, limit);
        setLogType(logList);
        return logList;
    }

    @Override
    public int getCountByOperTime(String date) {
        if(StringUtils.isBlank(date)){
            throw new ParamException("日期不能为空");
        }
        return logMapper.getCountByOperTime(date);
    }

    private void setLogType(List<LogWithBLOBs> logs){
        if(CollectionUtils.isEmpty(logs)){
            return;
        }
        List<LogType> logTypeList = logMapper.getLogTypeList();
        for (LogWithBLOBs log : logs) {
            for (LogType logType : logTypeList) {
                if(logType.getTypeId().equals(log.getType().toString())){
                    log.setLogTypeName(logType.getTypeName());
                    break;
                }
            }
        }
    }

}

