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
package com.madinnovations.rmu.view.activities.campaign;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.controller.rxhandler.FileRxHandler;
import com.madinnovations.rmu.controller.rxhandler.campaign.ImportExportCampaignRxHandler;
import com.madinnovations.rmu.controller.rxhandler.campaign.ImportExportRxHandler;
import com.madinnovations.rmu.data.entities.campaign.Campaign;
import com.madinnovations.rmu.view.RMUApp;
import com.madinnovations.rmu.view.activities.CampaignSelectorDialogFragment;
import com.madinnovations.rmu.view.activities.FileSelectorDialogFragment;
import com.madinnovations.rmu.view.activities.character.CharactersFragment;
import com.madinnovations.rmu.view.activities.character.CulturesFragment;
import com.madinnovations.rmu.view.activities.character.ProfessionsFragment;
import com.madinnovations.rmu.view.activities.character.RacesFragment;
import com.madinnovations.rmu.view.activities.combat.AttacksFragment;
import com.madinnovations.rmu.view.activities.combat.BodyPartsFragment;
import com.madinnovations.rmu.view.activities.combat.CriticalResultsFragment;
import com.madinnovations.rmu.view.activities.combat.CriticalTypesFragment;
import com.madinnovations.rmu.view.activities.combat.DamageResultsFragment;
import com.madinnovations.rmu.view.activities.combat.DiseasesFragment;
import com.madinnovations.rmu.view.activities.common.SizesFragment;
import com.madinnovations.rmu.view.activities.common.SkillCategoriesFragment;
import com.madinnovations.rmu.view.activities.common.SkillsFragment;
import com.madinnovations.rmu.view.activities.common.SpecializationsFragment;
import com.madinnovations.rmu.view.activities.common.TalentCategoriesFragment;
import com.madinnovations.rmu.view.activities.common.TalentsFragment;
import com.madinnovations.rmu.view.activities.creature.CreatureArchetypesFragment;
import com.madinnovations.rmu.view.activities.creature.CreatureCategoriesFragment;
import com.madinnovations.rmu.view.activities.creature.CreatureTypesFragment;
import com.madinnovations.rmu.view.activities.creature.CreatureVarietiesFragment;
import com.madinnovations.rmu.view.activities.creature.CreaturesFragment;
import com.madinnovations.rmu.view.activities.creature.OutlooksFragment;
import com.madinnovations.rmu.view.activities.item.ItemTemplatesFragment;
import com.madinnovations.rmu.view.activities.item.WeaponTemplatesFragment;
import com.madinnovations.rmu.view.activities.item.WeaponsFragment;
import com.madinnovations.rmu.view.activities.play.CampaignsFragment;
import com.madinnovations.rmu.view.activities.play.StartEncounterFragment;
import com.madinnovations.rmu.view.activities.spell.RealmsFragment;
import com.madinnovations.rmu.view.activities.spell.SpellListsFragment;
import com.madinnovations.rmu.view.activities.spell.SpellSubTypesFragment;
import com.madinnovations.rmu.view.activities.spell.SpellTypesFragment;
import com.madinnovations.rmu.view.activities.spell.SpellsFragment;
import com.madinnovations.rmu.view.di.components.ActivityComponent;
import com.madinnovations.rmu.view.di.modules.ActivityModule;

import java.io.File;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Activity class for managing the campaign UI.
 */
public class CampaignActivity extends AppCompatActivity implements FileSelectorDialogFragment.FileSelectorDialogListener,
		CampaignSelectorDialogFragment.CampaignSelectorDialogListener {
	private static final String EXPORT_FILE_NAME = "export.rmu";
	private static final String FILE_SELECTOR_FILTER = "fs_extension_filter";
	private static final String RMU_FILE_EXTENSION = ".rmu";
	private static final String RMU_CAMPAIGN_FILE_EXTENSION = ".cmp";
	@Inject
	FileRxHandler                      fileRxHandler;
	@Inject
	ImportExportRxHandler              importExportRxHandler;
	@Inject
	ImportExportCampaignRxHandler      importExportCampaignRxHandler;
	private ActivityComponent          activityComponent;
	private AboutFragment              aboutFragment;
	private AttacksFragment            attacksFragment;
	private BodyPartsFragment          bodyPartsFragment;
	private CampaignsFragment          campaignsFragment;
	private CharactersFragment         charactersFragment;
	private CreatureArchetypesFragment creatureArchetypesFragment;
	private CreatureCategoriesFragment creatureCategoriesFragment;
	private CreaturesFragment          creaturesFragment;
	private CreatureTypesFragment      creatureTypesFragment;
	private CreatureVarietiesFragment  creatureVarietiesFragment;
	private CriticalResultsFragment    criticalResultsFragment;
	private CriticalTypesFragment      criticalTypesFragment;
	private CulturesFragment           culturesFragment;
	private DamageResultsFragment      damageResultsFragment;
	private DiseasesFragment           diseasesFragment;
	private ItemTemplatesFragment      itemTemplatesFragment;
	private OutlooksFragment           outlooksFragment;
	private ProfessionsFragment        professionsFragment;
	private RacesFragment              racesFragment;
	private RealmsFragment           realmsFragment;
	private SizesFragment            sizesFragment;
	private SkillCategoriesFragment  skillCategoriesFragment;
	private SkillsFragment           skillsFragment;
	private SpecializationsFragment  specializationsFragment;
	private SpellListsFragment       spellListsFragment;
	private SpellsFragment           spellsFragment;
	private SpellSubTypesFragment    spellSubTypesFragment;
	private SpellTypesFragment       spellTypesFragment;
	private StartEncounterFragment   startEncounterFragment;
	private TalentCategoriesFragment talentCategoriesFragment;
	private TalentsFragment          talentsFragment;
	private WeaponsFragment          weaponsFragment;
	private WeaponTemplatesFragment  weaponTemplatesFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		activityComponent = ((RMUApp) getApplication()).getApplicationComponent()
				.newActivityComponent(new ActivityModule(this));
		activityComponent.injectInto(this);

		setContentView(R.layout.campaign_layout);

		FragmentManager fragmentManager = getFragmentManager();

		//noinspection StatementWithEmptyBody
		if(savedInstanceState != null) {
		}
		else {
			MainMenuFragment mainMenuFragment = new MainMenuFragment();
			aboutFragment = new AboutFragment();
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			fragmentTransaction.replace(R.id.menu_container, mainMenuFragment);
			fragmentTransaction.replace(R.id.details_container, aboutFragment);
			fragmentTransaction.commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_action_bar, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch(id) {
			case R.id.action_export:
				File dir = fileRxHandler.getImportExportDir();
				final File file = new File(dir, EXPORT_FILE_NAME);
				importExportRxHandler.exportDatabase(file)
						.subscribe(new Subscriber<Integer>() {
							Toast toast = null;

							@Override
							public void onStart() {
								request(1);
							}

							@Override
							public void onCompleted() {
								if (toast != null) {
									toast.cancel();
								}
								Toast.makeText(getApplication(), String.format(getString(R.string.toast_db_exported),
																			   file.getAbsolutePath()), Toast.LENGTH_SHORT)
										.show();
							}

							@Override
							public void onError(Throwable e) {
								Log.e("CampaignActivity", "Error occurred exporting database.", e);
								if (toast != null) {
									toast.cancel();
								}
								Toast.makeText(getApplication(), getString(R.string.toast_db_export_failed),
											   Toast.LENGTH_SHORT).show();
							}

							@Override
							public void onNext(Integer percentComplete) {
								if (toast != null) {
									toast.cancel();
								}
								toast = Toast.makeText(getApplication(),
													   String.format(getString(R.string.export_status), percentComplete),
													   Toast.LENGTH_SHORT);
								toast.show();
								request(1);
							}
						});
				return true;
			case R.id.action_import:
				DialogFragment dialogFragment;
				dialogFragment = new FileSelectorDialogFragment();
				Bundle bundle = new Bundle();
				bundle.putString(FILE_SELECTOR_FILTER, RMU_FILE_EXTENSION);
				dialogFragment.setArguments(bundle);
				dialogFragment.show(getFragmentManager(), "FileSelectorDialog");
				return true;
			case R.id.action_export_campaign:
				dialogFragment = new CampaignSelectorDialogFragment();
				dialogFragment.show(getFragmentManager(), "CampaignDialog");
				break;
			case R.id.action_import_campaign:
				dialogFragment = new FileSelectorDialogFragment();
				bundle = new Bundle();
				bundle.putString(FILE_SELECTOR_FILTER, RMU_CAMPAIGN_FILE_EXTENSION);
				dialogFragment.setArguments(bundle);
				dialogFragment.show(getFragmentManager(), "");
				return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onFileSelected(String fileName) {
		if(fileName != null) {
			if(fileName.endsWith(RMU_FILE_EXTENSION)) {
				importExportRxHandler.importDatabase(fileName)
						.subscribe(new Subscriber<Integer>() {
							Toast toast = null;
							@Override
							public void onCompleted() {
								if (toast != null) {
									toast.cancel();
								}
								Toast.makeText(getApplication(), getString(R.string.toast_db_imported), Toast.LENGTH_SHORT)
										.show();
							}
							@Override
							public void onError(Throwable e) {
								Log.e("CampaignActivity", "Error occurred importing database.", e);
								if (toast != null) {
									toast.cancel();
								}
								Toast.makeText(getApplication(), getString(R.string.toast_db_import_failed), Toast.LENGTH_SHORT)
										.show();
							}
							@Override
							public void onNext(Integer percentComplete) {
								if (toast != null) {
									toast.cancel();
								}
								toast = Toast.makeText(getApplication(),
													   String.format(getString(R.string.export_status), percentComplete),
													   Toast.LENGTH_SHORT);
								toast.show();
								request(1);
							}
						});
			}
			else {
				importExportCampaignRxHandler.importDatabase(fileName)
						.subscribe(new Subscriber<Integer>() {
							Toast toast = null;
							@Override
							public void onCompleted() {
								if (toast != null) {
									toast.cancel();
								}
								Toast.makeText(getApplication(), getString(R.string.toast_db_imported), Toast.LENGTH_SHORT)
										.show();
							}
							@Override
							public void onError(Throwable e) {
								Log.e("CampaignActivity", "Error occurred importing database.", e);
								if (toast != null) {
									toast.cancel();
								}
								Toast.makeText(getApplication(), getString(R.string.toast_db_import_failed), Toast.LENGTH_SHORT)
										.show();
							}
							@Override
							public void onNext(Integer percentComplete) {
								if (toast != null) {
									toast.cancel();
								}
								toast = Toast.makeText(getApplication(),
													   String.format(getString(R.string.export_status), percentComplete),
													   Toast.LENGTH_SHORT);
								toast.show();
								request(1);
							}
						});
			}
		}
	}

	@Override
	public void onCampaignSelected(Campaign campaign) {
		File dir = fileRxHandler.getImportExportDir();
		final File exportFile = new File(dir, campaign.getName().replace(" ", "_") + RMU_CAMPAIGN_FILE_EXTENSION);
		importExportCampaignRxHandler.exportDatabase(exportFile, campaign)
				.subscribe(new Subscriber<Integer>() {
					Toast toast = null;
					@Override
					public void onStart() {
						request(1);
					}
					@Override
					public void onCompleted() {
						if (toast != null) {
							toast.cancel();
						}
						Toast.makeText(getApplication(),
									   String.format(getString(R.string.toast_db_exported),
													 exportFile.getAbsolutePath()),
									   Toast.LENGTH_SHORT).show();
					}
					@Override
					public void onError(Throwable e) {
						Log.e("CampaignActivity", "Error occurred exporting database.", e);
						if (toast != null) {
							toast.cancel();
						}
						Toast.makeText(getApplication(), getString(R.string.toast_db_export_failed),
									   Toast.LENGTH_SHORT).show();
					}
					@Override
					public void onNext(Integer percentComplete) {
						if (toast != null) {
							toast.cancel();
						}
						toast = Toast.makeText(getApplication(),
											   String.format(getString(R.string.export_status), percentComplete),
											   Toast.LENGTH_SHORT);
						toast.show();
						request(1);
					}
				});
	}

	public void showAbout() {
		if(aboutFragment == null) {
			aboutFragment = new AboutFragment();
		}
		replaceDetailFragment(aboutFragment);
	}

	public void showAttacks() {
		if(attacksFragment == null) {
			attacksFragment = new AttacksFragment();
		}
		replaceDetailFragment(attacksFragment);
	}

	public void showBodyParts() {
		if(bodyPartsFragment == null) {
			bodyPartsFragment = new BodyPartsFragment();
		}
		replaceDetailFragment(bodyPartsFragment);
	}

	public void showCampaigns() {
		if(campaignsFragment == null) {
			campaignsFragment = new CampaignsFragment();
		}
		replaceDetailFragment(campaignsFragment);
	}

	public void showCharacters() {
		if(charactersFragment == null) {
			charactersFragment = new CharactersFragment();
		}
		replaceDetailFragment(charactersFragment);
	}

	public void showCreatureArchetypes() {
		if(creatureArchetypesFragment == null) {
			creatureArchetypesFragment = new CreatureArchetypesFragment();
		}
		replaceDetailFragment(creatureArchetypesFragment);
	}

	public void showCreatureCategories() {
		if(creatureCategoriesFragment == null) {
			creatureCategoriesFragment = new CreatureCategoriesFragment();
		}
		replaceDetailFragment(creatureCategoriesFragment);
	}

	public void showCreatures() {
		if(creaturesFragment == null) {
			creaturesFragment = new CreaturesFragment();
		}
		replaceDetailFragment(creaturesFragment);
	}

	public void showCreatureTypes() {
		if(creatureTypesFragment == null) {
			creatureTypesFragment = new CreatureTypesFragment();
		}
		replaceDetailFragment(creatureTypesFragment);
	}

	public void showCreatureVarieties() {
		if(creatureVarietiesFragment == null) {
			creatureVarietiesFragment = new CreatureVarietiesFragment();
		}
		replaceDetailFragment(creatureVarietiesFragment);
	}

	public void showCriticalResults() {
		if(criticalResultsFragment == null) {
			criticalResultsFragment = new CriticalResultsFragment();
		}
		replaceDetailFragment(criticalResultsFragment);
	}

	public void showCriticalTypes() {
		if(criticalTypesFragment == null) {
			criticalTypesFragment = new CriticalTypesFragment();
		}
		replaceDetailFragment(criticalTypesFragment);
	}

	public void showCultures() {
		if(culturesFragment == null) {
			culturesFragment = new CulturesFragment();
		}
		replaceDetailFragment(culturesFragment);
	}

	public void showDamageResults() {
		if(damageResultsFragment == null) {
			damageResultsFragment = new DamageResultsFragment();
		}
		replaceDetailFragment(damageResultsFragment);
	}

	public void showDiseases() {
		if(diseasesFragment == null) {
			diseasesFragment = new DiseasesFragment();
		}
		replaceDetailFragment(diseasesFragment);
	}

	public void showItemTemplates() {
		if(itemTemplatesFragment == null) {
			itemTemplatesFragment = new ItemTemplatesFragment();
		}
		replaceDetailFragment(itemTemplatesFragment);
	}

	public void showOutlooks() {
		if(outlooksFragment == null) {
			outlooksFragment = new OutlooksFragment();
		}
		replaceDetailFragment(outlooksFragment);
	}

	public void showProfessions() {
		if(professionsFragment == null) {
			professionsFragment = new ProfessionsFragment();
		}
		replaceDetailFragment(professionsFragment);
	}

	public void showRaces() {
		if(racesFragment == null) {
			racesFragment = new RacesFragment();
		}
		replaceDetailFragment(racesFragment);
	}

	public void showRealms() {
		if(realmsFragment == null) {
			realmsFragment = new RealmsFragment();
		}
		replaceDetailFragment(realmsFragment);
	}

	public void showSizes() {
		if(sizesFragment == null) {
			sizesFragment = new SizesFragment();
		}
		replaceDetailFragment(sizesFragment);
	}

	public void showSkillCategories() {
		if(skillCategoriesFragment == null) {
			skillCategoriesFragment = new SkillCategoriesFragment();
		}
		replaceDetailFragment(skillCategoriesFragment);
	}

	public void showSkills() {
		if(skillsFragment == null) {
			skillsFragment = new SkillsFragment();
		}
		replaceDetailFragment(skillsFragment);
	}

	public void showSpecializations() {
		if(specializationsFragment == null) {
			specializationsFragment = new SpecializationsFragment();
		}
		replaceDetailFragment(specializationsFragment);
	}

	public void showSpellLists() {
		if(spellListsFragment == null) {
			spellListsFragment = new SpellListsFragment();
		}
		replaceDetailFragment(spellListsFragment);
	}

	public void showSpells() {
		if(spellsFragment == null) {
			spellsFragment = new SpellsFragment();
		}
		replaceDetailFragment(spellsFragment);
	}

	public void showSpellSubTypes() {
		if(spellSubTypesFragment == null) {
			spellSubTypesFragment = new SpellSubTypesFragment();
		}
		replaceDetailFragment(spellSubTypesFragment);
	}

	public void showSpellTypes() {
		if(spellTypesFragment == null) {
			spellTypesFragment = new SpellTypesFragment();
		}
		replaceDetailFragment(spellTypesFragment);
	}

	public void showStartCombat() {
		if(startEncounterFragment == null) {
			startEncounterFragment = new StartEncounterFragment();
		}
		replaceDetailFragment(startEncounterFragment);
	}

	public void showTalentCategories() {
		if(talentCategoriesFragment == null) {
			talentCategoriesFragment = new TalentCategoriesFragment();
		}
		replaceDetailFragment(talentCategoriesFragment);
	}

	public void showTalents() {
		if(talentsFragment == null) {
			talentsFragment = new TalentsFragment();
		}
		replaceDetailFragment(talentsFragment);
	}

	public void showCreateWeapons() {
		if(weaponsFragment == null) {
			weaponsFragment = new WeaponsFragment();
		}
		replaceDetailFragment(weaponsFragment);
	}

	public void showWeaponTemplates() {
		if(weaponTemplatesFragment == null) {
			weaponTemplatesFragment = new WeaponTemplatesFragment();
		}
		replaceDetailFragment(weaponTemplatesFragment);
	}

	private void replaceDetailFragment(Fragment fragment) {
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.details_container, fragment);
		fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		fragmentTransaction.commit();
	}

	public ActivityComponent getActivityComponent() {
		return activityComponent;
	}
}
