package com.zingbug.demo.controller;

import com.zingbug.httpserver.common.BaseController;
import com.zingbug.httpserver.common.annotation.Controller;
import com.zingbug.httpserver.core.handle.response.BaseResponse;
import com.zingbug.httpserver.model.ControllerRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by ZingBug on 2019/5/16.
 */
@Slf4j
@Controller(url = "/myController")
public class MyController implements BaseController {

    @Override
    public BaseResponse doGet(ControllerRequest controllerRequest) {
        String username=(String)controllerRequest.getParameter("username");
        log.info("UserName: "+username);
        return new BaseResponse(1,username);
    }

    @Override
    public BaseResponse doPost(ControllerRequest controllerRequest) {
        return null;
    }
}
