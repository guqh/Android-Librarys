package com.john.librarys.utils.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 类工具
 */
public class ClassUtil {
    /**
     * 通过反射, 获得定义Class时声明的父类的泛型参数的类型. 如无法找到, 返回Object.class.
     *
     * @param object
     * @return
     */
    public static Class getSuperClassGenricType(Object object) {
        return getSuperClassGenricType(object.getClass(), object.getClass().getSuperclass(), 0);
    }

    /**
     * 通过反射, 获得定义Class时声明的父类的泛型参数的类型. 如无法找到, 返回Object.class.
     *
     * @param clazz         clazz The class to introspect
     * @param topSuperClass 最后一层父类
     * @return
     */
    public static Class getSuperClassGenricType(final Class clazz, final Class topSuperClass) {
        return getSuperClassGenricType(clazz, topSuperClass, 0);
    }

    /**
     * 通过反射, 获得定义Class时声明的父类的泛型参数的类型. 如无法找到, 返回Object.class.
     *
     * @param clazz         clazz The class to introspect
     * @param topSuperClass 最后一层父类
     * @param index         the Index of the generic ddeclaration,start from 0.
     * @return the index generic declaration, or Object.class if cannot be
     * determined
     */
    @SuppressWarnings("unchecked")
    public static Class<Object> getSuperClassGenricType(final Class clazz, final Class topSuperClass, final int index) {


        Class superClass = clazz;
        //往上遍历到 baseService 的子类
        Class tmpClass = superClass;

        while (superClass != topSuperClass) {
            tmpClass = superClass;
            superClass = superClass.getSuperclass();
        }

        return getClassGenricType(tmpClass, index);
    }


    /**
     * 获取泛型
     * @param clazz
     * @return
     */
    public static Class<Object> getClassGenricType(final Class clazz) {
        return getClassGenricType(clazz, 0);
    }

    /**
     * 获取泛型
     *
     * @param clazz
     * @param index
     * @return
     */
    public static Class<Object> getClassGenricType(final Class clazz, int index) {
        Type genType = clazz.getGenericSuperclass();

        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        }
        //返回表示此类型实际类型参数的 Type 对象的数组。
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

        if (index >= params.length || index < 0) {
            return Object.class;
        }

        Type type = params[index];
        if (type instanceof Class) {
            return (Class) params[index];
        }
        //如果还有泛型的就找当前的类型
        if(type instanceof ParameterizedType){
            Type t = ((ParameterizedType) type).getRawType();
            if (t instanceof Class) {
                return (Class) t;
            }
        }

        return Object.class;
    }
}
