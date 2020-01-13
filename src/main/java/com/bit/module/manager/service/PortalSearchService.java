package com.bit.module.manager.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.manager.bean.PortalContent;
import com.bit.module.manager.vo.PortalContentVO;

/**
 * @description:
 * @author: liyang
 * @date: 2019-07-04
 **/
public interface PortalSearchService {

    BaseVo keywordSearch(String content);

    BaseVo highLight(String content);

    BaseVo batchContents(PortalContentVO portalContentVO);

    BaseVo deleteAll();

}
