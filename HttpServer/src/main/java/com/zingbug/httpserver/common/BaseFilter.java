package com.zingbug.httpserver.common;

import com.zingbug.httpserver.model.ControllerRequest;

/**
 * 基础过滤器
 * Created by ZingBug on 2019/5/15.
 */
public interface BaseFilter {

    /**
     * 过滤器之前执行
     * @param controllerRequest
     */
    void beforeFilter(ControllerRequest controllerRequest);


    /**
     * 过滤器之后执行
     * @param controllerRequest
     */
    void afterFilter(ControllerRequest controllerRequest);
}
