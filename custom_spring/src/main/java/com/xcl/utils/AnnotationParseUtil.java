package com.xcl.utils;

import com.xcl.annotation.Autowired;
import com.xcl.annotation.Service;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

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
}
