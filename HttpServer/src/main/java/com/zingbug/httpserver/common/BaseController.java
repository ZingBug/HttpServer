package com.zingbug.httpserver.common;

import com.zingbug.httpserver.core.handle.ControllerReactor;
import com.zingbug.httpserver.core.handle.response.BaseResponse;
import com.zingbug.httpserver.model.ControllerRequest;

/**
 * 控制器基类，只能传递数据，不能视图
 * Created by ZingBug on 2019/5/14.
 */
public interface BaseController {

    /**
     * get请求
     * @param controllerRequest
     * @return
     */
    BaseResponse doGet(ControllerRequest controllerRequest);

    /**
     * post请求
     * @param controllerRequest
     * @return
     */
    BaseResponse doPost(ControllerRequest controllerRequest);


}
