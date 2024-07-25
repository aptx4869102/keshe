package org.keshe.keshe.service.impl;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.event.Listener;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import org.keshe.keshe.mapper.ServerMapper;
import org.keshe.keshe.pojo.Result;
import org.keshe.keshe.pojo.Server;
import org.keshe.keshe.pojo.ServerandListenerandProcess;
import org.keshe.keshe.service.BotService;
import org.keshe.keshe.service.ServerService;
import org.keshe.keshe.thread.ServerThread;
import org.keshe.keshe.thread.SeverConnectThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class ServerImpl implements ServerService {
    private final ArrayList<ServerandListenerandProcess> list = new ArrayList<>();
    private Process p = null;
    //@Autowired
    //private ServerThread serverthread;
    @Autowired
    private BotService botservice;
    @Autowired
    private ServerMapper servermapper;
    //@Autowired
    //private SeverConnectThread severconnectthread;

    @Override
    public Server findByServerName(String name) {
        return servermapper.findByServerName(name);
    }

    @Override
    public void update(Server server) {
        servermapper.update(server);
    }

    @Override
    public void register(String name, String cmd, String nickname) {
        servermapper.add(name, cmd, nickname);
    }

    @Override
    public void startServer(Server server) {
        ServerThread st = new ServerThread();
        st.setServerMapper(servermapper);
        servermapper.updatestatus(server);
        st.setStop(false);
        Runtime runtime = Runtime.getRuntime();
        String cmd = server.getCmd();
        String a = System.getenv("path");
        String[] envp = {};
        File directory = new File("");//设定为当前文件夹
        String str = directory.getAbsolutePath();//当前路径
        File dir = new File(str + "\\severs\\" + server.getName());
        try {
            //运行服务端进程
            p = (runtime.exec(cmd, null, dir));
            ServerandListenerandProcess salp = new ServerandListenerandProcess();
            salp.setServer(server);
            salp.setProcess(p);
            list.add(salp);
            System.out.println("pidddd:" + p.pid());
            //自动开始监测
            st.setIps(p.getInputStream());
            System.out.println("2222222222222222222");
            st.setServer(server);
            System.out.println("33333333333333333");
            st.start();
            //       server.setReader(new BufferedReader(new InputStreamReader(server.getP().getInputStream(), "gbk")));
//            String x = null;
//            while ((x = server.getReader().readLine())!=null) System.out.println(x);
        } catch (Exception e) {
            System.out.println("有问题::::::" + e);
        }

    }

    @Override
    public Result connect(long qq, long qqgroup, Server server) throws Exception {
        Bot bot = null;
        //先看机器人启动没启动
        if ((bot = botservice.getBot(qq)) != null) {
            //关闭先前的线程
            server.setIfconnect(1);
            servermapper.updateconnection(server);
            // serverthread.setStop(true);
            //reader = new BufferedReader(new InputStreamReader(p.getInputStream(), "gbk"));
            SeverConnectThread severconnectthread = new SeverConnectThread();
            severconnectthread.setBot(bot);
            severconnectthread.setServerMapper(servermapper);
            severconnectthread.setBotservice(botservice);
            severconnectthread.setIps(p.getInputStream());
            severconnectthread.setOps(p.getOutputStream());
            severconnectthread.setQqgroup(qqgroup);
            severconnectthread.setServer(server);
            severconnectthread.start();
            //开启监听
            Listener listener = bot.getEventChannel().subscribeAlways(GroupMessageEvent.class, (event) -> {
                //逻辑
                if (event.getGroup().getId() == qqgroup) {
                    String str = event.getMessage().contentToString();
                    String sender = event.getSenderName();
                    if (str.startsWith("#")) {
                        try {
                            //执行命令
                            System.out.println("在服务器内执行命令");
                            severconnectthread.setIfsend(true);
                            command(server,str.substring(1));
                        } catch (IOException | InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        //在服务器播放消息
                        try {
                            System.out.println("在服务器广播群内消息");
                            command(server,"say " + "[" + "§a" + sender + "§r]:" + str);
                        } catch (IOException | InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    System.out.println("8888888888888888888888888888888" + str);

                }
            });
            for(int i=0;i<list.size();i++) {
                ServerandListenerandProcess x = list.get(i);
                String s = x.getServer().getName();
                if (s.equals(server.getName())) {
                    //添加监听事件
                    x.setListener(listener);
                    System.out.println("添加了了了了");
                    break;
                }
            }
            server.setIfconnect(2);
            servermapper.updateconnection(server);
            return Result.success();
        }
        return Result.error("该账号还未登录!");
    }

    //442142407L
    @Override
    public void command(Server server, String cmd) throws IOException, InterruptedException {
        for(int i=0;i<list.size();i++) {
            ServerandListenerandProcess x = list.get(i);
            if (x.getServer().getName().equals(server.getName())) {
                System.out.println(server.getName());
                Process ps = x.getProcess();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(ps.getOutputStream()));
                writer.write(cmd + "\r\n");
                writer.flush();
            }
        }
        //System.out.println("1212121:" + cmd);

//        if(cmd.equals("stop")){
//            TimeUnit.SECONDS.sleep(3);
//            p.destroy();
//        }
    }

    @Override
    public void stopServer(Server server) throws IOException, InterruptedException {
        server.setIfon(3);//正在停止运行
        disconnect(server);//断开跟bot的连接
        servermapper.updatestatus(server);
        command(server,"stop");
    }


    @Override
    public List<Server> getAll() {
        return servermapper.getAll();
    }


    @Override
    public void done(Server server) {
        server.setIfon(2);//开启完毕
        servermapper.updatestatus(server);
    }

    @Override
    public void disconnect(Server server) {
        for(int i=0;i<list.size();i++) {
            ServerandListenerandProcess x = list.get(i);
            String s = x.getServer().getName();
            if (s.equals(server.getName())) {
                //取消监听事件
                if(x.getListener() != null) {
                    //如果不为null
                    x.getListener().complete();
                    x.setListener(null);
                    list.set(i,x);
                    System.out.println("停止监听!");
                }

            }
        }
        Server s = servermapper.findByServerName(server.getName());
        s.setIfconnect(0);
        System.out.println("22223333333333333333:" + s);
        servermapper.updateconnection(s);
    }


}
