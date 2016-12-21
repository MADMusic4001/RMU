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
import com.madinnovations.rmu.data.entities.common.Parameter;
import com.madinnovations.rmu.data.entities.common.Sense;
import com.madinnovations.rmu.data.entities.common.Specialization;
import com.madinnovations.rmu.data.entities.common.Talent;
import com.madinnovations.rmu.data.entities.common.TalentParameterRow;
import com.madinnovations.rmu.data.entities.common.TalentTier;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;

/**
 * Populates a ListView with {@link TalentTier} information
 */
public class TalentTierListAdapter extends ArrayAdapter<TalentTier> {
	private static final String TAG = "TalentTierListAdapter";
	private static final int LAYOUT_RESOURCE_ID = R.layout.list_talent_tiers_row;
	private Context                       context;
	private ListView                      listView = null;
	private LayoutInflater                layoutInflater;
	private TalentTiersAdapterCallbacks   callbacks;
	private Map<Parameter, Collection<?>> parameterCollectionsCache = new HashMap<>();
	private ReactiveUtils                 reactiveUtils;
	private boolean                       addChoiceOption;
	private Object                        choice;
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
		TalentTier talentTier = getItem(position);
		holder.talentTier = talentTier;
		if(holder.parameterViews != null) {
			for(View view : holder.parameterViews.keySet()) {
				holder.parametersLayout.removeView(view);
			}
			holder.parameterViews.clear();
		}
		if(talentTier != null && talentTier.getTalent() != null) {
			String builder = talentTier.getTalent().getName() + " (" + talentTier.getTalent().getDpCost() + "/"
					+ talentTier.getTalent().getDpCostPerTier() + ")";
			holder.talentNameView.setText(builder);
			holder.tiersView.setText(String.valueOf(talentTier.getStartingTiers() + talentTier.getEndingTiers()));
			for(TalentParameterRow row : talentTier.getTalent().getTalentParameterRows()) {
				if(row.getParameter().getEnumValues() != null && row.getEnumName() == null) {
					holder.addSpinner(row.getParameter());
				}
				if(row.getInitialValue() == null || row.getInitialValue() == -1 && row.getEnumName() == null) {
					switch (row.getParameter()) {
						case ATTACK:
							if (row.getInitialValue() == -1) {
								if (holder.parameterViews == null) {
									holder.parameterViews = new HashMap<>();
								}
								holder.addSpinner(row.getParameter());
							}
							break;
						case ELEMENTAL_RR:
						case FEAR_RR:
						case FOLLOWER_FEAR_RR:
						case MAGICAL_RR:
						case PHYSICAL_RR:
							if (row.getInitialValue() == -1) {
								if (holder.parameterViews == null) {
									holder.parameterViews = new HashMap<>();
								}
								holder.addSpinner(row.getParameter());
							}
							break;
						case SENSE:
							if (Sense.CHOICE.name().equals(row.getEnumName())) {
								if (holder.parameterViews == null) {
									holder.parameterViews = new HashMap<>();
								}
								holder.addSpinner(row.getParameter());
							}
							break;
						case SKILL:
							if (row.getInitialValue() == -1) {
								if (holder.parameterViews == null) {
									holder.parameterViews = new HashMap<>();
								}
								holder.addSpinner(row.getParameter());
							}
							break;
						case SPECIALIZATION:
							if (row.getInitialValue() == -1) {
								if (holder.parameterViews == null) {
									holder.parameterViews = new HashMap<>();
								}
								holder.addSpinner(row.getParameter());
							}
							break;
						case SPELL:
							if (row.getInitialValue() == -1) {
								if (holder.parameterViews == null) {
									holder.parameterViews = new HashMap<>();
								}
								holder.addSpinner(row.getParameter());
							}
							break;
						case SPELL_LIST:
							if (row.getInitialValue() == -1) {
								if (holder.parameterViews == null) {
									holder.parameterViews = new HashMap<>();
								}
								holder.addSpinner(row.getParameter());
							}
							break;
						case STAT:
							if (row.getInitialValue() == null || row.getInitialValue() == -1) {
								if (holder.parameterViews == null) {
									holder.parameterViews = new HashMap<>();
								}
								holder.addSpinner(row.getParameter());
							}
							break;
					}
				}
			}
		}

		return rowView;
	}

	public class TalentTierViewHolder {
		private TalentTier           talentTier;
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
					if(talentTier.getStartingTiers() < 2 && callbacks.purchaseTier(
							talentTier.getTalent(),
							talentTier.getStartingTiers(),
							(short)(talentTier.getEndingTiers() - talentTier.getStartingTiers()))) {
						talentTier.setEndingTiers((short)(talentTier.getEndingTiers() + 1));
						tiersView.setText(String.valueOf(talentTier.getStartingTiers() + talentTier.getEndingTiers()));
					}
				}
			});
		}

		private void initDecrementButton() {
			decrementButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(talentTier.getEndingTiers() > 0 && callbacks.sellTier(
							talentTier.getTalent(),
							talentTier.getStartingTiers(),
							(short)(talentTier.getEndingTiers() - talentTier.getStartingTiers()))) {
						talentTier.setEndingTiers((short)(talentTier.getEndingTiers() - 1));
						tiersView.setText(String.valueOf(talentTier.getStartingTiers() + talentTier.getEndingTiers()));
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
			final ArrayAdapter<Object> adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_row);
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
				adapter.addAll(parameter.getEnumValues());
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
								parameterCollectionsCache.put(parameter, (Collection<Object>)results);
								if(addChoiceOption) {
									adapter.add(choice);
								}
								adapter.addAll(parameterCollectionsCache.get(parameter));
								spinner.setAdapter(adapter);
							}
						});
			}

			spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					short tiers = Short.valueOf(tiersView.getText().toString());
					Log.d(TAG, "onItemSelected: tiers = " + tiers);
					if(tiers > 0) {
						if(parameter.getEnumValues() != null) {
							Object selection = spinner.getSelectedItem();
							if(selection instanceof Enum) {
								callbacks.setParameterValue(talentTier.getTalent(), parameter, 0, ((Enum)selection).name());
							}
							else {
								callbacks.setParameterValue(talentTier.getTalent(), parameter, 0, null);
							}
						}
						else {
							Log.d(TAG, "onItemSelected: ");
							callbacks.setParameterValue(talentTier.getTalent(), parameter,
									((Specialization) spinner.getSelectedItem()).getId(), null);
						}
					}
				}
				@Override
				public void onNothingSelected(AdapterView<?> parent) {}
			});
		}

		// Getters
		public TalentTier getTalentTier() {
			return talentTier;
		}
	}

	public interface TalentTiersAdapterCallbacks {
		boolean purchaseTier(Talent talent, short startingTiers, short purchasedThisLevel);
		boolean sellTier(Talent talent, short startingTiers, short purchasedThisLevel);
		void setParameterValue(Talent talent, Parameter parameter, int value, String name);
	}
}
