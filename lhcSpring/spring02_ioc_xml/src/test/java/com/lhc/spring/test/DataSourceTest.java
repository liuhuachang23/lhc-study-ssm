package com.lhc.spring.test;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @Author: 刘华昌
 * @Date: 2022/8/4 16:56
 * @ClassName:
 * @Description:
 */
public class DataSourceTest {

    @Test
    public void testDataSource() throws SQLException {
        ApplicationContext ioc = new ClassPathXmlApplicationContext("spring-datasource.xml");
        DataSource bean = ioc.getBean(DataSource.class);
        System.out.println(bean.getConnection());
    }

}
