package com.zingbug.httpserver.common.em;


import com.zingbug.httpserver.common.config.HttpServerConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by ZingBug on 2019/5/14.
 */
@Getter
@AllArgsConstructor
public enum PageMappingEnum {
    INDEX_PAGE(200, HttpServerConfig.INDEX_PAGE),
    NOT_FOUND_PAGE(404, HttpServerConfig.NOT_FOUND_PAGE),
    UNKOWN_EXCEPTION_PAGE(500, HttpServerConfig.UNKOWN_EXCEPTION_PAGE);

    private int code;

    private String path;

    public static String getPath(int code) {
        for (PageMappingEnum pageMappingEnum : PageMappingEnum.values()) {
            if (pageMappingEnum.getCode() == code) {
                return pageMappingEnum.getPath();
            }
        }
        return null;
    }
}
