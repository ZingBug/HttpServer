package com.zingbug.httpserver.core.handle;

import com.zingbug.httpserver.common.BaseFilter;
import com.zingbug.httpserver.model.ControllerRequest;
import com.zingbug.httpserver.model.FilterModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZingBug on 2019/5/14.
 */
public class FilterReactor {

    private final static Logger logger = LoggerFactory.getLogger(FilterReactor.class);


    public static List<FilterModel> FILTER_LIST = new ArrayList<>();

    public static void preHandler(ControllerRequest controllerRequest) {
        for (FilterModel filterModel : FILTER_LIST) {
            BaseFilter filter = (BaseFilter) filterModel.getFilter();
            filter.beforeFilter(controllerRequest);
        }
    }

    public static void aftHandler(ControllerRequest controllerRequest) {
        for (FilterModel filterModel : FILTER_LIST) {
            BaseFilter filter = (BaseFilter) filterModel.getFilter();
            filter.afterFilter(controllerRequest);
        }
    }


}
