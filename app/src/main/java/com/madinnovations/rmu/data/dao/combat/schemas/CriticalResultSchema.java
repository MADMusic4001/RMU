/*
  Copyright (C) 2016 MadInnovations
  <p/>
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  <p/>
  http://www.apache.org/licenses/LICENSE-2.0
  <p/>
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package com.madinnovations.rmu.data.dao.combat.schemas;

/**
 * Database schema data for the body_parts table
 */
public interface CriticalResultSchema {
    String TABLE_NAME = "critical_results";

    String COLUMN_ID = "id";
    String COLUMN_SEVERITY_CODE = "severityCode";
    String COLUMN_RESULT_TEXT = "resultText";
    String COLUMN_MIN_ROLL = "minRoll";
    String COLUMN_MAX_ROLL = "maxRoll";
    String COLUMN_BODY_LOCATION = "bodyLocation";
    String COLUMN_HITS = "hits";
    String COLUMN_BLEEDING = "bleeding";
    String COLUMN_FATIGUE = "fatigue";
    String COLUMN_BREAKAGE = "breakage";
    String COLUMN_INJURY = "injury";
    String COLUMN_DAZED = "dazed";
    String COLUMN_STUNNED = "stunned";
    String COLUMN_NO_PARRY = "noParry";
    String COLUMN_STAGGERED = "staggered";
    String COLUMN_KNOCK_BACK = "knockBack";
    String COLUMN_PRONE = "prone";
    String COLUMN_GRAPPLED = "grappled";
    String COLUMN_DEATH = "death";
    String COLUMN_CRITICAL_TYPE = "criticalType";

    String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_NAME
            + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY, "
            + COLUMN_SEVERITY_CODE + " TEXT NOT NULL, "
            + COLUMN_RESULT_TEXT  + " TEXT NOT NULL, "
            + COLUMN_MIN_ROLL + " INTEGER NOT NULL, "
            + COLUMN_MAX_ROLL + " INTEGER NOT NULL, "
            + COLUMN_BODY_LOCATION + " TEXT NOT NULL, "
            + COLUMN_HITS + " INTEGER NOT NULL, "
            + COLUMN_BLEEDING + " INTEGER NOT NULL, "
            + COLUMN_FATIGUE + " INTEGER NOT NULL, "
            + COLUMN_BREAKAGE + " INTEGER, "
            + COLUMN_INJURY + " INTEGER NOT NULL, "
            + COLUMN_DAZED + " INTEGER NOT NULL, "
            + COLUMN_STUNNED + " INTEGER NOT NULL, "
            + COLUMN_NO_PARRY + " INTEGER NOT NULL, "
            + COLUMN_STAGGERED + " INTEGER NOT NULL, "
            + COLUMN_KNOCK_BACK + " INTEGER NOT NULL, "
            + COLUMN_PRONE + " INTEGER NOT NULL, "
            + COLUMN_GRAPPLED + " INTEGER NOT NULL, "
			+ COLUMN_DEATH + " INTEGER, "
            + COLUMN_CRITICAL_TYPE + " TEXT NOT NULL)";

    String[] COLUMNS = new String[] { COLUMN_ID, COLUMN_SEVERITY_CODE, COLUMN_RESULT_TEXT, COLUMN_MIN_ROLL, COLUMN_MAX_ROLL,
            COLUMN_BODY_LOCATION, COLUMN_HITS, COLUMN_BLEEDING, COLUMN_FATIGUE, COLUMN_BREAKAGE, COLUMN_INJURY, COLUMN_DAZED,
            COLUMN_STUNNED, COLUMN_NO_PARRY, COLUMN_STAGGERED, COLUMN_KNOCK_BACK, COLUMN_PRONE, COLUMN_GRAPPLED, COLUMN_DEATH,
            COLUMN_CRITICAL_TYPE};
}
