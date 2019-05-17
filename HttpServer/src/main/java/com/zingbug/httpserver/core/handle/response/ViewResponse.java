package com.zingbug.httpserver.core.handle.response;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by ZingBug on 2019/5/17.
 */
@Data
@AllArgsConstructor
public class ViewResponse extends BaseResponse {

    /**
     * 响应数据
     */
    int code;
    String path;
}
