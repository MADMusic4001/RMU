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

import java.util.ArrayList;
import java.util.Collection;

/**
 * Damage table attributes
 */
public class DamageTable {
    public static final String JSON_NAME = "DamageTables";
    private int id = -1;
    private String name = null;
    private boolean ballTable = false;
    private Collection<DamageResultRow> resultRows = new ArrayList<>();

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

    public void initRows() {
		short start;
		short end;
        resultRows = new ArrayList<>(37);

		if(ballTable) {
			start = 125;
			end = 15;
		}
		else {
			start = 175;
			end = 65;
		}
        for(short i = start; i > end; i -= 3) {
            DamageResultRow row = new DamageResultRow();
            row.setDamageTable(this);
            row.setRangeLowValue((short)(i-2));
            row.setRangeHighValue(i);
            resultRows.add(row);
        }
    }

	private void convertRows() {
		short offset;
		resultRows = new ArrayList<>(37);

		if(ballTable) {
			offset = 50;
		}
		else {
			offset = -50;
		}
		for(DamageResultRow damageResultRow : resultRows) {
			damageResultRow.setRangeLowValue((short)(damageResultRow.getRangeLowValue() + offset));
			damageResultRow.setRangeHighValue((short)(damageResultRow.getRangeHighValue() + offset));
		}
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
		if(this.ballTable != ballTable) {
			convertRows();
		}
		this.ballTable = ballTable;
	}
	public Collection<DamageResultRow> getResultRows() {
        return resultRows;
    }
    public void setResultRows(Collection<DamageResultRow> resultRows) {
        this.resultRows = resultRows;
    }
}
