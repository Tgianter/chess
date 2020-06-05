package com.example.demo.dao;

import com.example.demo.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface UserMapper {
    @Options(useGeneratedKeys=true,keyColumn = "id",keyProperty = "id")
    @Insert("insert into t_user values(#{userOpenid},#{userName},#{userHead}) ")
    Boolean insertUser(User user);
    @Select("select * from t_user where userOpenid=#{openid}")
    User selectUserByOpenId(String openid);
}
