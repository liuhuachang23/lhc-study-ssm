package com.lhc.spring.dao;

/**
 * @Author: 刘华昌
 * @Date: 2022/8/7 20:28
 * @ClassName:
 * @Description:
 */
public interface BookDao {

    Integer getPriceByBookId(Integer bookId);

    void updateStock(Integer bookId);

    void updateBalance(Integer userId, Integer price);

}
