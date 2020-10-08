package com.epoch.test.dao;

import com.epoch.test.bean.UserOut;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.web.bind.annotation.RequestParam;

@Mapper
public interface UserDao {

    @Select("select count(0) from user where username = #{username} and password = #{password}")
    int findByUsernameAndPassword(@RequestParam String username, @RequestParam String password) throws Exception;

    @Select("select * from user where username = #{username}")
    UserOut findJbxx(@RequestParam String username) throws Exception;

    @Select("select password from user where username = #{username}")
    String findPassword(@RequestParam String username)throws Exception;

    @Update("update user set password = #{newPassword} where username = #{username}")
    void updatePassword(@RequestParam String username, @RequestParam String newPassword);
}
