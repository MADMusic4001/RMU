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
package com.madinnovations.rmu.view.activities.character;

import android.app.DialogFragment;
import android.app.Fragment;
import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.controller.rxhandler.combat.AttackRxHandler;
import com.madinnovations.rmu.controller.rxhandler.common.SkillRxHandler;
import com.madinnovations.rmu.controller.rxhandler.common.SpecializationRxHandler;
import com.madinnovations.rmu.controller.rxhandler.spell.SpellListRxHandler;
import com.madinnovations.rmu.controller.rxhandler.spell.SpellRxHandler;
import com.madinnovations.rmu.controller.utils.ReactiveUtils;
import com.madinnovations.rmu.data.entities.DatabaseObject;
import com.madinnovations.rmu.data.entities.character.Character;
import com.madinnovations.rmu.data.entities.common.Skill;
import com.madinnovations.rmu.data.entities.common.SkillRanks;
import com.madinnovations.rmu.data.entities.common.Specialization;
import com.madinnovations.rmu.data.entities.spells.SpellList;
import com.madinnovations.rmu.view.RMUDragShadowBuilder;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.di.modules.CharacterFragmentModule;
import com.madinnovations.rmu.view.widgets.TooltipDialogFragment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Handles interactions with the UI for character creation.
 */
public class CharacterProfessionalKnacksPageFragment extends Fragment implements GestureDetector.OnGestureListener,
		GestureDetector.OnDoubleTapListener, TooltipDialogFragment.TooltipDialogListener {
	private static final String TAG = "CharacterProfKnacksPage";
	private static final String DRAG_ADD_SKILL = "drag-add-skill";
	private static final String DRAG_REMOVE_PROFESSIONAL_SKILL = "drag-remove-professional-skill";
	private static final String DRAG_REMOVE_KNACK = "drag-remove-knack";
	private static final String SKILL_TYPE_STRING = "skill";
	private static final String SPECIALIZATION_TYPE_STRING = "specialization";
	private static final String SPELL_LIST_TYPE_STRING = "spell-list";
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
	protected ReactiveUtils                reactiveUtils;
	private   ListView                     skillsListView = null;
	private   ArrayAdapter<SkillRanks>     skillsListAdapter;
	private   ListView                     professionalSkillsListView;
	private   ArrayAdapter<SkillRanks>     professionalSkillsListAdapter;
	private   ArrayAdapter<SkillRanks>     knacksListAdapter;
	private   CharactersFragment           charactersFragment;
	private   TooltipDialogFragment        tooltipDialogFragment = null;

	/**
	 * Creates new CharacterProfessionalKnacksPageFragment instance.
	 *
	 * @param charactersFragment  the CharactersFragment instance this fragment is attached to.
	 * @return the new instance.
	 */
	public static CharacterProfessionalKnacksPageFragment newInstance(CharactersFragment charactersFragment) {
		CharacterProfessionalKnacksPageFragment fragment = new CharacterProfessionalKnacksPageFragment();
		fragment.charactersFragment = charactersFragment;
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if(charactersFragment == null) {
			return null;
		}

		((CampaignActivity)getActivity()).getActivityComponent().
				newCharacterFragmentComponent(new CharacterFragmentModule(this)).injectInto(this);

		View fragmentView = inflater.inflate(R.layout.character_profession_knacks_page, container, false);

		initSkillsListView(fragmentView);
		initProfessionalSkillsListView(fragmentView);
		initKnacksListView(fragmentView);

		return fragmentView;
	}

	@Override
	public void onPause() {
		if(copyViewsToItem()) {
			charactersFragment.saveItem();
		}
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		copyItemToViews();
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onDoubleTap(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onDoubleTapEvent(MotionEvent e) {
		int position = skillsListView.pointToPosition((int)e.getX(), (int)e.getY());
		if(position != AdapterView.INVALID_POSITION) {
			SkillRanks skillRanks = skillsListAdapter.getItem(position);
			if (skillRanks != null) {
				String content = null;
				if(skillRanks.getSkill() != null) {
					content = skillRanks.getSkill().getDescription();
				}
				else if(skillRanks.getSpecialization() != null) {
					content = skillRanks.getSpecialization().getDescription();
				}
				if(content != null && tooltipDialogFragment == null) {
					tooltipDialogFragment = new TooltipDialogFragment();
					tooltipDialogFragment.setListener(this);
					tooltipDialogFragment.setMessage(content);
					tooltipDialogFragment.show(getChildFragmentManager(), "tooltip");
				}
			}
		}
		return false;
	}

	@Override
	public void onOk(DialogFragment dialog) {
		tooltipDialogFragment = null;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		return false;
	}

	public boolean copyViewsToItem() {
		boolean changed = false;
		Character character = charactersFragment.getCurrentInstance();

		if(character != null) {
			for (int i = 0; i < professionalSkillsListAdapter.getCount(); i++) {
				SkillRanks skillRanks = professionalSkillsListAdapter.getItem(i);
				if(skillRanks != null) {
					DatabaseObject databaseObject = null;
					if (skillRanks.getSkill() != null) {
						databaseObject = skillRanks.getSkill();
					}
					else if (skillRanks.getSpecialization() != null) {
						databaseObject = skillRanks.getSpecialization();
					}
					else if (skillRanks.getSpellList() != null) {
						databaseObject = skillRanks.getSpellList();
					}
					if (databaseObject != null && !character.getProfessionSkills().contains(databaseObject)) {
						character.getProfessionSkills().add(databaseObject);
						changed = true;
					}
				}
			}
			List<DatabaseObject> professionalSkillsCopy = new ArrayList<>(character.getProfessionSkills());
			for (DatabaseObject databaseObject : professionalSkillsCopy) {
				SkillRanks skillRanks = new SkillRanks();
				if(databaseObject instanceof Skill) {
					skillRanks.setSkill((Skill)databaseObject);
				}
				else if(databaseObject instanceof Specialization) {
					skillRanks.setSpecialization((Specialization)databaseObject);
				}
				else if(databaseObject instanceof SpellList) {
					skillRanks.setSpellList((SpellList)databaseObject);
				}
				if(professionalSkillsListAdapter.getPosition(skillRanks) == AdapterView.INVALID_POSITION) {
					character.getProfessionSkills().remove(databaseObject);
					changed = true;
				}
			}

			for (int i = 0; i < knacksListAdapter.getCount(); i++) {
				SkillRanks skillRanks = knacksListAdapter.getItem(i);
				if(skillRanks != null) {
					DatabaseObject databaseObject = null;
					if (skillRanks.getSkill() != null) {
						databaseObject = skillRanks.getSkill();
					}
					else if (skillRanks.getSpecialization() != null) {
						databaseObject = skillRanks.getSpecialization();
					}
					else if (skillRanks.getSpellList() != null) {
						databaseObject = skillRanks.getSpellList();
					}
					if (databaseObject != null && !character.getKnacks().contains(databaseObject)) {
						character.getKnacks().add(databaseObject);
						changed = true;
					}
				}
			}
			List<DatabaseObject> knacksCopy = new ArrayList<>(character.getProfessionSkills());
			for (DatabaseObject databaseObject : knacksCopy) {
				SkillRanks skillRanks = new SkillRanks();
				if(databaseObject instanceof Skill) {
					skillRanks.setSkill((Skill)databaseObject);
				}
				else if(databaseObject instanceof Specialization) {
					skillRanks.setSpecialization((Specialization)databaseObject);
				}
				else if(databaseObject instanceof SpellList) {
					skillRanks.setSpellList((SpellList)databaseObject);
				}
				if(knacksListAdapter.getPosition(skillRanks) == AdapterView.INVALID_POSITION) {
					character.getKnacks().remove(databaseObject);
					changed = true;
				}
			}
		}

		return changed;
	}

	public void copyItemToViews() {
		if(charactersFragment != null) {
			loadProfessionalSkillsForCharacter();
			loadProfessionalSkills();
			loadKnacks();
		}
	}

	private void initSkillsListView(final View layout) {
		skillsListView = (ListView) layout.findViewById(R.id.skills_list);
		skillsListAdapter = new ArrayAdapter<>(getActivity(), R.layout.single_field_row);
		skillsListView.setAdapter(skillsListAdapter);
		final GestureDetectorCompat detector = new GestureDetectorCompat(getActivity(), this);
		detector.setOnDoubleTapListener(this);

		loadProfessionalSkillsForCharacter();

		skillsListView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				detector.onTouchEvent(event);
				return false;
			}
		});

		skillsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
				ClipData dragData;

				List<View> checkedViews = new ArrayList<>(1);
				SkillRanks skillRanks = skillsListAdapter.getItem(position);
				if(skillRanks != null) {
					String idString = null;
					String typeString = null;
					if(skillRanks.getSkill() != null) {
						idString = String.valueOf(skillRanks.getSkill().getId());
						typeString = SKILL_TYPE_STRING;
					}
					else if(skillRanks.getSpecialization() != null) {
						idString = String.valueOf(skillRanks.getSpecialization().getId());
						typeString = SPECIALIZATION_TYPE_STRING;
					}
					else if(skillRanks.getSpellList() != null) {
						idString = String.valueOf(skillRanks.getSpellList().getId());
						typeString = SPELL_LIST_TYPE_STRING;
					}
					if(idString != null) {
						dragData = new ClipData(DRAG_ADD_SKILL, new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN},
												new ClipData.Item(typeString));
						dragData.addItem(new ClipData.Item(idString));
						checkedViews.add(view);
						View.DragShadowBuilder myShadow = new RMUDragShadowBuilder(checkedViews);

						if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
							view.startDragAndDrop(dragData, myShadow, null, 0);
						}
						else {
							//noinspection deprecation
							view.startDrag(dragData, myShadow, null, 0);
						}
					}
				}
				return false;
			}
		});

		skillsListView.setOnDragListener(new View.OnDragListener() {
			private Drawable targetShape = ResourcesCompat.getDrawable(getActivity().getResources(), R.drawable.drag_target_background, null);
			private Drawable hoverShape  = ResourcesCompat.getDrawable(getActivity().getResources(), R.drawable.drag_hover_background, null);
			private Drawable normalShape = skillsListView.getBackground();

			@Override
			public boolean onDrag(View v, DragEvent event) {
				final int action = event.getAction();

				switch (action) {
					case DragEvent.ACTION_DRAG_STARTED:
						if(event.getClipDescription() != null && (
								DRAG_REMOVE_PROFESSIONAL_SKILL.equals(event.getClipDescription().getLabel()) ||
								DRAG_REMOVE_KNACK.equals(event.getClipDescription().getLabel()))) {
							v.setBackground(targetShape);
							v.invalidate();
						}
						else {
							return false;
						}
						break;
					case DragEvent.ACTION_DRAG_ENTERED:
						if(event.getClipDescription() != null && (
								DRAG_REMOVE_PROFESSIONAL_SKILL.equals(event.getClipDescription().getLabel()) ||
										DRAG_REMOVE_KNACK.equals(event.getClipDescription().getLabel()))) {
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
						if(event.getClipDescription() != null && (
								DRAG_REMOVE_PROFESSIONAL_SKILL.equals(event.getClipDescription().getLabel()) ||
										DRAG_REMOVE_KNACK.equals(event.getClipDescription().getLabel()))) {
							v.setBackground(targetShape);
							v.invalidate();
						}
						else {
							return false;
						}
						break;
					case DragEvent.ACTION_DROP:
						if(event.getClipDescription() != null) {
							ClipData.Item typeItem = event.getClipData().getItemAt(0);
							ClipData.Item idItem = event.getClipData().getItemAt(1);
							if(DRAG_REMOVE_PROFESSIONAL_SKILL.equals(event.getClipDescription().getLabel())) {
								SkillRanks skillRanks = new SkillRanks();
								DatabaseObject databaseObject = null;
								if(SKILL_TYPE_STRING.equals(typeItem.getText().toString())) {
									int skillId = Integer.valueOf(idItem.getText().toString());
									Skill skill = new Skill(skillId);
									skillRanks.setSkill(skill);
									databaseObject = skill;
								}
								else if(SPECIALIZATION_TYPE_STRING.equals(typeItem.getText().toString())) {
									int specializationId = Integer.valueOf(idItem.getText().toString());
									Specialization specialization = new Specialization(specializationId);
									skillRanks.setSpecialization(specialization);
									databaseObject = specialization;
								}
								else if(SPELL_LIST_TYPE_STRING.equals(typeItem.getText().toString())) {
									int spellListId = Integer.valueOf(idItem.getText().toString());
									SpellList spellList = new SpellList(spellListId);
									skillRanks.setSpellList(spellList);
									databaseObject = spellList;
								}
								int position = professionalSkillsListAdapter.getPosition(skillRanks);
								if(position != AdapterView.INVALID_POSITION) {
									skillRanks = professionalSkillsListAdapter.getItem(position);
									charactersFragment.getCurrentInstance().getProfessionSkills().remove(databaseObject);
									charactersFragment.saveItem();
									professionalSkillsListAdapter.remove(skillRanks);
									professionalSkillsListAdapter.notifyDataSetChanged();
								}
								v.setBackground(normalShape);
								v.invalidate();
							}
							else if(DRAG_REMOVE_KNACK.equals(event.getClipDescription().getLabel())) {
								SkillRanks skillRanks = new SkillRanks();
								DatabaseObject databaseObject = null;
								if(SKILL_TYPE_STRING.equals(typeItem.getText().toString())) {
									int skillId = Integer.valueOf(idItem.getText().toString());
									Skill skill = new Skill();
									skill.setId(skillId);
									skillRanks.setSkill(skill);
									databaseObject = skill;
								}
								else if(SPECIALIZATION_TYPE_STRING.equals(typeItem.getText().toString())) {
									int specializationId = Integer.valueOf(idItem.getText().toString());
									Specialization specialization = new Specialization();
									specialization.setId(specializationId);
									skillRanks.setSpecialization(specialization);
									databaseObject = specialization;
								}
								else if(SPELL_LIST_TYPE_STRING.equals(typeItem.getText().toString())) {
									int spellListId = Integer.valueOf(idItem.getText().toString());
									SpellList spellList = new SpellList(spellListId);
									skillRanks.setSpellList(spellList);
									databaseObject = spellList;
								}
								int position = knacksListAdapter.getPosition(skillRanks);
								if(position != AdapterView.INVALID_POSITION) {
									skillRanks = knacksListAdapter.getItem(position);
									charactersFragment.getCurrentInstance().getKnacks().remove(databaseObject);
									charactersFragment.saveItem();
									knacksListAdapter.remove(skillRanks);
									knacksListAdapter.notifyDataSetChanged();
								}
								v.setBackground(normalShape);
								v.invalidate();
							}
							else {
								return false;
							}
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

	private void initProfessionalSkillsListView(final View layout) {
		professionalSkillsListView = (ListView) layout.findViewById(R.id.professional_skills_list);
		professionalSkillsListAdapter = new ArrayAdapter<>(getActivity(), R.layout.single_field_row);
		professionalSkillsListView.setAdapter(professionalSkillsListAdapter);

		loadProfessionalSkills();

		professionalSkillsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				ClipData dragData = null;

				List<View> checkedViews = new ArrayList<>(1);
				SkillRanks skillRanks = professionalSkillsListAdapter.getItem(position);
				if(skillRanks != null) {
					if(skillRanks.getSkill() != null) {
						String idString = String.valueOf(skillRanks.getSkill().getId());
						dragData = new ClipData(DRAG_REMOVE_PROFESSIONAL_SKILL,
												new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN},
												new ClipData.Item(SKILL_TYPE_STRING));
						dragData.addItem(new ClipData.Item(idString));
					}
					else if(skillRanks.getSpecialization() != null) {
						String idString = String.valueOf(skillRanks.getSpecialization().getId());
						dragData = new ClipData(DRAG_REMOVE_PROFESSIONAL_SKILL,
												new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN},
												new ClipData.Item(SPECIALIZATION_TYPE_STRING));
						dragData.addItem(new ClipData.Item(idString));
					}
					else if(skillRanks.getSpellList() != null) {
						String idString = String.valueOf(skillRanks.getSpellList().getId());
						dragData = new ClipData(DRAG_REMOVE_PROFESSIONAL_SKILL,
												new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN},
												new ClipData.Item(SPELL_LIST_TYPE_STRING));
						dragData.addItem(new ClipData.Item(idString));
					}
					if(dragData != null) {
						checkedViews.add(view);
						View.DragShadowBuilder myShadow = new RMUDragShadowBuilder(checkedViews);
						if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
							view.startDragAndDrop(dragData, myShadow, null, 0);
						}
						else {
							//noinspection deprecation
							view.startDrag(dragData, myShadow, null, 0);
						}
					}
				}

				return false;
			}
		});

		professionalSkillsListView.setOnDragListener(new View.OnDragListener() {
			private Drawable targetShape = ResourcesCompat.getDrawable(getActivity().getResources(), R.drawable.drag_target_background, null);
			private Drawable hoverShape  = ResourcesCompat.getDrawable(getActivity().getResources(), R.drawable.drag_hover_background, null);
			private Drawable normalShape = professionalSkillsListView.getBackground();

			@Override
			public boolean onDrag(View v, DragEvent event) {
				final int action = event.getAction();
				Character character = charactersFragment.getCurrentInstance();

				switch (action) {
					case DragEvent.ACTION_DRAG_STARTED:
						if(event.getClipDescription() != null && DRAG_ADD_SKILL.equals(event.getClipDescription().getLabel())) {
							v.setBackground(targetShape);
							v.invalidate();
						}
						else {
							return false;
						}
						break;
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
							ClipData.Item typeItem = event.getClipData().getItemAt(0);
							ClipData.Item idItem = event.getClipData().getItemAt(1);
							if(SKILL_TYPE_STRING.equals(typeItem.getText().toString())) {
								int id = Integer.valueOf(idItem.getText().toString());
								SkillRanks skillRanks = new SkillRanks();
								Skill skill = new Skill(id);
								skillRanks.setSkill(skill);
								int position = skillsListAdapter.getPosition(skillRanks);
								if(position != AdapterView.INVALID_POSITION) {
									skillRanks = skillsListAdapter.getItem(position);
									if(skillRanks != null) {
										character.getProfessionSkills().add(skillRanks.getSkill());
										professionalSkillsListAdapter.add(skillRanks);
										professionalSkillsListAdapter.notifyDataSetChanged();
									}
								}
							}
							else if(SPECIALIZATION_TYPE_STRING.equals(typeItem.getText().toString())) {
								int id = Integer.valueOf(idItem.getText().toString());
								SkillRanks skillRanks = new SkillRanks();
								Specialization specialization = new Specialization(id);
								skillRanks.setSpecialization(specialization);
								int position = skillsListAdapter.getPosition(skillRanks);
								if(position != AdapterView.INVALID_POSITION) {
									skillRanks = skillsListAdapter.getItem(position);
									if(skillRanks != null) {
										character.getProfessionSkills().add(skillRanks.getSpecialization());
										professionalSkillsListAdapter.add(skillRanks);
										professionalSkillsListAdapter.notifyDataSetChanged();
									}
								}
							}
							else if(SPELL_LIST_TYPE_STRING.equals(typeItem.getText().toString())) {
								int id = Integer.valueOf(idItem.getText().toString());
								SkillRanks skillRanks = new SkillRanks();
								SpellList spellList = new SpellList(id);
								skillRanks.setSpellList(spellList);
								int position = skillsListAdapter.getPosition(skillRanks);
								if(position != AdapterView.INVALID_POSITION) {
									skillRanks = skillsListAdapter.getItem(position);
									if(skillRanks != null) {
										character.getProfessionSkills().add(skillRanks.getSpellList());
										professionalSkillsListAdapter.add(skillRanks);
										professionalSkillsListAdapter.notifyDataSetChanged();
									}
								}
							}
							charactersFragment.saveItem();
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

	private void initKnacksListView(final View layout) {
		final ListView knacksListView = (ListView) layout.findViewById(R.id.knacks_list);
		knacksListAdapter = new ArrayAdapter<>(getActivity(), R.layout.single_field_row);
		knacksListView.setAdapter(knacksListAdapter);
		final Character character = charactersFragment.getCurrentInstance();

		loadKnacks();

		knacksListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				ClipData dragData = null;

				List<View> checkedViews = new ArrayList<>(1);
				SkillRanks skillRanks = knacksListAdapter.getItem(position);
				if(skillRanks != null) {
					if(skillRanks.getSkill() != null) {
						String idString = String.valueOf(skillRanks.getSkill().getId());
						dragData = new ClipData(DRAG_REMOVE_KNACK,
												new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN},
												new ClipData.Item(SKILL_TYPE_STRING));
						dragData.addItem(new ClipData.Item(idString));
					}
					else if(skillRanks.getSpecialization() != null) {
						String idString = String.valueOf(skillRanks.getSpecialization().getId());
						dragData = new ClipData(DRAG_REMOVE_KNACK,
												new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN},
												new ClipData.Item(SPECIALIZATION_TYPE_STRING));
						dragData.addItem(new ClipData.Item(idString));
					}
					else if(skillRanks.getSpellList() != null) {
						String idString = String.valueOf(skillRanks.getSpellList().getId());
						dragData = new ClipData(DRAG_REMOVE_KNACK,
												new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN},
												new ClipData.Item(SPELL_LIST_TYPE_STRING));
						dragData.addItem(new ClipData.Item(idString));
					}
					if(dragData != null) {
						checkedViews.add(view);
						View.DragShadowBuilder myShadow = new RMUDragShadowBuilder(checkedViews);
						if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
							view.startDragAndDrop(dragData, myShadow, null, 0);
						}
						else {
							//noinspection deprecation
							view.startDrag(dragData, myShadow, null, 0);
						}
					}
				}

				return false;
			}
		});

		knacksListView.setOnDragListener(new View.OnDragListener() {
			private Drawable targetShape = ResourcesCompat.getDrawable(getActivity().getResources(), R.drawable.drag_target_background, null);
			private Drawable hoverShape  = ResourcesCompat.getDrawable(getActivity().getResources(), R.drawable.drag_hover_background, null);
			private Drawable normalShape = professionalSkillsListView.getBackground();

			@Override
			public boolean onDrag(View v, DragEvent event) {
				final int action = event.getAction();

				switch (action) {
					case DragEvent.ACTION_DRAG_STARTED:
						if(event.getClipDescription() != null && DRAG_ADD_SKILL.equals(event.getClipDescription().getLabel())) {
							v.setBackground(targetShape);
							v.invalidate();
						}
						else {
							return false;
						}
						break;
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
							ClipData.Item typeItem = event.getClipData().getItemAt(0);
							ClipData.Item idItem = event.getClipData().getItemAt(1);
							if(SKILL_TYPE_STRING.equals(typeItem.getText().toString())) {
								int id = Integer.valueOf(idItem.getText().toString());
								SkillRanks skillRanks = new SkillRanks();
								Skill skill = new Skill(id);
								skillRanks.setSkill(skill);
								int position = skillsListAdapter.getPosition(skillRanks);
								if(position != AdapterView.INVALID_POSITION) {
									skillRanks = skillsListAdapter.getItem(position);
									if(skillRanks != null) {
										character.getKnacks().add(skillRanks.getSkill());
										knacksListAdapter.add(skillRanks);
										knacksListAdapter.notifyDataSetChanged();
									}
								}
							}
							else if(SPECIALIZATION_TYPE_STRING.equals(typeItem.getText().toString())) {
								int id = Integer.valueOf(idItem.getText().toString());
								SkillRanks skillRanks = new SkillRanks();
								Specialization specialization = new Specialization(id);
								skillRanks.setSpecialization(specialization);
								int position = skillsListAdapter.getPosition(skillRanks);
								if(position != AdapterView.INVALID_POSITION) {
									skillRanks = skillsListAdapter.getItem(position);
									if(skillRanks != null) {
										character.getKnacks().add(skillRanks.getSpecialization());
										knacksListAdapter.add(skillRanks);
										knacksListAdapter.notifyDataSetChanged();
									}
								}
							}
							else if(SPELL_LIST_TYPE_STRING.equals(typeItem.getText().toString())) {
								int id = Integer.valueOf(idItem.getText().toString());
								SkillRanks skillRanks = new SkillRanks();
								SpellList spellList = new SpellList(id);
								skillRanks.setSpellList(spellList);
								int position = skillsListAdapter.getPosition(skillRanks);
								if(position != AdapterView.INVALID_POSITION) {
									skillRanks = skillsListAdapter.getItem(position);
									if(skillRanks != null) {
										character.getKnacks().add(skillRanks.getSpellList());
										knacksListAdapter.add(skillRanks);
										knacksListAdapter.notifyDataSetChanged();
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

	private void loadProfessionalSkillsForCharacter() {
		if(charactersFragment != null && charactersFragment.getCurrentInstance() != null
				&& charactersFragment.getCurrentInstance().getProfession() != null) {
			skillsListAdapter.clear();
			skillRxHandler.getCharacterProfessionalSkills(charactersFragment.getCurrentInstance())
					.subscribe(new Subscriber<Collection<DatabaseObject>>() {
						@Override
						public void onCompleted() {}
						@Override
						public void onError(Throwable e) {
								Log.e(TAG, "onError: Exception caught getting character purchasable skills", e);
							}
						@Override
						public void onNext(Collection<DatabaseObject> skillCollection) {
							List<SkillRanks> skillRanksList = new ArrayList<>();
							skillsListAdapter.clear();
							for (DatabaseObject databaseObject: skillCollection) {
								if(databaseObject instanceof Skill) {
									Skill skill = (Skill)databaseObject;
									if (skill.isRequiresSpecialization()) {
										for(Specialization specialization : skill.getSpecializations()) {
											SkillRanks skillRanks = new SkillRanks();
											skillRanks.setSpecialization(specialization);
											skillRanksList.add(skillRanks);
										}
									}
									else {
										SkillRanks skillRanks = new SkillRanks();
										skillRanks.setSkill(skill);
										skillRanksList.add(skillRanks);
									}
								}
								else if(databaseObject instanceof SpellList) {
									SpellList spellList = (SpellList)databaseObject;
									SkillRanks skillRanks = new SkillRanks();
									skillRanks.setSpellList(spellList);
									skillRanksList.add(skillRanks);
								}
							}
							//noinspection unchecked
							Collections.sort(skillRanksList);
							skillsListAdapter.addAll(skillRanksList);
							skillsListAdapter.notifyDataSetChanged();
						}
					});
		}
	}

	private void loadProfessionalSkills() {
		if(charactersFragment != null && charactersFragment.getCurrentInstance() != null) {
			professionalSkillsListAdapter.clear();
			for (DatabaseObject databaseObject : charactersFragment.getCurrentInstance().getProfessionSkills()) {
				SkillRanks skillRanks = new SkillRanks();
				if (databaseObject instanceof Skill) {
					skillRanks.setSkill((Skill)databaseObject);
				}
				else if (databaseObject instanceof Specialization) {
					skillRanks.setSpecialization((Specialization)databaseObject);
				}
				else if (databaseObject instanceof SpellList) {
					skillRanks.setSpellList((SpellList)databaseObject);
				}
				professionalSkillsListAdapter.add(skillRanks);
			}
			professionalSkillsListAdapter.notifyDataSetChanged();
		}
	}

	private void loadKnacks() {
		if(charactersFragment != null && charactersFragment.getCurrentInstance() != null) {
			knacksListAdapter.clear();
			for (DatabaseObject databaseObject : charactersFragment.getCurrentInstance().getKnacks()) {
				SkillRanks skillRanks = new SkillRanks();
				if (databaseObject instanceof Skill) {
					skillRanks.setSkill((Skill) databaseObject);
				}
				else if (databaseObject instanceof Specialization) {
					skillRanks.setSpecialization((Specialization) databaseObject);
				}
				else if (databaseObject instanceof SpellList) {
					skillRanks.setSpellList((SpellList)databaseObject);
				}
				knacksListAdapter.add(skillRanks);
			}
			knacksListAdapter.notifyDataSetChanged();
		}
	}
}
