package com.lhc.mybatis.test;


import com.lhc.mybatis.mapper.UserMapper;
import com.lhc.mybatis.pojo.User;
import com.lhc.mybatis.utils.SqlSessionUtil;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @Author: 刘华昌
 * @Date: 2022/7/29 21:16
 * @ClassName:
 * @Description:
 */
public class MyBatis {

    @Test
    public void testInsert() throws IOException {

        //1. 获取核心配置文件的输入流
        InputStream is = Resources .getResourceAsStream("mybatis-config.xml");
        //2. 获取SqlSessionFactoryBuilder对象
        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
        //3. 获取SqlSessionFactory对象
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(is);
        //4. 获取SqlSession对象，是MyBatis提供的操作数据库的对象
        SqlSession sqlSession = sqlSessionFactory.openSession(true); // true代表自动提交事务

        //5. 有两种方式 执行UserMapper.xml 中的sql语句

        // 方式一：(不推荐)
        //通过sql以及的唯一标识找到sql并执行，唯一标识是namespace.sqlId
        //int result = sqlSession.insert("com.atguigu.mybatis.mapper.UserMapper.insertUser");

        //方式二：（常用）
        //1). 获取UserMapper的代理实现类对象
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        //2). 调用mapper接口中的方法，实现数据库的添加功能
        int result = mapper.insertUser();

        //打印返回结果
        System.out.println("结果" + result);

        //提交事务 （如果在获取SqlSession对象时 没有设置默认提交 就需要手动提交）
        //sqlSession.commit();

        //关闭sqlSession
        sqlSession.close();

    }

    @Test
    public void testDelete() {

        //1. 获取SqlSession
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        //2. 获取UserMapper的代理实现类对象
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        //3. 调用mapper接口中的方法，实现数据库的修改功能
        mapper.deleteUser();
        //关闭SqlSession
        sqlSession.close();

    }

    @Test
    public void testUpdate() {

        //1. 获取SqlSession
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        //2. 获取UserMapper的代理实现类对象
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        //3. 调用mapper接口中的方法，实现数据库的修改功能
        mapper.updateUser();
        //关闭SqlSession
        sqlSession.close();

    }

    @Test
    public void testGetUser() {

        //1. 获取SqlSession
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        //2. 获取UserMapper的代理实现类对象
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        //3. 调用mapper接口中的方法，实现数据库的修改功能
        User user = mapper.getUserById();
        //关闭SqlSession
        sqlSession.close();
        //打印
        System.out.println(user);

    }

    @Test
    public void testGetUserList() {

        //1. 获取SqlSession
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        //2. 获取UserMapper的代理实现类对象
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        //3. 调用mapper接口中的方法，实现数据库的修改功能
        List<User> userList = mapper.getUserList();
        //关闭SqlSession
        sqlSession.close();

        //打印
        for (User user : userList) {
            System.out.println(user);
        }

    }
}
