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
package com.madinnovations.rmu.data.entities.combat;

import com.madinnovations.rmu.data.entities.DatabaseObject;

/**
 * Body part attributes
 */
public class BodyPart extends DatabaseObject {
    public static final String JSON_NAME = "BodyParts";
    private String name = null;
    private String description = null;

	/**
     * Creates a new BodyPart instance
     */
    public BodyPart() {
    }

	/**
	 * Creates a new BodyPart instance with the given id
	 *
	 * @param id  the id for the new instance
	 */
    public BodyPart(int id) {
        super(id);
    }

    /**
     * Checks the validity of the BodyPart instance.
     *
     * @return true if the BodyPart instance is valid, otherwise false.
     */
    public boolean isValid() {
        return name != null && !name.isEmpty() && description != null && !description.isEmpty();
    }

    @Override
    public String toString() {
        return name;
    }

    // Getters and setters
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
