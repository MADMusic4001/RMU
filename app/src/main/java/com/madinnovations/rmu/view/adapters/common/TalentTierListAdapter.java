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
package com.madinnovations.rmu.view.adapters.common;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.data.entities.common.TalentTier;

/**
 * Populates a ListView with {@link TalentTier} information
 */
public class TalentTierListAdapter extends ArrayAdapter<TalentTier> {
	private static final int LAYOUT_RESOURCE_ID = R.layout.list_talent_tiers_row;
	private ListView listView = null;
	private LayoutInflater layoutInflater;
	private SetTalentTier setTalentTierHandler;

	/**
	 * Creates a new TalentTierListAdapter instance.
	 *
	 * @param context the view {@link Context} the adapter will be attached to.
	 */
	public TalentTierListAdapter(Context context, SetTalentTier setTalentTierHandler) {
		super(context, LAYOUT_RESOURCE_ID);
		this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.setTalentTierHandler = setTalentTierHandler;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView;
		ViewHolder holder;

		if(listView == null && parent instanceof ListView) {
			listView = (ListView)parent;
		}
		if (convertView == null) {
			rowView = layoutInflater.inflate(LAYOUT_RESOURCE_ID, parent, false);
			holder = new ViewHolder((TextView) rowView.findViewById(R.id.talent_name_view),
									(EditText) rowView.findViewById(R.id.talent_tiers_edit));
			rowView.setTag(holder);
		}
		else {
			rowView = convertView;
			holder = (ViewHolder) convertView.getTag();
		}

		TalentTier talentTier = getItem(position);
		holder.currentInstance = talentTier;
		holder.talentNameView.setText(talentTier.getTalent().getName());
		holder.tierEdit.setText(String.valueOf(talentTier.getTier()));

		return rowView;
	}

	private class ViewHolder {
		private TalentTier currentInstance;
		private TextView talentNameView;
		private EditText tierEdit;

		ViewHolder(TextView talentNameView, EditText tierEdit) {
			this.talentNameView = talentNameView;
			initTalentNameView();
			this.tierEdit = tierEdit;
			initTierEdit();
		}

		private void initTalentNameView() {
			talentNameView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Log.d("RMU", "In talentNameView.onClick");
//					if(listView != null) {
//						int position = getPosition(currentInstance);
//						listView.setItemChecked(position, !listView.isItemChecked(position));
//					}
				}
			});
//
			talentNameView.setOnLongClickListener(new View.OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
//					Log.d("RMU", "In onLongClick");
//					if(listView != null) {
//						return listView.performLongClick();
//					}
					Log.d("RMU", "In talentNameView.onLongClick");
					return true;
				}
			});
		}

		private void initTierEdit() {
			tierEdit.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
//					if(listView != null) {
//						int position = getPosition(currentInstance);
//						listView.setItemChecked(position, !listView.isItemChecked(position));
//					}
					Log.d("RMU", "In tierEdit.onClick");
				}
				});

			tierEdit.setOnLongClickListener(new View.OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
//					if(listView != null) {
//						return listView.performLongClick();
//					}
					Log.d("RMU", "In tierEdit.onLongClick");
					return true;
			}
			});
			tierEdit.setOnDragListener(new View.OnDragListener() {
				@Override
				public boolean onDrag(View v, DragEvent event) {
					return true;
				}
			});
			tierEdit.addTextChangedListener(new TextWatcher() {
				@Override
				public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
				@Override
				public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
				@Override
				public void afterTextChanged(Editable editable) {
					if (editable.length() == 0) {
						tierEdit.setError(getContext().getString(R.string.validation_creature_variety_tier_required));
					}
					else {
						try {
							Short.valueOf(tierEdit.getText().toString());
							tierEdit.setError(null);
						}
						catch (NumberFormatException ex) {
							tierEdit.setError(getContext().getString(R.string.validation_creature_variety_tier_required));
						}
					}
				}
			});
			tierEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
				@Override
				public void onFocusChange(View view, boolean hasFocus) {
					if(!hasFocus) {
						if(tierEdit.length() > 0) {
							try {
								short newTier = Short.valueOf(tierEdit.getText().toString());
								if (newTier != currentInstance.getTier()) {
									currentInstance.setTier(newTier);
									setTalentTierHandler.setTalentTier(currentInstance);
								}
								tierEdit.setError(null);
							}
							catch (NumberFormatException ex) {
								tierEdit.setError(getContext().getString(R.string.validation_creature_variety_tier_required));
							}
						}
					}
				}
			});
		}
	}

	public interface SetTalentTier {
		/**
		 * Sets a talent tier
		 *
		 * @param talentTier  a TalentTier instance
		 */
		public void setTalentTier(TalentTier talentTier);
	}
}
