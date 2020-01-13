package com.bit.module.manager.service.Impl;

import com.bit.base.exception.BusinessException;
import com.bit.base.vo.BaseVo;
import com.bit.base.vo.SuccessVo;
import com.bit.module.manager.bean.PortalContent;
import com.bit.module.manager.bean.PortalResource;
import com.bit.module.manager.bean.PortalRole;
import com.bit.module.manager.bean.PortalRoleRelResource;
import com.bit.module.manager.dao.PortalResourceDao;
import com.bit.module.manager.service.PortalResourceService;
import com.bit.module.manager.vo.PortalRoleVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.bit.common.Const.MODIFYRESOURCERELROLE;
import static com.bit.common.Const.RESOURCEPARENTFLG;
import static com.bit.common.cmsenum.cmsEnum.USING_FLAG;

/**
 * @description: 资源相关实现
 * @author: liyang
 * @date: 2019-06-25
 **/
@Service
public class PortalResourceServiceImpl implements PortalResourceService {

    /**
     * resource 相关dao
     */
    @Autowired
    private PortalResourceDao portalResourceDao;

    /**
     * 获取所有资源树
     * @author liyang
     * @date 2019-06-25
     * @return : BaseVo
     */
    @Override
    public BaseVo findResource() {

        //获取所有资源
        List<PortalResource> portalResourceList = portalResourceDao.findResourceSql(USING_FLAG.getCode());

        //根据父ID进行分组
        Map<Long,List<PortalResource>> resourceMap = portalResourceList.stream().collect(Collectors.groupingBy(PortalResource::getPid));

        //获取所有父类集合
        List<PortalResource> portalResourceParentList = resourceMap.get(RESOURCEPARENTFLG);

        //将子类集合插入所属父类
        for(PortalResource portalResource : portalResourceParentList){
            portalResource.setPortalResourceList(resourceMap.get(portalResource.getId()));
        }

        BaseVo baseVo = new BaseVo();
        baseVo.setData(portalResourceParentList);

        return baseVo;
    }

    /**
     * 新增角色
     * @author liyang
     * @date 2019-06-25
     * @param portalRoleVo : 角色详情
     * @return : BaseVo
    */
    @Override
    @Transactional
    public BaseVo addRole(PortalRoleVo portalRoleVo) {

        //组装角色表
        PortalRole portalRole = new PortalRole();
        portalRole.setRoleName(portalRoleVo.getRoleName());
        portalRole.setRoleDescribe(portalRoleVo.getRoleDescribe());
        portalRole.setStatus(USING_FLAG.getCode());

        //先插入角色表
        portalResourceDao.addRoleSql(portalRole);

        //组装角色资源表
        List<PortalRoleRelResource> portalRoleRelResourceList = new ArrayList<>();
        for (Long resourceId : portalRoleVo.getResourceList()){
            PortalRoleRelResource roleRelResource = new PortalRoleRelResource();
            roleRelResource.setRoleId(portalRole.getId());
            roleRelResource.setResourceId(resourceId);
            portalRoleRelResourceList.add(roleRelResource);
        }
        //再插入角色资源关系表
        portalResourceDao.addRoleRelResourceSql(portalRoleRelResourceList);

        return new SuccessVo();
    }

    /**
     * 分页获取角色列表
     * @author liyang
     * @date 2019-06-26
     * @param portalRoleVo 查询条件
     * @return : BaseVo
    */
    @Override
    public BaseVo findRoleListPage(PortalRoleVo portalRoleVo) {

        //设置查询条件为正常状态
        portalRoleVo.setStatus(USING_FLAG.getCode());

        //分页查询
        PageHelper.startPage(portalRoleVo.getPageNum(), portalRoleVo.getPageSize());
        List<PortalRole> portalRoleList = portalResourceDao.findRoleListPageSql(portalRoleVo);
        PageInfo<PortalRole> pageInfo = new PageInfo<PortalRole>(portalRoleList);

        BaseVo baseVo = new BaseVo();
        baseVo.setData(pageInfo);

        return baseVo;
    }

    /**
     * 修改角色
     * @author liyang
     * @date 2019-06-26
     * @param portalRoleVo 修改详情
     * @return BaseVo
     */
    @Override
    @Transactional
    public BaseVo modfiyRole(PortalRoleVo portalRoleVo) {

        //先修改角色基本信息(不包含角色资源关系)
        portalResourceDao.modfiyRoleSql(portalRoleVo);

        //判断角色对应资源是否需要修改
        if(MODIFYRESOURCERELROLE.equals(portalRoleVo.getResourceFlg())){

            //需要修改，先删除  再新增
            portalResourceDao.deleteRoleRelResourceSql(portalRoleVo.getId());

            //组装角色资源表
            List<PortalRoleRelResource> portalRoleRelResourceList = new ArrayList<>();
            for (Long resourceId : portalRoleVo.getResourceList()){
                PortalRoleRelResource roleRelResource = new PortalRoleRelResource();
                roleRelResource.setRoleId(portalRoleVo.getId());
                roleRelResource.setResourceId(resourceId);
                portalRoleRelResourceList.add(roleRelResource);
            }
            portalResourceDao.addRoleRelResourceSql(portalRoleRelResourceList);
        }

        return new SuccessVo();
    }

    /**
     * 删除角色
     * @author liyang
     * @date 2019-06-26
     * @param id :  id
     * @return : BaseVo
     */
    @Override
    @Transactional
    public BaseVo deleteRole(Long id) {
        //先删除资源关联表
        portalResourceDao.deleteRoleRelResourceSql(id);

        //再删除角色表
        portalResourceDao.deleteRoleSql(id);

        return new SuccessVo();
    }

    /**
     * 检查角色是否可以删除
     * @author liyang
     * @date 2019-06-26
     * @param id id
     * @return BaseVo
     */
    @Override
    public BaseVo checkDelete(Long id) {
        Integer count = portalResourceDao.checkDeleteSql(id);

        if(count>0){
            throw  new BusinessException("此角色被使用，不能直接删除！");
        }else {
            return new SuccessVo();
        }

    }

    /**
     * 根据ID查询角色明细
     * @author liyang
     * @date 2019-06-26
     * @param id id
     * @return BaseVo
     */
    @Override
    public BaseVo findRole(Long id) {

        //查询角色
        PortalRoleVo portalRoleVo = portalResourceDao.findRoleSql(id);

        //获得资源明细
        List<Long> resourceList = portalResourceDao.findRoleRelRoleByRoleIdSql(id);

        portalRoleVo.setResourceList(resourceList);

        BaseVo baseVo = new BaseVo();
        baseVo.setData(portalRoleVo);

        return baseVo;
    }

    /**
     * 获得所有角色
     * @author liyang
     * @date 2019-06-27
     * @return BaseVo
     */
    @Override
    public BaseVo findAllRole() {

        PortalRoleVo portalRoleVo = new PortalRoleVo();
        portalRoleVo.setStatus(USING_FLAG.getCode());

        List<PortalRole> portalRoleList = portalResourceDao.findRoleListPageSql(portalRoleVo);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(portalRoleList);

        return baseVo;
    }
}
