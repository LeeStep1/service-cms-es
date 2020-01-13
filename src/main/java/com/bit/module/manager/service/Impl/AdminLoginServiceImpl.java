package com.bit.module.manager.service.Impl;

import com.alibaba.fastjson.JSON;
import com.bit.base.dto.UserInfo;
import com.bit.base.exception.BusinessException;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.base.vo.SuccessVo;
import com.bit.common.Const;
import com.bit.module.manager.bean.*;
import com.bit.module.manager.dao.PortalResourceDao;
import com.bit.module.manager.dao.PortalUserDao;
import com.bit.module.manager.service.AdminLoginService;
import com.bit.module.manager.vo.PortalUserVo;
import com.bit.module.manager.vo.RefreshTokenVO;
import com.bit.module.mqCore.MqBean.UserMessage;
import com.bit.util.MongoUtil;
import com.bit.util.StringRandom;
import com.bit.utils.CacheUtil;
import com.bit.utils.MD5Util;
import com.bit.utils.UUIDUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.bit.common.Const.RESOURCEPARENTFLG;
import static com.bit.common.cmsenum.cmsEnum.DISABLE_FLAG;
import static com.bit.common.cmsenum.cmsEnum.UNDEL_FLAG;
import static com.bit.common.cmsenum.cmsEnum.USING_FLAG;


/**
 * @description:
 * @author: chenduo
 * @create: 2019-05-06 15:44
 */
@Service("adminLoginService")
public class AdminLoginServiceImpl extends BaseService implements AdminLoginService {
    @Autowired
    private CacheUtil cacheUtil;
    @Value("${atToken.expire}")
    private String atTokenExpire;
    @Value("${rtToken.expire}")
    private String rtTokenExpire;
    @Autowired
    private MongoUtil mongoUtil;

    /**
     * 用户相关dao
     */
    @Autowired
    private PortalUserDao portalUserDao;

    /**
     * 资源数据相关dao
     */
    @Autowired
    private PortalResourceDao portalResourceDao;

    /**
     * admin登陆
     * @param adminLogin
     * @return
     */
    @Override
    public BaseVo adminLogin(AdminLogin adminLogin) {
        String username = adminLogin.getUserName();
        String password = adminLogin.getPassWord();

        //根据用户名密码查询用户是否存在
        PortalUser portalUser = portalUserDao.findByUsername(adminLogin.getUserName());

        if(portalUser == null){
            throw new BusinessException("用户不存在！");
        }

        if(Integer.valueOf(DISABLE_FLAG.getCode()).equals(portalUser.getStatus())){
            throw new BusinessException("该用户已被停用！");
        }

        if(portalUser!=null){

            //校验验证码
            String code = (String) cacheUtil.get(Const.REDIS_KEY_CAPTCHA + ":" + adminLogin.getCode());

            //获取到验证码后 无论登录成功或者失败，都删除原来的验证码
            cacheUtil.del(Const.REDIS_KEY_CAPTCHA + ":" + adminLogin.getCode());

            //校验验证码
            if(!adminLogin.getCode().equals(code)){
                throw new BusinessException("验证码错误");
            }

            //校验密码
            String pw = MD5Util.compute(adminLogin.getPassWord() + portalUser.getSalt());
            if (!pw.equals(portalUser.getPassword())) {
                throw new BusinessException("密码错误");
            }

            UserInfo userInfo = new UserInfo();
            String token = UUIDUtil.getUUID();
            userInfo.setToken(token);
            userInfo.setId(portalUser.getId());
            userInfo.setUsername(adminLogin.getUserName());
            userInfo.setTid(adminLogin.getTerminalId());
            userInfo.setRealName(portalUser.getRealName());
            String userJson = JSON.toJSONString(userInfo);
            Integer tid = null;
            if (adminLogin.getTerminalId().equals(Const.OA_DOOR)){
                tid = Const.OA_DOOR;
            }
            if (adminLogin.getTerminalId().equals(Const.PB_DOOR)){
                tid = Const.PB_DOOR;
            }

            cacheUtil.set(Const.TOKEN_PREFIX+ tid+":"+token, userJson,Long.valueOf(atTokenExpire));

            //rt token 失效时间为7天
            String rtToken = UUIDUtil.getUUID();
            RefreshTokenVO refreshTokenVO = new RefreshTokenVO();
            refreshTokenVO.setUserInfo(userInfo);
            refreshTokenVO.setAtKey(Const.TOKEN_PREFIX+tid+":"+token);
            String rtJson = JSON.toJSONString(refreshTokenVO);
            cacheUtil.set(Const.REFRESHTOKEN_TOKEN_PREFIX + adminLogin.getTerminalId() + ":" + rtToken, rtJson, Long.valueOf(rtTokenExpire));

            Map map = new HashMap<>();
            map.put("token", token);
            map.put("refreshToken", rtToken);
            map.put("id",portalUser.getId());
            map.put("username",adminLogin.getUserName());
            map.put("realName",portalUser.getRealName());
            map.put("roleId",portalUser.getRoleId());

            BaseVo baseVo = new BaseVo();
            baseVo.setData(map);
            return baseVo;

        }else {
            throw new BusinessException("该用户不存在！");
        }
    }

    /**
     * admin登出
     * @return
     */
    @Override
    public BaseVo adminLogout() {

        //获取token
        UserInfo userInfo = getCurrentUserInfo();
        String token = userInfo.getToken();
        String terminalId = userInfo.getTid().toString();

        cacheUtil.del("token:"+terminalId+":"+token);
        return successVo();

    }

    /**
     * mongo分页测试
     * @return
     */
    @Override
    public BaseVo mongotest() {
        Query query = new Query();
        Query total = new Query();
        Criteria criteria = Criteria.where("msgType").is(2);
        Sort.Order order = new Sort.Order(Sort.Direction.DESC,"recTime");
        PageResult<UserMessage> message = mongoUtil.listPage(query, total, criteria, order, 1, 50, "message", UserMessage.class);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(message);
        return baseVo;
    }

    /**
     * 用户新增
     * @author liyang
     * @date 2019-06-11
     * @param user : 用户详情
     * @return : BaseVo
    */
    @Override
    @Transactional
    public BaseVo add(PortalUser user) {

        //根据用户名密码查询用户是否存在
        PortalUser portalUser = portalUserDao.findByUsername(user.getUserName());

        if(portalUser == null){
            //随机密码盐
            String salt = StringRandom.getStringRandom(Const.RANDOM_PASSWORD_SALT);

            //密码和盐=新密码  md5加密新密码
            String password= MD5Util.compute(Const.RESET_PASSWORD + salt);
            user.setPassword(password);
            user.setSalt(salt);

            //状态  0 启用  1 停用
            user.setStatus(USING_FLAG.getCode());

            //新增时间
            user.setInsertTime(new Date());

            //修改时间
            user.setUpdateTime(new Date());

            portalUserDao.addNew(user);

            //用户插入后插入用户角色表
            portalUserDao.addRoleRelUser(user.getId(),user.getRoleId());

            return successVo();
        }else {
            throw new BusinessException("该用户已存在！");
        }


    }

    /**
     * 用户列表展示
     * @author liyang
     * @date 2019-06-11
     * @return : BaseVo
    */
    @Override
    public BaseVo findAll(PortalUserVo portalUserVo) {
        PageHelper.startPage(portalUserVo.getPageNum(), portalUserVo.getPageSize());
        List<PortalUser> portalUserLIst = portalUserDao.findAll(portalUserVo);
        PageInfo<PortalUser> pageInfo = new PageInfo<PortalUser>(portalUserLIst);

        BaseVo baseVo = new BaseVo();
        baseVo.setData(pageInfo);
        return baseVo;
    }

    /**
     * 修改用户信息（不带密码）
     * @author liyang
     * @date 2019-06-27
     * @param portalUser : 用户详情
     * @return : BaseVo
    */
    @Override
    @Transactional
    public BaseVo update(PortalUser portalUser) {

        //如果是修改密码
        if(portalUser.getPassword() !=null && !("").equals(portalUser.getPassword())){

            //随机密码盐
            String salt = StringRandom.getStringRandom(Const.RANDOM_PASSWORD_SALT);

            //密码和盐=新密码  md5加密新密码
            String password= MD5Util.compute(portalUser.getPassword() + salt);
            portalUser.setPassword(password);
            portalUser.setSalt(salt);
        }

        //更新人物所属
        portalUser.setUpdateTime(new Date());
        portalUserDao.update(portalUser);

        //如果是更新所属角色,则需要更新角色任务关联表
        if(portalUser.getRoleId() != null){
            portalUserDao.updateRoleRelUser(portalUser.getId(),portalUser.getRoleId());
        }

        return successVo();
    }

    /**
     * 删除用户
     * @author liyang
     * @date 2019-06-11
     * @param id : 用户ID
     * @return : BaseVo
    */
    @Override
    public BaseVo delete(Long id) {
        portalUserDao.delete(id);
        return successVo();
    }

    /**
     * 重置用户密码
     * @author liyang
     * @date 2019-06-27
     * @param id : 用户ID
     * @param portalUser : 用户密码
     * @return : BaseVo
    */
    @Override
    public BaseVo resetPwd(Long id, PortalUser portalUser) {

        //随机密码盐
        String salt = StringRandom.getStringRandom(Const.RANDOM_PASSWORD_SALT);

        //密码和盐=新密码  md5加密新密码
        String newPwd= MD5Util.compute(portalUser.getPassword() + salt);
        portalUser.setPassword(newPwd);
        portalUser.setSalt(salt);
        portalUser.setId(id);
        portalUserDao.update(portalUser);

        return successVo();
    }

    /**
     * 根据ID查询用户详情
     * @author liyang
     * @date 2019-06-27
     * @param id :用户ID
     * @return : BaseVo
    */
    @Override
    public BaseVo findUser(Long id) {

        PortalUser portalUser = portalUserDao.findUserSql(id);

        BaseVo baseVo = new BaseVo();
        baseVo.setData(portalUser);

        return baseVo;
    }

    /**
     * 修改密码
     * @author liyang
     * @date 2019-06-27
     * @param portalUserVo : 密码集合
     * @return : BaseVo
     */
    @Override
    public BaseVo updatePassword(PortalUserVo portalUserVo) {

        UserInfo userInfo = getCurrentUserInfo();

        //根据ID获取用户
        PortalUser pu = portalUserDao.findByUserId(userInfo.getId());

        //校验密码
        String pw = MD5Util.compute(portalUserVo.getOldPassword() + pu.getSalt());
        if (!pw.equals(pu.getPassword())) {
            throw new BusinessException("旧密码错误");
        }

        //修改新密码
        PortalUser portalUserUpdate = new PortalUser();

        //随机密码盐
        String salt = StringRandom.getStringRandom(Const.RANDOM_PASSWORD_SALT);

        //密码和盐=新密码  md5加密新密码
        String password= MD5Util.compute(portalUserVo.getPassword() + salt);
        portalUserUpdate.setPassword(password);
        portalUserUpdate.setSalt(salt);
        portalUserUpdate.setId(userInfo.getId());

        portalUserDao.update(portalUserUpdate);

        return successVo();
    }

    /**
     * 获取所有关联用户的角色
     * @author liyang
     * @date 2019-06-29
     * @return : BaseVo
    */
    @Override
    public BaseVo findUserRole() {
        List<PortalRole> portalRoleList = portalUserDao.findUserRoleSql();

        BaseVo baseVo = new BaseVo();
        baseVo.setData(portalRoleList);

        return baseVo;
    }

    /**
     * 查询角色所含资源
     * @author liyang
     * @date 2019-06-29
     * @param roleId 角色ID
     * @return : BaseVo
     */
    @Override
    public BaseVo findRoleResource(Long roleId) {

        //根据角色查询所有资源
        List<PortalResource> portalResourceList = portalResourceDao.findRoleResourceDetailSql(roleId);

        //根据父ID进行分组
        Map<Long,List<PortalResource>> resourceMap = portalResourceList.stream().collect(Collectors.groupingBy(PortalResource::getPid));

        //获取所有父类集合
        List<PortalResource> portalResourceParentList = resourceMap.get(RESOURCEPARENTFLG);

        //将子类集合插入所属父类
        for(PortalResource portalResource : portalResourceParentList){
            portalResource.setPortalResourceList(resourceMap.get(portalResource.getId()));
        }

        BaseVo baseVo = new BaseVo();
        baseVo.setData(portalResourceParentList);

        return baseVo;
    }
}
