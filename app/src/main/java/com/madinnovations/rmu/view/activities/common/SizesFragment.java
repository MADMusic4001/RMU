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
package com.madinnovations.rmu.view.activities.common;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.madinnovations.rmu.controller.rxhandler.common.SizeRxHandler;
import com.madinnovations.rmu.data.entities.common.Size;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.adapters.ThreeFieldListAdapter;
import com.madinnovations.rmu.view.di.modules.CommonFragmentModule;

import java.util.Collection;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Handles interactions with the UI for sizes.
 */
public class SizesFragment extends Fragment implements ThreeFieldListAdapter.GetValues<Size> {
	@Inject
	protected SizeRxHandler   sizeRxHandler;
	private ThreeFieldListAdapter<Size> listAdapter;
	private   ListView        listView;
	private   EditText        codeEdit;
	private   EditText        nameEdit;
	private   EditText        examplesEdit;
	private   EditText        maxHeightEdit;
	private   EditText        maxWeightEdit;
	private   EditText        minHeightEdit;
	private   EditText        minWeightEdit;
	private   Size            currentInstance = new Size();
	private   boolean         isNew = true;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((CampaignActivity)getActivity()).getActivityComponent().
				newCommonFragmentComponent(new CommonFragmentModule(this)).injectInto(this);

		View layout = inflater.inflate(R.layout.sizes_fragment, container, false);

		((TextView)layout.findViewById(R.id.header_field1)).setText(R.string.label_size_code);
		((TextView)layout.findViewById(R.id.header_field2)).setText(R.string.label_size_name);
		((LinearLayout.LayoutParams)layout.findViewById(R.id.header_field3).getLayoutParams()).weight = 5;
		((TextView)layout.findViewById(R.id.header_field3)).setText(R.string.label_size_examples);

		initCodeEdit(layout);
		initNameEdit(layout);
		initExamplesEdit(layout);
		initMinWeightEdit(layout);
		initMaxWeightEdit(layout);
		initMinHeightEdit(layout);
		initMaxHeightEdit(layout);
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
		inflater.inflate(R.menu.sizes_action_bar, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if(id == R.id.action_new_size) {
			if(copyViewsToItem()) {
				saveItem();
			}
			currentInstance = new Size();
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
		getActivity().getMenuInflater().inflate(R.menu.size_context_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		final Size size;

		AdapterView.AdapterContextMenuInfo info =
				(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

		switch (item.getItemId()) {
			case R.id.context_new_size:
				if(copyViewsToItem()) {
					saveItem();
				}
				currentInstance = new Size();
				isNew = true;
				copyItemToViews();
				listView.clearChoices();
				listAdapter.notifyDataSetChanged();
				return true;
			case R.id.context_delete_size:
				size = (Size) listView.getItemAtPosition(info.position);
				if(size != null) {
					deleteItem(size);
					return true;
				}
				break;
		}
		return super.onContextItemSelected(item);
	}

	private boolean copyViewsToItem() {
		boolean changed = false;

		View currentFocusView = getActivity().getCurrentFocus();
		if(currentFocusView != null) {
			currentFocusView.clearFocus();
		}

		String newValue = codeEdit.getText().toString();
		if(newValue.isEmpty()) {
			newValue = null;
		}
		if((newValue == null && currentInstance.getCode() != null) ||
				newValue != null && !newValue.equals(currentInstance.getCode())) {
			currentInstance.setCode(newValue);
			changed = true;
		}

		newValue = nameEdit.getText().toString();
		if(newValue.isEmpty()) {
			newValue = null;
		}
		if((newValue == null && currentInstance.getName() != null) ||
				newValue != null && !newValue.equals(currentInstance.getName())) {
			currentInstance.setName(newValue);
			changed = true;
		}

		newValue = examplesEdit.getText().toString();
		if(newValue.isEmpty()) {
			newValue = null;
		}
		if((newValue == null && currentInstance.getExamples() != null) ||
				newValue != null && !newValue.equals(currentInstance.getExamples())) {
			currentInstance.setExamples(newValue);
			changed = true;
		}

		Integer newInteger = null;
		if(minWeightEdit.getText().length() > 0) {
			newInteger = Integer.valueOf(minWeightEdit.getText().toString());
		}
		if((newInteger == null && currentInstance.getMinWeight() != null) ||
				(newInteger != null && !newInteger.equals(currentInstance.getMinWeight()))) {
			currentInstance.setMinWeight(newInteger);
			changed = true;
		}

		newInteger = null;
		if(maxWeightEdit.getText().length() > 0) {
			newInteger = Integer.valueOf(maxWeightEdit.getText().toString());
		}
		if((newInteger == null && currentInstance.getMaxWeight() != null) ||
				(newInteger != null && !newInteger.equals(currentInstance.getMaxWeight()))) {
			currentInstance.setMaxWeight(newInteger);
			changed = true;
		}

		newInteger = null;
		if(minHeightEdit.getText().length() > 0) {
			newInteger = Integer.valueOf(minHeightEdit.getText().toString());
		}
		if((newInteger == null && currentInstance.getMinHeight() != null) ||
				(newInteger != null && !newInteger.equals(currentInstance.getMinHeight()))) {
			currentInstance.setMinHeight(newInteger);
			changed = true;
		}

		newInteger = null;
		if(maxHeightEdit.getText().length() > 0) {
			newInteger = Integer.valueOf(maxHeightEdit.getText().toString());
		}
		if((newInteger == null && currentInstance.getMaxHeight() != null) ||
				(newInteger != null && !newInteger.equals(currentInstance.getMaxHeight()))) {
			currentInstance.setMaxHeight(newInteger);
			changed = true;
		}

		return changed;
	}

	private void copyItemToViews() {
		codeEdit.setText(currentInstance.getCode());
		nameEdit.setText(currentInstance.getName());
		examplesEdit.setText(currentInstance.getExamples());
		if(currentInstance.getMinWeight() != null) {
			minWeightEdit.setText(String.valueOf(currentInstance.getMinWeight()));
		}
		else {
			minWeightEdit.setText(null);
		}
		if(currentInstance.getMaxWeight() != null) {
			maxWeightEdit.setText(String.valueOf(currentInstance.getMaxWeight()));
		}
		else {
			maxWeightEdit.setText(null);
		}
		if(currentInstance.getMinHeight() != null) {
			minHeightEdit.setText(String.valueOf(currentInstance.getMinHeight()));
		}
		else {
			minHeightEdit.setText(null);
		}
		if(currentInstance.getMaxHeight() != null) {
			maxHeightEdit.setText(String.valueOf(currentInstance.getMaxHeight()));
		}
		else {
			maxHeightEdit.setText(null);
		}

		if(currentInstance.getCode() != null && !currentInstance.getCode().isEmpty()) {
			codeEdit.setError(null);
		}
		if(currentInstance.getName() != null && !currentInstance.getName().isEmpty()) {
			nameEdit.setError(null);
		}
		if(currentInstance.getExamples() != null && !currentInstance.getExamples().isEmpty()) {
			examplesEdit.setError(null);
		}
		minWeightEdit.setError(null);
		maxWeightEdit.setError(null);
		minHeightEdit.setError(null);
		maxHeightEdit.setError(null);
	}

	private void saveItem() {
		if(currentInstance.isValid()) {
			final boolean wasNew = isNew;
			isNew = false;
			sizeRxHandler.save(currentInstance)
					.observeOn(AndroidSchedulers.mainThread())
					.subscribeOn(Schedulers.io())
					.subscribe(new Subscriber<Size>() {
						@Override
						public void onCompleted() {}
						@Override
						public void onError(Throwable e) {
							Log.e("SizesFragment", "Exception saving new Size: " + currentInstance, e);
							Toast.makeText(getActivity(), R.string.toast_size_save_failed, Toast.LENGTH_SHORT).show();
						}
						@Override
						public void onNext(Size savedItem) {
							if (wasNew) {
								listAdapter.add(savedItem);
								if(savedItem == currentInstance) {
									listView.setSelection(listAdapter.getPosition(savedItem));
									listView.setItemChecked(listAdapter.getPosition(savedItem), true);
								}
								listAdapter.notifyDataSetChanged();
							}
							if(getActivity() != null) {
								Toast.makeText(getActivity(), R.string.toast_size_saved, Toast.LENGTH_SHORT).show();
								int position = listAdapter.getPosition(savedItem);
								LinearLayout v = (LinearLayout) listView.getChildAt(position - listView.getFirstVisiblePosition());
								if (v != null) {
									TextView textView = (TextView) v.findViewById(R.id.row_field1);
									textView.setText(savedItem.getCode());
									textView = (TextView) v.findViewById(R.id.row_field2);
									textView.setText(savedItem.getName());
									textView = (TextView) v.findViewById(R.id.row_field3);
									textView.setText(savedItem.getExamples());
								}
							}
						}
					});
		}
	}

	private void deleteItem(@NonNull final Size item) {
		sizeRxHandler.deleteById(item.getId())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new Subscriber<Boolean>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e("SizesFragment", "Exception when deleting: " + item, e);
						Toast.makeText(getActivity(), R.string.toast_size_delete_failed, Toast.LENGTH_SHORT).show();
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
								currentInstance = new Size();
								isNew = true;
							}
							copyItemToViews();
							Toast.makeText(getActivity(), R.string.toast_size_deleted, Toast.LENGTH_SHORT).show();
						}
					}
				});
	}

	private void initCodeEdit(View layout) {
		codeEdit = (EditText)layout.findViewById(R.id.code_edit);
		codeEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length() == 0 && codeEdit != null) {
					codeEdit.setError(getString(R.string.validation_size_code_required));
				}
			}
		});
		codeEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					final String newCode = codeEdit.getText().toString();
					if (currentInstance != null && !newCode.equals(currentInstance.getCode())) {
						currentInstance.setCode(newCode);
						saveItem();
					}
				}
			}
		});
	}

	private void initNameEdit(View layout) {
		nameEdit = (EditText)layout.findViewById(R.id.name_edit);
		nameEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length() == 0 && nameEdit != null) {
					nameEdit.setError(getString(R.string.validation_size_name_required));
				}
			}
		});
		nameEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					final String newName = nameEdit.getText().toString();
					if (currentInstance != null && !newName.equals(currentInstance.getName())) {
						currentInstance.setName(newName);
						saveItem();
					}
				}
			}
		});
	}

	private void initExamplesEdit(View layout) {
		examplesEdit = (EditText)layout.findViewById(R.id.examples_edit);
		examplesEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length() == 0 && examplesEdit != null) {
					examplesEdit.setError(getString(R.string.validation_size_examples_required));
				}
			}
		});
		examplesEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					final String newExamples = examplesEdit.getText().toString();
					if (currentInstance != null && !newExamples.equals(currentInstance.getExamples())) {
						currentInstance.setExamples(newExamples);
						saveItem();
					}
				}
			}
		});
	}

	private void initMinWeightEdit(View layout) {
		minWeightEdit = (EditText)layout.findViewById(R.id.min_weight_edit);
		minWeightEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length() > 0 ) {
					int value = Integer.valueOf(editable.toString());
					if(currentInstance.getMaxWeight() != null && value > currentInstance.getMaxWeight()) {
						minWeightEdit.setError(getString(R.string.validation_size_min_weight_gt_max_weight));
					}
				}
			}
		});
		minWeightEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					if(minWeightEdit.getText().length() > 0) {
						Integer newValue = Integer.valueOf(minWeightEdit.getText().toString());
						if(!newValue.equals(currentInstance.getMinWeight())) {
							currentInstance.setMinWeight(newValue);
							saveItem();
						}
					}
					else {
						if(currentInstance.getMinWeight() != null) {
							currentInstance.setMinWeight(null);
							saveItem();
						}
					}
				}
			}
		});
	}

	private void initMaxWeightEdit(View layout) {
		maxWeightEdit = (EditText)layout.findViewById(R.id.max_weight_edit);
		maxWeightEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length() > 0 ) {
					int value = Integer.valueOf(editable.toString());
					if(currentInstance.getMinWeight() != null && value < currentInstance.getMinWeight()) {
						maxWeightEdit.setError(getString(R.string.validation_size_max_weight_lt_min_weight));
					}
				}
			}
		});
		maxWeightEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					if(maxWeightEdit.getText().length() > 0) {
						Integer newValue = Integer.valueOf(maxWeightEdit.getText().toString());
						if(!newValue.equals(currentInstance.getMaxWeight())) {
							currentInstance.setMaxWeight(newValue);
							saveItem();
						}
					}
					else {
						if(currentInstance.getMaxWeight() != null) {
							currentInstance.setMaxWeight(null);
							saveItem();
						}
					}
				}
			}
		});
	}

	private void initMinHeightEdit(View layout) {
		minHeightEdit = (EditText)layout.findViewById(R.id.min_height_edit);
		minHeightEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length() > 0 ) {
					int value = Integer.valueOf(editable.toString());
					if(currentInstance.getMaxHeight() != null && value > currentInstance.getMaxHeight()) {
						minHeightEdit.setError(getString(R.string.validation_size_min_height_gt_max_height));
					}
				}
			}
		});
		minHeightEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					if(minHeightEdit.getText().length() > 0) {
						Integer newValue = Integer.valueOf(minHeightEdit.getText().toString());
						if(!newValue.equals(currentInstance.getMinHeight())) {
							currentInstance.setMinHeight(newValue);
							saveItem();
						}
					}
					else {
						if(currentInstance.getMinHeight() != null) {
							currentInstance.setMinHeight(null);
							saveItem();
						}
					}
				}
			}
		});
	}

	private void initMaxHeightEdit(View layout) {
		maxHeightEdit = (EditText)layout.findViewById(R.id.max_height_edit);
		maxHeightEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length() > 0 ) {
					int value = Integer.valueOf(editable.toString());
					if(currentInstance.getMinHeight() != null && value < currentInstance.getMinHeight()) {
						maxHeightEdit.setError(getString(R.string.validation_size_max_height_lt_min_height));
					}
				}
			}
		});
		maxHeightEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					if(maxHeightEdit.getText().length() > 0) {
						Integer newValue = Integer.valueOf(maxHeightEdit.getText().toString());
						if(!newValue.equals(currentInstance.getMaxHeight())) {
							currentInstance.setMaxHeight(newValue);
							saveItem();
						}
					}
					else {
						if(currentInstance.getMaxHeight() != null) {
							currentInstance.setMaxHeight(null);
							saveItem();
						}
					}
				}
			}
		});
	}

	private void initListView(View layout) {
		listView = (ListView) layout.findViewById(R.id.list_view);

		listAdapter = new ThreeFieldListAdapter<>(this.getActivity(), 1, 1, 5, this);
		listView.setAdapter(listAdapter);

		sizeRxHandler.getAll()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Collection<Size>>() {
					@Override
					public void onCompleted() {
						if(listAdapter.getCount() > 0) {
							currentInstance = listAdapter.getItem(0);
							isNew = false;
							listView.setSelection(0);
							listView.setItemChecked(0, true);
							listAdapter.notifyDataSetChanged();
							copyItemToViews();
						}
					}
					@Override
					public void onError(Throwable e) {
						Log.e("SizesFragment", "Exception caught getting all Size instances", e);
						Toast.makeText(SizesFragment.this.getActivity(), R.string.toast_sizes_load_failed,
								Toast.LENGTH_SHORT).show();
					}
					@Override
					public void onNext(Collection<Size> sizes) {
						listAdapter.clear();
						listAdapter.addAll(sizes);
						listAdapter.notifyDataSetChanged();
						String toastString;
						toastString = String.format(getString(R.string.toast_sizes_loaded), sizes.size());
						Toast.makeText(SizesFragment.this.getActivity(), toastString, Toast.LENGTH_SHORT).show();
					}
				});

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(copyViewsToItem()) {
					saveItem();
				}
				currentInstance = (Size) listView.getItemAtPosition(position);
				isNew = false;
				if (currentInstance == null) {
					currentInstance = new Size();
					isNew = true;
				}
				copyItemToViews();
			}
		});
		registerForContextMenu(listView);
	}

	@Override
	public CharSequence getField1Value(Size size) {
		return size.getCode();
	}

	@Override
	public CharSequence getField2Value(Size size) {
		return size.getName();
	}

	@Override
	public CharSequence getField3Value(Size size) {
		return size.getExamples();
	}
}
