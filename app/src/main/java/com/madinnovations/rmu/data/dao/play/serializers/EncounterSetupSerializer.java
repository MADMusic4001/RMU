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
import com.madinnovations.rmu.data.dao.play.schemas.EncounterSetupEncounterInfoSchema;
import com.madinnovations.rmu.data.dao.play.schemas.EncounterSetupSchema;
import com.madinnovations.rmu.data.entities.Position;
import com.madinnovations.rmu.data.entities.campaign.Campaign;
import com.madinnovations.rmu.data.entities.character.Character;
import com.madinnovations.rmu.data.entities.combat.Action;
import com.madinnovations.rmu.data.entities.combat.RestrictedQuarters;
import com.madinnovations.rmu.data.entities.common.Pace;
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
		out.name(COLUMN_CAMPAIGN_ID).value(value.getCampaign().getId());
		out.name(COLUMN_CURRENT_INITIATIVE).value(value.getCurrentInitiative());
		out.name(COLUMN_ENCOUNTER_START_TIME).value(value.getEncounterStartTime().getTimeInMillis());
		out.name(EncounterSetupEncounterInfoSchema.TABLE_NAME).beginArray();
		for(Map.Entry<Character, EncounterRoundInfo> entry : value.getCharacterCombatInfo().entrySet()) {
			writeEncounterRoundInfo(out, entry.getValue());
		}
		for(Map.Entry<Creature, EncounterRoundInfo> entry : value.getEnemyCombatInfo().entrySet()) {
			writeEncounterRoundInfo(out, entry.getValue());
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
				case COLUMN_CAMPAIGN_ID:
					encounterSetup.setCampaign(new Campaign(in.nextInt()));
					break;
				case COLUMN_CURRENT_INITIATIVE:
					encounterSetup.setCurrentInitiative((short)in.nextInt());
					break;
				case COLUMN_ENCOUNTER_START_TIME:
					Calendar calendar = Calendar.getInstance();
					calendar.setTimeInMillis(in.nextLong());
					encounterSetup.setEncounterStartTime(calendar);
					break;
				case EncounterSetupEncounterInfoSchema.TABLE_NAME:
					readEncounterInfo(in, encounterSetup);
					break;
			}
		}
		in.endObject();
		return encounterSetup;
	}

	private void writeEncounterRoundInfo(JsonWriter out, EncounterRoundInfo encounterRoundInfo) throws IOException {
		out.beginObject();
		out.name(EncounterSetupEncounterInfoSchema.COLUMN_BEING_ID).value(encounterRoundInfo.getCombatant().getId());
		out.name(EncounterSetupEncounterInfoSchema.COLUMN_IS_CHARACTER)
				.value(encounterRoundInfo.getCombatant() instanceof Character);
		out.name(EncounterSetupEncounterInfoSchema.COLUMN_LOCATION_X).value(encounterRoundInfo.getPosition().getX());
		out.name(EncounterSetupEncounterInfoSchema.COLUMN_LOCATION_Y).value(encounterRoundInfo.getPosition().getY());
		out.name(EncounterSetupEncounterInfoSchema.COLUMN_DIRECTION).value(encounterRoundInfo.getPosition().getDirection());
		out.name(EncounterSetupEncounterInfoSchema.COLUMN_INITIATIVE_ROLL).value(encounterRoundInfo.getInitiativeRoll());
		out.name(EncounterSetupEncounterInfoSchema.COLUMN_BASE_INITIATIVE).value(encounterRoundInfo.getBaseInitiative());
		if(encounterRoundInfo.getSelectedOpponent() != null) {
			out.name(EncounterSetupEncounterInfoSchema.COLUMN_SELECTED_OPPONENT_ID)
					.value(encounterRoundInfo.getSelectedOpponent().getId());
			out.name(EncounterSetupEncounterInfoSchema.COLUMN_OPPONENT_IS_CHARACTER)
					.value(encounterRoundInfo.getSelectedOpponent() instanceof Character);
		}
		out.name(EncounterSetupEncounterInfoSchema.COLUMN_PARRY).value(encounterRoundInfo.getParry());
		out.name(EncounterSetupEncounterInfoSchema.COLUMN_ACTION_POINTS_REMAINING)
				.value(encounterRoundInfo.getActionPointsRemaining());
		out.name(EncounterSetupEncounterInfoSchema.COLUMN_ACTION_POINTS_SPENT).value(encounterRoundInfo.getActionPointsSpent());
		out.name(EncounterSetupEncounterInfoSchema.COLUMN_INSTANTANEOUS_USED).value(encounterRoundInfo.isInstantaneousUsed());
		out.name(EncounterSetupEncounterInfoSchema.COLUMN_IS_CONCENTRATING).value(encounterRoundInfo.isConcentrating());
		if(encounterRoundInfo.getPace() != null) {
			out.name(EncounterSetupEncounterInfoSchema.COLUMN_PACE).value(encounterRoundInfo.getPace().name());
			out.name(EncounterSetupEncounterInfoSchema.COLUMN_IS_MOVING_BACKWARDS)
					.value(encounterRoundInfo.isMovingBackwards());
		}
		if(encounterRoundInfo.getRestrictedQuarters() != null) {
			out.name(EncounterSetupEncounterInfoSchema.COLUMN_RESTRICTED_QUARTERS)
					.value(encounterRoundInfo.getRestrictedQuarters().name());
		}
		if(encounterRoundInfo.getActionInProgress() != null) {
			out.name(EncounterSetupEncounterInfoSchema.COLUMN_ACTION_IN_PROGRESS)
					.value(encounterRoundInfo.getActionInProgress().name());
		}
		out.endObject();
	}

	private void readEncounterInfo(JsonReader in, EncounterSetup encounterSetup) throws IOException {
		in.beginArray();
		while (in.hasNext()) {
			in.beginObject();
			int beingId = -1;
			boolean combatantIsCharacter = true;
			int opponentId = -1;
			boolean opponentIsCharacter = false;
			EncounterRoundInfo encounterRoundInfo = new EncounterRoundInfo();
			encounterRoundInfo.setPosition(new Position());
			while(in.hasNext()) {
				switch (in.nextName()) {
					case EncounterSetupEncounterInfoSchema.COLUMN_BEING_ID:
						beingId = in.nextInt();
						break;
					case EncounterSetupEncounterInfoSchema.COLUMN_IS_CHARACTER:
						combatantIsCharacter = in.nextBoolean();
						break;
					case EncounterSetupEncounterInfoSchema.COLUMN_LOCATION_X:
						encounterRoundInfo.getPosition().setX((float)in.nextDouble());
						break;
					case EncounterSetupEncounterInfoSchema.COLUMN_LOCATION_Y:
						encounterRoundInfo.getPosition().setY((float)in.nextDouble());
						break;
					case EncounterSetupEncounterInfoSchema.COLUMN_DIRECTION:
						encounterRoundInfo.getPosition().setDirection((float)in.nextDouble());
						break;
					case EncounterSetupEncounterInfoSchema.COLUMN_INITIATIVE_ROLL:
						encounterRoundInfo.setInitiativeRoll((short)in.nextInt());
						break;
					case EncounterSetupEncounterInfoSchema.COLUMN_BASE_INITIATIVE:
						encounterRoundInfo.setBaseInitiative((short)in.nextInt());
						break;
					case EncounterSetupEncounterInfoSchema.COLUMN_SELECTED_OPPONENT_ID:
						opponentId = in.nextInt();
						break;
					case EncounterSetupEncounterInfoSchema.COLUMN_OPPONENT_IS_CHARACTER:
						opponentIsCharacter = in.nextBoolean();
						break;
					case EncounterSetupEncounterInfoSchema.COLUMN_PARRY:
						encounterRoundInfo.setParry((short)in.nextInt());
						break;
					case EncounterSetupEncounterInfoSchema.COLUMN_ACTION_POINTS_REMAINING:
						encounterRoundInfo.setActionPointsRemaining((short)in.nextInt());
						break;
					case EncounterSetupEncounterInfoSchema.COLUMN_ACTION_POINTS_SPENT:
						encounterRoundInfo.setActionPointsSpent((short)in.nextInt());
						break;
					case EncounterSetupEncounterInfoSchema.COLUMN_INSTANTANEOUS_USED:
						encounterRoundInfo.setInstantaneousUsed(in.nextBoolean());
						break;
					case EncounterSetupEncounterInfoSchema.COLUMN_IS_CONCENTRATING:
						encounterRoundInfo.setConcentrating(in.nextBoolean());
						break;
					case EncounterSetupEncounterInfoSchema.COLUMN_PACE:
						encounterRoundInfo.setPace(Pace.valueOf(in.nextString()));
						break;
					case EncounterSetupEncounterInfoSchema.COLUMN_IS_MOVING_BACKWARDS:
						encounterRoundInfo.setMovingBackwards(in.nextBoolean());
						break;
					case EncounterSetupEncounterInfoSchema.COLUMN_RESTRICTED_QUARTERS:
						encounterRoundInfo.setRestrictedQuarters(RestrictedQuarters.valueOf(in.nextString()));
						break;
					case EncounterSetupEncounterInfoSchema.COLUMN_ACTION_IN_PROGRESS:
						encounterRoundInfo.setActionInProgress(Action.valueOf(in.nextString()));
						break;
				}
			}
			if(opponentIsCharacter) {
				encounterRoundInfo.setSelectedOpponent(new Character(opponentId));
			}
			else {
				encounterRoundInfo.setSelectedOpponent(new Creature(opponentId));
			}
			if(combatantIsCharacter) {
				encounterSetup.getCharacterCombatInfo().put(new Character(beingId), encounterRoundInfo);
			}
			else {
				encounterSetup.getEnemyCombatInfo().put(new Creature(beingId), encounterRoundInfo);
			}
			in.endObject();
		}
		in.endArray();
	}
}
