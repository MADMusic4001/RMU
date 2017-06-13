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
package com.madinnovations.rmu.data.dao.item.serializers;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.madinnovations.rmu.data.dao.item.schemas.ItemSchema;
import com.madinnovations.rmu.data.dao.item.schemas.WeaponSchema;
import com.madinnovations.rmu.data.entities.campaign.Campaign;
import com.madinnovations.rmu.data.entities.common.Size;
import com.madinnovations.rmu.data.entities.object.Item;
import com.madinnovations.rmu.data.entities.object.ItemTemplate;
import com.madinnovations.rmu.data.entities.object.Weapon;

import java.io.IOException;

/**
 * Json serializer and deserializer for the {@link Item} entities
 */
public class ItemSerializer extends TypeAdapter<Item> implements ItemSchema {
	private static final String ITEM_TYPE = "itemType";
	@Override
	public void write(JsonWriter out, Item value) throws IOException {
		out.beginObject();
		out.name(ITEM_TYPE).value(value.getJsonName());
		value.serialize(out);
		out.endObject().flush();
	}

	@Override
	public Item read(JsonReader in) throws IOException {
		Item item = new Item();
		boolean weapon = false;
		in.beginObject();
		while (in.hasNext()) {
			switch (in.nextName()) {
				case ITEM_TYPE:
					switch (in.nextString()) {
						case Item.JSON_NAME:
							break;
						case Weapon.JSON_NAME:
							item = new Weapon(item);
							weapon = true;
							break;
					}
					break;
				case COLUMN_ID:
					item.setId(in.nextInt());
					break;
				case COLUMN_CAMPAIGN_ID:
					item.setCampaign(new Campaign(in.nextInt()));
					break;
				case COLUMN_ITEM_TEMPLATE_ID:
					item.setItemTemplate(new ItemTemplate(in.nextInt()));
					break;
				case COLUMN_NAME:
					item.setName(in.nextString());
					break;
				case COLUMN_HISTORY:
					item.setHistory(in.nextString());
					break;
				case COLUMN_SIZE_ID:
					item.setSize(new Size(in.nextInt()));
					break;
				case COLUMN_LEVEL:
					item.setLevel((short)in.nextInt());
					break;
				case WeaponSchema.COLUMN_BONUS:
					if(weapon) {
						((Weapon) item).setBonus((short) in.nextInt());
					}
					break;
				case WeaponSchema.COLUMN_TWO_HANDED:
					if(weapon) {
						((Weapon) item).setTwoHanded(in.nextBoolean());
					}
					break;
			}
		}
		in.endObject();
		return item;
	}
}
