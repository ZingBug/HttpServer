package com.zingbug.httpserver.core.invoke;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * Created by ZingBug on 2019/5/15.
 */
public class ControllerCglib implements MethodInterceptor {

    private Object target;
    public Object getTarget(Object target)
    {
        this.target=target;
        Enhancer enhancer=new Enhancer();
        //设置代理对象
        enhancer.setSuperclass(this.target.getClass());
        //设置代理
        enhancer.setCallback(this);
        return enhancer.create();
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        preHandle();
        Object value=proxy.invokeSuper(obj,args);
        afterHandle();
        return value;
    }

    private void preHandle()
    {
        //System.out.println("pre");
    }

    private void afterHandle()
    {
        //System.out.println("after");
    }


}

