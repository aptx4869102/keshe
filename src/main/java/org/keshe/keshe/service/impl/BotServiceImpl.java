package org.keshe.keshe.service.impl;


import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.auth.BotAuthorization;
import net.mamoe.mirai.contact.Contact;

import net.mamoe.mirai.utils.BotConfiguration;

import org.keshe.keshe.pojo.Result;
import org.keshe.keshe.service.BotService;
import org.keshe.keshe.service.GetqrService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;



@Service
public class BotServiceImpl implements BotService {
    @Autowired
    private GetqrService getqrservice;

    @Override
    public Bot getBot(long qq) {
        return Bot.getInstance(qq);
    }

    @Override
    public Bot getAllBot() {
        Bot firstBot = Bot.getInstances().stream().findFirst().orElse(null);
        if (firstBot != null) {
            if(firstBot.isOnline()) {
                System.out.println("First Bot QQ: " + firstBot.getId());
                System.out.println("First Bot Nickname: " + firstBot.getNick());
                return firstBot;
            }
            else return null;

        } else {
            System.out.println("No Bot instances found");
            return null;
        }
    }

    @Override
    public void close() {
        Bot bot = getAllBot();
        if (bot != null) {
            bot.close();
        }
    }


    @Override
    public Result start(long qq) {
        try {
            Bot bot = BotFactory.INSTANCE.newBot(qq, BotAuthorization.byQRCode(), configuration -> {
                configuration.setProtocol(BotConfiguration.MiraiProtocol.ANDROID_WATCH);
                configuration.fileBasedDeviceInfo("DeviceInfo"+ qq +".json");
            });
            //System.out.println("bot配置为:"+bot.getConfiguration());
            Thread thread = new Thread(bot::login);
            thread.start();
            Result result = getqrservice.getqr();

            return result;
        } catch (Exception e) {
            //若登录失败返回失败信息
            return Result.error(e.getMessage());
        }
    }

    @Override
    public Result send(Bot bot, long qqgroup, String message) {
        Contact a = bot.getGroup(qqgroup);//获取对象
        a.sendMessage(message);
        return Result.success("已发送");
    }
}
