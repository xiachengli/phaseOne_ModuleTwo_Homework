package com.xcl.annotation;

import java.lang.annotation.*;

/**
 * 创建对象
 *
 * 注解：对程序进行标识，可被注解解析器执行用于完成相应的功能
 * @Target 注解的作用范围
 * @Retention 注解的生命周期
 * @Documented 标明该注解将会包含在javadoc中
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Service {

    //默认属性
    String value() default "";
}
