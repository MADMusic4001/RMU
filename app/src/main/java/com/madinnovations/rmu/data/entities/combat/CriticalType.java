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
 * Critical Type attributes
 */
public class CriticalType extends DatabaseObject {
    public static final String JSON_NAME = "CriticalTypes";
    private char   code = 'A';
    private String name = null;

	/**
	 * Creates a new CriticalType instance
	 */
	public CriticalType() {}

	/**
	 * Creates a new CriticalType instance with the given id
	 *
	 * @param id  the id for the new instance
	 */
    public CriticalType(int id) {
        super(id);
    }

    /**
     * Checks the validity of the CriticalType instance.
     *
     * @return true if the CriticalType instance is valid, otherwise false.
     */
    public boolean isValid() {
        return name != null && !name.isEmpty();
    }

    @Override
    public String toString() {
        return name;
    }

    // Getters and setters
    public char getCode() {
        return code;
    }
    public void setCode(char code) {
        this.code = code;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
