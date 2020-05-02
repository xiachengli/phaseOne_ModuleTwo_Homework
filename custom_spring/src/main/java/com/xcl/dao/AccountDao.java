package com.xcl.dao;

import com.xcl.pojo.Account;

import java.math.BigDecimal;
import java.sql.SQLException;

public interface AccountDao {

    Account selectAccountById(Integer id) throws SQLException;

    int update(Account account) throws SQLException;

    void transfer(Integer from, Integer to, BigDecimal money) throws SQLException;
}
