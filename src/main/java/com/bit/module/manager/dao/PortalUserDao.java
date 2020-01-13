package com.bit.module.manager.dao;

import com.bit.module.manager.bean.PortalRole;
import com.bit.module.manager.bean.PortalUser;
import com.bit.module.manager.vo.PortalUserVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * User管理的Dao
 * @author 
 *
 */
@Repository
public interface PortalUserDao {

	/**
	 * 根据条件查询User
	 * @param portalUser
	 * @return
	 */
	List<PortalUser> findByConditionPage(PortalUser portalUser);

	/**
	 * 查询所有User
	 * @return
	 */
	List<PortalUser> findAll(@Param("portalUserVo") PortalUserVo portalUserVo);

	/**
	 * 通过主键查询单个User
	 * @param id
	 * @return
	 */
	PortalUser findById(@Param(value = "id") Long id);

	/**
	 * 保存User新方法
	 * @param portalUser
	 */
	void addNew(PortalUser portalUser);

	/**
	 * 更新User
	 * @param portalUser
	 */
	void update(PortalUser portalUser);

	/**
	 * 删除User
	 * @param id
	 */
	void delete(@Param(value = "id") Long id);


	/**
	 * 根据用户名查询用户信息
	 * @param username
	 * @return
	 */
	PortalUser findByUsername(@Param(value = "userName") String username);

	/**
	 * 根据ID查询用户详情
	 * @param id 用户ID
	 * @return
	 */
	PortalUser findUserSql(Long id);

	/**
	 * 根据ID查询用户
	 * @param id 用户ID
	 * @return
	 */
	PortalUser findByUserId(Long id);

	/**
	 * 插入角色用户关联表
	 * @param userId 用户ID
	 * @param roleId 角色ID
	 */
	void addRoleRelUser(@Param(value = "userId")Long userId,@Param(value = "roleId")Long roleId);

	/**
	 * 更新角色用户关联
	 * @param userId 用户ID
	 * @param roleId 角色ID
	 */
	void updateRoleRelUser(@Param(value = "userId")Long userId,@Param(value = "roleId")Long roleId);

	/**
	 * 获取所有关联用户的角色
	 * @return List<PortalRole>
	 */
	List<PortalRole> findUserRoleSql();
}
