/*
  Copyright (C) 2016 MadInnovations
  <p></p>
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
package com.madinnovations.rmu.view.activities.item;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.controller.rxhandler.combat.AttackRxHandler;
import com.madinnovations.rmu.controller.rxhandler.combat.DamageTableRxHandler;
import com.madinnovations.rmu.controller.rxhandler.common.SpecializationRxHandler;
import com.madinnovations.rmu.controller.rxhandler.item.ItemTemplateRxHandler;
import com.madinnovations.rmu.controller.rxhandler.item.WeaponTemplateRxHandler;
import com.madinnovations.rmu.data.entities.combat.Attack;
import com.madinnovations.rmu.data.entities.combat.DamageTable;
import com.madinnovations.rmu.data.entities.common.Specialization;
import com.madinnovations.rmu.data.entities.item.ItemTemplate;
import com.madinnovations.rmu.data.entities.item.WeaponTemplate;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.adapters.TwoFieldListAdapter;
import com.madinnovations.rmu.view.di.modules.ItemFragmentModule;
import com.madinnovations.rmu.view.utils.CheckBoxUtils;
import com.madinnovations.rmu.view.utils.SpinnerUtils;
import com.madinnovations.rmu.view.utils.TextInputLayoutUtils;

import java.util.Collection;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Handles interactions with the UI for item templates.
 */
public class WeaponTemplatesFragment extends Fragment implements TwoFieldListAdapter.GetValues<WeaponTemplate>,
		ItemTemplatePaneFragment.DataAccessInterface, SpinnerUtils.ValuesCallback, CheckBoxUtils.ValuesCallback,
		TextInputLayoutUtils.ValuesCallback {
	private static final String TAG = "WeaponTemplatesFragment";
	@Inject
	protected AttackRxHandler                     attackRxHandler;
	@Inject
	protected DamageTableRxHandler                damageTableRxHandler;
	@Inject
	protected ItemTemplateRxHandler               itemTemplateRxHandler;
	@Inject
	protected SpecializationRxHandler             specializationRxHandler;
	@Inject
	protected WeaponTemplateRxHandler             weaponTemplateRxHandler;
	private   TwoFieldListAdapter<WeaponTemplate> listAdapter;
	private   ListView                            listView;
	private   SpinnerUtils<Specialization>        combatSpecializationSpinnerUtils;
	private   SpinnerUtils<DamageTable>           damageTableSpinnerUtils;
	private   SpinnerUtils<Attack>                attackSpinnerUtils;
	private   CheckBox                            braceableCheckbox;
	private   EditText                            fumbleEdit;
	private   EditText                            lengthEdit;
	private   EditText                            sizeAdjustmentEdit;
	private   WeaponTemplate                      currentInstance = new WeaponTemplate();
	private   boolean                             isNew = true;
	private   ItemTemplatePaneFragment            itemTemplatePaneFragment;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((CampaignActivity)getActivity()).getActivityComponent().
				newItemFragmentComponent(new ItemFragmentModule(this)).injectInto(this);

		View layout = inflater.inflate(R.layout.weapon_templates_fragment, container, false);

		((TextView)layout.findViewById(R.id.header_field1)).setText(R.string.label_weapon_template_name);
		((TextView)layout.findViewById(R.id.header_field2)).setText(R.string.label_weapon_template_description);

		itemTemplatePaneFragment = new ItemTemplatePaneFragment();
		itemTemplatePaneFragment.setDataAccessInterface(this);
		getFragmentManager().beginTransaction().add(R.id.item_template_pane_container, itemTemplatePaneFragment).commit();

		combatSpecializationSpinnerUtils = new SpinnerUtils<>();
		combatSpecializationSpinnerUtils.initSpinner(layout, getActivity(), specializationRxHandler.getWeaponSpecializations(),
													 this, R.id.combat_specialization_spinner, null);
		damageTableSpinnerUtils = new SpinnerUtils<>();
		damageTableSpinnerUtils.initSpinner(layout, getActivity(), damageTableRxHandler.getAll(), this,
											R.id.damage_table_spinner, null);
		attackSpinnerUtils = new SpinnerUtils<>();
		attackSpinnerUtils.initSpinner(layout, getActivity(), attackRxHandler.getAll(), this, R.id.attack_spinner, null);
		braceableCheckbox = CheckBoxUtils.initCheckBox(layout, this, R.id.braceable_checkbox);

		fumbleEdit = TextInputLayoutUtils.initEdit(layout, getActivity(), this, R.id.fumble_textInputLayout,
												   R.id.fumble_edit, R.string.validation_weapon_template_fumble_required);
		lengthEdit = TextInputLayoutUtils.initEdit(layout, getActivity(), this, R.id.length_textInputLayout,
												   R.id.length_edit, R.string.validation_weapon_template_length_required);
		sizeAdjustmentEdit = TextInputLayoutUtils.initEdit(layout, getActivity(), this, R.id.size_adjustment_textInputLayout,
												   R.id.size_adjustment_edit, 0);
		initListView(layout);

		setHasOptionsMenu(true);

		return layout;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.items_action_bar, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if(id == R.id.action_new_item) {
			if(copyViewsToItem()) {
				saveItem();
			}
			currentInstance = new WeaponTemplate();
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
		getActivity().getMenuInflater().inflate(R.menu.item_context_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem menuItem) {
		final WeaponTemplate item;

		AdapterView.AdapterContextMenuInfo info =
				(AdapterView.AdapterContextMenuInfo)menuItem.getMenuInfo();

		switch (menuItem.getItemId()) {
			case R.id.context_new_item:
				if(copyViewsToItem()) {
					saveItem();
				}
				currentInstance = new WeaponTemplate();
				isNew = true;
				copyItemToViews();
				listView.clearChoices();
				listAdapter.notifyDataSetChanged();
				return true;
			case R.id.context_delete_item:
				item = (WeaponTemplate)listView.getItemAtPosition(info.position);
				if(item != null) {
					deleteItem(item);
					return true;
				}
		}
		return super.onContextItemSelected(menuItem);
	}

	@Override
	public ItemTemplate getItemTemplate() {
		return currentInstance;
	}

	@Override
	public Object getValueForSpinner(@IdRes int spinnerId) {
		Object result = null;

		switch (spinnerId) {
			case R.id.combat_specialization_spinner:
				result = currentInstance.getCombatSpecialization();
				break;
			case R.id.damage_table_spinner:
				result = currentInstance.getDamageTable();
				break;
			case R.id.attack_spinner:
				result = currentInstance.getAttack();
				break;
		}

		return result;
	}

	@Override
	public void setValueFromSpinner(@IdRes int spinnerId, Object newItem) {
		switch (spinnerId) {
			case R.id.combat_specialization_spinner:
				currentInstance.setCombatSpecialization((Specialization) newItem);
				saveItem();
				break;
			case R.id.damage_table_spinner:
				currentInstance.setDamageTable((DamageTable)newItem);
				saveItem();
				break;
			case R.id.attack_spinner:
				currentInstance.setAttack((Attack)newItem);
				saveItem();
				break;
		}
	}

	@Override
	public void observerCompleted(@IdRes int spinnerId) {
	}

	@Override
	public boolean getValueForCheckBox(@IdRes int checkBoxId) {
		boolean result = false;

		switch (checkBoxId) {
			case R.id.braceable_checkbox:
				result = currentInstance.isBraceable();
				break;
		}

		return result;
	}

	@Override
	public void setValueFromCheckBox(@IdRes int checkBoxId, boolean newBoolean) {
		switch (checkBoxId) {
			case R.id.braceable_checkbox:
				currentInstance.setBraceable(newBoolean);
				saveItem();
				break;
		}
	}

	@Override
	public String getValueForEditText(@IdRes int editTextId) {
		String result = null;

		switch (editTextId) {
			case R.id.fumble_edit:
				result = String.valueOf(currentInstance.getFumble());
				break;
			case R.id.length_edit:
				result = String.valueOf(currentInstance.getLength());
				break;
			case R.id.size_adjustment_edit:
				result = String.valueOf(currentInstance.getSizeAdjustment());
				break;
		}

		return result;
	}

	@Override
	public void setValueFromEditText(@IdRes int editTextId, String newString) {
		switch (editTextId) {
			case R.id.fumble_edit:
				currentInstance.setFumble(Short.valueOf(newString));
				saveItem();
				break;
			case R.id.length_edit:
				currentInstance.setLength(Float.valueOf(newString));
				saveItem();
				break;
			case R.id.size_adjustment_edit:
				if(newString.isEmpty()) {
					currentInstance.setSizeAdjustment(null);
				}
				else {
					currentInstance.setSizeAdjustment(Short.valueOf(newString));
				}
				saveItem();
				break;
		}
	}

	@Override
	public boolean performLongClick(@IdRes int editTextId) {
		return false;
	}

	@Override
	public void saveItem() {
		if(currentInstance.isValid()) {
			final boolean wasNew = isNew;
			isNew = false;
			weaponTemplateRxHandler.save(currentInstance)
					.observeOn(AndroidSchedulers.mainThread())
					.subscribeOn(Schedulers.io())
					.subscribe(new Subscriber<WeaponTemplate>() {
						@Override
						public void onCompleted() {}
						@Override
						public void onError(Throwable e) {
							Log.e(TAG, "Exception saving new WeaponTemplate: " + currentInstance, e);
							Toast.makeText(getActivity(), R.string.toast_weapon_template_save_failed, Toast.LENGTH_SHORT).show();
						}
						@Override
						public void onNext(WeaponTemplate savedWeaponTemplate) {
							if (wasNew) {
								listAdapter.add(savedWeaponTemplate);
								if(savedWeaponTemplate == currentInstance) {
									listView.setSelection(listAdapter.getPosition(savedWeaponTemplate));
									listView.setItemChecked(listAdapter.getPosition(savedWeaponTemplate), true);
								}
								listAdapter.notifyDataSetChanged();
							}
							if(getActivity() != null) {
								Toast.makeText(getActivity(), R.string.toast_weapon_template_saved, Toast.LENGTH_SHORT).show();
								int position = listAdapter.getPosition(savedWeaponTemplate);
								LinearLayout v = (LinearLayout) listView.getChildAt(position - listView.getFirstVisiblePosition());
								if (v != null) {
									TextView textView = (TextView) v.findViewById(R.id.row_field1);
									textView.setText(savedWeaponTemplate.getName());
									textView = (TextView) v.findViewById(R.id.row_field2);
									textView.setText(savedWeaponTemplate.getNotes());
								}
							}
						}
					});
		}
	}

	private void deleteItem(@NonNull final WeaponTemplate item) {
		weaponTemplateRxHandler.deleteById(item.getId())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new Subscriber<Boolean>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e(TAG, "Exception when deleting: " + item, e);
						Toast.makeText(getActivity(), R.string.toast_weapon_template_delete_failed, Toast.LENGTH_SHORT).show();
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
								currentInstance = new WeaponTemplate();
								isNew = true;
							}
							copyItemToViews();
							Toast.makeText(getActivity(), R.string.toast_weapon_template_deleted, Toast.LENGTH_SHORT).show();
						}
					}
				});
	}

	private boolean copyViewsToItem() {
		boolean changed;

		changed = itemTemplatePaneFragment.copyViewsToItem();

		Specialization newSpecialization = combatSpecializationSpinnerUtils.getSelectedItem();
		if(newSpecialization != null && !newSpecialization.equals(currentInstance.getCombatSpecialization())) {
			currentInstance.setCombatSpecialization(newSpecialization);
			changed = true;
		}

		DamageTable newDamageTable = damageTableSpinnerUtils.getSelectedItem();
		if(newDamageTable != null && !newDamageTable.equals(currentInstance.getDamageTable())) {
			currentInstance.setDamageTable(newDamageTable);
			changed = true;
		}

		Attack newAttack = attackSpinnerUtils.getSelectedItem();
		if(newAttack != null && !newAttack.equals(currentInstance.getAttack())) {
			currentInstance.setAttack(newAttack);
			changed = true;
		}

		boolean newBoolean = braceableCheckbox.isChecked();
		if(newBoolean != currentInstance.isBraceable()) {
			currentInstance.setBraceable(newBoolean);
			changed = true;
		}

		String newString = fumbleEdit.getText().toString();
		if(!newString.isEmpty()) {
			short newShort = Short.valueOf(newString);
			if (newShort != currentInstance.getFumble()) {
				currentInstance.setFumble(newShort);
				changed = true;
			}
		}

		newString = lengthEdit.getText().toString();
		if(!newString.isEmpty()) {
			float newFloat = Float.valueOf(newString);
			if (newFloat != currentInstance.getLength()) {
				currentInstance.setLength(newFloat);
				changed = true;
			}
		}

		newString = sizeAdjustmentEdit.getText().toString();
		if(newString.isEmpty() && currentInstance.getSizeAdjustment() != null) {
			currentInstance.setSizeAdjustment(null);
			changed = true;
		}
		else if(!newString.isEmpty()) {
			short newShort = Short.valueOf(newString);
			if (currentInstance.getSizeAdjustment() == null || newShort != currentInstance.getSizeAdjustment()) {
				currentInstance.setSizeAdjustment(newShort);
				changed = true;
			}
		}

		return changed;
	}

	private void copyItemToViews() {
		itemTemplatePaneFragment.copyItemToViews();

		if(currentInstance.getCombatSpecialization() != null) {
			combatSpecializationSpinnerUtils.setSelection(currentInstance.getCombatSpecialization());
		}

		if(currentInstance.getDamageTable() != null) {
			damageTableSpinnerUtils.setSelection(currentInstance.getDamageTable());
		}

		if(currentInstance.getAttack() != null) {
			attackSpinnerUtils.setSelection(currentInstance.getAttack());
		}

		braceableCheckbox.setChecked(currentInstance.isBraceable());
		fumbleEdit.setText(String.valueOf(currentInstance.getFumble()));
		((TextInputLayout)getActivity().findViewById(R.id.fumble_textInputLayout)).setErrorEnabled(false);
		lengthEdit.setText(String.valueOf(currentInstance.getLength()));
		((TextInputLayout)getActivity().findViewById(R.id.length_textInputLayout)).setErrorEnabled(false);
		if(currentInstance.getSizeAdjustment() == null) {
			sizeAdjustmentEdit.setText("");
		}
		else {
			sizeAdjustmentEdit.setText(String.valueOf(currentInstance.getSizeAdjustment()));
		}
	}

	private void initListView(View layout) {
		listView = (ListView) layout.findViewById(R.id.list_view);
		listAdapter = new TwoFieldListAdapter<>(this.getActivity(), 1, 5, this);
		listView.setAdapter(listAdapter);

		weaponTemplateRxHandler.getAll()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Collection<WeaponTemplate>>() {
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
						Log.e(TAG, "Exception caught loading all WeaponTemplate instances in onCreateView", e);
						Toast.makeText(WeaponTemplatesFragment.this.getActivity(),
								R.string.toast_weapon_templates_load_failed,
								Toast.LENGTH_SHORT).show();
					}
					@Override
					public void onNext(Collection<WeaponTemplate> weaponTemplates) {
						listAdapter.clear();
						listAdapter.addAll(weaponTemplates);
						listAdapter.notifyDataSetChanged();
						String toastString;
						toastString = String.format(getString(R.string.toast_weapon_templates_loaded), weaponTemplates.size());
						Toast.makeText(WeaponTemplatesFragment.this.getActivity(), toastString, Toast.LENGTH_SHORT).show();
					}
				});

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(copyViewsToItem()) {
					saveItem();
				}
				currentInstance = (WeaponTemplate) listView.getItemAtPosition(position);
				isNew = false;
				if (currentInstance == null) {
					currentInstance = new WeaponTemplate();
					isNew = true;
				}
				copyItemToViews();
			}
		});
		registerForContextMenu(listView);
	}

	@Override
	public CharSequence getField1Value(WeaponTemplate item) {
		return item.getName();
	}

	@Override
	public CharSequence getField2Value(WeaponTemplate item) {
		return item.getNotes();
	}
}
