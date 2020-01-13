package com.bit.module.manager.controller;

import com.bit.base.exception.BusinessException;
import com.bit.base.vo.BaseVo;
import com.bit.module.manager.bean.VideoFile;
import com.bit.module.manager.service.FileService;
import com.bit.module.manager.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileService fileService;
    @Autowired
    private VideoService videoService;

    /**
     * 上传文件
     * @param filedata :
     * @return : com.bit.base.vo.BaseVo
     * @description:
     * @author liyujun
     * @date 2018-10-13
     */
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public BaseVo uploadFileNew(@RequestParam MultipartFile filedata) {
        if (filedata != null && !filedata.isEmpty()) {
            return fileService.uploadFile(filedata);
        }else {
            throw new BusinessException("文件不允许为空");
        }
    }

    /**
     * 上传视频文件
     * @param file :
     * @return : com.bit.base.vo.BaseVo
     * @description:
     * @author liyujun
     * @date 2018-10-13
     */
    @RequestMapping(value = "/uploadVideoFile", method = RequestMethod.POST)
    public BaseVo uploadVideoFile(@RequestParam MultipartFile file,@RequestParam Integer chunkNumber) {
        if (file != null && !file.isEmpty()) {
            return fileService.uploadFile(file);
        }else {
            throw new BusinessException("文件不允许为空");
        }
    }

    @PostMapping("/video/merge")
    public BaseVo mergeFile(@RequestBody VideoFile videoFile){
        return videoService.mergeFile(videoFile);
    }
}
