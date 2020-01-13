package com.bit.module.manager.controller;

import com.bit.base.vo.BaseVo;

import com.bit.module.manager.bean.Dict;
import com.bit.module.manager.service.DictService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Dict的相关请求
 * @author zhangjie
 * @date 2018-12-28
 */
@RestController
@RequestMapping(value = "/dict")
public class DictController {

    private static final Logger logger = LoggerFactory.getLogger(DictController.class);

    @Autowired
    private DictService dictService;


    /**
     * 通过主键查询单个Dict
     * @param id
     * @return Dict
     */
    @GetMapping("/findById/{id}")
    public BaseVo findById(@PathVariable(value = "id") Long id){
        BaseVo baseVo = new BaseVo();
        baseVo.setData(dictService.findById(id));
        return baseVo;
    }



    /**
     * 根据模块查询字典
     * @param module
     */
    @GetMapping("/findByModule/{module}")
    public BaseVo findByModule(@PathVariable(value = "module") String module){
        List<Dict> dictList=dictService.findByModule(module);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(dictList);
        return baseVo;
    }

    /**
     * 根据模块和code查询，用于回显
     * @param dict
     * @return
     */
    @PostMapping("/findByModuleAndCode")
    public BaseVo findByModuleAndCode(@RequestBody Dict dict){
        return dictService.findByModuleAndCode(dict);
    }



    /**
     * 根据模块名称集合批量查询字典表
     * @param dict
     * @return
     */
    @PostMapping("/findByModules")
    public BaseVo findByModules(@RequestBody Dict dict){
        return dictService.findByModules(dict);
    }



}
