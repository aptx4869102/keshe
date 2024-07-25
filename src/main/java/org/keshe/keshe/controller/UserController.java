package org.keshe.keshe.controller;

import jakarta.validation.constraints.Pattern;
import org.keshe.keshe.pojo.Result;
import org.keshe.keshe.pojo.User;
import org.keshe.keshe.service.UserService;
import org.keshe.keshe.utills.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.keshe.keshe.utills.JwtUtil;

@RestController
@RequestMapping("/api/user")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Result register(@Pattern(regexp = "^\\S{5,16}$") String username, @Pattern(regexp = "^\\S{5,16}$") String password) {
        //查询用户
        User u = userService.findByUserName(username);
        if (u == null) {
            //没有占用
            userService.register(username, password);
            return Result.success();
        } else {
//占用了
            return Result.error("用户名已被占用");
        }
    }

    @PostMapping("/login")
    public Result login(@Pattern(regexp = "^\\S{5,16}$") String username, @Pattern(regexp = "^\\S{5,16}$") String password) {
        //查询用户
        User u = userService.findByUserName(username);
        if (u == null) {
            return Result.error("用户名错误");

        }
        //密码是否正确
        else{
            if (u.getPassword().equals(password)) {
                //登录成功
                Map<String,Object> claims = new HashMap<String,Object>();
                claims.put("id",u.getId());
                claims.put("username",u.getUsername());
                String token = JwtUtil.genToken(claims);
                return Result.success(token);
            }
            else{
                return Result.error("密码错误");
            }

        }
    }

    @GetMapping("/info")
        public Result<User> userInfo(/*@RequestHeader(name = "Authorization") String token */){
        //根据用户名查询用户
//        Map<String,Object> map = JwtUtil.parseToken(token);
//        String username = (String) map.get("username");
        Map<String,Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");
        User user = userService.findByUserName(username);
        return Result.success(user);
    }

    @PutMapping("/update")
    public Result update(@RequestBody @Validated User user){
        userService.update(user);
        return Result.success();
    }

    @PatchMapping("/updatepsw")
    public Result updatePassword(@RequestBody Map<String,String> params){
        //1.校验参数
        String oldPassword = params.get("oldPassword");
        String newPassword = params.get("newPassword");
        String confirmPassword = params.get("confirmPassword");
        if(!StringUtils.hasLength(oldPassword) || !StringUtils.hasLength(newPassword) || !StringUtils.hasLength(confirmPassword)){
            return Result.error("缺水必要的参数");
        }
        //原密码是否正确
        //调用userservice获得密码
        Map<String,Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");
        User user = userService.findByUserName(username);
        if(!user.getPassword().equals(oldPassword)){
            return Result.error("原密码不正确");
        }
        if(!newPassword.equals(confirmPassword)){
           return Result.error("两次的密码不一样");
        }

        //2.调用service完成密码更新

        userService.updatePassword(newPassword);
        return Result.success();
    }

    @PostMapping("/reset")
    public Result reset(String username,String password){
        return userService.reset(username,password);
    }

    @GetMapping("/getall")
    public Result getallusers(){
        return Result.success(userService.getall());
    }

    @PostMapping("/del")
    public Result deluser(@RequestBody List<Integer> list){
        return Result.success(userService.del(list).getData());
    }







}



















