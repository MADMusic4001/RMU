/**
 * Copyright (C) 2017 MadInnovations
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
package com.madinnovations.rmu.data.entities.item;

import android.support.annotation.StringRes;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.view.RMUApp;

/**
 * Forms that Naturals can come from.
 */
public enum Form {
	ALGAE(R.string.enum_form_algae),
	BARK(R.string.enum_form_bark),
	BATS(R.string.enum_form_bats),
	BEE(R.string.enum_form_bee),
	BERRY(R.string.enum_form_berry),
	BUD(R.string.enum_form_bud),
	BULB(R.string.enum_form_bulb),
	CACTUS(R.string.enum_form_cactus),
	CLAMS(R.string.enum_form_clams),
	CONE(R.string.enum_form_cone),
	DRAKES(R.string.enum_form_drakes),
	DRAGON(R.string.enum_form_dragon),
	FISH(R.string.enum_form_fish),
	FLOWER(R.string.enum_form_flower),
	FROG(R.string.enum_form_frog),
	FRUIT(R.string.enum_form_fruit),
	FUNGUS(R.string.enum_form_fungus),
	GRASS(R.string.enum_form_grass),
	JUICE(R.string.enum_form_juice),
	KELP(R.string.enum_form_kelp),
	LEAF(R.string.enum_form_leaf),
	LICHEN(R.string.enum_form_lichen),
	MOSS(R.string.enum_form_moss),
	MUSHROOM(R.string.enum_form_mushroom),
	NECTAR(R.string.enum_form_nectar),
	NODULE(R.string.enum_form_nodule),
	NUT(R.string.enum_form_nut),
	POLLEN(R.string.enum_form_pollen),
	POWDER(R.string.enum_form_powder),
	REED(R.string.enum_form_reed),
	RESIN(R.string.enum_form_resin),
	ROOT(R.string.enum_form_root),
	SAP(R.string.enum_form_sap),
	SCORPION(R.string.enum_form_scorpion),
	SEEDS(R.string.enum_form_seeds),
	SNAKE(R.string.enum_form_snake),
	SPIDER(R.string.enum_form_spider),
	SPINE(R.string.enum_form_spine),
	STALK(R.string.enum_form_stalk),
	STEM(R.string.enum_form_stem);

	private @StringRes int nameResource;

	Form(int nameResource) {
		this.nameResource = nameResource;
	}

	@Override
	public String toString() {
		return RMUApp.getResourceUtils().getString(nameResource);
	}

	// Getters
	public int getNameResource() {
		return nameResource;
	}
}
