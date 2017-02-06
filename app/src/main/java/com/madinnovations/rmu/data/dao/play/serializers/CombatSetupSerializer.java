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
package com.madinnovations.rmu.data.dao.play.serializers;

import android.graphics.Point;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.madinnovations.rmu.data.dao.play.schemas.CombatSetupCharacterCombatInfoSchema;
import com.madinnovations.rmu.data.dao.play.schemas.CombatSetupCreatureCombatInfoSchema;
import com.madinnovations.rmu.data.dao.play.schemas.CombatSetupSchema;
import com.madinnovations.rmu.data.entities.character.Character;
import com.madinnovations.rmu.data.entities.creature.Creature;
import com.madinnovations.rmu.data.entities.play.CombatInfo;
import com.madinnovations.rmu.data.entities.play.CombatSetup;

import java.io.IOException;
import java.util.Calendar;
import java.util.Map;

/**
 * Json serializer and deserializer for the {@link CombatSetup} entities
 */
public class CombatSetupSerializer extends TypeAdapter<CombatSetup> implements CombatSetupSchema {
	@Override
	public void write(JsonWriter out, CombatSetup value) throws IOException {
		out.beginObject();
		out.name(COLUMN_ID).value(value.getId());
		out.name(COLUMN_COMBAT_START_TIME).value(value.getCombatStartTime().getTimeInMillis());
		out.name(CombatSetupCharacterCombatInfoSchema.TABLE_NAME).beginArray();
		for(Map.Entry<Character, CombatInfo> entry : value.getCharacterCombatInfo().entrySet()) {
			out.beginObject();
			out.name(CombatSetupCharacterCombatInfoSchema.COLUMN_CHARACTER_ID).value(entry.getKey().getId());
			out.name(CombatSetupCharacterCombatInfoSchema.COLUMN_LOCATION_X).value(entry.getValue().getHexCoordinate().x);
			out.name(CombatSetupCharacterCombatInfoSchema.COLUMN_LOCATION_Y).value(entry.getValue().getHexCoordinate().y);
			out.name(CombatSetupCharacterCombatInfoSchema.COLUMN_BASE_INITIATIVE).value(entry.getValue().getBaseInitiative());
			out.name(CombatSetupCharacterCombatInfoSchema.COLUMN_ACTION_POINTS_REMAINING).value(
					entry.getValue().getActionPointsRemaining());
			out.endObject();
		}
		out.endArray();
		out.name(CombatSetupCreatureCombatInfoSchema.TABLE_NAME).beginArray();
		for(Map.Entry<Creature, CombatInfo> entry : value.getCreatureCombatInfo().entrySet()) {
			out.beginObject();
			out.name(CombatSetupCreatureCombatInfoSchema.COLUMN_CREATURE_ID).value(entry.getKey().getId());
			out.name(CombatSetupCreatureCombatInfoSchema.COLUMN_LOCATION_X).value(entry.getValue().getHexCoordinate().x);
			out.name(CombatSetupCreatureCombatInfoSchema.COLUMN_LOCATION_Y).value(entry.getValue().getHexCoordinate().y);
			out.name(CombatSetupCreatureCombatInfoSchema.COLUMN_BASE_INITIATIVE).value(entry.getValue().getBaseInitiative());
			out.name(CombatSetupCreatureCombatInfoSchema.COLUMN_ACTION_POINTS_REMAINING).value(
					entry.getValue().getActionPointsRemaining());
			out.endObject();
		}
		out.endArray();
		out.endObject().flush();
	}

	@Override
	public CombatSetup read(JsonReader in) throws IOException {
		CombatSetup combatSetup = new CombatSetup();
		in.beginObject();
		while (in.hasNext()) {
			switch (in.nextName()) {
				case COLUMN_ID:
					combatSetup.setId(in.nextInt());
					break;
				case COLUMN_COMBAT_START_TIME:
					Calendar calendar = Calendar.getInstance();
					calendar.setTimeInMillis(in.nextLong());
					combatSetup.setCombatStartTime(calendar);
					break;
				case CombatSetupCharacterCombatInfoSchema.TABLE_NAME:
					readCharacterLocations(in, combatSetup);
					break;
				case CombatSetupCreatureCombatInfoSchema.TABLE_NAME:
					readCreatureLocations(in, combatSetup);
					break;
			}
		}
		in.endObject();
		return combatSetup;
	}

	private void readCharacterLocations(JsonReader in, CombatSetup combatSetup) throws IOException {
		in.beginArray();
		while (in.hasNext()) {
			in.beginObject();
			Character character = new Character();
			CombatInfo combatInfo = new CombatInfo();
			combatInfo.setHexCoordinate(new Point());
			while(in.hasNext()) {
				switch (in.nextName()) {
					case CombatSetupCharacterCombatInfoSchema.COLUMN_CHARACTER_ID:
						character.setId(in.nextInt());
						break;
					case CombatSetupCharacterCombatInfoSchema.COLUMN_LOCATION_X:
						combatInfo.getHexCoordinate().x = in.nextInt();
						break;
					case CombatSetupCharacterCombatInfoSchema.COLUMN_LOCATION_Y:
						combatInfo.getHexCoordinate().y = in.nextInt();
						break;
					case CombatSetupCharacterCombatInfoSchema.COLUMN_BASE_INITIATIVE:
						combatInfo.setBaseInitiative((short)in.nextInt());
						break;
					case CombatSetupCharacterCombatInfoSchema.COLUMN_ACTION_POINTS_REMAINING:
						combatInfo.setActionPointsRemaining((short)in.nextInt());
						break;
				}
			}
			combatSetup.getCharacterCombatInfo().put(character, combatInfo);
			in.endObject();
		}
		in.endArray();
	}

	private void readCreatureLocations(JsonReader in, CombatSetup combatSetup) throws IOException {
		in.beginArray();
		while (in.hasNext()) {
			in.beginObject();
			Creature creature = new Creature();
			CombatInfo combatInfo = new CombatInfo();
			combatInfo.setHexCoordinate(new Point());
			while(in.hasNext()) {
				switch (in.nextName()) {
					case CombatSetupCreatureCombatInfoSchema.COLUMN_CREATURE_ID:
						creature.setId(in.nextInt());
						break;
					case CombatSetupCreatureCombatInfoSchema.COLUMN_LOCATION_X:
						combatInfo.getHexCoordinate().x = in.nextInt();
						break;
					case CombatSetupCreatureCombatInfoSchema.COLUMN_LOCATION_Y:
						combatInfo.getHexCoordinate().y = in.nextInt();
						break;
					case CombatSetupCreatureCombatInfoSchema.COLUMN_BASE_INITIATIVE:
						combatInfo.setBaseInitiative((short)in.nextInt());
						break;
					case CombatSetupCreatureCombatInfoSchema.COLUMN_ACTION_POINTS_REMAINING:
						combatInfo.setActionPointsRemaining((short)in.nextInt());
						break;
				}
			}
			combatSetup.getCreatureCombatInfo().put(creature, combatInfo);
			in.endObject();
		}
		in.endArray();
	}
}
