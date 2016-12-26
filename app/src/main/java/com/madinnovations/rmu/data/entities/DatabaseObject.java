/**
 * Copyright (C) 2016 MadInnovations
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
package com.madinnovations.rmu.data.entities;

/**
 * Base class for any objects that will be saved in the database.
 */
public abstract class DatabaseObject {
	private int id = -1;

	/**
	 * Default constructor
	 */
	public DatabaseObject() {
	}

	/**
	 * Creates a new DatabaseObject instance with the given id
	 *
	 * @param id  the id of the new instance
	 */
	public DatabaseObject(int id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		DatabaseObject that = (DatabaseObject) o;

		return getId() == that.getId();
	}

	@Override
	public int hashCode() {
		return getId();
	}

	// Getter and setter
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
}
