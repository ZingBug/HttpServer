package com.zingbug.demo.controller;

import com.zingbug.httpserver.common.BaseController;
import com.zingbug.httpserver.common.ViewController;
import com.zingbug.httpserver.common.annotation.Controller;
import com.zingbug.httpserver.core.handle.response.BaseResponse;
import com.zingbug.httpserver.core.handle.response.ViewResponse;
import com.zingbug.httpserver.model.ControllerRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by ZingBug on 2019/5/17.
 */
@Slf4j
@Controller(url = "/getView")
public class SecondController implements ViewController {

    @Override
    public ViewResponse doGet(ControllerRequest controllerRequest) {
        return new ViewResponse(0,"html/home.html");
    }

    @Override
    public ViewResponse doPost(ControllerRequest controllerRequest) {
        return null;
    }
}
