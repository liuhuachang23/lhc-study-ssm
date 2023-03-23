package com.lhc.mybatis.test;

import com.lhc.mybatis.mapper.ParameterMapper;
import com.lhc.mybatis.pojo.User;
import com.lhc.mybatis.utils.SqlSessionUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 刘华昌
 * @Date: 2022/7/31 14:19
 * @ClassName:
 * @Description:
 */
public class ParameterTest {

    //1. mapper接口的参数为 单个字面量类型的参数
    @Test
    public void getUserByUsername() {

        //1. 获取SqlSession
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        //2. 获取UserMapper的代理实现类对象
        ParameterMapper mapper = sqlSession.getMapper(ParameterMapper.class);
        //3. 调用mapper接口中的方法
        User user = mapper.getUserByUsername("link");
        System.out.println(user);
        //关闭SqlSession
        sqlSession.close();
    }

    //2. mapper接口的参数为 多个字面量类型的参数
    @Test
    public void checkLogin() {

        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        ParameterMapper mapper = sqlSession.getMapper(ParameterMapper.class);

        User user = mapper.checkLogin("link","ok");
        System.out.println(user);

        sqlSession.close();
    }

    //3. mapper接口的参数为 map类型的参数
    @Test
    public void checkLoginByMap() {
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        ParameterMapper mapper = sqlSession.getMapper(ParameterMapper.class);

        //创建一个map
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("username","link"); // ( 表字段为key，字段值为value )
        map.put("password","ok");
        //调用mapper接口中的方法
        User user = mapper.checkLoginByMap(map);
        System.out.println(user);

        sqlSession.close();

    }

    //4. mapper接口的参数为 实体类类型的参数
    @Test
    public void insertUser() {
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        ParameterMapper mapper = sqlSession.getMapper(ParameterMapper.class);

        //创建一个User
        User user = new User(null,"root","123456",23,"女", "1234@qq.com");
        //调用mapper接口中的方法
        mapper.insertUser(user);

        sqlSession.close();

    }

    //5. mapper接口的参数 使用@Param标识
    @Test
    public void checkLoginByParam() {
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        ParameterMapper mapper = sqlSession.getMapper(ParameterMapper.class);

        //调用mapper接口中的方法
        User user = mapper.checkLoginByParam("link", "ok");
        System.out.println(user);

        sqlSession.close();
    }


//========================================================================

    @Test
    public void getUserToMap() {

        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        ParameterMapper mapper = sqlSession.getMapper(ParameterMapper.class);

        //3. 调用mapper接口中的方法
        Map<String, Object> userToMap = mapper.getUserToMap(1);
        System.out.println(userToMap);

        sqlSession.close();
    }

    @Test
    public void getAllUserToMap() {

        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        ParameterMapper mapper = sqlSession.getMapper(ParameterMapper.class);

        //3. 调用mapper接口中的方法
        List<Map<String, Object>> allUserToMap = mapper.getAllUserToMap();
        System.out.println(allUserToMap);

        sqlSession.close();
    }

    @Test
    public void testGetAllUserToMap() {

        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        ParameterMapper mapper = sqlSession.getMapper(ParameterMapper.class);

        //3. 调用mapper接口中的方法
        Map<String, Object> userToMap = mapper.testGetAllUserToMap();
        System.out.println(userToMap);

        sqlSession.close();
    }

}
