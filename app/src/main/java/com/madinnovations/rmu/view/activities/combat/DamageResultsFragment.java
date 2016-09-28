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

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.controller.rxhandler.combat.DamageResultRowRxHandler;
import com.madinnovations.rmu.controller.rxhandler.combat.DamageTableRxHandler;
import com.madinnovations.rmu.data.entities.combat.DamageResultRow;
import com.madinnovations.rmu.data.entities.combat.DamageTable;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.adapters.combat.DamageResultsGridAdapter;
import com.madinnovations.rmu.view.di.modules.CombatFragmentModule;
import com.madinnovations.rmu.view.utils.CheckBoxUtils;
import com.madinnovations.rmu.view.utils.EditTextUtils;

import java.util.Collection;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Handles interactions with the UI for damage results.
 */
public class DamageResultsFragment extends Fragment implements EditTextUtils.ValuesCallback, CheckBoxUtils.ValuesCallback {
	private static final String LOG_TAG = "DamageResultsFragment";
	@Inject
	protected DamageResultRowRxHandler  damageResultRowRxHandler;
	@Inject
	protected DamageTableRxHandler      damageTableRxHandler;
	@Inject
	protected DamageResultsGridAdapter  damageResultsGridAdapter;
	private   ArrayAdapter<DamageTable> damageTableFilterSpinnerAdapter;
	private   Spinner                   damageTableFilterSpinner;
	private   EditText                  damageTableNameEdit;
	private   CheckBox                  ballTableCheckbox;
	private DamageTable currentInstance = new DamageTable();
	private boolean isNew = true;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((CampaignActivity)getActivity()).getActivityComponent().
				newCombatFragmentComponent(new CombatFragmentModule(this)).injectInto(this);

		View layout = inflater.inflate(R.layout.damage_results_fragment, container, false);

		damageTableNameEdit = EditTextUtils.initEdit(layout, getActivity(), this, R.id.name_edit,
													 R.string.validation_damage_table_name_required);
		ballTableCheckbox = CheckBoxUtils.initCheckBox(layout, this, R.id.ball_table_check_box);
		initDamageTableFilterSpinner(layout);
		initDeleteTableButton(layout);
		initDamageResultsGridView(layout);

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
		inflater.inflate(R.menu.damage_results_action_bar, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if(id == R.id.action_new_damage_table) {
			if(copyViewsToItem()) {
				saveItem();
			}
			currentInstance = new DamageTable();
			currentInstance.initRows();
			isNew = true;
			copyItemToViews();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean getValueForCheckBox(@IdRes int checkBoxId) {
		boolean result = false;

		switch (checkBoxId) {
			case R.id.ball_table_check_box:
				result = currentInstance.isBallTable();
		}

		return result;
	}

	@Override
	public void setValueFromCheckBox(@IdRes int checkBoxId, boolean newBoolean) {
		switch (checkBoxId) {
			case R.id.ball_table_check_box:
				currentInstance.setBallTable(newBoolean);
				saveItem();
				copyItemToViews();
				break;
		}
	}

	@Override
	public String getValueForEditText(@IdRes int editTextId) {
		String result = null;

		switch (editTextId) {
			case R.id.name_edit:
				result = currentInstance.getName();
				break;
		}

		return result;
	}

	@Override
	public void setValueFromEditText(@IdRes int editTextId, String newString) {
		switch (editTextId) {
			case R.id.name_edit:
				currentInstance.setName(newString);
				saveItem();
				break;
		}
	}

	private boolean copyViewsToItem() {
		currentInstance.setName(damageTableNameEdit.getText().toString());
		currentInstance.setBallTable(ballTableCheckbox.isChecked());
		return false;
	}

	private void copyItemToViews() {
		damageTableNameEdit.setText(currentInstance.getName());
		ballTableCheckbox.setChecked(currentInstance.isBallTable());
		damageResultsGridAdapter.clear();
		damageResultsGridAdapter.addAll(currentInstance.getResultRows());
		damageResultsGridAdapter.notifyDataSetChanged();
		loadFilteredDamageResultRows(currentInstance);
	}

	private void saveItem() {
		if(currentInstance.isValid()) {
			final boolean wasNew = isNew;
			isNew = false;
			damageTableRxHandler.save(currentInstance)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new Subscriber<DamageTable>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e(LOG_TAG, "Exception saving DamageTable", e);
					}
					@Override
					public void onNext(DamageTable savedDamageTable) {
						if (wasNew) {
							for(DamageResultRow row : currentInstance.getResultRows()) {
								row.setDamageTable(savedDamageTable);
								damageResultRowRxHandler.save(row)
										.observeOn(AndroidSchedulers.mainThread())
										.subscribeOn(Schedulers.io())
										.subscribe();
							}
							damageTableFilterSpinnerAdapter.add(savedDamageTable);
							damageTableFilterSpinner.setSelection(damageTableFilterSpinnerAdapter.getPosition(savedDamageTable));
							damageTableFilterSpinnerAdapter.notifyDataSetChanged();
						}
						if(getActivity() != null) {
							String toastString;
							toastString = getString(R.string.toast_damage_table_saved);
							Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
						}
					}
				});
		}
	}

	private void deleteItem(@NonNull final DamageTable damageTable) {
		damageResultRowRxHandler.deleteAllForDamageTable(damageTable)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new Subscriber<Collection<DamageResultRow>>() {
					@Override
					public void onCompleted() {
						damageTableFilterSpinnerAdapter.remove(damageTable);
						int position = damageTableFilterSpinner.getSelectedItemPosition();
						if(position != -1) {
							currentInstance = damageTableFilterSpinnerAdapter.getItem(position);
						}
						else {
							currentInstance = new DamageTable();
							currentInstance.initRows();
							isNew = true;
						}
						copyItemToViews();
						Toast.makeText(getActivity(), getString(R.string.toast_damage_table_deleted), Toast.LENGTH_SHORT).show();
					}
					@Override
					public void onError(Throwable e) {
						Log.e(LOG_TAG, "Exception when deleting: " + damageTable, e);
						String toastString = getString(R.string.toast_damage_table_delete_failed);
						Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
					}
					@Override
					public void onNext(Collection<DamageResultRow> damageResultRow) {}
				});
	}

	private void initDamageTableFilterSpinner(View layout) {
		damageTableFilterSpinner = (Spinner)layout.findViewById(R.id.damage_tables_spinner);
		damageTableFilterSpinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_row);
		damageTableFilterSpinner.setAdapter(damageTableFilterSpinnerAdapter);
		registerForContextMenu(damageTableFilterSpinner);

		damageTableRxHandler.getAll()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new Subscriber<Collection<DamageTable>>() {
					@Override
					public void onCompleted() {
						int position = damageTableFilterSpinner.getSelectedItemPosition();
						if(position >= 0) {
							DamageTable newDamageTable = damageTableFilterSpinnerAdapter.getItem(position);
							if(newDamageTable != null) {
								currentInstance = newDamageTable;
								damageTableNameEdit.setText(currentInstance.getName());
								loadFilteredDamageResultRows(currentInstance);
							}
							else {
								currentInstance.initRows();
							}
						}
						else {
							currentInstance.initRows();
						}
						damageResultsGridAdapter.clear();
						damageResultsGridAdapter.addAll(currentInstance.getResultRows());
						damageResultsGridAdapter.notifyDataSetChanged();
					}
					@Override
					public void onError(Throwable e) {
						Log.e(LOG_TAG, "Exception caught getting all DamageResult instances", e);
						Toast.makeText(DamageResultsFragment.this.getActivity(),
								getString(R.string.toast_damage_tables_load_failed),
								Toast.LENGTH_SHORT).show();
					}
					@Override
					public void onNext(Collection<DamageTable> damageTables) {
						damageTableFilterSpinnerAdapter.clear();
						damageTableFilterSpinnerAdapter.addAll(damageTables);
						damageTableFilterSpinnerAdapter.notifyDataSetChanged();
					}
				});
		damageTableFilterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
				if(copyViewsToItem()) {
					saveItem();
				}
				DamageTable newDamageTable = damageTableFilterSpinnerAdapter.getItem(position);
				if(newDamageTable != null) {
					currentInstance = newDamageTable;
					damageTableNameEdit.setText(currentInstance.getName());
					loadFilteredDamageResultRows(currentInstance);
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {
			}
		});
	}

	private void initDeleteTableButton(View layout) {
		ImageButton deleteTableButton = (ImageButton)layout.findViewById(R.id.delete_table_button);

		deleteTableButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog deleteDialog = ConfirmDelete();
				deleteDialog.show();
			}
		});
	}

	private void initDamageResultsGridView(View layout) {
		GridView damageResultsGridView = (GridView) layout.findViewById(R.id.damage_results_grid);
		damageResultsGridView.setAdapter(damageResultsGridAdapter);

		damageResultsGridView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
				DamageResultRow newDamageResultRow = damageResultsGridAdapter.getItem(position);
				if(newDamageResultRow != null) {
					loadFilteredDamageResultRows(newDamageResultRow.getDamageTable());
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
	}

	private void loadFilteredDamageResultRows(final DamageTable filter) {
		damageResultRowRxHandler.getDamageResultRowsForDamageTable(filter)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Collection<DamageResultRow>>() {
					@Override
					public void onCompleted() {
						if(currentInstance.getResultRows() == null) {
							currentInstance.initRows();
						}
						copyItemToViews();
					}
					@Override
					public void onError(Throwable e) {
						Log.e(LOG_TAG, "Exception caught getting all DamageResultRow instances", e);
						Toast.makeText(DamageResultsFragment.this.getActivity(),
								getString(R.string.toast_damage_result_rows_load_failed),
								Toast.LENGTH_SHORT).show();
					}
					@Override
					public void onNext(Collection<DamageResultRow> damageResultRows) {
						currentInstance.setResultRows(damageResultRows);
						String toastString;
						toastString = String.format(getString(R.string.toast_damage_result_rows_loaded), damageResultRows.size());
						Toast.makeText(DamageResultsFragment.this.getActivity(), toastString, Toast.LENGTH_SHORT).show();
					}
				});
	}

	private AlertDialog ConfirmDelete() {
		return new AlertDialog.Builder(getActivity())
				//set message, title, and icon
				.setTitle(getString(R.string.confirm_delete_title))
				.setMessage(getString(R.string.confirm_delete_message))
				.setIcon(R.drawable.file_icon)
				.setPositiveButton(getString(R.string.label_delete), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						int position = damageTableFilterSpinner.getSelectedItemPosition();
						DamageTable damageTable = damageTableFilterSpinnerAdapter.getItem(position);
						if(damageTable != null) {
							deleteItem(damageTable);
						}
						dialog.dismiss();
					}
				})
				.setNegativeButton(getString(R.string.label_cancel), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				})
				.create();
	}
}
