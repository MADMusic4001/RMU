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
package com.madinnovations.rmu.view.activities.spell;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.controller.rxhandler.spell.SpellListRxHandler;
import com.madinnovations.rmu.controller.rxhandler.spell.SpellRxHandler;
import com.madinnovations.rmu.controller.rxhandler.spell.SpellSubTypeRxHandler;
import com.madinnovations.rmu.controller.rxhandler.spell.SpellTypeRxHandler;
import com.madinnovations.rmu.data.entities.spells.AreaOfEffect;
import com.madinnovations.rmu.data.entities.spells.Duration;
import com.madinnovations.rmu.data.entities.spells.Range;
import com.madinnovations.rmu.data.entities.spells.Spell;
import com.madinnovations.rmu.data.entities.spells.SpellList;
import com.madinnovations.rmu.data.entities.spells.SpellSubType;
import com.madinnovations.rmu.data.entities.spells.SpellType;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.adapters.TwoFieldListAdapter;
import com.madinnovations.rmu.view.di.modules.SpellFragmentModule;

import java.util.Collection;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Handles interactions with the UI for spells.
 */
public class SpellsFragment extends Fragment implements TwoFieldListAdapter.GetValues<Spell> {
	private static final String TAG = "SpellsFragment";
	@Inject
	protected SpellRxHandler             spellRxHandler;
	@Inject
	protected SpellListRxHandler         spellListRxHandler;
	@Inject
	protected SpellTypeRxHandler         spellTypeRxHandler;
	@Inject
	protected SpellSubTypeRxHandler      spellSubTypeRxHandler;
	private   ArrayAdapter<SpellList>    spellListFilterSpinnerAdapter;
	private   ArrayAdapter<SpellList>    spellListSpinnerAdapter;
	private   ArrayAdapter<AreaOfEffect> areaOfEffectSpinnerAdapter;
	private   ArrayAdapter<Duration>     durationSpinnerAdapter;
	private   ArrayAdapter<Range>        rangeSpinnerAdapter;
	private   ArrayAdapter<SpellType>    spellTypeSpinnerAdapter;
	private   ArrayAdapter<SpellSubType> spellSubTypeSpinnerAdapter;
	private   TwoFieldListAdapter<Spell> listAdapter;
	private   Spinner                    spellListFilterSpinner;
	private   ListView                   listView;
	private   EditText                   nameEdit;
	private   EditText                   descriptionEdit;
	private   Spinner                    spellListSpinner;
	private   Spinner                    spellTypeSpinner;
	private   Spinner                    spellSubTypeSpinner;
	private   Spinner                    areaOfEffectSpinner;
	private   EditText                   aoeParam1Edit;
	private   EditText                   aoeParam2Edit;
	private   EditText                   aoeParam3Edit;
	private   EditText                   aoeParam4Edit;
	private   Spinner                    durationSpinner;
	private   EditText                   durationParam1Edit;
	private   EditText                   durationParam2Edit;
	private   Spinner                    rangeSpinner;
	private   EditText                   rangeParamEdit;
	private Spell currentInstance = new Spell();
	private boolean isNew = true;
	private SpellSubType noSpellSubType = new SpellSubType();

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((CampaignActivity)getActivity()).getActivityComponent().
				newSpellFragmentComponent(new SpellFragmentModule(this)).injectInto(this);

		noSpellSubType.setName(getString(R.string.label_no_sub_type));

		View layout = inflater.inflate(R.layout.spells_fragment, container, false);

		((TextView)layout.findViewById(R.id.header_field1)).setText(getString(R.string.label_spell_name));
		((TextView)layout.findViewById(R.id.header_field2)).setText(getString(R.string.label_spell_description));

		initSpellListFilterSpinner(layout);
		initNameEdit(layout);
		initDescriptionEdit(layout);
		initSpellListSpinner(layout);
		initSpellTypeSpinner(layout);
		initSpellSubTypeSpinner(layout);
		initAreaOfEffectSpinner(layout);
		aoeParam1Edit = initAoeParamEdit(layout, R.id.aoe_param1_edit, R.string.validation_spell_aoe_param1_required, 0);
		aoeParam2Edit = initAoeParamEdit(layout, R.id.aoe_param2_edit, R.string.validation_spell_aoe_param2_required, 1);
		aoeParam3Edit = initAoeParamEdit(layout, R.id.aoe_param3_edit, R.string.validation_spell_aoe_param3_required, 2);
		aoeParam4Edit = initAoeParamEdit(layout, R.id.aoe_param4_edit, R.string.validation_spell_aoe_param4_required, 3);
		initDurationSpinner(layout);
		durationParam1Edit = initDurationParamEdit(layout, R.id.duration_param1_edit,
							  R.string.validation_spell_duration_param1_required, 0);
		durationParam2Edit = initDurationParamEdit(layout, R.id.duration_param2_edit,
							  R.string.validation_spell_duration_param2_required, 1);
		initRangeSpinner(layout);
		initRangeEdit(layout);
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
		inflater.inflate(R.menu.spells_action_bar, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if(id == R.id.action_new_spell) {
			if(copyViewsToItem()) {
				saveItem();
			}
			currentInstance = new Spell();
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
		getActivity().getMenuInflater().inflate(R.menu.spell_context_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		final Spell spell;

		AdapterView.AdapterContextMenuInfo info =
				(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

		switch (item.getItemId()) {
			case R.id.context_new_spell:
				if(copyViewsToItem()) {
					saveItem();
				}
				currentInstance = new Spell();
				isNew = true;
				copyItemToViews();
				listView.clearChoices();
				listAdapter.notifyDataSetChanged();
				return true;
			case R.id.context_delete_spell:
				spell = (Spell)listView.getItemAtPosition(info.position);
				if(spell != null) {
					deleteItem(spell);
					return true;
				}
				break;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	public CharSequence getField1Value(Spell spell) {
		return spell.getName();
	}

	@Override
	public CharSequence getField2Value(Spell spell) {
		return spell.getDescription();
	}

	private boolean copyViewsToItem() {
		boolean changed = false;
		int position;
		SpellList spellList;
		SpellType spellType;
		SpellSubType spellSubType;
		AreaOfEffect areaOfEffect;
		Duration duration;
		Range range;
		int[] params;
		Integer param;

		View currentFocusView = getActivity().getCurrentFocus();
		if(currentFocusView != null) {
			currentFocusView.clearFocus();
		}

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

		position = spellListSpinner.getSelectedItemPosition();
		if(position != -1) {
			spellList = spellListSpinnerAdapter.getItem(position);
			if(spellList != null && !spellList.equals(currentInstance.getSpellList())) {
				currentInstance.setSpellList(spellList);
				changed = true;
			}
		}

		position = spellTypeSpinner.getSelectedItemPosition();
		if(position != -1) {
			spellType = spellTypeSpinnerAdapter.getItem(position);
			if(spellType != null && !spellType.equals(currentInstance.getSpellType())) {
				currentInstance.setSpellType(spellType);
				changed = true;
			}
		}

		position = spellSubTypeSpinner.getSelectedItemPosition();
		if(position != -1) {
			spellSubType = spellSubTypeSpinnerAdapter.getItem(position);
			if(spellSubType != null && !spellSubType.equals(currentInstance.getSpellSubType())) {
				currentInstance.setSpellSubType(spellSubType);
				changed = true;
			}
		}

		position = areaOfEffectSpinner.getSelectedItemPosition();
		if(position != -1) {
			areaOfEffect = areaOfEffectSpinnerAdapter.getItem(position);
			if(areaOfEffect != null) {
				if (!areaOfEffect.equals(currentInstance.getAreaOfEffect())) {
					currentInstance.setAreaOfEffect(areaOfEffect);
					changed = true;
				}
				params = new int[areaOfEffect.getParameterCount()];
				changed |= copyParamToItem(params, currentInstance.getAreaOfEffectParams(),
						aoeParam1Edit.getText().toString(), 0);
				changed |= copyParamToItem(params, currentInstance.getAreaOfEffectParams(),
						aoeParam2Edit.getText().toString(), 1);
				changed |= copyParamToItem(params, currentInstance.getAreaOfEffectParams(),
						aoeParam3Edit.getText().toString(), 2);
				changed |= copyParamToItem(params, currentInstance.getAreaOfEffectParams(),
						aoeParam4Edit.getText().toString(), 3);
				currentInstance.setAreaOfEffectParams(params);
			}
		}

		position = durationSpinner.getSelectedItemPosition();
		if(position != -1) {
			duration = durationSpinnerAdapter.getItem(position);
			if(duration != null) {
				if (!duration.equals(currentInstance.getDuration())) {
					currentInstance.setDuration(duration);
					changed = true;
				}
				if (duration.getParameterCount() >= 0) {
					params = new int[duration.getParameterCount()];
					changed |= copyParamToItem(params, currentInstance.getDurationParams(),
							durationParam1Edit.getText().toString(), 0);
					changed |= copyParamToItem(params, currentInstance.getDurationParams(),
							durationParam2Edit.getText().toString(), 1);
					currentInstance.setDurationParams(params);
				} else {
					if (currentInstance.getDurationParams() != null) {
						currentInstance.setDurationParams(null);
						changed = true;
					}
				}
			}
		}

		position = rangeSpinner.getSelectedItemPosition();
		if(position != -1) {
			range = rangeSpinnerAdapter.getItem(position);
			if(range != null) {
				if (!range.equals(currentInstance.getRange())) {
					currentInstance.setRange(range);
					changed = true;
				}
				if (range.getParameterCount() >= 0) {
					if (rangeParamEdit.getText().length() > 0) {
						param = Integer.valueOf(rangeParamEdit.getText().toString());
					} else {
						param = 1;
					}
					if (currentInstance.getRangeParam() == null || !param.equals(currentInstance.getRangeParam())) {
						currentInstance.setRangeParam(param);
						changed = true;
					}
				} else {
					if (currentInstance.getRangeParam() != null) {
						currentInstance.setRangeParam(null);
						changed = true;
					}
				}
			}
		}

		return changed;
	}

	private boolean copyParamToItem(int[] newParams, int[] oldParams, String editText, int index) {
		boolean changed = false;

		if(newParams.length <= index) {
			return newParams.length != oldParams.length;
		}
		if(editText.length() > 0) {
			newParams[index] = Integer.valueOf(editText);
		}
		else {
			newParams[index] = 1;
		}
		if(oldParams == null || oldParams.length <= index ||
				oldParams[index] != newParams[index]) {
			changed = true;
		}

		return changed;
	}

	private void copyItemToViews() {
		nameEdit.setText(currentInstance.getName());
		descriptionEdit.setText(currentInstance.getDescription());
		if(currentInstance.getSpellList() == null) {
			if(spellListSpinner.getSelectedItemPosition() != -1) {
				currentInstance.setSpellList(spellListSpinnerAdapter.getItem(spellListSpinner.getSelectedItemPosition()));
			}
		}
		else {
			spellListSpinner.setSelection(spellListSpinnerAdapter.getPosition(currentInstance.getSpellList()));
		}

		spellTypeSpinner.setSelection(spellTypeSpinnerAdapter.getPosition(currentInstance.getSpellType()));
		spellSubTypeSpinner.setSelection(spellSubTypeSpinnerAdapter.getPosition(currentInstance.getSpellSubType()));
		areaOfEffectSpinner.setSelection(areaOfEffectSpinnerAdapter.getPosition(currentInstance.getAreaOfEffect()));
		if(currentInstance.getAreaOfEffect() != null) {
			aoeParam1Edit.setEnabled(currentInstance.getAreaOfEffect().getParameterCount() >= 1);
			aoeParam2Edit.setEnabled(currentInstance.getAreaOfEffect().getParameterCount() >= 2);
			aoeParam3Edit.setEnabled(currentInstance.getAreaOfEffect().getParameterCount() >= 3);
			aoeParam4Edit.setEnabled(currentInstance.getAreaOfEffect().getParameterCount() >= 4);
			if(currentInstance.getAreaOfEffectParams() != null) {
				if(currentInstance.getAreaOfEffectParams().length >= 1) {
					aoeParam1Edit.setText(String.valueOf(currentInstance.getAreaOfEffectParams()[0]));
				}
				else {
					aoeParam1Edit.setError(null);
				}
				if(currentInstance.getAreaOfEffectParams().length >= 2) {
					aoeParam2Edit.setText(String.valueOf(currentInstance.getAreaOfEffectParams()[1]));
				}
				else {
					aoeParam2Edit.setError(null);
				}
				if(currentInstance.getAreaOfEffectParams().length >= 3) {
					aoeParam3Edit.setText(String.valueOf(currentInstance.getAreaOfEffectParams()[2]));
				}
				else {
					aoeParam3Edit.setError(null);
				}
				if(currentInstance.getAreaOfEffectParams().length >= 4) {
					aoeParam4Edit.setText(String.valueOf(currentInstance.getAreaOfEffectParams()[3]));
				}
				else {
					aoeParam4Edit.setError(null);
				}
			}
		}
		else {
			aoeParam1Edit.setEnabled(false);
			aoeParam2Edit.setEnabled(false);
			aoeParam3Edit.setEnabled(false);
			aoeParam4Edit.setEnabled(false);
		}
		durationSpinner.setSelection(durationSpinnerAdapter.getPosition(currentInstance.getDuration()));
		if(currentInstance.getDuration() != null) {
			durationParam1Edit.setEnabled(currentInstance.getDuration().getParameterCount() >= 1);
			durationParam2Edit.setEnabled(currentInstance.getDuration().getParameterCount() >= 2);
			if(currentInstance.getDurationParams() != null) {
				if(currentInstance.getDurationParams().length >= 1) {
					durationParam1Edit.setText(String.valueOf(currentInstance.getDurationParams()[0]));
				}
				else {
					durationParam1Edit.setError(null);
				}
				if(currentInstance.getDurationParams().length >= 2) {
					durationParam2Edit.setText(String.valueOf(currentInstance.getDurationParams()[1]));
				}
				else {
					durationParam2Edit.setError(null);
				}
			}
		}
		else {
			durationParam1Edit.setEnabled(false);
			durationParam2Edit.setEnabled(false);
		}
		rangeSpinner.setSelection(rangeSpinnerAdapter.getPosition(currentInstance.getRange()));
		if(currentInstance.getRange() != null) {
			rangeParamEdit.setEnabled(currentInstance.getRange().getParameterCount() >= 1);
			if(currentInstance.getRangeParam() != null) {
				rangeParamEdit.setText(String.valueOf(currentInstance.getRangeParam()));
			}
			else {
				rangeParamEdit.setError(null);
			}
		}
		else {
			rangeParamEdit.setEnabled(false);
		}

		if(currentInstance.getName() != null && !currentInstance.getName().isEmpty()) {
			nameEdit.setError(null);
		}
		if(currentInstance.getDescription() != null && !currentInstance.getDescription().isEmpty()) {
			descriptionEdit.setError(null);
		}
	}

	private void deleteItem(final Spell item) {
		spellRxHandler.deleteById(item.getId())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new Subscriber<Boolean>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e(TAG, "Exception when deleting: " + item, e);
						Toast.makeText(getActivity(), getString(R.string.toast_spell_delete_failed), Toast.LENGTH_SHORT).show();
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
								currentInstance = new Spell();
								isNew = true;
							}
							copyItemToViews();
							Toast.makeText(getActivity(), getString(R.string.toast_spell_deleted), Toast.LENGTH_SHORT).show();
						}
					}
				});
	}

	private void saveItem() {
		if(currentInstance.isValid()) {
			final boolean wasNew = isNew;
			isNew = false;
			spellRxHandler.save(currentInstance)
					.observeOn(AndroidSchedulers.mainThread())
					.subscribeOn(Schedulers.io())
					.subscribe(new Subscriber<Spell>() {
						@Override
						public void onCompleted() {}
						@Override
						public void onError(Throwable e) {
							Log.e(TAG, "Exception saving Skill", e);
							String toastString = getString(R.string.toast_spell_save_failed);
							Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
						}
						@Override
						public void onNext(Spell savedSpell) {
							if (wasNew) {
								listAdapter.add(savedSpell);
								if(savedSpell == currentInstance) {
									listView.setSelection(listAdapter.getPosition(savedSpell));
									listView.setItemChecked(listAdapter.getPosition(savedSpell), true);
								}
								listAdapter.notifyDataSetChanged();
							}
							if (getActivity() != null) {
								String toastString;
								toastString = getString(R.string.toast_spell_saved);
								Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();

								int position = listAdapter.getPosition(currentInstance);
								SpellList spellList = spellListFilterSpinnerAdapter.getItem
										(spellListFilterSpinner.getSelectedItemPosition());
								if(spellList != null) {
									if (position >= 0) {
										if (!spellList.getName().equals(savedSpell.getSpellList().getName())
												&& spellList.getId() != -1) {
											listAdapter.remove(savedSpell);
											listAdapter.notifyDataSetChanged();
										} else {
											LinearLayout v = (LinearLayout) listView.getChildAt(
													position - listView.getFirstVisiblePosition());
											if (v != null) {
												TextView textView = (TextView) v.findViewById(R.id.row_field1);
												textView.setText(currentInstance.getName());
												textView = (TextView) v.findViewById(R.id.row_field2);
												textView.setText(currentInstance.getDescription());
											}
										}
									} else if (spellList.equals(savedSpell.getSpellList()) && spellList.getId() != -1) {
										listAdapter.add(savedSpell);
										listAdapter.notifyDataSetChanged();
									}
								}
							}
						}
					});
		}
	}

	private void initSpellListFilterSpinner(View layout) {
		spellListFilterSpinner = (Spinner)layout.findViewById(R.id.spell_list_filter_spinner);
		spellListFilterSpinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.single_field_row);
		spellListFilterSpinner.setAdapter(spellListFilterSpinnerAdapter);

		final SpellList allSpellLists = new SpellList();
		allSpellLists.setName(getString(R.string.label_all_spell_lists));
		spellListFilterSpinnerAdapter.add(allSpellLists);
		spellListRxHandler.getAll()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new Subscriber<Collection<SpellList>>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e(TAG, "Exception caught getting all SpellList instances", e);
					}
					@Override
					public void onNext(Collection<SpellList> spellCategories) {
						spellListFilterSpinnerAdapter.addAll(spellCategories);
						spellListFilterSpinnerAdapter.notifyDataSetChanged();
						spellListFilterSpinner.setSelection(spellListFilterSpinnerAdapter.getPosition(allSpellLists));
					}
				});

		spellListFilterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
				loadFilteredSpells(spellListFilterSpinnerAdapter.getItem(position));
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				loadFilteredSpells(null);
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
					nameEdit.setError(getString(R.string.validation_spell_name_required));
				}
			}
		});
		nameEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					final String newName = nameEdit.getText().toString();
					if (currentInstance != null && !newName.equals(currentInstance.getName())) {
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
					descriptionEdit.setError(getString(R.string.validation_spell_description_required));
				}
			}
		});
		descriptionEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					final String newDescription = descriptionEdit.getText().toString();
					if (currentInstance != null && !newDescription.equals(currentInstance.getDescription())) {
						currentInstance.setDescription(newDescription);
						saveItem();
					}
				}
			}
		});
	}

	private void initSpellListSpinner(View layout) {
		spellListSpinner = (Spinner)layout.findViewById(R.id.spell_list_spinner);
		spellListSpinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.single_field_row);
		spellListSpinner.setAdapter(spellListSpinnerAdapter);

		spellListRxHandler.getAll()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new Subscriber<Collection<SpellList>>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e(TAG, "Exception caught getting all SpellList instances", e);
					}
					@Override
					public void onNext(Collection<SpellList> spellCategories) {
						spellListSpinnerAdapter.addAll(spellCategories);
						spellListSpinnerAdapter.notifyDataSetChanged();
						if(currentInstance.getSpellList() != null) {
							spellListSpinner.setSelection(spellListSpinnerAdapter.getPosition(currentInstance.getSpellList()));
						}
					}
				});

		spellListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
				if(currentInstance.getSpellList() == null ||
						spellListSpinnerAdapter.getPosition(currentInstance.getSpellList()) != position) {
					currentInstance.setSpellList(spellListSpinnerAdapter.getItem(position));
					saveItem();
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				if(currentInstance.getSpellList() != null) {
					currentInstance.setSpellList(null);
				}
			}
		});
	}

	private void initSpellTypeSpinner(View layout) {
		spellTypeSpinner = (Spinner)layout.findViewById(R.id.spell_type_spinner);
		spellTypeSpinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.single_field_row);
		spellTypeSpinner.setAdapter(spellTypeSpinnerAdapter);

		spellTypeRxHandler.getAll()
				.subscribe(new Subscriber<Collection<SpellType>>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e(TAG, "Exception caught getting all SpellType instances.", e);
					}
					@Override
					public void onNext(Collection<SpellType> spellTypes) {
						spellTypeSpinnerAdapter.clear();
						spellTypeSpinnerAdapter.addAll(spellTypes);
						spellTypeSpinnerAdapter.notifyDataSetChanged();
					}
				});
		spellTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				SpellType spellType = spellTypeSpinnerAdapter.getItem(position);
				if(spellType != null && !spellType.equals(currentInstance.getSpellType())) {
					currentInstance.setSpellType(spellType);
					saveItem();
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				if(currentInstance.getSpellType() != null) {
					currentInstance.setSpellType(null);
					saveItem();
				}
			}
		});
	}

	private void initSpellSubTypeSpinner(View layout) {
		spellSubTypeSpinner = (Spinner)layout.findViewById(R.id.spell_sub_type_spinner);
		spellSubTypeSpinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.single_field_row);
		spellSubTypeSpinner.setAdapter(spellSubTypeSpinnerAdapter);

		spellSubTypeRxHandler.getAll()
				.subscribe(new Subscriber<Collection<SpellSubType>>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e(TAG, "Exception caught getting all SpellSubType instances", e);
					}
					@Override
					public void onNext(Collection<SpellSubType> spellSubTypes) {
						spellSubTypeSpinnerAdapter.clear();
						spellSubTypeSpinnerAdapter.add(noSpellSubType);
						spellSubTypeSpinnerAdapter.addAll(spellSubTypes);
						spellSubTypeSpinnerAdapter.notifyDataSetChanged();
					}
				});

		spellSubTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				SpellSubType spellSubType = spellSubTypeSpinnerAdapter.getItem(position);
				if(spellSubType != null && spellSubType.getId() == -1 && currentInstance.getSpellSubType() != null) {
					currentInstance.setSpellSubType(null);
					saveItem();
				}
				else if(spellSubType != null && spellSubType.getId() != -1 && !spellSubType.equals(currentInstance.getSpellSubType())) {
					currentInstance.setSpellSubType(spellSubType);
					saveItem();
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				if(currentInstance.getSpellSubType() != null) {
					currentInstance.setSpellSubType(null);
					saveItem();
				}
			}
		});
	}

	private void initAreaOfEffectSpinner(View layout) {
		areaOfEffectSpinner = (Spinner)layout.findViewById(R.id.area_of_effect_spinner);
		areaOfEffectSpinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.single_field_row);
		areaOfEffectSpinner.setAdapter(areaOfEffectSpinnerAdapter);

		areaOfEffectSpinnerAdapter.clear();
		areaOfEffectSpinnerAdapter.addAll(AreaOfEffect.values());
		areaOfEffectSpinnerAdapter.notifyDataSetChanged();

		areaOfEffectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				AreaOfEffect areaOfEffect = areaOfEffectSpinnerAdapter.getItem(position);
				if(areaOfEffect != null && !areaOfEffect.equals(currentInstance.getAreaOfEffect())) {
					currentInstance.setAreaOfEffect(areaOfEffect);
					updateAreaOfEffectParams();
					saveItem();
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				if(currentInstance.getAreaOfEffect() != null) {
					currentInstance.setAreaOfEffect(null);
					updateAreaOfEffectParams();
					saveItem();
				}
			}
		});
	}

	private void updateAreaOfEffectParams() {
		int[] params = new int[currentInstance.getAreaOfEffect().getParameterCount()];
		for(int i = 0; i < params.length; i++) {
			if(currentInstance.getAreaOfEffectParams().length > i) {
				params[i] = currentInstance.getAreaOfEffectParams()[i];
			}
			else {
				params[i] = 1;
			}
		}

		aoeParam1Edit.setEnabled(params.length > 0);
		if(params.length > 0) {
			aoeParam1Edit.setText(String.valueOf(params[0]));
		}
		else {
			aoeParam1Edit.setError(null);
		}

		aoeParam2Edit.setEnabled(params.length > 1);
		if(params.length > 1) {
			aoeParam2Edit.setText(String.valueOf(params[1]));
		}
		else {
			aoeParam2Edit.setError(null);
		}

		aoeParam3Edit.setEnabled(params.length > 2);
		if(params.length > 2) {
			aoeParam3Edit.setText(String.valueOf(params[2]));
		}
		else {
			aoeParam3Edit.setError(null);
		}

		aoeParam4Edit.setEnabled(params.length > 3);
		if(params.length > 3) {
			aoeParam4Edit.setText(String.valueOf(params[3]));
		}
		else {
			aoeParam4Edit.setError(null);
		}
	}

	private EditText initAoeParamEdit(View layout, @IdRes int id, @StringRes final int validationId,
								  final int paramIndex) {
		EditText editText = (EditText)layout.findViewById(id);
		final EditText finalEditText = editText;
		editText.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length() == 0) {
					finalEditText.setError(getString(validationId));
				}
			}
		});
		editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					if(finalEditText.getText().length() > 0) {
						int param = Integer.valueOf(finalEditText.getText().toString());
						if(currentInstance.getAreaOfEffectParams().length <= paramIndex) {
							int[] params = new int[currentInstance.getAreaOfEffect().getParameterCount()];
							int length = params.length <= currentInstance.getAreaOfEffectParams().length ?
										 params.length : currentInstance.getAreaOfEffectParams().length;
							System.arraycopy(currentInstance.getAreaOfEffectParams(), 0, params, 0, length);
							for(int i = currentInstance.getAreaOfEffectParams().length; i < paramIndex; i++) {
								params[i] = 1;
							}
							if(currentInstance.getAreaOfEffect().getParameterCount() > paramIndex) {
								params[paramIndex] = param;
							}
							currentInstance.setAreaOfEffectParams(params);
							saveItem();
						}
						else if(currentInstance.getAreaOfEffectParams()[paramIndex] != param){
							currentInstance.getAreaOfEffectParams()[paramIndex] = param;
							saveItem();
						}
					}
				}
			}
		});
		return editText;
	}

	private void initDurationSpinner(View layout) {
		durationSpinner = (Spinner)layout.findViewById(R.id.duration_spinner);
		durationSpinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.single_field_row);
		durationSpinner.setAdapter(durationSpinnerAdapter);

		durationSpinnerAdapter.clear();
		durationSpinnerAdapter.addAll(Duration.values());
		durationSpinnerAdapter.notifyDataSetChanged();

		durationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				Duration duration = durationSpinnerAdapter.getItem(position);
				if(duration != null && !duration.equals(currentInstance.getDuration())) {
					currentInstance.setDuration(duration);
					updateDurationParams();
					saveItem();
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				if(currentInstance.getDuration() != null) {
					currentInstance.setDuration(null);
					updateDurationParams();
				}
			}
		});
	}

	private void updateDurationParams() {
		int[] params = new int[currentInstance.getDuration().getParameterCount()];
		for(int i = 0; i < params.length; i++) {
			if(currentInstance.getDurationParams().length > i) {
				params[i] = currentInstance.getDurationParams()[i];
			}
			else {
				params[i] = 1;
			}
		}

		durationParam1Edit.setEnabled(params.length > 0);
		if(params.length > 0) {
			durationParam1Edit.setText(String.valueOf(params[0]));
		}
		else {
			durationParam1Edit.setError(null);
		}

		durationParam2Edit.setEnabled(params.length > 1);
		if(params.length > 1) {
			durationParam2Edit.setText(String.valueOf(params[1]));
		}
		else {
			durationParam2Edit.setError(null);
		}
	}

	private EditText initDurationParamEdit(View layout, @IdRes int id, @StringRes final int validationId,
								  final int paramIndex) {
		EditText editText = (EditText)layout.findViewById(id);
		final EditText finalEditText = editText;
		editText.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length() == 0) {
					finalEditText.setError(getString(validationId));
				}
			}
		});
		editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					if(finalEditText.getText().length() > 0) {
						int param = Integer.valueOf(finalEditText.getText().toString());
						if(currentInstance.getDurationParams().length <= paramIndex) {
							int[] params = new int[currentInstance.getDuration().getParameterCount()];
							int length = params.length <= currentInstance.getDurationParams().length ?
										 params.length : currentInstance.getDurationParams().length;
							System.arraycopy(currentInstance.getDurationParams(), 0, params, 0, length);
							for(int i = currentInstance.getDurationParams().length; i < paramIndex; i++) {
								params[i] = 1;
							}
							if(currentInstance.getDuration().getParameterCount() > paramIndex) {
								params[paramIndex] = param;
							}
							currentInstance.setDurationParams(params);
							saveItem();
						}
						else if(currentInstance.getDurationParams()[paramIndex] != param){
							currentInstance.getDurationParams()[paramIndex] = param;
							saveItem();
						}
					}
				}
			}
		});
		return editText;
	}

	private void initRangeSpinner(View layout) {
		rangeSpinner = (Spinner)layout.findViewById(R.id.range_spinner);
		rangeSpinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.single_field_row);
		rangeSpinner.setAdapter(rangeSpinnerAdapter);

		rangeSpinnerAdapter.clear();
		rangeSpinnerAdapter.addAll(Range.values());
		rangeSpinnerAdapter.notifyDataSetChanged();

		rangeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				Range range = rangeSpinnerAdapter.getItem(position);
				if(range != null && !range.equals(currentInstance.getRange())) {
					currentInstance.setRange(range);
					if(range.getParameterCount() == 0) {
						currentInstance.setRangeParam(null);
						rangeParamEdit.setEnabled(false);
						rangeParamEdit.setError(null);
					}
					else {
						if(currentInstance.getRangeParam() == null) {
							currentInstance.setRangeParam(1);
						}
						rangeParamEdit.setEnabled(true);
						rangeParamEdit.setText(String.valueOf(currentInstance.getRangeParam()));
					}
					saveItem();
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				if(currentInstance.getRange() != null) {
					currentInstance.setRange(null);
					currentInstance.setRangeParam(null);
					rangeParamEdit.setEnabled(false);
					saveItem();
				}
			}
		});
	}

	private void initRangeEdit(View layout) {
		rangeParamEdit = (EditText)layout.findViewById(R.id.range_param_edit);
		rangeParamEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length() == 0) {
					rangeParamEdit.setError(getString(R.string.validation_spell_range_param_required));
				}
			}
		});
		rangeParamEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					if(rangeParamEdit.getText().length() > 0) {
						final int param = Integer.valueOf(descriptionEdit.getText().toString());
						if (currentInstance.getRangeParam() == null || param != currentInstance.getRangeParam()) {
							currentInstance.setRangeParam(param);
							saveItem();
						}
					}
				}
			}
		});
	}

	private void initListView(View layout) {
		listView = (ListView) layout.findViewById(R.id.list_view);
		listAdapter = new TwoFieldListAdapter<>(this.getActivity(), 1, 5, this);
		listView.setAdapter(listAdapter);

		loadFilteredSpells(null);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(copyViewsToItem()) {
					saveItem();
				}
				currentInstance = (Spell) listView.getItemAtPosition(position);
				isNew = false;
				if (currentInstance == null) {
					currentInstance = new Spell();
					isNew = true;
				}
				copyItemToViews();
			}
		});
		registerForContextMenu(listView);
	}

	private void loadFilteredSpells(final SpellList filter) {
		Observable<Collection<Spell>> observable;

		if(filter == null || filter.getId() == -1) {
			observable = spellRxHandler.getAll();
		}
		else {
			observable = spellRxHandler.getSpellsForList(filter);
		}
		observable.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Collection<Spell>>() {
					@Override
					public void onCompleted() {
						int position = listAdapter.getPosition(currentInstance);
						if (position == -1 && listAdapter.getCount() > 0) {
							currentInstance = listAdapter.getItem(0);
							isNew = false;
							position = 0;
						}
						if (position >= 0) {
							listView.setSelection(position);
							listView.setItemChecked(position, true);
							listAdapter.notifyDataSetChanged();
						}
						copyItemToViews();
					}
					@Override
					public void onError(Throwable e) {
						Log.e(TAG, "Exception caught getting all Spell instances", e);
						Toast.makeText(SpellsFragment.this.getActivity(),
								getString(R.string.toast_spells_load_failed),
								Toast.LENGTH_SHORT).show();
					}
					@Override
					public void onNext(Collection<Spell> spells) {
						listAdapter.clear();
						listAdapter.addAll(spells);
						listAdapter.notifyDataSetChanged();
						if(filter == null) {
							String toastString;
							toastString = String.format(getString(R.string.toast_spells_loaded), spells.size());
							Toast.makeText(SpellsFragment.this.getActivity(), toastString, Toast.LENGTH_SHORT).show();
						}
					}
				});
	}
}
