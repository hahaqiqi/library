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

    @RequestMapping("/space.html")
    public String toSpace(){return "Parentspace"; };


    @ResponseBody
    @RequestMapping("/save.json")
    public JsonData save(SpaceParam record){
        log.info("开始添加空间",record);
        spaceser.insert(record);
        return JsonData.success();
    }

    @ResponseBody
    @RequestMapping("/list.json")
    public JsonData ParamList(){
        List<Space> spaceList=spaceser.selectList();
        log.info("开始查询空间",spaceList);
        return JsonData.success(spaceList);
    }

    @ResponseBody
    @RequestMapping("/update.json")
    public JsonData update(SpaceParam record){
        log.info("开始编辑空间",record);
        spaceser.updateByPrimaryKeySelective(record);
        return JsonData.success();
    }

    @ResponseBody
    @RequestMapping("/delete.json")
    public JsonData delete(@RequestParam(value = "id") Integer id){
        log.info("开始删除空间",id);
        spaceser.deleteByPrimaryKey(id);
        return JsonData.success();
    }

    @ResponseBody
    @RequestMapping("/move.json")
    public JsonData move(Integer id,Integer pid){
        log.info("开始移动空间");
        spaceser.Movespace(id,pid);
        return JsonData.success();
    }

    @ResponseBody
    @RequestMapping("/spacelist.json")
    public JsonData getSpacelist(@RequestParam(value = "spaceid") Integer spaceid){
        log.info("开始获取空间");
        List<Integer> list=spaceser.selectSpaceBook(spaceid);
        StringBuffer listStr=new StringBuffer();
        //1,2,3,4
        for(int i=0;i<list.size();i++){
            if(i==0){
                listStr.append(list.get(i));
                continue;
            }
            listStr.append(",");
            listStr.append(list.get(i));
        }
        return JsonData.success(listStr);
    }

}
