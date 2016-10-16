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
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
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
import com.madinnovations.rmu.controller.rxhandler.character.CharacterRxHandler;
import com.madinnovations.rmu.data.entities.character.Character;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.adapters.ThreeFieldListAdapter;
import com.madinnovations.rmu.view.di.modules.CharacterFragmentModule;

import java.util.Collection;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Handles interactions with the UI for creature varieties.
 */
public class CharactersFragment extends Fragment implements ThreeFieldListAdapter.GetValues<Character> {
	private static final String LOG_TAG = "CharactersFragment";
	@Inject
	protected CharacterRxHandler               characterRxHandler;
	private   ThreeFieldListAdapter<Character> listAdapter;
	private CharacterMainPageFragment          mainPageFragment = null;
	private CharacterBackgroundPageFragment    backgroundPageFragment = null;
	private CharacterSkillsPageFragment        skillsPageFragment = null;
	private CharacterGeneratedValuesFragment   generatedValuesFragment = null;
	private ListView                           listView;
	CharacterFragmentPagerAdapter              pagerAdapter = null;
	private Character currentInstance = new Character();
	private boolean   isNew           = true;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((CampaignActivity)getActivity()).getActivityComponent().
				newCharacterFragmentComponent(new CharacterFragmentModule(this)).injectInto(this);

		View layout = inflater.inflate(R.layout.characters_fragment, container, false);

		((TextView)layout.findViewById(R.id.header_field1)).setText(getString(R.string.label_character_first_name));
		((TextView)layout.findViewById(R.id.header_field2)).setText(getString(R.string.label_character_last_name));
		((LinearLayout.LayoutParams)layout.findViewById(R.id.header_field3).getLayoutParams()).weight = 1;
		((TextView)layout.findViewById(R.id.header_field3)).setText(getString(R.string.label_character_known_as));

		initViewPager(layout);
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
		inflater.inflate(R.menu.characters_action_bar, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if(id == R.id.action_new_character) {
			if(copyViewsToItem()) {
				saveItem();
			}
			currentInstance = new Character();
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
		getActivity().getMenuInflater().inflate(R.menu.character_context_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		final Character character;

		AdapterView.AdapterContextMenuInfo info =
				(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

		switch (item.getItemId()) {
			case R.id.context_new_character:
				if(copyViewsToItem()) {
					saveItem();
				}
				currentInstance = new Character();
				isNew = true;
				copyItemToViews();
				listView.clearChoices();
				listAdapter.notifyDataSetChanged();
				return true;
			case R.id.context_delete_character:
				character = (Character)listView.getItemAtPosition(info.position);
				if(character != null) {
					deleteItem(character);
					return true;
				}
		}
		return super.onContextItemSelected(item);
	}

	@Override
	public CharSequence getField1Value(Character character) {
		return character.getFirstName();
	}

	@Override
	public CharSequence getField2Value(Character character) {
		return character.getLastName();
	}

	@Override
	public CharSequence getField3Value(Character character) {
		return character.getKnownAs();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();

		if(this.mainPageFragment != null && getFragmentManager().findFragmentById(this.mainPageFragment.getId()) != null) {
			getFragmentManager().beginTransaction().remove(this.mainPageFragment).commit();
			this.mainPageFragment = null;
		}

		if(this.backgroundPageFragment != null && getFragmentManager().findFragmentById(this.backgroundPageFragment.getId()) != null) {
			getFragmentManager().beginTransaction().remove(this.backgroundPageFragment).commit();
			this.backgroundPageFragment = null;
		}

		if(this.skillsPageFragment != null && getFragmentManager().findFragmentById(this.skillsPageFragment.getId()) != null) {
			getFragmentManager().beginTransaction().remove(this.skillsPageFragment).commit();
			this.skillsPageFragment = null;
		}

		if(this.generatedValuesFragment != null &&
				getFragmentManager().findFragmentById(this.generatedValuesFragment.getId()) != null) {
			this.generatedValuesFragment = null;
		}
	}

	private boolean copyViewsToItem() {
		boolean changed = false;

		if(mainPageFragment != null) {
			changed = mainPageFragment.copyViewsToItem();
		}
		if(backgroundPageFragment != null) {
			changed |= backgroundPageFragment.copyViewsToItem();
		}
		if(skillsPageFragment != null) {
			changed |= skillsPageFragment.copyViewsToItem();
		}

		return changed;
	}

	private void copyItemToViews() {
		if(mainPageFragment != null) {
			mainPageFragment.copyItemToViews();
		}
		if(backgroundPageFragment != null) {
			backgroundPageFragment.copyItemToViews();
		}
		if(skillsPageFragment != null) {
			skillsPageFragment.copyItemToViews();
		}
		if(generatedValuesFragment != null) {
			generatedValuesFragment.copyItemToViews();
		}
	}

	public void saveItem() {
		if(currentInstance.isValid()) {
			final boolean wasNew = isNew;
			isNew = false;
			characterRxHandler.save(currentInstance)
					.observeOn(AndroidSchedulers.mainThread())
					.subscribeOn(Schedulers.io())
					.subscribe(new Subscriber<Character>() {
						@Override
						public void onCompleted() {}
						@Override
						public void onError(Throwable e) {
							Log.e(LOG_TAG, "Exception saving new Character: " + currentInstance, e);
							Toast.makeText(getActivity(), getString(R.string.toast_character_save_failed), Toast.LENGTH_SHORT).show();
						}
						@Override
						public void onNext(Character savedItem) {
							if (wasNew) {
								listAdapter.add(savedItem);
								if(savedItem == currentInstance) {
									listView.setSelection(listAdapter.getPosition(savedItem));
									listView.setItemChecked(listAdapter.getPosition(savedItem), true);
								}
								listAdapter.notifyDataSetChanged();
							}
							if(getActivity() != null) {
								Toast.makeText(getActivity(), getString(R.string.toast_character_saved), Toast.LENGTH_SHORT).show();
								int position = listAdapter.getPosition(savedItem);
								LinearLayout v = (LinearLayout) listView.getChildAt(position - listView.getFirstVisiblePosition());
								if (v != null) {
									TextView textView = (TextView) v.findViewById(R.id.row_field1);
									textView.setText(savedItem.getFirstName());
									textView = (TextView) v.findViewById(R.id.row_field2);
									textView.setText(savedItem.getLastName());
									textView = (TextView) v.findViewById(R.id.row_field3);
									textView.setText(savedItem.getKnownAs());
								}
							}
						}
					});
		}
	}

	private void deleteItem(@NonNull final Character item) {
		characterRxHandler.deleteById(item.getId())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new Subscriber<Boolean>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e(LOG_TAG, "Exception when deleting: " + item, e);
						String toastString = getString(R.string.toast_character_delete_failed);
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
								currentInstance = new Character();
								isNew = true;
							}
							copyItemToViews();
							Toast.makeText(getActivity(), getString(R.string.toast_character_deleted), Toast.LENGTH_SHORT).show();
						}
					}
				});
	}

	private void initViewPager(View layout) {
		ViewPager viewPager = (ViewPager) layout.findViewById(R.id.pager);
		pagerAdapter = new CharacterFragmentPagerAdapter(getFragmentManager());
		viewPager.setAdapter(pagerAdapter);
		viewPager.setCurrentItem(0);
	}

	private void initListView(View layout) {
		listView = (ListView) layout.findViewById(R.id.list_view);
		listAdapter = new ThreeFieldListAdapter<>(this.getActivity(), 1, 1, 1, this);
		listView.setAdapter(listAdapter);

		characterRxHandler.getAll()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Collection<Character>>() {
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
						Log.e(LOG_TAG,
								"Exception caught getting all Character instances in initListView", e);
						Toast.makeText(CharactersFragment.this.getActivity(),
								getString(R.string.toast_characters_load_failed),
								Toast.LENGTH_SHORT).show();
					}
					@Override
					public void onNext(Collection<Character> creatureVarieties) {
						listAdapter.clear();
						listAdapter.addAll(creatureVarieties);
						listAdapter.notifyDataSetChanged();
						String toastString;
						toastString = String.format(getString(R.string.toast_characters_loaded), creatureVarieties.size());
						Toast.makeText(CharactersFragment.this.getActivity(), toastString, Toast.LENGTH_SHORT).show();
					}
				});

		// Clicking a row in the listView will send the user to the edit world activity
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(copyViewsToItem()) {
					saveItem();
				}
				currentInstance = (Character) listView.getItemAtPosition(position);
				isNew = false;
				if (currentInstance == null) {
					currentInstance = new Character();
					isNew = true;
				}
				copyItemToViews();
			}
		});
		registerForContextMenu(listView);
	}

	public Character getCurrentInstance() {
		return this.currentInstance;
	}

	/**
	 * Manages the page fragments for a ViewPager
	 */
	private class CharacterFragmentPagerAdapter extends FragmentPagerAdapter {
		CharacterFragmentPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = null;

			switch (position) {
				case 0:
					fragment = Fragment.instantiate(getActivity(), CharacterMainPageFragment.class.getName());
					((CharacterMainPageFragment)fragment).setCharactersFragment(CharactersFragment.this);
					break;
				case 1:
					fragment = Fragment.instantiate(getActivity(), CharacterSkillsPageFragment.class.getName());
					((CharacterSkillsPageFragment)fragment).setCharactersFragment(CharactersFragment.this);
					break;
				case 2:
					fragment = Fragment.instantiate(getActivity(), CharacterBackgroundPageFragment.class.getName());
					((CharacterBackgroundPageFragment)fragment).setCharactersFragment(CharactersFragment.this);
					break;
				case 3:
					fragment = Fragment.instantiate(getActivity(), CharacterGeneratedValuesFragment.class.getName());
					((CharacterGeneratedValuesFragment)fragment).setCharactersFragment(CharactersFragment.this);
					break;
			}
			return fragment;
		}

		@Override
		public int getCount() {
			return 4;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			String title = null;

			switch (position) {
				case 0:
					title = getString(R.string.title_character_main_page);
					break;
				case 1:
					title = getString(R.string.title_character_skills_page);
					break;
				case 2:
					title = getString(R.string.title_character_background_page);
					break;
				case 3:
					title = getString(R.string.title_character_generated_values_page);
					break;
			}
			return title;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			Fragment createdFragment = (Fragment)super.instantiateItem(container, position);
			switch (position) {
				case 0:
					mainPageFragment = (CharacterMainPageFragment)createdFragment;
					break;
				case 1:
					skillsPageFragment= (CharacterSkillsPageFragment)createdFragment;
					break;
				case 2:
					backgroundPageFragment = (CharacterBackgroundPageFragment)createdFragment;
					break;
				case 3:
					generatedValuesFragment = (CharacterGeneratedValuesFragment)createdFragment;
					break;
			}

			return createdFragment;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			super.destroyItem(container, position, object);
			switch (position) {
				case 0:
					mainPageFragment = null;
					break;
				case 1:
					skillsPageFragment = null;
					break;
				case 2:
					backgroundPageFragment= null;
					break;
				case 3:
					generatedValuesFragment = null;
					break;
			}
		}
	}

	public void changeProfession() {
		if(skillsPageFragment != null) {
			skillsPageFragment.changeProfession();
		}
	}
}
