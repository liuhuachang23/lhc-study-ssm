package com.lhc.spring.test;

import com.lhc.spring.pojo.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @Author: 刘华昌
 * @Date: 2022/8/7 16:47
 * @ClassName:
 * @Description:
 */
//指定当前测试类在Spring的测试环境中执行，此时就可以通过注入的方式获取IOC容器中的bean
@RunWith(SpringJUnit4ClassRunner.class)
//设置Spring测试环境的配置文件
@ContextConfiguration("classpath:spring-jdbc.xml")
public class JdbcTemplateTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test //测试增删改功能
    public void testUpdate() {
        String sql = "insert into t_user values(null,?,?,?,?,?)";
        int result = jdbcTemplate.update(sql, "张三", "ok", 11, "男", "123@qq.com");
        System.out.println(result);
    }

    @Test
    //查询一条数据为一个实体类对象
    public void testSelectEmpById() {
        String sql = "select * from t_user where id = ?";
        User user = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), 1);
        System.out.println(user);
    }

    @Test
    //查询多条数据为一个list集合
    public void testSelectList() {
        String sql = "select * from t_user";
        List<User> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class));

        //list.forEach(System.out::println);
        list.forEach(user -> System.out.println(user));
    }

    @Test
    //查询单行单列的值
    public void selectCount() {
        String sql = "select count(id) from t_user";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        System.out.println(count);
    }


}
