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

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.madinnovations.rmu.data.dao.combat.BodyPartDao;
import com.madinnovations.rmu.data.dao.combat.CriticalTypeDao;
import com.madinnovations.rmu.data.dao.combat.DamageTableDao;
import com.madinnovations.rmu.data.dao.combat.schemas.AttackSchema;
import com.madinnovations.rmu.data.dao.combat.schemas.CriticalResultSchema;
import com.madinnovations.rmu.data.dao.common.SpecializationDao;
import com.madinnovations.rmu.data.entities.combat.Attack;
import com.madinnovations.rmu.data.entities.combat.CriticalResult;
import com.madinnovations.rmu.data.entities.combat.CriticalType;

import java.lang.reflect.Type;

import javax.inject.Inject;

/**
 * Json serializer and deserializer for the {@link CriticalResult} entities
 */
public class CriticalResultSerializer implements JsonSerializer<CriticalResult>, JsonDeserializer<CriticalResult>,
		CriticalResultSchema {
	BodyPartDao     bodyPartDao;
	CriticalTypeDao criticalTypeDao;

	/**
	 * Creates a new CriticalResultSerializer instance.
	 */
	@Inject
	public CriticalResultSerializer(BodyPartDao bodyPartDao, CriticalTypeDao criticalTypeDao) {
		this.bodyPartDao = bodyPartDao;
		this.criticalTypeDao = criticalTypeDao;
	}

	@Override
	public JsonElement serialize(CriticalResult src, Type typeOfSrc, JsonSerializationContext context) {
		final JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(COLUMN_ID, src.getId());
		jsonObject.addProperty(COLUMN_SEVERITY_CODE, src.getSeverityCode());
		jsonObject.addProperty(COLUMN_RESULT_TEXT, src.getResultText());
		jsonObject.addProperty(COLUMN_MIN_ROLL, src.getMinRoll());
		jsonObject.addProperty(COLUMN_MAX_ROLL, src.getMaxRoll());
		jsonObject.addProperty(COLUMN_HITS, src.getHits());
		jsonObject.addProperty(COLUMN_BLEEDING, src.getBleeding());
		jsonObject.addProperty(COLUMN_FATIGUE, src.getFatigue());
		if(src.getBreakage() != null) {
			jsonObject.addProperty(COLUMN_BREAKAGE, src.getBreakage());
		}
		jsonObject.addProperty(COLUMN_INJURY, src.getInjury());
		jsonObject.addProperty(COLUMN_DAZED, src.getDazed());
		jsonObject.addProperty(COLUMN_STUNNED, src.getStunned());
		jsonObject.addProperty(COLUMN_NO_PARRY, src.getNoParry());
		jsonObject.addProperty(COLUMN_STAGGERED, src.isStaggered());
		jsonObject.addProperty(COLUMN_KNOCK_BACK, src.getKnockBack());
		jsonObject.addProperty(COLUMN_PRONE, src.isProne());
		jsonObject.addProperty(COLUMN_GRAPPLED, src.getGrappled());
		jsonObject.addProperty(COLUMN_BODY_PART_ID, src.getBodyPart().getId());
		jsonObject.addProperty(COLUMN_CRITICAL_TYPE_ID, src.getCriticalType().getId());

		return jsonObject;
	}

	@Override
	public CriticalResult deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
	throws JsonParseException {
		CriticalResult criticalResult = new CriticalResult();
		JsonObject jsonObject = json.getAsJsonObject();
		criticalResult.setId(jsonObject.get(COLUMN_ID).getAsInt());
		criticalResult.setSeverityCode(jsonObject.get(COLUMN_SEVERITY_CODE).getAsCharacter());
		criticalResult.setResultText(jsonObject.get(COLUMN_RESULT_TEXT).getAsString());
		criticalResult.setMinRoll(jsonObject.get(COLUMN_MIN_ROLL).getAsShort());
		criticalResult.setMaxRoll(jsonObject.get(COLUMN_MAX_ROLL).getAsShort());
		criticalResult.setHits(jsonObject.get(COLUMN_HITS).getAsShort());
		criticalResult.setBleeding(jsonObject.get(COLUMN_BLEEDING).getAsShort());
		criticalResult.setFatigue(jsonObject.get(COLUMN_FATIGUE).getAsShort());
		JsonElement element = jsonObject.get(COLUMN_BREAKAGE);
		if(element != null) {
			criticalResult.setBreakage(element.getAsShort());
		}
		criticalResult.setInjury(jsonObject.get(COLUMN_INJURY).getAsShort());
		criticalResult.setDazed(jsonObject.get(COLUMN_DAZED).getAsShort());
		criticalResult.setStunned(jsonObject.get(COLUMN_STUNNED).getAsShort());
		criticalResult.setNoParry(jsonObject.get(COLUMN_NO_PARRY).getAsShort());
		criticalResult.setStaggered(jsonObject.get(COLUMN_STAGGERED).getAsBoolean());
		criticalResult.setKnockBack(jsonObject.get(COLUMN_KNOCK_BACK).getAsShort());
		criticalResult.setProne(jsonObject.get(COLUMN_PRONE).getAsBoolean());
		criticalResult.setGrappled(jsonObject.get(COLUMN_GRAPPLED).getAsShort());
		criticalResult.setBodyPart(bodyPartDao.getById(jsonObject.get(COLUMN_BODY_PART_ID).getAsInt()));
		criticalResult.setCriticalType(criticalTypeDao.getById(jsonObject.get(COLUMN_CRITICAL_TYPE_ID).getAsInt()));

		return criticalResult;
	}
}
