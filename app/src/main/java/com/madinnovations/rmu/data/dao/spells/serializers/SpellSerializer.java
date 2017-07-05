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
package com.madinnovations.rmu.data.dao.spells.serializers;

import android.util.Log;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.madinnovations.rmu.data.dao.spells.schemas.SpellAreaOfEffectParamSchema;
import com.madinnovations.rmu.data.dao.spells.schemas.SpellDurationParamSchema;
import com.madinnovations.rmu.data.dao.spells.schemas.SpellSchema;
import com.madinnovations.rmu.data.entities.spells.AreaOfEffect;
import com.madinnovations.rmu.data.entities.spells.Duration;
import com.madinnovations.rmu.data.entities.spells.Range;
import com.madinnovations.rmu.data.entities.spells.Spell;
import com.madinnovations.rmu.data.entities.spells.SpellList;
import com.madinnovations.rmu.data.entities.spells.SpellSubType;
import com.madinnovations.rmu.data.entities.spells.SpellType;

import java.io.IOException;

/**
 * Json serializer and deserializer for the {@link Spell} entities
 */
public class SpellSerializer extends TypeAdapter<Spell> implements SpellSchema {
	private static final String TAG = "SpellSerializer";
	private static final String LENGTH = "LENGTH";
	private static final String VALUES = "VALUES";
	@Override
	public void write(JsonWriter out, Spell value) throws IOException {
		out.beginObject();
		out.name(COLUMN_ID).value(value.getId());
		out.name(COLUMN_SPELL_LEVEL).value(value.getSpellLevel());
		out.name(COLUMN_NAME).value(value.getName());
		out.name(COLUMN_DESCRIPTION).value(value.getDescription());
		out.name(COLUMN_AREA_OF_EFFECT).value(value.getAreaOfEffect().name());
		out.name(COLUMN_DURATION).value(value.getDuration().name());
		out.name(COLUMN_RANGE).value(value.getRange().name());
		if(value.getRangeParam() != null) {
			out.name(COLUMN_RANGE_PARAM).value(value.getRangeParam());
		}
		out.name(COLUMN_SPELL_TYPE_ID).value(value.getSpellType().getId());
		if(value.getSpellSubType() != null) {
			out.name(COLUMN_SPELL_SUB_TYPE_ID).value(value.getSpellSubType().getId());
		}
		out.name(COLUMN_RR_MOD).value(value.getResistanceRollMod());
		out.name(COLUMN_SPELL_LIST_ID).value(value.getSpellList().getId());
		if(value.getAreaOfEffectParams().length > 0) {
			writeAreaOfEffectParams(out, value);
		}
		if(value.getDurationParams().length > 0) {
			writeDurationParams(out, value);
		}
		out.endObject().flush();
	}

	@Override
	public Spell read(JsonReader in) throws IOException {
		Spell spell = new Spell();
		in.beginObject();
		while (in.hasNext()) {
			String nextName = in.nextName();
			Log.d(TAG, "read: nextName = " + nextName);
			switch (nextName) {
				case COLUMN_ID:
					spell.setId(in.nextInt());
					break;
				case COLUMN_SPELL_LEVEL:
					spell.setSpellLevel((short)in.nextInt());
					break;
				case COLUMN_NAME:
					spell.setName(in.nextString());
					break;
				case COLUMN_DESCRIPTION:
					spell.setDescription(in.nextString());
					break;
				case COLUMN_SPELL_LIST_ID:
					spell.setSpellList(new SpellList(in.nextInt()));
					break;
				case COLUMN_AREA_OF_EFFECT:
					spell.setAreaOfEffect(AreaOfEffect.valueOf(in.nextString()));
					break;
				case COLUMN_DURATION:
					spell.setDuration(Duration.valueOf(in.nextString()));
					break;
				case COLUMN_RANGE:
					spell.setRange(Range.valueOf(in.nextString()));
					break;
				case COLUMN_RANGE_PARAM:
					spell.setRangeParam(in.nextInt());
					break;
				case COLUMN_SPELL_TYPE_ID:
					spell.setSpellType(new SpellType(in.nextInt()));
					break;
				case COLUMN_SPELL_SUB_TYPE_ID:
					spell.setSpellSubType(new SpellSubType(in.nextInt()));
					break;
				case COLUMN_RR_MOD:
					spell.setResistanceRollMod((short)in.nextInt());
					break;
				case SpellAreaOfEffectParamSchema.TABLE_NAME:
					readAreaOfEffectParams(in, spell);
					break;
				case SpellDurationParamSchema.TABLE_NAME:
					readDurationParams(in, spell);
					break;
			}
		}
		in.endObject();

		return spell;
	}

	private void writeAreaOfEffectParams(JsonWriter out, Spell value)  throws IOException {
		out.name(SpellAreaOfEffectParamSchema.TABLE_NAME);
		out.beginObject();
		out.name(LENGTH).value(value.getAreaOfEffectParams().length);
		out.name(VALUES).beginArray();
		for(int param : value.getAreaOfEffectParams()) {
			out.value(param);
		}
		out.endArray();
		out.endObject();
	}

	private void readAreaOfEffectParams(JsonReader in, Spell spell) throws IOException {
		in.beginObject();
		if(LENGTH.equals(in.nextName())) {
			int length = in.nextInt();
			int[] values = new int[length];
			int index = 0;
			if(VALUES.equals(in.nextName())) {
				in.beginArray();
				while (in.hasNext()) {
					values[index++] = in.nextInt();
				}
				in.endArray();
				spell.setAreaOfEffectParams(values);
			}
		}
		in.endObject();
}

	private void writeDurationParams(JsonWriter out, Spell value) throws IOException {
		out.name(SpellDurationParamSchema.TABLE_NAME);
		out.beginObject();
		out.name(LENGTH).value(value.getDurationParams().length);
		out.name(VALUES).beginArray();
		for(int param : value.getDurationParams()) {
			out.value(param);
		}
		out.endArray();
		out.endObject();
	}

	private void readDurationParams(JsonReader in, Spell spell) throws IOException {
		in.beginObject();
		if(LENGTH.equals(in.nextName())) {
			int length = in.nextInt();
			int[] values = new int[length];
			int index = 0;
			if(VALUES.equals(in.nextName())) {
				in.beginArray();
				while (in.hasNext()) {
					values[index++] = in.nextInt();
				}
				in.endArray();
				spell.setDurationParams(values);
			}
		}
		in.endObject();
	}
}
