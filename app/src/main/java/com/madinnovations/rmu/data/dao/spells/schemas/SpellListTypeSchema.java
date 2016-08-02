package com.madinnovations.rmu.data.dao.spells.schemas;

/**
 * Database schema data for the spell_list_types table
 */
public interface SpellListTypeSchema {
	public static final String TABLE_NAME = "spell_list_types";

	public static final String COLUMN_ID = "id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_DESCRIPTION = "description";

	public static final String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME
			+ " ("
			+ COLUMN_ID + " INTEGER PRIMARY KEY, "
			+ COLUMN_NAME + " TEXT NOT NULL, "
			+ COLUMN_DESCRIPTION + " TEXT NOT NULL"
			+ ")";

	public static final String[] COLUMNS = new String[] {COLUMN_ID, COLUMN_NAME, COLUMN_DESCRIPTION};
}
