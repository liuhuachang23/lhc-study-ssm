package com.lhc.spring.factory;

import com.lhc.spring.pojo.User;
import org.springframework.beans.factory.FactoryBean;

/**
 * @Author: 刘华昌
 * @Date: 2022/8/5 14:40
 * @ClassName:
 * @Description:
 */
public class UserFactoryBean implements FactoryBean<User> {

    @Override
    public User getObject() throws Exception {
        return new User();
    }

    @Override
    public Class<?> getObjectType() {
        return User.class;
    }

}
