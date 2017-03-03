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
import com.madinnovations.rmu.data.dao.item.schemas.ItemSchema;
import com.madinnovations.rmu.data.dao.item.schemas.WeaponSchema;
import com.madinnovations.rmu.data.entities.object.Weapon;
import com.madinnovations.rmu.data.entities.object.WeaponTemplate;

import java.io.IOException;

/**
 * Json serializer and deserializer for the {@link Weapon} entities
 */
public class WeaponSerializer extends TypeAdapter<Weapon> implements WeaponSchema {
	@Override
	public void write(JsonWriter out, Weapon value) throws IOException {
		out.beginObject();
		out.name(COLUMN_ID).value(value.getId());
		out.name(COLUMN_BONUS).value(value.getBonus());
		out.name(ItemSchema.COLUMN_ITEM_TEMPLATE_ID).value(value.getItemTemplate().getId());
		out.name(COLUMN_TWO_HANDED).value(value.isTwoHanded());

		out.endObject().flush();
	}

	@Override
	public Weapon read(JsonReader in) throws IOException {
		Weapon weapon = new Weapon();
		in.beginObject();
		while (in.hasNext()) {
			switch (in.nextName()) {
				case COLUMN_ID:
					weapon.setId(in.nextInt());
					break;
				case COLUMN_BONUS:
					weapon.setBonus((short)in.nextInt());
					break;
				case ItemSchema.COLUMN_ITEM_TEMPLATE_ID:
					weapon.setItemTemplate(new WeaponTemplate(in.nextInt()));
					break;
				case COLUMN_TWO_HANDED:
					weapon.setTwoHanded(in.nextBoolean());
					break;
			}
		}
		in.endObject();
		return weapon;
	}
}
