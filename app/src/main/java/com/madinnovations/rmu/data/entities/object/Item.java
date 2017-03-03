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
import com.madinnovations.rmu.data.entities.campaign.Campaign;
import com.madinnovations.rmu.data.entities.common.Size;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Item attributes
 */
public class Item extends DatabaseObject {
	public static final String JSON_NAME = "Items";
	private Campaign campaign = null;
	private ItemTemplate itemTemplate = null;
	private String name = null;
	private String history = null;
	private Size size;
	private short level = 1;
	private boolean twoHanded = false;

	/**
	 * Creates a new default Item instance
 	 */
	public Item() {
	}

	/**
	 * Creates an Item instance with the given id
	 *
	 * @param id  the starting id for the new instance
	 */
	public Item(int id) {
		super(id);
	}

	/**
     * Checks the validity of the Item instance.
     *
     * @return true if the Item instance is valid, otherwise false.
     */
    public boolean isValid() {
		return (campaign != null && itemTemplate != null && size != null)
				&& (itemTemplate.getName() != null || name != null);
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
				.append("campaign", campaign)
				.append("itemTemplate", itemTemplate)
				.append("name", name)
				.append("history", history)
				.append("size", size)
				.append("level", level)
				.toString();
	}

    // Getters and setters
	public Campaign getCampaign() {
		return campaign;
	}
	public void setCampaign(Campaign campaign) {
		this.campaign = campaign;
	}
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
	public Size getSize() {
		return size;
	}
	public void setSize(Size size) {
		this.size = size;
	}
	public short getLevel() {
		return level;
	}
	public void setLevel(short level) {
		this.level = level;
	}
}
