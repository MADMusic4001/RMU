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
package com.madinnovations.rmu.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.data.entities.common.Stat;

/**
 * Populates a ListView with {@link Stat} information
 */
public class TwoFieldListAdapter<T> extends ArrayAdapter<T> {
	private static final int LAYOUT_RESOURCE_ID = R.layout.list_2_field_row;
	private LayoutInflater layoutInflater;
	private float field1Weight;
	private float field2Weight;
	private GetValues<T> getValues;

	/**
	 * Creates a new StatListAdapter instance.
	 *
	 * @param context the view {@link Context} the adapter will be attached to.
	 * @param field1Weight  the width weight to be used for feild 1. If a value of 0 is given then the width will be set to WRAP_CONTENT
	 * @param field2Weight  the width weight to be used for feild 2. If a value of 0 is given then the width will be set to WRAP_CONTENT
	 * @param getValues  An instance of the {@link GetValues} interface that will be used to get the text values for each field
	 */
	public TwoFieldListAdapter(Context context, float field1Weight, float field2Weight, GetValues<T> getValues) {
		super(context, LAYOUT_RESOURCE_ID);
		this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.field1Weight = field1Weight;
		this.field2Weight = field2Weight;
		this.getValues = getValues;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView;
		ViewHolder holder;

		if (convertView == null) {
			rowView = layoutInflater.inflate(LAYOUT_RESOURCE_ID, parent, false);
			holder = new ViewHolder((TextView) rowView.findViewById(R.id.row_field1),
									(TextView) rowView.findViewById(R.id.row_field2));
			if(field1Weight == 0) {
				holder.field1View.getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
			}
			else {
				((LinearLayout.LayoutParams) holder.field1View.getLayoutParams()).weight = field1Weight;
			}
			if(field2Weight == 0) {
				holder.field2View.getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
			}
			else {
				((LinearLayout.LayoutParams) holder.field2View.getLayoutParams()).weight = field2Weight;
			}
			rowView.setTag(holder);
		}
		else {
			rowView = convertView;
			//noinspection unchecked
			holder = (ViewHolder) convertView.getTag();
		}

		T t = getItem(position);
		holder.field1View.setText(getValues.getField1Value(t));
		holder.field2View.setText(getValues.getField2Value(t));
		return rowView;
	}

	private class ViewHolder {
		private TextView field1View;
		private TextView field2View;

		ViewHolder(TextView field1View, TextView field2View) {
			this.field1View = field1View;
			this.field2View = field2View;
		}
	}

	/**
	 * Implement this interface to allow the text of the 2 TextViews to be set. Each method will be called whenever the adapter GetView
	 * method is called and will be passed the T instance to be displayed.
	 *
	 * @param <T>  The type displayed by the adapter.
	 */
	public interface GetValues<T> {
		/**
		 * Gets the text to be displayed in the field 1 TextView.
		 *
		 * @param t  the instance of T for the row being displayed
		 * @return  the text to be displayed.
		 */
		CharSequence getField1Value(T t);

		/**
		 * Gets the text to be displayed in the field 2 TextView.
		 *
		 * @param t  the instance of T for the row being displayed
		 * @return  the text to be displayed.
		 */
		CharSequence getField2Value(T t);
	}
}
