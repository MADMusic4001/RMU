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
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.controller.rxhandler.combat.AttackRxHandler;
import com.madinnovations.rmu.controller.rxhandler.combat.CriticalCodeRxHandler;
import com.madinnovations.rmu.controller.rxhandler.common.SkillRxHandler;
import com.madinnovations.rmu.data.entities.combat.Attack;
import com.madinnovations.rmu.data.entities.combat.AttackBonus;
import com.madinnovations.rmu.data.entities.combat.CriticalCode;
import com.madinnovations.rmu.data.entities.common.Skill;
import com.madinnovations.rmu.data.entities.common.SkillBonus;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.adapters.combat.AttackBonusListAdapter;
import com.madinnovations.rmu.view.adapters.combat.AttacksAdapter;
import com.madinnovations.rmu.view.adapters.combat.CriticalCodesListAdapter;
import com.madinnovations.rmu.view.adapters.common.SkillBonusListAdapter;
import com.madinnovations.rmu.view.adapters.common.SkillSpinnerAdapter;
import com.madinnovations.rmu.view.di.modules.CreatureFragmentModule;

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
		SkillBonusListAdapter.SetSkillBonus {
	private static final String DRAG_ADD_ATTACK = "add-attack";
	private static final String DRAG_REMOVE_ATTACK = "remove-attack";
	private static final String DRAG_ADD_SKILL = "add-skill";
	private static final String DRAG_REMOVE_SKILL = "remove-skill";
	@Inject
	protected AttackRxHandler          attackRxHandler;
	@Inject
	protected CriticalCodeRxHandler    criticalCodeRxHandler;
	@Inject
	protected SkillRxHandler           skillRxHandler;
	@Inject
	protected AttacksAdapter           attacksListAdapter;
	@Inject
	protected SkillSpinnerAdapter      skillsListAdapter;
	@Inject
	protected CriticalCodesListAdapter criticalCodesListAdapter;
	protected AttackBonusListAdapter   attackBonusesListAdapter;
	protected SkillBonusListAdapter    skillBonusesListAdapter;
	private   ListView                 attacksList;
	private   ListView                 attackBonusesList;
	private   EditText                 attackSequenceEdit;
	private   ListView                  skillsList;
	private   ListView                  skillBonusesList;
	private   ListView                  criticalCodesList;
	private   CreatureVarietiesFragment varietiesFragment;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((CampaignActivity)getActivity()).getActivityComponent().
				newCreatureFragmentComponent(new CreatureFragmentModule(this)).injectInto(this);

		View layout = inflater.inflate(R.layout.creature_variety_attacks_page, container, false);

		initAttacksList(layout);
		initAttackBonusesList(layout);
		initAttackSequenceEdit(layout);
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

	public boolean copyViewsToItem() {
		boolean changed = false;
		SparseBooleanArray checkedItemPositions;
		List<CriticalCode> newCriticalCodesList;
		CriticalCode newCriticalCode;
		Map<Attack, Short> newAttackMap;
		AttackBonus newAttackBonus;
		Map<Skill, Short> newSkillMap;
		SkillBonus newSkillBonus;

		newAttackMap = new HashMap<>(attackBonusesListAdapter.getCount());
		for(int i = 0; i < attackBonusesListAdapter.getCount(); i++) {
			newAttackBonus = attackBonusesListAdapter.getItem(i);
			if(varietiesFragment.getCurrentInstance().getAttackBonusesMap().containsKey(newAttackBonus.getAttack())) {
				if(!varietiesFragment.getCurrentInstance().getAttackBonusesMap().get(newAttackBonus.getAttack()).equals(newAttackBonus.getBonus())) {
					changed = true;
				}
				varietiesFragment.getCurrentInstance().getAttackBonusesMap().remove(newAttackBonus.getAttack());
			}
			else {
				changed = true;
			}
			newAttackMap.put(newAttackBonus.getAttack(), newAttackBonus.getBonus());
		}
		if(!varietiesFragment.getCurrentInstance().getAttackBonusesMap().isEmpty() && !newAttackMap.isEmpty()) {
			changed = true;
		}
		varietiesFragment.getCurrentInstance().setAttackBonusesMap(newAttackMap);

		newSkillMap = new HashMap<>(skillBonusesListAdapter.getCount());
		for(int i = 0; i < skillBonusesListAdapter.getCount(); i++) {
			newSkillBonus = skillBonusesListAdapter.getItem(i);
			if(varietiesFragment.getCurrentInstance().getSkillBonusesMap().containsKey(newSkillBonus.getSkill())) {
				if(!varietiesFragment.getCurrentInstance().getSkillBonusesMap().get(newSkillBonus.getSkill()).equals(newSkillBonus.getBonus())) {
					changed = true;
				}
				varietiesFragment.getCurrentInstance().getSkillBonusesMap().remove(newSkillBonus.getSkill());
			}
			else {
				changed = true;
			}
			newSkillMap.put(newSkillBonus.getSkill(), newSkillBonus.getBonus());
		}
		if(!varietiesFragment.getCurrentInstance().getSkillBonusesMap().isEmpty() && !newSkillMap.isEmpty()) {
			changed = true;
		}
		varietiesFragment.getCurrentInstance().setSkillBonusesMap(newSkillMap);

		checkedItemPositions = criticalCodesList.getCheckedItemPositions();
		if(checkedItemPositions != null) {
			newCriticalCodesList = new ArrayList<>(checkedItemPositions.size());
			for (int i = 0; i < checkedItemPositions.size(); i++) {
				newCriticalCode = criticalCodesListAdapter.getItem(checkedItemPositions.keyAt(i));
				if (!varietiesFragment.getCurrentInstance().getCriticalCodes().contains(newCriticalCode)) {
					changed = true;
				} else {
					varietiesFragment.getCurrentInstance().getCriticalCodes().remove(newCriticalCode);
				}
				newCriticalCodesList.add(newCriticalCode);
			}
			if (!varietiesFragment.getCurrentInstance().getCriticalCodes().isEmpty() && !newCriticalCodesList.isEmpty()) {
				changed = true;
			}
			varietiesFragment.getCurrentInstance().setCriticalCodes(newCriticalCodesList);
		}
		else {
			varietiesFragment.getCurrentInstance().getCriticalCodes().clear();
		}

		return changed;
	}

	public void copyItemToViews() {
		attackBonusesList.clearChoices();
		attackBonusesListAdapter.clear();
		for(Map.Entry<Attack, Short> entry : varietiesFragment.getCurrentInstance().getAttackBonusesMap().entrySet()) {
			AttackBonus attackBonus = new AttackBonus(entry.getKey(), entry.getValue());
			attackBonusesListAdapter.add(attackBonus);
		}
		attackBonusesListAdapter.notifyDataSetChanged();

		skillBonusesList.clearChoices();
		skillBonusesListAdapter.clear();
		for(Map.Entry<Skill, Short> entry : varietiesFragment.getCurrentInstance().getSkillBonusesMap().entrySet()) {
			SkillBonus skillBonus = new SkillBonus(entry.getKey(), entry.getValue());
			skillBonusesListAdapter.add(skillBonus);
		}
		skillBonusesListAdapter.notifyDataSetChanged();

		criticalCodesList.clearChoices();
		for(CriticalCode criticalCode : varietiesFragment.getCurrentInstance().getCriticalCodes()) {
			criticalCodesList.setItemChecked(criticalCodesListAdapter.getPosition(criticalCode), true);
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
						Log.e("CreatureVarietiesFrag",
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
						String attackIdString = String.valueOf(adapterView.getId());
						ClipData.Item clipDataItem = new ClipData.Item(attackIdString);
						if(dragData == null) {
							dragData = new ClipData(DRAG_ADD_ATTACK, new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, clipDataItem);
						}
						else {
							dragData.addItem(clipDataItem);
						}
						checkedViews.add(getViewByPosition(checkedItems.keyAt(i), attacksList));
					}
				}
				View.DragShadowBuilder myShadow = new CustomDragShadowBuilder(checkedViews);

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
		attackBonusesListAdapter = new AttackBonusListAdapter(this.getActivity(), this);
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
				View.DragShadowBuilder myShadow = new CustomDragShadowBuilder(checkedViews);

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

	private void initAttackSequenceEdit(View layout) {
		attackSequenceEdit = (EditText)layout.findViewById(R.id.attack_sequence_edit);
		attackSequenceEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length() == 0) {
					attackSequenceEdit.setError(getString(R.string.validation_creature_variety_attack_sequence_required));
				}
			}
		});
		attackSequenceEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					final String newAttackSequence = attackSequenceEdit.getText().toString();
					if (!newAttackSequence.equals(varietiesFragment.getCurrentInstance().getAttackSequence())) {
						varietiesFragment.getCurrentInstance().setAttackSequence(newAttackSequence);
						varietiesFragment.saveItem();
					}
				}
			}
		});
	}

	private void initSkillsList(View layout) {
		skillsList = (ListView) layout.findViewById(R.id.skills_list);
		skillsList.setAdapter(skillsListAdapter);

		skillRxHandler.getAll()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new Subscriber<Collection<Skill>>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e("CreatureVarietiesFrag",
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
						String skillIdString = String.valueOf(adapterView.getId());
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
				View.DragShadowBuilder myShadow = new CustomDragShadowBuilder(checkedViews);

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
		skillBonusesListAdapter = new SkillBonusListAdapter(this.getActivity(), this);
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
				View.DragShadowBuilder myShadow = new CustomDragShadowBuilder(checkedViews);

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

		criticalCodeRxHandler.getAll()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new Subscriber<Collection<CriticalCode>>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e("CreatureVarietiesFrag", "Exception caught loading all CriticalCode instances", e);
					}
					@Override
					public void onNext(Collection<CriticalCode> criticalCodes) {
						criticalCodesListAdapter.clear();
						criticalCodesListAdapter.addAll(criticalCodes);
						criticalCodesListAdapter.notifyDataSetChanged();
					}
				});

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
		private Drawable targetShape = ResourcesCompat.getDrawable(getActivity().getResources(), R.drawable.drag_target_background, null);
		private Drawable hoverShape  = ResourcesCompat.getDrawable(getActivity().getResources(), R.drawable.drag_hover_background, null);
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
							// We just send attack ID but since that is the only field used in the Attack.equals method we can create a
							// temporary attack and set its id field then use the new Attack to find the position of the actual Attack
							// instance in the adapter
							int attackId = Integer.valueOf(item.getText().toString());
							Attack newAttack = new Attack();
							newAttack.setId(attackId);
							int position = attacksListAdapter.getPosition(newAttack);
							Attack attack = attacksListAdapter.getItem(position);
							AttackBonus attackBonus = new AttackBonus(attack, (short) 0);
							if (attackBonusesListAdapter.getPosition(attackBonus) == -1) {
								attackBonusesListAdapter.add(attackBonus);
								varietiesFragment.getCurrentInstance().getAttackBonusesMap().put(attack, (short)0);
								changed = true;
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
							AttackBonus attackBonus = attackBonusesListAdapter.getItem(position);
							varietiesFragment.getCurrentInstance().getAttackBonusesMap().remove(attackBonus.getAttack());
							attackBonusesListAdapter.remove(attackBonus);
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
							Skill skill = skillsListAdapter.getItem(position);
							SkillBonus skillBonus = new SkillBonus(skill, (short) 0);
							if (skillBonusesListAdapter.getPosition(skillBonus) == -1) {
								skillBonusesListAdapter.add(skillBonus);
								varietiesFragment.getCurrentInstance().getSkillBonusesMap().put(skill, (short)0);
								changed = true;
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
							SkillBonus skillBonus =skillBonusesListAdapter.getItem(position);
							varietiesFragment.getCurrentInstance().getSkillBonusesMap().remove(skillBonus.getSkill());
							skillBonusesListAdapter.remove(skillBonus);
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

	private static class CustomDragShadowBuilder extends View.DragShadowBuilder {
		private Drawable[] shadows;
		private List<View> views;

		public CustomDragShadowBuilder(List<View> views) {
			this.views = views;
			shadows = new Drawable[views.size()];
			for(int i = 0 ; i < shadows.length; i ++) {
				shadows[i] = new ColorDrawable(Color.LTGRAY);
			}
		}

		@Override
		public void onProvideShadowMetrics (Point size, Point touch) {
			int topLeftX = Integer.MAX_VALUE, topLeftY = Integer.MAX_VALUE;
			int bottomRightX = 0, bottomRightY = 0;
			int width, height;

			for(int i = 0; i < views.size(); i++) {
				shadows[i].setBounds(views.get(i).getLeft(), views.get(i).getTop(), views.get(i).getRight(), views.get(i).getBottom());
				if(views.get(i).getLeft() < topLeftX) {
					topLeftX = views.get(i).getLeft();
				}
				if(views.get(i).getTop() < topLeftY) {
					topLeftY = views.get(i).getTop();
				}
				if(views.get(i).getRight() > bottomRightX) {
					bottomRightX = views.get(i).getRight();
				}
				if(views.get(i).getBottom() > bottomRightY) {
					bottomRightY = views.get(i).getBottom();
				}
			}

			width = bottomRightX - topLeftX;
			height = bottomRightY - topLeftY;
			size.set(width, height);
			touch.set(width / 2, height / 2);
		}

		@Override
		public void onDrawShadow(Canvas canvas) {
			for(Drawable shadow : shadows) {
				shadow.draw(canvas);
			}
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
