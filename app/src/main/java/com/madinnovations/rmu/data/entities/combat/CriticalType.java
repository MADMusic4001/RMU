package com.madinnovations.rmu.data.entities.combat;

import java.util.List;

/**
 * Critical Type attributes
 */
public class CriticalType {
    private int id;
    private String name;
    private List<CriticalResult> results;

    // Getters and setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List<CriticalResult> getResults() {
        return results;
    }
    public void setResults(List<CriticalResult> results) {
        this.results = results;
    }
}
