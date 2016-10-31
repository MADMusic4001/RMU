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
package com.madinnovations.rmu.view.activities.creature;

import android.app.Fragment;
import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.controller.rxhandler.combat.AttackRxHandler;
import com.madinnovations.rmu.controller.rxhandler.common.SizeRxHandler;
import com.madinnovations.rmu.controller.rxhandler.common.SkillRxHandler;
import com.madinnovations.rmu.data.entities.combat.Attack;
import com.madinnovations.rmu.data.entities.combat.AttackBonus;
import com.madinnovations.rmu.data.entities.combat.CriticalCode;
import com.madinnovations.rmu.data.entities.common.Size;
import com.madinnovations.rmu.data.entities.common.Skill;
import com.madinnovations.rmu.data.entities.common.SkillBonus;
import com.madinnovations.rmu.data.entities.creature.CreatureVariety;
import com.madinnovations.rmu.view.RMUDragShadowBuilder;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.adapters.combat.AttackBonusListAdapter;
import com.madinnovations.rmu.view.adapters.combat.AttacksAdapter;
import com.madinnovations.rmu.view.adapters.combat.CriticalCodesListAdapter;
import com.madinnovations.rmu.view.adapters.common.SkillBonusListAdapter;
import com.madinnovations.rmu.view.di.modules.CreatureFragmentModule;
import com.madinnovations.rmu.view.utils.EditTextUtils;
import com.madinnovations.rmu.view.utils.SpinnerUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Handles interactions with the UI for creature varieties.
 */
public class CreatureVarietyAttackPageFragment extends Fragment implements AttackBonusListAdapter.SetAttackBonus,
		SkillBonusListAdapter.SetSkillBonus, EditTextUtils.ValuesCallback, SpinnerUtils.ValuesCallback {
	private static final String LOG_TAG = "CVAttackPageFragment";
	private static final String DRAG_ADD_ATTACK = "add-attack";
	private static final String DRAG_REMOVE_ATTACK = "remove-attack";
	private static final String DRAG_ADD_SKILL = "add-skill";
	private static final String DRAG_REMOVE_SKILL = "remove-skill";
	@Inject
	protected AttackRxHandler           attackRxHandler;
	@Inject
	protected SizeRxHandler             sizeRxHandler;
	@Inject
	protected SkillRxHandler            skillRxHandler;
	@Inject
	protected AttacksAdapter            attacksListAdapter;
	protected ArrayAdapter<Skill>       skillsListAdapter;
	@Inject
	protected CriticalCodesListAdapter  criticalCodesListAdapter;
	protected AttackBonusListAdapter    attackBonusesListAdapter;
	protected SkillBonusListAdapter     skillBonusesListAdapter;
	private   ListView                  attacksList;
	private   ListView                  attackBonusesList;
	private   Spinner                   criticalSizeSpinner;
	private   EditText                  attackSequenceEdit;
	private   ListView                  skillsList;
	private   ListView                  skillBonusesList;
	private   ListView                  criticalCodesList;
	private   CreatureVarietiesFragment varietiesFragment;
	private   Size                      noSizeModifier = new Size();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((CampaignActivity)getActivity()).getActivityComponent().
				newCreatureFragmentComponent(new CreatureFragmentModule(this)).injectInto(this);

		View layout = inflater.inflate(R.layout.creature_variety_attacks_page, container, false);

		initAttacksList(layout);
		initAttackBonusesList(layout);
		noSizeModifier.setCode("-");
		noSizeModifier.setName(getString(R.string.no_size_modifier));
		criticalSizeSpinner = new SpinnerUtils<Size>().initSpinner(layout, getActivity(), sizeRxHandler.getAll(), this,
																   R.id.critical_size_spinner, noSizeModifier);
		attackSequenceEdit = EditTextUtils.initEdit(layout, getActivity(), this, R.id.attack_sequence_edit,
													R.string.validation_creature_variety_attack_sequence_required);
		initSkillsList(layout);
		initSkillBonusesList(layout);
		initCriticalCodesList(layout);

		setHasOptionsMenu(true);

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
	public String getValueForEditText(@IdRes int editTextId) {
		String result = null;

		switch (editTextId) {
			case R.id.attack_sequence_edit:
				result = varietiesFragment.getCurrentInstance().getAttackSequence();
				break;
		}

		return result;
	}

	@Override
	public void setValueFromEditText(@IdRes int editTextId, String newString) {
		switch (editTextId) {
			case R.id.attack_sequence_edit:
				varietiesFragment.getCurrentInstance().setAttackSequence(newString);
				varietiesFragment.saveItem();
				break;
		}
	}

	@Override
	public Object getValueForSpinner(@IdRes int spinnerId) {
		Size result = null;

		switch (spinnerId) {
			case R.id.critical_size_spinner:
				result = varietiesFragment.getCurrentInstance().getCriticalSizeModifier();
				if(result == null) {
					result = noSizeModifier;
				}
				break;
		}

		return result;
	}

	@Override
	public void setValueFromSpinner(@IdRes int spinnerId, Object newItem) {
		switch (spinnerId) {
			case R.id.critical_size_spinner:
				if(noSizeModifier.equals(newItem)) {
					varietiesFragment.getCurrentInstance().setCriticalSizeModifier(null);
				}
				else {
					varietiesFragment.getCurrentInstance().setCriticalSizeModifier((Size)newItem);
				}
				break;
		}
	}

	public boolean copyViewsToItem() {
		CreatureVariety creatureVariety = varietiesFragment.getCurrentInstance();
		boolean changed = false;
		SparseBooleanArray checkedItemPositions;
		List<CriticalCode> newCriticalCodesList;
		CriticalCode newCriticalCode;
		Map<Attack, Short> newAttackMap;
		AttackBonus newAttackBonus;
		Map<Skill, Short> newSkillMap;
		SkillBonus newSkillBonus;
		String newString;
		Size newSize;

		if(this.getView() != null) {
			newAttackMap = new HashMap<>(attackBonusesListAdapter.getCount());
			for (int i = 0; i < attackBonusesListAdapter.getCount(); i++) {
				newAttackBonus = attackBonusesListAdapter.getItem(i);
				if(newAttackBonus != null) {
					if (creatureVariety.getAttackBonusesMap().containsKey(newAttackBonus.getAttack())) {
						if (!creatureVariety
								.getAttackBonusesMap()
								.get(newAttackBonus.getAttack())
								.equals(newAttackBonus.getBonus())) {
							changed = true;
						}
						creatureVariety.getAttackBonusesMap().remove(newAttackBonus.getAttack());
					} else {
						changed = true;
					}
					newAttackMap.put(newAttackBonus.getAttack(), newAttackBonus.getBonus());
				}
			}
			if (!creatureVariety.getAttackBonusesMap().isEmpty() && !newAttackMap.isEmpty()) {
				changed = true;
			}
			creatureVariety.setAttackBonusesMap(newAttackMap);

			newSkillMap = new HashMap<>(skillBonusesListAdapter.getCount());
			for (int i = 0; i < skillBonusesListAdapter.getCount(); i++) {
				newSkillBonus = skillBonusesListAdapter.getItem(i);
				if(newSkillBonus != null) {
					if (creatureVariety.getSkillBonusesMap().containsKey(
							newSkillBonus.getSkill())) {
						if (!creatureVariety
								.getSkillBonusesMap()
								.get(newSkillBonus.getSkill())
								.equals(newSkillBonus.getBonus())) {
							changed = true;
						}
						creatureVariety.getSkillBonusesMap().remove(newSkillBonus.getSkill());
					} else {
						changed = true;
					}
					newSkillMap.put(newSkillBonus.getSkill(), newSkillBonus.getBonus());
				}
			}
			if (!creatureVariety.getSkillBonusesMap().isEmpty() && !newSkillMap.isEmpty()) {
				changed = true;
			}
			creatureVariety.setSkillBonusesMap(newSkillMap);

			checkedItemPositions = criticalCodesList.getCheckedItemPositions();
			if (checkedItemPositions != null) {
				newCriticalCodesList = new ArrayList<>(checkedItemPositions.size());
				for (int i = 0; i < checkedItemPositions.size(); i++) {
					newCriticalCode = criticalCodesListAdapter.getItem(checkedItemPositions.keyAt(i));
					if (!creatureVariety.getCriticalCodes().contains(newCriticalCode)) {
						changed = true;
					}
					else {
						creatureVariety.getCriticalCodes().remove(newCriticalCode);
					}
					newCriticalCodesList.add(newCriticalCode);
				}
				if (!creatureVariety.getCriticalCodes().isEmpty() && !newCriticalCodesList.isEmpty()) {
					changed = true;
				}
				creatureVariety.setCriticalCodes(newCriticalCodesList);
			}
			else {
				creatureVariety.getCriticalCodes().clear();
			}
		}

		newString = attackSequenceEdit.getText().toString();
		if((newString == null && creatureVariety.getAttackSequence() != null) ||
				(newString != null && !newString.equals(creatureVariety.getAttackSequence()))) {
			creatureVariety.setAttackSequence(newString);
			changed = true;
		}

		newSize = (Size)criticalSizeSpinner.getSelectedItem();
		if((newSize == null || newSize.equals(noSizeModifier)) && creatureVariety.getCriticalSizeModifier() != null) {
			creatureVariety.setCriticalSizeModifier(null);
			changed = true;
		}
		else if(newSize != null && !newSize.equals(creatureVariety.getCriticalSizeModifier())) {
			creatureVariety.setCriticalSizeModifier(newSize);
			changed = true;
		}

		return changed;
	}

	@SuppressWarnings("unchecked")
	public void copyItemToViews() {
		CreatureVariety creatureVariety = varietiesFragment.getCurrentInstance();

		attackBonusesList.clearChoices();
		attackBonusesListAdapter.clear();
		for(Map.Entry<Attack, Short> entry : creatureVariety.getAttackBonusesMap().entrySet()) {
			AttackBonus attackBonus = new AttackBonus(entry.getKey(), entry.getValue());
			attackBonusesListAdapter.add(attackBonus);
		}
		attackBonusesListAdapter.notifyDataSetChanged();

		skillBonusesList.clearChoices();
		skillBonusesListAdapter.clear();
		for(Map.Entry<Skill, Short> entry : creatureVariety.getSkillBonusesMap().entrySet()) {
			SkillBonus skillBonus = new SkillBonus(entry.getKey(), entry.getValue());
			skillBonusesListAdapter.add(skillBonus);
		}
		skillBonusesListAdapter.notifyDataSetChanged();

		criticalCodesList.clearChoices();
		for(CriticalCode criticalCode : creatureVariety.getCriticalCodes()) {
			criticalCodesList.setItemChecked(criticalCodesListAdapter.getPosition(criticalCode), true);
		}

		attackSequenceEdit.setText(creatureVariety.getAttackSequence());
		if(creatureVariety.getCriticalSizeModifier() == null) {
			criticalSizeSpinner.setSelection(((ArrayAdapter)criticalSizeSpinner.getAdapter()).getPosition(noSizeModifier));
		}
		else {
			criticalSizeSpinner.setSelection(((ArrayAdapter)criticalSizeSpinner.getAdapter()).getPosition(
					creatureVariety.getCriticalSizeModifier()));
		}
	}

	private void initAttacksList(View layout) {
		attacksList = (ListView) layout.findViewById(R.id.attacks_list);
		attacksList.setAdapter(attacksListAdapter);

		attackRxHandler.getAll()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new Subscriber<Collection<Attack>>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e(LOG_TAG,
							  "Exception caught loading all Attack instances in initSecondaryAttacksList", e);
					}
					@Override
					public void onNext(Collection<Attack> attacks) {
						attacksListAdapter.clear();
						attacksListAdapter.addAll(attacks);
						attacksList.clearChoices();
						attacksListAdapter.notifyDataSetChanged();
					}
				});

		attacksList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
				if(!attacksList.isItemChecked(position)) {
					attacksList.setItemChecked(position, true);
				}
				ClipData dragData = null;

				SparseBooleanArray checkedItems = attacksList.getCheckedItemPositions();
				List<View> checkedViews = new ArrayList<>(attacksList.getCheckedItemCount());
				for(int i = 0; i < checkedItems.size(); i++) {
					int currentPosition = checkedItems.keyAt(i);
					Attack attack = attacksListAdapter.getItem(currentPosition);
					if(attack != null) {
						String attackIdString = String.valueOf(attack.getId());
						ClipData.Item clipDataItem = new ClipData.Item(attackIdString);
						if(dragData == null) {
							dragData = new ClipData(DRAG_ADD_ATTACK, new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN},
													clipDataItem);
						}
						else {
							dragData.addItem(clipDataItem);
						}
						checkedViews.add(getViewByPosition(checkedItems.keyAt(i), attacksList));
					}
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

		attacksList.setOnDragListener(new AttackDragListener());
	}

	private void initAttackBonusesList(View layout) {
		attackBonusesList = (ListView) layout.findViewById(R.id.attack_bonuses_list);
		attackBonusesListAdapter = new AttackBonusListAdapter(this.getActivity(), this, attackBonusesList);
		attackBonusesList.setAdapter(attackBonusesListAdapter);

		attackBonusesListAdapter.clear();
		for(Map.Entry<Attack, Short> entry : varietiesFragment.getCurrentInstance().getAttackBonusesMap().entrySet()) {
			attackBonusesListAdapter.add(new AttackBonus(entry.getKey(), entry.getValue()));
		}
		attackBonusesListAdapter.notifyDataSetChanged();

		attackBonusesList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				if(!attackBonusesList.isItemChecked(position)) {
					attackBonusesList.setItemChecked(position, true);
				}
				ClipData dragData = null;

				SparseBooleanArray checkedItems = attackBonusesList.getCheckedItemPositions();
				List<View> checkedViews = new ArrayList<>(attackBonusesList.getCheckedItemCount());
				for(int i = 0; i < checkedItems.size(); i++) {
					int currentPosition = checkedItems.keyAt(i);
					AttackBonus attackBonus = attackBonusesListAdapter.getItem(currentPosition);
					if(checkedItems.valueAt(i) && attackBonus != null) {
						String attackIdString = String.valueOf(attackBonus.getAttack().getId());
						ClipData.Item clipDataItem = new ClipData.Item(attackIdString);
						if(dragData == null) {
							dragData = new ClipData(DRAG_REMOVE_ATTACK, new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, clipDataItem);
						}
						else {
							dragData.addItem(clipDataItem);
						}
						checkedViews.add(getViewByPosition(checkedItems.keyAt(i), attackBonusesList));
					}
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

		attackBonusesList.setOnDragListener(new AttackBonusDragListener());
	}

	private void initSkillsList(View layout) {
		skillsList = (ListView) layout.findViewById(R.id.skills_list);
		skillsListAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_row);
		skillsList.setAdapter(skillsListAdapter);

		skillRxHandler.getAll()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new Subscriber<Collection<Skill>>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e(LOG_TAG,
							  "Exception caught loading all Skill instances in initSecondaryAttacksList", e);
					}
					@Override
					public void onNext(Collection<Skill> skills) {
						skillsListAdapter.clear();
						skillsListAdapter.addAll(skills);
						skillsList.clearChoices();
						skillsListAdapter.notifyDataSetChanged();
					}
				});

		skillsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
				if(!skillsList.isItemChecked(position)) {
					skillsList.setItemChecked(position, true);
				}
				ClipData dragData = null;

				SparseBooleanArray checkedItems = skillsList.getCheckedItemPositions();
				List<View> checkedViews = new ArrayList<>(skillsList.getCheckedItemCount());
				for(int i = 0; i < checkedItems.size(); i++) {
					int currentPosition = checkedItems.keyAt(i);
					Skill skill = skillsListAdapter.getItem(currentPosition);
					if(skill != null) {
						String skillIdString = String.valueOf(skill.getId());
						ClipData.Item clipDataItem = new ClipData.Item(skillIdString);
						if(dragData == null) {
							dragData = new ClipData(DRAG_ADD_SKILL, new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, clipDataItem);
						}
						else {
							dragData.addItem(clipDataItem);
						}
						checkedViews.add(getViewByPosition(checkedItems.keyAt(i), skillsList));
					}
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

		skillsList.setOnDragListener(new SkillDragListener());
	}

	private void initSkillBonusesList(View layout) {
		skillBonusesList = (ListView) layout.findViewById(R.id.skill_bonuses_list);
		skillBonusesListAdapter = new SkillBonusListAdapter(this.getActivity(), this, skillBonusesList);
		skillBonusesList.setAdapter(skillBonusesListAdapter);

		skillBonusesListAdapter.clear();
		for(Map.Entry<Skill, Short> entry : varietiesFragment.getCurrentInstance().getSkillBonusesMap().entrySet()) {
			skillBonusesListAdapter.add(new SkillBonus(entry.getKey(), entry.getValue()));
		}
		skillBonusesListAdapter.notifyDataSetChanged();

		skillBonusesList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				if(!skillBonusesList.isItemChecked(position)) {
					skillBonusesList.setItemChecked(position, true);
				}
				ClipData dragData = null;

				SparseBooleanArray checkedItems = skillBonusesList.getCheckedItemPositions();
				List<View> checkedViews = new ArrayList<>(skillBonusesList.getCheckedItemCount());
				for(int i = 0; i < checkedItems.size(); i++) {
					int currentPosition = checkedItems.keyAt(i);
					SkillBonus skillBonus = skillBonusesListAdapter.getItem(currentPosition);
					if(checkedItems.valueAt(i) && skillBonus != null) {
						String skillIdString = String.valueOf(skillBonus.getSkill().getId());
						ClipData.Item clipDataItem = new ClipData.Item(skillIdString);
						if(dragData == null) {
							dragData = new ClipData(DRAG_REMOVE_SKILL, new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, clipDataItem);
						}
						else {
							dragData.addItem(clipDataItem);
						}
						checkedViews.add(getViewByPosition(checkedItems.keyAt(i), skillBonusesList));
					}
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

		skillBonusesList.setOnDragListener(new SkillBonusDragListener());
	}

	private void initCriticalCodesList(View layout) {
		criticalCodesList = (ListView) layout.findViewById(R.id.critical_codes_list);
		criticalCodesList.setAdapter(criticalCodesListAdapter);
		criticalCodesListAdapter.clear();
		criticalCodesListAdapter.addAll(CriticalCode.values());
		criticalCodesListAdapter.notifyDataSetChanged();

		criticalCodesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				CriticalCode criticalCode = criticalCodesListAdapter.getItem(position);
				boolean changed = false;
				if(criticalCodesList.isItemChecked(position)) {
					if(!varietiesFragment.getCurrentInstance().getCriticalCodes().contains(criticalCode)) {
						varietiesFragment.getCurrentInstance().getCriticalCodes().add(criticalCode);
						changed = true;
					}
				}
				else {
					if(varietiesFragment.getCurrentInstance().getCriticalCodes().contains(criticalCode)) {
						varietiesFragment.getCurrentInstance().getCriticalCodes().remove(criticalCode);
						changed = true;
					}
				}
				if(changed) {
					varietiesFragment.saveItem();
				}
			}
		});
	}

	@Override
	public void setAttackBonus(AttackBonus attackBonus) {
		boolean changed = false;

		if(varietiesFragment.getCurrentInstance().getAttackBonusesMap().containsKey(attackBonus.getAttack())) {
			if(varietiesFragment.getCurrentInstance().getAttackBonusesMap().get(attackBonus.getAttack()) != attackBonus.getBonus()) {
				varietiesFragment.getCurrentInstance().getAttackBonusesMap().put(attackBonus.getAttack(), attackBonus.getBonus());
				changed = true;
			}
		}
		else {
			varietiesFragment.getCurrentInstance().getAttackBonusesMap().put(attackBonus.getAttack(), attackBonus.getBonus());
			changed = true;
		}
		if(changed) {
			varietiesFragment.saveItem();
		}
	}

	@Override
	public void setSkillBonus(SkillBonus skillBonus) {
		boolean changed = false;

		if(varietiesFragment.getCurrentInstance().getSkillBonusesMap().containsKey(skillBonus.getSkill())) {
			if(varietiesFragment.getCurrentInstance().getSkillBonusesMap().get(skillBonus.getSkill()) != skillBonus.getBonus()) {
				varietiesFragment.getCurrentInstance().getSkillBonusesMap().put(skillBonus.getSkill(), skillBonus.getBonus());
				changed = true;
			}
		}
		else {
			varietiesFragment.getCurrentInstance().getSkillBonusesMap().put(skillBonus.getSkill(), skillBonus.getBonus());
			changed = true;
		}
		if(changed) {
			varietiesFragment.saveItem();
		}
	}

	protected class AttackBonusDragListener implements View.OnDragListener {
		private Drawable targetShape = ResourcesCompat.getDrawable(getActivity().getResources(),
																   R.drawable.drag_target_background, null);
		private Drawable hoverShape  = ResourcesCompat.getDrawable(getActivity().getResources(),
																   R.drawable.drag_hover_background, null);
		private Drawable normalShape = attackBonusesList.getBackground();

		@Override
		public boolean onDrag(View v, DragEvent event) {
			final int action = event.getAction();

			switch(action) {
				case DragEvent.ACTION_DRAG_STARTED:
					if(event.getClipDescription() != null && DRAG_ADD_ATTACK.equals(event.getClipDescription().getLabel())) {
						v.setBackground(targetShape);
						v.invalidate();
						break;
					}
					return false;
				case DragEvent.ACTION_DRAG_ENTERED:
					if(event.getClipDescription() != null && DRAG_ADD_ATTACK.equals(event.getClipDescription().getLabel())) {
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
					if(event.getClipDescription() != null && DRAG_ADD_ATTACK.equals(event.getClipDescription().getLabel())) {
						v.setBackground(targetShape);
						v.invalidate();
					}
					else {
						return false;
					}
					break;
				case DragEvent.ACTION_DROP:
					if(event.getClipDescription() != null && DRAG_ADD_ATTACK.equals(event.getClipDescription().getLabel())) {
						boolean changed = false;
						for(int i = 0; i < event.getClipData().getItemCount(); i++) {
							ClipData.Item item = event.getClipData().getItemAt(i);
							// We just send attack ID but since that is the only field used in the Attack.equals method we can
							// create a temporary attack and set its id field then use the new Attack to find the position of
							// the actual Attack instance in the adapter
							int attackId = Integer.valueOf(item.getText().toString());
							Attack newAttack = new Attack(attackId);
							int position = attacksListAdapter.getPosition(newAttack);
							if(position != -1) {
								Attack attack = attacksListAdapter.getItem(position);
								AttackBonus attackBonus = new AttackBonus(attack, (short) 0);
								if (attackBonusesListAdapter.getPosition(attackBonus) == -1) {
									attackBonusesListAdapter.add(attackBonus);
									varietiesFragment.getCurrentInstance().getAttackBonusesMap().put(attack, (short) 0);
									changed = true;
								}
							}
						}
						if(changed) {
							varietiesFragment.saveItem();
							attackBonusesListAdapter.notifyDataSetChanged();
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
	}

	protected class AttackDragListener implements View.OnDragListener {
		private Drawable targetShape = ResourcesCompat.getDrawable(getActivity().getResources(), R.drawable.drag_target_background, null);
		private Drawable hoverShape = ResourcesCompat.getDrawable(getActivity().getResources(), R.drawable.drag_hover_background, null);
		private Drawable normalShape = attacksList.getBackground();

		@Override
		public boolean onDrag(View v, DragEvent event) {
			final int action = event.getAction();

			switch (action) {
				case DragEvent.ACTION_DRAG_STARTED:
					if(event.getClipDescription() != null && DRAG_REMOVE_ATTACK.equals(event.getClipDescription().getLabel())) {
						v.setBackground(targetShape);
						v.invalidate();
					}
					else {
						return false;
					}
					break;
				case DragEvent.ACTION_DRAG_ENTERED:
					if(event.getClipDescription() != null && DRAG_REMOVE_ATTACK.equals(event.getClipDescription().getLabel())) {
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
					if(event.getClipDescription() != null && DRAG_REMOVE_ATTACK.equals(event.getClipDescription().getLabel())) {
						v.setBackground(targetShape);
						v.invalidate();
					}
					else {
						return false;
					}
					break;
				case DragEvent.ACTION_DROP:
					if(event.getClipDescription() != null && DRAG_REMOVE_ATTACK.equals(event.getClipDescription().getLabel())) {
						for (int i = 0; i < event.getClipData().getItemCount(); i++) {
							ClipData.Item item = event.getClipData().getItemAt(i);
							// We just send attack ID but since that is the only field used in the Attack.equals method and attack is the
							// only field used in the AttackBonus.equals method we can create a temporary Attack and set its id field then
							// create a new AttackBonus and set its attack field then use the new AttackBonus to find the position of the
							// complete AttackBonus instance in the adapter
							int attackId = Integer.valueOf(item.getText().toString());
							Attack newAttack = new Attack();
							newAttack.setId(attackId);
							AttackBonus newAttackBonus = new AttackBonus(newAttack, (short)0);
							int position = attackBonusesListAdapter.getPosition(newAttackBonus);
							if(position != -1) {
								AttackBonus attackBonus = attackBonusesListAdapter.getItem(position);
								if(attackBonus != null) {
									varietiesFragment.getCurrentInstance().getAttackBonusesMap().remove(attackBonus.getAttack());
									attackBonusesListAdapter.remove(attackBonus);
								}
							}
						}
						varietiesFragment.saveItem();
						attackBonusesList.clearChoices();
						attackBonusesListAdapter.notifyDataSetChanged();
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
	}

	protected class SkillBonusDragListener implements View.OnDragListener {
		private Drawable targetShape = ResourcesCompat.getDrawable(getActivity().getResources(), R.drawable.drag_target_background, null);
		private Drawable hoverShape = ResourcesCompat.getDrawable(getActivity().getResources(), R.drawable.drag_hover_background, null);
		private Drawable normalShape = skillBonusesList.getBackground();

		@Override
		public boolean onDrag(View v, DragEvent event) {
			final int action = event.getAction();

			switch(action) {
				case DragEvent.ACTION_DRAG_STARTED:
					if(event.getClipDescription() != null && DRAG_ADD_SKILL.equals(event.getClipDescription().getLabel())) {
						v.setBackground(targetShape);
						v.invalidate();
						break;
					}
					return false;
				case DragEvent.ACTION_DRAG_ENTERED:
					if(event.getClipDescription() != null && DRAG_ADD_SKILL.equals(event.getClipDescription().getLabel())) {
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
					if(event.getClipDescription() != null && DRAG_ADD_SKILL.equals(event.getClipDescription().getLabel())) {
						v.setBackground(targetShape);
						v.invalidate();
					}
					else {
						return false;
					}
					break;
				case DragEvent.ACTION_DROP:
					if(event.getClipDescription() != null && DRAG_ADD_SKILL.equals(event.getClipDescription().getLabel())) {
						boolean changed = false;
						for(int i = 0; i < event.getClipData().getItemCount(); i++) {
							ClipData.Item item = event.getClipData().getItemAt(i);
							// We just send skill ID but since that is the only field used in the Skill.equals method we can create a
							// temporary skill and set its id field then use the new Skill to find the position of the actual Skill
							// instance in the adapter
							int skillId = Integer.valueOf(item.getText().toString());
							Skill newSkill = new Skill();
							newSkill.setId(skillId);
							int position = skillsListAdapter.getPosition(newSkill);
							if(position != -1) {
								Skill skill = skillsListAdapter.getItem(position);
								SkillBonus skillBonus = new SkillBonus(skill, (short) 0);
								if (skillBonusesListAdapter.getPosition(skillBonus) == -1) {
									skillBonusesListAdapter.add(skillBonus);
									varietiesFragment.getCurrentInstance().getSkillBonusesMap().put(skill, (short) 0);
									changed = true;
								}
							}
						}
						if(changed) {
							varietiesFragment.saveItem();
							skillBonusesListAdapter.notifyDataSetChanged();
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
	}

	protected class SkillDragListener implements View.OnDragListener {
		private Drawable targetShape = ResourcesCompat.getDrawable(getActivity().getResources(), R.drawable.drag_target_background, null);
		private Drawable hoverShape = ResourcesCompat.getDrawable(getActivity().getResources(), R.drawable.drag_hover_background, null);
		private Drawable normalShape = skillsList.getBackground();

		@Override
		public boolean onDrag(View v, DragEvent event) {
			final int action = event.getAction();

			switch (action) {
				case DragEvent.ACTION_DRAG_STARTED:
					if(event.getClipDescription() != null && DRAG_REMOVE_SKILL.equals(event.getClipDescription().getLabel())) {
						v.setBackground(targetShape);
						v.invalidate();
					}
					else {
						return false;
					}
					break;
				case DragEvent.ACTION_DRAG_ENTERED:
					if(event.getClipDescription() != null && DRAG_REMOVE_SKILL.equals(event.getClipDescription().getLabel())) {
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
					if(event.getClipDescription() != null && DRAG_REMOVE_SKILL.equals(event.getClipDescription().getLabel())) {
						v.setBackground(targetShape);
						v.invalidate();
					}
					else {
						return false;
					}
					break;
				case DragEvent.ACTION_DROP:
					if(event.getClipDescription() != null && DRAG_REMOVE_SKILL.equals(event.getClipDescription().getLabel())) {
						for (int i = 0; i < event.getClipData().getItemCount(); i++) {
							ClipData.Item item = event.getClipData().getItemAt(i);
							// We just send skill ID but since that is the only field used in the Skill.equals method and skill is the
							// only field used in the SkillBonus.equals method we can create a temporary Skill and set its id field then
							// create a new SkillBonus and set its skill field then use the new SkillBonus to find the position of the
							// complete SkillBonus instance in the adapter
							int skillId = Integer.valueOf(item.getText().toString());
							Skill newSkill = new Skill();
							newSkill.setId(skillId);
							SkillBonus newSkillBonus = new SkillBonus(newSkill, (short)0);
							int position = skillBonusesListAdapter.getPosition(newSkillBonus);
							if(position != -1) {
								SkillBonus skillBonus = skillBonusesListAdapter.getItem(position);
								if(skillBonus != null) {
									varietiesFragment.getCurrentInstance().getSkillBonusesMap().remove(skillBonus.getSkill());
									skillBonusesListAdapter.remove(skillBonus);
								}
							}
						}
						varietiesFragment.saveItem();
						skillBonusesList.clearChoices();
						skillBonusesListAdapter.notifyDataSetChanged();
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
	}

	private View getViewByPosition(int pos, ListView listView) {
		final int firstListItemPosition = listView.getFirstVisiblePosition();
		final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

		if (pos < firstListItemPosition || pos > lastListItemPosition ) {
			return listView.getAdapter().getView(pos, null, listView);
		} else {
			final int childIndex = pos - firstListItemPosition;
			return listView.getChildAt(childIndex);
		}
	}

	public void setVarietiesFragment(CreatureVarietiesFragment varietiesFragment) {
		this.varietiesFragment = varietiesFragment;
	}
}
