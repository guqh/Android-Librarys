package com.john.librarys.utils.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Bean 工具类
 */
public class BeanHelper {

    /**
     * 拷贝属性 从 src 到dest
     * @param src
     * @param dest
     * @return
     * @throws Exception
     */
    public static Object CopyBeanToBean(Object src, Object dest) {
        Method[] method1 = src.getClass().getMethods();
        Method[] method2 = dest.getClass().getMethods();
        String methodName1;
        String methodFix1;
        String methodName2;
        String methodFix2;
        for (int i = 0; i < method1.length; i++) {
            methodName1 = method1[i].getName();
            methodFix1 = methodName1.substring(3, methodName1.length());
            if (methodName1.startsWith("get")) {
                for (int j = 0; j < method2.length; j++) {
                    methodName2 = method2[j].getName();
                    methodFix2 = methodName2.substring(3, methodName2.length());
                    if (methodName2.startsWith("set")) {
                        if (methodFix2.equals(methodFix1)) {
                            Object[] objs1 = new Object[0];
                            Object[] objs2 = new Object[1];

                            try {
                                objs2[0] = method1[i].invoke(src, objs1);//激活obj1的相应的get的方法，objs1数组存放调用该方法的参数,此例中没有参数，该数组的长度为0
                                method2[j].invoke(dest, objs2);//激活obj2的相应的set的方法，objs2数组存放调用该方法的参数
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                            continue;

                        }
                    }
                }
            }
        }
        return dest;
    }

}
