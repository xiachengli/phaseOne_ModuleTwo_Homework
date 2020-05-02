package com.xcl.annotation;

import java.lang.annotation.*;

/**
 * 设置属性
 *
 * 注解：对程序进行标识，可被注解解析器执行用于完成相应的功能
 *  * @Target 注解的作用范围
 *  * @Retention 注解的生命周期
 *  * @Documented 标明该注解将会包含在javadoc中
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Autowired {

    String value() default "";
}
