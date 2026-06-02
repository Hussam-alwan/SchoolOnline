package com.bootcamp.onlineschool.dto;

/**
 * Optional filters for a dynamic Department search.
 * Any field left null is ignored, so callers can combine any subset of criteria.
 */
public class DepartmentSearchCriteria {

    private String code;
    private String location;
    private Double minBudget;
    private Double maxBudget;

    public DepartmentSearchCriteria() {
    }

    public DepartmentSearchCriteria(String code, String location, Double minBudget, Double maxBudget) {
        this.code = code;
        this.location = location;
        this.minBudget = minBudget;
        this.maxBudget = maxBudget;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getMinBudget() {
        return minBudget;
    }

    public void setMinBudget(Double minBudget) {
        this.minBudget = minBudget;
    }

    public Double getMaxBudget() {
        return maxBudget;
    }

    public void setMaxBudget(Double maxBudget) {
        this.maxBudget = maxBudget;
    }
}
