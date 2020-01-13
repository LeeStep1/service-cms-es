package com.bit.module.manager.service;

import com.bit.base.vo.BaseVo;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    /**
     * @param filedata
     * @return
     */
    BaseVo uploadFile(MultipartFile filedata);
}
