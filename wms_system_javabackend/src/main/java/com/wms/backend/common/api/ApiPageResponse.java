package com.wms.backend.common.api;

import java.util.List;

public class ApiPageResponse<T> {

    private List<T> records;
    private long total;

    public ApiPageResponse() {
    }

    public ApiPageResponse(List<T> records, long total) {
        this.records = records;
        this.total = total;
    }

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
