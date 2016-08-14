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
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.view.RMUApp;
import com.madinnovations.rmu.view.activities.combat.BodyPartsFragment;
import com.madinnovations.rmu.view.activities.combat.CriticalCodesFragment;
import com.madinnovations.rmu.view.activities.combat.CriticalResultsFragment;
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
import com.madinnovations.rmu.view.di.components.ActivityComponent;
import com.madinnovations.rmu.view.di.modules.ActivityModule;

/**
 * Activity class for managing the campaign UI.
 */
public class CampaignActivity extends Activity {
	private ActivityComponent          activityComponent;
	private AboutFragment              aboutFragment;
	private BodyPartsFragment          bodyPartsFragment;
	private CreatureArchetypesFragment creatureArchetypesFragment;
	private CreatureCategoriesFragment creatureCategoriesFragment;
	private CreatureTypesFragment      creatureTypesFragment;
	private CreatureVarietiesFragment  creatureVarietiesFragment;
	private CriticalCodesFragment      criticalCodesFragment;
	private CriticalResultsFragment    criticalResultsFragment;
	private ItemsFragment              itemsFragment;
	private LocomotionTypesFragment    locomotionTypesFragment;
	private ParametersFragment         parametersFragment;
	private OutlooksFragment           outlooksFragment;
	private SizesFragment              sizesFragment;
	private SkillCategoriesFragment    skillCategoriesFragment;
	private SkillsFragment             skillsFragment;
	private SpecializationsFragment    specializationsFragment;
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

	public void showAbout() {
		if(aboutFragment == null) {
			aboutFragment = new AboutFragment();
		}
		replaceDetailFragment(aboutFragment);
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
