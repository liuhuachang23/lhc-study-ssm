package com.lhc.spring.service.impl;

import com.lhc.spring.service.UserService;
import com.lhc.spring.dao.UserDAO;

/**
 * @Author: 刘华昌
 * @Date: 2022/8/5 15:20
 * @ClassName:
 * @Description:
 */
public class UserServiceImpl implements UserService {

    private UserDAO userDao;

    public void setUserDao(UserDAO userDao) {
        this.userDao = userDao;
    }

    @Override
    public void saveUser() {
        userDao.saveUser();
    }
}
