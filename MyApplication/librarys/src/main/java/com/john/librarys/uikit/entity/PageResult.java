package com.john.librarys.uikit.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页数据
 */
public class PageResult<T> {
    Integer pageNo = 0;
    Integer totalPages = 0;
    Integer totalCount = 0;
    List<T> result = new ArrayList();

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public List<T> getResult() {
        return result;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }
}
