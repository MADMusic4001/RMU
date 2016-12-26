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
package com.madinnovations.rmu.data.entities.object;

import com.madinnovations.rmu.data.entities.DatabaseObject;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Item attributes
 */
public class Item extends DatabaseObject {
	public static final String JSON_NAME = "Items";
	private ItemTemplate itemTemplate = null;
	private String name = null;
	private String history = null;

    /**
     * Checks the validity of the Item instance.
     *
     * @return true if the Item instance is valid, otherwise false.
     */
    public boolean isValid() {
        return itemTemplate != null;
    }

	@Override
	public String toString() {
		return name == null ? itemTemplate.getName() : name;
	}

	/**
	 * Debug output of this instance.
	 *
	 * @return a String of this instance's attributes.
	 */
	public String debugToString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("id", getId())
				.append("itemTemplate", itemTemplate)
				.append("name", name)
				.toString();
	}

    // Getters and setters
	public ItemTemplate getItemTemplate() {
		return itemTemplate;
	}
	public void setItemTemplate(ItemTemplate itemTemplate) {
		this.itemTemplate = itemTemplate;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHistory() {
		return history;
	}
	public void setHistory(String history) {
		this.history = history;
	}
}
