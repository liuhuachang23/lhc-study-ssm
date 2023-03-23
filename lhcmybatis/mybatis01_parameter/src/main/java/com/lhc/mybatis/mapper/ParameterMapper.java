package com.lhc.mybatis.mapper;

import com.lhc.mybatis.pojo.User;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author: 刘华昌
 * @Date: 2022/7/29 21:10
 * @ClassName: Mapper接口
 * @Description:
 */
public interface ParameterMapper {



    //========================== 1.单个字面量类型的参数 =============================

    /**
     * mapper接口中的方法参数为 单个的字面量类型的参数
     * @param username
     * @return
     */
    User getUserByUsername(String username);

    //========================== 2.多个字面量类型的参数 =============================

    //------------------ 2.1 MyBatis自动封装成map集合 ------------------
    /**
     * mapper接口中的方法参数为 多个的字面量类型的参数
     * @param username
     * @param password
     * @return
     * 此时MyBatis会自动将这些参数放在一个map集合中
     *      1）以 arg0,arg1...为键，以参数 为值
     *      2）以 param1,param2...为键，以参数 为值
     * 只需要通过${}和#{}访问map集合的键就可以获取相对应的值
     */
    User checkLogin(String username, String password);

    //------------------ 2.2 手动封装成map集合 ------------------

    /**
     * mapper接口中的方法参数为 map集合类型
     * @param map
     * @return
     */
    User checkLoginByMap(Map<String, Object> map);

    //------------------ 2.3 手动封装成实体类 ------------------

    /**
     * mapper接口中的方法参数为 实体类类型
     * @param user
     */
    void insertUser(User user);

    //------------------ 2.4 使用@Param标识参数 ------------------

    /**
     * mapper接口中的方法参数 使用@Param标识
     * @param username
     * @param password
     * @return
     */
    User checkLoginByParam(@Param("username") String username,@Param("password") String password);


    //==========================================================================================

    /**
     * 根据用户id查询用户信息为map集合
     * @param id
     * @return
     */
    Map<String, Object> getUserToMap(@Param("id") int id);


    /**
     * 查询所有用户信息为map集合
     * @return
     * 将表中的数据以map集合的方式查询，一条数据对应一个map；
     * 若有多条数据，就会产生多个map集合，此时可以将这些map放在一个list集合中获取
     */
    List<Map<String, Object>> getAllUserToMap();

    /**
     * 查询所有用户信息为map集合
     * @return
     * 将表中的数据以map集合的方式查询，一条数据对应一个map；
     * 若有多条数据，就会产生多个map集合，并且最终要以一个map的方式返回数据，
     * 此时需要通过@MapKey注解设置map集合的键，值是每条数据所对应的map集合
     */
    @MapKey("id") //设置id属性 为这个map的key value为 Object(User)
    Map<String, Object> testGetAllUserToMap();
}
