package com.bit.module.manager.dao;

import com.bit.module.manager.bean.PortalContent;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @description: 内容表ES操作
 * @author: liyang
 * @date: 2019-07-02
 **/
public interface PortalContentEsDao extends ElasticsearchRepository<PortalContent,Long>{

}
