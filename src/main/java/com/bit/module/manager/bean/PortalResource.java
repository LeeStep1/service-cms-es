package com.bit.module.manager.bean;

import lombok.Data;

import java.util.List;

/**
 * @description: 资源表
 * @author: liyang
 * @date: 2019-06-25
 **/
@Data
public class PortalResource {

    /**
     * 主键
     */
    private Long id;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 相关连接
     */
    private String url;

    /**
     * 系统图标
     */
    private String icon;

    /**
     * 父ID  顶级为0
     */
    private Long pid;

    /**
     *  是否显示  0  显示 1不显示
     */
    private Integer display;

    /**
     * 子资源集合
     */
    private List<PortalResource> portalResourceList;

    /**
     * 扩展字段
     */
    private String extField;
}
