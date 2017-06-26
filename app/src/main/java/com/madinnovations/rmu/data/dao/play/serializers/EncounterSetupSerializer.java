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
package com.madinnovations.rmu.data.dao.play.serializers;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.madinnovations.rmu.data.dao.play.schemas.EncounterSetupCharacterEncounterInfoSchema;
import com.madinnovations.rmu.data.dao.play.schemas.EncounterSetupCreatureEncounterInfoSchema;
import com.madinnovations.rmu.data.dao.play.schemas.EncounterSetupSchema;
import com.madinnovations.rmu.data.entities.Position;
import com.madinnovations.rmu.data.entities.character.Character;
import com.madinnovations.rmu.data.entities.creature.Creature;
import com.madinnovations.rmu.data.entities.play.EncounterRoundInfo;
import com.madinnovations.rmu.data.entities.play.EncounterSetup;

import java.io.IOException;
import java.util.Calendar;
import java.util.Map;

/**
 * Json serializer and deserializer for the {@link EncounterSetup} entities
 */
public class EncounterSetupSerializer extends TypeAdapter<EncounterSetup> implements EncounterSetupSchema {
	@Override
	public void write(JsonWriter out, EncounterSetup value) throws IOException {
		out.beginObject();
		out.name(COLUMN_ID).value(value.getId());
		out.name(COLUMN_ENCOUNTER_START_TIME).value(value.getEncounterStartTime().getTimeInMillis());
		out.name(EncounterSetupCharacterEncounterInfoSchema.TABLE_NAME).beginArray();
		for(Map.Entry<Character, EncounterRoundInfo> entry : value.getCharacterCombatInfo().entrySet()) {
			out.beginObject();
			out.name(EncounterSetupCharacterEncounterInfoSchema.COLUMN_CHARACTER_ID).value(entry.getKey().getId());
			out.name(EncounterSetupCharacterEncounterInfoSchema.COLUMN_LOCATION_X).value(entry.getValue().getPosition().getX());
			out.name(EncounterSetupCharacterEncounterInfoSchema.COLUMN_LOCATION_Y).value(entry.getValue().getPosition().getY());
			out.name(EncounterSetupCharacterEncounterInfoSchema.COLUMN_DIRECTION).value(
					entry.getValue().getPosition().getDirection());
			out.name(EncounterSetupCharacterEncounterInfoSchema.COLUMN_BASE_INITIATIVE).value(entry.getValue().getInitiativeRoll());
			out.name(EncounterSetupCharacterEncounterInfoSchema.COLUMN_ACTION_POINTS_REMAINING).value(
					entry.getValue().getActionPointsRemaining());
			out.endObject();
		}
		out.endArray();
		out.name(EncounterSetupCreatureEncounterInfoSchema.TABLE_NAME).beginArray();
		for(Map.Entry<Creature, EncounterRoundInfo> entry : value.getEnemyCombatInfo().entrySet()) {
			out.beginObject();
			out.name(EncounterSetupCreatureEncounterInfoSchema.COLUMN_CREATURE_ID).value(entry.getKey().getId());
			out.name(EncounterSetupCreatureEncounterInfoSchema.COLUMN_LOCATION_X).value(entry.getValue().getPosition().getX());
			out.name(EncounterSetupCreatureEncounterInfoSchema.COLUMN_LOCATION_Y).value(entry.getValue().getPosition().getY());
			out.name(EncounterSetupCreatureEncounterInfoSchema.COLUMN_DIRECTION).value(
					entry.getValue().getPosition().getDirection());
			out.name(EncounterSetupCreatureEncounterInfoSchema.COLUMN_BASE_INITIATIVE).value(entry.getValue().getInitiativeRoll());
			out.name(EncounterSetupCreatureEncounterInfoSchema.COLUMN_ACTION_POINTS_REMAINING).value(
					entry.getValue().getActionPointsRemaining());
			out.endObject();
		}
		out.endArray();
		out.endObject().flush();
	}

	@Override
	public EncounterSetup read(JsonReader in) throws IOException {
		EncounterSetup encounterSetup = new EncounterSetup();
		in.beginObject();
		while (in.hasNext()) {
			switch (in.nextName()) {
				case COLUMN_ID:
					encounterSetup.setId(in.nextInt());
					break;
				case COLUMN_ENCOUNTER_START_TIME:
					Calendar calendar = Calendar.getInstance();
					calendar.setTimeInMillis(in.nextLong());
					encounterSetup.setEncounterStartTime(calendar);
					break;
				case EncounterSetupCharacterEncounterInfoSchema.TABLE_NAME:
					readCharacterLocations(in, encounterSetup);
					break;
				case EncounterSetupCreatureEncounterInfoSchema.TABLE_NAME:
					readCreatureLocations(in, encounterSetup);
					break;
			}
		}
		in.endObject();
		return encounterSetup;
	}

	private void readCharacterLocations(JsonReader in, EncounterSetup encounterSetup) throws IOException {
		in.beginArray();
		while (in.hasNext()) {
			in.beginObject();
			Character character = new Character();
			EncounterRoundInfo encounterRoundInfo = new EncounterRoundInfo();
			float x = 0, y = 0, direction = 0;
			while(in.hasNext()) {
				switch (in.nextName()) {
					case EncounterSetupCharacterEncounterInfoSchema.COLUMN_CHARACTER_ID:
						character.setId(in.nextInt());
						break;
					case EncounterSetupCharacterEncounterInfoSchema.COLUMN_LOCATION_X:
						x = (float)in.nextDouble();
						break;
					case EncounterSetupCharacterEncounterInfoSchema.COLUMN_LOCATION_Y:
						y = (float)in.nextDouble();
						break;
					case EncounterSetupCharacterEncounterInfoSchema.COLUMN_DIRECTION:
						direction = (float)in.nextDouble();
						break;
					case EncounterSetupCharacterEncounterInfoSchema.COLUMN_BASE_INITIATIVE:
						encounterRoundInfo.setInitiativeRoll((short)in.nextInt());
						break;
					case EncounterSetupCharacterEncounterInfoSchema.COLUMN_ACTION_POINTS_REMAINING:
						encounterRoundInfo.setActionPointsRemaining((short)in.nextInt());
						break;
				}
			}
			encounterRoundInfo.setPosition(new Position(x, y, direction));
			encounterSetup.getCharacterCombatInfo().put(character, encounterRoundInfo);
			in.endObject();
		}
		in.endArray();
	}

	private void readCreatureLocations(JsonReader in, EncounterSetup encounterSetup) throws IOException {
		in.beginArray();
		while (in.hasNext()) {
			in.beginObject();
			Creature creature = new Creature();
			EncounterRoundInfo encounterRoundInfo = new EncounterRoundInfo();
			float x = 0, y = 0, direction = 0;
			while(in.hasNext()) {
				switch (in.nextName()) {
					case EncounterSetupCreatureEncounterInfoSchema.COLUMN_CREATURE_ID:
						creature.setId(in.nextInt());
						break;
					case EncounterSetupCreatureEncounterInfoSchema.COLUMN_LOCATION_X:
						x = (float)in.nextDouble();
						break;
					case EncounterSetupCreatureEncounterInfoSchema.COLUMN_LOCATION_Y:
						y = (float)in.nextDouble();
						break;
					case EncounterSetupCreatureEncounterInfoSchema.COLUMN_DIRECTION:
						direction = (float)in.nextDouble();
						break;
					case EncounterSetupCreatureEncounterInfoSchema.COLUMN_BASE_INITIATIVE:
						encounterRoundInfo.setInitiativeRoll((short)in.nextInt());
						break;
					case EncounterSetupCreatureEncounterInfoSchema.COLUMN_ACTION_POINTS_REMAINING:
						encounterRoundInfo.setActionPointsRemaining((short)in.nextInt());
						break;
				}
			}
			encounterRoundInfo.setPosition(new Position(x, y, direction));
			encounterSetup.getEnemyCombatInfo().put(creature, encounterRoundInfo);
			in.endObject();
		}
		in.endArray();
	}
}
