package com.november.space.controller;


import com.november.common.JsonData;
import com.november.space.model.Space;
import com.november.space.param.SpaceParam;
import com.november.space.service.SpaceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/space")
public class SpaceController {

    @Resource(name = "spaceService")
    private SpaceService spaceser;

    @ResponseBody
    @RequestMapping("/save.json")
    public JsonData save(SpaceParam record){
        log.info("开始添加空间",record);
        spaceser.insert(record);
        return JsonData.success();
    }

    @RequestMapping("/list.html")
    public String save (){
        List<Space> spaceList=spaceser.selectList();
        log.info("开始查询空间",spaceList);
        return "Parentspace";
    }

    @ResponseBody
    @RequestMapping("/update.json")
    public JsonData update(SpaceParam record){
        log.info("开始修改空间",record);
        spaceser.updateByPrimaryKey(record);
        return JsonData.success();
    }
}
