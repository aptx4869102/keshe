package org.keshe.keshe.mapper;

import org.apache.ibatis.annotations.*;
import org.keshe.keshe.pojo.User;

import java.util.List;

@Mapper
public interface UserMapper {
    //根据用户名查询
    @Select("select * from user where username=#{username}")
    User findByUserName(String username);

    //添加
    @Insert("insert into user(username,password,create_time,update_time)"+
    "values(#{username},#{password},now(),now())")
    void add(String username, String password);

    //更新
    @Update("update user set update_time=#{updateTime},qq=#{qq} where id=#{id}")
    void update(User user);

    //更新密码
    @Update("update user set password=#{newPassword},update_time=now() where id=#{id}")
    void updatePassword(String newPassword,Integer id);

    //返回所有user信息
    @Select("SELECT * FROM user")
    List<User> getall();

    //删除user
    @Delete("DELETE  FROM user where id=#{id}")
    void del(Integer id);

    //更新密码
    @Update("update user set password=#{password},update_time=now() where username=#{username}")
    void reset(String username, String password);
}
