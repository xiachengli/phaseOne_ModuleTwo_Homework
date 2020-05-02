package com.xcl.utils;


import java.sql.SQLException;

public class TransactionManager {
    private ConnectionUtil connectionUtil;

    public void setConnectionUtil(ConnectionUtil connectionUtil) {
        this.connectionUtil = connectionUtil;
    }
    //开启事务

    public void beginTransaction() throws SQLException {
        connectionUtil.getConnection().setAutoCommit(false);
    }
    //提交事务
    public void commit() throws SQLException {
        connectionUtil.getConnection().commit();
    }
    //回滚事务
    public void rollback() throws SQLException {
        connectionUtil.getConnection().rollback();
    }
}
