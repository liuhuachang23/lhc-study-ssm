package com.lhc.mybatis.mapper;

import com.lhc.mybatis.pojo.Emp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: 刘华昌
 * @Date: 2022/8/1 19:04
 * @ClassName:
 * @Description:
 */
public interface EmpMapper {


    //============= 多对一 分布查询（第一步）================
    /**
     * 通过分步查询，员工及所对应的部门信息
     * 分步查询第一步：查询员工信息
     */
    Emp getEmpAndDeptByStepOne(@Param("empId") Integer empId);
}
