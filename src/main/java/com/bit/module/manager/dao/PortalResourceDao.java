package com.bit.module.manager.dao;


import com.bit.module.manager.bean.PortalResource;
import com.bit.module.manager.bean.PortalRole;
import com.bit.module.manager.bean.PortalRoleRelResource;
import com.bit.module.manager.vo.PortalRoleVo;
import org.apache.ibatis.annotations.Param;

import javax.management.relation.Role;
import java.util.List;

/**
 * Rescource相关dao
 * @author: liyang
 * @date: 2019-06-25
 **/
public interface PortalResourceDao {

	/**
	 * 获取所有资源
	 * @return
	 */
	List<PortalResource> findResourceSql(@Param("status") Integer status);

	/**
	 * 新增角色
	 * @param role
	 */
	void addRoleSql(@Param("role") PortalRole role);

	/**
	 * 新增角色资源关系
	 * @param portalRoleRelResourceList
	 */
	void addRoleRelResourceSql(@Param("portalRoleRelResourceList") List<PortalRoleRelResource> portalRoleRelResourceList);

	/**
	 * 查询角色列表
	 * @param portalRoleVo 查询条件
	 * @return
	 */
	List<PortalRole> findRoleListPageSql(@Param("portalRoleVo") PortalRoleVo portalRoleVo);

	/**
	 * 修改角色
	 * @param portalRoleVo 修改详情
	 */
	void modfiyRoleSql(@Param("portalRoleVo") PortalRoleVo portalRoleVo);

	/**
	 * 根据ID删除角色资源对应关系
	 * @param roleId
	 */
	void deleteRoleRelResourceSql(Long roleId);

	/**
	 * 删除角色
	 * @param roleId
	 */
	void deleteRoleSql(Long roleId);

	/**
	 * 检查角色是否可以删除
	 * @param roleId id
	 * @return
	 */
	Integer checkDeleteSql(Long roleId);

	/**
	 * 根据ID查询角色明细
	 * @param roleId id
	 * @return
	 */
	PortalRoleVo findRoleSql(Long roleId);

	/**
	 * 根据角色ID 查询角色资源
	 * @param roleId
	 * @return
	 */
	List<Long> findRoleRelRoleByRoleIdSql(Long roleId);


	/**
	 * 查询角色所含资源明细
	 * @param roleId
	 * @return
	 */
	List<PortalResource> findRoleResourceDetailSql(Long roleId);
}
