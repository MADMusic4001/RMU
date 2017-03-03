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
package com.madinnovations.rmu.view.activities.combat;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.IdRes;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.controller.rxhandler.combat.AttackRxHandler;
import com.madinnovations.rmu.controller.rxhandler.combat.DamageTableRxHandler;
import com.madinnovations.rmu.controller.rxhandler.common.SpecializationRxHandler;
import com.madinnovations.rmu.data.entities.combat.Attack;
import com.madinnovations.rmu.data.entities.combat.DamageTable;
import com.madinnovations.rmu.data.entities.common.Specialization;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.adapters.TwoFieldListAdapter;
import com.madinnovations.rmu.view.di.modules.CombatFragmentModule;
import com.madinnovations.rmu.view.utils.EditTextUtils;
import com.madinnovations.rmu.view.utils.SpinnerUtils;

import java.util.Collection;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Handles interactions with the UI for body parts.
 */
public class AttacksFragment extends Fragment implements TwoFieldListAdapter.GetValues<Attack>, EditTextUtils.ValuesCallback,
		SpinnerUtils.ValuesCallback {
	private static final String TAG = "AttacksFragment";
	@Inject
	protected AttackRxHandler              attackRxHandler;
	@Inject
	protected DamageTableRxHandler         damageTableRxHandler;
	@Inject
	protected SpecializationRxHandler      specializationRxHandler;
	private   TwoFieldListAdapter<Attack>  listAdapter;
	private ListView                       listView;
	private EditText                       codeEdit;
	private EditText                       nameEdit;
	private SpinnerUtils<DamageTable>      damageTableSpinner;
	private SpinnerUtils<Specialization>   specializationSpinner;
	private Attack currentInstance = new Attack();
	private boolean isNew = true;
	private Specialization noSpecialization = new Specialization();

	// <editor-fold desc="method overrides/implementations">
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((CampaignActivity)getActivity()).getActivityComponent().
				newCombatFragmentComponent(new CombatFragmentModule(this)).injectInto(this);

		noSpecialization.setName(getString(R.string.no_specialization));
		View layout = inflater.inflate(R.layout.attacks_fragment, container, false);

		((TextView)layout.findViewById(R.id.header_field1)).setText(R.string.label_attack_code);
		((TextView)layout.findViewById(R.id.header_field2)).setText(R.string.label_attack_name);

		codeEdit = EditTextUtils.initEdit(layout, getActivity(), this, R.id.attack_code_edit,
										  R.string.validation_attack_code_required);
		nameEdit = EditTextUtils.initEdit(layout, getActivity(), this, R.id.name_edit, R.string.validation_attack_name_required);
		damageTableSpinner = new SpinnerUtils<>();
		damageTableSpinner.initSpinner(layout, getActivity(), damageTableRxHandler.getAll(), this, R.id.damage_table_spinner,
									   null);
		specializationSpinner = new SpinnerUtils<>();
		specializationSpinner.initSpinner(layout, getActivity(), specializationRxHandler.getAll(), this,
										  R.id.attack_specialization_spinner, noSpecialization);
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
		inflater.inflate(R.menu.attacks_action_bar, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if(id == R.id.action_new_attack) {
			if(copyViewsToItem()) {
				saveItem();
			}
			currentInstance = new Attack();
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
		getActivity().getMenuInflater().inflate(R.menu.attack_context_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		final Attack attack;

		AdapterView.AdapterContextMenuInfo info =
				(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

		switch (item.getItemId()) {
			case R.id.context_new_attack:
				if(copyViewsToItem()) {
					saveItem();
				}
				currentInstance = new Attack();
				isNew = true;
				copyItemToViews();
				listView.clearChoices();
				listAdapter.notifyDataSetChanged();
				return true;
			case R.id.context_delete_attack:
				attack = (Attack)listView.getItemAtPosition(info.position);
				if(attack != null) {
					deleteItem(attack);
					return true;
				}
				break;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	public CharSequence getField1Value(Attack attack) {
		return attack.getCode();
	}

	@Override
	public CharSequence getField2Value(Attack attack) {
		return attack.getName();
	}

	@Override
	public String getValueForEditText(@IdRes int editTextId) {
		String result = null;

		switch (editTextId) {
			case R.id.attack_code_edit:
				result = currentInstance.getCode();
				break;
			case R.id.name_edit:
				result = currentInstance.getName();
				break;
		}

		return result;
	}

	@Override
	public void setValueFromEditText(@IdRes int editTextId, String newString) {
		switch (editTextId) {
			case R.id.attack_code_edit:
				currentInstance.setCode(newString);
				break;
			case R.id.name_edit:
				currentInstance.setName(newString);
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
			case R.id.damage_table_spinner:
				result = currentInstance.getDamageTable();
				break;
			case R.id.attack_specialization_spinner:
				result = currentInstance.getSpecialization();
				break;
		}

		return result;
	}

	@Override
	public void setValueFromSpinner(@IdRes int spinnerId, Object newItem) {
		switch (spinnerId) {
			case R.id.damage_table_spinner:
				currentInstance.setDamageTable((DamageTable)newItem);
				saveItem();
				break;
			case R.id.attack_specialization_spinner:
				currentInstance.setSpecialization((Specialization)newItem);
				break;
		}
	}

	@Override
	public void observerCompleted(@IdRes int spinnerId) {}
	// </editor-fold>

	// <editor-fold desc="Copy to/from views/entity methods">
	private boolean copyViewsToItem() {
		boolean changed = false;
		String value;

		View currentFocusView = getActivity().getCurrentFocus();
		if(currentFocusView != null) {
			currentFocusView.clearFocus();
		}

		if(codeEdit != null) {
			value = codeEdit.getText().toString();
			if (value.isEmpty()) {
				value = null;
			}
			if ((value == null && currentInstance.getCode() != null) ||
					(value != null && !value.equals(currentInstance.getCode()))) {
				currentInstance.setCode(value);
				changed = true;
			}
		}

		if(nameEdit != null) {
			value = nameEdit.getText().toString();
			if (value.isEmpty()) {
				value = null;
			}
			if ((value == null && currentInstance.getName() != null) ||
					(value != null && !value.equals(currentInstance.getName()))) {
				currentInstance.setName(value);
				changed = true;
			}
		}

		DamageTable newDamageTable = damageTableSpinner.getSelectedItem();
		DamageTable oldDamageTable = currentInstance.getDamageTable();
		if((newDamageTable != null && !newDamageTable.equals(oldDamageTable)) ||
				(oldDamageTable != null && !oldDamageTable.equals(newDamageTable))) {
			currentInstance.setDamageTable(newDamageTable);
			changed = true;
		}

		Specialization newSpecialization = specializationSpinner.getSelectedItem();
		if(noSpecialization.equals(newSpecialization)) {
			newSpecialization = null;
		}
		Specialization oldSpecialization = currentInstance.getSpecialization();
		if((newSpecialization != null && !newSpecialization.equals(oldSpecialization)) ||
				(oldSpecialization != null && !oldSpecialization.equals(newSpecialization))) {
			currentInstance.setSpecialization(newSpecialization);
			changed = true;
		}

		return changed;
	}

	private void copyItemToViews() {
		codeEdit.setText(currentInstance.getCode());
		nameEdit.setText(currentInstance.getName());
		damageTableSpinner.setSelection(currentInstance.getDamageTable());
		if(currentInstance.getSpecialization() == null) {
			specializationSpinner.setSelection(noSpecialization);
		}
		else {
			specializationSpinner.setSelection(currentInstance.getSpecialization());
		}

		if(currentInstance.getCode() != null && !currentInstance.getCode().isEmpty()) {
			codeEdit.setError(null);
		}
		if(currentInstance.getName() != null && !currentInstance.getName().isEmpty()) {
			nameEdit.setError(null);
		}
	}
	// </editor-fold>

	// <editor-fold desc="Save/delete entity methods">
	private void deleteItem(final Attack item) {
		attackRxHandler.deleteById(item.getId())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribeOn(Schedulers.io())
			.subscribe(new Subscriber<Boolean>() {
				@Override
				public void onCompleted() {}
				@Override
				public void onError(Throwable e) {
					Log.e(TAG, "Exception when deleting: " + item, e);
					Toast.makeText(getActivity(), R.string.toast_attack_delete_failed, Toast.LENGTH_SHORT).show();
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
							currentInstance = new Attack();
							isNew = true;
						}
						copyItemToViews();
						Toast.makeText(getActivity(), R.string.toast_attack_deleted, Toast.LENGTH_SHORT).show();
					}
				}
			});
	}

	private void saveItem() {
		if(currentInstance.isValid()) {
			final boolean wasNew = isNew;
			isNew = false;
			attackRxHandler.save(currentInstance)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new Subscriber<Attack>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e(TAG, "Exception saving Attack", e);
						Toast.makeText(getActivity(), R.string.toast_attack_save_failed, Toast.LENGTH_SHORT).show();
					}
					@Override
					public void onNext(Attack savedAttack) {
						if (wasNew) {
							listAdapter.add(savedAttack);
							if(savedAttack == currentInstance) {
								listView.setSelection(listAdapter.getPosition(savedAttack));
								listView.setItemChecked(listAdapter.getPosition(savedAttack), true);
							}
							listAdapter.notifyDataSetChanged();
						}
						if (getActivity() != null) {
							String toastString;
							Toast.makeText(getActivity(), R.string.toast_attack_saved, Toast.LENGTH_SHORT).show();

							int position = listAdapter.getPosition(currentInstance);
							LinearLayout v = (LinearLayout) listView.getChildAt(position - listView.getFirstVisiblePosition());
							if (v != null) {
								TextView textView = (TextView) v.findViewById(R.id.row_field1);
								textView.setText(currentInstance.getCode());
								textView = (TextView) v.findViewById(R.id.row_field2);
								textView.setText(currentInstance.getName());
							}
						}
					}
				});
		}
	}
	// </editor-fold>

	// <editor-fold desc="Widget initialization methods">
	private void initListView(View layout) {
		listView = (ListView) layout.findViewById(R.id.list_view);
		listAdapter = new TwoFieldListAdapter<>(this.getActivity(), 1, 5, this);
		listView.setAdapter(listAdapter);

		attackRxHandler.getAll()
			.observeOn(AndroidSchedulers.mainThread())
			.subscribeOn(Schedulers.io())
			.subscribe(new Subscriber<Collection<Attack>>() {
				@Override
				public void onCompleted() {}
				@Override
				public void onError(Throwable e) {
					Log.e(TAG, "Exception caught getting all Attack instances", e);
					Toast.makeText(AttacksFragment.this.getActivity(),
							R.string.toast_attacks_load_failed,
							Toast.LENGTH_SHORT).show();
				}
				@Override
				public void onNext(Collection<Attack> attacks) {
					listAdapter.clear();
					listAdapter.addAll(attacks);
					listAdapter.notifyDataSetChanged();
					if(attacks.size() > 0) {
						listView.setSelection(0);
						listView.setItemChecked(0, true);
						currentInstance = listAdapter.getItem(0);
						isNew = false;
						copyItemToViews();
					}
					Toast.makeText(AttacksFragment.this.getActivity(),
								   String.format(getString(R.string.toast_attacks_loaded), attacks.size()),
								   Toast.LENGTH_SHORT).show();
				}
			});

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(copyViewsToItem()) {
					saveItem();
				}
				currentInstance = (Attack) listView.getItemAtPosition(position);
				isNew = false;
				if (currentInstance == null) {
					currentInstance = new Attack();
					isNew = true;
				}
				copyItemToViews();
			}
		});
		registerForContextMenu(listView);
	}
	// </editor-fold>
}
