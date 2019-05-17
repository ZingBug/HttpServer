package com.zingbug.httpserver;


import com.zingbug.httpserver.common.config.HttpServerConfig;
import com.zingbug.httpserver.core.NettyHttpServer;
import com.zingbug.httpserver.core.handle.ControllerMapping;
import com.zingbug.httpserver.core.handle.ControllerReactor;
import com.zingbug.httpserver.core.handle.FilterReactor;
import com.zingbug.httpserver.model.ControllerRequest;
import com.zingbug.httpserver.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class HttpserverApplication {

    public static void start(Class clazz) throws InstantiationException, IllegalAccessException {
        log.info("start Http Server");
        HttpServerConfig.APPLICATION_CLASS = clazz;//公共应用类
        HttpServerConfig.init();

        Map<String, String> map = CommonUtil.scanController(clazz.getPackage().getName());//寻找所有控制器
        for (String url : map.keySet()) {
            ControllerReactor.CONTROLLER_LIST.add(new ControllerMapping(url, map.get(url)));
        }
        FilterReactor.FILTER_LIST = CommonUtil.scanFilter(clazz.getPackage().getName());//寻找所有过滤器
        NettyHttpServer server = new NettyHttpServer(HttpServerConfig.PORT);
        try {
            server.init();
        } catch (Exception e) {
            log.error("[sever init exception:]", e);
        }
        log.info("server close!");


    }

    public static void main(String[] args) throws IllegalAccessException, InstantiationException {
        HttpserverApplication.start(HttpserverApplication.class);

    }

}
