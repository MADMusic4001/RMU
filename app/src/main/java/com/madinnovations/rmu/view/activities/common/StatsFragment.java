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
package com.madinnovations.rmu.view.activities.common;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.madinnovations.rmu.R;
import com.madinnovations.rmu.controller.rxhandler.FileRxHandler;
import com.madinnovations.rmu.controller.rxhandler.common.StatRxHandler;
import com.madinnovations.rmu.data.entities.common.Stat;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.adapters.ThreeFieldListAdapter;
import com.madinnovations.rmu.view.di.modules.CommonFragmentModule;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Handles interactions with the UI for stats.
 */
public class StatsFragment extends Fragment implements ThreeFieldListAdapter.GetValues<Stat> {
	@Inject
	protected StatRxHandler statRxHandler;
	@Inject
	protected FileRxHandler fileRxHandler;
	private ThreeFieldListAdapter<Stat> listAdapter = null;
	private ListView listView;
	private EditText abbreviationEdit;
	private EditText nameEdit;
	private EditText descriptionEdit;
	private Stat currentInstance = new Stat();
	private boolean isNew = true;
	private Collection<Stat> importData = null;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((CampaignActivity)getActivity()).getActivityComponent().
				newCommonFragmentComponent(new CommonFragmentModule(this)).injectInto(this);

		View layout = inflater.inflate(R.layout.stats_fragment, container, false);

		((TextView)layout.findViewById(R.id.header_field1)).setText(getString(R.string.label_abbreviation));
		((TextView)layout.findViewById(R.id.header_field2)).setText(getString(R.string.label_name));
		((LinearLayout.LayoutParams)layout.findViewById(R.id.header_field3).getLayoutParams()).weight = 6;
		((TextView)layout.findViewById(R.id.header_field3)).setText(getString(R.string.label_description));

		initAbbreviationEdit(layout);
		initNameEdit(layout);
		initDescriptionEdit(layout);

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
		inflater.inflate(R.menu.stats_action_bar, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if(id == R.id.action_new_stat) {
			if(copyViewsToItem()) {
				saveItem();
			}
			currentInstance = new Stat();
			isNew = true;
			copyItemToViews();
			listView.clearChoices();
			listAdapter.notifyDataSetChanged();
			return true;
		}
		else if(id == R.id.action_export) {
			exportStats();
		}
		else if(id == R.id.action_import) {
			importStats();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		getActivity().getMenuInflater().inflate(R.menu.stat_context_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		final Stat stat;

		AdapterView.AdapterContextMenuInfo info =
				(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

		switch (item.getItemId()) {
			case R.id.context_new_stat:
				if(copyViewsToItem()) {
					saveItem();
				}
				currentInstance = new Stat();
				isNew = true;
				copyItemToViews();
				listView.clearChoices();
				listAdapter.notifyDataSetChanged();
				return true;
			case R.id.context_delete_stat:
				stat = (Stat) listView.getItemAtPosition(info.position);
				if(stat != null) {
					deleteItem(stat);
					return true;
				}
				break;
		}
		return super.onContextItemSelected(item);
	}

	private boolean copyViewsToItem() {
		boolean changed = false;

		String newValue = abbreviationEdit.getText().toString();
		if(newValue.isEmpty()) {
			newValue = null;
		}
		if((newValue == null && currentInstance.getAbbreviation() != null) ||
				(newValue != null && !newValue.equals(currentInstance.getAbbreviation()))) {
			currentInstance.setAbbreviation(newValue);
			changed = true;
		}

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

		return changed;
	}

	private void copyItemToViews() {
		abbreviationEdit.setText(currentInstance.getAbbreviation());
		nameEdit.setText(currentInstance.getName());
		descriptionEdit.setText(currentInstance.getDescription());

		if(currentInstance.getAbbreviation() != null && !currentInstance.getAbbreviation().isEmpty()) {
			abbreviationEdit.setError(null);
		}
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
			statRxHandler.save(currentInstance)
					.observeOn(AndroidSchedulers.mainThread())
					.subscribeOn(Schedulers.io())
					.subscribe(new Subscriber<Stat>() {
						@Override
						public void onCompleted() {}
						@Override
						public void onError(Throwable e) {
							Log.e("StatsFragment", "Exception saving new Stat: " + currentInstance, e);
							Toast.makeText(getActivity(), getString(R.string.toast_stat_save_failed), Toast.LENGTH_SHORT).show();
						}
						@Override
						public void onNext(Stat savedItem) {
							if (wasNew) {
								listAdapter.add(savedItem);
								if(savedItem == currentInstance) {
									listView.setSelection(listAdapter.getPosition(savedItem));
									listView.setItemChecked(listAdapter.getPosition(savedItem), true);
								}
								listAdapter.notifyDataSetChanged();
							}
							if(getActivity() != null) {
								Toast.makeText(getActivity(), getString(R.string.toast_stat_saved), Toast.LENGTH_SHORT).show();
								int position = listAdapter.getPosition(savedItem);
								LinearLayout v = (LinearLayout) listView.getChildAt(position - listView.getFirstVisiblePosition());
								if (v != null) {
									TextView textView = (TextView) v.findViewById(R.id.row_field1);
									textView.setText(savedItem.getAbbreviation());
									textView = (TextView) v.findViewById(R.id.row_field2);
									textView.setText(savedItem.getName());
									textView = (TextView) v.findViewById(R.id.row_field3);
									textView.setText(savedItem.getDescription());
								}
							}
						}
					});
		}
	}

	private void deleteItem(@NonNull final Stat item) {
		statRxHandler.deleteById(item.getId())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new Subscriber<Boolean>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e("StatsFragment", "Exception when deleting: " + item, e);
						String toastString = getString(R.string.toast_stat_delete_failed);
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
								currentInstance = new Stat();
								isNew = true;
							}
							copyItemToViews();
							Toast.makeText(getActivity(), getString(R.string.toast_stat_deleted), Toast.LENGTH_SHORT).show();
						}
					}
				});
	}

	private void initAbbreviationEdit(View layout) {
		abbreviationEdit = (EditText)layout.findViewById(R.id.abbreviation_edit);
		abbreviationEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length() == 0 && abbreviationEdit != null) {
					abbreviationEdit.setError(getString(R.string.validation_abbreviation_required));
				}
			}
		});
		abbreviationEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					final String newAbbreviation = abbreviationEdit.getText().toString();
					if (currentInstance != null && !newAbbreviation.equals(currentInstance.getAbbreviation())) {
						currentInstance.setAbbreviation(newAbbreviation);
						saveItem();
					}
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
					nameEdit.setError(getString(R.string.validation_stat_name_required));
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
		descriptionEdit = (EditText)layout.findViewById(R.id.description_edit);
		descriptionEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length() == 0 && descriptionEdit != null) {
					descriptionEdit.setError(getString(R.string.validation_stat_description_required));
				}
			}
		});
		descriptionEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if (!hasFocus) {
					final String newDescription = descriptionEdit.getText().toString();
					if (currentInstance != null && !newDescription.equals(currentInstance.getDescription())) {
						currentInstance.setDescription(newDescription);
						saveItem();
					}
				}
			}
		});
	}

	private void initListView(View layout) {
		listView = (ListView) layout.findViewById(R.id.list_view);

		listAdapter = new ThreeFieldListAdapter<>(this.getActivity(), 1, 1, 6, this);
		listView.setAdapter(listAdapter);

		statRxHandler.getAll()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Collection<Stat>>() {
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
						Log.e("StatsFragment", "Exception caught getting all Stat instances", e);
						Toast.makeText(StatsFragment.this.getActivity(), getString(R.string.toast_stats_load_failed),
								Toast.LENGTH_SHORT).show();
					}
					@Override
					public void onNext(Collection<Stat> stats) {
						listAdapter.clear();
						listAdapter.addAll(stats);
						listAdapter.notifyDataSetChanged();
						String toastString;
						toastString = String.format(getString(R.string.toast_stats_loaded), stats.size());
						Toast.makeText(StatsFragment.this.getActivity(), toastString, Toast.LENGTH_SHORT).show();
					}
				});

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(copyViewsToItem()) {
					saveItem();
				}
				currentInstance = (Stat) listView.getItemAtPosition(position);
				isNew = false;
				if (currentInstance == null) {
					currentInstance = new Stat();
					isNew = true;
				}
				copyItemToViews();
			}
		});
		registerForContextMenu(listView);
	}

	private void exportStats() {
		statRxHandler.getAll()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new Subscriber<Collection<Stat>>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e("RMU", "Error getting all stats", e);
						Toast.makeText(getActivity(), getString(R.string.toast_stats_export_failed),
								Toast.LENGTH_SHORT).show();
					}
					@Override
					public void onNext(Collection<Stat> stats) {
						JSONArray jsonArray = new JSONArray();
						for(Stat stat : stats) {
							JSONObject statObject = new JSONObject();
							try {
								statObject.put("abbreviation", stat.getAbbreviation())
										.put("name", stat.getName())
										.put("description", stat.getDescription());
								jsonArray.put(statObject);
							}
							catch(JSONException ex) {
								Log.e("RMU", "JSONException caught exporting Stats", ex);
							}
						}
						fileRxHandler.writeFile("stats.json", jsonArray.toString())
								.observeOn(AndroidSchedulers.mainThread())
								.subscribeOn(Schedulers.io())
								.subscribe(new Subscriber<Boolean>() {
									@Override
									public void onCompleted() {
										Toast.makeText(getActivity(), getString(R.string.toast_stats_export_succeeded),
												Toast.LENGTH_SHORT).show();
									}
									@Override
									public void onError(Throwable e) {
										Log.e("RMU", "Error writing stats.json", e);
										Toast.makeText(getActivity(), getString(R.string.toast_stats_export_failed),
												Toast.LENGTH_SHORT).show();
									}
									@Override
									public void onNext(Boolean aBoolean) {
									}
								});
					}
				});
	}

	private void importStats() {
		importData = null;
		fileRxHandler.readFile("stats.json")
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new Subscriber<String>() {
					@Override
					public void onCompleted() {
						statRxHandler.deleteAll()
								.observeOn(AndroidSchedulers.mainThread())
								.subscribeOn(Schedulers.io())
								.subscribe(new Subscriber<Collection<Stat>>() {
									@Override
									public void onCompleted() {
										statRxHandler.save(importData)
												.observeOn(AndroidSchedulers.mainThread())
												.subscribeOn(Schedulers.io())
												.subscribe(new Subscriber<Boolean>() {
													@Override
													public void onCompleted() {
														listAdapter.clear();
														listAdapter.addAll(importData);
														listAdapter.notifyDataSetChanged();
														Toast.makeText(getActivity(), getString(R.string.toast_stats_import_succeeded),
																Toast.LENGTH_SHORT).show();
													}
													@Override
													public void onError(Throwable e) {
														Log.e("RMU", "Error occurred saving imported Stat instances", e);
														Toast.makeText(getActivity(), getString(R.string.toast_stats_import_failed),
																Toast.LENGTH_SHORT).show();
													}
													@Override
													public void onNext(Boolean aBoolean) {}
												});
									}
									@Override
									public void onError(Throwable e) {
										Log.e("RMU", "Error occurred saving imported Stat instances", e);
										Toast.makeText(getActivity(), getString(R.string.toast_stats_import_failed),
												Toast.LENGTH_SHORT).show();
									}
									@Override
									public void onNext(Collection<Stat> stats) {
									}
								});
					}
					@Override
					public void onError(Throwable e) {
						Log.e("RMU", "Error reading stats.json");
						Toast.makeText(getActivity(), getString(R.string.toast_stats_import_failed), Toast.LENGTH_SHORT).show();
					}
					@Override
					public void onNext(String s) {
						//noinspection unchecked
						importData = new Gson().fromJson(s, new TypeToken<Collection<Stat>>(){}.getType());
					}
				});
	}

	@Override
	public CharSequence getField1Value(Stat stat) {
		return stat.getAbbreviation();
	}

	@Override
	public CharSequence getField2Value(Stat stat) {
		return stat.getName();
	}

	@Override
	public CharSequence getField3Value(Stat stat) {
		return stat.getDescription();
	}
}
