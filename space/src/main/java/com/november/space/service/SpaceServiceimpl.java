package com.november.space.service;

import com.november.common.RequestHolder;
import com.november.exception.ParamException;
import com.november.space.dao.SpaceMapper;
import com.november.space.model.Space;
import com.november.space.param.SpaceParam;
import com.november.util.BeanValidator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("spaceService")
public class SpaceServiceimpl implements SpaceService {
    @Resource
    private SpaceMapper spacemapper;

    /*@Override
    public int deleteByPrimaryKey(Integer id) {
        return spacemapper.deleteByPrimaryKey(id);
    }*/



    /*
    判断父空间下的子空间名称
     */
    public boolean parentIdJudge(Integer parentid,String parentName){
        return spacemapper.selectByparentid(parentid,parentName)>0;
    }

    //增加父空间
    public int insert(SpaceParam param){
        ////检查param类BeanValidator.check
        BeanValidator.check(param);
        //使用parentIdJudge判断父空间下的子空间名称
       if(parentIdJudge(param.getParentId(),param.getSpaceName())){
           throw new ParamException("这个父空间已经有这个子空间了");
       }
        //非空判断
        if(param.getRemark()==""){
            param.setRemark("暂时没有");
        }
        //使用builder()方法把SpaceParam类存储的数据传递给Space类
        Space space =Space.builder().spaceName(param.getSpaceName()).level(param.getLevel())
                .remark(param.getRemark()).parentId(param.getParentId()).build();
       if(RequestHolder.getCurrentAdmin()!=null){
           space.setOperator(RequestHolder.getCurrentAdmin().getAdminCode());
       }else{
           space.setOperator("admin");
       }
       space.setOperateTime(new Date());
       return spacemapper.insert(space);
    }

    /*
        查询
     */
    public List<Space> selectList(){
        //查询ParentId值为0的父空间
        List<Space> listSpace=spacemapper.selectList(0);
        //调用forListSpace方法并把值放入listSpace中
        listSpace= forListSpace(0,listSpace.size(),listSpace);
        return listSpace;
    }

    public List<Space> forListSpace(int start,int end,List<Space> listSpace){
        //循环获取ID值
            for(int i=start;i<end;i++){
                List<Space> newList=spacemapper.selectList(listSpace.get(i).getId());
                for (Space space : newList) {
                    listSpace.add(space);
                }
            }
            if(end!=listSpace.size()){
                forListSpace(end,listSpace.size(),listSpace);
            }
            return listSpace;
    }

    /*public List<Space> getListSpaceId(int start,int end,List<Space> listSpace){
        for(int i=start;i<end;i++){

        }
        return listSpace;
    }*/

    /*
        修改
     */
    public int updateByPrimaryKeySelective(SpaceParam param){
        //检查param类BeanValidator.check
        BeanValidator.check(param);
        //使用parentIdJudge判断父空间下的子空间名称
        if(parentIdJudge(param.getParentId(),param.getSpaceName())){
            throw new ParamException("同一个父空间不能有两个相同的子空间名称");
        }
        //非空判断
        if(param.getRemark()==""){
            param.setRemark("暂时没有");
        }
        //使用builder()方法把SpaceParam类存储的数据传递给Space类
        Space space=Space.builder().id(param.getId()).spaceName(param.getSpaceName()).level(param.getLevel()).
                remark(param.getRemark()).build();
        if(RequestHolder.getCurrentAdmin()!=null){
            space.setOperator(RequestHolder.getCurrentAdmin().getAdminCode());
        }else{
            space.setOperator("admin");
        }
        space.setOperateTime(new Date());
        //调用updateByPrimaryKeySelective方法
        return spacemapper.updateByPrimaryKeySelective(space);
    }

    /*
        删除
     */
    public int deleteByPrimaryKey(Integer id){
        List<Space> listSpace=spacemapper.selectList(id);
        listSpace=forListSpace(0,listSpace.size(),listSpace);
        List<Integer> listId=new ArrayList<>();
        listId.add(id);
        for(Space sp :listSpace){
            listId.add(sp.getId());
        }
        return spacemapper.deleteByPrimaryKey(listId);
    }
    /*
        20 23 24 25 32 33 34 35 36 37
     */

    public int Movespace(Integer id,Integer pid){
        //使用parentIdJudge判断父空间下的子空间名称
        Space param=spacemapper.selectByPrimaryKey(id);
        if(parentIdJudge(pid,param.getSpaceName())){
            throw new ParamException("同一个父空间不能有两个相同的子空间名称");
        }
        return spacemapper.Movespace(id,pid);
    }

    public List<Integer> selectSpaceBook(Integer id){
        List<Space> listSpace=spacemapper.selectList(id);
        listSpace=forListSpace(0,listSpace.size(),listSpace);
        List<Integer> listId=new ArrayList<>();
        listId.add(id);
        for(Space sp :listSpace){
            listId.add(sp.getId());
        }
        return listId;
    }

    /*
        根据传过来的ID值查出位置
     */


    public String showBookspacePlace(Integer id){
        StringBuffer name=new StringBuffer();
        List<String> listSpace=new ArrayList<>();
        listSpace = rrr(id,listSpace);
        for (int i=listSpace.size()-1;i>=0;i--){
            name.append(listSpace.get(i));
            if(i>0){
                name.append("→");
            }
        }
        return name.toString();
    }

    public List<String> rrr(Integer sapceid,List<String> listsapce){
        Space space =spacemapper.selectByPrimaryKey(sapceid);
        if(space !=null){
            listsapce.add(space.getSpaceName());
            if(space.getParentId()>0){
                rrr(space.getParentId(),listsapce);
            }
        }
        return listsapce;
    }

    /*@Override
    public int updateByPrimaryKey(SpaceParam record) {
        return spacemapper.updateByPrimaryKey(record);
}

    @Override
    public int updateByPrimaryKeySelective(SpaceParam record) {
        return spacemapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public SpaceParam selectByPrimaryKey(Integer id) {
        return spacemapper.selectByPrimaryKey(id);
    }*/
}
