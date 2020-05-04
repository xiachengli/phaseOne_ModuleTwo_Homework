package com.xcl.utils;


import java.sql.SQLException;

public class TransactionManager {
    private ConnectionUtil connectionUtil;

    public void setConnectionUtil(ConnectionUtil connectionUtil) {
        this.connectionUtil = connectionUtil;
    }
    //开启事务
    public void beginTransaction() throws SQLException {
        System.out.println("事务管理器*******开启事务");
        connectionUtil.getConnection().setAutoCommit(false);
    }
    //提交事务
    public void commit() throws SQLException {
        System.out.println("事务管理器*******提交事务");
        connectionUtil.getConnection().commit();
    }
    //回滚事务
    public void rollback() throws SQLException {
        System.out.println("事务管理器*******回滚事务");
        connectionUtil.getConnection().rollback();
    }

    public void after() {
        System.out.println("事务管理器*******方法执行完毕");
    }
}
