package com.zingbug.demo.filter;

import com.zingbug.httpserver.common.BaseFilter;
import com.zingbug.httpserver.common.annotation.Filter;
import com.zingbug.httpserver.model.ControllerRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by ZingBug on 2019/5/17.
 */
@Filter(order = 1)
@Slf4j
public class SecondFilter implements BaseFilter {
    @Override
    public void beforeFilter(ControllerRequest controllerRequest) {
        log.info("filter before 1");
    }

    @Override
    public void afterFilter(ControllerRequest controllerRequest) {
        log.info("filter after 1");
    }
}
