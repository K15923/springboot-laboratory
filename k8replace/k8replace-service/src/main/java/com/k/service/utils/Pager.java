package com.k.service.utils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * @Description: 分页
 * @Version:
 */
@ApiModel("H5模块-分页出参")
public class Pager<T> implements Serializable {


    @ApiModelProperty(value = "列表", dataType = "List")
    private List<T> datas;

    private int pageSize = getDefaultPageSize();

    private int pageNumber = 1;

    private int totalCount;

    @SuppressWarnings("unchecked")
    public List<T> getDatas() {
        return datas == null ? Collections.EMPTY_LIST : datas;
    }

    public void setDatas(List<T> datas) {
        this.datas = datas;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize < 1 ? getDefaultPageSize() : pageSize;
    }

    public static int getDefaultPageSize() {
        return 20;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber < 1 ? 1 : pageNumber;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getFirstResult() {
        return (pageNumber - 1) * pageSize;
    }

}
