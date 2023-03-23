package com.lhc.spring.service.impl;

import com.lhc.spring.dao.BookDao;
import com.lhc.spring.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

/**
 * @Author: 刘华昌
 * @Date: 2022/8/7 20:26
 * @ClassName:
 * @Description:
 */
@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookDao bookDao;

    @Override
    /*@Transactional(
            //readOnly = true

            //设置超时时间为3s
            //timeout = 3

            //noRollbackFor = {ArithmeticException.class}
            //noRollbackForClassName = "java.lang.ArithmeticException"

            //isolation = Isolation.DEFAULT

            propagation = Propagation.REQUIRES_NEW

    )*/
    public void buyBook(Integer bookId, Integer userId) {

        try {
            TimeUnit.SECONDS.sleep(5); //设置休眠时间 5s
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //查询图书的价格
        Integer price = bookDao.getPriceByBookId(bookId);
        //更新图书的库存
        bookDao.updateStock(bookId);
        //更新用户的余额
        bookDao.updateBalance(userId, price);

        //在这里故意制造员工运行时异常
        //System.out.println(1/0);
    }
}
