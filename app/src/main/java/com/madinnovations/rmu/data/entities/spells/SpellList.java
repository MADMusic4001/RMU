package com.madinnovations.rmu.data.entities.spells;

/**
 * Spell list attributes
 */
public class SpellList {
	private int id = -1;
	private String name;
	private String description;
	private Realm realm;
	private SpellListType spellListType;

	/**
	 * Checks the validity of the SpellList instance.
	 *
	 * @return true if the SpellList instance is valid, otherwise false.
	 */
	public boolean isValid() {
		return name != null && !name.isEmpty() && description != null && !description.isEmpty() && realm != null && spellListType != null;
	}

	@Override
	public String toString() {
		return "SpellList{" +
				"id=" + id +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", realm=" + realm +
				", spellListType=" + spellListType +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		SpellList spellList = (SpellList) o;

		return id == spellList.id;

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
	public Realm getRealm() {
		return realm;
	}
	public void setRealm(Realm realm) {
		this.realm = realm;
	}
	public SpellListType getSpellListType() {
		return spellListType;
	}
	public void setSpellListType(SpellListType spellListType) {
		this.spellListType = spellListType;
	}
}
