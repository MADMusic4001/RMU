package com.madinnovations.rmu.data.entities.object;

/**
 * Item attributes
 */
public class Item {
    private int id = -1;
    private String name;
    private String description;
    private int weight;

    /**
     * Checks the validity of the Item instance.
     *
     * @return true if the Item instance is valid, otherwise false.
     */
    public boolean isValid() {
        return name != null && !name.isEmpty() && description != null && !description.isEmpty();
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", weight=" + weight +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        return id == item.id;

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
    public int getWeight() {
        return weight;
    }
    public void setWeight(int weight) {
        this.weight = weight;
    }
}
