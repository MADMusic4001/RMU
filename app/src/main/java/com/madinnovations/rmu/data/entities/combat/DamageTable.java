/**
 * Copyright (C) 2016 MadInnovations
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.madinnovations.rmu.data.entities.combat;

import android.util.SparseArray;

/**
 * Damage table attributes
 */
public class DamageTable {
	private static final int ROW_COUNT = 37;
	private static final short NON_BALL_HIGH = 175;
	private static final short NON_BALL_LOW = 65;
	private static final short BALL_HIGH = 125;
	private static final short BALL_LOW = 15;
    public static final String JSON_NAME = "DamageTables";
    private int id = -1;
    private String name = null;
    private boolean ballTable = false;
	private SparseArray<DamageResultRow> resultRows = new SparseArray<>(ROW_COUNT);

	/**
     * Creates a new DamageTable instance
     */
    public DamageTable() {
    }

	/**
	 * Creates a new DamageTable instance with the given id
	 *
	 * @param id  the id for the new instance
	 */
    public DamageTable(int id) {
        this.id = id;
    }

    /**
     * Checks the validity of the DamageTable instance.
     *
     * @return true if the DamageTable instance is valid, otherwise false.
     */
    public boolean isValid() {
        return name != null && !name.isEmpty();
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DamageTable that = (DamageTable) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    public void addMissingRows() {
		short start;
		short end;
		short offset = 0;
		SparseArray<DamageResultRow> newArray = new SparseArray<>(ROW_COUNT);

		if(ballTable) {
			if(resultRows.keyAt(0) == NON_BALL_LOW + 2) {
				start = NON_BALL_HIGH;
				end = NON_BALL_LOW;
				offset = BALL_HIGH - NON_BALL_HIGH;
			}
			else {
				start = BALL_HIGH;
				end = BALL_LOW;
			}
		}
		else {
			if(resultRows.keyAt(0) == BALL_LOW + 2) {
				start = BALL_HIGH;
				end = BALL_LOW;
				offset = NON_BALL_HIGH - BALL_HIGH;
			}
			else {
				start = NON_BALL_HIGH;
				end = NON_BALL_LOW;
			}
		}
        for(short rollValue = start; rollValue > end; rollValue -= 3) {
            DamageResultRow row = resultRows.get(rollValue);
			if(row == null) {
				row = new DamageResultRow();
				row.setDamageTable(this);
				row.setRangeLowValue((short)(rollValue + offset - 2));
				row.setRangeHighValue((short)(rollValue + offset));
			}
			newArray.put(rollValue + offset, row);
        }
		resultRows = newArray;
    }

	public void convertRows() {
		short highValue;
		short offset;
		SparseArray<DamageResultRow> convertedArray = new SparseArray<>(ROW_COUNT);

		if(resultRows.keyAt(0) == NON_BALL_LOW + 2 && ballTable) {
			offset = BALL_HIGH - NON_BALL_HIGH;
		}
		else if(resultRows.keyAt(0) == BALL_LOW + 2 && !ballTable) {
			offset = NON_BALL_HIGH - BALL_HIGH;
		}
		else {
			return;
		}
		for(int i = 0; i < resultRows.size(); i++) {
			DamageResultRow damageResultRow = resultRows.valueAt(i);
			damageResultRow.setRangeLowValue((short)(damageResultRow.getRangeLowValue() + offset));
			highValue = (short)(damageResultRow.getRangeHighValue() + offset);
			damageResultRow.setRangeHighValue(highValue);
			convertedArray.put(highValue, damageResultRow);
		}
		resultRows = convertedArray;
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
	public boolean isBallTable() {
		return ballTable;
	}
	public void setBallTable(boolean ballTable) {
		this.ballTable = ballTable;
	}
	public SparseArray<DamageResultRow> getResultRows() {
        return resultRows;
    }
}
