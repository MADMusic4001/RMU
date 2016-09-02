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
package com.madinnovations.rmu.view.activities.campaign;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.controller.rxhandler.FileRxHandler;
import com.madinnovations.rmu.controller.rxhandler.campaign.ImportExportRxHandler;
import com.madinnovations.rmu.view.RMUApp;
import com.madinnovations.rmu.view.activities.FileSelectorDialogFragment;
import com.madinnovations.rmu.view.activities.character.ProfessionsFragment;
import com.madinnovations.rmu.view.activities.combat.AttacksFragment;
import com.madinnovations.rmu.view.activities.combat.BodyPartsFragment;
import com.madinnovations.rmu.view.activities.combat.CriticalCodesFragment;
import com.madinnovations.rmu.view.activities.combat.CriticalResultsFragment;
import com.madinnovations.rmu.view.activities.combat.CriticalTypesFragment;
import com.madinnovations.rmu.view.activities.combat.DamageResultsFragment;
import com.madinnovations.rmu.view.activities.common.LocomotionTypesFragment;
import com.madinnovations.rmu.view.activities.common.ParametersFragment;
import com.madinnovations.rmu.view.activities.common.SizesFragment;
import com.madinnovations.rmu.view.activities.common.SkillCategoriesFragment;
import com.madinnovations.rmu.view.activities.common.SkillsFragment;
import com.madinnovations.rmu.view.activities.common.SpecializationsFragment;
import com.madinnovations.rmu.view.activities.common.StatsFragment;
import com.madinnovations.rmu.view.activities.common.TalentCategoriesFragment;
import com.madinnovations.rmu.view.activities.common.TalentsFragment;
import com.madinnovations.rmu.view.activities.creature.CreatureArchetypesFragment;
import com.madinnovations.rmu.view.activities.creature.CreatureCategoriesFragment;
import com.madinnovations.rmu.view.activities.creature.CreatureTypesFragment;
import com.madinnovations.rmu.view.activities.creature.CreatureVarietiesFragment;
import com.madinnovations.rmu.view.activities.creature.OutlooksFragment;
import com.madinnovations.rmu.view.activities.item.ItemsFragment;
import com.madinnovations.rmu.view.activities.spell.RealmsFragment;
import com.madinnovations.rmu.view.activities.spell.SpellListTypesFragment;
import com.madinnovations.rmu.view.di.components.ActivityComponent;
import com.madinnovations.rmu.view.di.modules.ActivityModule;

import java.io.File;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Activity class for managing the campaign UI.
 */
public class CampaignActivity extends Activity implements FileSelectorDialogFragment.FileSelectorDialogListener {
	private static final String EXPORT_FILE_NAME = "export.rmu";
	private static final String FILE_SELECTOR_FILTER = "fs_extension_filter";
	private static final String RMU_FILE_EXTENSION = ".rmu";
	@Inject
	FileRxHandler                      fileRxHandler;
	@Inject
	ImportExportRxHandler              importExportRxHandler;
	private ActivityComponent          activityComponent;
	private AboutFragment              aboutFragment;
	private AttacksFragment            attacksFragment;
	private BodyPartsFragment          bodyPartsFragment;
	private CreatureArchetypesFragment creatureArchetypesFragment;
	private CreatureCategoriesFragment creatureCategoriesFragment;
	private CreatureTypesFragment      creatureTypesFragment;
	private CreatureVarietiesFragment  creatureVarietiesFragment;
	private CriticalCodesFragment      criticalCodesFragment;
	private CriticalResultsFragment    criticalResultsFragment;
	private CriticalTypesFragment      criticalTypesFragment;
	private DamageResultsFragment      damageResultsFragment;
	private ItemsFragment              itemsFragment;
	private LocomotionTypesFragment    locomotionTypesFragment;
	private ParametersFragment         parametersFragment;
	private ProfessionsFragment        professionsFragment;
	private OutlooksFragment           outlooksFragment;
	private RealmsFragment             realmsFragment;
	private SizesFragment              sizesFragment;
	private SkillCategoriesFragment    skillCategoriesFragment;
	private SkillsFragment             skillsFragment;
	private SpecializationsFragment    specializationsFragment;
	private SpellListTypesFragment     spellListTypesFragment;
	private StatsFragment              statsFragment;
	private TalentCategoriesFragment   talentCategoriesFragment;
	private TalentsFragment            talentsFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		activityComponent = ((RMUApp) getApplication()).getApplicationComponent()
				.newActivityComponent(new ActivityModule(this));
		activityComponent.injectInto(this);

		setContentView(R.layout.campaign_layout);

		FragmentManager fragmentManager = getFragmentManager();

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
		if(id == R.id.action_export) {
			File dir = fileRxHandler.getImportExportDir();
			final File file = new File(dir, EXPORT_FILE_NAME);
			importExportRxHandler.exportDatabase(file)
					.subscribe(new Subscriber<Integer>() {
						@Override
						public void onCompleted() {}
						@Override
						public void onError(Throwable e) {
							Log.e("CampaignActivity", "Error occurred exporting database.", e);
							Toast.makeText(getApplication(), getString(R.string.toast_db_export_failed),
										   Toast.LENGTH_SHORT).show();
						}
						@Override
						public void onNext(Integer percentComplete) {
							Toast.makeText(getApplication(), String.format(getString(R.string.toast_db_exported),
																		   file.getAbsolutePath()), Toast.LENGTH_SHORT).show();
						}
					});
			return true;
		}
		else if(id == R.id.action_import) {
			DialogFragment dialogFragment;
			dialogFragment = new FileSelectorDialogFragment();
			Bundle bundle = new Bundle();
			bundle.putString(FILE_SELECTOR_FILTER, RMU_FILE_EXTENSION);
			dialogFragment.setArguments(bundle);
			Log.d("RMU", "Preparing to show FileSelectorDialogFragment");
			dialogFragment.show(getFragmentManager(), "");
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onFileSelected(String fileName) {
		if(fileName != null) {
			importExportRxHandler.importDatabase(fileName)
					.subscribe(new Subscriber<Integer>() {
						@Override
						public void onCompleted() {
							Toast.makeText(getApplication(), getString(R.string.toast_db_imported), Toast.LENGTH_SHORT).show();
						}

						@Override
						public void onError(Throwable e) {
							Log.e("CampaignActivity", "Error occurred importing database.", e);
							Toast.makeText(getApplication(), getString(R.string.toast_db_import_failed), Toast.LENGTH_SHORT)
									.show();
						}

						@Override
						public void onNext(Integer percentComplete) {
						}
					});
		}
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

	public void showCriticalCodes() {
		if(criticalCodesFragment == null) {
			criticalCodesFragment = new CriticalCodesFragment();
		}
		replaceDetailFragment(criticalCodesFragment);
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

	public void showDamageResults() {
		if(damageResultsFragment == null) {
			damageResultsFragment = new DamageResultsFragment();
		}
		replaceDetailFragment(damageResultsFragment);
	}

	public void showItems() {
		if(itemsFragment == null) {
			itemsFragment = new ItemsFragment();
		}
		replaceDetailFragment(itemsFragment);
	}

	public void showLocomotionTypes() {
		if(locomotionTypesFragment == null) {
			locomotionTypesFragment = new LocomotionTypesFragment();
		}
		replaceDetailFragment(locomotionTypesFragment);
	}

	public void showOutlooks() {
		if(outlooksFragment == null) {
			outlooksFragment = new OutlooksFragment();
		}
		replaceDetailFragment(outlooksFragment);
	}

	public void showParameters() {
		if(parametersFragment == null) {
			parametersFragment = new ParametersFragment();
		}
		replaceDetailFragment(parametersFragment);
	}

	public void showProfessions() {
		if(professionsFragment == null) {
			professionsFragment = new ProfessionsFragment();
		}
		replaceDetailFragment(professionsFragment);
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

	public void showSpellListTypes() {
		if(spellListTypesFragment == null) {
			spellListTypesFragment = new SpellListTypesFragment();
		}
		replaceDetailFragment(spellListTypesFragment);
	}

	public void showStats() {
		if(statsFragment == null) {
			statsFragment = new StatsFragment();
		}
		replaceDetailFragment(statsFragment);
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

	private void replaceDetailFragment(Fragment fragment) {
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.details_container, fragment);
		fragmentTransaction.commit();
	}

	public ActivityComponent getActivityComponent() {
		return activityComponent;
	}
}
