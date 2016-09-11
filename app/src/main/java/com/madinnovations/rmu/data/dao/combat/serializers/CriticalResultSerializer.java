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
package com.madinnovations.rmu.data.dao.combat.serializers;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.madinnovations.rmu.data.dao.combat.schemas.CriticalResultSchema;
import com.madinnovations.rmu.data.entities.combat.BodyPart;
import com.madinnovations.rmu.data.entities.combat.CriticalResult;
import com.madinnovations.rmu.data.entities.combat.CriticalType;

import java.io.IOException;

/**
 * Json serializer and deserializer for the {@link CriticalResult} entities
 */
public class CriticalResultSerializer extends TypeAdapter<CriticalResult> implements CriticalResultSchema {

	@Override
	public void write(JsonWriter out, CriticalResult value) throws IOException {
		out.beginObject();
		out.name(COLUMN_ID).value(value.getId());
		out.name(COLUMN_SEVERITY_CODE).value(value.getSeverityCode());
		out.name(COLUMN_RESULT_TEXT).value(value.getResultText());
		out.name(COLUMN_MIN_ROLL).value(value.getMinRoll());
		out.name(COLUMN_MAX_ROLL).value(value.getMaxRoll());
		out.name(COLUMN_HITS).value(value.getHits());
		out.name(COLUMN_BLEEDING).value(value.getBleeding());
		out.name(COLUMN_FATIGUE).value(value.getFatigue());
		if(value.getBreakage() != null) {
			out.name(COLUMN_BREAKAGE).value(value.getBreakage());
		}
		out.name(COLUMN_INJURY).value(value.getInjury());
		out.name(COLUMN_DAZED).value(value.getDazed());
		out.name(COLUMN_STUNNED).value(value.getStunned());
		out.name(COLUMN_NO_PARRY).value(value.getNoParry());
		out.name(COLUMN_STAGGERED).value(value.isStaggered());
		out.name(COLUMN_KNOCK_BACK).value(value.getKnockBack());
		out.name(COLUMN_PRONE).value(value.isProne());
		out.name(COLUMN_GRAPPLED).value(value.getGrappled());
		out.name(COLUMN_BODY_PART_ID).value(value.getBodyPart().getId());
		out.name(COLUMN_CRITICAL_TYPE_ID).value(value.getCriticalType().getId());
		out.endObject();
		out.flush();
	}

	@Override
	public CriticalResult read(JsonReader in) throws IOException {
		CriticalResult criticalResult = new CriticalResult();
		in.beginObject();
		while(in.hasNext()) {
			switch(in.nextName()) {
				case COLUMN_ID:
					criticalResult.setId(in.nextInt());
					break;
				case COLUMN_SEVERITY_CODE:
					criticalResult.setSeverityCode(in.nextString().charAt(0));
					break;
				case COLUMN_RESULT_TEXT:
					criticalResult.setResultText(in.nextString());
					break;
				case COLUMN_MIN_ROLL:
					criticalResult.setMinRoll((short)in.nextInt());
					break;
				case COLUMN_MAX_ROLL:
					criticalResult.setMaxRoll((short)in.nextInt());
					break;
				case COLUMN_HITS:
					criticalResult.setHits((short)in.nextInt());
					break;
				case COLUMN_BLEEDING:
					criticalResult.setBleeding((short)in.nextInt());
					break;
				case COLUMN_FATIGUE:
					criticalResult.setFatigue((short)in.nextInt());
					break;
				case COLUMN_BREAKAGE:
					criticalResult.setBreakage((short)in.nextInt());
					break;
				case COLUMN_INJURY:
					criticalResult.setInjury((short)in.nextInt());
					break;
				case COLUMN_DAZED:
					criticalResult.setDazed((short)in.nextInt());
					break;
				case COLUMN_STUNNED:
					criticalResult.setStunned((short)in.nextInt());
					break;
				case COLUMN_NO_PARRY:
					criticalResult.setNoParry((short)in.nextInt());
					break;
				case COLUMN_STAGGERED:
					criticalResult.setStaggered(in.nextBoolean());
					break;
				case COLUMN_KNOCK_BACK:
					criticalResult.setKnockBack((short)in.nextInt());
					break;
				case COLUMN_PRONE:
					criticalResult.setProne(in.nextBoolean());
					break;
				case COLUMN_GRAPPLED:
					criticalResult.setGrappled((short)in.nextInt());
					break;
				case COLUMN_BODY_PART_ID:
					criticalResult.setBodyPart(new BodyPart(in.nextInt()));
					break;
				case COLUMN_CRITICAL_TYPE_ID:
					criticalResult.setCriticalType(new CriticalType(in.nextInt()));
					break;
			}
		}
		return criticalResult;
	}
}
