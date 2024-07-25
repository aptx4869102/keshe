package org.keshe.keshe.service.impl;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.keshe.keshe.pojo.FileInfo;
import org.keshe.keshe.pojo.Result;
import org.keshe.keshe.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    @Override
    public Result uploadZip(MultipartFile zipfile) {
        File directory = new File("");//设定为当前文件夹
        String str = directory.getAbsolutePath();//当前路径
        String fileName = zipfile.getOriginalFilename();//上传的文件名
        String suffix = fileName.substring(fileName.lastIndexOf("."));//后缀名(.zip)
        UUID name = UUID.randomUUID();
        File file = new File(str + "\\severs\\1" + fileName);//转的file文件
        File severfile = new File(str + "\\severs\\" + name);//服务端文件夹
        if (!severfile.exists()) {
            severfile.mkdirs();
        }
        if (suffix.equals(".zip")) {
            //file.transferTo(new File(str+"\\severs\\"+fileName));
            try {
                //把MultipartFile转化为File
                FileCopyUtils.copy(zipfile.getBytes(), file);
                //
                ZipFile zip = new ZipFile(file);
                zip.setFileNameCharset("GBK");
                zip.extractAll(str + "\\severs\\" + name + "\\");
            } catch (IOException | ZipException e) {
                System.out.println(e.getMessage());
                return Result.error(e.getMessage());
            }
            //解压成功后删除压缩包(2个)
            file.delete();
            severfile.delete();
            System.out.println(name);
            return Result.success(name);
        } else {
            System.out.println("沙巴傻逼:" + suffix);
            return Result.error("请上传zip文件");
        }

    }

    @Override
    public Result<String> uploadfile(MultipartFile file, String path) {
        File directory = new File("");//设定为当前文件夹
        String str = directory.getAbsolutePath();//当前路径
        String fileName = file.getOriginalFilename();
        if (path.startsWith(str)) {
            try {
                Files.write(Path.of(path + "\\" + fileName), file.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                Files.write(Path.of(str + "\\severs\\" + path + "\\" + fileName), file.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return Result.success("新建成功");
    }

    @Override
    public Result getFiles(String directoryPath) {
        System.out.println("傻逼目录是:" + directoryPath);
        File directory = new File(directoryPath);
        List<FileInfo> fileList = new ArrayList<>();
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    FileInfo fileInfo = new FileInfo();
                    fileInfo.setName(file.getName());
                    if (file.isDirectory()) {
                        fileInfo.setIsDirectory("目录");
                    } else {
                        fileInfo.setIsDirectory("文件");
                    }
                    fileInfo.setSize(file.length());
                    fileInfo.setPath(file.getAbsolutePath());
                    fileList.add(fileInfo);
                }
            }
            return Result.success(fileList);
        } else return Result.error("文件夹不存在或者不是文件夹");
    }

    @Override
    public Result delFiles(String path) {
        try {
            System.out.println("pqth:::::::" + path);
            Files.deleteIfExists(Path.of(path));
            return Result.success("删除成功");
        } catch (IOException e) {
            return Result.error(e.getMessage());
        }

    }

    //新建文件夹
    @Override
    public Result newFilePack(String path) {
        File directory = new File(path + "\\" + path + "\\");
        if (!directory.exists()) {
            directory.mkdirs();
            return Result.success("新建成功");
        } else return Result.error("目录已存在");
    }

    @Override
    public Result newFile(String name, int num, String path) throws IOException {
        File file = new File(path + "\\" + name);
        if (!file.exists()) {
            if (num == 0)
                file.createNewFile();
            else
                file.mkdirs();
            return Result.success("新建成功");
        } else return Result.error("文件已存在");
    }

    @Override
    public Result getFileContent(String path) {
        try {
            System.out.println("path:" + path);
            Path path1 = Paths.get(path);
            System.out.println("path1:" + path1);
            String content = new String(Files.readAllBytes(path1));
            System.out.println("content:" + content);
            return Result.success(content);
        } catch (IOException e) {
            return Result.error(e.getMessage());
        }

    }

    @Override
    public Result saveFile(String path, String content) {
        try {
            Files.write(Paths.get(path), content.getBytes());
            return Result.success();
        } catch (IOException e) {
            return Result.error(e.getMessage());
        }
    }


}
