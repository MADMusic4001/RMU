package com.madinnovations.rmu.data.entities.spells;

/**
 * Spell attributes
 */
public class Spell {
	private int id = -1;
	private SpellList spellList;
	private String name;
	private String description;

	/**
	 * Checks the validity of the Spell instance.
	 *
	 * @return true if the Spell instance is valid, otherwise false.
	 */
	public boolean isValid() {
		return name != null && !name.isEmpty() && description != null && !description.isEmpty() && spellList != null;
	}

	@Override
	public String toString() {
		return "Spell{" +
				"id=" + id +
				", spellList=" + spellList +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Spell spell = (Spell) o;

		return id == spell.id;

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
