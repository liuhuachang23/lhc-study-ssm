package com.lhc.spring.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

/**
 * @Author: 刘华昌
 * @Date: 2022/8/6 20:41
 * @ClassName:
 * @Description:
 */
public class ProxyFactory {

    private Object target;

    public ProxyFactory(Object target) {
        this.target = target;
    }

    public Object getProxy() {

        ClassLoader classLoader = this.getClass().getClassLoader();
        Class<?>[] interfaces = target.getClass().getInterfaces();
        InvocationHandler h = new InvocationHandler() {
            /*
             * Object proxy  : 代理对象
             * Method method : 要执行的方法
             * Object[] args : 要执行的方法的参数列表
             * */
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                Object result = null;

                try {
                    //1）执行目标方法前的日志
                    System.out.println("[动态代理][日志] " + method.getName() + "，参数：" + Arrays.toString(args));

                    //2）调用目标类核心方法
                    result = method.invoke(target, args);

                    //3）调用目标类方法后的日志
                    System.out.println("[动态代理][日志] " + method.getName() + "，结果：" + result);

                } catch (Exception e) {
                    e.printStackTrace();
                    //4）出现异常时的日志
                    System.out.println("[动态代理][日志] "+method.getName()+"，异常："+e);
                } finally {
                    //5）方法执行完毕时的日志
                    System.out.println("[动态代理][日志] "+method.getName()+"，方法执行完毕");
                }
                return result;
            }
        };

        /*
         * ClassLoader loader  ：指定加载动态生成代理类的 类加载器
         * Class[] interfaces  ：获取目标对象实现的所有的接口的Class对象数组 （为了确保代理类和目标类实现相同的功能）
         * InvocationHandler h ：设置代理类中的抽象方法如何重写 （重写目标对象，调用目标类核心方法 并加入需要的日志信息）
         * */
        return Proxy.newProxyInstance(classLoader, interfaces, h);

    }
}
