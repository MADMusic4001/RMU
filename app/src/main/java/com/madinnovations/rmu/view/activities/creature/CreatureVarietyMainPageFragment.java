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
package com.madinnovations.rmu.view.activities.creature;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.controller.rxhandler.combat.AttackRxHandler;
import com.madinnovations.rmu.controller.rxhandler.common.SkillRxHandler;
import com.madinnovations.rmu.controller.rxhandler.common.SpecializationRxHandler;
import com.madinnovations.rmu.controller.rxhandler.creature.CreatureTypeRxHandler;
import com.madinnovations.rmu.controller.rxhandler.creature.OutlookRxHandler;
import com.madinnovations.rmu.controller.rxhandler.spell.SpellListRxHandler;
import com.madinnovations.rmu.controller.rxhandler.spell.SpellRxHandler;
import com.madinnovations.rmu.controller.utils.ReactiveUtils;
import com.madinnovations.rmu.data.entities.common.Size;
import com.madinnovations.rmu.data.entities.creature.CreatureLevelSpreadTable;
import com.madinnovations.rmu.data.entities.creature.CreatureType;
import com.madinnovations.rmu.data.entities.creature.Outlook;
import com.madinnovations.rmu.data.entities.spells.Realm;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.di.modules.CreatureFragmentModule;
import com.madinnovations.rmu.view.utils.TextInputLayoutUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Handles interactions with the UI for creature varieties.
 */
public class CreatureVarietyMainPageFragment extends Fragment implements TextInputLayoutUtils.ValuesCallback {
	private static final String TAG = "CVMainPageFragment";
	@Inject
	protected AttackRxHandler            attackRxHandler;
	@Inject
	protected CreatureTypeRxHandler      creatureTypeRxHandler;
	@Inject
	protected OutlookRxHandler           outlookRxHandler;
	@Inject
	protected SkillRxHandler             skillRxHandler;
	@Inject
	protected SpecializationRxHandler    specializationRxHandler;
	@Inject
	protected SpellRxHandler             spellRxHandler;
	@Inject
	protected SpellListRxHandler         spellListRxHandler;
	@Inject
	protected ReactiveUtils              reactiveUtils;
	private   ArrayAdapter<CreatureType> creatureTypeSpinnerAdapter;
	private   ArrayAdapter<String>       sizeSpinnerAdapter;
	private   ArrayAdapter<Character>    levelSpreadSpinnerAdapter;
	private   ArrayAdapter<String>       realm1SpinnerAdapter;
	private   ArrayAdapter<String>       realm2SpinnerAdapter;
	private   ArrayAdapter<Outlook>      outlookSpinnerAdapter;
	private   EditText                   nameEdit;
	private   EditText                   descriptionEdit;
	private   Spinner                    creatureTypeSpinner;
	private   EditText                   typicalLevelEdit;
	private   Spinner                    levelSpreadSpinner;
	private   Spinner                    sizeSpinner;
	private   EditText                   heightEdit;
	private   EditText                   lengthEdit;
	private   EditText                   weightEdit;
	private   EditText                   healingRateEdit;
	private   EditText                   baseHitsEdit;
	private   EditText                   baseEnduranceEdit;
	private   EditText                   armorTypeEdit;
	private   EditText                   channelingRREdit;
	private   EditText                   essenceRREdit;
	private   EditText                   mentalismRREdit;
	private   EditText                   physicalRREdit;
	private   EditText                   fearRREdit;
	private   Spinner                    realm1Spinner;
	private   Spinner                    realm2Spinner;
	private   Spinner                    outlookSpinner;
	private   CreatureVarietiesFragment  varietiesFragment;
	private   CreatureTypeComparator     creatureTypeComparator = new CreatureTypeComparator();
	private   String                     noRealmName = null;

	/**
	 * Creates a new CreatureVarietyMainPageFragment instance.
	 *
	 * @param varietiesFragment  the CreatureVarietiesFragment instance this fragment is attached to.
	 * @return the new instance.
	 */
	public static CreatureVarietyMainPageFragment newInstance(CreatureVarietiesFragment varietiesFragment) {
		CreatureVarietyMainPageFragment fragment = new CreatureVarietyMainPageFragment();
		fragment.varietiesFragment = varietiesFragment;
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((CampaignActivity)getActivity()).getActivityComponent().
				newCreatureFragmentComponent(new CreatureFragmentModule(this)).injectInto(this);

		noRealmName = getString(R.string.label_no_realm);

		View layout = inflater.inflate(R.layout.creature_variety_main_page, container, false);

		nameEdit = TextInputLayoutUtils.initEdit(layout, this.getActivity(), this, R.id.name_textInputLayout, R.id.name_edit,
												 R.string.validation_creature_variety_name_required);
		descriptionEdit = TextInputLayoutUtils.initEdit(layout, this.getActivity(), this, R.id.notes_edit_textInputLayout,
														R.id.notes_edit,
														R.string.validation_creature_variety_description_required);
		typicalLevelEdit = TextInputLayoutUtils.initEdit(layout, this.getActivity(), this, R.id.typical_level_textInputLayout,
														R.id.typical_level_edit,
														R.string.validation_creature_variety_typical_level_required);
		heightEdit = TextInputLayoutUtils.initEdit(layout, this.getActivity(), this, R.id.height_textInputLayout,
														 R.id.height_edit,
														 R.string.validation_creature_variety_height_required);
		lengthEdit = TextInputLayoutUtils.initEdit(layout, this.getActivity(), this, R.id.length_textInputLayout,
												   R.id.length_edit,
												   R.string.validation_creature_variety_length_required);
		weightEdit = TextInputLayoutUtils.initEdit(layout, this.getActivity(), this, R.id.weight_textInputLayout,
												   R.id.weight_edit,
												   R.string.validation_creature_variety_weight_required);
		healingRateEdit = TextInputLayoutUtils.initEdit(layout, this.getActivity(), this, R.id.healing_rate_textInputLayout,
												   R.id.healing_rate_edit,
												   R.string.validation_creature_variety_healing_rate_required);
		baseHitsEdit = TextInputLayoutUtils.initEdit(layout, this.getActivity(), this, R.id.base_hits_textInputLayout,
														R.id.base_hits_edit,
														R.string.validation_creature_variety_base_hits_required);
		baseEnduranceEdit = TextInputLayoutUtils.initEdit(layout, this.getActivity(), this, R.id.base_endurance_textInputLayout,
													 R.id.base_endurance_edit,
													 R.string.validation_creature_variety_base_endurance_required);
		armorTypeEdit = TextInputLayoutUtils.initEdit(layout, this.getActivity(), this, R.id.armor_type_textInputLayout,
														  R.id.armor_type_edit,
														  R.string.validation_creature_variety_armor_type_required);
		channelingRREdit = TextInputLayoutUtils.initEdit(layout, this.getActivity(), this,
														 R.id.base_channeling_rr_textInputLayout,
														 R.id.base_channeling_rr_edit,
														 R.string.validation_creature_variety_channeling_rr_required);
		essenceRREdit = TextInputLayoutUtils.initEdit(layout, this.getActivity(), this,
														 R.id.base_essence_rr_textInputLayout,
														 R.id.base_essence_rr_edit,
														 R.string.validation_creature_variety_essence_rr_required);
		mentalismRREdit = TextInputLayoutUtils.initEdit(layout, this.getActivity(), this,
														R.id.base_mentalism_rr_textInputLayout,
													    R.id.base_mentalism_rr_edit,
													    R.string.validation_creature_variety_mentalism_rr_required);
		physicalRREdit = TextInputLayoutUtils.initEdit(layout, this.getActivity(), this,
														R.id.base_physical_rr_textInputLayout,
														R.id.base_physical_rr_edit,
														R.string.validation_creature_variety_physical_rr_required);
		fearRREdit = TextInputLayoutUtils.initEdit(layout, this.getActivity(), this,
													   R.id.base_fear_rr_textInputLayout,
													   R.id.base_fear_rr_edit,
													   R.string.validation_creature_variety_fear_rr_required);
		initCreatureTypeSpinner(layout);
		initLevelSpreadSpinner(layout);
		initSizeSpinner(layout);
		initFearRREdit(layout);
		initRealm1Spinner(layout);
		initRealm2Spinner(layout);
		initOutlookSpinner(layout);

		return layout;
	}

	@Override
	public void onPause() {
		if(copyViewsToItem()) {
			varietiesFragment.saveItem();
		}
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		copyItemToViews();
	}

	@Override
	public String getValueForEditText(@IdRes int editTextId) {
		String result = "";

		switch (editTextId) {
			case R.id.name_edit:
				result = varietiesFragment.getCurrentInstance().getName();
				break;
			case R.id.notes_edit:
				result = varietiesFragment.getCurrentInstance().getDescription();
				break;
			case R.id.typical_level_edit:
				result = String.valueOf(varietiesFragment.getCurrentInstance().getTypicalLevel());
				break;
			case R.id.height_edit:
				result = String.valueOf(varietiesFragment.getCurrentInstance().getHeight());
				break;
			case R.id.length_edit:
				result = String.valueOf(varietiesFragment.getCurrentInstance().getLength());
				break;
			case R.id.weight_edit:
				result = String.valueOf(varietiesFragment.getCurrentInstance().getWeight());
				break;
			case R.id.healing_rate_edit:
				result = String.valueOf(varietiesFragment.getCurrentInstance().getHealingRate());
				break;
			case R.id.base_hits_edit:
				result = String.valueOf(varietiesFragment.getCurrentInstance().getBaseHits());
				break;
			case R.id.base_endurance_edit:
				result = String.valueOf(varietiesFragment.getCurrentInstance().getBaseEndurance());
				break;
			case R.id.armor_type_edit:
				result = String.valueOf(varietiesFragment.getCurrentInstance().getArmorType());
				break;
			case R.id.base_channeling_rr_edit:
				result = String.valueOf(varietiesFragment.getCurrentInstance().getBaseChannellingRR());
				break;
			case R.id.base_essence_rr_edit:
				result = String.valueOf(varietiesFragment.getCurrentInstance().getBaseEssenceRR());
				break;
			case R.id.base_mentalism_rr_edit:
				result = String.valueOf(varietiesFragment.getCurrentInstance().getBaseMentalismRR());
				break;
			case R.id.base_physical_rr_edit:
				result = String.valueOf(varietiesFragment.getCurrentInstance().getBasePhysicalRR());
				break;
			case R.id.base_fear_rr_edit:
				result = String.valueOf(varietiesFragment.getCurrentInstance().getBaseFearRR());
				break;
		}

		return result;
	}

	@Override
	public void setValueFromEditText(@IdRes int editTextId, String newString) {
		switch (editTextId) {
			case R.id.name_edit:
				varietiesFragment.getCurrentInstance().setName(newString);
				varietiesFragment.saveItem();
				break;
			case R.id.notes_edit:
				varietiesFragment.getCurrentInstance().setDescription(newString);
				varietiesFragment.saveItem();
				break;
			case R.id.typical_level_edit:
				varietiesFragment.getCurrentInstance().setTypicalLevel(Short.valueOf(newString));
				varietiesFragment.saveItem();
				break;
			case R.id.height_edit:
				varietiesFragment.getCurrentInstance().setHeight(Short.valueOf(newString));
				varietiesFragment.saveItem();
				break;
			case R.id.length_edit:
				varietiesFragment.getCurrentInstance().setLength(Short.valueOf(newString));
				varietiesFragment.saveItem();
				break;
			case R.id.weight_edit:
				varietiesFragment.getCurrentInstance().setWeight(Short.valueOf(newString));
				varietiesFragment.saveItem();
				break;
			case R.id.healing_rate_edit:
				varietiesFragment.getCurrentInstance().setHealingRate(Short.valueOf(newString));
				varietiesFragment.saveItem();
				break;
			case R.id.base_hits_edit:
				varietiesFragment.getCurrentInstance().setBaseHits(Short.valueOf(newString));
				varietiesFragment.saveItem();
				break;
			case R.id.base_endurance_edit:
				varietiesFragment.getCurrentInstance().setBaseEndurance(Short.valueOf(newString));
				varietiesFragment.saveItem();
				break;
			case R.id.armor_type_edit:
				varietiesFragment.getCurrentInstance().setArmorType(Short.valueOf(newString));
				varietiesFragment.saveItem();
				break;
			case R.id.base_channeling_rr_edit:
				varietiesFragment.getCurrentInstance().setBaseChannellingRR(Short.valueOf(newString));
				varietiesFragment.saveItem();
				break;
			case R.id.base_essence_rr_edit:
				varietiesFragment.getCurrentInstance().setBaseEssenceRR(Short.valueOf(newString));
				varietiesFragment.saveItem();
				break;
			case R.id.base_mentalism_rr_edit:
				varietiesFragment.getCurrentInstance().setBaseMentalismRR(Short.valueOf(newString));
				varietiesFragment.saveItem();
				break;
			case R.id.base_physical_rr_edit:
				varietiesFragment.getCurrentInstance().setBasePhysicalRR(Short.valueOf(newString));
				varietiesFragment.saveItem();
				break;
			case R.id.base_fear_rr_edit:
				varietiesFragment.getCurrentInstance().setBaseFearRR(Short.valueOf(newString));
				varietiesFragment.saveItem();
				break;
		}
	}

	@Override
	public boolean performLongClick(@IdRes int editTextId) {
		return false;
	}

	@SuppressWarnings("ConstantConditions")
	public boolean copyViewsToItem() {
		boolean changed = false;
		String newString;
		int position;
		CreatureType newType;
		short newShort;
		char newLevelSpread;
		Outlook newOutlook;

		if(this.getView() != null) {
			if (nameEdit != null) {
				newString = nameEdit.getText().toString();
				if (newString.isEmpty()) {
					newString = null;
				}
				if ((newString == null && varietiesFragment.getCurrentInstance().getName() != null) ||
						(newString != null && !newString.equals(varietiesFragment.getCurrentInstance().getName()))) {
					varietiesFragment.getCurrentInstance().setName(newString);
					changed = true;
				}
			}

			if (descriptionEdit != null) {
				newString = descriptionEdit.getText().toString();
				if (newString.isEmpty()) {
					newString = null;
				}
				if ((newString == null && varietiesFragment.getCurrentInstance().getDescription() != null) ||
						(newString != null && !newString.equals(varietiesFragment.getCurrentInstance().getDescription()))) {
					varietiesFragment.getCurrentInstance().setDescription(newString);
					changed = true;
				}
			}

			position = creatureTypeSpinner.getSelectedItemPosition();
			if (position != -1) {
				newType = creatureTypeSpinnerAdapter.getItem(position);
				if (newType != null && !newType.equals(varietiesFragment.getCurrentInstance().getType())) {
					varietiesFragment.getCurrentInstance().setType(newType);
					changed = true;
				}
			}

			if (typicalLevelEdit.length() > 0) {
				newShort = Short.valueOf(typicalLevelEdit.getText().toString());
				if (varietiesFragment.getCurrentInstance().getTypicalLevel() != newShort) {
					varietiesFragment.getCurrentInstance().setTypicalLevel(newShort);
					changed = true;
				}
			}

			position = levelSpreadSpinner.getSelectedItemPosition();
			if (position != -1) {
				if(levelSpreadSpinnerAdapter.getItem(position) != null) {
					newLevelSpread = levelSpreadSpinnerAdapter.getItem(position);
					if (newLevelSpread != varietiesFragment.getCurrentInstance().getLevelSpread()) {
						varietiesFragment.getCurrentInstance().setLevelSpread(newLevelSpread);
						changed = true;
					}
				}
			}

			position = sizeSpinner.getSelectedItemPosition();
			if (position != -1) {
				newString = sizeSpinnerAdapter.getItem(position);
				Size newSize = Size.getSizeWithName(newString);
				if (newSize != null && !newSize.equals(varietiesFragment.getCurrentInstance().getSize())) {
					varietiesFragment.getCurrentInstance().setSize(newSize);
					changed = true;
				}
			}

			if (heightEdit.length() > 0) {
				newShort = Short.valueOf(heightEdit.getText().toString());
				if (varietiesFragment.getCurrentInstance().getHeight() != newShort) {
					varietiesFragment.getCurrentInstance().setHeight(newShort);
					changed = true;
				}
			}

			if (lengthEdit.length() > 0) {
				newShort = Short.valueOf(lengthEdit.getText().toString());
				if (varietiesFragment.getCurrentInstance().getLength() != newShort) {
					varietiesFragment.getCurrentInstance().setLength(newShort);
					changed = true;
				}
			}

			if (weightEdit.length() > 0) {
				newShort = Short.valueOf(weightEdit.getText().toString());
				if (varietiesFragment.getCurrentInstance().getWeight() != newShort) {
					varietiesFragment.getCurrentInstance().setWeight(newShort);
					changed = true;
				}
			}

			if (healingRateEdit.length() > 0) {
				newShort = Short.valueOf(healingRateEdit.getText().toString());
				if (varietiesFragment.getCurrentInstance().getHealingRate() != newShort) {
					varietiesFragment.getCurrentInstance().setHealingRate(newShort);
					changed = true;
				}
			}

			if (baseHitsEdit.length() > 0) {
				newShort = Short.valueOf(baseHitsEdit.getText().toString());
				if (varietiesFragment.getCurrentInstance().getBaseHits() != newShort) {
					varietiesFragment.getCurrentInstance().setBaseHits(newShort);
					changed = true;
				}
			}

			if (baseEnduranceEdit.length() > 0) {
				newShort = Short.valueOf(baseEnduranceEdit.getText().toString());
				if (varietiesFragment.getCurrentInstance().getBaseEndurance() != newShort) {
					varietiesFragment.getCurrentInstance().setBaseEndurance(newShort);
					changed = true;
				}
			}

			if (armorTypeEdit.length() > 0) {
				newShort = Short.valueOf(armorTypeEdit.getText().toString());
				if (varietiesFragment.getCurrentInstance().getArmorType() != newShort) {
					varietiesFragment.getCurrentInstance().setArmorType(newShort);
					changed = true;
				}
			}

			if (channelingRREdit.length() > 0) {
				newShort = Short.valueOf(channelingRREdit.getText().toString());
				if (varietiesFragment.getCurrentInstance().getBaseChannellingRR() != newShort) {
					varietiesFragment.getCurrentInstance().setBaseChannellingRR(newShort);
					changed = true;
				}
			}

			if (essenceRREdit.length() > 0) {
				newShort = Short.valueOf(essenceRREdit.getText().toString());
				if (varietiesFragment.getCurrentInstance().getBaseEssenceRR() != newShort) {
					varietiesFragment.getCurrentInstance().setBaseEssenceRR(newShort);
					changed = true;
				}
			}

			if (mentalismRREdit.length() > 0) {
				newShort = Short.valueOf(mentalismRREdit.getText().toString());
				if (varietiesFragment.getCurrentInstance().getBaseMentalismRR() != newShort) {
					varietiesFragment.getCurrentInstance().setBaseMentalismRR(newShort);
					changed = true;
				}
			}

			if (physicalRREdit.length() > 0) {
				newShort = Short.valueOf(physicalRREdit.getText().toString());
				if (varietiesFragment.getCurrentInstance().getBasePhysicalRR() != newShort) {
					varietiesFragment.getCurrentInstance().setBasePhysicalRR(newShort);
					changed = true;
				}
			}

			if (fearRREdit.length() > 0) {
				newShort = Short.valueOf(fearRREdit.getText().toString());
				if (varietiesFragment.getCurrentInstance().getBaseFearRR() != newShort) {
					varietiesFragment.getCurrentInstance().setBaseFearRR(newShort);
					changed = true;
				}
			}

			position = realm1Spinner.getSelectedItemPosition();
			if (position != -1) {
				newString = realm1SpinnerAdapter.getItem(position);
				Realm newRealm = Realm.getRealmWithName(newString);
				if (newRealm != null && !newRealm.equals(varietiesFragment.getCurrentInstance().getRealm1())) {
					varietiesFragment.getCurrentInstance().setRealm1(newRealm);
					changed = true;
				}
			}

			position = realm2Spinner.getSelectedItemPosition();
			if (position != -1) {
				newString = realm2SpinnerAdapter.getItem(position);
				Realm newRealm = Realm.getRealmWithName(newString);
				if (newRealm != null && !newRealm.equals(varietiesFragment.getCurrentInstance().getRealm2())) {
					varietiesFragment.getCurrentInstance().setRealm2(newRealm);
					changed = true;
				}
				else if (newRealm == null && varietiesFragment.getCurrentInstance().getRealm2() != null) {
					varietiesFragment.getCurrentInstance().setRealm2(null);
					changed = true;
				}
			}

			position = outlookSpinner.getSelectedItemPosition();
			if (position != -1) {
				newOutlook = outlookSpinnerAdapter.getItem(position);
				if (newOutlook != null && !newOutlook.equals(varietiesFragment.getCurrentInstance().getOutlook())) {
					varietiesFragment.getCurrentInstance().setOutlook(newOutlook);
					changed = true;
				}
			}
		}

		return changed;
	}

	public void copyItemToViews() {
		nameEdit.setText(varietiesFragment.getCurrentInstance().getName());
		creatureTypeSpinner.setSelection(creatureTypeSpinnerAdapter.getPosition(varietiesFragment.getCurrentInstance()
																						.getType()));
		typicalLevelEdit.setText(String.valueOf(varietiesFragment.getCurrentInstance().getTypicalLevel()));
		levelSpreadSpinner.setSelection(levelSpreadSpinnerAdapter.getPosition(varietiesFragment.getCurrentInstance()
																					  .getLevelSpread()));
		descriptionEdit.setText(varietiesFragment.getCurrentInstance().getDescription());
		if(varietiesFragment.getCurrentInstance().getSize() != null) {
			sizeSpinner.setSelection(sizeSpinnerAdapter.getPosition(varietiesFragment.getCurrentInstance().getSize().toString()));

		}
		heightEdit.setText(String.valueOf(varietiesFragment.getCurrentInstance().getHeight()));
		lengthEdit.setText(String.valueOf(varietiesFragment.getCurrentInstance().getLength()));
		weightEdit.setText(String.valueOf(varietiesFragment.getCurrentInstance().getWeight()));
		healingRateEdit.setText(String.valueOf(varietiesFragment.getCurrentInstance().getHealingRate()));
		baseHitsEdit.setText(String.valueOf(varietiesFragment.getCurrentInstance().getBaseHits()));
		baseEnduranceEdit.setText(String.valueOf(varietiesFragment.getCurrentInstance().getBaseEndurance()));
		armorTypeEdit.setText(String.valueOf(varietiesFragment.getCurrentInstance().getArmorType()));
		channelingRREdit.setText(String.valueOf(varietiesFragment.getCurrentInstance().getBaseChannellingRR()));
		essenceRREdit.setText(String.valueOf(varietiesFragment.getCurrentInstance().getBaseEssenceRR()));
		mentalismRREdit.setText(String.valueOf(varietiesFragment.getCurrentInstance().getBaseMentalismRR()));
		physicalRREdit.setText(String.valueOf(varietiesFragment.getCurrentInstance().getBasePhysicalRR()));
		fearRREdit.setText(String.valueOf(varietiesFragment.getCurrentInstance().getBaseFearRR()));
		if(varietiesFragment.getCurrentInstance().getRealm1() != null) {
			realm1Spinner.setSelection(realm1SpinnerAdapter.getPosition(
					varietiesFragment.getCurrentInstance().getRealm1().toString()));
		}
		if(varietiesFragment.getCurrentInstance().getRealm2() == null) {
			realm2Spinner.setSelection(realm2SpinnerAdapter.getPosition(noRealmName));
		}
		else {
			realm2Spinner.setSelection(realm2SpinnerAdapter.getPosition(
					varietiesFragment.getCurrentInstance().getRealm2().toString()));
		}
		outlookSpinner.setSelection(outlookSpinnerAdapter.getPosition(varietiesFragment.getCurrentInstance().getOutlook()));

		if(varietiesFragment.getCurrentInstance().getName() != null && !varietiesFragment.getCurrentInstance().getName().isEmpty()) {
			nameEdit.setError(null);
		}
		if(varietiesFragment.getCurrentInstance().getDescription() != null && !varietiesFragment.getCurrentInstance().getDescription().isEmpty()) {
			descriptionEdit.setError(null);
		}
	}

	private void initCreatureTypeSpinner(View layout) {
		creatureTypeSpinner = (Spinner)layout.findViewById(R.id.creature_type_spinner);
		creatureTypeSpinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.single_field_row);
		creatureTypeSpinner.setAdapter(creatureTypeSpinnerAdapter);

		creatureTypeRxHandler.getAll()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Collection<CreatureType>>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e(TAG, "Exception caught getting all CreatureType instances", e);
					}
					@Override
					public void onNext(Collection<CreatureType> items) {
						creatureTypeSpinnerAdapter.clear();
						creatureTypeSpinnerAdapter.addAll(items);
						creatureTypeSpinnerAdapter.sort(creatureTypeComparator);
						creatureTypeSpinnerAdapter.notifyDataSetChanged();
					}
				});

		creatureTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if(varietiesFragment.getCurrentInstance().getType() == null || creatureTypeSpinnerAdapter.getPosition(varietiesFragment.getCurrentInstance().getType()) != position) {
					varietiesFragment.getCurrentInstance().setType(creatureTypeSpinnerAdapter.getItem(position));
					varietiesFragment.saveItem();
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				if(varietiesFragment.getCurrentInstance().getType() != null) {
					varietiesFragment.getCurrentInstance().setType(null);
					varietiesFragment.saveItem();
				}
			}
		});
	}

	private void initLevelSpreadSpinner(View layout) {
		levelSpreadSpinner = (Spinner)layout.findViewById(R.id.level_spread_spinner);
		levelSpreadSpinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.single_field_row);
		levelSpreadSpinner.setAdapter(levelSpreadSpinnerAdapter);

		levelSpreadSpinnerAdapter.clear();
		levelSpreadSpinnerAdapter.addAll(Arrays.asList(CreatureLevelSpreadTable.LEVEL_SPREAD_CODES));
		levelSpreadSpinnerAdapter.notifyDataSetChanged();

		levelSpreadSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@SuppressWarnings("ConstantConditions")
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				char newLevelSpread = levelSpreadSpinnerAdapter.getItem(position);
				if (newLevelSpread != varietiesFragment.getCurrentInstance().getLevelSpread()) {
					varietiesFragment.getCurrentInstance().setLevelSpread(newLevelSpread);
					varietiesFragment.saveItem();
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});
	}

	private void initSizeSpinner(View layout) {
		sizeSpinner = (Spinner)layout.findViewById(R.id.size_spinner);
		sizeSpinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.single_field_row);
		sizeSpinner.setAdapter(sizeSpinnerAdapter);

		sizeSpinnerAdapter.addAll(Size.getSizeStrings());
		sizeSpinnerAdapter.notifyDataSetChanged();

		sizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String newSizeName = sizeSpinnerAdapter.getItem(position);
				Size newSize = Size.getSizeWithName(newSizeName);
				if(newSize != null && !newSize.equals(varietiesFragment.getCurrentInstance().getSize())) {
					varietiesFragment.getCurrentInstance().setSize(newSize);
					varietiesFragment.saveItem();
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				if(varietiesFragment.getCurrentInstance().getSize() != null) {
					varietiesFragment.getCurrentInstance().setSize(null);
					varietiesFragment.saveItem();
				}
			}
		});
	}

	private void initFearRREdit(View layout) {
		fearRREdit = (EditText)layout.findViewById(R.id.base_fear_rr_edit);
		fearRREdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length() == 0) {
					fearRREdit.setError(getString(R.string.validation_creature_variety_fear_rr_required));
				}
			}
		});
		fearRREdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					if(fearRREdit.length() > 0) {
						final short newShort = Short.valueOf(fearRREdit.getText().toString());
						if (newShort != varietiesFragment.getCurrentInstance().getBaseFearRR()) {
							varietiesFragment.getCurrentInstance().setBaseFearRR(newShort);
							varietiesFragment.saveItem();
						}
					}
				}
			}
		});
	}

	private void initRealm1Spinner(View layout) {
		realm1Spinner = (Spinner)layout.findViewById(R.id.realm1_spinner);
		realm1SpinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.single_field_row);
		realm1Spinner.setAdapter(realm1SpinnerAdapter);

		realm1SpinnerAdapter.add(Realm.CHANNELING.toString());
		realm1SpinnerAdapter.add(Realm.ESSENCE.toString());
		realm1SpinnerAdapter.add(Realm.MENTALISM.toString());
		realm1SpinnerAdapter.notifyDataSetChanged();

		realm1Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String newRealmName = realm1SpinnerAdapter.getItem(position);
				Realm newRealm = Realm.getRealmWithName(newRealmName);
				if(newRealm != null && !newRealm.equals(varietiesFragment.getCurrentInstance().getRealm1())) {
					varietiesFragment.getCurrentInstance().setRealm1(newRealm);
					varietiesFragment.saveItem();
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				if(varietiesFragment.getCurrentInstance().getRealm1() != null) {
					varietiesFragment.getCurrentInstance().setRealm1(null);
					varietiesFragment.saveItem();
				}
			}
		});
	}

	private void initRealm2Spinner(View layout) {
		realm2Spinner = (Spinner)layout.findViewById(R.id.realm2_spinner);
		realm2SpinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.single_field_row);
		realm2Spinner.setAdapter(realm2SpinnerAdapter);

		realm2SpinnerAdapter.add(noRealmName);
		realm2SpinnerAdapter.add(Realm.CHANNELING.toString());
		realm2SpinnerAdapter.add(Realm.ESSENCE.toString());
		realm2SpinnerAdapter.add(Realm.MENTALISM.toString());
		realm2SpinnerAdapter.notifyDataSetChanged();

		realm2Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String newRealmName = realm2SpinnerAdapter.getItem(position);
				Realm newRealm = Realm.getRealmWithName(newRealmName);
				if(newRealm != null && !newRealm.equals(varietiesFragment.getCurrentInstance().getRealm2())) {
					varietiesFragment.getCurrentInstance().setRealm1(newRealm);
					varietiesFragment.saveItem();
				}
				else if(newRealm == null && varietiesFragment.getCurrentInstance().getRealm2() != null) {
					varietiesFragment.getCurrentInstance().setRealm2(null);
					varietiesFragment.saveItem();
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				if(varietiesFragment.getCurrentInstance().getRealm2() != null) {
					varietiesFragment.getCurrentInstance().setRealm2(null);
					varietiesFragment.saveItem();
				}
			}
		});
	}

	private void initOutlookSpinner(View layout) {
		outlookSpinner = (Spinner)layout.findViewById(R.id.outlook_spinner);
		outlookSpinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.single_field_row);
		outlookSpinner.setAdapter(outlookSpinnerAdapter);

		outlookRxHandler.getAll()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new Subscriber<Collection<Outlook>>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e(TAG, "Exception caught getting all Outlook instances", e);
					}
					@Override
					public void onNext(Collection<Outlook> outlooks) {
						outlookSpinnerAdapter.clear();
						outlookSpinnerAdapter.addAll(outlooks);
						outlookSpinnerAdapter.notifyDataSetChanged();
					}
				});
		outlookSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				Outlook outlook = outlookSpinnerAdapter.getItem(position);
				if(outlook != null && !outlook.equals(varietiesFragment.getCurrentInstance().getOutlook())) {
					varietiesFragment.getCurrentInstance().setOutlook(outlookSpinnerAdapter.getItem(position));
					varietiesFragment.saveItem();
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				if(varietiesFragment.getCurrentInstance().getOutlook() != null) {
					varietiesFragment.getCurrentInstance().setOutlook(null);
					varietiesFragment.saveItem();
				}
			}
		});
	}

	@SuppressWarnings("unused")
	public void setVarietiesFragment(CreatureVarietiesFragment varietiesFragment) {
		this.varietiesFragment = varietiesFragment;
	}

	private class CreatureTypeComparator implements Comparator<CreatureType> {
		@Override
		public int compare(@NonNull CreatureType o1, @NonNull CreatureType o2) {
			return o1.getName().compareTo(o2.getName());
		}
	}
}
