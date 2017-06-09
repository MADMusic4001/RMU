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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.controller.rxhandler.campaign.CampaignRxHandler;
import com.madinnovations.rmu.controller.rxhandler.creature.CreatureRxHandler;
import com.madinnovations.rmu.controller.rxhandler.creature.CreatureVarietyRxHandler;
import com.madinnovations.rmu.data.entities.campaign.Campaign;
import com.madinnovations.rmu.data.entities.creature.Creature;
import com.madinnovations.rmu.data.entities.creature.CreatureVariety;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.adapters.TwoFieldListAdapter;
import com.madinnovations.rmu.view.di.modules.CreatureFragmentModule;
import com.madinnovations.rmu.view.utils.Boast;
import com.madinnovations.rmu.view.utils.EditTextUtils;
import com.madinnovations.rmu.view.utils.SpinnerUtils;
import com.nhaarman.supertooltips.ToolTip;
import com.nhaarman.supertooltips.ToolTipRelativeLayout;
import com.nhaarman.supertooltips.ToolTipView;

import java.util.Collection;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Handles interactions with the UI for creature types.
 */
public class CreaturesFragment extends Fragment implements TwoFieldListAdapter.GetValues<Creature>,
		EditTextUtils.ValuesCallback, SpinnerUtils.ValuesCallback {
	private static final String TAG = "CreaturesFragment";
	@Inject
	protected CampaignRxHandler             campaignRxHandler;
	@Inject
	protected CreatureRxHandler             creatureRxHandler;
	@Inject
	protected CreatureVarietyRxHandler      creatureVarietyRxHandler;
	private   SpinnerUtils<Campaign>        campaignSpinnerUtils;
	private   TwoFieldListAdapter<Creature> creatureListAdapter;
	private   SpinnerUtils<CreatureVariety> creatureVarietySpinnerUtils;
	private   ListView                      creatureListView;
	private   EditText                      levelEdit;
	private   Creature                      currentInstance = new Creature();
	private   boolean                       isNew           = true;
	private   ToolTipView                   toolTipView;
	private   ToolTipRelativeLayout         toolTipFrameLayout;

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
				result = String.valueOf(currentInstance.getLevel());
				break;
		}

		return result;
	}

	@Override
	public void setValueFromEditText(@IdRes int editTextId, String newString) {
		switch (editTextId) {
			case R.id.creature_level_edit:
				currentInstance.setLevel(Short.valueOf(newString));
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
					currentInstance.setCreatureVariety((CreatureVariety)newItem);
					saveItem();
				}
				break;
		}
	}

	@Override
	public void observerCompleted(@IdRes int spinnerId) {}

	private boolean copyViewsToItem() {
		boolean changed = false;
		short newShort;

		View currentFocusView = getActivity().getCurrentFocus();
		if(currentFocusView != null) {
			currentFocusView.clearFocus();
		}

		if(levelEdit.getText() != null && levelEdit.getText().toString().length() > 0) {
			newShort = Short.valueOf(levelEdit.getText().toString());
			if (newShort != currentInstance.getLevel()) {
				currentInstance.setLevel(newShort);
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

		return changed;
	}

	private void copyItemToViews() {
		levelEdit.setText(String.valueOf(currentInstance.getLevel()));
		campaignSpinnerUtils.setSelection(currentInstance.getCampaign());
		creatureVarietySpinnerUtils.setSelection(currentInstance.getCreatureVariety());

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
							else {
								currentInstance = new Creature();
								isNew = true;
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
		toolTipFrameLayout = (ToolTipRelativeLayout) layout.findViewById(R.id.activity_main_tooltipframelayout);

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
}
