package com.bit.module.manager.dao;

import com.bit.module.manager.bean.FileInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 文件对象的crud
 * @author lyj
 */
@Component
public interface FileInfoDao {


    /**
     * @param fileInfo
     */
    void insert(FileInfo fileInfo);


    /**
     * @param fileId
     * @return
     */
    List<FileInfo> query(@Param("fileId") Long fileId);



    /**
     * 根据id主键查询
     * @param id
     * @return
     */
    FileInfo findById(@Param(("id")) Long id);

    /**
     * 根据id批量删除
     * @param ids
     */
    void batchDelete(@Param(value = "ids") List<Long> ids);


}
