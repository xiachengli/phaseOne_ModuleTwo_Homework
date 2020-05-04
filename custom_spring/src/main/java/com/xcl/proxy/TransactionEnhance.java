package com.xcl.proxy;

import com.xcl.utils.TransactionManager;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class TransactionEnhance implements MethodInterceptor {

    private TransactionManager transactionManager;

    public TransactionEnhance(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        Object result = null;
        try{
            //开启事务
            transactionManager.beginTransaction();

            result = methodProxy.invokeSuper(obj,args);

            //提交事务
            transactionManager.commit();

        }catch (Exception e) {
            System.out.println("CGLIB代理失败，"+e.getMessage());
            //回滚
            transactionManager.rollback();

        }finally {
            transactionManager.after();
        }
        return result;
    }
}
