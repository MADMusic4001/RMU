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

import java.util.List;

/**
 * Damage table attributes
 */
public class DamageTable {
    private int id = -1;
    private String name;
    private List<DamageResult> results;

    /**
     * Checks the validity of the DamageTable instance.
     *
     * @return true if the DamageTable instance is valid, otherwise false.
     */
    public boolean isValid() {
        return name != null && !name.isEmpty();
    }

    @Override
    public String toString() {
        return "DamageTable{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", results=" + results +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DamageTable that = (DamageTable) o;

        return id == that.id;

    }

    @Override
    public int hashCode() {
        return id;
    }

    // Getters and setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List<DamageResult> getResults() {
        return results;
    }
    public void setResults(List<DamageResult> results) {
        this.results = results;
    }
}
