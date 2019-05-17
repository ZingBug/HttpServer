package com.zingbug.httpserver.core.invoke;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 代理类（过时）
 * Created by ZingBug on 2019/5/15.
 */
@Deprecated
public class Invocation implements InvocationHandler {

    //目标对象
    private Object target;

    public Invocation(Object target)
    {
        super();
        this.target=target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return method.invoke(target,args);
    }

    /**
     * 获取目标对象的代理对象
     * @return
     */
    public Object getProxy()
    {
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),target.getClass().getInterfaces(),this);
    }
}
