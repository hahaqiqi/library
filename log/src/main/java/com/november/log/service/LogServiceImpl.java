package com.november.log.service;

import com.november.common.RequestHolder;
import com.november.log.Param.LogParam;
import com.november.log.dao.LogMapper;
import com.november.log.model.LogWithBLOBs;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

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


}

