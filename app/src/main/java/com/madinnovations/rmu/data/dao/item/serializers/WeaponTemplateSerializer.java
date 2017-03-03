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
package com.madinnovations.rmu.data.dao.item.serializers;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.madinnovations.rmu.data.dao.item.schemas.WeaponTemplateSchema;
import com.madinnovations.rmu.data.entities.combat.DamageTable;
import com.madinnovations.rmu.data.entities.common.Skill;
import com.madinnovations.rmu.data.entities.object.WeaponTemplate;

import java.io.IOException;

/**
 * Json serializer and deserializer for the {@link WeaponTemplate} entities
 */
public class WeaponTemplateSerializer extends TypeAdapter<WeaponTemplate> implements WeaponTemplateSchema {
	@Override
	public void write(JsonWriter out, WeaponTemplate value) throws IOException {
		out.beginObject();
		out.name(COLUMN_ID).value(value.getId());
		out.name(COLUMN_SKILL_ID).value(value.getCombatSkill().getId());
		out.name(COLUMN_DAMAGE_TABLE_ID).value(value.getDamageTable().getId());
		out.name(COLUMN_BRACEABLE).value(value.isBraceable());
		out.endObject().flush();
	}

	@Override
	public WeaponTemplate read(JsonReader in) throws IOException {
		WeaponTemplate weaponTemplate = new WeaponTemplate();
		in.beginObject();
		while (in.hasNext()) {
			switch (in.nextName()) {
				case COLUMN_ID:
					weaponTemplate.setId(in.nextInt());
					break;
				case COLUMN_SKILL_ID:
					weaponTemplate.setCombatSkill(new Skill(in.nextInt()));
					break;
				case COLUMN_DAMAGE_TABLE_ID:
					weaponTemplate.setDamageTable(new DamageTable(in.nextInt()));
					break;
				case COLUMN_BRACEABLE:
					weaponTemplate.setBraceable(in.nextBoolean());
					break;
			}
		}
		in.endObject();
		return weaponTemplate;
	}
}
