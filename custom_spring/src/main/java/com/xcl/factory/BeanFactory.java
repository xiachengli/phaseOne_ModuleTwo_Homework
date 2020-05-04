package com.xcl.factory;

import com.xcl.utils.AnnotationParseUtil;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 加载配置文件，反射创建对象，保存在map中
 * 提供外界获取bean的方法
 */
public class BeanFactory {

    //key:bean的id;value:bean实例化对象（已设置属性）
    private static Map<String,Object> beans = new ConcurrentHashMap<>(256);

    static {
        parse();
    }

    /**
     * 加载配置文件并解析
     */
    private static void parse() {
        InputStream inputStream = BeanFactory.class.getClassLoader().getResourceAsStream("applicationContext.xml");
        try {
            Document document = new SAXReader().read(inputStream);
            //得到根节点
            Element rootElement = document.getRootElement();
            //xpath表达式获取指定节点
            List<Element> beanElements = rootElement.selectNodes("//bean");
            //*实例化对象
            for (Element bean : beanElements) {
                //id
                String id = bean.attributeValue("id");
                //类全限定类名
                String clsName = bean.attributeValue("class");

                //通过反射创建对象
                Class cls = Class.forName(clsName);
                //通过类的无参构造器创建对象
                Object object =cls.getDeclaredConstructor().newInstance();

                //保存
                beans.put(id,object);
            }

            //*判断是否开启注解扫描
            List<Element> annotation = rootElement.selectNodes("component-scan");
            String packageName = "";
            boolean flag = false;
            if (null != annotation) {
                flag = true;
                packageName = annotation.get(0).attributeValue("base-package");
                AnnotationParseUtil.getServiceAnnotation(packageName,beans);
            }

            //*设置属性
            List<Element> propertyList = rootElement.selectNodes("//property");
            for (Element property : propertyList) {
                //具有property节点的节点（父节点），就是需要设置属性的bean
                //获取父节点相关信息
                Element parent = property.getParent();
                String beanId = parent.attributeValue("id");
                Object bean = beans.get(beanId);
                if (null == bean) {
                    throw new RuntimeException("解析异常，不存在id为"+beanId+"的bean");
                }

                //name
                String name = property.attributeValue("name");
                //value
                String value = property.attributeValue("value");
                //ref
                String ref = property.attributeValue("ref");
                if (null == value && null == ref) {
                    throw new RuntimeException("解析异常，请按照约定进行配置");
                }else if(null != value) {
                    //遍历bean的方法，找到set+value的方法
                    Method[] methods = bean.getClass().getMethods();
                    for (Method method : methods) {
                        if (method.getName().equalsIgnoreCase("set"+name)){
                            method.invoke(bean,value);
                        }
                    }

                }else if (null != ref){
                    Object object = beans.get(ref);
                    if (null == object) {
                        throw new RuntimeException("解析异常，bean未创建"+ref);
                    }
                    //遍历bean的方法，找到set+value的方法
                    Method[] methods = bean.getClass().getMethods();
                    for (Method method : methods) {
                        if (method.getName().equalsIgnoreCase("set"+name
                        )){
                            method.invoke(bean,object);
                        }
                    }
                }


                //覆盖之前实例化的对象
                beans.put(beanId,bean);
            }


            //已开启注解扫描，处理Autowired注解
            if (flag) {
                AnnotationParseUtil.getAutowiredAnnotation(packageName,beans);
            }

           //至此，对象创建完毕

            //已开启注解扫描，处理Transaction注解
            if (flag) {
                AnnotationParseUtil.getTransactionAnnotation(BeanFactory.class.getClassLoader(),beans);
            }

        } catch (Exception e) {
            System.out.println("解析失败"+e.getMessage());
            throw  new RuntimeException("解析失败");


        }finally {
            try {
                if (null != inputStream) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 外界通过此方法获取bean
     */
    public static Object getBean(String id){
        return beans.get(id);
    }
}
