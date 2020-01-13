package com.bit.module.manager.bean;

import lombok.Data;

/**
 * @description:
 * @author: liyang
 * @date: 2019-06-25
 **/
@Data
public class PortalRoleRelUser {

    /**
     * 主键
     */
    private Long id;

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 用户ID
     */
    private Long userId;
}
