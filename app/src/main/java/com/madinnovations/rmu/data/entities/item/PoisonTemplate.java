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

import java.io.IOException;

/**
 * Poison attributes
 */
public class PoisonTemplate extends NaturalsTemplate {
	public static final String JSON_NAME = "PoisonTemplate";

	/**
	 * Creates a new PoisonTemplate instance
	 */
	public PoisonTemplate() {
	}

	/**
	 * Creates a new PoisonTemplate instance with the given id
	 *
	 * @param id  the id to use for the new instance
	 */
	public PoisonTemplate(int id) {
		super(id);
	}

	/**
	 * Creates a new PoisonTemplate instance from the given ItemTemplate
	 *
	 * @param other  an ItemTemplate instance
	 */
	public PoisonTemplate(ItemTemplate other) {
		super(other);
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
	}

	@Override
	public String getJsonName() {
		return JSON_NAME;
	}
}
