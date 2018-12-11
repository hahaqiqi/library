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
        //判断当前用户权限,当用户权限为null的是时候赋值
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
            //创建List接收selectList方法查询id值
            List<Space> newList=spacemapper.selectList(listSpace.get(i).getId());
            //使用遍历添加把接收了id值的对象赋给listSpace
            for (Space space : newList) {
                listSpace.add(space);
            }
        }
        //当循环结束条件end不等于listSpace.size()的时候使用递归重新执行方法
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
        //判断当前用户权限,当用户权限为null的是时候赋值
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
        //创建对象类型的List接收selectList方法查询数据库传的值
        List<Space> listSpace=spacemapper.selectList(id);
        //用创建的对象类型的List接收forListSpace的值
        listSpace=forListSpace(0,listSpace.size(),listSpace);
        //创建一个Integer类型的List集合
        List<Integer> listId=new ArrayList<>();
        //集合放入参数
        listId.add(id);
        //遍历创建的对象类型的List,把对象类型的List中的getid放入Integer类型的List集合
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
        //判断相同空间下不能存在同一个空间
        if(parentIdJudge(pid,param.getSpaceName())){
            throw new ParamException("同一个父空间不能有两个相同的子空间名称");
        }
        return spacemapper.Movespace(id,pid);
    }

    /*
        查询空间中的书籍
     */
    public List<Integer> selectSpaceBook(Integer id){
        //创建一个对象类型的List
        List<Space> listSpace=spacemapper.selectList(id);
        //对象类型的List接收forListSpace
        listSpace=forListSpace(0,listSpace.size(),listSpace);
        //创建一个Integer类型的List集合
        List<Integer> listId=new ArrayList<>();
        //集合放入参数
        listId.add(id);
        //遍历创建的对象类型的List,把对象类型的List中的getid放入Integer类型的List集合
        for(Space sp :listSpace){
            listId.add(sp.getId());
        }
        return listId;
    }

    /*
        根据传过来的ID值查出位置
     */


    public String showBookspacePlace(Integer id){
        //创建StringBuffer来拼接
        StringBuffer name=new StringBuffer();
        //创建List<String>来接收
        List<String> listSpace=new ArrayList<>();
        //调用rrr方法，把值赋给listSpace
        listSpace = rrr(id,listSpace);
        //用i--循环，按从大到小的顺序来排列
        for (int i=listSpace.size()-1;i>=0;i--){
            //用StringBuffer的append(追加)把listSpace
            name.append(listSpace.get(i));
            //从左到右排序
            if(i>0){
                name.append("→");
            }
        }
        return name.toString();
    }

    public List<String> rrr(Integer sapceid,List<String> listsapce){
        //创建Space对象接收selectByPrimaryKey方法
        Space space =spacemapper.selectByPrimaryKey(sapceid);
        //判断对象不为空的时候添加空间名
        if(space !=null){
            listsapce.add(space.getSpaceName());
            //判断空间id大于0的时候用递归放入值
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
