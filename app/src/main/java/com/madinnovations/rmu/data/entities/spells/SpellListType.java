package com.madinnovations.rmu.data.entities.spells;

/**
 * Spell list type attributes
 */
public class SpellListType {
	private int id = -1;
	private String name;
	private String description;

	@Override
	public String toString() {
		return "SpellListType{" +
				"id=" + id +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
