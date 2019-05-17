package com.zingbug.httpserver.common.constant;

/**
 * Created by ZingBug on 2019/5/14.
 */
public class RequestConstants {

    public static String FROM_PARAMS_TYPE = "x-www-form-urlencoded";
    public static String JSON_PARAMS_TYPE = "application/json";
    public static String CONTENT_TYPE = "Content-Type";
    public static String PAGE_NOT_FOUND = "page-not-found";
    public static final int PAGE_NOT_FOUND_CODE = 404;
    public static final int INDEX_CODE = 200;
    public static final int SUCCESS_CODE = 202;
    //自定义初始化页面
    public static final int MY_DEF_CODE = 203;
    public static final int UNKOWN_EXCEPTION_CODE = 500;
    public static final String HTML_TYPE = ".html";
    public static final String JS_TYPE = ".js";
    public static final String CSS_TYPE = ".css";
}
