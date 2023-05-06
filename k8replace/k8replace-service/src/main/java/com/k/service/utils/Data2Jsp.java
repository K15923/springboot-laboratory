package com.k.service.utils;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Data2Jsp {

    /**
     * @param @param  map
     * @param @return
     * @return Map<String, Integer>
     * @throws @author
     * @Description: 获得开始页及一页有多少条数据
     * @date 2015-7-17
     */
    public static Map<String, Integer> getFristAndPageSize(int page, int rows) {
        Map<String, Integer> result = new HashMap<String, Integer>();
        int page_size = rows;
        @SuppressWarnings("unused") int pages = 10;
        int first_page = 1;
        if (page > 1) {
            first_page = page;
        }
        first_page = (first_page - 1) * page_size;
        pages = page_size;
        result.put("first_page", first_page);
        result.put("page_size", page_size);
        return result;
    }

    /**
     * @param pager
     * @param pages
     * @return String
     * @Description: 分页json
     */
    @SuppressWarnings("deprecation")
    public static String Json2Jsp(Pager<?> pager) {
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());
        StringBuffer bf = new StringBuffer();
        int total = pager.getTotalCount();
        List<?> list = pager.getDatas();
        JSONArray json = JSONArray.fromObject(list, jsonConfig);
        bf.append("{\"total\":").append(total + ",");
        bf.append("\"rows\":");
        bf.append(json.toString());
        bf.append("}");
        return bf.toString();
    }
}
