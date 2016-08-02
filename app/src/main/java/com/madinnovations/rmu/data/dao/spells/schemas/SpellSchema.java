package com.madinnovations.rmu.data.dao.spells.schemas;

/**
 * Database schema data for the spells table
 */
public interface SpellSchema {
	public static final String TABLE_NAME = "spells";

	public static final String COLUMN_ID = "id";
	public static final String COLUMN_SPELL_LIST_ID = "spellListId";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_DESCRIPTION = "description";

	public static final String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME
			+ " ("
			+ COLUMN_ID + " INTEGER PRIMARY KEY, "
			+ COLUMN_SPELL_LIST_ID + " INTEGER NOT NULL, "
			+ COLUMN_NAME + " TEXT NOT NULL, "
			+ COLUMN_DESCRIPTION + " TEXT NOT NULL, "
			+ "FOREIGN KEY (" + COLUMN_SPELL_LIST_ID + ") REFERENCES " + SpellListSchema.TABLE_NAME + "(" + SpellListSchema.COLUMN_ID + ")"
			+ ")";

	public static final String[] COLUMNS = new String[] {COLUMN_ID, COLUMN_SPELL_LIST_ID, COLUMN_NAME, COLUMN_DESCRIPTION};
}
