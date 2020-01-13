package com.bit.module.manager.bean;

import lombok.Data;

/**
 * @description: 资源角色关联
 * @author: liyang
 * @date: 2019-06-25
 **/
@Data
public class PortalRoleRelResource {

    /**
     * 主键
     */
    private Long id;

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 资源ID
     */
    private Long ResourceId;
}
