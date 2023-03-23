package com.lhc.spring.service.impl;

import com.lhc.spring.service.BookService;
import com.lhc.spring.service.CheckoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: 刘华昌
 * @Date: 2022/8/8 11:08
 * @ClassName:
 * @Description:
 */
@Service
public class CheckoutServiceImpl implements CheckoutService {

    @Autowired
    private BookService bookService;

    @Override
    //@Transactional
    public void checkout(Integer[] bookIds, Integer userId) {     //一次购买多本图书
        for (Integer bookId : bookIds) {
            bookService.buyBook(bookId, userId);
        }
    }

}
