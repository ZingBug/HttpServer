package com.zingbug.httpserver.core;

import com.alibaba.fastjson.JSON;
import com.zingbug.httpserver.common.constant.RequestConstants;
import com.zingbug.httpserver.util.CommonUtil;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.MemoryAttribute;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 获取参数
 * Created by ZingBug on 2019/5/16.
 */
@Slf4j
public class ParameterHandler {

    /**
     * 获取GET方式传递的参数
     *
     * @param fullHttpRequest
     * @return
     */
    public static Map<String, Object> getGetParamsFromChannel(FullHttpRequest fullHttpRequest) {
        Map<String, Object> params = new HashMap<>(5);
        if (fullHttpRequest.method() == HttpMethod.GET) {
            //处理get请求
            QueryStringDecoder decoder = new QueryStringDecoder(fullHttpRequest.uri());//url解析器，在uri中获取各种传递的参数
            for (Map.Entry<String, List<String>> entry : decoder.parameters().entrySet()) {
                params.put(entry.getKey(), entry.getValue().get(0));
            }
            return params;
        } else {
            return null;
        }
    }

    /**
     * 获取POST方式传递的参数
     *
     * @param fullHttpRequest
     * @return
     */
    public static Map<String, Object> getPostParamsFromChannel(FullHttpRequest fullHttpRequest) {
        Map<String, Object> params = new HashMap<>(5);
        if (fullHttpRequest.method() == HttpMethod.POST) {
            //处理post请求
            String strContentType = fullHttpRequest.headers().get(RequestConstants.CONTENT_TYPE).trim();
            if (strContentType.contains(RequestConstants.FROM_PARAMS_TYPE)) {
                params = getFormParams(fullHttpRequest);
            } else if (strContentType.contains(RequestConstants.JSON_PARAMS_TYPE)) {
                params = getJSONParams(fullHttpRequest);
            }
            return params;
        }
        return null;
    }

    /**
     * 解析from表单数据（Content-Type = x-www-form-urlencoded）
     *
     * @param fullHttpRequest
     * @return
     */
    public static Map<String, Object> getFormParams(FullHttpRequest fullHttpRequest) {
        Map<String, Object> params = new HashMap<>(5);

        HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(new DefaultHttpDataFactory(false), fullHttpRequest);
        for (InterfaceHttpData data : decoder.getBodyHttpDatas()) {
            if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute) {
                MemoryAttribute attribute = (MemoryAttribute) data;
                params.put(attribute.getName(), attribute.getValue());
            }
        }
        return params;
    }

    /**
     * 解析json数据（Content-Type = application/json）
     *
     * @param fullHttpRequest
     * @return
     */
    public static Map<String, Object> getJSONParams(FullHttpRequest fullHttpRequest) {
        Map<String, Object> params = new HashMap<>(5);

        ByteBuf content = fullHttpRequest.content();
        byte[] reqContent = new byte[content.readableBytes()];
        content.readBytes(reqContent);
        String strContent = null;
        try {
            strContent = new String(reqContent, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("[getJSONParams]编码字符异常为{}", e);
        }
        params = JSON.parseObject(strContent, Map.class);
        return params;
    }

    /**
     * 获取header里面的数据
     *
     * @param fullHttpRequest
     * @return
     */
    public static Map<String, String> getHeaderData(FullHttpRequest fullHttpRequest) {
        HttpHeaders headers = fullHttpRequest.headers();
        Map<String, String> headerParam = new HashMap<>(5);
        for (Map.Entry<String, String> headerEntry : headers.entries()) {
            headerParam.put(headerEntry.getKey(), headerEntry.getValue());
        }
        return headerParam;
    }

}
