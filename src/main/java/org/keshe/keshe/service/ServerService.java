package org.keshe.keshe.service;

import org.keshe.keshe.pojo.Result;
import org.keshe.keshe.pojo.Server;

import java.io.IOException;
import java.util.List;

public interface ServerService {
    //查找cmd
    Server findByServerName(String name);
    //更新指令
    void update(Server server);
    //添加
    void register(String name, String cmd,String nickname);
    //启动指令
    void startServer(Server server);
    //输入指令
    void command(Server server, String cmd) throws IOException, InterruptedException;
    //停止
    void stopServer(Server server) throws IOException, InterruptedException;
    //连接qq
    Result connect(long qq,long qqgroup,Server server) throws Exception;
    //获得所有服务器实例信息
    List<Server> getAll();
    //将服务器状态设置为启动完毕
    void done(Server server);
    //断开机器人连接
    void disconnect(Server server);
}
