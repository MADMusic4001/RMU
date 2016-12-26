/**
 * Copyright (C) 2016 MadInnovations
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
package com.madinnovations.rmu.view.activities.character;

import android.app.Fragment;
import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.controller.rxhandler.combat.AttackRxHandler;
import com.madinnovations.rmu.controller.rxhandler.common.SkillRxHandler;
import com.madinnovations.rmu.controller.rxhandler.common.SpecializationRxHandler;
import com.madinnovations.rmu.controller.rxhandler.common.TalentRxHandler;
import com.madinnovations.rmu.controller.rxhandler.spell.SpellListRxHandler;
import com.madinnovations.rmu.controller.rxhandler.spell.SpellRxHandler;
import com.madinnovations.rmu.controller.utils.ReactiveUtils;
import com.madinnovations.rmu.data.entities.common.Parameter;
import com.madinnovations.rmu.data.entities.common.Talent;
import com.madinnovations.rmu.data.entities.common.TalentCategory;
import com.madinnovations.rmu.data.entities.common.TalentInstance;
import com.madinnovations.rmu.data.entities.common.TalentParameterRow;
import com.madinnovations.rmu.view.RMUDragShadowBuilder;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.adapters.common.TalentTierListAdapter;
import com.madinnovations.rmu.view.di.modules.CharacterFragmentModule;
import com.madinnovations.rmu.view.utils.Boast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Handles interactions with the UI for Race talents.
 */
public class RacesTalentsPageFragment extends Fragment implements TalentTierListAdapter.TalentTiersAdapterCallbacks {
	@SuppressWarnings("unused")
	private static final String TAG = "RacesTalentsPageFrag";
	private static final String DRAG_ADD_TALENT = "drag-add-talent";
	private static final String DRAG_REMOVE_TALENT = "drag-remove-talent";
	@Inject
	protected AttackRxHandler              attackRxHandler;
	@Inject
	protected SkillRxHandler               skillRxHandler;
	@Inject
	protected SpecializationRxHandler      specializationRxHandler;
	@Inject
	protected SpellRxHandler               spellRxHandler;
	@Inject
	protected SpellListRxHandler           spellListRxHandler;
	@Inject
	protected TalentRxHandler              talentRxHandler;
	@Inject
	protected ReactiveUtils                reactiveUtils;
	private   ArrayAdapter<TalentCategory> talentCategoryArrayAdapter;
	private   Spinner                      talentCategoryFilterSpinner;
	private   ArrayAdapter<Talent>         addTalentsListAdapter;
	private   TalentTierListAdapter        talentTiersAdapter;
	private   RacesFragment                racesFragment;

	/**
	 * Creates a new RacesTalentsPageFragment instance.
	 *
	 * @param racesFragment  the RacesFragment instance this fragment is attached to.
	 * @return the new instance.
	 */
	public static RacesTalentsPageFragment newInstance(RacesFragment racesFragment) {
		RacesTalentsPageFragment fragment = new RacesTalentsPageFragment();
		fragment.racesFragment = racesFragment;
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((CampaignActivity) getActivity()).getActivityComponent().
				newCharacterFragmentComponent(new CharacterFragmentModule(this)).injectInto(this);

		View layout = inflater.inflate(R.layout.races_talents_page, container, false);

		initTalentCategoryFilterSpinner(layout);
		initAddTalentsListView(layout);
		initTalentListView(layout);
		return layout;
	}

	@Override
	public void onPause() {
		if(copyViewsToItem()) {
			racesFragment.saveItem();
		}
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		copyItemToViews();
	}

	@Override
	public short purchaseTier(TalentInstance talentInstance) {
		short result = talentInstance.getTiers();

		if(result < talentInstance.getTalent().getMaxTier()) {
			talentInstance.setTiers(++result);
			racesFragment.saveItem();
		}

		return result;
	}

	@Override
	public short sellTier(TalentInstance talentInstance) {
		short result = talentInstance.getTiers();

		if(result > 0) {
			result--;
			if(result > 0) {
				talentInstance.setTiers(result);
			}
			else {
				racesFragment.getCurrentInstance().getTalentsAndFlawsList().remove(talentInstance);
			}
			racesFragment.saveItem();
		}

		return result;
	}

	@Override
	public void setParameterValue(TalentInstance talentInstance, Parameter parameter, int value, String enumName) {
		Object paramValue = talentInstance.getParameterValues().get(parameter);
		if(enumName != null && !enumName.equals(paramValue)) {
			talentInstance.getParameterValues().put(parameter, enumName);
			racesFragment.saveItem();
		}
		else if(enumName == null && (paramValue == null || !Integer.valueOf(value).equals(paramValue))) {
			talentInstance.getParameterValues().put(parameter, value);
			racesFragment.saveItem();
		}
	}

	@Override
	public short getTiers(TalentInstance talentInstance) {
		return talentInstance.getTiers();
	}

	@Override
	public short getTiersThisLevel(TalentInstance talentInstance) {
		return talentInstance.getTiers();
	}

	// <editor-fold desc="Copy to/from views/entity methods">
	public boolean copyViewsToItem() {
		return false;
	}

	public void copyItemToViews() {
		talentTiersAdapter.clear();
		talentTiersAdapter.addAll(racesFragment.getCurrentInstance().getTalentsAndFlawsList());
		talentTiersAdapter.notifyDataSetChanged();
	}
	// </editor-fold>

	private void initTalentCategoryFilterSpinner(View layout) {
		talentCategoryFilterSpinner = (Spinner) layout.findViewById(R.id.talent_category_filter_spinner);
		talentCategoryArrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.single_field_row);
		talentCategoryFilterSpinner.setAdapter(talentCategoryArrayAdapter);

		final TalentCategory allCategories = new TalentCategory();
		allCategories.setName(getString(R.string.label_all_talent_categories));
		talentCategoryArrayAdapter.clear();
		talentCategoryArrayAdapter.add(allCategories);
		//noinspection unchecked
		((Observable<Collection<TalentCategory>>)reactiveUtils
				.getGetAllObservable(ReactiveUtils.Handler.TALENT_CATEGORY_RX_HANDLER))
				.subscribe(new Subscriber<Collection<TalentCategory>>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e(TAG, "initTalentCategoryFilterSpinner: Exception caught getting all TalentCategory instances", e);
					}
					@Override
					public void onNext(Collection<TalentCategory> talentCategories) {
						talentCategoryArrayAdapter.addAll(talentCategories);
						talentCategoryFilterSpinner.setSelection(talentCategoryArrayAdapter.getPosition(allCategories));
						talentCategoryArrayAdapter.notifyDataSetChanged();
					}
				});

		talentCategoryFilterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
				loadFilteredTalents(talentCategoryArrayAdapter.getItem(position));
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				loadFilteredTalents(null);
			}
		});
	}

	private void initAddTalentsListView(View layout) {
		final ListView addTalentsList = (ListView) layout.findViewById(R.id.add_talent_list);
		addTalentsListAdapter = new ArrayAdapter<>(getActivity(), R.layout.single_field_row);

		loadFilteredTalents(null);

		addTalentsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
				ClipData dragData = null;

				List<View> checkedViews = new ArrayList<>(1);
				Talent talent = addTalentsListAdapter.getItem(position);
				if(talent != null) {
					String talentIdString = String.valueOf(talent.getId());
					ClipData.Item clipDataItem = new ClipData.Item(talentIdString);
					dragData = new ClipData(DRAG_ADD_TALENT, new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, clipDataItem);
					checkedViews.add(view);
				}
				View.DragShadowBuilder myShadow = new RMUDragShadowBuilder(checkedViews);

				if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
					view.startDragAndDrop(dragData, myShadow, null, 0);
				}
				else {
					//noinspection deprecation
					view.startDrag(dragData, myShadow, null, 0);
				}
				return false;
			}
		});

		addTalentsList.setOnDragListener(new View.OnDragListener() {
			private Drawable targetShape = ResourcesCompat.getDrawable(getActivity().getResources(), R.drawable.drag_target_background, null);
			private Drawable hoverShape  = ResourcesCompat.getDrawable(getActivity().getResources(), R.drawable.drag_hover_background, null);
			private Drawable normalShape = addTalentsList.getBackground();

			@Override
			public boolean onDrag(View v, DragEvent event) {
				final int action = event.getAction();

				switch (action) {
					case DragEvent.ACTION_DRAG_STARTED:
						if(event.getClipDescription() != null && DRAG_REMOVE_TALENT.equals(event.getClipDescription().getLabel())) {
							v.setBackground(targetShape);
							v.invalidate();
						}
						else {
							return false;
						}
						break;
					case DragEvent.ACTION_DRAG_ENTERED:
						if(event.getClipDescription() != null && DRAG_REMOVE_TALENT.equals(event.getClipDescription().getLabel())) {
							v.setBackground(hoverShape);
							v.invalidate();
						}
						else {
							return false;
						}
						break;
					case DragEvent.ACTION_DRAG_LOCATION:
						break;
					case DragEvent.ACTION_DRAG_EXITED:
						if(event.getClipDescription() != null && DRAG_REMOVE_TALENT.equals(event.getClipDescription().getLabel())) {
							v.setBackground(targetShape);
							v.invalidate();
						}
						else {
							return false;
						}
						break;
					case DragEvent.ACTION_DROP:
						if(event.getClipDescription() != null && DRAG_REMOVE_TALENT.equals(event.getClipDescription().getLabel())) {
							for (int i = 0; i < event.getClipData().getItemCount(); i++) {
								ClipData.Item item = event.getClipData().getItemAt(i);
								// We just send attack ID but since that is the only field used in the Attack.equals method and attack is the
								// only field used in the AttackBonus.equals method we can create a temporary Attack and set its id field then
								// create a new AttackBonus and set its attack field then use the new AttackBonus to find the position of the
								// complete AttackBonus instance in the adapter
								int talentId = Integer.valueOf(item.getText().toString());
								Talent newTalent = new Talent();
								newTalent.setId(talentId);
								int position = addTalentsListAdapter.getPosition(newTalent);
								if(position != -1) {
									Talent talent = addTalentsListAdapter.getItem(position);
									TalentInstance talentInstance = new TalentInstance();
									talentInstance.setTalent(talent);
									talentInstance.setTiers((short)0);
									if(talent != null) {
										for (TalentParameterRow parameterRow : talent.getTalentParameterRows()) {
											Object parameterValue;
											if (parameterRow.getEnumName() != null) {
												parameterValue = parameterRow.getEnumName();
											}
											else {
												parameterValue = parameterRow.getInitialValue();
											}
											talentInstance.getParameterValues().put(parameterRow.getParameter(), parameterValue);
										}
										racesFragment.saveItem();
										talentTiersAdapter.add(talentInstance);
										talentTiersAdapter.notifyDataSetChanged();
									}
								}
							}
							v.setBackground(normalShape);
							v.invalidate();
						}
						else {
							return false;
						}
						break;
					case DragEvent.ACTION_DRAG_ENDED:
						v.setBackground(normalShape);
						v.invalidate();
						break;
				}
				return true;
			}
		});
	}

	private void initTalentListView(View layout) {
		ListView talentsListView = (ListView) layout.findViewById(R.id.talents_list_view);
		talentTiersAdapter = new TalentTierListAdapter(getActivity(), this, reactiveUtils, true);
		talentsListView.setAdapter(talentTiersAdapter);

		talentTiersAdapter.clear();
		talentTiersAdapter.addAll(racesFragment.getCurrentInstance().getTalentsAndFlawsList());
		talentTiersAdapter.notifyDataSetChanged();
	}

	private void loadFilteredTalents(final TalentCategory filter) {
		Observable<Collection<Talent>> observable;

		if(filter == null || filter.getId() == -1) {
			//noinspection unchecked
			observable = (Observable<Collection<Talent>>)reactiveUtils
					.getGetAllObservable(ReactiveUtils.Handler.TALENT_RX_HANDLER);
		}
		else {
			observable = talentRxHandler.getTalentsForTalentCategory(filter);
		}
		observable.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Collection<Talent>>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e(TAG, "loadFilteredTalents: Exception caught getting Talent instances", e);
						Toast.makeText(getActivity(),
									   getString(R.string.toast_talents_load_failed),
									   Toast.LENGTH_SHORT).show();
					}
					@Override
					public void onNext(Collection<Talent> talents) {
						addTalentsListAdapter.clear();
						addTalentsListAdapter.addAll(talents);
						addTalentsListAdapter.notifyDataSetChanged();
						if(filter == null) {
							Boast.makeText(getActivity(),
										   String.format(getString(R.string.toast_talents_loaded), talents.size()),
										   Toast.LENGTH_SHORT).show(true);
						}
					}
				});
	}
}
