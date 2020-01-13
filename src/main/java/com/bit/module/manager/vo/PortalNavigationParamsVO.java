package com.bit.module.manager.vo;

import lombok.Data;

/**
 * PortalNavigation
 * @author generator
 */
@Data
public class PortalNavigationParamsVO{

	//columns START

    /**
     * id主键 
     */	
	private Long id;
    /**
     * 导航名称
     */	
	private String navigationName;
    /**
     * 状态 0 启用  1  停用
     */	
	private Integer status;
    /**
     * 所属导航
     */	
	private Long stationId;
    /**
     * 操作人ID
     */	
	private Long operationUserId;
    /**
     * 操作人姓名
     */	
	private String operationUserName;
    private String orderBy;
    private String order;
	//columns END

}


