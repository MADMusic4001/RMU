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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.controller.rxhandler.item.WeaponRxHandler;
import com.madinnovations.rmu.controller.rxhandler.item.WeaponTemplateRxHandler;
import com.madinnovations.rmu.data.entities.object.Item;
import com.madinnovations.rmu.data.entities.object.ItemTemplate;
import com.madinnovations.rmu.data.entities.object.Slot;
import com.madinnovations.rmu.data.entities.object.Weapon;
import com.madinnovations.rmu.data.entities.object.WeaponTemplate;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.adapters.TwoFieldListAdapter;
import com.madinnovations.rmu.view.di.modules.ItemFragmentModule;
import com.madinnovations.rmu.view.utils.CheckBoxUtils;
import com.madinnovations.rmu.view.utils.EditTextUtils;
import com.madinnovations.rmu.view.utils.SpinnerUtils;

import java.util.Collection;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Handles interactions with the UI for item templates.
 */
public class WeaponsFragment extends Fragment implements TwoFieldListAdapter.GetValues<Weapon>,
		ItemPaneFragment.DataAccessInterface, EditTextUtils.ValuesCallback, SpinnerUtils.ValuesCallback,
		CheckBoxUtils.ValuesCallback, ItemTemplatePaneFragment.DataAccessInterface {
	private static final String TAG = "WeaponsFragment";
	@Inject
	protected WeaponRxHandler              weaponRxHandler;
	@Inject
	protected WeaponTemplateRxHandler      weaponTemplateRxHandler;
	private   TwoFieldListAdapter<Weapon>  listAdapter;
	private   ListView                     listView;
	private   EditText                     bonusEdit;
	private   SpinnerUtils<WeaponTemplate> weaponTemplateSpinnerUtils;
	private   CheckBox                     twoHandedCheckBox;
	private   Weapon                       currentInstance = new Weapon();
	private   boolean                      isNew = true;
	private   ItemPaneFragment             itemPaneFragment;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((CampaignActivity)getActivity()).getActivityComponent().
				newItemFragmentComponent(new ItemFragmentModule(this)).injectInto(this);

		View layout = inflater.inflate(R.layout.weapons_fragment, container, false);

		((TextView)layout.findViewById(R.id.header_field1)).setText(R.string.label_weapon_name);
		((TextView)layout.findViewById(R.id.header_field2)).setText(R.string.label_weapon_description);

		itemPaneFragment = new ItemPaneFragment();
		itemPaneFragment.setDataAccessInterface(this);
		getFragmentManager().beginTransaction().add(R.id.item_pane_container, itemPaneFragment).commit();

		bonusEdit = EditTextUtils.initEdit(layout, getActivity(), this, R.id.bonus_edit, R.string.validation_bonus_required);
		weaponTemplateSpinnerUtils = new SpinnerUtils<>();
		weaponTemplateSpinnerUtils.initSpinner(layout, getActivity(), weaponTemplateRxHandler.getAll(), this,
											   R.id.weapon_template_spinner, null);
		twoHandedCheckBox = CheckBoxUtils.initCheckBox(layout, this, R.id.two_handed_check_box);
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
			currentInstance = new Weapon();
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
		final Weapon item;

		AdapterView.AdapterContextMenuInfo info =
				(AdapterView.AdapterContextMenuInfo)menuItem.getMenuInfo();

		switch (menuItem.getItemId()) {
			case R.id.context_new_item:
				currentInstance = new Weapon();
				isNew = true;
				copyItemToViews();
				listView.clearChoices();
				listAdapter.notifyDataSetChanged();
				return true;
			case R.id.context_delete_item:
				item = (Weapon)listView.getItemAtPosition(info.position);
				if(item != null) {
					deleteItem(item);
					return true;
				}
		}
		return super.onContextItemSelected(menuItem);
	}

	@Override
	public Item getItem() {
		return currentInstance;
	}

	@Override
	public String getValueForEditText(@IdRes int editTextId) {
		String result = null;

		switch (editTextId) {
			case R.id.bonus_edit:
				result = String.valueOf(currentInstance.getBonus());
				break;
		}

		return result;
	}

	@Override
	public void setValueFromEditText(@IdRes int editTextId, String newString) {
		switch (editTextId) {
			case R.id.bonus_edit:
				currentInstance.setBonus(Short.valueOf(newString));
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
			case R.id.weapon_template_spinner:
				result = currentInstance.getItemTemplate();
				break;
		}

		return result;
	}

	@Override
	public void setValueFromSpinner(@IdRes int spinnerId, Object newItem) {
		switch (spinnerId) {
			case R.id.weapon_template_spinner:
				currentInstance.setItemTemplate((WeaponTemplate)newItem);
				saveItem();
				break;
		}
	}

	@Override
	public void observerCompleted(@IdRes int spinnerId) {
		switch (spinnerId) {
			case R.id.weapon_template_spinner:
				break;
		}
	}

	@Override
	public boolean getValueForCheckBox(@IdRes int checkBoxId) {
		boolean result = false;

		switch(checkBoxId) {
			case R.id.two_handed_check_box:
				result = currentInstance.isTwoHanded();
				break;
		}

		return result;
	}

	@Override
	public void setValueFromCheckBox(@IdRes int checkBoxId, boolean newBoolean) {
		switch(checkBoxId) {
			case R.id.two_handed_check_box:
				currentInstance.setTwoHanded(newBoolean);
				saveItem();
				break;
		}
	}

	@Override
	public ItemTemplate getItemTemplate() {
		return currentInstance.getItemTemplate();
	}

	@Override
	public void saveItem() {
		Log.d(TAG, "saveItem: saving weapon " + currentInstance.debugToString());
		if(currentInstance.isValid()) {
			final boolean wasNew = isNew;
			isNew = false;
			weaponRxHandler.save(currentInstance)
					.observeOn(AndroidSchedulers.mainThread())
					.subscribeOn(Schedulers.io())
					.subscribe(new Subscriber<Weapon>() {
						@Override
						public void onCompleted() {}
						@Override
						public void onError(Throwable e) {
							Log.e(TAG, "Exception saving new Weapon: " + currentInstance, e);
							Toast.makeText(getActivity(), R.string.toast_item_save_failed, Toast.LENGTH_SHORT).show();
						}
						@Override
						public void onNext(Weapon savedWeapon) {
							if (wasNew) {
								listAdapter.add(savedWeapon);
								if(savedWeapon == currentInstance) {
									listView.setSelection(listAdapter.getPosition(savedWeapon));
									listView.setItemChecked(listAdapter.getPosition(savedWeapon), true);
								}
								listAdapter.notifyDataSetChanged();
							}
							if(getActivity() != null) {
								Toast.makeText(getActivity(), R.string.toast_item_saved, Toast.LENGTH_SHORT).show();
								int position = listAdapter.getPosition(savedWeapon);
								LinearLayout v = (LinearLayout) listView.getChildAt(position - listView.getFirstVisiblePosition());
								if (v != null) {
									TextView textView = (TextView) v.findViewById(R.id.row_field1);
									textView.setText(savedWeapon.getName() == null ? savedWeapon.getItemTemplate().getName() :
													 savedWeapon.getName());
									textView = (TextView) v.findViewById(R.id.row_field2);
									textView.setText(savedWeapon.getHistory());
								}
							}
						}
					});
		}
	}

	private void deleteItem(@NonNull final Weapon item) {
		weaponRxHandler.deleteById(item.getId())
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
								currentInstance = new Weapon();
								isNew = true;
							}
							copyItemToViews();
							Toast.makeText(getActivity(), R.string.toast_talent_category_deleted, Toast.LENGTH_SHORT).show();
						}
					}
				});
	}

	private void copyItemToViews() {
		itemPaneFragment.copyItemToViews();

		if(currentInstance.getItemTemplate() instanceof WeaponTemplate) {
			weaponTemplateSpinnerUtils.setSelection((WeaponTemplate) currentInstance.getItemTemplate());
		}
		bonusEdit.setText(String.valueOf(currentInstance.getBonus()));
		twoHandedCheckBox.setChecked(currentInstance.isTwoHanded());
	}

	private void initListView(View layout) {
		listView = (ListView) layout.findViewById(R.id.list_view);
		listAdapter = new TwoFieldListAdapter<>(this.getActivity(), 1, 5, this);
		listView.setAdapter(listAdapter);

		weaponRxHandler.getAll()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Collection<Weapon>>() {
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
						Log.e(TAG, "Exception caught loading all Weapon instances in onCreateView", e);
						Toast.makeText(WeaponsFragment.this.getActivity(),
								R.string.toast_items_load_failed,
								Toast.LENGTH_SHORT).show();
					}
					@Override
					public void onNext(Collection<Weapon> creatureCategories) {
						listAdapter.clear();
						listAdapter.addAll(creatureCategories);
						listAdapter.notifyDataSetChanged();
						String toastString;
						toastString = String.format(getString(R.string.toast_items_loaded), creatureCategories.size());
						Toast.makeText(WeaponsFragment.this.getActivity(), toastString, Toast.LENGTH_SHORT).show();
					}
				});

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				currentInstance = (Weapon) listView.getItemAtPosition(position);
				isNew = false;
				if (currentInstance == null) {
					currentInstance = new Weapon();
					WeaponTemplate weaponTemplate = weaponTemplateSpinnerUtils.getSelectedItem();
					currentInstance.setTwoHanded(weaponTemplate != null
							&& (weaponTemplate.getSecondarySlot() == null || weaponTemplate.getSecondarySlot() == Slot.NONE));
					isNew = true;
				}
				copyItemToViews();
			}
		});
		registerForContextMenu(listView);
	}

	@Override
	public CharSequence getField1Value(Weapon item) {
		return item.getName() == null ? item.getItemTemplate().getName() : item.getName();
	}

	@Override
	public CharSequence getField2Value(Weapon item) {
		return item.getHistory();
	}
}
