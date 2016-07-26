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
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.view.RMUApp;
import com.madinnovations.rmu.view.activities.common.CommonDataFragment;
import com.madinnovations.rmu.view.di.components.ActivityComponent;
import com.madinnovations.rmu.view.di.modules.ActivityModule;

/**
 * Activity class for managing the campaign UI.
 */
public class CampaignActivity extends Activity {
	private ActivityComponent activityComponent;
	private MainMenuFragment mainMenuFragment;
	private AboutFragment aboutFragment;
	private CommonDataFragment commonDataFragment;

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
			mainMenuFragment = new MainMenuFragment();
			aboutFragment = new AboutFragment();
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			fragmentTransaction.replace(R.id.menu_container, mainMenuFragment);
			fragmentTransaction.replace(R.id.details_container, aboutFragment);
			fragmentTransaction.commit();
		}
	}

	public void showCommonData() {
		Log.e("CampaignActivity", "Showing common data fragment");
		if(commonDataFragment == null) {
			commonDataFragment = new CommonDataFragment();
		}
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.details_container, commonDataFragment);
		fragmentTransaction.commit();
		Log.e("CampaignActivity", "Common data fragment shown");
	}

	public void showAbout() {
		Log.e("CampaignActivity", "Showing about fragment");
		if(aboutFragment == null) {
			aboutFragment = new AboutFragment();
		}
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.details_container, aboutFragment);
		fragmentTransaction.commit();
		Log.e("CampaignActivity", "About fragment shown");
	}

	public ActivityComponent getActivityComponent() {
		return activityComponent;
	}
}
