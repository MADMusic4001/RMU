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
import android.support.annotation.NonNull;
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
import com.madinnovations.rmu.data.entities.campaign.Campaign;
import com.madinnovations.rmu.data.entities.character.Character;
import com.madinnovations.rmu.data.entities.character.Race;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.adapters.ThreeFieldListAdapter;
import com.madinnovations.rmu.view.adapters.ViewPagerAdapter;
import com.madinnovations.rmu.view.di.modules.CharacterFragmentModule;
import com.madinnovations.rmu.view.utils.Boast;

import java.util.Collection;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Handles interactions with the UI for creature varieties.
 */
public class CharactersFragment extends Fragment implements ThreeFieldListAdapter.GetValues<Character>,
		ViewPagerAdapter.Instantiator{
	private static final String TAG = "CharactersFragment";
	private static final int NUM_PAGES = 6;
	private static final int MAIN_PAGE_INDEX = 0;
	private static final int SKILLS_PAGE_INDEX = 1;
	private static final int PROFESSION_KNACKS_PAGE_INDEX = 2;
	private static final int TALENTS_PAGE_INDEX = 3;
	private static final int BACKGROUND_PAGE_INDEX = 4;
	private static final int GENERATED_PAGE_INDEX = 5;
	@Inject
	protected CharacterRxHandler               characterRxHandler;
	private   ThreeFieldListAdapter<Character> listAdapter;
	private   ListView                         listView;
	private ViewPagerAdapter                   pagerAdapter    = null;
	private Character                          currentInstance = new Character();
	private boolean                            isNew           = true;

	// Getter
	public Character getCurrentInstance() {
		return this.currentInstance;
	}

	/**
	 * Propagates a change made to the profession on the main page to the skills page.
	 */
	public void changeProfession() {
		CharacterSkillsPageFragment skillsPageFragment = (CharacterSkillsPageFragment)pagerAdapter.getFragment(SKILLS_PAGE_INDEX);
		if(skillsPageFragment != null) {
			skillsPageFragment.changeProfession();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((CampaignActivity)getActivity()).getActivityComponent().
				newCharacterFragmentComponent(new CharacterFragmentModule(this)).injectInto(this);

		View layout = inflater.inflate(R.layout.characters_fragment, container, false);

		((TextView)layout.findViewById(R.id.header_field1)).setText(getString(R.string.label_character_first_name));
		((TextView)layout.findViewById(R.id.header_field2)).setText(getString(R.string.label_character_last_name));
		((LinearLayout.LayoutParams)layout.findViewById(R.id.header_field3).getLayoutParams()).weight = 1;
		((TextView)layout.findViewById(R.id.header_field3)).setText(getString(R.string.label_character_known_as));

		initListView(layout);
		initViewPager(layout);

		setHasOptionsMenu(true);

		return layout;
	}

	@Override
	public void onPause() {
		if(copyViewsToItem()) {
			saveItem();
		}
		if(pagerAdapter.getFragment(MAIN_PAGE_INDEX) != null) {
			getChildFragmentManager().beginTransaction().remove(pagerAdapter.getFragment(MAIN_PAGE_INDEX)).commit();
		}
		if(pagerAdapter.getFragment(SKILLS_PAGE_INDEX) != null) {
			getChildFragmentManager().beginTransaction().remove(pagerAdapter.getFragment(SKILLS_PAGE_INDEX)).commit();
		}
		if(pagerAdapter.getFragment(PROFESSION_KNACKS_PAGE_INDEX) != null) {
			getChildFragmentManager().beginTransaction().remove(pagerAdapter.getFragment(PROFESSION_KNACKS_PAGE_INDEX)).commit();
		}
		if(pagerAdapter.getFragment(TALENTS_PAGE_INDEX) != null) {
			getChildFragmentManager().beginTransaction().remove(pagerAdapter.getFragment(TALENTS_PAGE_INDEX)).commit();
		}
		if(pagerAdapter.getFragment(BACKGROUND_PAGE_INDEX) != null) {
			getChildFragmentManager().beginTransaction().remove(pagerAdapter.getFragment(BACKGROUND_PAGE_INDEX)).commit();
		}
		if(pagerAdapter.getFragment(GENERATED_PAGE_INDEX) != null) {
			getChildFragmentManager().beginTransaction().remove(pagerAdapter.getFragment(GENERATED_PAGE_INDEX)).commit();
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
			currentInstance = createNewCharacter();
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
				currentInstance = createNewCharacter();
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
	public Fragment newInstance(int position) {
		Fragment fragment = null;

		switch (position) {
			case MAIN_PAGE_INDEX:
				fragment = CharacterMainPageFragment.newInstance(this);
				break;
			case BACKGROUND_PAGE_INDEX:
				fragment = CharacterBackgroundPageFragment.newInstance(this);
				break;
			case SKILLS_PAGE_INDEX:
				fragment = CharacterSkillsPageFragment.newInstance(this);
				break;
			case PROFESSION_KNACKS_PAGE_INDEX:
				fragment = CharacterProfessionalKnacksPageFragment.newInstance(this);
				break;
			case TALENTS_PAGE_INDEX:
				fragment = CharacterTalentsPageFragment.newInstance(this);
				break;
			case GENERATED_PAGE_INDEX:
				fragment = CharacterGeneratedValuesFragment.newInstance(this);
				break;
		}

		return fragment;
	}

	private boolean copyViewsToItem() {
		boolean changed = false;
		View currentFocusView = getActivity().getCurrentFocus();
		if(currentFocusView != null) {
			currentFocusView.clearFocus();
		}

		CharacterMainPageFragment mainPageFragment = (CharacterMainPageFragment)pagerAdapter.getFragment(MAIN_PAGE_INDEX);
		if(mainPageFragment != null) {
			changed = mainPageFragment.copyViewsToItem();
		}

		CharacterBackgroundPageFragment backgroundPageFragment = (CharacterBackgroundPageFragment)pagerAdapter
				.getFragment(BACKGROUND_PAGE_INDEX);
		if(backgroundPageFragment != null) {
			changed |= backgroundPageFragment.copyViewsToItem();
		}

		CharacterSkillsPageFragment skillsPageFragment = (CharacterSkillsPageFragment)pagerAdapter.getFragment(SKILLS_PAGE_INDEX);
		if(skillsPageFragment != null) {
			changed |= skillsPageFragment.copyViewsToItem();
		}

		CharacterProfessionalKnacksPageFragment professionalKnacksPageFragment =
				(CharacterProfessionalKnacksPageFragment) pagerAdapter.getFragment(PROFESSION_KNACKS_PAGE_INDEX);
		if(professionalKnacksPageFragment != null) {
			changed |= professionalKnacksPageFragment.copyViewsToItem();
		}

		CharacterTalentsPageFragment talentsPageFragment = (CharacterTalentsPageFragment)pagerAdapter.getFragment
				(TALENTS_PAGE_INDEX);
		if(talentsPageFragment != null) {
			changed |= talentsPageFragment.copyViewsToItem();
		}

		return changed;
	}

	private void copyItemToViews() {
		CharacterMainPageFragment mainPageFragment = (CharacterMainPageFragment)pagerAdapter.getFragment(MAIN_PAGE_INDEX);
		if(mainPageFragment != null) {
			mainPageFragment.copyItemToViews();
		}

		CharacterBackgroundPageFragment backgroundPageFragment = (CharacterBackgroundPageFragment)pagerAdapter
				.getFragment(BACKGROUND_PAGE_INDEX);
		if(backgroundPageFragment != null) {
			backgroundPageFragment.copyItemToViews();
		}

		CharacterSkillsPageFragment skillsPageFragment = (CharacterSkillsPageFragment)pagerAdapter.getFragment(SKILLS_PAGE_INDEX);
		if(skillsPageFragment != null) {
			skillsPageFragment.copyItemToViews();
		}

		CharacterProfessionalKnacksPageFragment professionalKnacksPageFragment =
				(CharacterProfessionalKnacksPageFragment) pagerAdapter.getFragment(PROFESSION_KNACKS_PAGE_INDEX);
		if(professionalKnacksPageFragment != null) {
			professionalKnacksPageFragment.copyItemToViews();
		}

		CharacterTalentsPageFragment talentsPageFragment = (CharacterTalentsPageFragment)pagerAdapter.getFragment
				(TALENTS_PAGE_INDEX);
		if(talentsPageFragment != null) {
			talentsPageFragment.copyItemToViews();
		}

		CharacterGeneratedValuesFragment generatedValuesFragment = (CharacterGeneratedValuesFragment)pagerAdapter
				.getFragment(GENERATED_PAGE_INDEX);
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
							Log.e(TAG, "Exception saving new Character: " + currentInstance, e);
							Toast.makeText(getActivity(), R.string.toast_character_save_failed, Toast.LENGTH_SHORT).show();
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
								Boast.makeText(getActivity(), R.string.toast_character_saved, Toast.LENGTH_SHORT).show(true);
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
						Log.e(TAG, "Exception when deleting: " + item, e);
						Toast.makeText(getActivity(), R.string.toast_character_delete_failed, Toast.LENGTH_SHORT).show();
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
								currentInstance = createNewCharacter();
								isNew = true;
							}
							copyItemToViews();
							Toast.makeText(getActivity(), R.string.toast_character_deleted, Toast.LENGTH_SHORT).show();
						}
					}
				});
	}

	private void initViewPager(View layout) {
		ViewPager viewPager = (ViewPager) layout.findViewById(R.id.pager);
		pagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), getActivity(), NUM_PAGES, this,
											R.array.character_page_names);
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
						Log.e(TAG, "Exception caught getting all Character instances in initListView", e);
						Toast.makeText(CharactersFragment.this.getActivity(),
								getString(R.string.toast_characters_load_failed),
								Toast.LENGTH_SHORT).show();
					}
					@Override
					public void onNext(Collection<Character> creatureVarieties) {
						listAdapter.clear();
						listAdapter.addAll(creatureVarieties);
						listAdapter.notifyDataSetChanged();
						Boast.makeText(CharactersFragment.this.getActivity(),
									   String.format(getString(R.string.toast_characters_loaded), creatureVarieties.size()),
									   Toast.LENGTH_SHORT).show(true);
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
					currentInstance = createNewCharacter();
					isNew = true;
				}
				copyItemToViews();
			}
		});
		registerForContextMenu(listView);
	}

	private Character createNewCharacter() {
		Character character = new Character();

		CharacterMainPageFragment mainPageFragment = (CharacterMainPageFragment)pagerAdapter.getFragment(MAIN_PAGE_INDEX);
		if(mainPageFragment != null) {
			Campaign campaign = mainPageFragment.getCampaign();
			if(campaign != null) {
				character.setCampaign(campaign);
				if(campaign.isBuyStats()) {
					character.setStatPurchasePoints(campaign.getPowerLevel().getStatPoints());
				}
			}

			Race race = mainPageFragment.getRace();
			if(race != null) {
				character.setRace(race);
				character.setCurrentDevelopmentPoints((short)(race.getBonusDevelopmentPoints() + 50));
			}
			else {
				character.setCurrentDevelopmentPoints((short)50);
			}
		}

		character.generateStats();

		return character;
	}
}
