package com.parovi.zadruga.models.requestModels;

import java.util.List;

import retrofit2.http.Query;

public class FilterAndSortRequest {
    private Integer compensationMin;
    private Integer compensationMax;
    private List<Integer> filterTagIds;

    public FilterAndSortRequest(Integer compensationMin, List<Integer> filterTagIds) {
        this.compensationMin = compensationMin;
        this.filterTagIds = filterTagIds;
    }

    public Integer getCompensationMin() {
        return compensationMin;
    }

    public void setCompensationMin(Integer compensationMin) {
        this.compensationMin = compensationMin;
    }

    public Integer getCompensationMax() {
        return compensationMax;
    }

    public void setCompensationMax(Integer compensationMax) {
        this.compensationMax = compensationMax;
    }

    public List<Integer> getFilterTagIds() {
        return filterTagIds;
    }

    public void setFilterTagIds(List<Integer> filterTagIds) {
        this.filterTagIds = filterTagIds;
    }
}
