package com.madinnovations.rmu.data.entities.spells;

/**
 * Spell attributes
 */
public class Spell {
	private int id = -1;
	private SpellList spellList;
	private String name;
	private String description;

	@Override
	public String toString() {
		return "Spell{" +
				"id=" + id +
				", spellList=" + spellList +
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
	public SpellList getSpellList() {
		return spellList;
	}
	public void setSpellList(SpellList spellList) {
		this.spellList = spellList;
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
