package com.zingbug.httpserver.model;

import lombok.Setter;

import java.util.Map;

/**
 * Created by ZingBug on 2019/5/15.
 */
@Setter
public class ControllerRequest {

    private Map<String,Object> params;
    private Map<String,String> header;

    public Object getParameter(String name)
    {
        return params.get(name);
    }

    public Object getHeader(String name)
    {
        return header.get(name);
    }
}
