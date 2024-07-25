package org.keshe.keshe.service.impl;

import org.keshe.keshe.mapper.UserMapper;
import org.keshe.keshe.pojo.Result;
import org.keshe.keshe.pojo.User;
import org.keshe.keshe.service.UserService;
import org.keshe.keshe.utills.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public User findByUserName(String username) {
        User u = userMapper.findByUserName(username);
        return u;
    }
    @Override
    public void register(String username, String password) {
        userMapper.add(username,password);//不加密直接添加
    }

    @Override
    public void update(User user) {
        user.setUpdateTime(LocalDateTime.now());
        userMapper.update(user);
    }

    @Override
    public void updatePassword(String newPassword) {
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        userMapper.updatePassword(newPassword,id);
    }

    @Override
    public List <User> getall() {
        return userMapper.getall();
    }

    @Override
    public Result del(@RequestBody List<Integer> list) {
        for(int i=0;i<list.size();i++){
            userMapper.del(list.get(i));
        }
        return Result.success("删除成功!");
    }

    @Override
    public Result reset(String username, String password) {
        userMapper.reset(username,password);
        return Result.success("修改成功");
    }
}












