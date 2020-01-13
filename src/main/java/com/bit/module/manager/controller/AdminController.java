package com.bit.module.manager.controller;

import cn.apiclub.captcha.Captcha;
import cn.apiclub.captcha.backgrounds.GradiatedBackgroundProducer;
import cn.apiclub.captcha.gimpy.RippleGimpyRenderer;
import cn.apiclub.captcha.text.producer.DefaultTextProducer;
import cn.apiclub.captcha.text.renderer.DefaultWordRenderer;
import cn.apiclub.captcha.text.renderer.WordRenderer;
import com.bit.base.exception.BusinessException;
import com.bit.base.vo.BaseVo;
import com.bit.common.Const;
import com.bit.module.manager.bean.AdminLogin;
import com.bit.module.manager.bean.PortalUser;
import com.bit.module.manager.service.AdminLoginService;
import com.bit.module.manager.vo.PortalUserVo;
import com.bit.utils.CacheUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.awt.*;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: chenduo
 * @create: 2019-05-06 15:37
 */
@RestController
@RequestMapping("/manager/user")
public class AdminController {

    @Autowired
    private AdminLoginService adminLoginService;

    @Autowired
    private CacheUtil cacheUtil;

    private static int captchaW = 100;  //图片验证码的宽
    private static int captchaH = 40;   //图片验证码的高
    private static int codeCount = 4;   //值得位数
    private static char[] codeNumber = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    /**
     * admin登陆
     * @param adminLogin
     * @return
     */
    @PostMapping(value = "/login")
    public BaseVo adminLogin(@RequestBody AdminLogin adminLogin){
        return adminLoginService.adminLogin(adminLogin);
    }

    /**
     * admin登出
     * @return
     */
    @GetMapping(value = "/logout")
    public BaseVo adminLogout(){
        return adminLoginService.adminLogout();
    }

    /**
     * mongo分页测试
     * @return
     */
    @PostMapping(value = "/mongo")
    public BaseVo mongotest(){
        return adminLoginService.mongotest();
    }

    /**
     * 用户新增
     * @author liyang
     * @date 2019-06-11
     * @param user : 用户详情
     * @return : BaseVo
    */
    @PostMapping("/add")
    public BaseVo addUser(@Valid @RequestBody PortalUser user){
        return adminLoginService.add(user);
    }

    /**
     * 用户列表展示
     * @author liyang
     * @date 2019-06-11
     * @return : BaseVo
     */
    @PostMapping("/findAll")
    public BaseVo findAll(@RequestBody PortalUserVo portalUserVo){
        return adminLoginService.findAll(portalUserVo);
    }

    /**
     * 修改用户信息（不带密码）
     * @author liyang
     * @date 2019-06-11
     * @return : BaseVo
     */
    @PostMapping("/update")
    public BaseVo update(@RequestBody PortalUser portalUser){
        return adminLoginService.update(portalUser);
    }

    /**
     * 删除用户
     * @author liyang
     * @date 2019-06-11
     * @return : BaseVo
     */
    @DeleteMapping("/delete/{id}")
    public BaseVo update(@PathVariable Long id){
        return adminLoginService.delete(id);
    }


    /**
     * 获取图形验证码
     * @author liyang
     * @date 2019-06-11
    */
    @GetMapping("/getCode")
    public void createCode(HttpServletRequest request, HttpServletResponse response) throws Exception {

        //创建验证码模型，指定图片的宽和高
        Captcha.Builder captcha = new Captcha.Builder(captchaW, captchaH);
        //设置字体的颜色和大小，若list内放置多个颜色或字体，则每次生成随机使用其中一个
        List<Font> fonts = new ArrayList<>();
        List<Color> colors = new ArrayList<>();
        fonts.add(new Font("Arial", Font.BOLD, 32));
        colors.add(Color.lightGray);
        WordRenderer wr = new DefaultWordRenderer(colors, fonts);
        //将数字放入到图片上
        captcha.addText(new DefaultTextProducer(codeCount, codeNumber), wr);
        captcha.addBackground(new GradiatedBackgroundProducer(Color.GRAY,Color.gray));
        //为验证码值设置边框效果---无
        captcha.gimp(new RippleGimpyRenderer());
        //图片验证码生成
        captcha.build();
        Captcha captchas = captcha.build();
        //将验证码以<key,value>形式缓存到redis
        cacheUtil.set(Const.REDIS_KEY_CAPTCHA + ":" + captchas.getAnswer(), captchas.getAnswer(), 60);
        OutputStream out = response.getOutputStream();
        try {
            ImageIO.write(captchas.getImage(), "jpg", out);
        } catch (Exception e) {
            throw new BusinessException("图片验证码生成出错");
        }finally {
            out.close();
        }

    }

    /**
     * 重置用户密码
     * @author liyang
     * @date 2019-06-27
     * @param id :  用户ID
     * @param portalUser :  重置的密码
     * @return : BaseVo
    */
    @PostMapping("/reset/{id}")
    public BaseVo resetPwd(@PathVariable(value = "id") Long id, @RequestBody PortalUser portalUser){
        return adminLoginService.resetPwd(id,portalUser);
    }

    /**
     * 根据ID查询用户明细
     * @author liyang
     * @date 2019-06-27
     * @param id : 用户ID
     * @return : BaseVo
    */
    @GetMapping("/find/{id}")
    public BaseVo findUser(@PathVariable(value = "id") Long id){
        return adminLoginService.findUser(id);
    }

    /**
     * 密码修改
     * @author liyang
     * @date 2019-06-27
     * @param portalUserVo : 密码集合
     * @return : BaseVo
    */
    @PostMapping("/updatePwd")
    public BaseVo updatePwd(@RequestBody PortalUserVo portalUserVo){

        return adminLoginService.updatePassword(portalUserVo);

    }

    /**
     * 获取所有关联用户的角色
     * @author liyang
     * @date 2019-06-29
     * @return : BaseVo
    */
    @GetMapping("/userRole")
    public BaseVo findUserRole(){
        return adminLoginService.findUserRole();
    }

    /**
     * 查询角色所含资源
     * @author liyang
     * @date 2019-06-29
     * @param roleId 角色ID
     * @return : BaseVo
    */
    @GetMapping("/roleResource/{roleId}")
    public BaseVo findRoleResource(@PathVariable(value = "roleId") Long roleId){
        return adminLoginService.findRoleResource(roleId);
    }
}
