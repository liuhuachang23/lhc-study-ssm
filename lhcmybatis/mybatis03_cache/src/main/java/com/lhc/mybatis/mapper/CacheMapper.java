package com.lhc.mybatis.mapper;

import com.lhc.mybatis.pojo.Emp;
import org.apache.ibatis.annotations.Param;

/**
 * @Author: 刘华昌
 * @Date: 2022/8/1 15:08
 * @ClassName:
 * @Description:
 */
public interface CacheMapper {

    /**
     * 根据员工id查询员工信息
     * @param empId
     * @return
     */
    Emp getEmpById(@Param("empId") Integer empId);

}
