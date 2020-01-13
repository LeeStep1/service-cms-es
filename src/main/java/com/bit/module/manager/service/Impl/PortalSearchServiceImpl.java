package com.bit.module.manager.service.Impl;

import com.bit.base.vo.BaseVo;
import com.bit.base.vo.SuccessVo;
import com.bit.module.manager.bean.PortalContent;
import com.bit.module.manager.dao.PortalContentDao;
import com.bit.module.manager.dao.PortalContentEsDao;
import com.bit.module.manager.service.PortalSearchService;
import com.bit.module.manager.vo.PortalContentVO;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: liyang
 * @date: 2019-07-04
 **/
@Service
public class PortalSearchServiceImpl implements PortalSearchService {

    @Autowired
    private PortalContentEsDao portalContentEsDao;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private PortalContentDao portalContentDao;

    /**
     * 根据查询词进行全文检索
     * @author liyang
     * @date 2019-07-04
     * @param content : 查询词
     * @return : BaseVo
    */
    @Override
    public BaseVo keywordSearch(String content) {

        BoolQueryBuilder bq = QueryBuilders.boolQuery();

        bq.should(QueryBuilders.matchQuery("title",content))
                .should(QueryBuilders.matchQuery("content",content))
                .should(QueryBuilders.matchQuery("contentName",content));

        List<PortalContent> portalContentList = new ArrayList<>();

        HighlightBuilder hbuilder = new HighlightBuilder();
        hbuilder.field("title").field("content").field("contentName");

        Iterable<PortalContent> pi = portalContentEsDao.search(bq);

        pi.forEach(p -> portalContentList.add(p));

        BaseVo baseVo = new BaseVo();
        baseVo.setData(portalContentList);
        return baseVo;
    }

    /**
     * mysql 同步ES
     * @param portalContent
     * @return
     */
    @Override
    public BaseVo batchContents(PortalContentVO portalContentVO){

        Iterable<PortalContent> portalContents = portalContentDao.getContentsByCreateTime(portalContentVO);


        portalContentEsDao.saveAll(portalContents);

        return new SuccessVo();
    }

    /**
     * 全部删除
     * @return
     */
    @Override
    public BaseVo deleteAll(){
        portalContentEsDao.deleteAll();
        return new SuccessVo();
    }




    public BaseVo highLight(String content){
        Integer pageNum = 0;
        Integer pageSize = 10;
        Pageable pageable = PageRequest.of(pageNum, pageSize);

        //SearchResponse searchResponse;

        SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(
                QueryBuilders.boolQuery().should(QueryBuilders.matchQuery("title",content))
                .should(QueryBuilders.matchQuery("content",content))
                .should(QueryBuilders.matchQuery("contentName",content)))
                .withHighlightFields(new HighlightBuilder.Field("title"),new HighlightBuilder.Field("content"),new HighlightBuilder.Field("contentName") )
                .build();
        searchQuery.setPageable(pageable);

//        System.out.println(searchQuery);
//
       AggregatedPage<PortalContent> ideas = elasticsearchTemplate.queryForPage(searchQuery, PortalContent.class);
//
        //System.out.println(searchResponse);
//
//        BaseVo baseVo = new BaseVo();
//        baseVo.setData(ideas);
//
//        return baseVo;


        AggregatedPage<PortalContent> portalContentPage = elasticsearchTemplate.queryForPage(searchQuery,PortalContent.class, new SearchResultMapper() {
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {

                List<PortalContent> chunk = new ArrayList<>();
                for (SearchHit searchHit : response.getHits()) {
                    if (response.getHits().getHits().length <= 0) {
                        return null;
                    }
                    PortalContent portalContent = new PortalContent();
                    //title or content or contentName
                    HighlightField title = searchHit.getHighlightFields().get("title");
                    if (title != null) {
                        portalContent.setTitle(title.fragments()[0].toString());
                    }

                    HighlightField contentHighlight = searchHit.getHighlightFields().get("content");
                    if (contentHighlight != null) {
                        portalContent.setContent(contentHighlight.fragments()[0].toString());
                    }

                    HighlightField contentName = searchHit.getHighlightFields().get("contentName");
                    if (contentName != null) {
                        portalContent.setContentName(contentName.fragments()[0].toString());
                    }

                    chunk.add(portalContent);
                }
                if (chunk.size() > 0) {
                    return new AggregatedPageImpl<>((List<T>) chunk);
                }
                return null;
            }
        });

        BaseVo baseVo = new BaseVo();
        baseVo.setData(portalContentPage);

        return baseVo;


    }


}
