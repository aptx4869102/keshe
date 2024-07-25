package org.keshe.keshe;

import lombok.Value;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.auth.BotAuthorization;
import net.mamoe.mirai.utils.BotConfiguration;
import org.keshe.keshe.pojo.Result;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

//@EnableSimbot
@SpringBootApplication

public class KesheApplication {
    public static void main(String[] args) throws IOException {
        SpringApplication.run(KesheApplication.class, args);
        File directory = new File("");//设定为当前文件夹
        String str = directory.getAbsolutePath();
        System.out.println(str);
        File file = new File(str + "\\severs");
        if (!file.exists()) {
            file.mkdirs();
        }
        //登录机器人
//        Bot bot = BotFactory.INSTANCE.newBot(3868890863L, BotAuthorization.byQRCode(), configuration -> {
//            configuration.setProtocol(BotConfiguration.MiraiProtocol.ANDROID_WATCH);});
//        bot.login();

    }


}

