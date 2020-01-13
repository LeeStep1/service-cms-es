package com.bit.module.manager.vo;

import com.bit.base.vo.BasePageVo;
import com.bit.module.manager.bean.PortalResource;
import lombok.Data;

import java.util.List;

/**
 * @description: 角色表
 * @author: liyang
 * @date: 2019-06-25
 **/
@Data
public class PortalRoleVo extends BasePageVo{

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

    /**
     * 是否修改资源 0  需要修改  1  不需要修改
     */
    private Integer resourceFlg;

    /**
     * 角色和资源关系集合
     */
    private List<Long> resourceList;

    /**
     * 资源明细
     */
    private List<PortalResource> portalResourceList;
}
