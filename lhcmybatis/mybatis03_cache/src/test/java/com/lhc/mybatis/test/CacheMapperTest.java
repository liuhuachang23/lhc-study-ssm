package com.lhc.mybatis.test;

import com.lhc.mybatis.mapper.CacheMapper;
import com.lhc.mybatis.pojo.Emp;
import com.lhc.mybatis.utils.SqlSessionUtil;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 * @Author: 刘华昌
 * @Date: 2022/8/1 16:11
 * @ClassName:
 * @Description:
 */
public class CacheMapperTest {

    //测试一级缓存
    @Test
    public void testCache01() {

        //1. 获取SqlSession对象
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        //2. 获取CacheMapper的代理实现类对象，对数据库表进行查询
        CacheMapper mapper1 = sqlSession.getMapper(CacheMapper.class);
        Emp emp1 = mapper1.getEmpById(1);
        System.out.println(emp1);

        //3. 二次使用SqlSession对象，去做相同的操作
        CacheMapper mapper2 = sqlSession.getMapper(CacheMapper.class);
        Emp emp2 = mapper2.getEmpById(1);
        System.out.println(emp2);
        sqlSession.close();

    }

    //测试二级缓存 和 第三方缓存
    @Test
    public void testCache02() throws IOException {

        InputStream is = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
        //1. 获取SqlSessionFactory对象
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(is);

        //2. 获取SqlSession对象
        SqlSession sqlSession1 = sqlSessionFactory.openSession(true); // true代表自动提交事务
        CacheMapper mapper1 = sqlSession1.getMapper(CacheMapper.class);
        Emp emp1 = mapper1.getEmpById(1);
        System.out.println(emp1);
        // 关闭sqlSession1 (将一级缓存中的数据、提交到二级缓存)
        sqlSession1.close();

        //3. 二次创建SqlSession对象，对数据库进行相同的查询操作
        SqlSession sqlSession2 = sqlSessionFactory.openSession();
        CacheMapper mapper2 = sqlSession2.getMapper(CacheMapper.class);
        Emp emp2 = mapper2.getEmpById(1);
        System.out.println(emp2);
        sqlSession2.close();

    }

}
