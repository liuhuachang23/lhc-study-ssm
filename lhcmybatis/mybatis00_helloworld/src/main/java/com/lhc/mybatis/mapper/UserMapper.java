package com.lhc.mybatis.mapper;

import com.lhc.mybatis.pojo.User;

import java.util.List;

/**
 * @Author: 刘华昌
 * @Date: 2022/7/29 21:10
 * @ClassName: Mapper接口
 * @Description:
 */
public interface UserMapper {

    //添加用户信息
    int insertUser();

    //修改用户信息
    void updateUser();

    //删除用户信息
    void deleteUser();

    //查询当个用户信息
    User getUserById();

    //查询多个用户信息
    List<User> getUserList();
}
