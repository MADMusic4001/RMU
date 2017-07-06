/*
 *
 *   Copyright (C) 2017 MadInnovations
 *   <p/>
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *   <p/>
 *   http://www.apache.org/licenses/LICENSE-2.0
 *   <p/>
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 *
 */

package com.madinnovations.rmu.data.entities.common;

import android.support.annotation.StringRes;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.view.RMUApp;

/**
 * Enumeration of all the Size categories
 */
public enum Size {
	MINUSCULE(1, R.string.enum_size_minuscule, "I", 0,
			  Double.valueOf("0"), Double.valueOf("1"),
			  Double.valueOf("0"), Double.valueOf("0.25")),
	DIMINUTIVE(2, R.string.enum_size_diminutive, "II", 0,
			   Double.valueOf("1"), Double.valueOf("4"),
			   Double.valueOf("0.25"), Double.valueOf("0.5")),
	TINY(3, R.string.enum_size_tiny, "II", 0,
		 Double.valueOf("4"), Double.valueOf("16"),
		 Double.valueOf("0.5"), Double.valueOf("1")),
	SMALL(4, R.string.enum_size_small, "IV", 0,
		  Double.valueOf("16"), Double.valueOf("64"),
		  Double.valueOf("1"), Double.valueOf("4")),
	MEDIUM(5, R.string.enum_size_medium, "V", 0,
		   Double.valueOf("64"), Double.valueOf("256"),
		   Double.valueOf("4"), Double.valueOf("8")),
	BIG(6, R.string.enum_size_big, "VI", 0,
		Double.valueOf("256"), Double.valueOf("1000"),
		Double.valueOf("8"), Double.valueOf("15")),
	LARGE(7, R.string.enum_size_large, "VII", 0,
		  Double.valueOf("1000"), Double.valueOf("4000"),
		  Double.valueOf("15"), Double.valueOf("25")),
	HUGE(8, R.string.enum_size_huge, "VIII", 0,
		 Double.valueOf("4000"), Double.valueOf("16000"),
		 Double.valueOf("25"), Double.valueOf("50")),
	GIGANTIC(9, R.string.enum_size_gigantic, "IX", 0,
			 Double.valueOf("16000"), Double.valueOf("64000"),
			 Double.valueOf("50"), Double.valueOf("100")),
	ENORMOUS(10, R.string.enum_size_enormous, "X", 0,
			 Double.valueOf("64000"), Double.valueOf("262000"),
			 Double.valueOf("100"), Double.valueOf("200")),
	IMMENSE(11, R.string.enum_size_immense, "XI", 0,
			Double.valueOf("262000"), Double.valueOf("1000000"),
			Double.valueOf("200"), Double.valueOf("400")),
	BEHEMOTH(12, R.string.enum_size_behemoth, "XII", 0,
			 Double.valueOf("1000000"), Double.valueOf("4000000"),
			 Double.valueOf("400"), Double.valueOf("800")),
	LEVIATHAN(13, R.string.enum_size_leviathan, "XIII", 0,
			  Double.valueOf("4000000"), Double.MAX_VALUE,
			  Double.valueOf("800"), Double.MAX_VALUE);

	private static final String[] sizeStrings;
	private            int    ordinal;
	private @StringRes int    textResourceId;
	private            String code;
	private @StringRes int    examplesResourceId;
	private            Double minWeight;
	private            Double maxWeight;
	private            Double minHeight;
	private            Double maxHeight;

	static {
		sizeStrings = new String[Size.values().length];
		int i = 0;
		for(Size size : Size.values()) {
			sizeStrings[i++] = size.toString();
		}
	}

	Size(int ordinal, int textResourceId, String code, int examplesResourceId, Double minWeight, Double maxWeight,
		 Double minHeight, Double maxHeight) {
		this.ordinal = ordinal;
		this.textResourceId = textResourceId;
		this.code = code;
		this.examplesResourceId = examplesResourceId;
		this.minWeight = minWeight;
		this.maxWeight = maxWeight;
		this.minHeight = minHeight;
		this.maxHeight = maxHeight;
	}

	@Override
	public String toString() {
		return RMUApp.getResourceUtils().getString(textResourceId);
	}

	/**
	 * Gets the Size with the given text value.
	 *
	 * @param textValue  the textValue of the desired Size instance
	 * @return the Realm instance with the given textValue or null if not found.
	 */
	public static Size getSizeWithName(String textValue) {
		for(Size size : Size.values()) {
			if(size.toString().equals(textValue)) {
				return size;
			}
		}
		return null;
	}

	/**
	 * Gets the examples string from the application string resources
	 *
	 * @return  a string with examples of creatures in this size category or an empty string if not defined.
	 */
	public String getExamples() {
		String result = "";
		if(examplesResourceId > 0) {
			result = RMUApp.getResourceUtils().getString(examplesResourceId);
		}
		return result;
	}

	// Getters
	public static String[] getSizeStrings() {
		return sizeStrings;
	}
	public int getOrdinal() {
		return ordinal;
	}
	public int getTextResourceId() {
		return textResourceId;
	}
	public String getCode() {
		return code;
	}
	public int getExamplesResourceId() {
		return examplesResourceId;
	}
	public Double getMinWeight() {
		return minWeight;
	}
	public Double getMaxWeight() {
		return maxWeight;
	}
	public Double getMinHeight() {
		return minHeight;
	}
	public Double getMaxHeight() {
		return maxHeight;
	}
}
