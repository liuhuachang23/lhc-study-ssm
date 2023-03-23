package com.lhc.mybatis.test;

import com.atguigu.mybatis.mapper.EmpMapper;
import com.atguigu.mybatis.pojo.Emp;
import com.atguigu.mybatis.utils.SqlSessionUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.List;

/**
 * @Author: 刘华昌
 * @Date: 2022/8/1 22:57
 * @ClassName:
 * @Description:
 */
public class PageTest {

    @Test
    public void testPage(){
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        EmpMapper mapper = sqlSession.getMapper(EmpMapper.class);

        //在查询功能之前 开启分页
        PageHelper.startPage(1,4); //第一页，每页四条记录
        //查询所有数据
        List<Emp> list = mapper.selectByExample(null);
        list.forEach(System.out::println);
    }

    @Test
    public void testPageHelper01() {
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        EmpMapper mapper = sqlSession.getMapper(EmpMapper.class);
        //1. 在查询功能之前 开启分页
        Page<Object> page = PageHelper.startPage(1, 4);
        //2. 查询所有数据
        List<Emp> emps = mapper.selectByExample(null);
        System.out.println("-------------------------");
        //3. 在查询功能之后，打印分页数据
        System.out.println(page);
        //4. 打印获取的记录
        System.out.println("-------------------------");
        emps.forEach(System.out::println);
    }

    @Test
    public void testPageHelper02() {

        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        EmpMapper mapper = sqlSession.getMapper(EmpMapper.class);

        //在查询功能之前 开启分页
        PageHelper.startPage(3,4); //第一页，每页四条记录
        //查询所有数据
        List<Emp> emps = mapper.selectByExample(null);
        //查询功能之后，获取分页相关数据
        System.out.println("-------------------------");
        PageInfo<Emp> page = new PageInfo<>(emps,5); //记录数集合, 导航分页的页码数
        System.out.println(page);


    }
}
