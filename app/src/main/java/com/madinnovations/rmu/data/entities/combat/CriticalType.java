package com.madinnovations.rmu.data.entities.combat;

import java.util.List;

/**
 * Critical Type attributes
 */
public class CriticalType {
    private int id = -1;
    private String name;
    private List<CriticalResult> results;

    /**
     * Checks the validity of the CriticalType instance.
     *
     * @return true if the CriticalType instance is valid, otherwise false.
     */
    public boolean isValid() {
        return name != null && !name.isEmpty();
    }

    @Override
    public String toString() {
        return "CriticalType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", results=" + results +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CriticalType that = (CriticalType) o;

        return id == that.id;

    }

    @Override
    public int hashCode() {
        return id;
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
    public List<CriticalResult> getResults() {
        return results;
    }
    public void setResults(List<CriticalResult> results) {
        this.results = results;
    }
}
