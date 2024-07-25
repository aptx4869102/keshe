package org.keshe.keshe.thread;

import lombok.Data;
import net.mamoe.mirai.Bot;
import org.keshe.keshe.mapper.ServerMapper;
import org.keshe.keshe.pojo.Server;
import org.keshe.keshe.service.BotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Data
public class SeverConnectThread extends Thread {
    @Autowired
    private BotService botservice;
    private Server server;
    private ServerMapper serverMapper;
    InputStream ips;
    OutputStream ops;
    Bot bot;
    long qqgroup;
    boolean stop = false;
    boolean ifsend = false;
    @Override
    public void run() {
        InputStreamReader isr = null;
        try {
            isr = new InputStreamReader(ips,"gbk");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        BufferedReader br = new BufferedReader(isr);
        String str = null;
        while (!stop){
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            try {
//                if(ops != null){
//                    System.out.println("输入入:" + ops.toString());
//                    ops.flush();
//                }
                if ((str = br.readLine()) == null) break;
                else{
                    System.out.println("捕获:" + str);
                    String pattern = "<.+";
                    Pattern x = Pattern.compile(pattern);
                    Matcher m = x.matcher(str);
                    if(str.contains("Done (")){
                        server.setIfon(2);
                        serverMapper.updatestatus(server);
                        System.out.println("启动完毕!!!!!!!!!!!!!!!!");
                    }
                    if(str.contains("All chunks are saved")){
                        server.setIfon(0);
                        server.setIfconnect(0);
                        serverMapper.updateconnection(server);
                        serverMapper.updatestatus(server);
                        System.out.println("服务器已关闭");
                        stop=true;
                    }
                    if(m.find()){
                        System.out.println("要打印的消息为:" + m.group());
                        //发送
                        botservice.send(bot,qqgroup,m.group());
                    }
                    //否则判断是不是要返回群里输入指令的返回结果
                    else{
                        if(ifsend){
                            System.out.println("要在群里发消息");
                            String pattern1 = "]:.+";
                            Pattern x1 = Pattern.compile(pattern1);
                            Matcher m1 = x1.matcher(str);
                            if(m1.find()){
                                System.out.println(m1.group().substring(3));
                                botservice.send(bot,qqgroup,m1.group().substring(3));
                            }
                            ifsend=false;
                        }

                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }
}
