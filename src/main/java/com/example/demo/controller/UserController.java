package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.constant.CommonResult;
import com.example.demo.constant.UserConstant;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import com.example.demo.util.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;


    @PostMapping("/onLogin")
    public CommonResult user_login(
            @RequestParam("code") String code,
            @RequestParam("userHead") String userHead,
            @RequestParam("userName") String userName
//            @RequestParam("userGender") String userGender,
//            @RequestParam("userCity") String userCity,
//            @RequestParam("userProvince") String userProvince
    ){
        log.info("收到的前端发来的："+code);
        // 配置请求参数
        Map<String, String> param = new HashMap<>();
        param.put("appid", UserConstant.WX_LOGIN_APPID);
        param.put("secret", UserConstant.WX_LOGIN_SECRET);
        param.put("js_code", code);
        param.put("grant_type", UserConstant.WX_LOGIN_GRANT_TYPE);
        // 向微信服务器发送请求
        String wxResult = HttpClientUtil.doGet(UserConstant.WX_LOGIN_URL, param);
//        log.info("获得的原生的数据"+wxResult);

        //解析获得的数据
        JSONObject jsonObject = JSONObject.parseObject(wxResult);
//        log.info("解析获得的openId和session_key："+String.valueOf(jsonObject));

        // 获取参数返回的
        String session_key = jsonObject.get("session_key").toString();
        String open_id = jsonObject.get("openid").toString();
        //查询数据库中是否存在该用户
        User user = userService.selectUserByOpenid(open_id);
        if(user != null){
            System.out.println("用户已存在");
        }else{
            User insert_user = new User();
            insert_user.setUserHead(userHead);
            insert_user.setUserName(userName);
            insert_user.setUserOpenid(open_id);
//            insert_user.setUserGender(userGender);
//            insert_user.setUserNewLogin((java.sql.Date) new Date());
//            insert_user.setUserCity(userCity);
//            insert_user.setUserProvince(userProvince);

            System.out.println("insert_user:"+insert_user.toString());
            // 添加到数据库
            Boolean flag = userService.insert(insert_user);
            if(!flag){
                return CommonResult.failed("数据插入数据库出现问题");
            }
        }
        // 封装返回小程序
        Map<String, String> result = new HashMap<>();
//        result.put("session_key", session_key);
        result.put("open_id", open_id);
        return CommonResult.success(result,"成功获取openid");

    }

}
