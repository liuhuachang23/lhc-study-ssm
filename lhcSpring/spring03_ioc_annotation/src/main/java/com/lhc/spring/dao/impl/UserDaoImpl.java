package com.lhc.spring.dao.impl;

import com.lhc.spring.dao.UserDao;
import org.springframework.stereotype.Repository;

/**
 * @Author: 刘华昌
 * @Date: 2022/8/6 14:52
 * @ClassName:
 * @Description:
 */
@Repository("userDaoImpl")
public class UserDaoImpl implements UserDao {

    @Override
    public void saveUser() {
        System.out.println("保存成功！");
    }
}
