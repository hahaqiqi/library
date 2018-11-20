package com.november.space.service;

import com.november.exception.ParamException;
import com.november.space.dao.SpaceMapper;
import com.november.space.model.Space;
import com.november.space.param.SpaceParam;
import com.november.util.BeanValidator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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

    @Override
    public int insert(SpaceParam param) {
        BeanValidator.check(param);
        if(parentIdJudge(param.getParentId(),param.getSpaceName())){
            throw new ParamException("这个父空间已经有这个空间名称了");
        }
        Space space=Space.builder().spaceName(param.getSpaceName()).level(param.getLevel())
                .remark(param.getRemark()).parentId(param.getParentId()).build();
        space.setOperator("admin");
        space.setOperateTime(new Date());
        return spacemapper.insertSelective(space);

    }

    public boolean parentIdJudge(Integer parentid,String parentName){
        return spacemapper.selectByparentid(parentid,parentName)>0;
    }

    public List<Space> selectList(){
        return spacemapper.selectList();
    }

    public int updateByPrimaryKey(SpaceParam param){
        BeanValidator.check(param);
        Space space=Space.builder().spaceName(param.getSpaceName()).level(param.getLevel()).
                remark(param.getRemark()).parentId(param.getParentId()).build();
        space.setOperator("admin");
        space.setOperateTime(new Date());
        return spacemapper.updateByPrimaryKeySelective(space);
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
