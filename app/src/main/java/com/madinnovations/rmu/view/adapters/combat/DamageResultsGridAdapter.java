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
package com.madinnovations.rmu.view.adapters.combat;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.controller.rxhandler.combat.DamageResultRowRxHandler;
import com.madinnovations.rmu.controller.rxhandler.combat.DamageResultRxHandler;
import com.madinnovations.rmu.data.entities.combat.CriticalType;
import com.madinnovations.rmu.data.entities.combat.DamageResult;
import com.madinnovations.rmu.data.entities.combat.DamageResultRow;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.widgets.MultiPasteEditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Populates a ListView with {@link DamageResultRow} information
 */
public class DamageResultsGridAdapter extends BaseAdapter {
	private static final String TAG = "DamageResultsGridAdapt";
	private static final int LAYOUT_RESOURCE_ID = R.layout.damage_results_list_row;
	private SparseArray<DamageResultRow> data;
	private Context                      context;
	private DamageResultRxHandler        damageResultRxHandler;
	private DamageResultRowRxHandler     damageResultRowRxHandler;
	private LayoutInflater               layoutInflater;
	private InputFilter                  inputFilter;
	private Pattern                      pattern;
	private Toast                        toast = null;

	/**
	 * Creates a new DamageResultListAdapter instance.
	 *
	 * @param context the view {@link Context} the adapter will be attached to.
	 * @param damageResultRxHandler  a DamageResultRxHandler instance
	 * @param damageResultRowRxHandler  a DamageResultRowRxHandler instance
	 */
	@Inject
	DamageResultsGridAdapter(CampaignActivity context, DamageResultRxHandler damageResultRxHandler,
									DamageResultRowRxHandler damageResultRowRxHandler) {
		this.context = context;
		this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.damageResultRxHandler = damageResultRxHandler;
		this.damageResultRowRxHandler = damageResultRowRxHandler;
		this.inputFilter = new DamageResultInputFilter();

		StringBuilder builder = new StringBuilder(20 + CriticalType.values().length*2);
		builder.append("(\\d*)");
		builder.append("([A-J])?([");
		for (CriticalType criticalType : CriticalType.values()) {
			builder.append(criticalType.getCode()).append(",");
		}
		builder.deleteCharAt(builder.length() - 1);
		builder.append("])?");
		pattern = Pattern.compile(builder.toString());
	}

	@Override
	public int getCount() {
		int result = 0;

		if(data != null) {
			result = data.size();
		}

		return result;
	}

	@Override
	public Object getItem(int position) {
		DamageResultRow result = null;

		// Reverse the order so the high roll values are at the top
		if(data != null) {
			result = data.valueAt((data.size() - 1) - position);
		}

		return result;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@NonNull
	@Override
	public View getView(int position, View convertView, @NonNull ViewGroup parent) {
		View rowView;
		ViewHolder holder;

		if (convertView == null) {
			rowView = layoutInflater.inflate(LAYOUT_RESOURCE_ID, parent, false);
			holder = new ViewHolder(
					(EditText) rowView.findViewById(R.id.left_roll_view),
					(MultiPasteEditText)rowView.findViewById(R.id.at1_result_edit),
					(MultiPasteEditText)rowView.findViewById(R.id.at2_result_edit),
					(MultiPasteEditText)rowView.findViewById(R.id.at3_result_edit),
					(MultiPasteEditText)rowView.findViewById(R.id.at4_result_edit),
					(MultiPasteEditText)rowView.findViewById(R.id.at5_result_edit),
					(MultiPasteEditText)rowView.findViewById(R.id.at6_result_edit),
					(MultiPasteEditText)rowView.findViewById(R.id.at7_result_edit),
					(MultiPasteEditText)rowView.findViewById(R.id.at8_result_edit),
					(MultiPasteEditText)rowView.findViewById(R.id.at9_result_edit),
					(MultiPasteEditText)rowView.findViewById(R.id.at10_result_edit),
					(EditText) rowView.findViewById(R.id.right_roll_view));
			rowView.setTag(holder);
		}
		else {
			rowView = convertView;
			holder = (ViewHolder) convertView.getTag();
		}

		DamageResultRow resultsRow = (DamageResultRow)getItem(position);
		holder.damageResultRow = resultsRow;

		if(resultsRow != null) {
			String rollString = String.format(context.getString(R.string.min_max_roll_value), resultsRow.getRangeLowValue(),
											  resultsRow.getRangeHighValue());
			holder.leftRollView.setText(rollString);
			holder.at1ResultEdit.setText(formatResultString(resultsRow.getResults().get(1)));
			holder.at2ResultEdit.setText(formatResultString(resultsRow.getResults().get(2)));
			holder.at3ResultEdit.setText(formatResultString(resultsRow.getResults().get(3)));
			holder.at4ResultEdit.setText(formatResultString(resultsRow.getResults().get(4)));
			holder.at5ResultEdit.setText(formatResultString(resultsRow.getResults().get(5)));
			holder.at6ResultEdit.setText(formatResultString(resultsRow.getResults().get(6)));
			holder.at7ResultEdit.setText(formatResultString(resultsRow.getResults().get(7)));
			holder.at8ResultEdit.setText(formatResultString(resultsRow.getResults().get(8)));
			holder.at9ResultEdit.setText(formatResultString(resultsRow.getResults().get(9)));
			holder.at10ResultEdit.setText(formatResultString(resultsRow.getResults().get(10)));
			holder.rightRollView.setText(rollString);
		}

		return rowView;
	}

	public void addAll(@NonNull SparseArray<DamageResultRow> newData) {
		if(this.data == null || this.data.size() == 0) {
			this.data = new SparseArray<>(newData.size());
		}

		for (int i = 0; i < newData.size(); i++) {
			this.data.append(newData.keyAt(i), newData.valueAt(i));
		}
	}

	public void clear() {
		if(data != null) {
			data.clear();
		}
	}

	private void deleteDamageResult(int id) {
		damageResultRxHandler.deleteById(id)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new Subscriber<Boolean>() {
					@Override
					public void onCompleted() {
					}
					@Override
					public void onError(Throwable e) {
						Log.e(TAG, "Exception deleting DamageResult", e);
						if(toast != null) {
							toast.cancel();
						}
						toast = Toast.makeText(context, context.getString(R.string.toast_damage_result_delete_failed),
								Toast.LENGTH_SHORT);
						toast.show();
					}
					@Override
					public void onNext(Boolean aBoolean) {
						if(toast != null) {
							toast.cancel();
						}
						toast = Toast.makeText(context, context.getString(R.string.toast_damage_result_deleted),
								Toast.LENGTH_SHORT);
						toast.show();
					}
				});
	}

	public class ViewHolder {
		private DamageResultRow damageResultRow;
		private EditText leftRollView;
		MultiPasteEditText at1ResultEdit;
		MultiPasteEditText at2ResultEdit;
		MultiPasteEditText at3ResultEdit;
		MultiPasteEditText at4ResultEdit;
		MultiPasteEditText at5ResultEdit;
		MultiPasteEditText at6ResultEdit;
		MultiPasteEditText at7ResultEdit;
		MultiPasteEditText at8ResultEdit;
		MultiPasteEditText at9ResultEdit;
		MultiPasteEditText at10ResultEdit;
		private EditText rightRollView;

		public ViewHolder(EditText leftRollView, MultiPasteEditText at1ResultEdit, MultiPasteEditText at2ResultEdit,
						  MultiPasteEditText at3ResultEdit, MultiPasteEditText at4ResultEdit, MultiPasteEditText at5ResultEdit,
						  MultiPasteEditText at6ResultEdit, MultiPasteEditText at7ResultEdit, MultiPasteEditText at8ResultEdit,
						  MultiPasteEditText at9ResultEdit, MultiPasteEditText at10ResultEdit, EditText rightRollView) {

			this.leftRollView = leftRollView;
			this.at1ResultEdit = at1ResultEdit;
			initEdit(at1ResultEdit, 1);
			at1ResultEdit.setViewHolder(this);

			this.at2ResultEdit = at2ResultEdit;
			initEdit(at2ResultEdit, 2);
			at1ResultEdit.setNextMultiPaste(at2ResultEdit);

			this.at3ResultEdit = at3ResultEdit;
			initEdit(at3ResultEdit, 3);
			at2ResultEdit.setNextMultiPaste(at3ResultEdit);

			this.at4ResultEdit = at4ResultEdit;
			initEdit(at4ResultEdit, 4);
			at3ResultEdit.setNextMultiPaste(at4ResultEdit);

			this.at5ResultEdit = at5ResultEdit;
			initEdit(at5ResultEdit, 5);
			at4ResultEdit.setNextMultiPaste(at5ResultEdit);

			this.at6ResultEdit = at6ResultEdit;
			initEdit(at6ResultEdit, 6);
			at5ResultEdit.setNextMultiPaste(at6ResultEdit);

			this.at7ResultEdit = at7ResultEdit;
			initEdit(at7ResultEdit, 7);
			at6ResultEdit.setNextMultiPaste(at7ResultEdit);

			this.at8ResultEdit = at8ResultEdit;
			initEdit(at8ResultEdit, 8);
			at7ResultEdit.setNextMultiPaste(at8ResultEdit);

			this.at9ResultEdit = at9ResultEdit;
			initEdit(at9ResultEdit, 9);
			at8ResultEdit.setNextMultiPaste(at9ResultEdit);

			this.at10ResultEdit = at10ResultEdit;
			initEdit(at10ResultEdit, 10);
			at9ResultEdit.setNextMultiPaste(at10ResultEdit);
			at10ResultEdit.setNextMultiPaste(null);

			this.rightRollView = rightRollView;
		}

		private void initEdit(final MultiPasteEditText editText, final int armorType) {
			editText.setAtIndex(armorType);
			editText.setViewHolder(this);
			editText.setFilters(new InputFilter[] {inputFilter});
			editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
				@Override
				public void onFocusChange(View view, boolean hasFocus) {
					if(!hasFocus) {
						editText.setBackground(null);
						setResult(((EditText)view).getText(), (EditText)view, armorType);
					}
					else {
						if(editText.getBackground() != null) {
							editText.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
						}
						else {
							editText.setBackgroundColor(Color.RED);
						}
					}
				}
			});
		}

		public boolean setResult(final CharSequence text, EditText view, int armorType) {
			Short hits = null;
			String severity = null;
			CriticalType type = null;
			boolean matches;

			Matcher matcher = pattern.matcher(text);
			matches = matcher.matches();
			if(!matches) {
				view.setError(context.getString(R.string.validation_damage_results_format_invalid));
			}
			else {
				if(matcher.groupCount() >= 1 && text.length() > 0) {
					if(matcher.group(1).length()>0) {
						hits = Short.valueOf(matcher.group(1));
					}
					if(matcher.groupCount() == 3) {
						severity = matcher.group(2);
						if (severity != null) {
							String typeString = matcher.group(3);
							if (typeString == null ||  typeString.isEmpty()) {
								matches = false;
							}
							else {
								type = CriticalType.getTypeForCode(typeString.charAt(0));
							}
						}
					}
				}
				if(((View)view.getParent()).getTag() instanceof ViewHolder) {
					updateResult(armorType, hits, severity, type);
				}
			}

			return matches;
		}

		private void updateResult(int armorType, Short hits, String severity, CriticalType type) {
			boolean resultChanged = false;
			boolean rowChanged = false;
			DamageResult damageResult = damageResultRow.getResults().get(armorType);
			if(hits != null) {
				if(damageResult == null) {
					damageResult = new DamageResult();
					damageResult.setArmorType((short)armorType);
					damageResultRow.getResults().put(armorType, damageResult);
					resultChanged = true;
					rowChanged = true;
				}
				damageResult.setDamageResultRow(damageResultRow);
				if(damageResult.getHits() != hits) {
					damageResult.setHits(hits);
					resultChanged = true;
				}
				if((severity == null && damageResult.getCriticalSeverity() != null) ||
						(severity != null && !severity.isEmpty() &&
								damageResult.getCriticalSeverity() == null) ||
						(severity != null && !severity.isEmpty() && severity.charAt(0)
								!= damageResult.getCriticalSeverity())) {
					resultChanged = true;
					if(severity == null || severity.isEmpty()) {
						damageResult.setCriticalSeverity(null);
					}
					else {
						damageResult.setCriticalSeverity(severity.charAt(0));
					}
				}
				if((type == null && damageResult.getCriticalType() != null) ||
						(type != null && !type.equals(damageResult.getCriticalType()))) {
					resultChanged = true;
					if(type == null) {
						damageResult.setCriticalType(null);
					}
					else {
						damageResult.setCriticalType(type);
					}
				}
				if(resultChanged) {
					if (rowChanged) {
						saveResultRow(damageResultRow);
					}
					saveResult(damageResult, damageResultRow);
				}
			}
			else {
				if(damageResult != null) {
					damageResultRow.getResults().remove(armorType);
					if(damageResult.getId() != -1) {
						final int damageResultId = damageResult.getId();
						saveDamageResultRow(damageResultRow, damageResultId);
					}
				}
			}
		}

		void saveResult(DamageResult damageResult, final DamageResultRow damageResultRow) {
			damageResultRxHandler.save(damageResult)
					.observeOn(AndroidSchedulers.mainThread())
					.subscribeOn(Schedulers.io())
					.subscribe(new Subscriber<DamageResult>() {
						@Override
						public void onCompleted() {}
						@Override
						public void onError(Throwable e) {
							Log.e(TAG, "Exception saving DamageResult", e);
							if(toast != null) {
								toast.cancel();
							}
							toast = Toast.makeText(context,  context.getString(R.string.toast_damage_result_save_failed),
									Toast.LENGTH_SHORT);
							toast.show();
						}
						@Override
						public void onNext(DamageResult damageResult) {
							if(toast != null) {
								toast.cancel();
							}
							toast = Toast.makeText(context,  context.getString(R.string.toast_damage_result_saved),
									Toast.LENGTH_SHORT);
							toast.show();
						}
					});
		}

		void saveResultRow(DamageResultRow damageResultRow) {
			damageResultRowRxHandler.save(damageResultRow)
					.subscribe(new Subscriber<DamageResultRow>() {
						@Override
						public void onCompleted() {
						}
						@Override
						public void onError(Throwable e) {
							Log.e(TAG, "Exception saving DamageResultRow", e);
							if(toast != null) {
								toast.cancel();
							}
							toast = Toast.makeText(context, context.getString(R.string.toast_damage_result_row_save_failed),
									Toast.LENGTH_SHORT);
							toast.show();
						}
						@Override
						public void onNext(DamageResultRow savedDamageResultRow) {
							if(toast != null) {
								toast.cancel();
							}
							toast = Toast.makeText(context, context.getString(R.string.toast_damage_result_row_saved),
									Toast.LENGTH_SHORT);
							toast.show();
						}
					});
		}

		void saveDamageResultRow(DamageResultRow damageResultRow, final int damageResultId) {
			damageResultRowRxHandler.save(damageResultRow)
					.observeOn(AndroidSchedulers.mainThread())
					.subscribeOn(Schedulers.io())
					.subscribe(new Subscriber<DamageResultRow>() {
						@Override
						public void onCompleted() {
							deleteDamageResult(damageResultId);
						}
						@Override
						public void onError(Throwable e) {
							Log.e(TAG, "Exception saving DamageResultRow", e);
							if(toast != null) {
								toast.cancel();
							}
							toast = Toast.makeText(context, context.getString(R.string.toast_damage_result_row_save_failed),
									Toast.LENGTH_SHORT);
							toast.show();
						}
						@Override
						public void onNext(DamageResultRow savedDamageResultRow) {
							if(toast != null) {
								toast.cancel();
							}
							toast = Toast.makeText(context, context.getString(R.string.toast_damage_result_row_saved),
									Toast.LENGTH_SHORT);
							toast.show();
						}
					});
		}

	}

	private String formatResultString(DamageResult damageResult) {
		StringBuilder builder = new StringBuilder(6);
		if(damageResult != null) {
			builder.append(String.valueOf(damageResult.getHits()));
			if(damageResult.getCriticalSeverity() != null) {
				builder.append(damageResult.getCriticalSeverity());
			}
			if(damageResult.getCriticalType() != null) {
				builder.append(damageResult.getCriticalType().getCode());
			}
		}
		return builder.toString();
	}

	private class DamageResultInputFilter implements InputFilter {
		@Override
		public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
			StringBuilder builder = new StringBuilder(dest.toString());
			if(source.length() > 0) {
				builder.insert(dstart, source);
			}
			else {
				builder.delete(dstart, dend);
			}

			Matcher matcher = pattern.matcher(builder);
			if(matcher.matches()) {
				View focused = ((Activity)context).getCurrentFocus();
				if(focused instanceof EditText) {
					((EditText) focused).setError(null);
				}
				return null;
			}
			else {
				if(source.length() > 0) {
					return "";
				}
				else {
					return dest.subSequence(dstart, dend);
				}
			}
		}
	}
}
