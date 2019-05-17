package com.zingbug.httpserver.core.handle.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Created by ZingBug on 2019/5/14.
 */
@Getter
@NoArgsConstructor//无参构造函数
@AllArgsConstructor//有参构造函数
public enum  ResponseEnum {
    NO_PATH_MAPPING(404,"路径搜索不到");

    private Integer code;
    private String des;
}
