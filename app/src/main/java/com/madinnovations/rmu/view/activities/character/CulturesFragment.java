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
package com.madinnovations.rmu.view.activities.character;

import android.app.Fragment;
import android.os.Bundle;
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
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.controller.rxhandler.character.CultureRxHandler;
import com.madinnovations.rmu.controller.rxhandler.common.SkillRxHandler;
import com.madinnovations.rmu.data.entities.character.Culture;
import com.madinnovations.rmu.data.entities.character.CultureSkillCategoryRanks;
import com.madinnovations.rmu.data.entities.character.CultureSkillRank;
import com.madinnovations.rmu.data.entities.common.Skill;
import com.madinnovations.rmu.data.entities.common.SkillCategory;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.adapters.TwoFieldListAdapter;
import com.madinnovations.rmu.view.adapters.character.CultureSkillRanksListAdapter;
import com.madinnovations.rmu.view.di.modules.CharacterFragmentModule;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Handles interactions with the UI for body parts.
 */
public class CulturesFragment extends Fragment implements TwoFieldListAdapter.GetValues<Culture>,
		CultureSkillRanksListAdapter.CultureRanksCallbacks {
	private static final String LOG_TAG = "CulturesFragment";
	@Inject
	protected CultureRxHandler             cultureRxHandler;
	@Inject
	protected SkillRxHandler               skillRxHandler;
	private   TwoFieldListAdapter<Culture> listAdapter;
	private   CultureSkillRanksListAdapter skillRanksListAdapter;
	private   ListView                     listView;
	private   EditText                     nameEdit;
	private   EditText                     descriptionEdit;
	private   EditText                     tradesAndCraftsEdit;
	private   Collection<Skill>            skills = null;
	private   Culture                      currentInstance = new Culture();
	private   boolean                      isNew           = true;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((CampaignActivity)getActivity()).getActivityComponent().
				newCharacterFragmentComponent(new CharacterFragmentModule(this)).injectInto(this);

		View layout = inflater.inflate(R.layout.cultures_fragment, container, false);

		((TextView)layout.findViewById(R.id.header_field1)).setText(getString(R.string.label_culture_name));
		((TextView)layout.findViewById(R.id.header_field2)).setText(getString(R.string.label_culture_description));

		initNameEdit(layout);
		initDescriptionEdit(layout);
		initTradesAndCraftsEdit(layout);
		initSkillRanksListView(layout);
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
		inflater.inflate(R.menu.cultures_action_bar, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if(id == R.id.action_new_culture) {
			if(copyViewsToItem()) {
				saveItem();
			}
			currentInstance = new Culture();
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
		getActivity().getMenuInflater().inflate(R.menu.culture_context_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		final Culture culture;

		AdapterView.AdapterContextMenuInfo info =
				(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

		switch (item.getItemId()) {
			case R.id.context_new_culture:
				if(copyViewsToItem()) {
					saveItem();
				}
				currentInstance = new Culture();
				isNew = true;
				copyItemToViews();
				listView.clearChoices();
				listAdapter.notifyDataSetChanged();
				return true;
			case R.id.context_delete_culture:
				culture = (Culture)listView.getItemAtPosition(info.position);
				if(culture != null) {
					deleteItem(culture);
					return true;
				}
				break;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	public Culture getCultureInstance() {
		return currentInstance;
	}

	@Override
	public CharSequence getField1Value(Culture culture) {
		return culture.getName();
	}

	@Override
	public CharSequence getField2Value(Culture culture) {
		return culture.getDescription();
	}

	private boolean copyViewsToItem() {
		boolean changed = false;
		String newString;
		short newShort;

		newString = nameEdit.getText().toString();
		if(newString.isEmpty()) {
			newString = null;
		}
		if((newString == null && currentInstance.getName() != null) ||
				(newString != null && !newString.equals(currentInstance.getName()))) {
			currentInstance.setName(newString);
			changed = true;
		}

		newString = descriptionEdit.getText().toString();
		if(newString.isEmpty()) {
			newString = null;
		}
		if((newString == null && currentInstance.getDescription() != null) ||
				(newString != null && !newString.equals(currentInstance.getDescription()))) {
			currentInstance.setDescription(newString);
			changed = true;
		}

		if(tradesAndCraftsEdit.getText().length() > 0) {
			newShort = Short.valueOf(tradesAndCraftsEdit.getText().toString());
			if(newShort != currentInstance.getTradesAndCraftsRanks()) {
				currentInstance.setTradesAndCraftsRanks(newShort);
				changed = true;
			}
		}

		return changed;
	}

	private void copyItemToViews() {
		nameEdit.setText(currentInstance.getName());
		if(currentInstance.getName() != null && !currentInstance.getName().isEmpty()) {
			nameEdit.setError(null);
		}

		descriptionEdit.setText(currentInstance.getDescription());
		if(currentInstance.getDescription() != null && !currentInstance.getDescription().isEmpty()) {
			descriptionEdit.setError(null);
		}

		tradesAndCraftsEdit.setText(String.valueOf(currentInstance.getTradesAndCraftsRanks()));
		tradesAndCraftsEdit.setError(null);

		if(skills != null) {
			skillRanksListAdapter.clear();
			skillRanksListAdapter.addAll(createRanksList());
			skillRanksListAdapter.notifyDataSetChanged();
		}
	}

	private void deleteItem(final Culture item) {
		cultureRxHandler.deleteById(item.getId())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribeOn(Schedulers.io())
			.subscribe(new Subscriber<Boolean>() {
				@Override
				public void onCompleted() {}
				@Override
				public void onError(Throwable e) {
					Log.e("CultureFragment", "Exception when deleting: " + item, e);
					Toast.makeText(getActivity(), getString(R.string.toast_culture_delete_failed), Toast.LENGTH_SHORT).show();
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
							currentInstance = new Culture();
							isNew = true;
						}
						copyItemToViews();
						Toast.makeText(getActivity(), getString(R.string.toast_culture_deleted), Toast.LENGTH_SHORT).show();
					}
				}
			});
	}

	public void saveItem() {
		if(currentInstance.isValid()) {
			final boolean wasNew = isNew;
			isNew = false;
			cultureRxHandler.save(currentInstance)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new Subscriber<Culture>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e("CulturesFragment", "Exception saving Culture", e);
						String toastString = getString(R.string.toast_culture_save_failed);
						Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
					}
					@Override
					public void onNext(Culture savedCulture) {
						if (wasNew) {
							listAdapter.add(savedCulture);
							if(savedCulture == currentInstance) {
								listView.setSelection(listAdapter.getPosition(savedCulture));
								listView.setItemChecked(listAdapter.getPosition(savedCulture), true);
							}
							listAdapter.notifyDataSetChanged();
						}
						if (getActivity() != null) {
							String toastString;
							toastString = getString(R.string.toast_culture_saved);
							Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();

							int position = listAdapter.getPosition(currentInstance);
							LinearLayout v = (LinearLayout) listView.getChildAt(position - listView.getFirstVisiblePosition());
							if (v != null) {
								TextView textView = (TextView) v.findViewById(R.id.row_field1);
								textView.setText(currentInstance.getName());
								textView = (TextView) v.findViewById(R.id.row_field2);
								textView.setText(currentInstance.getDescription());
							}
						}
					}
				});
		}
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
				if (editable.length() == 0) {
					nameEdit.setError(getString(R.string.validation_culture_name_required));
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
				if (editable.length() == 0) {
					descriptionEdit.setError(getString(R.string.validation_culture_description_required));
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

	private void initTradesAndCraftsEdit(View layout) {
		tradesAndCraftsEdit = (EditText)layout.findViewById(R.id.trades_ranks_edit);

		tradesAndCraftsEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length() == 0) {
					tradesAndCraftsEdit.setError(getString(R.string.validation_culture_trades_ranks_required));
				}
			}
		});
		tradesAndCraftsEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					if(tradesAndCraftsEdit.getText().length() > 0) {
						short newRanks = Short.valueOf(tradesAndCraftsEdit.getText().toString());
						if (currentInstance != null && newRanks != currentInstance.getTradesAndCraftsRanks()) {
							currentInstance.setTradesAndCraftsRanks(newRanks);
							saveItem();
						}
					}
				}
			}
		});
	}

	private void initSkillRanksListView(View layout) {
		ExpandableListView skillRanksListView = (ExpandableListView) layout.findViewById(R.id.skill_ranks_list_view);
		skillRanksListAdapter = new CultureSkillRanksListAdapter(getActivity(), this, skillRanksListView);
		skillRanksListView.setAdapter(skillRanksListAdapter);

	skillRxHandler.getAll()
				.subscribe(new Subscriber<Collection<Skill>>() {
					@Override
					public void onCompleted() {
						skillRanksListAdapter.clear();
						skillRanksListAdapter.addAll(createRanksList());
						skillRanksListAdapter.notifyDataSetChanged();
					}
					@Override
					public void onError(Throwable e) {
						Log.e(LOG_TAG, "Exception caught getting all Skill instances.", e);
					}
					@Override
					public void onNext(Collection<Skill> skills) {
						CulturesFragment.this.skills = skills;
					}
				});
	}

	private void initListView(View layout) {
		listView = (ListView) layout.findViewById(R.id.list_view);
		listAdapter = new TwoFieldListAdapter<>(this.getActivity(), 1, 5, this);
		listView.setAdapter(listAdapter);

		cultureRxHandler.getAll()
			.observeOn(AndroidSchedulers.mainThread())
			.subscribeOn(Schedulers.io())
			.subscribe(new Subscriber<Collection<Culture>>() {
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
					Log.e("CulturesFragment", "Exception caught getting all Culture instances", e);
					Toast.makeText(CulturesFragment.this.getActivity(),
							getString(R.string.toast_cultures_load_failed),
							Toast.LENGTH_SHORT).show();
				}
				@Override
				public void onNext(Collection<Culture> cultures) {
					listAdapter.clear();
					listAdapter.addAll(cultures);
					listAdapter.notifyDataSetChanged();
					if(cultures.size() > 0) {
						listView.setSelection(0);
						listView.setItemChecked(0, true);
						currentInstance = listAdapter.getItem(0);
						isNew = false;
						copyItemToViews();
					}
					String toastString;
					toastString = String.format(getString(R.string.toast_cultures_loaded), cultures.size());
					Toast.makeText(CulturesFragment.this.getActivity(), toastString, Toast.LENGTH_SHORT).show();
				}
			});

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(copyViewsToItem()) {
					saveItem();
				}
				currentInstance = (Culture) listView.getItemAtPosition(position);
				isNew = false;
				if (currentInstance == null) {
					currentInstance = new Culture();
					isNew = true;
				}
				copyItemToViews();
			}
		});
		registerForContextMenu(listView);
	}

	private Collection<CultureSkillCategoryRanks> createRanksList() {
		Map<SkillCategory, CultureSkillCategoryRanks> categoryRanksMap = new HashMap<>();
		for(Skill skill : skills) {
			CultureSkillCategoryRanks categoryRanks = categoryRanksMap.get(skill.getCategory());
			if (categoryRanks == null) {
				categoryRanks = new CultureSkillCategoryRanks(skill.getCategory());
				categoryRanksMap.put(skill.getCategory(), categoryRanks);
			}
			CultureSkillRank cultureSkillRank = new CultureSkillRank(skill);
			cultureSkillRank.setRanks(currentInstance.getSkillRanks().get(skill));
			categoryRanks.getSkillRanksList().add(cultureSkillRank);
		}
		return categoryRanksMap.values();
	}
}
