package com.bit.module.manager.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.manager.bean.PortalContent;
import com.bit.module.manager.service.PortalSearchService;
import com.bit.module.manager.vo.PortalContentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @description: 全局搜索
 * @author: liyang
 * @date: 2019-07-04
 **/
@RestController
@RequestMapping(value = "/manager")
public class PortalSearch {

    @Autowired
    private PortalSearchService portalSearchService;

    @PostMapping("/search")
    public BaseVo keywordSearch(@RequestParam(value = "content") String content){
        return portalSearchService.keywordSearch(content);
    }

    @PostMapping("/searchHighLight")
    public BaseVo searchHighLight(@RequestParam(value = "content") String content){
        return portalSearchService.highLight(content);
    }

    @PostMapping("/batchContents")
    public BaseVo batchContents(@RequestBody PortalContentVO portalContentVO){
        return portalSearchService.batchContents(portalContentVO);
    }

    @PostMapping("/deleteAll")
    public BaseVo deleteAll(){
        return portalSearchService.deleteAll();
    }
}
