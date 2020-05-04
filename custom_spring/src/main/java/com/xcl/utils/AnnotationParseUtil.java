package com.xcl.utils;

import com.xcl.annotation.Autowired;
import com.xcl.annotation.Service;
import com.xcl.annotation.Transaction;
import com.xcl.proxy.DynamicProxy;
import com.xcl.proxy.TransactionHandle;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 注解解析器
 */
public class AnnotationParseUtil {

    /**
     * 获取指定包下的标有Service注解的class
     * @param packageName
     * @return
     */
    public static void getServiceAnnotation(String packageName,Map<String,Object> beans) throws Exception{
        Reflections reflections = new Reflections(packageName);
        //获取标有service注解的类
        Set<Class<?>> beanList = reflections.getTypesAnnotatedWith(Service.class);
        //System.out.println(beanList.size());
        /*
        反射 创建对象 并放入beans中
         */
        for (Class<?> temp : beanList) {
            Object object = temp.getDeclaredConstructor().newInstance();
            String value = temp.getAnnotation(Service.class).value();
            //System.out.println(value);
            beans.put(value,object);
        }
    }

    public static void getAutowiredAnnotation(String packageName, Map<String, Object> beans) throws IllegalAccessException {
        Reflections reflections = new Reflections(packageName);
        //获取标有service注解的类
        Set<Class<?>> beanList = reflections.getTypesAnnotatedWith(Service.class);
        for (Class<?> temp : beanList) {
            //得到key
            String key = temp.getAnnotation(Service.class).value();
            Object object = beans.get(key);

           Field[] fields = temp.getDeclaredFields();
            for (Field field : fields) {
                Annotation annotation = field.getAnnotation(Autowired.class);
                Object value = beans.get(field.getName());
                //不为空，表明该字段被Autowired修饰
               if (null != annotation) {
                    field.setAccessible(true);
                    field.set(object,value);
               }
            }
        }
    }

    /**
     * 处理Transaction注解
     * @param classLoader
     * @param beans
     */
    public static void getTransactionAnnotation(ClassLoader classLoader, Map<String, Object> beans) {
        TransactionManager transactionManager = (TransactionManager) beans.get("transactionManager");
        for (Map.Entry<String,Object> temp : beans.entrySet()) {
            String beanId = temp.getKey();
            Object instance =  temp.getValue();

            Class target = instance.getClass();
            Annotation annotation = target.getAnnotation(Transaction.class);

            if (null != annotation) {
                //为目标类生成代理对象
                Object proxy = null;
                Class[] interfaces = target.getInterfaces();

                DynamicProxy dynamicProxy = new DynamicProxy(transactionManager,instance);
                if (interfaces.length > 0) {
                    proxy = dynamicProxy.getJDKProxy(classLoader);
                }else {
                    proxy = dynamicProxy.getCGLIBProxy();
                }

                //用代理类对象替代目标类
                beans.put(beanId,proxy);
            }

        }
    }
}
