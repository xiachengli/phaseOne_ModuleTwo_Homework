package com.xcl.proxy;

import com.xcl.utils.TransactionManager;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class TransactionHandle implements InvocationHandler {

    private TransactionManager transactionManager;
    private Object instance;

    public TransactionHandle(Object target,TransactionManager transactionManager) {
        this.instance = target;
        this.transactionManager = transactionManager;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try{
            //开启事务
            transactionManager.beginTransaction();

            //to do something
            method.invoke(instance,args);

            //提交事务
            transactionManager.commit();

        }catch (Exception e) {
            System.out.println("JDK代理失败，"+e.getMessage());
            //回滚
            transactionManager.rollback();

        }finally {
            transactionManager.after();
        }
        return proxy;
    }
}
