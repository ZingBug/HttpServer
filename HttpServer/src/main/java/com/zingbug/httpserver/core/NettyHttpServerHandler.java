package com.zingbug.httpserver.core;

import com.alibaba.fastjson.JSON;
import com.zingbug.httpserver.common.BaseController;
import com.zingbug.httpserver.common.ViewController;
import com.zingbug.httpserver.core.handle.ControllerReactor;
import com.zingbug.httpserver.core.handle.FilterReactor;
import com.zingbug.httpserver.core.handle.StaticFileHandler;
import com.zingbug.httpserver.core.handle.response.BaseResponse;
import com.zingbug.httpserver.core.handle.response.ResponseCoreHandle;
import com.zingbug.httpserver.core.handle.response.ViewResponse;
import com.zingbug.httpserver.core.invoke.ControllerCglib;
import com.zingbug.httpserver.model.ControllerRequest;
import com.zingbug.httpserver.model.PicModel;
import com.zingbug.httpserver.util.CommonUtil;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 核心控件
 * Created by ZingBug on 2019/5/16.
 */
@Slf4j
public class NettyHttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest fullHttpRequest) throws Exception {
        String uri= CommonUtil.getUri(fullHttpRequest.uri());
        Object object= ControllerReactor.getClazzFromList(uri);
        String result="receive msg";
        Object response=null;

        response= StaticFileHandler.responseHandle(object,ctx,fullHttpRequest);//是否在读取静态资源，如果没有，则返回null

        if(!(response instanceof FullHttpResponse)&&!(response instanceof PicModel))
        {
            //接口处理
            if(CommonUtil.isContainInterFace(object, BaseController.class))//是否存在baseController接口
            {
                ControllerCglib cc=new ControllerCglib();//代理
                Object proxyObj=cc.getTarget(object);
                Method[] methodArr=null;
                Method aimMethod=null;

                if(fullHttpRequest.method().equals(HttpMethod.GET))
                {
                    methodArr=proxyObj.getClass().getMethods();//获取控制器中所有的方法
                    aimMethod=CommonUtil.getMethodByName(methodArr,"doGet");//找出doGet方法
                }
                else if(fullHttpRequest.method().equals(HttpMethod.POST))
                {
                    methodArr=proxyObj.getClass().getMethods();
                    aimMethod=CommonUtil.getMethodByName(methodArr,"doPost");
                }
                //代理执行method
                if(aimMethod!=null)
                {
                    ControllerRequest controllerRequest=parameterHandler(fullHttpRequest);//获取传递参数及头参数
                    FilterReactor.preHandler(controllerRequest);//添加前置过滤器执行
                    Object respon=aimMethod.invoke(proxyObj,controllerRequest);
                    if(respon instanceof ViewResponse)
                    {
                        //如果是视图控制器
                        ViewResponse viewResponse=(ViewResponse)respon;
                        result=CommonUtil.readPageHtmlInPath(viewResponse.getPath());
                    }
                    else
                    {
                        //是基础控制器
                        BaseResponse baseResponse=(BaseResponse) respon;
                        result= JSON.toJSONString(baseResponse);

                    }
                    FilterReactor.aftHandler(controllerRequest);//添加后置过滤器执行
                    //BaseResponse baseResponse=(BaseResponse)aimMethod.invoke(proxyObj,controllerRequest);//转到对应的controller执行

                }
            }
            response= ResponseCoreHandle.responseHtml(HttpResponseStatus.OK,result);
        }
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);

    }

    /**
     * 处理请求的参数内容
     * @param fullHttpRequest
     * @return
     */
    private ControllerRequest parameterHandler(FullHttpRequest fullHttpRequest)
    {
        //参数处理部分内容
        Map<String,Object> paramMap=new HashMap<>(60);
        if(fullHttpRequest.method()==HttpMethod.GET)
        {
            paramMap=ParameterHandler.getGetParamsFromChannel(fullHttpRequest);
        }
        else if(fullHttpRequest.method()==HttpMethod.POST)
        {
            paramMap=ParameterHandler.getPostParamsFromChannel(fullHttpRequest);
        }
        Map<String,String> headers=ParameterHandler.getHeaderData(fullHttpRequest);
        ControllerRequest ctr=new ControllerRequest();
        ctr.setParams(paramMap);
        ctr.setHeader(headers);
        return ctr;
    }
}
