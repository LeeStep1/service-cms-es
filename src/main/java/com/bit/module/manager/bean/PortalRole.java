package com.bit.module.manager.bean;

import lombok.Data;

/**
 * @description: 角色表
 * @author: liyang
 * @date: 2019-06-25
 **/
@Data
public class PortalRole {

    /**
     * 主键
     */
    private Long id;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色描述
     */
    private String roleDescribe;

    /**
     * 删除标识  0 未删除  1 删除
     */
    private Integer status;
}
