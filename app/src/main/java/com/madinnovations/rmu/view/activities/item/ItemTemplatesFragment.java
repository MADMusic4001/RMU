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

import java.io.Serializable;
import java.util.Collection;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.content.ContentValues.TAG;

/**
 * Handles interactions with the UI for item templates.
 */
public class ItemTemplatesFragment extends Fragment implements TwoFieldListAdapter.GetValues<ItemTemplate>,
		ItemTemplatePaneFragment.DataAccessInterface, Serializable {
	private static final long serialVersionUID = 1672923104734079235L;
	@Inject
	protected ItemTemplateRxHandler             itemRxHandler;
	@Inject
	protected SpecializationRxHandler           specializationRxHandler;
	@Inject
	protected DamageTableRxHandler              damageTableRxHandler;
	@Inject
	protected WeaponTemplateRxHandler           weaponTemplateRxHandler;
	private   TwoFieldListAdapter<ItemTemplate> listAdapter;
	private   ListView                          listView;
	private   ItemTemplate                      currentInstance = new ItemTemplate();
	private   ItemTemplatePaneFragment          itemTemplatePaneFragment;
	private   boolean                           isNew = true;
	private   Specialization                    defaultCombatSpecialization;
	private   DamageTable                       defaultDamageTable;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((CampaignActivity)getActivity()).getActivityComponent().
				newItemFragmentComponent(new ItemFragmentModule(this)).injectInto(this);

		View layout = inflater.inflate(R.layout.item_templates_fragment, container, false);

		((TextView)layout.findViewById(R.id.header_field1)).setText(R.string.label_item_template_name);
		((TextView)layout.findViewById(R.id.header_field2)).setText(R.string.label_item_description);

		itemTemplatePaneFragment = new ItemTemplatePaneFragment();
		itemTemplatePaneFragment.setDataAccessInterface(this);
		getFragmentManager().beginTransaction().add(R.id.item_template_pane_container, itemTemplatePaneFragment).commit();
		initListView(layout);

		setHasOptionsMenu(true);

		initCombatDefaults();
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
			currentInstance = new ItemTemplate();
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
		final ItemTemplate item;

		AdapterView.AdapterContextMenuInfo info =
				(AdapterView.AdapterContextMenuInfo)menuItem.getMenuInfo();

		switch (menuItem.getItemId()) {
			case R.id.context_new_item:
				currentInstance = new ItemTemplate();
				isNew = true;
				copyItemToViews();
				listView.clearChoices();
				listAdapter.notifyDataSetChanged();
				return true;
			case R.id.context_delete_item:
				item = (ItemTemplate)listView.getItemAtPosition(info.position);
				if(item != null) {
					deleteItem(item);
					return true;
				}
				break;
			case R.id.context_make_weapon:
				item = (ItemTemplate)listView.getItemAtPosition(info.position);
				if(item != null) {
					WeaponTemplate weaponTemplate = new WeaponTemplate(item);
					weaponTemplate.setCombatSpecialization(defaultCombatSpecialization);
					weaponTemplate.setDamageTable(defaultDamageTable);
					removeItemFromList(item);
					weaponTemplateRxHandler.save(weaponTemplate).subscribe(new Subscriber<WeaponTemplate>() {
						@Override
						public void onCompleted() {}
						@Override
						public void onError(Throwable e) {
							Log.e(TAG, "onError: Excception caught saving weapon template.", e);
							Toast.makeText(ItemTemplatesFragment.this.getActivity(), R.string.toast_weapon_template_save_failed,
									Toast.LENGTH_LONG).show();
						}
						@Override
						public void onNext(WeaponTemplate weaponTemplate) {
							Toast.makeText(ItemTemplatesFragment.this.getActivity(), R.string.toast_weapon_template_saved,
									Toast.LENGTH_LONG).show();
						}
					});
				}
				break;
		}
		return super.onContextItemSelected(menuItem);
	}

	@Override
	public ItemTemplate getItemTemplate() {
		return currentInstance;
	}

	@Override
	public void saveItem() {
		if(currentInstance.isValid()) {
			final boolean wasNew = isNew;
			isNew = false;
			itemRxHandler.save(currentInstance)
					.observeOn(AndroidSchedulers.mainThread())
					.subscribeOn(Schedulers.io())
					.subscribe(new Subscriber<ItemTemplate>() {
						@Override
						public void onCompleted() {}
						@Override
						public void onError(Throwable e) {
							Log.e("ItemTemplatesFragment", "Exception saving new ItemTemplate: " + currentInstance, e);
							Toast.makeText(getActivity(), R.string.toast_item_save_failed, Toast.LENGTH_SHORT).show();
						}
						@Override
						public void onNext(ItemTemplate savedItemTemplate) {
							if (wasNew) {
								listAdapter.add(savedItemTemplate);
								if(savedItemTemplate == currentInstance) {
									listView.setSelection(listAdapter.getPosition(savedItemTemplate));
									listView.setItemChecked(listAdapter.getPosition(savedItemTemplate), true);
								}
								listAdapter.notifyDataSetChanged();
							}
							if(getActivity() != null) {
								Toast.makeText(getActivity(), R.string.toast_item_saved, Toast.LENGTH_SHORT).show();
								int position = listAdapter.getPosition(savedItemTemplate);
								LinearLayout v = (LinearLayout) listView.getChildAt(position - listView.getFirstVisiblePosition());
								if (v != null) {
									TextView textView = (TextView) v.findViewById(R.id.row_field1);
									textView.setText(savedItemTemplate.getName());
									textView = (TextView) v.findViewById(R.id.row_field2);
									textView.setText(savedItemTemplate.getNotes());
								}
							}
						}
					});
		}
	}

	private void deleteItem(@NonNull final ItemTemplate item) {
		weaponTemplateRxHandler.deleteById(item.getId())
				.subscribe(new Subscriber<Boolean>() {
					@Override
					public void onCompleted() {
						Log.d(TAG, "onCompleted: weapon template deleted");
					}
					@Override
					public void onError(Throwable e) {
						Log.e(TAG, "onError: Exception caught deleting weapon templage", e);
					}
					@Override
					public void onNext(Boolean success) {
						if(success) {
							removeItemFromList(item);
						}
						else {
							itemRxHandler.deleteById(item.getId())
									.subscribe(new Subscriber<Boolean>() {
										@Override
										public void onCompleted() {}
										@Override
										public void onError(Throwable e) {
											Log.e("ItemTemplatesFragment", "Exception when deleting: " + item, e);
											Toast.makeText(getActivity(), R.string.toast_item_delete_failed, Toast.LENGTH_SHORT).show();
										}
										@Override
										public void onNext(Boolean success) {
											if(success) {
												removeItemFromList(item);
											}
										}
									});
						}
					}
				});
	}

	private void copyItemToViews() {
		itemTemplatePaneFragment.copyItemToViews();
	}

	private void removeItemFromList(@NonNull final ItemTemplate item) {
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
			currentInstance = new ItemTemplate();
			isNew = true;
		}
		copyItemToViews();
		Toast.makeText(getActivity(), R.string.toast_talent_category_deleted, Toast.LENGTH_SHORT).show();
	}

	private void initListView(View layout) {
		listView = (ListView) layout.findViewById(R.id.list_view);
		listAdapter = new TwoFieldListAdapter<>(this.getActivity(), 1, 5, this);
		listView.setAdapter(listAdapter);

		itemRxHandler.getAllWithoutSubclass()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Collection<ItemTemplate>>() {
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
						Log.e("ItemTemplatesFragment",
								"Exception caught getting all ItemTemplate instances in onCreateView", e);
						Toast.makeText(ItemTemplatesFragment.this.getActivity(),
								R.string.toast_items_load_failed,
								Toast.LENGTH_SHORT).show();
					}
					@Override
					public void onNext(Collection<ItemTemplate> creatureCategories) {
						listAdapter.clear();
						listAdapter.addAll(creatureCategories);
						listAdapter.notifyDataSetChanged();
						String toastString;
						toastString = String.format(getString(R.string.toast_items_loaded), creatureCategories.size());
						Toast.makeText(ItemTemplatesFragment.this.getActivity(), toastString, Toast.LENGTH_SHORT).show();
					}
				});

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				currentInstance = (ItemTemplate) listView.getItemAtPosition(position);
				isNew = false;
				if (currentInstance == null) {
					currentInstance = new ItemTemplate();
					isNew = true;
				}
				copyItemToViews();
			}
		});
		registerForContextMenu(listView);
	}

	@Override
	public CharSequence getField1Value(ItemTemplate item) {
		return item.getName();
	}

	@Override
	public CharSequence getField2Value(ItemTemplate item) {
		return item.getNotes();
	}

	private void initCombatDefaults() {
		specializationRxHandler.getAllAttackSpecializations().subscribe(new Subscriber<Collection<Specialization>>() {
			@Override
			public void onCompleted() {}
			@Override
			public void onError(Throwable e) {
				Log.e(TAG, "onError: Exception caught getting attack specializations.", e);
			}
			@Override
			public void onNext(Collection<Specialization> specializations) {
				defaultCombatSpecialization = specializations.iterator().next();
			}
		});

		damageTableRxHandler.getAll().subscribe(new Subscriber<Collection<DamageTable>>() {
			@Override
			public void onCompleted() {}
			@Override
			public void onError(Throwable e) {
				Log.e(TAG, "onError: Exception caught getting all DamageTable instances.", e);
			}
			@Override
			public void onNext(Collection<DamageTable> damageTables) {
				defaultDamageTable = damageTables.iterator().next();
			}
		});
	}
}
