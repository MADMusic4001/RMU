/**
 * Copyright (C) 2017 MadInnovations
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.madinnovations.rmu.data.dao.item.serializers;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.madinnovations.rmu.data.dao.item.schemas.ItemTemplateSchema;
import com.madinnovations.rmu.data.entities.common.ManeuverDifficulty;
import com.madinnovations.rmu.data.entities.object.ItemTemplate;
import com.madinnovations.rmu.data.entities.object.Slot;

import java.io.IOException;

/**
 * Json serializer and deserializer for the {@link ItemTemplate} entities
 */
public class ItemTemplateSerializer extends TypeAdapter<ItemTemplate> implements ItemTemplateSchema {
	@Override
	public void write(JsonWriter out, ItemTemplate value) throws IOException {
		out.beginObject();
		out.name(COLUMN_ID).value(value.getId());
		out.name(COLUMN_NAME).value(value.getName());
		out.name(COLUMN_WEIGHT).value(value.getWeight());
		out.name(COLUMN_BASE_COST).value(value.getBaseCost());
		out.name(COLUMN_STRENGTH).value(value.getStrength());
		out.name(COLUMN_CONSTRUCTION_TIME).value(value.getConstructionTime());
		if(value.getManeuverDifficulty() != null) {
			out.name(COLUMN_MANEUVER_DIFFICULTY).value(value.getManeuverDifficulty().name());
		}
		if(value.getNotes() != null) {
			out.name(COLUMN_NOTES).value(value.getNotes());
		}
		if(value.getPrimarySlot() != null) {
			out.name(COLUMN_PRIMARY_SLOT).value(value.getPrimarySlot().name());
		}
		if(value.getSecondarySlot() != null) {
			out.name(COLUMN_SECONDARY_SLOT).value(value.getSecondarySlot().name());
		}

		out.endObject().flush();
	}

	@Override
	public ItemTemplate read(JsonReader in) throws IOException {
		ItemTemplate itemTemplate = new ItemTemplate();
		in.beginObject();
		while (in.hasNext()) {
			switch (in.nextName()) {
				case COLUMN_ID:
					itemTemplate.setId(in.nextInt());
					break;
				case COLUMN_NAME:
					itemTemplate.setName(in.nextString());
					break;
				case COLUMN_WEIGHT:
					itemTemplate.setWeight((float)in.nextDouble());
					break;
				case COLUMN_BASE_COST:
					itemTemplate.setBaseCost((float)in.nextDouble());
					break;
				case COLUMN_STRENGTH:
					itemTemplate.setStrength((short)in.nextInt());
					break;
				case COLUMN_CONSTRUCTION_TIME:
					itemTemplate.setConstructionTime((float)in.nextDouble());
					break;
				case COLUMN_MANEUVER_DIFFICULTY:
					itemTemplate.setManeuverDifficulty(ManeuverDifficulty.valueOf(in.nextString()));
					break;
				case COLUMN_NOTES:
					itemTemplate.setNotes(in.nextString());
					break;
				case COLUMN_PRIMARY_SLOT:
					itemTemplate.setPrimarySlot(Slot.valueOf(in.nextString()));
					break;
				case COLUMN_SECONDARY_SLOT:
					itemTemplate.setSecondarySlot(Slot.valueOf(in.nextString()));
					break;
			}
		}
		in.endObject();
		return itemTemplate;
	}
}
