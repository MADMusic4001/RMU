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
import com.madinnovations.rmu.controller.rxhandler.combat.AttackRxHandler;
import com.madinnovations.rmu.controller.rxhandler.common.SkillRxHandler;
import com.madinnovations.rmu.controller.rxhandler.common.SpecializationRxHandler;
import com.madinnovations.rmu.controller.rxhandler.spell.SpellListRxHandler;
import com.madinnovations.rmu.controller.rxhandler.spell.SpellRxHandler;
import com.madinnovations.rmu.controller.utils.ReactiveUtils;
import com.madinnovations.rmu.data.entities.combat.Attack;
import com.madinnovations.rmu.data.entities.combat.Resistance;
import com.madinnovations.rmu.data.entities.common.Parameter;
import com.madinnovations.rmu.data.entities.common.Sense;
import com.madinnovations.rmu.data.entities.common.Skill;
import com.madinnovations.rmu.data.entities.common.Specialization;
import com.madinnovations.rmu.data.entities.common.Statistic;
import com.madinnovations.rmu.data.entities.common.Talent;
import com.madinnovations.rmu.data.entities.common.TalentParameterRow;
import com.madinnovations.rmu.data.entities.common.TalentTier;
import com.madinnovations.rmu.data.entities.spells.Spell;
import com.madinnovations.rmu.data.entities.spells.SpellList;

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
	private AttackRxHandler             attackRxHandler;
	private SkillRxHandler              skillRxHandler;
	private SpecializationRxHandler     specializationRxHandler;
	private SpellRxHandler              spellRxHandler;
	private SpellListRxHandler          spellListRxHandler;
	private Context                     context;
	private ListView                    listView = null;
	private LayoutInflater              layoutInflater;
	private TalentTiersAdapterCallbacks callbacks;
	private Collection<Attack>          attacksList;
	private Collection<Skill>           skillsList;
	private Map<Parameter, Collection<?>> parameterCollectionsCache = new HashMap<>();
	private Collection<Specialization>  specializationsList;
	private Collection<Spell>           spellsList;
	private Collection<SpellList>       spellListsList;
	private int[] colors = new int[]{
			R.color.list_even_row_background,
			R.color.list_odd_row_background};

	/**
	 * Creates a new TalentTierListAdapter instance.
	 *
	 * @param context the view {@link Context} the adapter will be attached to.
	 */
	public TalentTierListAdapter(Context context, TalentTiersAdapterCallbacks callbacks, AttackRxHandler attackRxHandler,
								 SkillRxHandler skillRxHandler, SpecializationRxHandler specializationRxHandler,
								 SpellRxHandler spellRxHandler, SpellListRxHandler spellListRxHandler) {
		super(context, LAYOUT_RESOURCE_ID);
		this.context = context;
		this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.callbacks = callbacks;
		this.attackRxHandler = attackRxHandler;
		this.skillRxHandler = skillRxHandler;
		this.specializationRxHandler = specializationRxHandler;
		this.spellRxHandler = spellRxHandler;
		this.spellListRxHandler = spellListRxHandler;
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
				Log.d(TAG, "getView: parameter = " + row.getParameter());
				if(row.getParameter().getEnumType() != null && row.getEnumName() == null) {
					Log.d(TAG, "getView: Adding spínner" + row.getParameter().getEnumType());
					holder.addSpinner(row.getParameter());
				}
				if(row.getInitialValue() == null || row.getInitialValue() == -1 && row.getEnumName() == null) {
					Log.d(TAG, "getView: row.getParameter = " + row.getParameter());
					switch (row.getParameter()) {
						case ATTACK:
							if (row.getInitialValue() == -1) {
								if (holder.parameterViews == null) {
									holder.parameterViews = new HashMap<>();
								}
								holder.addAttackSpinner(row.getParameter());
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
								holder.addResistanceSpinner(row.getParameter());
							}
							break;
						case SENSE:
							if (Sense.CHOICE.name().equals(row.getEnumName())) {
								if (holder.parameterViews == null) {
									holder.parameterViews = new HashMap<>();
								}
								holder.addSenseSpinner(row.getParameter());
							}
							break;
						case SKILL:
							if (row.getInitialValue() == -1) {
								if (holder.parameterViews == null) {
									holder.parameterViews = new HashMap<>();
								}
								holder.addSkillSpinner(row.getParameter());
							}
							break;
						case SPECIALIZATION:
							if (row.getInitialValue() == -1) {
								if (holder.parameterViews == null) {
									holder.parameterViews = new HashMap<>();
								}
								holder.addSpecializationSpinner(row.getParameter());
							}
							break;
						case SPELL:
							if (row.getInitialValue() == -1) {
								if (holder.parameterViews == null) {
									holder.parameterViews = new HashMap<>();
								}
								holder.addSpellSpinner(row.getParameter());
							}
							break;
						case SPELL_LIST:
							if (row.getInitialValue() == -1) {
								if (holder.parameterViews == null) {
									holder.parameterViews = new HashMap<>();
								}
								holder.addSpellListSpinner(row.getParameter());
							}
							break;
						case STAT:
							if (row.getInitialValue() == null || row.getInitialValue() == -1) {
								if (holder.parameterViews == null) {
									holder.parameterViews = new HashMap<>();
								}
								holder.addStatSpinner(row.getParameter());
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

		private void addAttackSpinner(final Parameter parameter) {
			final Spinner spinner = new Spinner(getContext());
			parameterViews.put(spinner, parameter);
			parametersLayout.addView(spinner);
			final ArrayAdapter<Attack> adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_row);
			if(attacksList != null) {
				adapter.addAll(attacksList);
				spinner.setAdapter(adapter);
			}
			else {
				attackRxHandler.getAll()
						.subscribe(new Subscriber<Collection<Attack>>() {
							@Override
							public void onCompleted() {}
							@Override
							public void onError(Throwable e) {
								Log.e(TAG, "Exception caught getting non-specialization attacks.", e);
							}
							@Override
							public void onNext(Collection<Attack> attackCollection) {
								attacksList = attackCollection;
								adapter.addAll(attacksList);
								spinner.setAdapter(adapter);
							}
						});
			}

			spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					short tiers = Short.valueOf(tiersView.getText().toString());
					if(tiers > 0) {
						callbacks.setParameterValue(talentTier.getTalent(), parameter,
													((Attack)spinner.getSelectedItem()).getId(), null);
					}
				}
				@Override
				public void onNothingSelected(AdapterView<?> parent) {}
			});
		}

		private void addResistanceSpinner(final Parameter parameter) {
			final Spinner spinner = new Spinner(getContext());
			parameterViews.put(spinner, parameter);
			parametersLayout.addView(spinner);
			final ArrayAdapter<Resistance> adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_row);
			adapter.addAll(Resistance.values());
			spinner.setAdapter(adapter);

			spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					short tiers = Short.valueOf(tiersView.getText().toString());
					if(tiers > 0) {
						callbacks.setParameterValue(talentTier.getTalent(), parameter, 0,
													((Resistance)spinner.getSelectedItem()).name());
					}
				}
				@Override
				public void onNothingSelected(AdapterView<?> parent) {}
			});
		}

		private void addSenseSpinner(final Parameter parameter) {
			final Spinner spinner = new Spinner(getContext());
			parameterViews.put(spinner, parameter);
			parametersLayout.addView(spinner);
			final ArrayAdapter<Sense> adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_row);
			adapter.addAll(Sense.getNoChoiceSenses());
			spinner.setAdapter(adapter);

			spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					short tiers = Short.valueOf(tiersView.getText().toString());
					if(tiers > 0) {
						callbacks.setParameterValue(talentTier.getTalent(), parameter, 0,
													((Sense)spinner.getSelectedItem()).name());
					}
				}
				@Override
				public void onNothingSelected(AdapterView<?> parent) {}
			});
		}

		private void addSkillSpinner(final Parameter parameter) {
			final Spinner spinner = new Spinner(getContext());
			parameterViews.put(spinner, parameter);
			parametersLayout.addView(spinner);
			final ArrayAdapter<Skill> adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_row);
			if(skillsList != null) {
				adapter.addAll(skillsList);
				spinner.setAdapter(adapter);
			}
			else {
				skillRxHandler.getNonSpecializationSkills()
						.subscribe(new Subscriber<Collection<Skill>>() {
							@Override
							public void onCompleted() {}
							@Override
							public void onError(Throwable e) {
								Log.e(TAG, "Exception caught getting non-specialization skills.", e);
							}
							@Override
							public void onNext(Collection<Skill> skillCollection) {
								skillsList = skillCollection;
								adapter.addAll(skillsList);
								spinner.setAdapter(adapter);
							}
						});
			}

			spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					short tiers = Short.valueOf(tiersView.getText().toString());
					if(tiers > 0) {
						callbacks.setParameterValue(talentTier.getTalent(), parameter,
													((Skill)spinner.getSelectedItem()).getId(), null);
					}
				}
				@Override
				public void onNothingSelected(AdapterView<?> parent) {}
			});
		}

		private void addSpecializationSpinner(final Parameter parameter) {
			final Spinner spinner = new Spinner(getContext());
			parameterViews.put(spinner, parameter);
			parametersLayout.addView(spinner);
			final ArrayAdapter<Specialization> adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_row);
			if(specializationsList != null) {
				adapter.addAll(specializationsList);
				spinner.setAdapter(adapter);
			}
			else {
				specializationRxHandler.getCharacterPurchasableSpecializations()
						.subscribe(new Subscriber<Collection<Specialization>>() {
							@Override
							public void onCompleted() {}
							@Override
							public void onError(Throwable e) {
								Log.e(TAG, "Exception caught getting character purchasable Specializations.", e);
							}
							@Override
							public void onNext(Collection<Specialization> specializationCollection) {
								specializationsList = specializationCollection;
								adapter.addAll(specializationsList);
								spinner.setAdapter(adapter);
							}
						});
			}

			spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					short tiers = Short.valueOf(tiersView.getText().toString());
					if(tiers > 0) {
						callbacks.setParameterValue(talentTier.getTalent(), parameter,
													((Specialization)spinner.getSelectedItem()).getId(), null);
					}
				}
				@Override
				public void onNothingSelected(AdapterView<?> parent) {}
			});
		}

		private void addSpellSpinner(final Parameter parameter) {
			final Spinner spinner = new Spinner(getContext());
			parameterViews.put(spinner, parameter);
			parametersLayout.addView(spinner);
			final ArrayAdapter<Spell> adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_row);
			if(spellsList != null) {
				adapter.addAll(spellsList);
				spinner.setAdapter(adapter);
			}
			else {
				spellRxHandler.getAll()
						.subscribe(new Subscriber<Collection<Spell>>() {
							@Override
							public void onCompleted() {}
							@Override
							public void onError(Throwable e) {
								Log.e(TAG, "Exception caught getting spells.", e);
							}
							@Override
							public void onNext(Collection<Spell> spellCollection) {
								spellsList = spellCollection;
								adapter.addAll(spellsList);
								spinner.setAdapter(adapter);
							}
						});
			}

			spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					short tiers = Short.valueOf(tiersView.getText().toString());
					if(tiers > 0) {
						callbacks.setParameterValue(talentTier.getTalent(), parameter,
													((Spell)spinner.getSelectedItem()).getId(), null);
					}
				}
				@Override
				public void onNothingSelected(AdapterView<?> parent) {}
			});
		}

		private void addSpellListSpinner(final Parameter parameter) {
			final Spinner spinner = new Spinner(getContext());
			parameterViews.put(spinner, parameter);
			parametersLayout.addView(spinner);
			final ArrayAdapter<SpellList> adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_row);
			if(spellListsList != null) {
				adapter.addAll(spellListsList);
				spinner.setAdapter(adapter);
			}
			else {
				spellListRxHandler.getAll()
						.subscribe(new Subscriber<Collection<SpellList>>() {
							@Override
							public void onCompleted() {}
							@Override
							public void onError(Throwable e) {
								Log.e(TAG, "Exception caught getting all spellLists.", e);
							}
							@Override
							public void onNext(Collection<SpellList> spellListCollection) {
								spellListsList = spellListCollection;
								adapter.addAll(spellListsList);
								spinner.setAdapter(adapter);
							}
						});
			}

			spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					short tiers = Short.valueOf(tiersView.getText().toString());
					if(tiers > 0) {
						callbacks.setParameterValue(talentTier.getTalent(), parameter,
													((SpellList)spinner.getSelectedItem()).getId(), null);
					}
				}
				@Override
				public void onNothingSelected(AdapterView<?> parent) {}
			});
		}

		private void addStatSpinner(final Parameter parameter) {
			final Spinner spinner = new Spinner(getContext());
			parameterViews.put(spinner, parameter);
			parametersLayout.addView(spinner);
			final ArrayAdapter<Statistic> adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_row);
			adapter.addAll(Statistic.getAllStats());
			spinner.setAdapter(adapter);

			spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					short tiers = Short.valueOf(tiersView.getText().toString());
					if(tiers > 0) {
						callbacks.setParameterValue(talentTier.getTalent(), parameter, 0,
													((Statistic)spinner.getSelectedItem()).name());
					}
				}
				@Override
				public void onNothingSelected(AdapterView<?> parent) {}
			});
		}

		private void addSpinner(final Parameter parameter) {
			Log.d(TAG, "addSpinner: Adding spinner...........................");
			final Spinner spinner = new Spinner(getContext());
			parameterViews.put(spinner, parameter);
			parametersLayout.addView(spinner);
			final ArrayAdapter<Object> adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_row);
			if(parameterCollectionsCache.get(parameter) != null) {
				Log.d(TAG, "addSpinner: count = " + adapter.getCount());
				adapter.addAll(parameterCollectionsCache.get(parameter));
				spinner.setAdapter(adapter);
			}
			else if(parameter.getEnumType() != null) {
				Log.d(TAG, "addSpinner: count = " + parameter.getEnumType().getDeclaringClass().getEnumConstants().length);
				adapter.addAll((parameter.getEnumType().getDeclaringClass()).getEnumConstants());
			}
			else if(parameter.getHandler() != null) {
				new ReactiveUtils().getGetAllObservable(parameter.getHandler())
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
								adapter.addAll(results);
								spinner.setAdapter(adapter);
							}
						});
			}

			spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					short tiers = Short.valueOf(tiersView.getText().toString());
					if(tiers > 0) {
						callbacks.setParameterValue(talentTier.getTalent(), parameter,
													((Specialization)spinner.getSelectedItem()).getId(), null);
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
