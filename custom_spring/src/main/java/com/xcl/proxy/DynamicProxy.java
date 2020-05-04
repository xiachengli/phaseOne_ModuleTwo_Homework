package com.xcl.proxy;

import com.xcl.utils.TransactionManager;
import net.sf.cglib.proxy.Enhancer;

import java.lang.reflect.Proxy;

/**
 * 动态代理
 * 目标类实现了接口就使用JDK动态代理
 * 没有实现接口就使用CGLIB动态代理
 */
public class DynamicProxy {

    private TransactionManager transactionManager;

    private  Object instance;

    public DynamicProxy(TransactionManager transactionManager,Object target) {
        this.transactionManager = transactionManager;
        this.instance = target;
    }

    //JDK动态代理
    public Object getJDKProxy(ClassLoader classLoader){
        TransactionHandle transactionHandle = new TransactionHandle(instance,transactionManager);
        return Proxy.newProxyInstance(classLoader,instance.getClass().getInterfaces(),transactionHandle);
    }


    //CGLIB动态代理
    public Object getCGLIBProxy() {
        TransactionEnhance transactionEnhance = new TransactionEnhance(transactionManager);
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(instance.getClass());
        enhancer.setCallback(transactionEnhance);
        return enhancer.create();
    }
}
