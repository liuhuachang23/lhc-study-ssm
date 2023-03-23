package com.lhc.mybatis.mapper;

import com.lhc.mybatis.pojo.Dept;
import com.lhc.mybatis.pojo.Emp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: 刘华昌
 * @Date: 2022/8/1 19:24
 * @ClassName:
 * @Description:
 */
public interface DeptMapper {

    //============= 多对一 分布查询（第二步）================
    /**
     * 通过分步查询，员工及所对应的部门信息
     * 分步查询第二步：通过deptId查询员工对应的部门信息
     */
    Dept getEmpAndDeptByStepTwo(@Param("deptId") Integer deptId); //第一步中 select属性 就是绑定这个方法


}
