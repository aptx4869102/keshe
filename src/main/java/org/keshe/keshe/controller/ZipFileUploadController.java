package org.keshe.keshe.controller;


import org.keshe.keshe.pojo.Result;
import org.keshe.keshe.service.FileService;
import org.keshe.keshe.service.ServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;


@RestController
@RequestMapping("/api")
public class ZipFileUploadController {
    @Autowired
    private FileService fileservice;
    @Autowired
    private ServerService serverservice;

    @PostMapping("/zipupload")
    public Result<String> upload(MultipartFile zipfile,String cmd,String nickname) throws IOException {
        //把文件存在服务器服务端实例文件中
        //调用service层方法
        Result result = new Result();
        result = fileservice.uploadZip(zipfile);
        //把服务器实例存进数据库
        serverservice.register(result.getData().toString(),cmd,nickname);
        return result;
    }

    @PostMapping("/upload")
    public Result<String> uploadfile(MultipartFile file,String path) throws IOException {
      return fileservice.uploadfile(file,path);
    }

}
