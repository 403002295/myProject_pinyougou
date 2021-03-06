package com.pinyougou.shop.controller;

import entity.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import util.FastDFSClient;

@RestController
public class UploadController {
    @Value("${FILE_SERVICE_URL}")
    private String FILE_SERVICE_URL;

    @RequestMapping("/upload")
    public Result upload(MultipartFile file) {
        //1、取文件的扩展名
        String originalFilename = file.getOriginalFilename();
        String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        try {
            FastDFSClient fastDFSClient= new FastDFSClient("classpath:config/fdfs_client.conf");
            //2、执行上传操作
            String path = fastDFSClient.uploadFile(file.getBytes(), extName);
            //拼接URL 和 ip
            String url = FILE_SERVICE_URL+path;
            return new Result(true,url);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"上传失败");
        }


    }
}
