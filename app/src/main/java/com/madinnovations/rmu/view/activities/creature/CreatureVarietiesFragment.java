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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ContextMenu;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.controller.rxhandler.common.SizeRxHandler;
import com.madinnovations.rmu.controller.rxhandler.common.StatRxHandler;
import com.madinnovations.rmu.controller.rxhandler.common.TalentRxHandler;
import com.madinnovations.rmu.controller.rxhandler.creature.CreatureTypeRxHandler;
import com.madinnovations.rmu.controller.rxhandler.creature.CreatureVarietyRxHandler;
import com.madinnovations.rmu.data.entities.common.Size;
import com.madinnovations.rmu.data.entities.common.Stat;
import com.madinnovations.rmu.data.entities.common.Talent;
import com.madinnovations.rmu.data.entities.common.TalentTier;
import com.madinnovations.rmu.data.entities.creature.CreatureLevelSpreadTable;
import com.madinnovations.rmu.data.entities.creature.CreatureType;
import com.madinnovations.rmu.data.entities.creature.CreatureVariety;
import com.madinnovations.rmu.data.entities.creature.RacialStatBonus;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.adapters.TwoFieldListAdapter;
import com.madinnovations.rmu.view.adapters.common.SizeSpinnerAdapter;
import com.madinnovations.rmu.view.adapters.common.TalentNamesListAdapter;
import com.madinnovations.rmu.view.adapters.common.TalentTierListAdapter;
import com.madinnovations.rmu.view.adapters.creature.CreatureTypeSpinnerAdapter;
import com.madinnovations.rmu.view.adapters.creature.LevelSpreadSpinnerAdapter;
import com.madinnovations.rmu.view.adapters.creature.RacialStatBonusListAdapter;
import com.madinnovations.rmu.view.di.modules.CreatureFragmentModule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Handles interactions with the UI for creature varieties.
 */
public class CreatureVarietiesFragment extends Fragment implements TwoFieldListAdapter.GetValues<CreatureVariety>,
		RacialStatBonusListAdapter.SetRacialStatBonus, TalentTierListAdapter.SetTalentTier {
	private static final String DRAG_ADD_TALENT = "add-talent";
	private static final String DRAG_REMOVE_TALENT = "remove-talent";
	@Inject
	protected CreatureVarietyRxHandler   creatureVarietyRxHandler;
	@Inject
	protected CreatureTypeRxHandler      creatureTypeRxHandler;
	@Inject
	protected StatRxHandler              statRxHandler;
	@Inject
	protected SizeRxHandler              sizeRxHandler;
	@Inject
	protected TalentRxHandler            talentRxHandler;
	@Inject
	protected CreatureTypeSpinnerAdapter creatureTypeSpinnerAdapter;
	@Inject
	protected SizeSpinnerAdapter         sizeSpinnerAdapter;
	@Inject
	protected LevelSpreadSpinnerAdapter  levelSpreadSpinnerAdapter;
	@Inject
	protected TalentNamesListAdapter     talentNamesListAdapter;
	protected RacialStatBonusListAdapter racialStatBonusListAdapter;
	protected TalentTierListAdapter talentTiersListAdapter;
	private   TwoFieldListAdapter<CreatureVariety> listAdapter;
	private   ListView                    listView;
	private   EditText                    nameEdit;
	private   EditText                    descriptionEdit;
	private   Spinner                     creatureTypeSpinner;
	private   Spinner                     levelSpreadSpinner;
	private   Spinner                     sizeSpinner;
	private   ListView                    racialStatBonusList;
	private   ListView                    talentNamesList;
	private   ListView                    talentTiersList;
	private CreatureVariety currentInstance = new CreatureVariety();
	private boolean         isNew           = true;
	private int             touchPositionX;
	private int             touchPositionY;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((CampaignActivity)getActivity()).getActivityComponent().
				newCreatureFragmentComponent(new CreatureFragmentModule(this)).injectInto(this);

		View layout = inflater.inflate(R.layout.creature_varieties_fragment, container, false);

		((TextView)layout.findViewById(R.id.header_field1)).setText(getString(R.string.label_creature_variety_name));
		((TextView)layout.findViewById(R.id.header_field2)).setText(getString(R.string.label_creature_variety_description));

		initNameEdit(layout);
		initDescriptionEdit(layout);
		initCretureTypeSpinner(layout);
		initLevelSpreadEdit(layout);
		initSizeSpinner(layout);
		initRacialStatBonusList(layout);
		initTalentNamesList(layout);
		initTalentTiersList(layout);
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
		inflater.inflate(R.menu.creature_varieties_action_bar, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if(id == R.id.action_new_creature_variety) {
			if(copyViewsToItem()) {
				saveItem();
			}
			currentInstance = new CreatureVariety();
			isNew = true;
			copyItemToViews();
			listView.clearChoices();
			listAdapter.notifyDataSetChanged();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		getActivity().getMenuInflater().inflate(R.menu.creature_variety_context_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		final CreatureVariety creatureVariety;

		AdapterView.AdapterContextMenuInfo info =
				(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

		switch (item.getItemId()) {
			case R.id.context_new_creature_variety:
				if(copyViewsToItem()) {
					saveItem();
				}
				currentInstance = new CreatureVariety();
				isNew = true;
				copyItemToViews();
				listView.clearChoices();
				listAdapter.notifyDataSetChanged();
				return true;
			case R.id.context_delete_creature_variety:
				creatureVariety = (CreatureVariety)listView.getItemAtPosition(info.position);
				if(creatureVariety != null) {
					deleteItem(creatureVariety);
					return true;
				}
		}
		return super.onContextItemSelected(item);
	}

	private boolean copyViewsToItem() {
		boolean changed = false;
		String newValue;
		int position;
		CreatureType newType;
		Size newSize;
		char newLevelSpread;

		newValue = nameEdit.getText().toString();
		if(newValue.isEmpty()) {
			newValue = null;
		}
		if((newValue == null && currentInstance.getName() != null) ||
				(newValue != null && !newValue.equals(currentInstance.getName()))) {
			currentInstance.setName(newValue);
			changed = true;
		}

		newValue = descriptionEdit.getText().toString();
		if(newValue.isEmpty()) {
			newValue = null;
		}
		if((newValue == null && currentInstance.getDescription() != null) ||
				(newValue != null && !newValue.equals(currentInstance.getDescription()))) {
			currentInstance.setDescription(newValue);
			changed = true;
		}

		position = creatureTypeSpinner.getSelectedItemPosition();
		if(position != -1) {
			newType = creatureTypeSpinnerAdapter.getItem(position);
			if(!newType.equals(currentInstance.getType())) {
				currentInstance.setType(newType);
				changed = true;
			}
		}

		position = sizeSpinner.getSelectedItemPosition();
		if(position != -1) {
			newSize = sizeSpinnerAdapter.getItem(position);
			if(!newSize.equals(currentInstance.getSize())) {
				currentInstance.setSize(newSize);
				changed = true;
			}
		}

		position = levelSpreadSpinner.getSelectedItemPosition();
		if(position != -1) {
			newLevelSpread = levelSpreadSpinnerAdapter.getItem(position);
			if(newLevelSpread != currentInstance.getLevelSpread()) {
				currentInstance.setLevelSpread(newLevelSpread);
				changed = true;
			}
		}

		return changed;
	}

	private void copyItemToViews() {
		nameEdit.setText(currentInstance.getName());
		descriptionEdit.setText(currentInstance.getDescription());
		creatureTypeSpinner.setSelection(creatureTypeSpinnerAdapter.getPosition(currentInstance.getType()));
		sizeSpinner.setSelection(sizeSpinnerAdapter.getPosition(currentInstance.getSize()));
		levelSpreadSpinner.setSelection(levelSpreadSpinnerAdapter.getPosition(currentInstance.getLevelSpread()));

		if(currentInstance.getName() != null && !currentInstance.getName().isEmpty()) {
			nameEdit.setError(null);
		}
		if(currentInstance.getDescription() != null && !currentInstance.getDescription().isEmpty()) {
			descriptionEdit.setError(null);
		}
	}

	private void saveItem() {
		if(currentInstance.isValid()) {
			final boolean wasNew = isNew;
			isNew = false;
			creatureVarietyRxHandler.save(currentInstance)
					.observeOn(AndroidSchedulers.mainThread())
					.subscribeOn(Schedulers.io())
					.subscribe(new Subscriber<CreatureVariety>() {
						@Override
						public void onCompleted() {}
						@Override
						public void onError(Throwable e) {
							Log.e("CreatureVarietiesFrag", "Exception saving new CreatureVariety: " + currentInstance, e);
							Toast.makeText(getActivity(), getString(R.string.toast_creature_variety_save_failed), Toast.LENGTH_SHORT).show();
						}
						@Override
						public void onNext(CreatureVariety savedItem) {
							if (wasNew) {
								listAdapter.add(savedItem);
								if(savedItem == currentInstance) {
									listView.setSelection(listAdapter.getPosition(savedItem));
									listView.setItemChecked(listAdapter.getPosition(savedItem), true);
								}
								listAdapter.notifyDataSetChanged();
							}
							if(getActivity() != null) {
								Toast.makeText(getActivity(), getString(R.string.toast_creature_variety_saved), Toast.LENGTH_SHORT).show();
								int position = listAdapter.getPosition(savedItem);
								LinearLayout v = (LinearLayout) listView.getChildAt(position - listView.getFirstVisiblePosition());
								if (v != null) {
									TextView textView = (TextView) v.findViewById(R.id.row_field1);
									textView.setText(savedItem.getName());
									textView = (TextView) v.findViewById(R.id.row_field2);
									textView.setText(savedItem.getDescription());
								}
							}
						}
					});
		}
	}

	private void deleteItem(@NonNull final CreatureVariety item) {
		creatureVarietyRxHandler.deleteById(item.getId())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new Subscriber<Boolean>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e("CreatureVarietiesFrag", "Exception when deleting: " + item, e);
						String toastString = getString(R.string.toast_creature_variety_delete_failed);
						Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
					}
					@Override
					public void onNext(Boolean success) {
						if(success) {
							int position = listAdapter.getPosition(item);
							if(position == listAdapter.getCount() -1) {
								position--;
							}
							listAdapter.remove(item);
							listAdapter.notifyDataSetChanged();
							if(position >= 0) {
								listView.setSelection(position);
								listView.setItemChecked(position, true);
								currentInstance = listAdapter.getItem(position);
							}
							else {
								currentInstance = new CreatureVariety();
								isNew = true;
							}
							copyItemToViews();
							Toast.makeText(getActivity(), getString(R.string.toast_creature_variety_deleted), Toast.LENGTH_SHORT).show();
						}
					}
				});
	}

	private void initNameEdit(View layout) {
		nameEdit = (EditText)layout.findViewById(R.id.name_edit);
		nameEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length() == 0 && nameEdit != null) {
					nameEdit.setError(getString(R.string.validation_creature_variety_name_required));
				}
			}
		});
		nameEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					final String newName = nameEdit.getText().toString();
					if (!newName.equals(currentInstance.getName())) {
						currentInstance.setName(newName);
						saveItem();
					}
				}
			}
		});
	}

	private void initDescriptionEdit(View layout) {
		descriptionEdit = (EditText)layout.findViewById(R.id.description_edit);
		descriptionEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length() == 0 && descriptionEdit != null) {
					descriptionEdit.setError(getString(R.string.validation_creature_variety_description_required));
				}
			}
		});
		descriptionEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					final String newDescription = descriptionEdit.getText().toString();
					if (!newDescription.equals(currentInstance.getDescription())) {
						currentInstance.setDescription(newDescription);
						saveItem();
					}
				}
			}
		});
	}

	private void initCretureTypeSpinner(View layout) {
		creatureTypeSpinner = (Spinner)layout.findViewById(R.id.creature_type_spinner);
		creatureTypeSpinner.setAdapter(creatureTypeSpinnerAdapter);

		creatureTypeRxHandler.getAll()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Collection<CreatureType>>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e("CreatureVarietiesFrag", "Exception caught getting all CreatureType instances", e);
					}
					@Override
					public void onNext(Collection<CreatureType> items) {
						creatureTypeSpinnerAdapter.clear();
						creatureTypeSpinnerAdapter.addAll(items);
						creatureTypeSpinnerAdapter.notifyDataSetChanged();
					}
				});

		creatureTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if(currentInstance.getType() == null || creatureTypeSpinnerAdapter.getPosition(currentInstance.getType()) != position) {
					currentInstance.setType(creatureTypeSpinnerAdapter.getItem(position));
					saveItem();
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				if(currentInstance.getType() != null) {
					currentInstance.setType(null);
					saveItem();
				}
			}
		});
	}

	private void initLevelSpreadEdit(View layout) {
		levelSpreadSpinner = (Spinner)layout.findViewById(R.id.level_spread_spinner);
		levelSpreadSpinner.setAdapter(levelSpreadSpinnerAdapter);

		levelSpreadSpinnerAdapter.clear();
		levelSpreadSpinnerAdapter.addAll(Arrays.asList(CreatureLevelSpreadTable.LEVEL_SPREAD_CODES));
		levelSpreadSpinnerAdapter.notifyDataSetChanged();

		levelSpreadSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				char newLevelSpread = levelSpreadSpinnerAdapter.getItem(position);
				if(newLevelSpread != currentInstance.getLevelSpread()) {
					currentInstance.setLevelSpread(newLevelSpread);
					saveItem();
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});
	}

	private void initSizeSpinner(View layout) {
		sizeSpinner = (Spinner)layout.findViewById(R.id.size_spinner);
		sizeSpinner.setAdapter(sizeSpinnerAdapter);

		sizeRxHandler.getAll()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Collection<Size>>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e("CreatureVarietiesFrag", "Exception caught getting all Size instances", e);
					}
					@Override
					public void onNext(Collection<Size> items) {
						sizeSpinnerAdapter.clear();
						sizeSpinnerAdapter.addAll(items);
						sizeSpinnerAdapter.notifyDataSetChanged();
					}
				});

		sizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if(currentInstance.getSize() == null || sizeSpinnerAdapter.getPosition(currentInstance.getSize()) != position) {
					currentInstance.setSize(sizeSpinnerAdapter.getItem(position));
					saveItem();
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				if(currentInstance.getSize() != null) {
					currentInstance.setSize(null);
					saveItem();
				}
			}
		});
	}

	private void initRacialStatBonusList(View layout) {
		racialStatBonusList = (ListView) layout.findViewById(R.id.racial_stat_bonuses_list);
		racialStatBonusListAdapter = new RacialStatBonusListAdapter(this.getActivity(), this);
		racialStatBonusList.setAdapter(racialStatBonusListAdapter);

		statRxHandler.getAll()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Collection<Stat>>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e("CreatureVarietiesFrag",
								"Exception caught getting all Stat instances in initRacialStatBonusList", e);
					}
					@Override
					public void onNext(Collection<Stat> stats) {
						for(Stat stat : stats) {
							if(!currentInstance.getRacialStatBonuses().containsKey(stat)) {
								currentInstance.getRacialStatBonuses().put(stat, (short)0);
							}
						}
						Collection<RacialStatBonus> listItems = new ArrayList<>(currentInstance.getRacialStatBonuses().size());
						for(Map.Entry<Stat, Short> entry : currentInstance.getRacialStatBonuses().entrySet()) {
							listItems.add(new RacialStatBonus(entry.getKey(), entry.getValue()));
						}
						racialStatBonusListAdapter.clear();
						racialStatBonusListAdapter.addAll(listItems);
						racialStatBonusListAdapter.notifyDataSetChanged();
					}
				});
	}

	private void initTalentNamesList(View layout) {
		talentNamesList = (ListView) layout.findViewById(R.id.talents_list);
		talentNamesList.setAdapter(talentNamesListAdapter);

		talentRxHandler.getAll()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new Subscriber<Collection<Talent>>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e("CreatureVarietiesFrag", "Exception caught getting all Talent instances in initTalentNamesList", e);
					}
					@Override
					public void onNext(Collection<Talent> talents) {
						talentNamesListAdapter.clear();
						talentNamesListAdapter.addAll(talents);
						talentNamesListAdapter.notifyDataSetChanged();
					}
				});

		talentNamesList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
				if(!talentNamesList.isItemChecked(position)) {
					talentNamesList.setItemChecked(position, true);
				}
				ClipData dragData = null;

				SparseBooleanArray checkedItems = talentNamesList.getCheckedItemPositions();
				View[] checkedViews = new View[talentNamesList.getCheckedItemCount()];
				int index = 0;
				for(int i = 0; i < checkedItems.size(); i++) {
					int currentPosition = checkedItems.keyAt(i);
					Talent talent = talentNamesListAdapter.getItem(currentPosition);
					if(talent != null) {
						String talentIdString = String.valueOf(talent.getId());
						ClipData.Item clipDataItem = new ClipData.Item(talentIdString);
						if(dragData == null) {
							dragData = new ClipData(DRAG_ADD_TALENT, new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, clipDataItem);
						}
						else {
							dragData.addItem(clipDataItem);
						}
						checkedViews[index++] = getViewByPosition(i, talentNamesList);
					}
				}
				View.DragShadowBuilder myShadow = new TalentTierDragShadowBuilder(checkedViews);

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

		talentNamesList.setOnDragListener(new TalentNamesDragListener());
	}

	private void initTalentTiersList(View layout) {
		talentTiersList = (ListView) layout.findViewById(R.id.talent_tiers_list);
		talentTiersListAdapter = new TalentTierListAdapter(this.getActivity(), this);
		talentTiersList.setAdapter(talentTiersListAdapter);
//		talentTiersList.setItemsCanFocus(true);

		talentTiersListAdapter.clear();
		for(Map.Entry<Talent, Short> entry : currentInstance.getTalentTiersMap().entrySet()) {
			talentTiersListAdapter.add(new TalentTier(entry.getKey(), entry.getValue()));
		}
		talentTiersListAdapter.notifyDataSetChanged();

		talentTiersList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				Log.d("RMU", "In talentTiersList.onLongClick");
				if(!talentTiersList.isItemChecked(position)) {
					talentTiersList.setItemChecked(position, true);
				}
				ClipData dragData = null;

				SparseBooleanArray checkedItems = talentTiersList.getCheckedItemPositions();
				View[] checkedViews = new View[talentTiersList.getCheckedItemCount()];
				int index = 0;
				for(int i = 0; i < checkedItems.size(); i++) {
					int currentPosition = checkedItems.keyAt(i);
					TalentTier talentTier = talentTiersListAdapter.getItem(currentPosition);
					if(checkedItems.valueAt(i) && talentTier != null) {
						String talentIdString = String.valueOf(talentTier.getTalent().getId());
						ClipData.Item clipDataItem = new ClipData.Item(talentIdString);
						if(dragData == null) {
							dragData = new ClipData(DRAG_REMOVE_TALENT, new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, clipDataItem);
						}
						else {
							dragData.addItem(clipDataItem);
						}
						checkedViews[index++] = getViewByPosition(i, talentTiersList);
					}
				}
				View[] finalCheckedViews;
				if(index < checkedViews.length) {
					finalCheckedViews = Arrays.copyOf(checkedViews, index);
				}
				else {
					finalCheckedViews = checkedViews;
				}
				View.DragShadowBuilder myShadow = new TalentTierDragShadowBuilder(finalCheckedViews);

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

		talentTiersList.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				touchPositionX = (int)event.getX();
				touchPositionY = (int)event.getY();
				return false;
			}
		});

		talentTiersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Log.d("RMU", "In talentTiersList.onItemClick");

//				EditText tiersEdit = (EditText)view.findViewById(R.id.talent_tiers_edit);
//				if(tiersEdit != null) {
//					Rect editTextRect = new Rect();
//					tiersEdit.getHitRect(editTextRect);
//					if(editTextRect.contains(touchPositionX, touchPositionY)) {
//						tiersEdit.requestFocus();
//					}
//				}
//				talentTiersList.setItemChecked(position, !talentTiersList.isItemChecked(position));
			}
		});

		talentTiersList.setOnDragListener(new TalentTierDragListener());
	}

	private void initListView(View layout) {
		listView = (ListView) layout.findViewById(R.id.list_view);
		listAdapter = new TwoFieldListAdapter<>(this.getActivity(), 1, 5, this);
		listView.setAdapter(listAdapter);

		creatureVarietyRxHandler.getAll()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Collection<CreatureVariety>>() {
					@Override
					public void onCompleted() {
						if(listAdapter.getCount() > 0) {
							currentInstance = listAdapter.getItem(0);
							isNew = false;
							listView.setSelection(0);
							listView.setItemChecked(0, true);
							listAdapter.notifyDataSetChanged();
							copyItemToViews();
						}
					}
					@Override
					public void onError(Throwable e) {
						Log.e("CreatureVarietiesFrag",
								"Exception caught getting all CreatureVariety instances in initListView", e);
						Toast.makeText(CreatureVarietiesFragment.this.getActivity(),
								getString(R.string.toast_creature_varieties_load_failed),
								Toast.LENGTH_SHORT).show();
					}
					@Override
					public void onNext(Collection<CreatureVariety> creatureVarieties) {
						listAdapter.clear();
						listAdapter.addAll(creatureVarieties);
						listAdapter.notifyDataSetChanged();
						String toastString;
						toastString = String.format(getString(R.string.toast_creature_varieties_loaded), creatureVarieties.size());
						Toast.makeText(CreatureVarietiesFragment.this.getActivity(), toastString, Toast.LENGTH_SHORT).show();
					}
				});

		// Clicking a row in the listView will send the user to the edit world activity
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(copyViewsToItem()) {
					saveItem();
				}
				currentInstance = (CreatureVariety) listView.getItemAtPosition(position);
				isNew = false;
				if (currentInstance == null) {
					currentInstance = new CreatureVariety();
					isNew = true;
				}
				copyItemToViews();
			}
		});
		registerForContextMenu(listView);
	}

	@Override
	public CharSequence getField1Value(CreatureVariety variety) {
		return variety.getName();
	}

	@Override
	public CharSequence getField2Value(CreatureVariety variety) {
		return variety.getDescription();
	}

	@Override
	public void setRacialStatBonus(RacialStatBonus racialStatBonus) {
		boolean changed = false;

		if(currentInstance.getRacialStatBonuses().containsKey(racialStatBonus.getStat())) {
			if(currentInstance.getRacialStatBonuses().get(racialStatBonus.getStat()) != racialStatBonus.getBonus()) {
				currentInstance.getRacialStatBonuses().put(racialStatBonus.getStat(), racialStatBonus.getBonus());
				changed = true;
			}
		}
		else {
			currentInstance.getRacialStatBonuses().put(racialStatBonus.getStat(), racialStatBonus.getBonus());
			changed = true;
		}
		if(changed) {
			saveItem();
		}
	}

	@Override
	public void setTalentTier(TalentTier talentTier) {
		boolean changed = false;

		if(currentInstance.getTalentTiersMap().containsKey(talentTier.getTalent())) {
			if(currentInstance.getTalentTiersMap().get(talentTier.getTalent()) != talentTier.getTier()) {
				currentInstance.getTalentTiersMap().put(talentTier.getTalent(), talentTier.getTier());
				changed = true;
			}
		}
		else {
			currentInstance.getTalentTiersMap().put(talentTier.getTalent(), talentTier.getTier());
			changed = true;
		}
		if(changed) {
			saveItem();
		}
	}

	protected class TalentTierDragListener implements View.OnDragListener {
		private Drawable targetShape = ResourcesCompat.getDrawable(getActivity().getResources(), R.drawable.drag_target_background, null);
		private Drawable hoverShape = ResourcesCompat.getDrawable(getActivity().getResources(), R.drawable.drag_hover_background, null);
		private Drawable normalShape = talentTiersList.getBackground();

		@Override
		public boolean onDrag(View v, DragEvent event) {
			final int action = event.getAction();

			switch(action) {
				case DragEvent.ACTION_DRAG_STARTED:
					if(event.getClipDescription().getLabel().equals(DRAG_ADD_TALENT)) {
						v.setBackground(targetShape);
						v.invalidate();
						break;
					}
					return false;
				case DragEvent.ACTION_DRAG_ENTERED:
					if(event.getClipDescription().getLabel().equals(DRAG_ADD_TALENT)) {
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
					if(event.getClipDescription().getLabel().equals(DRAG_ADD_TALENT)) {
						v.setBackground(targetShape);
						v.invalidate();
					}
					else {
						return false;
					}
					break;
				case DragEvent.ACTION_DROP:
					if(event.getClipDescription().getLabel().equals(DRAG_ADD_TALENT)) {
						boolean changed = false;
						Log.d("RMU", "item count = " + event.getClipData().getItemCount());
						for(int i = 0; i < event.getClipData().getItemCount(); i++) {
							ClipData.Item item = event.getClipData().getItemAt(i);
							// We just send talent ID but since that is the only field used in the Talent.equals method we can create a
							// temporary talent and set its id field then use the new Talent to find the position of the actual Talent
							// instance in the adapter
							int talentId = Integer.valueOf(item.getText().toString());
							Talent newTalent = new Talent();
							newTalent.setId(talentId);
							int position = talentNamesListAdapter.getPosition(newTalent);
							Talent talent = talentNamesListAdapter.getItem(position);
							TalentTier talentTier = new TalentTier(talent, (short) 0);
							if (talentTiersListAdapter.getPosition(talentTier) == -1) {
								talentTiersListAdapter.add(talentTier);
								currentInstance.getTalentTiersMap().put(talent, (short)0);
								changed = true;
							}
						}
						if(changed) {
							saveItem();
							talentTiersListAdapter.notifyDataSetChanged();
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

	protected class TalentNamesDragListener implements View.OnDragListener {
		private Drawable targetShape = ResourcesCompat.getDrawable(getActivity().getResources(), R.drawable.drag_target_background, null);
		private Drawable hoverShape = ResourcesCompat.getDrawable(getActivity().getResources(), R.drawable.drag_hover_background, null);
		private Drawable normalShape = talentNamesList.getBackground();

		@Override
		public boolean onDrag(View v, DragEvent event) {
			final int action = event.getAction();

			switch (action) {
				case DragEvent.ACTION_DRAG_STARTED:
					if(event.getClipDescription().getLabel().equals(DRAG_REMOVE_TALENT)) {
						v.setBackground(targetShape);
						v.invalidate();
					}
					else {
						return false;
					}
					break;
				case DragEvent.ACTION_DRAG_ENTERED:
					if(event.getClipDescription().getLabel().equals(DRAG_REMOVE_TALENT)) {
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
					if(event.getClipDescription().getLabel().equals(DRAG_REMOVE_TALENT)) {
						v.setBackground(targetShape);
						v.invalidate();
					}
					else {
						return false;
					}
					break;
				case DragEvent.ACTION_DROP:
					if(event.getClipDescription().getLabel().equals(DRAG_REMOVE_TALENT)) {
						for (int i = 0; i < event.getClipData().getItemCount(); i++) {
							ClipData.Item item = event.getClipData().getItemAt(i);
							// We just send talent ID but since that is the only field used in the Talent.equals method and talent is the
							// only field used in the TalentTier.equals method we can create a temporary talent and set its id field then
							// create a new TalentTier and set its talent field then use the new TalentTier to find the position of the
							// complete TalentTier instance in the adapter
							int talentId = Integer.valueOf(item.getText().toString());
							Talent newTalent = new Talent();
							newTalent.setId(talentId);
							TalentTier newTalentTier = new TalentTier(newTalent, (short)0);
							int position = talentTiersListAdapter.getPosition(newTalentTier);
							TalentTier talentTier = talentTiersListAdapter.getItem(position);
							Log.d("RMU", "talentTier = " + talentTier);
							currentInstance.getTalentTiersMap().remove(talentTier.getTalent());
							talentTiersListAdapter.remove(talentTier);
						}
						saveItem();
						talentTiersList.clearChoices();
						talentTiersListAdapter.notifyDataSetChanged();
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

	private static class TalentTierDragShadowBuilder extends View.DragShadowBuilder {
		private Drawable[] shadows;
		private View[] views;

		public TalentTierDragShadowBuilder(View[] views) {
			this.views = views;
			shadows = new Drawable[views.length];
			for(int i = 0 ; i < shadows.length; i ++) {
				shadows[i] = new ColorDrawable(Color.LTGRAY);
			}
		}

		@Override
		public void onProvideShadowMetrics (Point size, Point touch) {
			int topLeftX = Integer.MAX_VALUE, topLeftY = Integer.MAX_VALUE;
			int bottomRightX = 0, bottomRightY = 0;
			int width, height;

			for(int i = 0; i < views.length; i++) {
				shadows[i].setBounds(views[i].getLeft(), views[i].getTop(), views[i].getRight(), views[i].getBottom());
				if(views[i].getLeft() < topLeftX) {
					topLeftX = views[i].getLeft();
				}
				if(views[i].getTop() < topLeftY) {
					topLeftY = views[i].getTop();
				}
				if(views[i].getRight() > bottomRightX) {
					bottomRightX = views[i].getRight();
				}
				if(views[i].getBottom() > bottomRightY) {
					bottomRightY = views[i].getBottom();
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
}
