package com.xcl.service.imp;

import com.xcl.annotation.Autowired;
import com.xcl.annotation.Service;
import com.xcl.annotation.Transaction;
import com.xcl.dao.AccountDao;
import com.xcl.service.AccountService;

import java.math.BigDecimal;
import java.sql.SQLException;

@Service("accService")
@Transaction
public class AccServiceImp{

    @Autowired
    private AccountDao accountDao;
    /**
     * 转账业务
     * @param from 付款人账号
     * @param to 收款人账号
     * @param money 本次交易金额
     */
    public void transfer(Integer from, Integer to, BigDecimal money) {
        try {
            //通过Autowired注入属性accountDao
            accountDao.transfer(from,to,money);

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
}
