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
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.controller.rxhandler.common.TalentCategoryRxHandler;
import com.madinnovations.rmu.controller.rxhandler.common.TalentRxHandler;
import com.madinnovations.rmu.controller.rxhandler.creature.CreatureCategoryRxHandler;
import com.madinnovations.rmu.data.entities.common.Talent;
import com.madinnovations.rmu.data.entities.common.TalentCategory;
import com.madinnovations.rmu.data.entities.creature.CreatureCategory;
import com.madinnovations.rmu.view.RMUDragShadowBuilder;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.adapters.TwoFieldListAdapter;
import com.madinnovations.rmu.view.adapters.common.ExpandableTalentsListAdapter;
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
 * Handles interactions with the UI for creature categories.
 */
public class CreatureCategoriesFragment extends Fragment implements TwoFieldListAdapter.GetValues<CreatureCategory> {
	private static final String LOG_TAG = "CreatureCategoriesFrag";
	private static final String DRAG_ADD_TALENT = "add-talent";
	private static final String DRAG_REMOVE_TALENT = "remove-talent";
	@Inject
	protected CreatureCategoryRxHandler           creatureCategoryRxHandler;
	@Inject
	protected TalentCategoryRxHandler             talentCategoryRxHandler;
	@Inject
	protected TalentRxHandler                     talentRxHandler;
	private TwoFieldListAdapter<CreatureCategory> listAdapter;
	private ExpandableTalentsListAdapter          expandableTalentsListAdapter;
	private ArrayAdapter<Talent>                  selectedTalentsListAdapter;
	private ListView                              listView;
	private EditText                              nameEdit;
	private EditText                              descriptionEdit;
	private ExpandableListView                    talentsListView;
	private ListView                              selectedTalentsListView;
	private CreatureCategory currentInstance = new CreatureCategory();
	private boolean          isNew            = true;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((CampaignActivity)getActivity()).getActivityComponent().
				newCreatureFragmentComponent(new CreatureFragmentModule(this)).injectInto(this);

		View layout = inflater.inflate(R.layout.creature_categories_fragment, container, false);

		((LinearLayout.LayoutParams)layout.findViewById(R.id.header_field1).getLayoutParams()).weight = 3;
		((TextView)layout.findViewById(R.id.header_field1)).setText(getString(R.string.label_creature_category_name));
		((LinearLayout.LayoutParams)layout.findViewById(R.id.header_field2).getLayoutParams()).weight = 10;
		((TextView)layout.findViewById(R.id.header_field2)).setText(getString(R.string.label_creature_category_description));

		initNameEdit(layout);
		initDescriptionEdit(layout);
		initTalentsList(layout);
		initSelectedTalentsList(layout);
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
		inflater.inflate(R.menu.creature_categories_action_bar, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if(id == R.id.action_new_creature_category) {
			if(copyViewsToItem()) {
				saveItem();
			}
			currentInstance = new CreatureCategory();
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
		getActivity().getMenuInflater().inflate(R.menu.creature_category_context_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		final CreatureCategory creatureCategory;

		AdapterView.AdapterContextMenuInfo info =
				(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

		switch (item.getItemId()) {
			case R.id.context_new_creature_category:
				if(copyViewsToItem()) {
					saveItem();
				}
				currentInstance = new CreatureCategory();
				isNew = true;
				copyItemToViews();
				listView.clearChoices();
				listAdapter.notifyDataSetChanged();
				return true;
			case R.id.context_delete_creature_category:
				creatureCategory = (CreatureCategory)listView.getItemAtPosition(info.position);
				if(creatureCategory != null) {
					deleteItem(creatureCategory);
					return true;
				}
		}
		return super.onContextItemSelected(item);
	}

	@Override
	public CharSequence getField1Value(CreatureCategory creatureCategory) {
		return creatureCategory.getName();
	}

	@Override
	public CharSequence getField2Value(CreatureCategory creatureCategory) {
		return creatureCategory.getDescription();
	}

	private boolean copyViewsToItem() {
		boolean changed = false;

		String newValue = nameEdit.getText().toString();
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

		int size = currentInstance.getTalents().size() > selectedTalentsListAdapter.getCount() ?
				   currentInstance.getTalents().size() :
				   selectedTalentsListAdapter.getCount();
		List<Talent> finalTalents = new ArrayList<>(size);
		for(Talent talent : currentInstance.getTalents()) {
			if(selectedTalentsListAdapter.getPosition(talent) == -1) {
				changed = true;
			}
			else {
				finalTalents.add(talent);
			}
		}
		for(int i = 0; i < selectedTalentsListAdapter.getCount(); i++) {
			Talent talent = selectedTalentsListAdapter.getItem(i);
			if(!currentInstance.getTalents().contains(talent)) {
				finalTalents.add(talent);
				changed = true;
			}
		}
		currentInstance.setTalents(finalTalents);

		return changed;
	}

	private void copyItemToViews() {
		nameEdit.setText(currentInstance.getName());
		descriptionEdit.setText(currentInstance.getDescription());

		if(currentInstance.getName() != null && !currentInstance.getName().isEmpty()) {
			nameEdit.setError(null);
		}
		if(currentInstance.getDescription() != null && !currentInstance.getDescription().isEmpty()) {
			descriptionEdit.setError(null);
		}

		selectedTalentsListAdapter.clear();
		for(Talent talent : currentInstance.getTalents()) {
			selectedTalentsListAdapter.add(talent);
		}
		selectedTalentsListAdapter.notifyDataSetChanged();
	}

	private void saveItem() {
		if(currentInstance.isValid()) {
			final boolean wasNew = isNew;
			isNew = false;
			creatureCategoryRxHandler.save(currentInstance)
					.observeOn(AndroidSchedulers.mainThread())
					.subscribeOn(Schedulers.io())
					.subscribe(new Subscriber<CreatureCategory>() {
						@Override
						public void onCompleted() {}
						@Override
						public void onError(Throwable e) {
							Log.e(LOG_TAG, "Exception saving new CreatureCategory: " + currentInstance, e);
							Toast.makeText(getActivity(), getString(R.string.toast_creature_category_save_failed), Toast.LENGTH_SHORT).show();
						}
						@Override
						public void onNext(CreatureCategory savedItem) {
							if (wasNew) {
								listAdapter.add(savedItem);
								if(savedItem == currentInstance) {
									listView.setSelection(listAdapter.getPosition(savedItem));
									listView.setItemChecked(listAdapter.getPosition(savedItem), true);
								}
								listAdapter.notifyDataSetChanged();
							}
							if(getActivity() != null) {
								Toast.makeText(getActivity(), getString(R.string.toast_creature_category_saved), Toast.LENGTH_SHORT).show();
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

	private void deleteItem(@NonNull final CreatureCategory item) {
		creatureCategoryRxHandler.deleteById(item.getId())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new Subscriber<Boolean>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e(LOG_TAG, "Exception when deleting: " + item, e);
						String toastString = getString(R.string.toast_creature_category_delete_failed);
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
								currentInstance = new CreatureCategory();
								isNew = true;
							}
							copyItemToViews();
							Toast.makeText(getActivity(), getString(R.string.toast_creature_category_deleted), Toast.LENGTH_SHORT).show();
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
					nameEdit.setError(getString(R.string.validation_creature_category_name_required));
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
		descriptionEdit = (EditText)layout.findViewById(R.id.notes_edit);
		descriptionEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length() == 0 && descriptionEdit != null) {
					descriptionEdit.setError(getString(R.string.validation_creature_category_description_required));
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

	private void initTalentsList(View layout) {
		talentsListView = (ExpandableListView)layout.findViewById(R.id.talents_list);
		expandableTalentsListAdapter = new ExpandableTalentsListAdapter(getActivity(), null, null);

		talentCategoryRxHandler.getAll()
				.subscribe(new Subscriber<Collection<TalentCategory>>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e(LOG_TAG, "Exception caught getting all TalentCategory instances.", e);
					}
					@Override
					public void onNext(Collection<TalentCategory> talentCategories) {
						expandableTalentsListAdapter.setGroupData(talentCategories.toArray(
								new TalentCategory[talentCategories.size()]));
						expandableTalentsListAdapter.notifyDataSetChanged();
						talentsListView.setAdapter(expandableTalentsListAdapter);
					}
				});

		talentRxHandler.getAll()
				.subscribe(new Subscriber<Collection<Talent>>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e(LOG_TAG, "Exception caught getting all Talent instances.", e);
					}
					@Override
					public void onNext(Collection<Talent> talents) {
						Map<TalentCategory, List<Talent>> talentMap = new HashMap<>();
						for(Talent talent : talents) {
							List<Talent> talentList = talentMap.get(talent.getCategory());
							if(talentList == null) {
								talentList = new ArrayList<>();
								talentMap.put(talent.getCategory(), talentList);
							}
							talentList.add(talent);
						}
						Talent[][] talentsArray = new Talent[talentMap.size()][];
						int categoryIndex = 0;
						for(Map.Entry<TalentCategory, List<Talent>> entry : talentMap.entrySet()) {
							talentsArray[categoryIndex++] = entry.getValue().toArray(new Talent[entry.getValue().size()]);
						}
						expandableTalentsListAdapter.setChildData(talentsArray);
						expandableTalentsListAdapter.notifyDataSetChanged();
						talentsListView.setAdapter(expandableTalentsListAdapter);
					}
				});

		talentsListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
				long packedPosition = ExpandableListView.getPackedPositionForChild(groupPosition, childPosition);
				int flatPosition = talentsListView.getFlatListPosition(packedPosition);
				talentsListView.setItemChecked(flatPosition, !talentsListView.isItemChecked(flatPosition));
				return true;
			}
		});

		talentsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
				if(!talentsListView.isItemChecked(position) && ExpandableListView.getPackedPositionChild(id) != -1) {
					talentsListView.setItemChecked(position, true);
				}
				ClipData dragData = null;

				SparseBooleanArray checkedItems = talentsListView.getCheckedItemPositions();
				List<View> checkedViews = new ArrayList<>(talentsListView.getCheckedItemCount());
				for(int i = 0; i < checkedItems.size(); i++) {
					int currentFlatPosition = checkedItems.keyAt(i);
					long packedPosition = talentsListView.getExpandableListPosition(currentFlatPosition);
					int groupId = ExpandableListView.getPackedPositionGroup(packedPosition);
					int childId = ExpandableListView.getPackedPositionChild(packedPosition);
					Talent talent = (Talent)expandableTalentsListAdapter.getChild(groupId, childId);
					if(talent != null) {
						String talentIdString = String.valueOf(talent.getId());
						ClipData.Item clipDataItem = new ClipData.Item(talentIdString);
						if(dragData == null) {
							dragData = new ClipData(DRAG_ADD_TALENT, new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN},
													clipDataItem);
						}
						else {
							dragData.addItem(clipDataItem);
						}
						checkedViews.add(getViewByPosition(currentFlatPosition, talentsListView));
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
		talentsListView.setOnDragListener(new RemoveTalentDragListener());
	}

	private void initSelectedTalentsList(View layout) {
		selectedTalentsListView = (ListView)layout.findViewById(R.id.selected_talents_list);
		selectedTalentsListAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_row);
		selectedTalentsListView.setAdapter(selectedTalentsListAdapter);

		selectedTalentsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				if(!selectedTalentsListView.isItemChecked(position)) {
					selectedTalentsListView.setItemChecked(position, true);
				}
				ClipData dragData = null;

				SparseBooleanArray checkedItems = selectedTalentsListView.getCheckedItemPositions();
				List<View> checkedViews = new ArrayList<>(selectedTalentsListView.getCheckedItemCount());
				for(int i = 0; i < checkedItems.size(); i++) {
					int currentPosition = checkedItems.keyAt(i);
					Talent talent = selectedTalentsListAdapter.getItem(currentPosition);
					if(talent != null) {
						String talentIdString = String.valueOf(talent.getId());
						ClipData.Item clipDataItem = new ClipData.Item(talentIdString);
						if(dragData == null) {
							dragData = new ClipData(DRAG_REMOVE_TALENT, new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN},
													clipDataItem);
						}
						else {
							dragData.addItem(clipDataItem);
						}
						checkedViews.add(getViewByPosition(currentPosition, selectedTalentsListView));
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
		selectedTalentsListView.setOnDragListener(new AddTalentDragListener());
	}

	private void initListView(View layout) {
		listView = (ListView) layout.findViewById(R.id.list_view);
		listAdapter = new TwoFieldListAdapter<>(this.getActivity(), 3, 10, this);
		listView.setAdapter(listAdapter);

		creatureCategoryRxHandler.getAll()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Collection<CreatureCategory>>() {
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
						Log.e(LOG_TAG, "Exception caught getting all CreatureCategory instances in onCreateView", e);
						Toast.makeText(CreatureCategoriesFragment.this.getActivity(),
								getString(R.string.toast_creature_categories_load_failed),
								Toast.LENGTH_SHORT).show();
					}
					@Override
					public void onNext(Collection<CreatureCategory> creatureCategories) {
						listAdapter.clear();
						listAdapter.addAll(creatureCategories);
						listAdapter.notifyDataSetChanged();
						String toastString;
						toastString = String.format(getString(R.string.toast_creature_categories_loaded), creatureCategories.size());
						Toast.makeText(CreatureCategoriesFragment.this.getActivity(), toastString, Toast.LENGTH_SHORT).show();
					}
				});

		// Clicking a row in the listView will send the user to the edit world activity
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(copyViewsToItem()) {
					saveItem();
				}
				currentInstance = (CreatureCategory) listView.getItemAtPosition(position);
				isNew = false;
				if (currentInstance == null) {
					currentInstance = new CreatureCategory();
					isNew = true;
				}
				copyItemToViews();
			}
		});
		registerForContextMenu(listView);
	}

	protected class AddTalentDragListener implements View.OnDragListener {
		private Drawable targetShape = ResourcesCompat.getDrawable(getActivity().getResources(), R.drawable.drag_target_background, null);
		private Drawable hoverShape  = ResourcesCompat.getDrawable(getActivity().getResources(), R.drawable.drag_hover_background, null);
		private Drawable normalShape = selectedTalentsListView.getBackground();

		@Override
		public boolean onDrag(View v, DragEvent event) {
			final int action = event.getAction();

			switch(action) {
				case DragEvent.ACTION_DRAG_STARTED:
					if(event.getClipDescription() != null && DRAG_ADD_TALENT.equals(event.getClipDescription().getLabel())) {
						v.setBackground(targetShape);
						v.invalidate();
						break;
					}
					return false;
				case DragEvent.ACTION_DRAG_ENTERED:
					if(event.getClipDescription() != null && DRAG_ADD_TALENT.equals(event.getClipDescription().getLabel())) {
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
					if(event.getClipDescription() != null && DRAG_ADD_TALENT.equals(event.getClipDescription().getLabel())) {
						v.setBackground(targetShape);
						v.invalidate();
					}
					else {
						return false;
					}
					break;
				case DragEvent.ACTION_DROP:
					if(event.getClipDescription() != null && DRAG_ADD_TALENT.equals(event.getClipDescription().getLabel())) {
						boolean changed = false;
						for(int i = 0; i < event.getClipData().getItemCount(); i++) {
							ClipData.Item item = event.getClipData().getItemAt(i);
							// We just send attack ID but since that is the only field used in the Attack.equals method we can create a
							// temporary attack and set its id field then use the new Attack to find the position of the actual Attack
							// instance in the adapter
							int talentId = Integer.valueOf(item.getText().toString());
							Talent newTalent = new Talent();
							newTalent.setId(talentId);
							long position = expandableTalentsListAdapter.getPosition(newTalent);
							if(position != -1) {
								Talent talent = expandableTalentsListAdapter.getItem(position);
								if (selectedTalentsListAdapter.getPosition(talent) == -1) {
									selectedTalentsListAdapter.add(talent);
									currentInstance.getTalents().add(talent);
									changed = true;
								}
							}
						}
						if(changed) {
							saveItem();
							selectedTalentsListAdapter.notifyDataSetChanged();
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

	protected class RemoveTalentDragListener implements View.OnDragListener {
		private Drawable targetShape = ResourcesCompat.getDrawable(getActivity().getResources(), R.drawable.drag_target_background, null);
		private Drawable hoverShape  = ResourcesCompat.getDrawable(getActivity().getResources(), R.drawable.drag_hover_background, null);
		private Drawable normalShape = talentsListView.getBackground();

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
							int attackId = Integer.valueOf(item.getText().toString());
							Talent newTalent = new Talent();
							newTalent.setId(attackId);
							int position = selectedTalentsListAdapter.getPosition(newTalent);
							if(position != -1) {
								Talent talent = selectedTalentsListAdapter.getItem(position);
								currentInstance.getTalents().remove(talent);
								selectedTalentsListAdapter.remove(talent);
							}
						}
						saveItem();
						selectedTalentsListView.clearChoices();
						selectedTalentsListAdapter.notifyDataSetChanged();
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
}
