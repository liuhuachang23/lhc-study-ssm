package com.lhc.spring.dao.impl;

import com.lhc.spring.dao.UserDAO;

/**
 * @Author: 刘华昌
 * @Date: 2022/8/5 15:22
 * @ClassName:
 * @Description:
 */
public class UserDaoImpl implements UserDAO {
    @Override
    public void saveUser() {
        System.out.println("保存成功！");
    }
}
