package org.keshe.keshe.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.keshe.keshe.pojo.Server;

import java.util.List;

@Mapper
public interface ServerMapper {
    @Select("select * from server where name=#{name}")
    Server findByServerName(String name);

    //更新命令
    @Update("update server set cmd=#{cmd}")
    void update(Server server);
    //添加
    @Insert("insert into server(name,cmd,nickname)"+
            "values(#{name},#{cmd},#{nickname})")
    void add(String name, String cmd,String nickname);
    //获得所有实例
    @Select("SELECT * FROM server")
    List<Server> getAll();
    //更新状态
    @Update("update server set ifon=#{ifon} where name=#{name} ")
    void updatestatus(Server server);
    //更新连接状态
    @Update("update server set ifconnect=#{ifconnect} where name=#{name} ")
    void updateconnection(Server server);
}
