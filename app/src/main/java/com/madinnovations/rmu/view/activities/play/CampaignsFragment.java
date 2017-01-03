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
package com.madinnovations.rmu.view.activities.play;

import android.app.Fragment;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ContextMenu;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.controller.rxhandler.campaign.CampaignRxHandler;
import com.madinnovations.rmu.controller.rxhandler.character.CharacterRxHandler;
import com.madinnovations.rmu.controller.rxhandler.common.SpecializationRxHandler;
import com.madinnovations.rmu.data.entities.campaign.Campaign;
import com.madinnovations.rmu.data.entities.character.Character;
import com.madinnovations.rmu.data.entities.common.PowerLevel;
import com.madinnovations.rmu.data.entities.common.Specialization;
import com.madinnovations.rmu.view.RMUDragShadowBuilder;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.adapters.TwoFieldListAdapter;
import com.madinnovations.rmu.view.adapters.character.SelectCharacterAdapter;
import com.madinnovations.rmu.view.di.modules.PlayFragmentModule;
import com.madinnovations.rmu.view.utils.CheckBoxUtils;
import com.madinnovations.rmu.view.utils.EditTextUtils;
import com.madinnovations.rmu.view.utils.SpinnerUtils;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Handles interactions with the UI for campaigns.
 */
public class CampaignsFragment extends Fragment implements TwoFieldListAdapter.GetValues<Campaign>, EditTextUtils.ValuesCallback,
		CheckBoxUtils.ValuesCallback, SpinnerUtils.ValuesCallback, SelectCharacterAdapter.CharacterAdapterCallbacks {
	private static final String TAG = "CampaignsFragment";
	private static final String DRAG_ADD_RESTRICTION = "add-restriction";
	private static final String DRAG_REMOVE_RESTRICTION = "remove-restriction";
	@Inject
	protected CampaignRxHandler            campaignRxHandler;
	@Inject
	protected CharacterRxHandler           characterRxHandler;
	@Inject
	protected SpecializationRxHandler      specializationsRxHandler;
	private   ArrayAdapter<Specialization> specializationAdapter;
	private   ArrayAdapter<Specialization> restrictionsAdapter;
	private   ArrayAdapter<Campaign>       listAdapter;
	private   ListView                     listView;
	private   EditText                     nameEdit;
	private   Spinner                      powerLevelSpinner;
	private   CheckBox                     awardDPCheckBox;
	private   CheckBox                     intenseTrainingCheckBox;
	private   CheckBox                     individualStrideCheckBox;
	private   CheckBox                     noProfessionsCheckBox;
	private   CheckBox                     buyStatsCheckBox;
	private   CheckBox                     allowTalentsBeyondFirstCheckBox;
	private   CheckBox                     openRoundsCheckBox;
	private   CheckBox                     grittyPoisonsCheckBox;
	private   ListView                     specializationsListView;
	private   ListView                     restrictionsListView;
	private   boolean                      isNew = true;
	private   Campaign                     currentInstance = new Campaign();
	private   Map<Character, Boolean>      characterSelectedMap = new HashMap<>();
	private   Toast                        toast = null;

	// <editor-fold desc="method overrides/implementations">
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((CampaignActivity)getActivity()).getActivityComponent().
				newPlayFragmentComponent(new PlayFragmentModule(this)).injectInto(this);

		View layout = inflater.inflate(R.layout.campaigns_fragment, container, false);

		((TextView)layout.findViewById(R.id.header_field1)).setText(getString(R.string.label_campaign_name));
		((TextView)layout.findViewById(R.id.header_field2)).setText(getString(R.string.label_campaign_create_date));

		nameEdit = EditTextUtils.initEdit(layout, getActivity(), this, R.id.name_edit,
				R.string.validation_campaign_name_required);
		powerLevelSpinner = new SpinnerUtils<PowerLevel>().initSpinner(layout, getActivity(),
				Arrays.asList(PowerLevel.values()), this, R.id.power_level_spinner, null);
		initAwardXPButton((ViewGroup)layout);
		awardDPCheckBox = CheckBoxUtils.initCheckBox(layout, this, R.id.award_dp_check_box);
		intenseTrainingCheckBox = CheckBoxUtils.initCheckBox(layout, this, R.id.intense_training_check_box);
		individualStrideCheckBox = CheckBoxUtils.initCheckBox(layout, this, R.id.individual_stride_check_box);
		noProfessionsCheckBox = CheckBoxUtils.initCheckBox(layout, this, R.id.no_professions_check_box);
		buyStatsCheckBox = CheckBoxUtils.initCheckBox(layout, this, R.id.buy_stats_check_box);
		allowTalentsBeyondFirstCheckBox = CheckBoxUtils.initCheckBox(layout, this, R.id.allow_talents_beyond_first_check_box);
		openRoundsCheckBox = CheckBoxUtils.initCheckBox(layout, this, R.id.open_rounds_check_box);
		grittyPoisonsCheckBox = CheckBoxUtils.initCheckBox(layout, this, R.id.gritty_check_box);
		initSpecializationsViews(layout);
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
		inflater.inflate(R.menu.campaigns_action_bar, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if(id == R.id.action_new_campaign) {
			if(copyViewsToItem()) {
				saveItem();
			}
			currentInstance = new Campaign();
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
		getActivity().getMenuInflater().inflate(R.menu.campaign_context_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		final Campaign campaign;

		AdapterView.AdapterContextMenuInfo info =
				(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

		switch (item.getItemId()) {
			case R.id.context_new_campaign:
				if(copyViewsToItem()) {
					saveItem();
				}
				currentInstance = new Campaign();
				isNew = true;
				copyItemToViews();
				listView.clearChoices();
				listAdapter.notifyDataSetChanged();
				return true;
			case R.id.context_delete_campaign:
				campaign = (Campaign)listView.getItemAtPosition(info.position);
				if(campaign != null) {
					deleteItem(campaign);
					return true;
				}
				break;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	public CharSequence getField1Value(Campaign campaign) {
		return campaign.getName();
	}

	@Override
	public CharSequence getField2Value(Campaign campaign) {
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT, Locale.getDefault());
		dateFormat.setCalendar(campaign.getCreateDate());
		return dateFormat.format(campaign.getCreateDate().getTime());
	}

	@Override
	public String getValueForEditText(@IdRes int editTextId) {
		String result = null;

		switch (editTextId) {
			case R.id.name_edit:
				result = currentInstance.getName();
				break;
		}

		return result;
	}

	@Override
	public void setValueFromEditText(@IdRes int editTextId, String newString) {
		switch (editTextId) {
			case R.id.name_edit:
				currentInstance.setName(newString);
				saveItem();
				break;
		}
	}

	@Override
	public boolean performLongClick(@IdRes int editTextId) {
		return false;
	}

	@Override
	public boolean getValueForCheckBox(@IdRes int checkBoxId) {
		boolean result = false;

		switch (checkBoxId) {
			case R.id.award_dp_check_box:
				result = currentInstance.isAwardDP();
				break;
			case R.id.intense_training_check_box:
				result = currentInstance.isIntenseTrainingAllowed();
				break;
			case R.id.individual_stride_check_box:
				result = currentInstance.isIndividualStride();
				break;
			case R.id.no_professions_check_box:
				result = currentInstance.isNoProfessions();
				break;
			case R.id.buy_stats_check_box:
				result = currentInstance.isBuyStats();
				break;
			case R.id.allow_talents_beyond_first_check_box:
				result = currentInstance.isAllowTalentsBeyondFirst();
				break;
			case R.id.open_rounds_check_box:
				result = currentInstance.isOpenRounds();
				break;
			case R.id.gritty_check_box:
				result = currentInstance.isGrittyPoisonAndDisease();
				break;
		}

		return result;
	}

	@Override
	public void setValueFromCheckBox(@IdRes int checkBoxId, boolean newBoolean) {
		switch (checkBoxId) {
			case R.id.award_dp_check_box:
				currentInstance.setAwardDP(newBoolean);
				saveItem();
				break;
			case R.id.intense_training_check_box:
				currentInstance.setIntenseTrainingAllowed(newBoolean);
				saveItem();
				break;
			case R.id.individual_stride_check_box:
				currentInstance.setIndividualStride(newBoolean);
				saveItem();
				break;
			case R.id.no_professions_check_box:
				currentInstance.setNoProfessions(newBoolean);
				saveItem();
				break;
			case R.id.buy_stats_check_box:
				currentInstance.setBuyStats(newBoolean);
				saveItem();
				break;
			case R.id.allow_talents_beyond_first_check_box:
				currentInstance.setAllowTalentsBeyondFirst(newBoolean);
				saveItem();
				break;
			case R.id.open_rounds_check_box:
				currentInstance.setOpenRounds(newBoolean);
				saveItem();
				break;
			case R.id.gritty_check_box:
				currentInstance.setGrittyPoisonAndDisease(newBoolean);
				saveItem();
				break;
		}
	}

	@Override
	public Object getValueForSpinner(@IdRes int spinnerId) {
		PowerLevel result = null;

		switch (spinnerId) {
			case R.id.power_level_spinner:
				result = currentInstance.getPowerLevel();
				break;
		}

		return result;
	}

	@Override
	public void setValueFromSpinner(@IdRes int spinnerId, Object newItem) {
		switch (spinnerId) {
			case R.id.power_level_spinner:
				currentInstance.setPowerLevel((PowerLevel)newItem);
				saveItem();
				break;
		}
	}

	@Override
	public boolean isSelected(Character character) {
		boolean result = false;

		if(characterSelectedMap.get(character) != null) {
			result = characterSelectedMap.get(character);
		}

		return result;
	}

	@Override
	public void setSelected(Character character, boolean selected) {
		characterSelectedMap.put(character, selected);
	}
// </editor-fold>

	// <editor-fold desc="Copy to/from views/entity methods">
	private boolean copyViewsToItem() {
		boolean changed = false;
		String newString;
		PowerLevel newPowerLevel;
		boolean newBoolean;

		View currentFocusView = getActivity().getCurrentFocus();
		if(currentFocusView != null) {
			currentFocusView.clearFocus();
		}

		newString = nameEdit.getText().toString();
		if(newString.isEmpty()) {
			newString = null;
		}
		if((newString == null && currentInstance.getName() != null) ||
				(newString != null && !newString.equals(currentInstance.getName()))) {
			currentInstance.setName(newString);
			changed = true;
		}

		newPowerLevel = (PowerLevel)powerLevelSpinner.getSelectedItem();
		if(newPowerLevel == null && currentInstance.getPowerLevel() != null ||
				(newPowerLevel != null && !newPowerLevel.equals(currentInstance.getPowerLevel()))) {
			currentInstance.setPowerLevel(newPowerLevel);
			changed = true;
		}

		newBoolean = awardDPCheckBox.isChecked();
		if(newBoolean != currentInstance.isAwardDP()) {
			currentInstance.setAwardDP(newBoolean);
			changed = true;
		}

		newBoolean = intenseTrainingCheckBox.isChecked();
		if(newBoolean != currentInstance.isIntenseTrainingAllowed()) {
			currentInstance.setIntenseTrainingAllowed(newBoolean);
			changed = true;
		}

		newBoolean = individualStrideCheckBox.isChecked();
		if(newBoolean != currentInstance.isIndividualStride()) {
			currentInstance.setIndividualStride(newBoolean);
			changed = true;
		}

		newBoolean = noProfessionsCheckBox.isChecked();
		if(newBoolean != currentInstance.isNoProfessions()) {
			currentInstance.setNoProfessions(newBoolean);
			changed = true;
		}

		newBoolean = buyStatsCheckBox.isChecked();
		if(newBoolean != currentInstance.isBuyStats()) {
			currentInstance.setBuyStats(newBoolean);
			changed = true;
		}

		newBoolean = allowTalentsBeyondFirstCheckBox.isChecked();
		if(newBoolean != currentInstance.isAllowTalentsBeyondFirst()) {
			currentInstance.setAllowTalentsBeyondFirst(newBoolean);
			changed = true;
		}

		newBoolean = openRoundsCheckBox.isChecked();
		if(newBoolean != currentInstance.isOpenRounds()) {
			currentInstance.setOpenRounds(newBoolean);
			changed = true;
		}

		newBoolean = grittyPoisonsCheckBox.isChecked();
		if(newBoolean != currentInstance.isGrittyPoisonAndDisease()) {
			currentInstance.setGrittyPoisonAndDisease(newBoolean);
			changed = true;
		}

		List<Specialization> restrictions = new ArrayList<>();
		restrictions.addAll(currentInstance.getRestrictedSpecializations());
		for(int i = 0; i < restrictionsAdapter.getCount(); i++) {
			if(!restrictions.contains(restrictionsAdapter.getItem(i))) {
				restrictions.add(restrictionsAdapter.getItem(i));
				changed = true;
			}
		}
		for(Specialization specialization : currentInstance.getRestrictedSpecializations()) {
			if(restrictionsAdapter.getPosition(specialization) == -1) {
				restrictions.remove(specialization);
				changed = true;
			}
		}
		currentInstance.setRestrictedSpecializations(restrictions);

		return changed;
	}

	private void copyItemToViews() {
		nameEdit.setText(currentInstance.getName());
		if(currentInstance.getName() != null && !currentInstance.getName().isEmpty()) {
			nameEdit.setError(null);
		}

		for(int i = 0; i < powerLevelSpinner.getCount(); i++) {
			if(powerLevelSpinner.getItemAtPosition(i).equals(currentInstance.getPowerLevel())) {
				powerLevelSpinner.setSelection(i);
				break;
			}
		}

		awardDPCheckBox.setChecked(currentInstance.isAwardDP());
		intenseTrainingCheckBox.setChecked(currentInstance.isIntenseTrainingAllowed());
		individualStrideCheckBox.setChecked(currentInstance.isIndividualStride());
		noProfessionsCheckBox.setChecked(currentInstance.isNoProfessions());
		buyStatsCheckBox.setChecked(currentInstance.isBuyStats());
		allowTalentsBeyondFirstCheckBox.setChecked(currentInstance.isAllowTalentsBeyondFirst());
		openRoundsCheckBox.setChecked(currentInstance.isOpenRounds());
		grittyPoisonsCheckBox.setChecked(currentInstance.isGrittyPoisonAndDisease());
		restrictionsAdapter.clear();
		restrictionsAdapter.addAll(currentInstance.getRestrictedSpecializations());
		restrictionsAdapter.notifyDataSetChanged();
	}
	// </editor-fold>

	// <editor-fold desc="Save/delete entity methods">
	private void deleteItem(final Campaign item) {
		campaignRxHandler.deleteById(item.getId())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new Subscriber<Boolean>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e(TAG, "Exception when deleting: " + item, e);
						Toast.makeText(getActivity(), getString(R.string.toast_campaign_delete_failed), Toast.LENGTH_SHORT).show();
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
								currentInstance = new Campaign();
								isNew = true;
							}
							copyItemToViews();
							Toast.makeText(getActivity(), getString(R.string.toast_campaign_deleted), Toast.LENGTH_SHORT).show();
						}
					}
				});
	}

	public void saveItem() {
		if(currentInstance.isValid()) {
			final boolean wasNew = isNew;
			isNew = false;
			campaignRxHandler.save(currentInstance)
					.observeOn(AndroidSchedulers.mainThread())
					.subscribeOn(Schedulers.io())
					.subscribe(new Subscriber<Campaign>() {
						@Override
						public void onCompleted() {}
						@Override
						public void onError(Throwable e) {
							Log.e(TAG, "Exception saving Campaign", e);
							String toastString = getString(R.string.toast_campaign_save_failed);
							Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
						}
						@Override
						public void onNext(Campaign savedCampaign) {
							if (wasNew) {
								listAdapter.add(savedCampaign);
								if(savedCampaign == currentInstance) {
									listView.setSelection(listAdapter.getPosition(savedCampaign));
									listView.setItemChecked(listAdapter.getPosition(savedCampaign), true);
								}
								listAdapter.notifyDataSetChanged();
							}
							if (getActivity() != null) {
								String toastString;
								toastString = getString(R.string.toast_campaign_saved);
								Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();

								int position = listAdapter.getPosition(currentInstance);
								LinearLayout v = (LinearLayout) listView.getChildAt(position - listView.getFirstVisiblePosition());
								if (v != null) {
									TextView textView = (TextView) v.findViewById(R.id.row_field1);
									textView.setText(currentInstance.getName());
									textView = (TextView) v.findViewById(R.id.row_field2);
									DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT,
											Locale.getDefault());
									dateFormat.setCalendar(currentInstance.getCreateDate());
									textView.setText(dateFormat.format(currentInstance.getCreateDate().getTime()));
								}
							}
						}
					});
		}
	}

	/**
	 * Gets the current {@link Campaign} instance
	 *
	 * @return  the current Campaign instance.
	 */
	public Campaign getCurrentInstance() {
		return currentInstance;
	}
	// </editor-fold>

	// <editor-fold desc="Widget initialization methods">
	private void initAwardXPButton(final ViewGroup layout) {
		Button awardXpButton = (Button)layout.findViewById(R.id.award_xp_button);
		awardXpButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LayoutInflater layoutInflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View popupView = layoutInflater.inflate(R.layout.award_xp_popup, null);
				popupView.setBackgroundColor(Color.BLACK);
				final PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT,
																ViewGroup.LayoutParams.WRAP_CONTENT);

				ListView charactersListView = (ListView)popupView.findViewById(R.id.characters_list_view);
				final SelectCharacterAdapter adapter = new SelectCharacterAdapter(getActivity(), CampaignsFragment.this);
				charactersListView.setAdapter(adapter);

				characterRxHandler.getAllForCampaign(currentInstance)
						.observeOn(AndroidSchedulers.mainThread())
						.subscribeOn(Schedulers.io())
						.subscribe(new Subscriber<Collection<Character>>() {
							@Override
							public void onCompleted() {}
							@Override
							public void onError(Throwable e) {
								Log.e(TAG, "Error loading Characters for Campaign", e);
							}
							@Override
							public void onNext(Collection<Character> characters) {
								for(Character character : characters) {
									characterSelectedMap.put(character, false);
								}
								adapter.clear();
								adapter.addAll(characters);
								adapter.notifyDataSetChanged();
							}
						});

				final EditText xpEdit = (EditText)popupView.findViewById(R.id.experience_points_edit);
				Button popupAwardButton = (Button)popupView.findViewById(R.id.award_xp_button);
				popupAwardButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if(xpEdit.getText().length() > 0) {
							int xp = Integer.valueOf(xpEdit.getText().toString());
							for(Map.Entry<Character, Boolean> entry : characterSelectedMap.entrySet()) {
								if(entry.getValue()) {
									entry.getKey().setExperiencePoints(entry.getKey().getExperiencePoints() + xp);
									characterRxHandler.save(entry.getKey())
											.observeOn(AndroidSchedulers.mainThread())
											.subscribeOn(Schedulers.io())
											.subscribe(new Subscriber<Character>() {
												@Override
												public void onCompleted() {}
												@Override
												public void onError(Throwable e) {
													Log.e(TAG, "Exception caught saving character xp", e);
												}
												@Override
												public void onNext(Character character) {
													if(toast != null) {
														toast.cancel();
													}
													toast = Toast.makeText(getActivity(),
																		   getString(R.string.toast_character_saved),
																		   Toast.LENGTH_SHORT);
													toast.show();
												}
											});
								}
							}
						}
						popupWindow.dismiss();
					}
				});

				final CheckBox selectAllCheckBox = (CheckBox)popupView.findViewById(R.id.select_all_check_box);
				selectAllCheckBox.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						for(Character character :  characterSelectedMap.keySet()) {
							characterSelectedMap.put(character, selectAllCheckBox.isChecked());
						}
						adapter.notifyDataSetChanged();
					}
				});

				popupWindow.setFocusable(true);
				popupWindow.update();
				popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
			}
		});
	}

	private void initListView(View layout) {
		listView = (ListView) layout.findViewById(R.id.list_view);
		listAdapter = new TwoFieldListAdapter<>(this.getActivity(), 1, 5, this);
		listView.setAdapter(listAdapter);

		campaignRxHandler.getAll()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new Subscriber<Collection<Campaign>>() {
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
						Log.e(TAG, "Exception caught getting all Campaign instances", e);
						Toast.makeText(CampaignsFragment.this.getActivity(),
								getString(R.string.toast_campaigns_load_failed),
								Toast.LENGTH_SHORT).show();
					}
					@Override
					public void onNext(Collection<Campaign> campaigns) {
						listAdapter.clear();
						listAdapter.addAll(campaigns);
						listAdapter.notifyDataSetChanged();
						if(campaigns.size() > 0) {
							listView.setSelection(0);
							listView.setItemChecked(0, true);
							currentInstance = listAdapter.getItem(0);
							isNew = false;
							copyItemToViews();
						}
						if(isAdded()) {
							Toast.makeText(CampaignsFragment.this.getActivity(),
									String.format(getString(R.string.toast_campaigns_loaded), campaigns.size()),
									Toast.LENGTH_SHORT).show();
						}
					}
				});

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(copyViewsToItem()) {
					saveItem();
				}
				currentInstance = (Campaign) listView.getItemAtPosition(position);
				isNew = false;
				if (currentInstance == null) {
					currentInstance = new Campaign();
					isNew = true;
				}
				copyItemToViews();
			}
		});
		registerForContextMenu(listView);
	}

	private void initSpecializationsViews(View layout) {
		specializationsListView = (ListView)layout.findViewById(R.id.attack_specializations_list);
		specializationAdapter = new ArrayAdapter<>(getActivity(), R.layout.single_field_row);
		specializationsListView.setAdapter(specializationAdapter);

		restrictionsListView = (ListView)layout.findViewById(R.id.restricted_specializations_list);
		restrictionsAdapter = new ArrayAdapter<>(getActivity(), R.layout.single_field_row);
		restrictionsListView.setAdapter(restrictionsAdapter);
		restrictionsAdapter.addAll(currentInstance.getRestrictedSpecializations());
		restrictionsAdapter.notifyDataSetChanged();

		specializationsRxHandler.getAllCharacterAttackSpecializations()
				.subscribe(new Subscriber<Collection<Specialization>>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e(TAG, "Exception caught getting character attack Specializations.", e);
					}
					@Override
					public void onNext(Collection<Specialization> specializations) {
						specializationAdapter.clear();
						specializationAdapter.addAll(specializations);
						specializationAdapter.notifyDataSetChanged();
					}
				});

		specializationsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
				if(!specializationsListView.isItemChecked(position)) {
					specializationsListView.setItemChecked(position, true);
				}
				ClipData dragData = null;

				SparseBooleanArray checkedItems = specializationsListView.getCheckedItemPositions();
				List<View> checkedViews = new ArrayList<>(specializationsListView.getCheckedItemCount());
				for(int i = 0; i < checkedItems.size(); i++) {
					int currentPosition = checkedItems.keyAt(i);
					Specialization specialization = specializationAdapter.getItem(currentPosition);
					if(specialization != null) {
						String specializationIdString = String.valueOf(specialization.getId());
						ClipData.Item clipDataItem = new ClipData.Item(specializationIdString);
						if(dragData == null) {
							dragData = new ClipData(DRAG_ADD_RESTRICTION, new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, clipDataItem);
						}
						else {
							dragData.addItem(clipDataItem);
						}
						checkedViews.add(getViewByPosition(checkedItems.keyAt(i), specializationsListView));
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

		restrictionsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
				if(!restrictionsListView.isItemChecked(position)) {
					restrictionsListView.setItemChecked(position, true);
				}
				ClipData dragData = null;

				SparseBooleanArray checkedItems = restrictionsListView.getCheckedItemPositions();
				List<View> checkedViews = new ArrayList<>(restrictionsListView.getCheckedItemCount());
				for(int i = 0; i < checkedItems.size(); i++) {
					int currentPosition = checkedItems.keyAt(i);
					Specialization specialization = restrictionsAdapter.getItem(currentPosition);
					if(specialization != null) {
						String specializationIdString = String.valueOf(specialization.getId());
						ClipData.Item clipDataItem = new ClipData.Item(specializationIdString);
						if(dragData == null) {
							dragData = new ClipData(DRAG_REMOVE_RESTRICTION, new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN},
									clipDataItem);
						}
						else {
							dragData.addItem(clipDataItem);
						}
						checkedViews.add(getViewByPosition(checkedItems.keyAt(i), restrictionsListView));
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

		specializationsListView.setOnDragListener(new RemoveRestrictionDragListener());
		restrictionsListView.setOnDragListener(new AddRestrictionDragListener());
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
	// </editor-fold>

	protected class AddRestrictionDragListener implements View.OnDragListener {
		private Drawable targetShape = ResourcesCompat.getDrawable(getActivity().getResources(), R.drawable.drag_target_background, null);
		private Drawable hoverShape = ResourcesCompat.getDrawable(getActivity().getResources(), R.drawable.drag_hover_background, null);
		private Drawable normalShape = restrictionsListView.getBackground();

		@Override
		public boolean onDrag(View v, DragEvent event) {
			final int action = event.getAction();

			switch (action) {
				case DragEvent.ACTION_DRAG_STARTED:
					if(event.getClipDescription() != null && DRAG_ADD_RESTRICTION.equals(event.getClipDescription().getLabel())) {
						v.setBackground(targetShape);
						v.invalidate();
					}
					else {
						return false;
					}
					break;
				case DragEvent.ACTION_DRAG_ENTERED:
					if(event.getClipDescription() != null && DRAG_ADD_RESTRICTION.equals(event.getClipDescription().getLabel())) {
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
					if(event.getClipDescription() != null && DRAG_ADD_RESTRICTION.equals(event.getClipDescription().getLabel())) {
						v.setBackground(targetShape);
						v.invalidate();
					}
					else {
						return false;
					}
					break;
				case DragEvent.ACTION_DROP:
					if(event.getClipDescription() != null && DRAG_ADD_RESTRICTION.equals(event.getClipDescription().getLabel())) {
						for (int i = 0; i < event.getClipData().getItemCount(); i++) {
							ClipData.Item item = event.getClipData().getItemAt(i);
							int specializationId = Integer.valueOf(item.getText().toString());
							Specialization newSpecialization = new Specialization();
							newSpecialization.setId(specializationId);
							int position = specializationAdapter.getPosition(newSpecialization);
							if(position != -1) {
								newSpecialization = specializationAdapter.getItem(position);
								CampaignsFragment.this.getCurrentInstance().getRestrictedSpecializations().add(newSpecialization);
								restrictionsAdapter.add(newSpecialization);
							}
						}
						CampaignsFragment.this.saveItem();
						specializationsListView.clearChoices();
						restrictionsAdapter.notifyDataSetChanged();
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

	protected class RemoveRestrictionDragListener implements View.OnDragListener {
		private Drawable targetShape = ResourcesCompat.getDrawable(getActivity().getResources(), R.drawable.drag_target_background, null);
		private Drawable hoverShape = ResourcesCompat.getDrawable(getActivity().getResources(), R.drawable.drag_hover_background, null);
		private Drawable normalShape = restrictionsListView.getBackground();

		@Override
		public boolean onDrag(View v, DragEvent event) {
			final int action = event.getAction();

			switch (action) {
				case DragEvent.ACTION_DRAG_STARTED:
					if(event.getClipDescription() != null && DRAG_REMOVE_RESTRICTION.equals(event.getClipDescription().getLabel())) {
						v.setBackground(targetShape);
						v.invalidate();
					}
					else {
						return false;
					}
					break;
				case DragEvent.ACTION_DRAG_ENTERED:
					if(event.getClipDescription() != null && DRAG_REMOVE_RESTRICTION.equals(event.getClipDescription().getLabel())) {
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
					if(event.getClipDescription() != null && DRAG_REMOVE_RESTRICTION.equals(event.getClipDescription().getLabel())) {
						v.setBackground(targetShape);
						v.invalidate();
					}
					else {
						return false;
					}
					break;
				case DragEvent.ACTION_DROP:
					if(event.getClipDescription() != null && DRAG_REMOVE_RESTRICTION.equals(event.getClipDescription().getLabel())) {
						for (int i = 0; i < event.getClipData().getItemCount(); i++) {
							ClipData.Item item = event.getClipData().getItemAt(i);
							int specializationId = Integer.valueOf(item.getText().toString());
							Specialization newSpecialization = new Specialization();
							newSpecialization.setId(specializationId);
							int position = restrictionsAdapter.getPosition(newSpecialization);
							if(position != -1) {
								CampaignsFragment.this.getCurrentInstance().getRestrictedSpecializations().remove(newSpecialization);
								restrictionsAdapter.remove(newSpecialization);
							}
						}
						CampaignsFragment.this.saveItem();
						restrictionsListView.clearChoices();
						restrictionsAdapter.notifyDataSetChanged();
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
}
