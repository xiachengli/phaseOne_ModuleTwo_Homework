package com.xcl.dao.imp;

import com.xcl.dao.AccountDao;
import com.xcl.pojo.Account;
import com.xcl.utils.ConnectionUtil;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountDaoImp implements AccountDao {

    //通过xml+setter的方式注入属性
    ConnectionUtil connectionUtil;
    public void setConnectionUtil(ConnectionUtil connectionUtil) {
        this.connectionUtil = connectionUtil;
    }

    @Override
    public void transfer(Integer from, Integer to, BigDecimal money) throws SQLException {

        Account fromAccount = selectAccountById(from);
        Account toAccount = selectAccountById(to);

       System.out.println("转账前，付款人余额："+fromAccount);
       System.out.println("转账前，收款人余额："+toAccount);

        fromAccount.setMoney(fromAccount.getMoney().subtract(money));
        toAccount.setMoney(toAccount.getMoney().add(money));

        //更新
        System.out.println("付款人："+update(fromAccount));

        //认为制造异常
        //int i = 10/0;

        System.out.println("收款人："+update(toAccount));


        //查询余额
        System.out.println("转账后，付款人余额："+fromAccount);
        System.out.println("转账后，收款人余额："+toAccount);
    }

    /**
     * 根据卡号查询账号信息
     * @param id 卡号
     * @return
     */
    @Override
    public Account selectAccountById(Integer id) throws SQLException {
       // ConnectionUtil connectionUtil = (ConnectionUtil)BeanFactory.getBean("connectionUtil");
        Account account = new Account();
        Connection connection = connectionUtil.getConnection();
        String sql = "select * from account where id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setObject(1,id);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            Integer cardId = resultSet.getInt("id");
            String username = resultSet.getString("username");
            BigDecimal money = resultSet.getBigDecimal("money");
            account.setId(cardId);
            account.setName(username);
            account.setMoney(money);

        }
        return account;

    }

    /**
     * 更新卡号
     * @param account
     */
    @Override
    public int update(Account account) throws SQLException {
        Connection connection = connectionUtil.getConnection();
        String sql = "update account set money = ? WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setObject(1,account.getMoney());
        preparedStatement.setObject(2,account.getId());

        return preparedStatement.executeUpdate();

    }

}
