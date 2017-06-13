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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.controller.rxhandler.combat.DamageTableRxHandler;
import com.madinnovations.rmu.controller.rxhandler.common.SpecializationRxHandler;
import com.madinnovations.rmu.controller.rxhandler.item.ItemTemplateRxHandler;
import com.madinnovations.rmu.controller.rxhandler.item.WeaponTemplateRxHandler;
import com.madinnovations.rmu.data.entities.combat.DamageTable;
import com.madinnovations.rmu.data.entities.common.Specialization;
import com.madinnovations.rmu.data.entities.object.ItemTemplate;
import com.madinnovations.rmu.data.entities.object.WeaponTemplate;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.adapters.TwoFieldListAdapter;
import com.madinnovations.rmu.view.di.modules.ItemFragmentModule;
import com.madinnovations.rmu.view.utils.CheckBoxUtils;
import com.madinnovations.rmu.view.utils.SpinnerUtils;

import java.util.Collection;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Handles interactions with the UI for item templates.
 */
public class WeaponTemplatesFragment extends Fragment implements TwoFieldListAdapter.GetValues<WeaponTemplate>,
		ItemTemplatePaneFragment.DataAccessInterface, SpinnerUtils.ValuesCallback, CheckBoxUtils.ValuesCallback {
	private static final String TAG = "WeaponTemplatesFragment";
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
	private   CheckBox                            braceableCheckbox;
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
		braceableCheckbox = CheckBoxUtils.initCheckBox(layout, this, R.id.braceable_checkbox);

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
							Toast.makeText(getActivity(), R.string.toast_item_save_failed, Toast.LENGTH_SHORT).show();
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
								Toast.makeText(getActivity(), R.string.toast_item_saved, Toast.LENGTH_SHORT).show();
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
						Toast.makeText(getActivity(), R.string.toast_item_delete_failed, Toast.LENGTH_SHORT).show();
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
							Toast.makeText(getActivity(), R.string.toast_talent_category_deleted, Toast.LENGTH_SHORT).show();
						}
					}
				});
	}

	private void copyItemToViews() {
		itemTemplatePaneFragment.copyItemToViews();

		if(currentInstance.getCombatSpecialization() != null) {
			combatSpecializationSpinnerUtils.setSelection(currentInstance.getCombatSpecialization());
		}

		if(currentInstance.getDamageTable() != null) {
			damageTableSpinnerUtils.setSelection(currentInstance.getDamageTable());
		}

		braceableCheckbox.setChecked(currentInstance.isBraceable());
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
								R.string.toast_items_load_failed,
								Toast.LENGTH_SHORT).show();
					}
					@Override
					public void onNext(Collection<WeaponTemplate> creatureCategories) {
						listAdapter.clear();
						listAdapter.addAll(creatureCategories);
						listAdapter.notifyDataSetChanged();
						String toastString;
						toastString = String.format(getString(R.string.toast_items_loaded), creatureCategories.size());
						Toast.makeText(WeaponTemplatesFragment.this.getActivity(), toastString, Toast.LENGTH_SHORT).show();
					}
				});

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
