package com.bit.module.manager.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.manager.bean.AdminLogin;
import com.bit.module.manager.bean.PortalUser;
import com.bit.module.manager.vo.PortalUserVo;

/**
 * @description:
 * @author: chenduo
 * @create: 2019-05-06 15:44
 */
public interface AdminLoginService {
    /**
     * admin登陆
     * @param adminLogin
     * @return
     */
    BaseVo adminLogin(AdminLogin adminLogin);

    /**
     * admin登出
     * @return
     */
    BaseVo adminLogout();

    /**
     * mongo分页测试
     * @return
     */
    BaseVo mongotest();

    /**
     * 用户新增
     * @return
     */
    BaseVo add(PortalUser user);

    /**
     * 用户列表展示
     * @return
     */
    BaseVo findAll(PortalUserVo portalUserVo);

    /**
     * 修改用户信息（不带密码）
     * @return
     */
    BaseVo update(PortalUser portalUser);

    /**
     * 删除用户
     * @return
     */
    BaseVo delete(Long id);

    /**
     * 重置用户密码
     * @author liyang
     * @date 2019-06-27
     * @param id :  用户ID
     * @param portalUser :  重置的密码
     * @return : BaseVo
     */
    BaseVo resetPwd(Long id,PortalUser portalUser);

    /**
     * 根据ID查询用户明细
     * @author liyang
     * @date 2019-06-27
     * @param id : 用户ID
     * @return : BaseVo
     */
    BaseVo findUser(Long id);

    /**
     * 修改密码
     * @author liyang
     * @date 2019-06-27
     * @param portalUserVo : 密码集合
     * @return : BaseVo
    */
    BaseVo updatePassword(PortalUserVo portalUserVo);


    /**
     * 获取所有关联用户的角色
     * @author liyang
     * @date 2019-06-29
     * @return : BaseVo
     */
    BaseVo findUserRole();

    /**
     * 查询角色所含资源
     * @author liyang
     * @date 2019-06-29
     * @param roleId 角色ID
     * @return : BaseVo
     */
    BaseVo findRoleResource(Long roleId);
}
