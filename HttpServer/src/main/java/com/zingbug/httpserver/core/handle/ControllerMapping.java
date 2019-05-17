package com.zingbug.httpserver.core.handle;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by ZingBug on 2019/5/15.
 */
@Data
@AllArgsConstructor
public class ControllerMapping {

    private String url;

    private String clazz;
}
