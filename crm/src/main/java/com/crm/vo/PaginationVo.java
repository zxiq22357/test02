package com.crm.vo;

import java.util.List;

/**
 * 作者：周熙强
 */
public class PaginationVo<T> {
    private int count;
    private List<T> dataList;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }
}
