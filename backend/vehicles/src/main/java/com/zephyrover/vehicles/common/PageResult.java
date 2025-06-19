package com.zephyrover.vehicles.common;

import java.util.List;

/**
 * 分页结果包装类
 */
public class PageResult<T> {

    private long total;
    private List<T> list;
    private long current;
    private long size;

    public PageResult() {
    }

    public PageResult(long total, List<T> list, long current, long size) {
        this.total = total;
        this.list = list;
        this.current = current;
        this.size = size;
    }

    // Getters and Setters
    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public long getCurrent() {
        return current;
    }

    public void setCurrent(long current) {
        this.current = current;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
