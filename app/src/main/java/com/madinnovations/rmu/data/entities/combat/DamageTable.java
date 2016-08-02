package com.madinnovations.rmu.data.entities.combat;

import java.util.List;

/**
 * Damage table attributes
 */
public class DamageTable {
    private int id = -1;
    private String name;
    private List<DamageResult> results;

    @Override
    public String toString() {
        return "DamageTable{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", results=" + results +
                '}';
    }

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
    public List<DamageResult> getResults() {
        return results;
    }
    public void setResults(List<DamageResult> results) {
        this.results = results;
    }
}
