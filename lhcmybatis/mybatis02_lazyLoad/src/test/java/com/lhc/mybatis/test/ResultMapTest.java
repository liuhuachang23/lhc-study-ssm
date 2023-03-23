package com.lhc.mybatis.test;

import com.lhc.mybatis.mapper.EmpMapper;
import com.lhc.mybatis.pojo.Emp;
import com.lhc.mybatis.utils.SqlSessionUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

/**
 * @Author: 刘华昌
 * @Date: 2022/8/1 19:35
 * @ClassName:
 * @Description:
 */
public class ResultMapTest {

    @Test
    public void checkLogin() {

        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        EmpMapper mapper = sqlSession.getMapper(EmpMapper.class);
        //开启了延迟加载之后我们调用 查询员工及所对应的部门信息的方法（分布查询）
        Emp emp = mapper.getEmpAndDeptByStepOne(1);
        //这里我只获取emp.getEmpName() 他就会按需加载，不会去执行第二步（获取部门信息）
        System.out.println("只需获取员工的名字：" + emp.getEmpName());

        sqlSession.close();
    }
}
