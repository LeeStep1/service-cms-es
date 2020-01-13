package com.bit.module.manager.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.manager.bean.PortalRole;
import com.bit.module.manager.bean.PortalRoleRelResource;
import com.bit.module.manager.service.PortalResourceService;
import com.bit.module.manager.vo.PortalRoleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description: 资源相关controller
 * @author: liyang
 * @date: 2019-06-25
 **/
@RestController
@RequestMapping(value = "/manager/PortalResource")
public class PortalResourceController {

    @Autowired
    private PortalResourceService portalResourceService;

    /**
     * 获取所有资源树
     * @author liyang
     * @date 2019-06-25
     * @return : BaseVo
     */
    @GetMapping("/Resource")
    public BaseVo findResource(){
        return portalResourceService.findResource();
    }

    /**
     * 新增角色
     * @author liyang
     * @date 2019-06-25
     * @return : BaseVo
     */
    @PostMapping("/addRole")
    public BaseVo addRole(@RequestBody PortalRoleVo portalRoleVo){
        return portalResourceService.addRole(portalRoleVo);
    }

    /**
     * 分页获取角色列表
     * @author liyang
     * @date 2019-06-26
     * @param portalRoleVo 查询条件
     * @return : BaseVo
    */
    @PostMapping("/roleListPage")
    public BaseVo findRoleListPage(@RequestBody PortalRoleVo portalRoleVo){
        return portalResourceService.findRoleListPage(portalRoleVo);
    }

    /**
     * 修改角色
     * @author liyang
     * @date 2019-06-26
     * @param portalRoleVo 查询条件
     * @return : BaseVo
     */
    @PostMapping("/modfiy")
    public BaseVo modfiyRole(@RequestBody PortalRoleVo portalRoleVo){
        return portalResourceService.modfiyRole(portalRoleVo);
    }

    /**
     * 删除角色
     * @author liyang
     * @date 2019-06-26
     * @param id :  id
     * @return : BaseVo
    */
    @DeleteMapping("/Role/{id}")
    public BaseVo deleteRole(@PathVariable(value = "id") Long id){
        return portalResourceService.deleteRole(id);
    }

    /**
     * 检查角色是否可以删除
     * @author liyang
     * @date 2019-06-26
     * @param id :  id
     * @return : BaseVo
     */
    @GetMapping("/checkDelRole/{id}")
    public BaseVo checkDelete(@PathVariable(value = "id") Long id){
        return portalResourceService.checkDelete(id);
    }

    /**
     * 查看角色
     * @author liyang
     * @date 2019-06-26
     * @param id :  id
     * @return : BaseVo
     */
    @GetMapping("/findRole/{id}")
    public BaseVo findRole(@PathVariable(value = "id") Long id){
        return portalResourceService.findRole(id);
    }

    /**
     * 获得所有角色
     * @author liyang
     * @date 2019-06-27
     * @return : BaseVo
     */
    @GetMapping("/roleList")
    public BaseVo findAllRole(){
        return portalResourceService.findAllRole();
    }

}
