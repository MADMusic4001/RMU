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

import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.controller.rxhandler.campaign.CampaignRxHandler;
import com.madinnovations.rmu.controller.rxhandler.common.SkillRxHandler;
import com.madinnovations.rmu.controller.rxhandler.creature.CreatureArchetypeRxHandler;
import com.madinnovations.rmu.controller.rxhandler.creature.CreatureRxHandler;
import com.madinnovations.rmu.controller.rxhandler.creature.CreatureVarietyRxHandler;
import com.madinnovations.rmu.data.entities.campaign.Campaign;
import com.madinnovations.rmu.data.entities.combat.Attack;
import com.madinnovations.rmu.data.entities.common.DevelopmentCostGroup;
import com.madinnovations.rmu.data.entities.common.Skill;
import com.madinnovations.rmu.data.entities.common.SkillBonus;
import com.madinnovations.rmu.data.entities.common.SkillRanks;
import com.madinnovations.rmu.data.entities.common.Specialization;
import com.madinnovations.rmu.data.entities.common.Statistic;
import com.madinnovations.rmu.data.entities.common.TalentInstance;
import com.madinnovations.rmu.data.entities.creature.Creature;
import com.madinnovations.rmu.data.entities.creature.CreatureArchetype;
import com.madinnovations.rmu.data.entities.creature.CreatureArchetypeLevel;
import com.madinnovations.rmu.data.entities.creature.CreatureVariety;
import com.madinnovations.rmu.data.entities.creature.LevelSpread;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.adapters.TwoFieldListAdapter;
import com.madinnovations.rmu.view.adapters.common.SkillRanksAdapter;
import com.madinnovations.rmu.view.di.modules.CreatureFragmentModule;
import com.madinnovations.rmu.view.utils.Boast;
import com.madinnovations.rmu.view.utils.EditTextUtils;
import com.madinnovations.rmu.view.utils.RandomUtils;
import com.madinnovations.rmu.view.utils.SpinnerUtils;
import com.madinnovations.rmu.view.widgets.TooltipDialogFragment;
import com.nhaarman.supertooltips.ToolTip;
import com.nhaarman.supertooltips.ToolTipRelativeLayout;
import com.nhaarman.supertooltips.ToolTipView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Handles interactions with the UI for creature types.
 */
public class CreaturesFragment extends Fragment implements TwoFieldListAdapter.GetValues<Creature>,
		EditTextUtils.ValuesCallback, SpinnerUtils.ValuesCallback, SkillRanksAdapter.SkillRanksAdapterCallbacks,
		TooltipDialogFragment.TooltipDialogListener {
	private static final String TAG = "CreaturesFragment";
	@Inject
	protected CampaignRxHandler               campaignRxHandler;
	@Inject
	protected CreatureRxHandler               creatureRxHandler;
	@Inject
	protected CreatureArchetypeRxHandler      creatureArchetypeRxHandler;
	@Inject
	protected CreatureVarietyRxHandler        creatureVarietyRxHandler;
	@Inject
	protected SkillRxHandler                  skillRxHandler;
	private   TwoFieldListAdapter<Creature>   creatureListAdapter;
	private   SkillRanksAdapter               skillRanksAdapter;
	private   SpinnerUtils<Campaign>          campaignSpinnerUtils;
	private   SpinnerUtils<CreatureVariety>   creatureVarietySpinnerUtils;
	private   SpinnerUtils<CreatureArchetype> creatureArchetypeSpinnerUtils;
	private   ListView                        creatureListView;
	private   EditText                        levelEdit;
	private   List<Skill>                     skillList = null;
	private   Creature                        currentInstance = new Creature();
	private   boolean                         isNew           = true;
	private   ToolTipView                     toolTipView;
	private   ToolTipRelativeLayout           toolTipFrameLayout;
	private TooltipDialogFragment tooltipDialogFragment = null;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((CampaignActivity)getActivity()).getActivityComponent().
				newCreatureFragmentComponent(new CreatureFragmentModule(this)).injectInto(this);

		View layout = inflater.inflate(R.layout.creature_fragment, container, false);

		((TextView)layout.findViewById(R.id.header_field1)).setText(R.string.label_creature_name);
		((TextView)layout.findViewById(R.id.header_field2)).setText(R.string.label_campaign_name);

		initLevelEdit(layout);

		campaignSpinnerUtils = new SpinnerUtils<>();
		campaignSpinnerUtils.initSpinner(layout, getActivity(), campaignRxHandler.getAll(), this, R.id.campaign_spinner, null);
		creatureVarietySpinnerUtils = new SpinnerUtils<>();
		creatureVarietySpinnerUtils.initSpinner(layout, getActivity(), creatureVarietyRxHandler.getAll(), this,
												R.id.creature_variety_spinner, null);
		creatureArchetypeSpinnerUtils = new SpinnerUtils<>();
		creatureArchetypeSpinnerUtils.initSpinner(layout, getActivity(), creatureArchetypeRxHandler.getAll(), this,
												R.id.archetype_spinner, null);
		initRandomButton(layout);
		initSkillRanksListView(layout);
		initListView(layout);

		setHasOptionsMenu(true);

		return layout;
	}

	@Override
	public void onPause() {
		if(copyViewsToItem()) {
			saveItem();
		}
		super.onPause();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.creatures_action_bar, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if(id == R.id.action_new_creature) {
			if(copyViewsToItem()) {
				saveItem();
			}
			currentInstance = new Creature();
			isNew = true;
			copyItemToViews();
			creatureListView.clearChoices();
			creatureListAdapter.notifyDataSetChanged();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		getActivity().getMenuInflater().inflate(R.menu.creature_context_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		final Creature creature;

		AdapterView.AdapterContextMenuInfo info =
				(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

		switch (item.getItemId()) {
			case R.id.context_new_creature:
				if(copyViewsToItem()) {
					saveItem();
				}
				currentInstance = new Creature();
				isNew = true;
				copyItemToViews();
				creatureListView.clearChoices();
				creatureListAdapter.notifyDataSetChanged();
				return true;
			case R.id.context_delete_creature:
				creature = (Creature)creatureListView.getItemAtPosition(info.position);
				if(creature != null) {
					deleteItem(creature);
					return true;
				}
		}
		return super.onContextItemSelected(item);
	}

	@Override
	public CharSequence getField1Value(Creature creature) {
		return creature.getCreatureVariety().getName();
	}

	@Override
	public CharSequence getField2Value(Creature creature) {
		return creature.getCampaign().getName();
	}

	@Override
	public String getValueForEditText(@IdRes int editTextId) {
		String result = null;

		switch (editTextId) {
			case R.id.creature_level_edit:
				result = String.valueOf(currentInstance.getCurrentLevel());
				break;
		}

		return result;
	}

	@Override
	public void setValueFromEditText(@IdRes int editTextId, String newString) {
		switch (editTextId) {
			case R.id.creature_level_edit:
				currentInstance.setCurrentLevel(Short.valueOf(newString));
				saveItem();
				break;
		}
	}

	@Override
	public boolean performLongClick(@IdRes int editTextId) {
		return false;
	}

	@Override
	public Object getValueForSpinner(@IdRes int spinnerId) {
		Object result = null;

		switch (spinnerId) {
			case R.id.campaign_spinner:
				result = currentInstance.getCampaign();
				break;
			case R.id.creature_variety_spinner:
				result = currentInstance.getCreatureVariety();
				break;
			case R.id.archetype_spinner:
				result = currentInstance.getArchetype();
				break;
		}

		return result;
	}

	@Override
	public void setValueFromSpinner(@IdRes int spinnerId, Object newItem) {
		switch (spinnerId) {
			case R.id.campaign_spinner:
				if(newItem instanceof Campaign) {
					currentInstance.setCampaign((Campaign)newItem);
					saveItem();
				}
				break;
			case R.id.creature_variety_spinner:
				if(newItem instanceof CreatureVariety) {
					setVariety((CreatureVariety)newItem);
					saveItem();
				}
				break;
			case R.id.archetype_spinner:
				if(newItem instanceof CreatureArchetype) {
					currentInstance.setArchetype((CreatureArchetype)newItem);
					saveItem();
				}
				break;
		}
	}

	@Override
	public void observerCompleted(@IdRes int spinnerId) {}

	@Override
	public short purchaseRank(SkillRanks skillRanks) {
		return 0;
	}

	@Override
	public short sellRank(SkillRanks skillRanks) {
		return 0;
	}

	@Override
	public DevelopmentCostGroup getSkillCost(SkillRanks skillRanks) {
		return null;
	}

	@Override
	public short getRanks(SkillRanks skillRanks) {
		return 0;
	}

	@Override
	public short getCultureRanks(SkillRanks skillRanks) {
		return 0;
	}

	@Override
	public void onOk(DialogFragment dialog) {
		tooltipDialogFragment = null;
	}

	private boolean copyViewsToItem() {
		boolean changed = false;
		short newShort;

		View currentFocusView = getActivity().getCurrentFocus();
		if(currentFocusView != null) {
			currentFocusView.clearFocus();
		}

		if(levelEdit.getText() != null && levelEdit.getText().toString().length() > 0) {
			newShort = Short.valueOf(levelEdit.getText().toString());
			if (newShort != currentInstance.getCurrentLevel()) {
				currentInstance.setCurrentLevel(newShort);
				changed = true;
			}
		}

		Campaign newCampaign = campaignSpinnerUtils.getSelectedItem();
		if(newCampaign != null && !newCampaign.equals(currentInstance.getCampaign())) {
			currentInstance.setCampaign(newCampaign);
			changed = true;
		}

		CreatureVariety newVariety = creatureVarietySpinnerUtils.getSelectedItem();
		if(newVariety != null && !newVariety.equals(currentInstance.getCreatureVariety())) {
			currentInstance.setCreatureVariety(newVariety);
			changed = true;
		}

		CreatureArchetype newArchetype = creatureArchetypeSpinnerUtils.getSelectedItem();
		if(newArchetype != null && !newArchetype.equals(currentInstance.getArchetype())) {
			currentInstance.setArchetype(newArchetype);
			changed = true;
		}

		return changed;
	}

	private void copyItemToViews() {
		levelEdit.setText(String.valueOf(currentInstance.getCurrentLevel()));
		campaignSpinnerUtils.setSelection(currentInstance.getCampaign());
		creatureVarietySpinnerUtils.setSelection(currentInstance.getCreatureVariety());
		creatureArchetypeSpinnerUtils.setSelection(currentInstance.getArchetype());

		levelEdit.setError(null);
	}

	private void saveItem() {
		if(currentInstance.isValid()) {
			final boolean wasNew = isNew;
			isNew = false;
			creatureRxHandler.save(currentInstance)
					.observeOn(AndroidSchedulers.mainThread())
					.subscribeOn(Schedulers.io())
					.subscribe(new Subscriber<Creature>() {
						@Override
						public void onCompleted() {}
						@Override
						public void onError(Throwable e) {
							Log.e(TAG, "Exception saving new Creature: " + currentInstance, e);
							Toast.makeText(getActivity(), R.string.toast_creature_save_failed, Toast.LENGTH_SHORT).show();
						}
						@Override
						public void onNext(Creature savedItem) {
							if (wasNew) {
								creatureListAdapter.add(savedItem);
								if(savedItem == currentInstance) {
									creatureListView.setSelection(creatureListAdapter.getPosition(savedItem));
									creatureListView.setItemChecked(creatureListAdapter.getPosition(savedItem), true);
								}
								creatureListAdapter.notifyDataSetChanged();
							}
							if(getActivity() != null) {
								Toast.makeText(getActivity(), R.string.toast_creature_saved, Toast.LENGTH_SHORT).show();
								int position = creatureListAdapter.getPosition(savedItem);
								LinearLayout v = (LinearLayout) creatureListView.getChildAt(
										position - creatureListView.getFirstVisiblePosition());
								if (v != null) {
									TextView textView = (TextView) v.findViewById(R.id.row_field1);
									textView.setText(savedItem.getCreatureVariety().getName());
									textView = (TextView) v.findViewById(R.id.row_field2);
									textView.setText(savedItem.getCampaign().getName());
								}
							}
						}
					});
		}
	}

	private void deleteItem(@NonNull final Creature item) {
		creatureRxHandler.deleteById(item.getId())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new Subscriber<Boolean>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e(TAG, "Exception when deleting: " + item, e);
						Toast.makeText(getActivity(), R.string.toast_creature_delete_failed, Toast.LENGTH_SHORT).show();
					}
					@Override
					public void onNext(Boolean success) {
						if(success) {
							int position = creatureListAdapter.getPosition(item);
							if(position == creatureListAdapter.getCount() -1) {
								position--;
							}
							creatureListAdapter.remove(item);
							creatureListAdapter.notifyDataSetChanged();
							if(position >= 0) {
								creatureListView.setSelection(position);
								creatureListView.setItemChecked(position, true);
								currentInstance = creatureListAdapter.getItem(position);
							}
							else if(creatureListAdapter.getCount() > 0){
								creatureListView.setSelection(0);
								creatureListView.setItemChecked(0, true);
								currentInstance = creatureListAdapter.getItem(0);
							}
							copyItemToViews();
							Boast.showText(getActivity(), R.string.toast_creature_deleted, Toast.LENGTH_SHORT);
						}
					}
				});
	}

	private void initLevelEdit(View layout) {
		levelEdit = EditTextUtils.initEdit(layout, getActivity(), this, R.id.creature_level_edit,
										   R.string.validation_creature_level_required);
		toolTipFrameLayout = (ToolTipRelativeLayout) layout.findViewById(R.id.tooltipRelativeLayout);

		levelEdit.setOnHoverListener(new View.OnHoverListener() {
			@Override
			public boolean onHover(View v, MotionEvent event) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_HOVER_ENTER:
						ToolTip toolTip = new ToolTip()
								.withText(levelEdit.getContentDescription())
								.withAnimationType(ToolTip.AnimationType.FROM_TOP);
						toolTipView = toolTipFrameLayout.showToolTipForView(
								toolTip,
								CreaturesFragment.this.getActivity().findViewById(R.id.creature_level_edit));
						break;
					case MotionEvent.ACTION_HOVER_EXIT:
						toolTipView.remove();
						break;
				}
				return false;
			}
		});
	}

	private void initRandomButton(View layout) {
		Button randomButton = (Button)layout.findViewById(R.id.random_level_button);

		randomButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(currentInstance.getCreatureVariety() != null) {
					short typicalLevel = currentInstance.getCreatureVariety().getTypicalLevel();
					char levelSpread = currentInstance.getCreatureVariety().getLevelSpread();
					short level;
					short offset = LevelSpread.valueOf(String.valueOf(levelSpread)).getOffset(RandomUtils.roll1d100OE());
					if (offset == Short.MIN_VALUE) {
						level = 0;
					}
					else {
						level = (short) (typicalLevel + offset);
						if (level <= 0) {
							level = 1;
						}
					}
					if(currentInstance.getCurrentLevel() != level) {
						setCurrentLevel(level);
					}
				}
			}
		});
	}

	private void initSkillRanksListView(final View layout) {
		ListView skillRanksListView = (ListView) layout.findViewById(R.id.skill_bonus_list);
		skillRanksAdapter = new SkillRanksAdapter(getActivity(), this);
		skillRanksListView.setAdapter(skillRanksAdapter);

		loadSkills();

		skillRanksListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				if(position != AdapterView.INVALID_POSITION) {
					SkillRanks skillRanks = skillRanksAdapter.getItem(position);
					if (skillRanks != null) {
						String content = null;
						if(skillRanks.getSkill() != null) {
							content = skillRanks.getSkill().getDescription();
						}
						else if(skillRanks.getSpecialization() != null) {
							content = skillRanks.getSpecialization().getDescription();
						}
						else if(skillRanks.getSpellList() != null) {
							content = skillRanks.getSpellList().getName() + System.getProperty("line.separator")
									+ skillRanks.getSpellList().getNotes();
						}
						if(content != null && tooltipDialogFragment == null) {
							tooltipDialogFragment = new TooltipDialogFragment();
							tooltipDialogFragment.setListener(CreaturesFragment.this);
							tooltipDialogFragment.setMessage(content);
							tooltipDialogFragment.show(getChildFragmentManager(), "tooltip");
						}
					}
				}
				return false;
			}
		});

		registerForContextMenu(skillRanksListView);
	}

	private void initListView(View layout) {
		creatureListView = (ListView) layout.findViewById(R.id.creatures_list_view);
		creatureListAdapter = new TwoFieldListAdapter<>(this.getActivity(), 1, 5, this);
		creatureListView.setAdapter(creatureListAdapter);

		creatureRxHandler.getAll()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Collection<Creature>>() {
					@Override
					public void onCompleted() {
						if(creatureListAdapter.getCount() > 0) {
							currentInstance = creatureListAdapter.getItem(0);
							isNew = false;
							creatureListView.setSelection(0);
							creatureListView.setItemChecked(0, true);
							creatureListAdapter.notifyDataSetChanged();
							copyItemToViews();
						}
					}
					@Override
					public void onError(Throwable e) {
						Log.e(TAG, "Exception caught getting all Creature instances in onCreateView", e);
						Toast.makeText(CreaturesFragment.this.getActivity(),
								R.string.toast_creature_load_failed,
								Toast.LENGTH_SHORT).show();
					}
					@Override
					public void onNext(Collection<Creature> creatures) {
						creatureListAdapter.clear();
						creatureListAdapter.addAll(creatures);
						creatureListAdapter.notifyDataSetChanged();
						Boast.showText(CreaturesFragment.this.getActivity(),
									   String.format(getString(R.string.toast_creature_loaded), creatures.size()),
									   Toast.LENGTH_SHORT);
					}
				});

		// Clicking a row in the listView will send the user to the edit world activity
		creatureListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(copyViewsToItem()) {
					saveItem();
				}
				currentInstance = (Creature) creatureListView.getItemAtPosition(position);
				isNew = false;
				if (currentInstance == null) {
					currentInstance = new Creature();
					isNew = true;
				}
				copyItemToViews();
			}
		});
		registerForContextMenu(creatureListView);
	}

	private void loadSkills() {
		skillRxHandler.getAll()
				.subscribe(new Subscriber<Collection<Skill>>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e(TAG, "Exception caught getting all Skill instances.", e);
					}
					@SuppressWarnings("unchecked")
					@Override
					public void onNext(Collection<Skill> skillCollection) {
						if (skillCollection instanceof List) {
							skillList = (List) skillCollection;
						}
						else {
							skillList = new ArrayList<>(skillCollection);
						}
						copySkillRanksToView();
					}
				});
	}

	private void copySkillRanksToView() {
		List<SkillRanks> ranksList = new ArrayList<>();
		for (Skill skill : skillList) {
			SkillRanks skillRanks;
			if (!skill.isRequiresSpecialization()) {
				skillRanks = new SkillRanks();
				skillRanks.setSkill(skill);
				ranksList.add(skillRanks);
			}
			else {
				for (Specialization specialization : skill.getSpecializations()) {
					skillRanks = new SkillRanks();
					skillRanks.setSpecialization(specialization);
					ranksList.add(skillRanks);
				}
			}
		}
		//noinspection unchecked
		Collections.sort(ranksList);
		skillRanksAdapter.clear();
		skillRanksAdapter.addAll(ranksList);
		skillRanksAdapter.notifyDataSetChanged();
	}

	private void setVariety(CreatureVariety variety) {
		currentInstance.setCreatureVariety(variety);
		currentInstance.setCurrentLevel(variety.getTypicalLevel());
		currentInstance.setBaseMovementRate(variety.getBaseMovementRate());
		currentInstance.setCurrentDevelopmentPoints(variety.getLeftoverDP());
		currentInstance.setCurrentHits(variety.getBaseHits());
		currentInstance.setHeight(variety.getHeight());
		currentInstance.setMaxHits(variety.getBaseHits());
		currentInstance.setRealm(variety.getRealm1());
		currentInstance.setRealm2(variety.getRealm2());
		currentInstance.setWeight(variety.getWeight());
		for(SkillBonus skillBonus : variety.getSkillBonusesList()) {
			currentInstance.getPrimarySkillBonusesList().add(skillBonus.clone());
		}
		for(TalentInstance talentInstance : variety.getTalentInstancesList()) {
			currentInstance.getTalentInstancesList().add(talentInstance.clone());
		}
	}

	private void setCurrentLevel(short level) {
		CreatureArchetypeLevel currentArchetypeLevel = currentInstance.getArchetype().getLevels().get(
				currentInstance.getCurrentLevel());
		CreatureArchetypeLevel newArchetypeLevel = currentInstance.getArchetype().getLevels().get(level);
		for(Map.Entry<Attack, Short> entry : currentInstance.getPrimaryAttackBonusesMap().entrySet()) {
			short bonusDiff = (short) (currentArchetypeLevel.getAttack() - newArchetypeLevel.getAttack());
			currentInstance.getPrimaryAttackBonusesMap().put(entry.getKey(), (short)(entry.getValue() - bonusDiff));
		}
		for(Map.Entry<Attack, Short> entry : currentInstance.getSecondaryAttackBonusesMap().entrySet()) {
			short bonusDiff = (short) (currentArchetypeLevel.getAttack2() - newArchetypeLevel.getAttack2());
			currentInstance.getSecondaryAttackBonusesMap().put(entry.getKey(), (short)(entry.getValue() - bonusDiff));
		}

		for(SkillBonus skillBonus : currentInstance.getPrimarySkillBonusesList()) {
			if(currentInstance.getArchetype().getPrimarySkills().contains(skillBonus.getSkill().getCategory())) {
				short bonusDiff = (short)(currentArchetypeLevel.getPrimeSkill() - newArchetypeLevel.getPrimeSkill());
				skillBonus.setBonus((short)(skillBonus.getBonus() + bonusDiff));
			}
			else {
				short bonusDiff = (short)(currentArchetypeLevel.getSecondarySkill() - newArchetypeLevel.getSecondarySkill());
				skillBonus.setBonus((short)(skillBonus.getBonus() + bonusDiff));
			}
			adjustStat(currentArchetypeLevel, newArchetypeLevel, skillBonus);
		}
		currentInstance.setCurrentLevel(level);
		levelEdit.setText(String.valueOf(level));
		saveItem();
	}

	private void adjustStat(CreatureArchetypeLevel oldLevel, CreatureArchetypeLevel newLevel, SkillBonus skillBonus) {
		for(Statistic statistic : skillBonus.getSkill().getStats()) {
			short diff = 0;
			switch (statistic) {
				case AGILITY:
					diff = (short) (oldLevel.getAgility() - newLevel.getAgility());
					break;
				case CONSTITUTION:
					diff = (short) (oldLevel.getConstitution() - newLevel.getConstitution());
					break;
				case EMPATHY:
					diff = (short) (oldLevel.getEmpathy() - newLevel.getEmpathy());
					break;
				case INTUITION:
					diff = (short) (oldLevel.getIntuition() - newLevel.getIntuition());
					break;
				case MEMORY:
					diff = (short) (oldLevel.getMemory() - newLevel.getMemory());
					break;
				case PRESENCE:
					diff = (short) (oldLevel.getPresence() - newLevel.getPresence());
					break;
				case QUICKNESS:
					diff = (short) (oldLevel.getQuickness() - newLevel.getQuickness());
					break;
				case REASONING:
					diff = (short) (oldLevel.getReasoning() - newLevel.getReasoning());
					break;
				case SELF_DISCIPLINE:
					diff = (short) (oldLevel.getSelfDiscipline() - newLevel.getSelfDiscipline());
					break;
				case STRENGTH:
					diff = (short) (oldLevel.getStrength() - newLevel.getStrength());
					break;
			}
			skillBonus.setBonus((short) (skillBonus.getBonus() - diff));
		}
	}
}
