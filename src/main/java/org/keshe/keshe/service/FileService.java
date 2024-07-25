package org.keshe.keshe.service;

import org.keshe.keshe.pojo.Result;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
    //上传服务端压缩文件
    Result uploadZip(MultipartFile zipfile);

    Result getFiles(String path);

    Result delFiles(String path);

    Result newFilePack(String path);

    Result newFile(String name,int num,String path) throws IOException;

    Result getFileContent(String path);

    Result saveFile(String path, String content);

    Result<String> uploadfile(MultipartFile file, String path);
}
