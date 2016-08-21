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
package com.madinnovations.rmu.view.adapters.character;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.controller.rxhandler.character.ProfessionRxHandler;
import com.madinnovations.rmu.data.entities.character.ProfessionSkillCategoryCost;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Populates a ListView with {@link ProfessionSkillCategoryCost} information
 */
public class ProfessionCategoryCostListAdapter extends ArrayAdapter<ProfessionSkillCategoryCost> {
	private static final int LAYOUT_RESOURCE_ID = R.layout.list_profession_category_costs_row;
	private ProfessionRxHandler professionRxHandler;
	private LayoutInflater layoutInflater;

	/**
	 * Creates a new ProfessionCategoryCostListAdapter instance.
	 *
	 * @param context the view {@link Context} the adapter will be attached to.
	 */
	@Inject
	public ProfessionCategoryCostListAdapter(Context context, ProfessionRxHandler professionRxHandler) {
		super(context, LAYOUT_RESOURCE_ID);
		this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.professionRxHandler = professionRxHandler;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView;
		ViewHolder holder;

		if (convertView == null) {
			rowView = layoutInflater.inflate(LAYOUT_RESOURCE_ID, parent, false);
			holder = new ViewHolder((TextView) rowView.findViewById(R.id.name_view),
					(EditText)rowView.findViewById(R.id.initial_cost_edit),
					(EditText)rowView.findViewById(R.id.additional_cost_edit));
			rowView.setTag(holder);
		}
		else {
			rowView = convertView;
			holder = (ViewHolder) convertView.getTag();
		}

		ProfessionSkillCategoryCost skillCategoryCost = getItem(position);
		holder.skillCategoryCost = skillCategoryCost;
		holder.nameView.setText(skillCategoryCost.getSkillCategory().getName());
		holder.initialCostEdit.setText(String.valueOf(skillCategoryCost.getSkillCost().getFirstCost()));
		holder.additionalCostEdit.setText(String.valueOf(skillCategoryCost.getSkillCost().getAdditionalCost()));
		return rowView;
	}

	private class ViewHolder {
		private ProfessionSkillCategoryCost skillCategoryCost;
		private TextView nameView;
		private EditText initialCostEdit;
		private EditText additionalCostEdit;

		ViewHolder(TextView nameView, EditText initialCostEdit, EditText additionalCostEdit) {
			this.nameView = nameView;
			this.initialCostEdit = initialCostEdit;
			initCostEdit(this.initialCostEdit, 0);
			this.additionalCostEdit = additionalCostEdit;
			initCostEdit(this.additionalCostEdit, 1);
		}

		private void initCostEdit(@NonNull final EditText costEdit, final int costIndex) {
			costEdit.addTextChangedListener(new TextWatcher() {
				@Override
				public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
				@Override
				public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
				@Override
				public void afterTextChanged(Editable editable) {
					if(editable.length() == 0) {
						if(costIndex == 0) {
							costEdit.setError(getContext().getString(R.string.validation_initial_cost_required));
						}
						else {
							costEdit.setError(getContext().getString(R.string.validation_additional_cost_required));
						}
					}
					else {
						int newValue = Integer.valueOf(editable.toString());
						int otherValue;
						if(costIndex == 0) {
							if(additionalCostEdit.length() > 0) {
								otherValue = Integer.valueOf(additionalCostEdit.getText().toString());
								if(otherValue < newValue) {
									initialCostEdit.setError(getContext().getString(R.string.validation_initial_cost_gt_additional_code));
								}
								else {
									initialCostEdit.setError(null);
									if(additionalCostEdit.length() > 0) {
										additionalCostEdit.setError(null);
									}
								}
							}
						}
						else {
							if(initialCostEdit.length() > 0) {
								otherValue = Integer.valueOf(initialCostEdit.getText().toString());
								if(newValue < otherValue) {
									additionalCostEdit.setError(getContext().getString(R.string.validation_initial_cost_gt_additional_code));
								}
								else {
									additionalCostEdit.setError(null);
									if(initialCostEdit.length() > 0) {
										initialCostEdit.setError(null);
									}
								}
							}
						}
					}
				}
			});
			costEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
				@Override
				public void onFocusChange(View view, boolean hasFocus) {
					if(!hasFocus) {
						if(((View)view.getParent()).getTag() instanceof ViewHolder && costEdit == view) {
							if (costEdit.length() > 0) {
								boolean changed = false;
								int cost = Integer.valueOf(costEdit.getText().toString());
								if(costIndex == 0 && skillCategoryCost.getSkillCost().getFirstCost() != cost) {
									skillCategoryCost.getSkillCost().setFirstCost(cost);
									changed = true;
								}
								else if(costIndex == 1 && skillCategoryCost.getSkillCost().getAdditionalCost() != cost) {
									skillCategoryCost.getSkillCost().setAdditionalCost(cost);
									changed = true;
								}
								if(changed && skillCategoryCost.isValid()) {
									professionRxHandler.saveSkillCategoryCost(skillCategoryCost)
											.observeOn(AndroidSchedulers.mainThread())
											.subscribeOn(Schedulers.io())
											.subscribe(new Subscriber<ProfessionSkillCategoryCost>() {
												@Override
												public void onCompleted() {}
												@Override
												public void onError(Throwable e) {
													Log.e("ProfCatCostListAdapter", "Exception occurred saving ProfessionSkillCategoyCost instance", e);
													String toastString;
													toastString = getContext().getString(R.string.toast_profession_category_skill_cost_save_failed);
													Toast.makeText(getContext(), toastString, Toast.LENGTH_SHORT).show();
												}

												@Override
												public void onNext(ProfessionSkillCategoryCost professionSkillCategoryCost) {
													String toastString;
													toastString = getContext().getString(R.string.toast_profession_category_skill_cost_saved);
													Toast.makeText(getContext(), toastString, Toast.LENGTH_SHORT).show();
												}
											});
								}
							}
						}
					}
				}
			});
		}
	}
}
