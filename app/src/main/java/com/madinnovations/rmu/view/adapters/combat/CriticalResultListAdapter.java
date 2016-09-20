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
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
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

import java.util.Collection;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Populates a ListView with {@link CriticalResult} information
 */
public class CriticalResultListAdapter extends ArrayAdapter<CriticalResult> {
	private static final int LAYOUT_RESOURCE_ID = R.layout.list_critical_result_row;
	private LayoutInflater layoutInflater;
	@Inject
	BodyPartRxHandler       bodyPartRxHandler;
	private   ArrayAdapter<BodyPart>  bodyPartSpinnerAdapter;
	@Inject
	CriticalResultRxHandler criticalResultRxHandler;

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
			holder.resultTextEdit.setText(criticalResult.getResultText());
			holder.rightRollView.setText(rollString);
		}
		return rowView;
	}

	private class ViewHolder {
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
			initHitsEdit();
			this.bleedingEdit = bleedingEdit;
			initBleedingEdit();
			this.fatigueEdit = fatigueEdit;
			initFatigueEdit();
			this.breakageEdit = breakageEdit;
			initBreakageEdit();
			this.injuryEdit = injuryEdit;
			initInjuryEdit();
			this.dazedEdit = dazedEdit;
			initDazedEdit();
			this.stunnedEdit = stunnedEdit;
			initStunnedEdit();
			this.noParryEdit = noParryEdit;
			initNoParryEdit();
			this.staggeredCheckBox = staggeredCheckBox;
			initStaggeredCheckBox();
			this.knockBackEdit = knockBackEdit;
			initKnockBackEdit();
			this.proneCheckBox = proneCheckBox;
			initProneCheckBox();
			this.grappledEdit = grappledEdit;
			initGrappledEdit();
			this.resultTextEdit = resultTextEdit;
			initResultTextEdit();
			this.rightRollView = rightRollView;
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
								Log.e("CriticalResultsFragment", "Exception saving new CriticalResult", e);
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
			bodyPartSpinner.setAdapter(bodyPartSpinnerAdapter);
			bodyPartRxHandler.getAll()
					.observeOn(AndroidSchedulers.mainThread())
					.subscribeOn(Schedulers.io())
					.subscribe(new Subscriber<Collection<BodyPart>>() {
						@Override
						public void onCompleted() {}
						@Override
						public void onError(Throwable e) {
							Log.e("CriticalResultsFragment", "Exception caught getting all BodyPart instances in initBodyPartSpinner", e);
						}
						@Override
						public void onNext(Collection<BodyPart> bodyPartsResults) {
							bodyPartSpinnerAdapter.clear();
							bodyPartSpinnerAdapter.addAll(bodyPartsResults);
							bodyPartSpinnerAdapter.notifyDataSetChanged();
						}
					});
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

		private void initHitsEdit() {
			hitsEdit.addTextChangedListener(new TextWatcher() {
				@Override
				public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
				@Override
				public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
				@Override
				public void afterTextChanged(Editable editable) {
					if (editable.length() == 0) {
						hitsEdit.setError(getContext().getString(R.string.validation_hits_required));
					}
					else {
						hitsEdit.setError(null);
					}
				}
			});
			hitsEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
				@Override
				public void onFocusChange(View view, boolean hasFocus) {
					Log.d("RMU", "hitsEdit focus changed. hasFocus = " + hasFocus);
					if(!hasFocus) {
						if(hitsEdit.getText().length() > 0) {
							short newHits = Short.valueOf(hitsEdit.getText().toString());
							if (newHits != currentInstance.getHits()) {
								currentInstance.setHits(newHits);
								saveItem();
							}
						}
					}
				}
			});
		}

		private void initBleedingEdit() {
			bleedingEdit.addTextChangedListener(new TextWatcher() {
				@Override
				public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
				@Override
				public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
				@Override
				public void afterTextChanged(Editable editable) {
					if (editable.length() == 0) {
						bleedingEdit.setError(getContext().getString(R.string.validation_bleeding_required));
					}
					else {
						bleedingEdit.setError(null);
					}
				}
			});
			bleedingEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
				@Override
				public void onFocusChange(View view, boolean hasFocus) {
					if(!hasFocus) {
						if(bleedingEdit.getText().length() > 0) {
							short newBleeding = Short.valueOf(bleedingEdit.getText().toString());
							if (newBleeding != currentInstance.getBleeding()) {
								currentInstance.setBleeding(newBleeding);
								saveItem();
							}
						}
					}
				}
			});
		}

		private void initFatigueEdit() {
			fatigueEdit.addTextChangedListener(new TextWatcher() {
				@Override
				public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
				@Override
				public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
				@Override
				public void afterTextChanged(Editable editable) {
					if (editable.length() == 0) {
						fatigueEdit.setError(getContext().getString(R.string.validation_fatigue_required));
					}
					else {
						fatigueEdit.setError(null);
					}
				}
			});
			fatigueEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
				@Override
				public void onFocusChange(View view, boolean hasFocus) {
					if(!hasFocus) {
						if(fatigueEdit.getText().length() > 0) {
							short newValue = Short.valueOf(fatigueEdit.getText().toString());
							if (newValue != currentInstance.getFatigue()) {
								currentInstance.setFatigue(newValue);
								saveItem();
							}
						}
					}
				}
			});
		}

		private void initBreakageEdit() {
			breakageEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
				@Override
				public void onFocusChange(View view, boolean hasFocus) {
					if(!hasFocus) {
						Short newValue = null;
						if(breakageEdit.getText().length() > 0) {
							newValue = Short.valueOf(breakageEdit.getText().toString());
						}
						if (newValue == null && currentInstance.getBreakage() != null ||
								newValue != null && !newValue.equals(currentInstance.getBreakage())) {
							currentInstance.setBreakage(newValue);
							saveItem();
						}
					}
				}
			});
		}

		private void initInjuryEdit() {
			injuryEdit.addTextChangedListener(new TextWatcher() {
				@Override
				public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
				@Override
				public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
				@Override
				public void afterTextChanged(Editable editable) {
					if (editable.length() == 0) {
						injuryEdit.setError(getContext().getString(R.string.validation_injury_required));
					}
					else {
						injuryEdit.setError(null);
					}
				}
			});
			injuryEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
				@Override
				public void onFocusChange(View view, boolean hasFocus) {
					if(!hasFocus) {
						if(injuryEdit.getText().length() > 0) {
							short newValue = Short.valueOf(injuryEdit.getText().toString());
							if (newValue != currentInstance.getInjury()) {
								currentInstance.setInjury(newValue);
								saveItem();
							}
						}
					}
				}
			});
		}

		private void initDazedEdit() {
			dazedEdit.addTextChangedListener(new TextWatcher() {
				@Override
				public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
				@Override
				public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
				@Override
				public void afterTextChanged(Editable editable) {
					if (editable.length() == 0) {
						dazedEdit.setError(getContext().getString(R.string.validation_dazed_required));
					}
					else {
						dazedEdit.setError(null);
					}
				}
			});
			dazedEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
				@Override
				public void onFocusChange(View view, boolean hasFocus) {
					if(!hasFocus) {
						if(dazedEdit.getText().length() > 0) {
							short newValue = Short.valueOf(dazedEdit.getText().toString());
							if (newValue != currentInstance.getDazed()) {
								currentInstance.setDazed(newValue);
								saveItem();
							}
						}
					}
				}
			});
		}

		private void initStunnedEdit() {
			stunnedEdit.addTextChangedListener(new TextWatcher() {
				@Override
				public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
				@Override
				public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
				@Override
				public void afterTextChanged(Editable editable) {
					if (editable.length() == 0) {
						stunnedEdit.setError(getContext().getString(R.string.validation_stunned_required));
					}
					else {
						stunnedEdit.setError(null);
					}
				}
			});
			stunnedEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
				@Override
				public void onFocusChange(View view, boolean hasFocus) {
					if(!hasFocus) {
						if(stunnedEdit.getText().length() > 0) {
							short newValue = Short.valueOf(stunnedEdit.getText().toString());
							if (newValue != currentInstance.getStunned()) {
								currentInstance.setStunned(newValue);
								saveItem();
							}
						}
					}
				}
			});
		}

		private void initNoParryEdit() {
			noParryEdit.addTextChangedListener(new TextWatcher() {
				@Override
				public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
				@Override
				public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
				@Override
				public void afterTextChanged(Editable editable) {
					if (editable.length() == 0) {
						noParryEdit.setError(getContext().getString(R.string.validation_no_parry_required));
					}
					else {
						noParryEdit.setError(null);
					}
				}
			});
			noParryEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
				@Override
				public void onFocusChange(View view, boolean hasFocus) {
					if(!hasFocus) {
						if(noParryEdit.getText().length() > 0) {
							short newValue = Short.valueOf(noParryEdit.getText().toString());
							if (newValue != currentInstance.getNoParry()) {
								currentInstance.setNoParry(newValue);
								saveItem();
							}
						}
					}
				}
			});
		}

		private void initStaggeredCheckBox() {
			staggeredCheckBox.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					currentInstance.setStaggered(staggeredCheckBox.isChecked());
					saveItem();
				}
			});
		}

		private void initKnockBackEdit() {
			knockBackEdit.addTextChangedListener(new TextWatcher() {
				@Override
				public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
				@Override
				public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
				@Override
				public void afterTextChanged(Editable editable) {
					if (editable.length() == 0) {
						knockBackEdit.setError(getContext().getString(R.string.validation_knock_back_required));
					}
					else {
						knockBackEdit.setError(null);
					}
				}
			});
			knockBackEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
				@Override
				public void onFocusChange(View view, boolean hasFocus) {
					if(!hasFocus) {
						if(knockBackEdit.getText().length() > 0) {
							short newValue = Short.valueOf(knockBackEdit.getText().toString());
							if (newValue != currentInstance.getKnockBack()) {
								currentInstance.setKnockBack(newValue);
								saveItem();
							}
						}
					}
				}
			});
		}

		private void initProneCheckBox() {
			proneCheckBox.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					currentInstance.setProne(proneCheckBox.isChecked());
					saveItem();
				}
			});
		}

		private void initGrappledEdit() {
			grappledEdit.addTextChangedListener(new TextWatcher() {
				@Override
				public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
				@Override
				public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
				@Override
				public void afterTextChanged(Editable editable) {
					if (editable.length() == 0) {
						grappledEdit.setError(getContext().getString(R.string.validation_grappled_required));
					}
					else {
						grappledEdit.setError(null);
					}
				}
			});
			grappledEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
				@Override
				public void onFocusChange(View view, boolean hasFocus) {
					if(!hasFocus) {
						if(grappledEdit.getText().length() > 0) {
							short newValue = Short.valueOf(grappledEdit.getText().toString());
							if (newValue != currentInstance.getGrappled()) {
								currentInstance.setGrappled(newValue);
								saveItem();
							}
						}
					}
				}
			});
		}

		private void initResultTextEdit() {
			resultTextEdit.addTextChangedListener(new TextWatcher() {
				@Override
				public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
				@Override
				public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
				@Override
				public void afterTextChanged(Editable editable) {
					if (editable.length() == 0) {
						resultTextEdit.setError(getContext().getString(R.string.validation_critical_result_text_required));
					}
				}
			});
			resultTextEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
				@Override
				public void onFocusChange(View view, boolean hasFocus) {
					if(!hasFocus) {
						final String newResultText = resultTextEdit.getText().toString();
						if (!newResultText.equals(currentInstance.getResultText())) {
							currentInstance.setResultText(newResultText);
							saveItem();
						}
						if(!newResultText.isEmpty()) {
							resultTextEdit.setError(null);
						}
					}
				}
			});
		}
	}
}
