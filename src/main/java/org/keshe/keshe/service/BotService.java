package org.keshe.keshe.service;

import net.mamoe.mirai.Bot;
import org.keshe.keshe.pojo.Result;



public interface BotService {
    //登录机器人
    Result start(long qq);
    //向指定群聊发送消息
    Result send(Bot bot,long qqgroup,String message);
    //获得bot
    Bot getBot(long qq);
    //获得所有bot
    Bot getAllBot();
    //登出bot
    void close();
}
