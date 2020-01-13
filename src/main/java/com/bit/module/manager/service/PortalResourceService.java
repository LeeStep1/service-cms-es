package com.bit.module.manager.service;


import com.bit.base.vo.BaseVo;
import com.bit.module.manager.vo.PortalRoleVo;

public interface PortalResourceService {

    /**
     * 获取所有资源树
     * @author liyang
     * @date 2019-06-25
     * @return : BaseVo
    */
    BaseVo findResource();

    /**
     * 新增角色
     * @author liyang
     * @date 2019-06-25
     * @param portalRoleVo : 角色详情
     * @return : BaseVo
    */
    BaseVo addRole(PortalRoleVo portalRoleVo);

    /**
     * 分页获取角色列表
     * @author liyang
     * @date 2019-06-26
     * @param portalRoleVo 查询条件
     * @return : BaseVo
    */
    BaseVo findRoleListPage(PortalRoleVo portalRoleVo);

    /**
     * 修改角色
     * @author liyang
     * @date 2019-06-26
     * @param portalRoleVo 修改详情
     * @return BaseVo
     */
    BaseVo modfiyRole(PortalRoleVo portalRoleVo);

    /**
     * 删除角色
     * @author liyang
     * @date 2019-06-26
     * @param id id
     * @return BaseVo
     */
    BaseVo deleteRole(Long id);

    /**
     * 检查角色是否可以删除
     * @author liyang
     * @date 2019-06-26
     * @param id id
     * @return BaseVo
     */
    BaseVo checkDelete(Long id);

    /**
     * 根据ID查询角色明细
     * @author liyang
     * @date 2019-06-26
     * @param id id
     * @return BaseVo
     */
    BaseVo findRole(Long id);

    /**
     * 获得所有角色
     * @author liyang
     * @date 2019-06-27
     * @return BaseVo
     */
    BaseVo findAllRole();


}
