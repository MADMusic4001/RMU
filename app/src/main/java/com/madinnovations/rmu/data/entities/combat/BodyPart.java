package com.madinnovations.rmu.data.entities.combat;

/**
 * Body part attributes
 */
public class BodyPart {
    private int id = -1;
    private String name = null;
    private String description = null;

    /**
     * Checks the validity of the BodyPart instance.
     *
     * @return true if the BodyPart instance is valid, otherwise false.
     */
    public boolean isValid() {
        return name != null && !name.isEmpty() && description != null && !description.isEmpty();
    }

    @Override
    public String toString() {
        return "BodyPart{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BodyPart bodyPart = (BodyPart) o;

        return id == bodyPart.id;

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
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
