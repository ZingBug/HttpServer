package com.zingbug.httpserver.core.handle.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by ZingBug on 2019/5/14.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse {

    /**
     * 返回的响应类型
     */
    private int code;
    private Object data;
}
