package com.zingbug.demo.filter;

import com.zingbug.httpserver.common.BaseFilter;
import com.zingbug.httpserver.common.annotation.Filter;
import com.zingbug.httpserver.model.ControllerRequest;
import lombok.extern.slf4j.Slf4j;

import javax.swing.text.DocumentFilter;

/**
 * Created by ZingBug on 2019/5/17.
 */
@Filter(order = 0)
@Slf4j
public class MyFilter implements BaseFilter {
    @Override
    public void beforeFilter(ControllerRequest controllerRequest) {
        log.info("filter before 0");
    }

    @Override
    public void afterFilter(ControllerRequest controllerRequest) {
        log.info("filter after 0");
    }
}
