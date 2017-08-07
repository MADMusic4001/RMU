/*
  Copyright (C) 2017 MadInnovations
  <p>
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  <p>
  http://www.apache.org/licenses/LICENSE-2.0
  <p>
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package com.madinnovations.rmu.data.entities.item;

import com.google.gson.stream.JsonWriter;
import com.madinnovations.rmu.data.dao.item.schemas.NaturalsTemplateSchema;
import com.madinnovations.rmu.data.entities.common.Biome;

import java.io.IOException;

/**
 * Attributes of items found in nature (herbs, poisons, etc.)
 */
public abstract class NaturalsTemplate extends ItemTemplate {
	public static final String JSON_NAME = "NaturalsTemplate";
	private Biome biome = null;
	private Form form = null;
	private Prep prep = null;
	private String season = null;
	private String effects = null;

	/**
	 * Creates a new NaturalsTemplate instance
	 */
	public NaturalsTemplate() {
	}

	/**
	 * Creates a new NaturalsTemplate instance with the given id
	 *
	 * @param id  the id to use for the new instance
	 */
	public NaturalsTemplate(int id) {
		super(id);
	}

	/**
	 * Creates a new NaturalsTemplate instance from the given ItemTemplate
	 *
	 * @param other  an ItemTemplate instance
	 */
	public NaturalsTemplate(ItemTemplate other) {
		this.setId(other.getId());
		this.setName(other.getName());
		this.setWeight(other.getWeight());
		this.setBaseCost(other.getBaseCost());
		this.setStrength(other.getStrength());
		this.setConstructionTime(other.getConstructionTime());
		this.setManeuverDifficulty(other.getManeuverDifficulty());
		this.setNotes(other.getNotes());
		this.setPrimarySlot(other.getPrimarySlot());
		this.setSecondarySlot(other.getSecondarySlot());
	}

	/**
	 * Checks the validity of the Item instance.
	 *
	 * @return true if the ItemTemplate instance is valid, otherwise false.
	 */
	public boolean isValid() {
		return super.isValid() && biome != null && form != null && prep != null && effects != null;
	}

	/**
	 * Writes this instances fields to a JSONWriter
	 *
	 * @param out  a JSONWrite instance to write the fields to
	 * @throws IOException  when an IO error occurs
	 */
	public void serialize(JsonWriter out)
	throws IOException {
		super.serialize(out);
		out.name(NaturalsTemplateSchema.COLUMN_BIOME_ID).value(getBiome().getId());
		out.name(NaturalsTemplateSchema.COLUMN_FORM_NAME).value(getForm().name());
		out.name(NaturalsTemplateSchema.COLUMN_PREP_NAME).value(getPrep().name());
		if(season != null) {
			out.name(NaturalsTemplateSchema.COLUMN_SEASON).value(getSeason());
		}
		out.name(NaturalsTemplateSchema.COLUMN_EFFECTS).value(getEffects());
	}

	// Getters and setters
	public Biome getBiome() {
		return biome;
	}
	public void setBiome(Biome biome) {
		this.biome = biome;
	}
	public Form getForm() {
		return form;
	}
	public void setForm(Form form) {
		this.form = form;
	}
	public Prep getPrep() {
		return prep;
	}
	public void setPrep(Prep prep) {
		this.prep = prep;
	}
	public String getSeason() {
		return season;
	}
	public void setSeason(String season) {
		this.season = season;
	}
	public String getEffects() {
		return effects;
	}
	public void setEffects(String effects) {
		this.effects = effects;
	}
	@Override
	public String getJsonName() {
		return JSON_NAME;
	}
}
