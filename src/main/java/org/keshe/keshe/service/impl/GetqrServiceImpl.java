package org.keshe.keshe.service.impl;

import net.mamoe.mirai.Bot;
import org.keshe.keshe.pojo.Result;
import org.keshe.keshe.service.GetqrService;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class GetqrServiceImpl implements GetqrService {
    public Result start() {
        return new Result();
    }

    public Result getqr() throws InterruptedException {
        PrintStream original = System.out;
        PrintStream err = System.err;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        System.setOut(ps);
        //从这里开始控制台不会输出
        //等待5秒捕获bot.login()输出的信息
        TimeUnit.SECONDS.sleep(4);
        System.setOut(original);
        System.out.println(baos + "傻逼100");
        ps.close();
        if (baos.toString().contains("等待扫描二维码中")) {
            System.out.println("成功捕获到图片地址" + "/n" + baos + "啦啦啦啦啦啦");
            //下面是从字符串里筛选图片地址的简单正则算法
            String pattern = "file:.+png";
            Pattern x = Pattern.compile(pattern);
            Matcher m = x.matcher(baos.toString());
            if (m.find()) {
                //把图片存到静态资源下
                File directory = new File("");//设定为当前文件夹
                String str = directory.getAbsolutePath();//当前路径
                Path sourcePath = Path.of(m.group(0).substring(8));
                Path destinationPath = Path.of(str+"\\src\\main\\resources\\static\\1.png");
                try {
                    // 复制文件，覆盖目标文件（如果存在）
                    Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("File copied successfully!");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return Result.success(m.group(0));
            }

        } else {
            System.out.println(baos + "傻逼");
        }
        return Result.success("之前已登录");
    }

    @Override
    public Result listen() throws InterruptedException {
        PrintStream original = System.out;
        PrintStream err = System.err;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        System.setOut(ps);
        while(true){
            TimeUnit.SECONDS.sleep(1);
            if(baos.toString().contains("Bot login successful")){
                System.setOut(original);
                System.out.println(baos + "成功登录了了了");
                return Result.success("登录成功");
            }
            else if(baos.toString().contains("扫描超时")||baos.toString().contains("已取消登录")){
                Bot firstBot = Bot.getInstances().stream().findFirst().orElse(null);
                if(firstBot != null){
                    firstBot.close();
                    System.setOut(original);
                    System.out.println(baos + "超时了了了了");
                    return Result.success("超时或者已取消登录");
                }
                else return  Result.success("???");
            }
        }
    }
//"[QRCodeLogin]"

}
