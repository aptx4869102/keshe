package org.keshe.keshe.controller;


import net.mamoe.mirai.Bot;
import org.keshe.keshe.service.GetqrService;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import org.keshe.keshe.pojo.Result;
import org.keshe.keshe.service.BotService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;



import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

@RestController
@RequestMapping("/api/bot")
public class BotController {
    @Autowired
    private BotService botService;
    @Autowired
    private GetqrService getqrService;

    @PostMapping("/login")
    public Result start(String qq) {
        Long qqLong = Long.parseLong(qq);
        return botService.start(qqLong);
    }

    @GetMapping("/get")
    public Result getbotinfo() {
        Bot bot = botService.getAllBot();
        if (bot != null) {
            return Result.success(bot.getId());
        } else return Result.success(null);
    }

    @PostMapping("/close")
    public Result<String> close() {
        botService.close();
        return Result.success("已登出");
    }

    //前端可以直接根据URL：/image 来获取图片   注意:资源文件ID最好进行加密和设置有效期
    @GetMapping(value = "/image")
    public Result getImageBase64() throws IOException {
        File directory = new File("");//设定为当前文件夹
        String str = directory.getAbsolutePath();//当前路径
        String destinationPath = (str+"\\src\\main\\resources\\static\\1.png");
        Resource resource = new FileSystemResource(destinationPath);   ;
        InputStream inputStream = resource.getInputStream();
        byte[] imageBytes = inputStream.readAllBytes();
        inputStream.close();

        String base64Image = Base64.getEncoder().encodeToString(imageBytes);
        return Result.success(base64Image);
    }

    @PutMapping("/listen")
    public Result listen() {
        try {
            return getqrService.listen();
        } catch (InterruptedException e) {
            return Result.success(e.getMessage());
        }
    }
}
