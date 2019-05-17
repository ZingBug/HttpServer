package com.zingbug.httpserver.common;

import com.zingbug.httpserver.common.annotation.Controller;
import com.zingbug.httpserver.core.handle.response.ViewResponse;
import com.zingbug.httpserver.model.ControllerRequest;

/**
 * 视图控制器
 * Created by ZingBug on 2019/5/17.
 */
public interface ViewController extends BaseController {

    /**
     * GET请求
     * @param controllerRequest
     * @return
     */
    ViewResponse doGet(ControllerRequest controllerRequest);

    /**
     * POST请求
     * @param controllerRequest
     * @return
     */
    ViewResponse doPost(ControllerRequest controllerRequest);
}
