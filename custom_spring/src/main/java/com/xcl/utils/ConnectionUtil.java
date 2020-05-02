package com.xcl.utils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 获取连接
 */
public class ConnectionUtil {

    private DataSource dataSource;
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    //一个线程一个连接（不同于一请求一连接）
    private ThreadLocal<Connection> threadLocal = new ThreadLocal<>();


    /**
     * 本地存储里面有则返回，无新建并保存
     * @return
     */
    public Connection getConnection() throws SQLException {
        Connection connection = threadLocal.get();
        if (null == connection) {
            connection = dataSource.getConnection();
            threadLocal.set(connection);
        }
        return connection;
    }
}
