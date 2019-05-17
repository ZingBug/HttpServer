package com.zingbug.httpserver.model;

import com.zingbug.httpserver.common.constant.RequestConstants;
import com.zingbug.httpserver.common.em.PageMappingEnum;
import lombok.Data;

/**
 * Created by ZingBug on 2019/5/15.
 */
@Data
public class PageModel {

    private int code;

    private String pagePath;

    public PageModel()
    {
        this.code= RequestConstants.INDEX_CODE;
        this.pagePath= PageMappingEnum.getPath(code);
    }

    public PageModel(String pagePath)
    {
        this.code=RequestConstants.SUCCESS_CODE;
        this.pagePath=pagePath;
    }
}
