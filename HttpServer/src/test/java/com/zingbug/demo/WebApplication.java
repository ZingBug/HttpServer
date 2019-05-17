package com.zingbug.demo;

import com.zingbug.httpserver.HttpserverApplication;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ZingBug on 2019/5/16.
 */
@Slf4j
public class WebApplication {

    public static void main(String[] args) throws IllegalAccessException,InstantiationException
    {
        HttpserverApplication.start(WebApplication.class);
    }
}
