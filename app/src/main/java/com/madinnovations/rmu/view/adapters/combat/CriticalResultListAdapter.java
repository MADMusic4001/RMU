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
package com.madinnovations.rmu.view.adapters.combat;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.controller.rxhandler.combat.BodyPartRxHandler;
import com.madinnovations.rmu.controller.rxhandler.combat.CriticalResultRxHandler;
import com.madinnovations.rmu.data.entities.combat.BodyPart;
import com.madinnovations.rmu.data.entities.combat.CriticalResult;
import com.madinnovations.rmu.view.utils.CheckBoxUtils;
import com.madinnovations.rmu.view.utils.EditTextUtils;

import java.util.Collection;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Populates a ListView with {@link CriticalResult} information
 */
public class CriticalResultListAdapter extends ArrayAdapter<CriticalResult> {
	private static final String LOG_TAG = "CriticalResultListAdapt";
	private static final int LAYOUT_RESOURCE_ID = R.layout.list_critical_result_row;
	@Inject
	BodyPartRxHandler              bodyPartRxHandler;
	@Inject
	CriticalResultRxHandler        criticalResultRxHandler;
	private ArrayAdapter<BodyPart> bodyPartSpinnerAdapter;
	private Collection<BodyPart>   bodyParts = null;
	private LayoutInflater         layoutInflater;

	/**
	 * Creates a new CriticalResultListAdapter instance.
	 *
	 * @param context the view {@link Context} the adapter will be attached to.
	 */
	@Inject
	CriticalResultListAdapter(Context context) {
		super(context, LAYOUT_RESOURCE_ID);
		this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@NonNull
	@Override
	public View getView(int position, View convertView, @NonNull ViewGroup parent) {
		View rowView;
		ViewHolder holder;

		if (convertView == null) {
			rowView = layoutInflater.inflate(LAYOUT_RESOURCE_ID, parent, false);
			holder = new ViewHolder((EditText) rowView.findViewById(R.id.left_roll_view),
									(Spinner) rowView.findViewById(R.id.body_part_spinner),
									(EditText) rowView.findViewById(R.id.hits_edit),
									(EditText) rowView.findViewById(R.id.bleeding_edit),
									(EditText) rowView.findViewById(R.id.fatigue_edit),
									(EditText) rowView.findViewById(R.id.breakage_edit),
									(EditText) rowView.findViewById(R.id.injury_edit),
									(EditText) rowView.findViewById(R.id.dazed_edit),
									(EditText) rowView.findViewById(R.id.stunned_edit),
									(EditText) rowView.findViewById(R.id.stunned_no_parry_edit),
									(CheckBox) rowView.findViewById(R.id.staggered_checkbox),
									(EditText) rowView.findViewById(R.id.knock_back_edit),
									(CheckBox) rowView.findViewById(R.id.prone_checkbox),
									(EditText) rowView.findViewById(R.id.grappled_edit),
									(EditText) rowView.findViewById(R.id.result_text_edit),
									(EditText) rowView.findViewById(R.id.right_roll_view));
			rowView.setTag(holder);
		}
		else {
			rowView = convertView;
			holder = (ViewHolder) convertView.getTag();
		}

		CriticalResult criticalResult = getItem(position);
		holder.currentInstance = criticalResult;

		String rollString;
		if(criticalResult != null) {
			if (criticalResult.getMinRoll() == criticalResult.getMaxRoll()) {
				rollString = String.valueOf(criticalResult.getMinRoll());
			} else {
				rollString = String.format(getContext().getString(R.string.min_max_roll_value), criticalResult.getMinRoll(),
						criticalResult.getMaxRoll());
			}
			holder.leftRollView.setText(rollString);
			holder.bodyPartSpinner.setSelection(bodyPartSpinnerAdapter.getPosition(criticalResult.getBodyPart()));
			holder.hitsEdit.setText(String.valueOf(criticalResult.getHits()));
			holder.bleedingEdit.setText(String.valueOf(criticalResult.getBleeding()));
			holder.fatigueEdit.setText(String.valueOf(criticalResult.getFatigue()));
			if(criticalResult.getBreakage() == null) {
				holder.breakageEdit.setText(null);
			}
			else {
				holder.breakageEdit.setText(String.valueOf(criticalResult.getBreakage()));
			}
			holder.injuryEdit.setText(String.valueOf(criticalResult.getInjury()));
			holder.dazedEdit.setText(String.valueOf(criticalResult.getDazed()));
			holder.stunnedEdit.setText(String.valueOf(criticalResult.getStunned()));
			holder.noParryEdit.setText(String.valueOf(criticalResult.getNoParry()));
			holder.staggeredCheckBox.setChecked(criticalResult.isStaggered());
			holder.knockBackEdit.setText(String.valueOf(criticalResult.getKnockBack()));
			holder.proneCheckBox.setChecked(criticalResult.isProne());
			holder.grappledEdit.setText(String.valueOf(criticalResult.getGrappled()));
			if(criticalResult.getResultText() == null || criticalResult.getResultText().isEmpty()) {
				holder.resultTextEdit.setText(null);
				holder.resultTextEdit.setError(getContext().getString(R.string.validation_critical_result_text_required));
			}
			else {
				holder.resultTextEdit.setText(criticalResult.getResultText());
				holder.resultTextEdit.setError(null);
			}
			holder.rightRollView.setText(rollString);
		}
		return rowView;
	}

	private class ViewHolder implements EditTextUtils.ValuesCallback, CheckBoxUtils.ValuesCallback {
		private CriticalResult currentInstance;
		private EditText leftRollView;
		private Spinner  bodyPartSpinner;
		private EditText hitsEdit;
		private EditText bleedingEdit;
		private EditText fatigueEdit;
		private EditText breakageEdit;
		private EditText injuryEdit;
		private EditText dazedEdit;
		private EditText stunnedEdit;
		private EditText noParryEdit;
		private CheckBox staggeredCheckBox;
		private EditText knockBackEdit;
		private CheckBox proneCheckBox;
		private EditText grappledEdit;
		private EditText resultTextEdit;
		private EditText rightRollView;

		public ViewHolder(EditText leftRollView, Spinner bodyPartSpinner, EditText hitsEdit, EditText bleedingEdit, EditText fatigueEdit,
						  EditText breakageEdit, EditText injuryEdit, EditText dazedEdit, EditText stunnedEdit, EditText noParryEdit,
						  CheckBox staggeredCheckBox, EditText knockBackEdit, CheckBox proneCheckBox, EditText grappledEdit,
						  EditText resultTextEdit, EditText rightRollView) {
			this.leftRollView = leftRollView;
			this.bodyPartSpinner = bodyPartSpinner;
			initBodyPartSpinner();
			this.hitsEdit = hitsEdit;
			EditTextUtils.initEdit(hitsEdit, getContext(), this, R.id.hits_edit, R.string.validation_hits_required);
			this.bleedingEdit = bleedingEdit;
			EditTextUtils.initEdit(bleedingEdit, getContext(), this, R.id.bleeding_edit, R.string.validation_bleeding_required);
			this.fatigueEdit = fatigueEdit;
			EditTextUtils.initEdit(fatigueEdit, getContext(), this, R.id.fatigue_edit, R.string.validation_fatigue_required);
			this.breakageEdit = breakageEdit;
			EditTextUtils.initEdit(breakageEdit, getContext(), this, R.id.breakage_edit, 0);
			this.injuryEdit = injuryEdit;
			EditTextUtils.initEdit(injuryEdit, getContext(), this, R.id.injury_edit, R.string.validation_injury_required);
			this.dazedEdit = dazedEdit;
			EditTextUtils.initEdit(dazedEdit, getContext(), this, R.id.dazed_edit, R.string.validation_dazed_required);
			this.stunnedEdit = stunnedEdit;
			EditTextUtils.initEdit(stunnedEdit, getContext(), this, R.id.stunned_edit, R.string.validation_stunned_required);
			this.noParryEdit = noParryEdit;
			EditTextUtils.initEdit(noParryEdit, getContext(), this, R.id.stunned_no_parry_edit,
								   R.string.validation_no_parry_required);
			this.staggeredCheckBox = staggeredCheckBox;
			CheckBoxUtils.initCheckBox(staggeredCheckBox, this, R.id.staggered_checkbox);
			this.knockBackEdit = knockBackEdit;
			EditTextUtils.initEdit(knockBackEdit, getContext(), this, R.id.knock_back_edit,
								   R.string.validation_knock_back_required);
			this.proneCheckBox = proneCheckBox;
			CheckBoxUtils.initCheckBox(proneCheckBox, this, R.id.prone_checkbox);
			this.grappledEdit = grappledEdit;
			EditTextUtils.initEdit(grappledEdit, getContext(), this, R.id.grappled_edit, R.string.validation_grappled_required);
			this.resultTextEdit = resultTextEdit;
			EditTextUtils.initEdit(resultTextEdit, getContext(), this, R.id.result_text_edit,
								   R.string.validation_critical_result_text_required);
			this.rightRollView = rightRollView;
		}

		@Override
		public boolean getValueForCheckBox(@IdRes int checkBoxId) {
			boolean result = false;

			switch (checkBoxId) {
				case R.id.staggered_checkbox:
					result = currentInstance.isStaggered();
					break;
				case R.id.prone_checkbox:
					result = currentInstance.isProne();
					break;
			}

			return result;
		}

		@Override
		public void setValueFromCheckBox(@IdRes int checkBoxId, boolean newBoolean) {
			switch (checkBoxId) {
				case R.id.staggered_checkbox:
					currentInstance.setStaggered(newBoolean);
					saveItem();
					break;
				case R.id.prone_checkbox:
					currentInstance.setProne(newBoolean);
					saveItem();
					break;
			}
		}

		@Override
		public String getValueForEditText(@IdRes int editTextId) {
			String result = null;

			switch (editTextId) {
				case R.id.hits_edit:
					result = String.valueOf(currentInstance.getHits());
					break;
				case R.id.bleeding_edit:
					result = String.valueOf(currentInstance.getBleeding());
					break;
				case R.id.fatigue_edit:
					result = String.valueOf(currentInstance.getFatigue());
					break;
				case R.id.breakage_edit:
					if(currentInstance.getBreakage() != null) {
						result = String.valueOf(currentInstance.getBreakage());
					}
					break;
				case R.id.injury_edit:
					result = String.valueOf(currentInstance.getInjury());
					break;
				case R.id.dazed_edit:
					result = String.valueOf(currentInstance.getDazed());
					break;
				case R.id.stunned_edit:
					result = String.valueOf(currentInstance.getStunned());
					break;
				case R.id.stunned_no_parry_edit:
					result = String.valueOf(currentInstance.getNoParry());
					break;
				case R.id.knock_back_edit:
					result = String.valueOf(currentInstance.getKnockBack());
					break;
				case R.id.grappled_edit:
					result = String.valueOf(currentInstance.getGrappled());
					break;
				case R.id.result_text_edit:
					result = currentInstance.getResultText();
					break;
			}

			return result;
		}

		@Override
		public void setValueFromEditText(@IdRes int editTextId, String newString) {
			switch (editTextId) {
				case R.id.hits_edit:
					currentInstance.setHits(Short.valueOf(newString));
					saveItem();
					break;
				case R.id.bleeding_edit:
					currentInstance.setBleeding(Short.valueOf(newString));
					saveItem();
					break;
				case R.id.fatigue_edit:
					currentInstance.setFatigue(Short.valueOf(newString));
					saveItem();
					break;
				case R.id.breakage_edit:
					if(newString != null && newString.length() > 0) {
						currentInstance.setBreakage(Short.valueOf(newString));
						saveItem();
					}
					else {
						currentInstance.setBreakage(null);
						saveItem();
					}
					break;
				case R.id.injury_edit:
					currentInstance.setInjury(Short.valueOf(newString));
					saveItem();
					break;
				case R.id.dazed_edit:
					currentInstance.setDazed(Short.valueOf(newString));
					saveItem();
					break;
				case R.id.stunned_edit:
					currentInstance.setStunned(Short.valueOf(newString));
					saveItem();
					break;
				case R.id.stunned_no_parry_edit:
					currentInstance.setNoParry(Short.valueOf(newString));
					saveItem();
					break;
				case R.id.knock_back_edit:
					currentInstance.setKnockBack(Short.valueOf(newString));
					saveItem();
					break;
				case R.id.grappled_edit:
					currentInstance.setGrappled(Short.valueOf(newString));
					saveItem();
					break;
				case R.id.result_text_edit:
					currentInstance.setResultText(newString);
					saveItem();
					break;
			}
		}

		private void saveItem() {
			if(currentInstance.isValid()) {
				criticalResultRxHandler.save(currentInstance)
						.observeOn(AndroidSchedulers.mainThread())
						.subscribeOn(Schedulers.io())
						.subscribe(new Subscriber<CriticalResult>() {
							@Override
							public void onCompleted() {}
							@Override
							public void onError(Throwable e) {
								Log.e(LOG_TAG, "Exception saving new CriticalResult", e);
							}
							@Override
							public void onNext(CriticalResult savedCriticalResult) {
								String toastString;
								toastString = getContext().getString(R.string.toast_critical_result_saved);
								Toast.makeText(getContext(), toastString, Toast.LENGTH_SHORT).show();
							}
						});
			}
		}

		private void initBodyPartSpinner() {
			bodyPartSpinnerAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_row);
			if(bodyParts != null) {
				bodyPartSpinnerAdapter.clear();
				bodyPartSpinnerAdapter.addAll(bodyParts);
				bodyPartSpinnerAdapter.notifyDataSetChanged();
				bodyPartSpinner.setAdapter(bodyPartSpinnerAdapter);
			}
			else {
				bodyPartRxHandler.getAll()
						.observeOn(AndroidSchedulers.mainThread())
						.subscribeOn(Schedulers.io())
						.subscribe(new Subscriber<Collection<BodyPart>>() {
							@Override
							public void onCompleted() {}
							@Override
							public void onError(Throwable e) {
								Log.e(LOG_TAG, "Exception caught getting all BodyPart instances in initBodyPartSpinner", e);
							}
							@Override
							public void onNext(Collection<BodyPart> bodyPartsResults) {
								bodyParts = bodyPartsResults;
								bodyPartSpinnerAdapter.clear();
								bodyPartSpinnerAdapter.addAll(bodyPartsResults);
								bodyPartSpinnerAdapter.notifyDataSetChanged();
								bodyPartSpinner.setAdapter(bodyPartSpinnerAdapter);
							}
						});
			}

			bodyPartSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					if(currentInstance.getBodyPart() == null || bodyPartSpinnerAdapter.getPosition(currentInstance.getBodyPart()) != position) {
						currentInstance.setBodyPart(bodyPartSpinnerAdapter.getItem(position));
						saveItem();
					}
				}
				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					if(currentInstance.getBodyPart() != null) {
						currentInstance.setBodyPart(null);
						saveItem();
					}
				}
			});
		}
	}
}
