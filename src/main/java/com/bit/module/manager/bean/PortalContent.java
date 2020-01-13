package com.bit.module.manager.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

/**
 * PortalContent
 * @author liuyancheng
 */
@Data
@Document(indexName = "cms", type = "content", shards = 1, replicas = 1, indexStoreType = "fs", refreshInterval = "-1")
public class PortalContent {

	//columns START

    /**
     * 主键ID
     */	
	private Long id;
    /**
     * 内容名称
     */
//    @Field(type = FieldType.keyword, analyzer = "ik_smart")
	private String contentName;
    /**
     * 标题
     */
//    @Field(type = FieldType.keyword, analyzer = "ik_smart")
	private String title;
    /**
     * 封面图片ID
     */	
	private Long coverId;
    /**
     * 封面路径
     */	
	private String coverUrl;
    /**
     * 视频ID
     */	
	private Long videoId;
    /**
     * 视频路径
     */	
	private String videoUrl;

    /**
     * 视频名称
     */
    private String videoFileName;
    /**
     * 内容
     */
//    @Field(type = FieldType.keyword, analyzer = "ik_smart")
	private String content;
    /**
     * 发布状态 0 未发布 1 已发布
     */	
	private Integer publishStatus;
    /**
     * 栏目ID
     */	
	private Long categoryId;

    /**
     * 栏目名称
     */
    private String categoryName;

    /**
     * 站点ID
     */	
	private Long stationId;

    /**
     * 站点名称
     */
    private String stationName;
    /**
     * 发布时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @Field(type = FieldType.Date, format = DateFormat.custom,pattern = "yyyy-MM-dd HH:mm:ss")
	private Date publishTime;
    /**
     * 创建人ID
     */	
	private Long operationUserId;
    /**
     * 操作人姓名
     */	
	private String operationUserName;

    /**
     * 内容状态 0 正常 1 已删除
     */	
	private Integer status;
    /**
     * 副标题
     */
    private String subTitle;
    /**
     * 发布方
     */
    private String publisher;


    /**
     * 发布时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @Field(type = FieldType.Date, format = DateFormat.custom,pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

	//columns END

}


