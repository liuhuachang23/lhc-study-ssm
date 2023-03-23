package com.lhc.mybatis.test;

import com.atguigu.mybatis.mapper.EmpMapper;
import com.atguigu.mybatis.pojo.Emp;
import com.atguigu.mybatis.pojo.EmpExample;
import com.atguigu.mybatis.utils.SqlSessionUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.List;

/**
 * @Author: 刘华昌
 * @Date: 2022/8/1 22:00
 * @ClassName:
 * @Description:
 */
public class MBGTest {

    @Test
    public void testMBG(){
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        EmpMapper mapper = sqlSession.getMapper(EmpMapper.class);

        //1. 根据主键id，查询单条数据
        // Emp emp = mapper.selectByPrimaryKey(1);
        // System.out.println(emp);

        //2. 查询所有数据
        // List<Emp> list = mapper.selectByExample(null);
        // System.out.println(list);

        /*
        //3. 根据条件来查询
        //3.1 创建条件对象
        EmpExample empExample = new EmpExample();
        //3.2 添加条件（QBC风格）
        empExample.createCriteria().andEmpNameEqualTo("张三").andAgeGreaterThanOrEqualTo(20);
        empExample.or().andGenderEqualTo("男"); // == > ( emp_name = "张三" and age >= 20 ) or( gender = "男" )
        //3.3 执行
        List<Emp> list = mapper.selectByExample(empExample);
        System.out.println(list);
        */

        //4. 普通修改 (当修改时 属性设置为null，或者不设置 就会把字段修改为 null)
        //update t_emp set emp_name = ?, age = ?, gender = ?, dept_id = ? where emp_id = ?
        //Emp emp = new Emp(1,"小黑",null,"女");
        //mapper.updateByPrimaryKey(emp);

        //5. 选择修改 (当修改时 属性设置为null，或者不设置 就不会字段修改字段)
        //update t_emp SET emp_name = ?, gender = ? where emp_id = ?
        //Emp emp = new Emp(1,"小黑",null,"女");
        //mapper.updateByPrimaryKeySelective(emp);

        //6. 普通添加 (当添加时 属性设置为null，或者不设置 就会把字段的值添加为 null)
        //insert into t_emp (emp_id, emp_name, age, gender, dept_id) values (?, ?, ?, ?, ?)
        //Emp emp = new Emp(null,"小白",null,"女");
        //mapper.insert(emp);

        //7. 选择性添加 (当修改时 属性设置为null，或者不设置 就不会字段添加字段值，即让他遵循本数据库的默认值约束)
        //insert into t_emp ( emp_name, gender ) values ( ?, ? )
        Emp emp = new Emp(null,"小白",null,"女");
        mapper.insertSelective(emp);

    }
}
