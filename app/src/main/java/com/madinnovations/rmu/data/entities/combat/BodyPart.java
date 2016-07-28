package com.madinnovations.rmu.data.entities.combat;

/**
 * Body part attributes
 */
public class BodyPart {
    private int id = -1;
    private String name;
    private String description;

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
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
