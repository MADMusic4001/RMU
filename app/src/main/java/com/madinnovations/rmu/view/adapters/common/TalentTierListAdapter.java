/*
  Copyright (C) 2016 MadInnovations
  <p/>
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  <p/>
  http://www.apache.org/licenses/LICENSE-2.0
  <p/>
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package com.madinnovations.rmu.view.adapters.common;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.controller.utils.ReactiveUtils;
import com.madinnovations.rmu.data.entities.DatabaseObject;
import com.madinnovations.rmu.data.entities.common.Parameter;
import com.madinnovations.rmu.data.entities.common.TalentInstance;
import com.madinnovations.rmu.data.entities.common.TalentParameterRow;
import com.madinnovations.rmu.data.entities.common.TalentTier;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;

/**
 * Populates a ListView with {@link TalentTier} information
 */
public class TalentTierListAdapter extends ArrayAdapter<TalentInstance> {
	private static final String TAG = "TalentTierListAdapter";
	private static final int LAYOUT_RESOURCE_ID = R.layout.character_talents_talent_tiers_list_row;
	private Context                                    context;
	private ListView                                   listView = null;
	private LayoutInflater                             layoutInflater;
	private TalentTiersAdapterCallbacks                callbacks;
	private Map<Parameter, Collection<DatabaseObject>> parameterCollectionsCache = new HashMap<>();
	private ReactiveUtils                              reactiveUtils;
	private boolean                                    addChoiceOption;
	private Object                                     choice;
	private int[] colors = new int[]{
			R.color.list_even_row_background,
			R.color.list_odd_row_background};

	/**
	 * Creates a new TalentTierListAdapter instance.
	 *
	 * @param context the view {@link Context} the adapter will be attached to.
	 * @param callbacks  an instance of a class that implements the {@link TalentTiersAdapterCallbacks} interface.
	 * @param reactiveUtils  an instance of the {@link ReactiveUtils} class.
	 * @param addChoiceOption  true if a "choice" option should be added to the spinners to allow the parameter value to be selected at a
	 *                         later time, otherwise false.
	 */
	public TalentTierListAdapter(final Context context, TalentTiersAdapterCallbacks callbacks, ReactiveUtils reactiveUtils,
								 boolean addChoiceOption) {
		super(context, LAYOUT_RESOURCE_ID);
		this.context = context;
		this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.callbacks = callbacks;
		this.reactiveUtils = reactiveUtils;
		this.addChoiceOption = addChoiceOption;
		choice = new Object() {
			@Override
			public String toString() {
				return context.getString(R.string.choice_label);
			}
		};
	}

	@Override
	@NonNull
	public View getView(int position, View convertView, @NonNull ViewGroup parent) {
		View rowView;
		TalentTierViewHolder holder;

		if(listView == null && parent instanceof ListView) {
			listView = (ListView)parent;
		}
		if (convertView == null) {
			rowView = layoutInflater.inflate(LAYOUT_RESOURCE_ID, parent, false);
			holder = new TalentTierViewHolder((TextView) rowView.findViewById(R.id.talent_name_view),
											  (TextView) rowView.findViewById(R.id.talent_tiers_view),
											  (ImageButton) rowView.findViewById(R.id.increment_button),
											  (ImageButton) rowView.findViewById(R.id.decrement_button),
											  (LinearLayout) rowView.findViewById(R.id.parameters_layout));
			rowView.setTag(holder);
		}
		else {
			rowView = convertView;
			holder = (TalentTierViewHolder) convertView.getTag();
		}

		rowView.setBackgroundColor(ContextCompat.getColor(context, colors[position % colors.length]));
		TalentInstance talentInstance = getItem(position);
		holder.talentInstance = talentInstance;
		if(holder.parameterViews != null) {
			for(View view : holder.parameterViews.keySet()) {
				holder.parametersLayout.removeView(view);
			}
			holder.parameterViews.clear();
		}
		if(talentInstance != null && talentInstance.getTalent() != null) {
			String builder = talentInstance.getTalent().getName() + " (" + talentInstance.getTalent().getDpCost() + "/"
					+ talentInstance.getTalent().getDpCostPerTier() + ")";
			holder.talentNameView.setText(builder);
			holder.tiersView.setText(String.valueOf(talentInstance.getTiers()));
			for(TalentParameterRow row : talentInstance.getTalent().getTalentParameterRows()) {
				if(row.getParameter().getEnumValues() != null) {
					holder.addSpinner(row.getParameter());
				}
				else if(row.getParameter().getHandler() != null) {
					holder.addSpinner(row.getParameter());
				}
			}
		}

		rowView.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				return false;
			}
		});
		return rowView;
	}

	public class TalentTierViewHolder {
		private TalentInstance       talentInstance;
		private TextView             talentNameView;
		private TextView             tiersView;
		private ImageButton          incrementButton;
		private ImageButton          decrementButton;
		private LinearLayout         parametersLayout;
		private Map<View, Parameter> parameterViews;

		TalentTierViewHolder(TextView talentNameView, TextView tiersView, ImageButton incrementButton,
							 ImageButton decrementButton, LinearLayout parametersLayout) {
			this.talentNameView = talentNameView;
			this.tiersView = tiersView;
			this.incrementButton = incrementButton;
			this.decrementButton = decrementButton;
			this.parametersLayout = parametersLayout;

			initIncrementButton();
			initDecrementButton();
		}

		private void initIncrementButton() {
			incrementButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(callbacks.getTiersThisLevel(talentInstance) < 2) {
						short newTiers =  callbacks.purchaseTier(talentInstance);
						talentInstance.setTiers(newTiers);
						tiersView.setText(String.valueOf(newTiers));
					}
				}
			});
		}

		private void initDecrementButton() {
			decrementButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(talentInstance.getTiers() > 0) {
						short newTiers = callbacks.sellTier(talentInstance);
						talentInstance.setTiers(newTiers);
						tiersView.setText(String.valueOf(newTiers));
					}
				}
			});
		}

		private void addSpinner(final Parameter parameter) {
			final Spinner spinner = new Spinner(getContext());
			if(parameterViews == null) {
				parameterViews = new HashMap<>();
			}
			parameterViews.put(spinner, parameter);
			parametersLayout.addView(spinner);
			final ArrayAdapter<Object> adapter = new ArrayAdapter<>(getContext(), R.layout.single_field_row);
			if(parameterCollectionsCache.get(parameter) != null) {
				adapter.clear();
				if(addChoiceOption) {
					adapter.add(choice);
				}
				adapter.addAll(parameterCollectionsCache.get(parameter));
				adapter.notifyDataSetChanged();
				spinner.setAdapter(adapter);
			}
			else if(parameter.getEnumValues() != null) {
				adapter.clear();
				if(addChoiceOption) {
					adapter.add(choice);
				}
				adapter.addAll((Object[])parameter.getEnumValues());
				adapter.notifyDataSetChanged();
				spinner.setAdapter(adapter);
			}
			else if(parameter.getHandler() != null) {
				reactiveUtils.getGetAllObservable(parameter.getHandler())
						.subscribe(new Subscriber<Object>() {
							@Override
							public void onCompleted() {}
							@Override
							public void onError(Throwable e) {
								Log.e(TAG, "Exception caught getting talent parameter values.", e);
							}
							@SuppressWarnings("unchecked")
							@Override
							public void onNext(Object results) {
								parameterCollectionsCache.put(parameter, (Collection<DatabaseObject>)results);
								if(addChoiceOption) {
									adapter.add(choice);
								}
								adapter.addAll(parameterCollectionsCache.get(parameter));
								spinner.setAdapter(adapter);
								for(DatabaseObject databaseObject : parameterCollectionsCache.get(parameter)) {
									if(talentInstance.getParameterValues().get(parameter) != null &&
										databaseObject.getId() == (Integer)talentInstance.getParameterValues().get(parameter)) {
										spinner.setSelection(adapter.getPosition(databaseObject));
										break;
									}
								}
							}
						});
			}

			spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					short tiers = Short.valueOf(tiersView.getText().toString());
					if(tiers > 0) {
						if(parameter.getEnumValues() != null) {
							Object selection = spinner.getSelectedItem();
							if(selection instanceof Enum) {
								callbacks.setParameterValue(talentInstance, parameter, -1, ((Enum)selection).name());
							}
							else {
								callbacks.setParameterValue(talentInstance, parameter, -1, null);
							}
						}
						else {
							if(spinner.getSelectedItem() instanceof DatabaseObject) {
								callbacks.setParameterValue(talentInstance, parameter,
															((DatabaseObject) spinner.getSelectedItem()).getId(), null);
							}
							else {
								callbacks.setParameterValue(talentInstance, parameter,
															-1, null);
							}
						}
					}
				}
				@Override
				public void onNothingSelected(AdapterView<?> parent) {}
			});
		}

		// Getters
		public TalentInstance getTalentInstance() {
			return talentInstance;
		}
	}

	public interface TalentTiersAdapterCallbacks {
		short purchaseTier(TalentInstance talentInstance);
		short sellTier(TalentInstance talentInstance);
		void setParameterValue(TalentInstance talentInstance, Parameter parameter, int value, String name);
		short getTiersThisLevel(TalentInstance talentInstance);
		short getTiers(TalentInstance talentInstance);
	}
}
