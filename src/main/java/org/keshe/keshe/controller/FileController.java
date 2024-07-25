package org.keshe.keshe.controller;

import org.keshe.keshe.pojo.Result;
import org.keshe.keshe.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/api/file")
public class FileController {
    @Autowired
    private FileService fileService;

    //获得文件夹下文件信息
    @PutMapping("/getfile")
    public Result getfiles(String path) {
        File directory = new File("");//设定为当前文件夹
        String str = directory.getAbsolutePath();//当前路径
        //如果给的是绝对路径
        if (path.startsWith(str))
            return fileService.getFiles(path);
        else
            return fileService.getFiles(str + "\\severs\\" + path);
    }
    //获得文件内容
    @PutMapping("/getfilecontent")
    public Result getfilecontent(String path) {
        return fileService.getFileContent(path);
    }
    //保存文件
    @PutMapping("/savefile")
    public Result savefile(String path, String content) {
        return fileService.saveFile(path,content);
    }
    //删除文件
    @PostMapping("/delete")
    public Result delfiels(String path) {
        return fileService.delFiles(path);
    }

    //新建文件夹
    @PostMapping("/newfilepack")
    public Result newfilepack(String path) {
        return fileService.newFilePack(path);
    }

    //新建文件
    @PostMapping("/newfile")
    public Result newfile(String name,int num,String path) throws IOException {
        File directory = new File("");//设定为当前文件夹
        String str = directory.getAbsolutePath();//当前路径
        String realpath ;
        //如果给的是绝对路径
        if (path.startsWith(str)) realpath = path;
        else realpath = str + "\\severs\\" + path;
        return fileService.newFile(name,num,realpath);
    }

}
