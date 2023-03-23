package com.lhc.mybatis.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * @Author: 刘华昌
 * @Date: 2022/7/30 9:42
 * @ClassName:
 * @Description: 获取SqlSession的工具类
 */
public class SqlSessionUtil {

    public static SqlSession getSqlSession() {

        SqlSession sqlSession = null;

        try {

            //1. 获取核心配置文件的输入流
            InputStream is = Resources.getResourceAsStream("mybatis-config.xml");
            //2. 获取SqlSessionFactoryBuilder对象
            SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
            //3. 获取SqlSessionFactory对象
            SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(is);
            //4. 获取SqlSession对象，是MyBatis提供的操作数据库的对象
            sqlSession = sqlSessionFactory.openSession(true); // true代表自动提交事务

        } catch (IOException e) {
            e.printStackTrace();
        }

        return sqlSession;

    }


}
