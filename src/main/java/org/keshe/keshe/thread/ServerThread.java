package org.keshe.keshe.thread;

import lombok.Data;
import org.keshe.keshe.mapper.ServerMapper;
import org.keshe.keshe.service.BotService;
import org.keshe.keshe.service.ServerService;
import org.keshe.keshe.pojo.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.concurrent.TimeUnit;

@Data
@Service
public class ServerThread extends Thread {
    @Autowired
    private ServerMapper serverMapper;
    private Server server;
    private BotService bs;
    InputStream ips;
    OutputStream os;
    boolean stop = false;
    @Override
    public void run() {
        InputStreamReader isr = new InputStreamReader(ips);
        BufferedReader br = new BufferedReader(isr);
        String line = null;
        System.out.println(server);
        while(!stop){
            if(serverMapper.findByServerName(server.getName()).getIfconnect()!=0){
                stop = true;
                break;
            }
            try {
                if ((line=br.readLine()) == null) break;
                else {
                    System.out.println("thread:" + line);
                    if(line.contains("Done (")){
                        server.setIfon(2);
                        serverMapper.updatestatus(server);
                        System.out.println("启动完毕!!!!!!!!!!!!!!!!");
                    }
                    if(line.contains("All chunks are saved")){
                        server.setIfon(0);
                        server.setIfconnect(0);
                        serverMapper.updatestatus(server);
                        serverMapper.updateconnection(server);
                        System.out.println("服务器已关闭");
                        stop=true;
                    }

                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
