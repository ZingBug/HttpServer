package com.zingbug.httpserver.core.handle;

import com.zingbug.httpserver.common.constant.RequestConstants;
import com.zingbug.httpserver.core.handle.response.ResponseCoreHandle;
import com.zingbug.httpserver.model.CssModel;
import com.zingbug.httpserver.model.JsModel;
import com.zingbug.httpserver.model.PageModel;
import com.zingbug.httpserver.model.PicModel;
import com.zingbug.httpserver.util.CommonUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.io.IOException;

/**
 * 静态文件处理器
 * Created by ZingBug on 2019/5/15.
 */
public class StaticFileHandler {


    /**
     * 静态文件处理器
     * @param object
     * @param ctx
     * @param fullHttpRequest
     * @return
     * @throws IOException
     */
    public static Object responseHandle(Object object, ChannelHandlerContext ctx,FullHttpRequest fullHttpRequest) throws IOException{
        String result;
        FullHttpResponse response=null;
        //接口的404处理模块
        if(object==null)
        {
            result= CommonUtil.read404Html();
            return ResponseCoreHandle.responseHtml(HttpResponseStatus.OK,result);
        }
        else if(object instanceof JsModel)
        {
            JsModel jsModel=(JsModel) object;
            result=CommonUtil.readFileFromResource(jsModel.getUrl());
            response=notFoundHandler(result);
            return (response==null)? ResponseCoreHandle.responseJs(HttpResponseStatus.OK,result):response;
        }
        else if(object instanceof CssModel)
        {
            CssModel cssModel=(CssModel)object;
            result=CommonUtil.readFileFromResource(cssModel.getUrl());
            response=notFoundHandler(result);
            return (response==null)?ResponseCoreHandle.responseCss(HttpResponseStatus.OK,result):response;
        }
        else if(object instanceof PageModel)
        {
            PageModel pageModel=(PageModel) object;
            if(pageModel.getCode()==RequestConstants.INDEX_CODE)
            {
                result=CommonUtil.readIndexHtml(pageModel.getPagePath());
            }
            else
            {
                result=CommonUtil.readFileFromResource(pageModel.getPagePath());
            }
            return ResponseCoreHandle.responseHtml(HttpResponseStatus.OK,result);
        }
        else if(object instanceof PicModel)
        {
            PicModel picModel=(PicModel)object;
            ResponseCoreHandle.writePic(picModel.getUrl(),ctx,fullHttpRequest);
            return picModel;
        }
        return null;
    }


    /**
     * 专门防止静态资源未有读取的情况
     * @param result
     * @return
     * @throws IOException
     */
    private static FullHttpResponse notFoundHandler(String result) throws IOException{
        if(RequestConstants.PAGE_NOT_FOUND.equals(result))
        {
            return ResponseCoreHandle.response404Html();
        }
        return null;
    }
}
