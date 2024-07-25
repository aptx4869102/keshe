package org.keshe.keshe.service;

import org.keshe.keshe.pojo.Result;
import org.keshe.keshe.pojo.User;
import java.util.List;

public interface UserService {
    //根据用户名查询数据
    User findByUserName(String username);
    //注册
    void register(String username, String password);
    //更新
    void update(User user);

    void updatePassword(String newPassword);

    List<User> getall();

    //删除
    Result del(List<Integer> list);

    //更新密码
    Result reset(String username, String password);
}
