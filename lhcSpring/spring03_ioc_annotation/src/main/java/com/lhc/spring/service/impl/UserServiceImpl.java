package com.lhc.spring.service.impl;

import com.lhc.spring.dao.UserDao;
import com.lhc.spring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * @Author: 刘华昌
 * @Date: 2022/8/6 14:51
 * @ClassName:
 * @Description:
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public void saveUser() {
        userDao.saveUser();
    }
}
