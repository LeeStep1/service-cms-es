package com.bit.module.manager.service.Impl;

import com.bit.base.vo.BaseVo;
import com.bit.module.manager.bean.FileInfo;
import com.bit.module.manager.dao.FileInfoDao;
import com.bit.module.manager.service.FileService;
import com.bit.util.FastDFSClient;
import com.bit.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Transactional
public class FileServiceImpl implements FileService {

    @Autowired
    private FastDFSClient fastDFSClient;

    @Autowired
    private FileInfoDao fileInfoDao;


    @Value("${fastdfs.address}")
    private String fastAddress;


    @Override
    public BaseVo uploadFile(MultipartFile filedata) {
        BaseVo baseVo = new BaseVo();
        FileInfo info = new FileInfo();
        try {
            info.setFileName(FileUtil.getFileName(filedata.getOriginalFilename()));
            info.setSuffix(FileUtil.getExtensionName(filedata.getOriginalFilename()));
            info.setFileSize(filedata.getSize());
            String path = fastDFSClient.uploadFile(filedata.getBytes(), filedata.getOriginalFilename());
            info.setPath(path);
            fileInfoDao.insert(info);
            FileInfo fileInfo = fileInfoDao.findById(info.getId());
            if (fileInfo != null){
                fileInfo.setPath(fastAddress + "/" + fileInfo.getPath());
            }
            baseVo.setData(fileInfo);
            return baseVo;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baseVo;
    }
}
