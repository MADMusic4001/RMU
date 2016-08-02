package com.madinnovations.rmu.data.dao.spells.schemas;

/**
 * Database schema data for the spell_lists table
 */
public interface SpellListSchema {
	public static final String TABLE_NAME = "spell_lists";

	public static final String COLUMN_ID = "id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_REALM_ID = "realmId";
	public static final String COLUMN_TYPE_ID = "typeId";

	public static final String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME
			+ " ("
			+ COLUMN_ID + " INTEGER PRIMARY KEY, "
			+ COLUMN_NAME + " TEXT NOT NULL, "
			+ COLUMN_DESCRIPTION + " TEXT NOT NULL, "
			+ COLUMN_REALM_ID + " INTEGER NOT NULL, "
			+ COLUMN_TYPE_ID + " INTEGER NOT NULL, "
			+ "FOREIGN KEY (" + COLUMN_REALM_ID + ") REFERENCES " + RealmSchema.TABLE_NAME + "(" + RealmSchema.COLUMN_ID + ")"
			+ "FOREIGN KEY (" + COLUMN_TYPE_ID + ") REFERENCES " + SpellListTypeSchema.TABLE_NAME + "(" + SpellListTypeSchema.COLUMN_ID + ")"
			+ ")";

	public static final String[] COLUMNS = new String[] {COLUMN_ID, COLUMN_NAME, COLUMN_DESCRIPTION, COLUMN_REALM_ID, COLUMN_TYPE_ID};
}
