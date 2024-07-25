package org.keshe.keshe.controller;

import org.keshe.keshe.pojo.Result;
import org.keshe.keshe.pojo.Server;
import org.keshe.keshe.service.BotService;
import org.keshe.keshe.service.ServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;

@RestController
@RequestMapping("/api/server")
public class ServerController {
    @Autowired
    private ServerService serverservice;
    @Autowired
    private BotService botservice;
    Process p = null;

    @PutMapping("/start")
    public Result<String> start(String name) throws InterruptedException, IOException {
        //获得服务器
        Server s = serverservice.findByServerName(name);
        //开启
        s.setIfon(1);
        serverservice.startServer(s);

        return Result.success("已开启");
    }

    @PutMapping
    public Result update(Server server) {
        serverservice.update(server);
        return Result.success("更新启动命令成功");
    }

    @PutMapping("/connect")
    public Result connect(String qq,String qqgroup,String name) throws Exception {
        //获得服务器
        //调用函数让服务器连到机器人
        long qq1 = Long.parseLong(qq);
        long qq2 = Long.parseLong(qqgroup);
        Server server = serverservice.findByServerName(name);
        return serverservice.connect(qq1,qq2,server);
    }

    @PutMapping("/command")
    public Result command(String cmd,String name) throws IOException, InterruptedException {
        Server s = serverservice.findByServerName(name);
        serverservice.command(s,cmd);
        return Result.success();
    }

    @GetMapping("/getall")
    public Result getAll() {
        return Result.success(serverservice.getAll());
    }

    @PutMapping("/stop")
    public Result stop(String name) {
        Server s = serverservice.findByServerName(name);
        try {
            serverservice.stopServer(s);
        } catch (IOException e) {
            return Result.error(e.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return Result.success();
    }

    @PutMapping("/disconnect")
    public Result disconnect(String name) {
        Server s = serverservice.findByServerName(name);
        System.out.println("qqqqqqqqqqqqqqqq:"+s);
        serverservice.disconnect(s);
        return Result.success();
    }
    @PutMapping("/get")
    public Result get(String name) {
        Server s = serverservice.findByServerName(name);
        return Result.success(s);
    }

}
