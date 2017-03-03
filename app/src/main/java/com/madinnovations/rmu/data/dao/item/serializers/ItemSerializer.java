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
import com.madinnovations.rmu.data.dao.item.schemas.ItemSchema;
import com.madinnovations.rmu.data.entities.campaign.Campaign;
import com.madinnovations.rmu.data.entities.common.Size;
import com.madinnovations.rmu.data.entities.object.Item;
import com.madinnovations.rmu.data.entities.object.ItemTemplate;

import java.io.IOException;

/**
 * Json serializer and deserializer for the {@link Item} entities
 */
public class ItemSerializer extends TypeAdapter<Item> implements ItemSchema {
	@Override
	public void write(JsonWriter out, Item value) throws IOException {
		out.beginObject();
		out.name(COLUMN_ID).value(value.getId());
		out.name(COLUMN_CAMPAIGN_ID).value(value.getCampaign().getId());
		out.name(COLUMN_ITEM_TEMPLATE_ID).value(value.getItemTemplate().getId());
		out.name(COLUMN_NAME).value(value.getName());
		out.name(COLUMN_HISTORY).value(value.getHistory());
		out.name(COLUMN_SIZE_ID).value(value.getSize().getId());
		out.name(COLUMN_LEVEL).value(value.getLevel());
		out.endObject().flush();
	}

	@Override
	public Item read(JsonReader in) throws IOException {
		Item item = new Item();
		in.beginObject();
		while (in.hasNext()) {
			switch (in.nextName()) {
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
			}
		}
		in.endObject();
		return item;
	}
}
