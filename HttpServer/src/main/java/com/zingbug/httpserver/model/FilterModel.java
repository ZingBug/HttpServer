package com.zingbug.httpserver.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by ZingBug on 2019/5/14.
 */

@Data
@AllArgsConstructor
public class FilterModel {
    private int order;//优先级

    private String name;

    private Object filter;

}
