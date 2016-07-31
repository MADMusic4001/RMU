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
import android.content.res.Resources;
import android.os.Bundle;
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
import com.madinnovations.rmu.view.adapters.common.SizeListAdapter;
import com.madinnovations.rmu.view.di.modules.CommonFragmentModule;

import java.util.Collection;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Handles interactions with the UI for sizes.
 */
public class SizesFragment extends Fragment {
	@Inject
	protected SizeRxHandler   sizeRxHandler;
	@Inject
	protected SizeListAdapter listAdapter;
	private   ListView        listView;
	private   EditText        codeEdit;
	private   EditText        nameEdit;
	private   EditText        examplesEdit;
	private   EditText        maxHeightEdit;
	private   EditText        maxWeightEdit;
	private   EditText        minHeightEdit;
	private   EditText        minWeightEdit;
	private   Size            currentInstance = null;
	private   boolean         dirty = false;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((CampaignActivity)getActivity()).getActivityComponent().
				newCommonFragmentComponent(new CommonFragmentModule(this)).injectInto(this);

		View layout = inflater.inflate(R.layout.sizes_fragment, container, false);

		initCodeEdit(layout);
		initNameEdit(layout);
		initExamplesEdit(layout);
		initMaxHeightEdit(layout);
		initMaxWeightEdit(layout);
		initMinHeightEdit(layout);
		initMinWeightEdit(layout);
		initListView(layout);

		setHasOptionsMenu(true);

		sizeRxHandler.getAll()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Collection<Size>>() {
					@Override
					public void onCompleted() {

					}

					@Override
					public void onError(Throwable e) {
						Log.e("SizesFragment", "Exception caught getting all Size instances in onCreateView", e);
						Toast.makeText(SizesFragment.this.getActivity(), getString(R.string.toast_sizes_load_failed),
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

		return layout;
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
			Size size = new Size();
			size.setCode(getString(R.string.default_size_code));
			size.setName(getString(R.string.default_size_name));
			size.setExamples(getString(R.string.default_size_examples));
			Resources resources = getActivity().getResources();
			size.setMaxHeight(resources.getInteger(R.integer.default_size_max_height));
			size.setMaxWeight(resources.getInteger(R.integer.default_size_max_weight));
			size.setMinHeight(resources.getInteger(R.integer.default_size_min_height));
			size.setMinWeight(resources.getInteger(R.integer.default_size_min_weight));
			sizeRxHandler.save(size)
					.observeOn(AndroidSchedulers.mainThread())
					.subscribeOn(Schedulers.io())
					.subscribe(new Subscriber<Size>() {
						@Override
						public void onCompleted() {

						}

						@Override
						public void onError(Throwable e) {
							Log.e("SizesFragment", "Exception saving new Size in onOptionsItemSelected", e);
						}

						@Override
						public void onNext(Size savedSize) {
							listAdapter.add(savedSize);
							codeEdit.setText(savedSize.getCode());
							nameEdit.setText(savedSize.getName());
							examplesEdit.setText(savedSize.getExamples());
							maxHeightEdit.setText(String.valueOf(savedSize.getMaxHeight()));
							maxWeightEdit.setText(String.valueOf(savedSize.getMaxWeight()));
							minHeightEdit.setText(String.valueOf(savedSize.getMinHeight()));
							minWeightEdit.setText(String.valueOf(savedSize.getMinWeight()));
							currentInstance = savedSize;
						}
					});
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
			case R.id.context_delete_size:
				size = (Size)listView.getItemAtPosition(info.position);
				if(size != null) {
					sizeRxHandler.deleteById(size.getId())
							.observeOn(AndroidSchedulers.mainThread())
							.subscribe(new Subscriber<Boolean>() {
								@Override
								public void onCompleted() {

								}

								@Override
								public void onError(Throwable e) {
									Log.e("SizeFragment", "Exception when deleting: " + size, e);
									String toastString = getString(R.string.toast_size_delete_failed);
									Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
								}

								@Override
								public void onNext(Boolean success) {
									String toastString;

									if(success) {
										listAdapter.remove(size);
										listAdapter.notifyDataSetChanged();
										toastString = getString(R.string.toast_size_deleted);
										Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
									}
								}
							});
					return true;
				}
				else {
					return false;
				}
			default:
				return super.onContextItemSelected(item);
		}
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
					codeEdit.setError(getString(R.string.validation_abbreviation_required));
				}
				else if (currentInstance != null && !editable.toString().equals(currentInstance.getCode())) {
					dirty = true;
				}
			}
		});
		codeEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					final String newCode = codeEdit.getText().toString();
					if (currentInstance != null && !newCode.equals(currentInstance.getCode())) {
						dirty = true;
						currentInstance.setCode(newCode);
						save();
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
					nameEdit.setError(getString(R.string.validation_name_required));
				}
				else if (currentInstance != null && !editable.toString().equals(currentInstance.getName())) {
					dirty = true;
				}
			}
		});
		nameEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					final String newName = nameEdit.getText().toString();
					if (currentInstance != null && !newName.equals(currentInstance.getName())) {
						dirty = true;
						currentInstance.setName(newName);
						save();
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
					examplesEdit.setError(getString(R.string.validation_description_required));
				}
				else if (currentInstance != null && !editable.toString().equals(currentInstance.getExamples())) {
					dirty = true;
				}
			}
		});
		examplesEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					final String newExamples = examplesEdit.getText().toString();
					if (currentInstance != null && !newExamples.equals(currentInstance.getExamples())) {
						dirty = true;
						currentInstance.setExamples(newExamples);
						save();
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
				if (currentInstance != null && ((editable.length() == 0 && currentInstance.getMaxHeight() != null) ||
					(editable.length() > 0 && !Integer.valueOf(editable.toString()).equals(currentInstance.getMaxHeight())))) {
					dirty = true;
				}
			}
		});
		maxHeightEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					final Integer newMaxHeight;
					if(maxHeightEdit.getText() != null && maxHeightEdit.getText().length() > 0) {
						newMaxHeight = Integer.valueOf(maxHeightEdit.getText().toString());
					}
					else {
						newMaxHeight = null;
					}
					if (currentInstance != null && ((currentInstance.getMaxHeight() == null && newMaxHeight != null) ||
						(currentInstance.getMaxHeight() != null && currentInstance.getMaxHeight().equals(newMaxHeight)))) {
						dirty = true;
						currentInstance.setMaxHeight(newMaxHeight);
						Log.d("SizesFragment", "Saving size");
						save();
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
				if (currentInstance != null && ((editable.length() == 0 && currentInstance.getMaxWeight() != null) ||
					(editable.length() > 0 && !Integer.valueOf(editable.toString()).equals(currentInstance.getMinWeight())))) {
					dirty = true;
				}
			}
		});
		maxWeightEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					final Integer newMaxWeight;
					if(maxWeightEdit.getText() != null && maxWeightEdit.getText().length() > 0) {
						newMaxWeight = Integer.valueOf(maxWeightEdit.getText().toString());
					}
					else {
						newMaxWeight = null;
					}
					if (currentInstance != null && ((currentInstance.getMaxWeight() == null && newMaxWeight != null) ||
							(currentInstance.getMaxWeight() != null && currentInstance.getMaxWeight().equals(newMaxWeight)))) {
						dirty = true;
						currentInstance.setMaxWeight(newMaxWeight);
						save();
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
				if (currentInstance != null && ((editable.length() == 0 && currentInstance.getMinHeight() != null) ||
					(editable.length() > 0 && !Integer.valueOf(editable.toString()).equals(currentInstance.getMinHeight())))) {
					dirty = true;
				}
			}
		});
		minHeightEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					final Integer newMinHeight;
					if(minHeightEdit.getText() != null && minHeightEdit.getText().length() > 0) {
						newMinHeight = Integer.valueOf(minHeightEdit.getText().toString());
					}
					else {
						newMinHeight = null;
					}
					if (currentInstance != null && ((currentInstance.getMinHeight() == null && newMinHeight != null) ||
							(currentInstance.getMinHeight() != null && currentInstance.getMinHeight().equals(newMinHeight)))) {
						dirty = true;
						currentInstance.setMinHeight(newMinHeight);
						save();
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
				if (currentInstance != null && ((editable.length() == 0 && currentInstance.getMinWeight() != null) ||
					(editable.length() > 0 && !Integer.valueOf(editable.toString()).equals(currentInstance.getMinWeight())))) {
					dirty = true;
				}
			}
		});
		minWeightEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					final Integer newMinWeight;
					if(minWeightEdit.getText() != null && minWeightEdit.getText().length() > 0) {
						newMinWeight = Integer.valueOf(minWeightEdit.getText().toString());
					}
					else {
						newMinWeight = null;
					}
					if (currentInstance != null && ((currentInstance.getMinWeight() == null && newMinWeight != null) ||
							(currentInstance.getMinWeight() != null && currentInstance.getMinWeight().equals(newMinWeight)))) {
						dirty = true;
						currentInstance.setMinWeight(newMinWeight);
						save();
					}
				}
			}
		});
	}

	private void save() {
		if(currentInstance.isValid()) {
			sizeRxHandler.save(currentInstance)
					.observeOn(AndroidSchedulers.mainThread())
					.subscribe(new Subscriber<Size>() {
						@Override
						public void onCompleted() {
						}

						@Override
						public void onError(Throwable e) {
							Log.e("SizesFragment", "Exception saving Size", e);
							String toastString = getString(R.string.toast_size_save_failed);
							Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
						}

						@Override
						public void onNext(Size savedSize) {
							onSaved(savedSize);
						}
					});
		}
	}

	private void onSaved(Size size) {
		if(getActivity() == null) {
			return;
		}
		String toastString;
		toastString = getString(R.string.toast_size_saved);
		Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();

		int position = listAdapter.getPosition(size);
		LinearLayout v = (LinearLayout) listView.getChildAt(position - listView.getFirstVisiblePosition());
		if (v != null) {
			TextView textView = (TextView) v.findViewById(R.id.code_view);
			if (textView != null) {
				textView.setText(size.getCode());
			}
			textView = (TextView) v.findViewById(R.id.name_view);
			if (textView != null) {
				textView.setText(size.getName());
			}
			textView = (TextView) v.findViewById(R.id.examples_view);
			if (textView != null) {
				textView.setText(size.getExamples());
			}
		}
	}

	private void initListView(View layout) {
		listView = (ListView) layout.findViewById(R.id.list_view);

		listView.setAdapter(listAdapter);

		// Clicking a row in the listView will send the user to the edit world activity
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(dirty && currentInstance != null) {
					currentInstance.setCode(codeEdit.getText().toString());
					currentInstance.setName(nameEdit.getText().toString());
					currentInstance.setExamples(examplesEdit.getText().toString());
					if(maxHeightEdit.getText().length() > 0) {
						currentInstance.setMaxHeight(Integer.valueOf(maxHeightEdit.getText().toString()));
					}
					else {
						currentInstance.setMaxHeight(null);
					}
					if(maxWeightEdit.getText().length() > 0) {
						currentInstance.setMaxWeight(Integer.valueOf(maxWeightEdit.getText().toString()));
					}
					else {
						currentInstance.setMaxWeight(null);
					}
					if(minHeightEdit.getText().length() > 0) {
						currentInstance.setMinHeight(Integer.valueOf(minHeightEdit.getText().toString()));
					}
					else {
						currentInstance.setMinHeight(null);
					}
					if(minWeightEdit.getText().length() > 0) {
						currentInstance.setMinWeight(Integer.valueOf(minWeightEdit.getText().toString()));
					}
					else {
						currentInstance.setMinWeight(null);
					}
					sizeRxHandler.save(currentInstance)
							.observeOn(AndroidSchedulers.mainThread())
							.subscribe(new Subscriber<Size>() {
								@Override
								public void onCompleted() {
								}
								@Override
								public void onError(Throwable e) {
									Log.e("SizesFragment", "Exception saving new Size in initListView", e);
									String toastString = getString(R.string.toast_size_save_failed);
									Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
								}
								@Override
								public void onNext(Size savedSize) {
									onSaved(savedSize);
								}
							});
					dirty = false;
				}

				currentInstance = (Size) listView.getItemAtPosition(position);
				if (currentInstance != null) {
					codeEdit.setText(currentInstance.getCode());
					nameEdit.setText(currentInstance.getName());
					examplesEdit.setText(currentInstance.getExamples());
					if(currentInstance.getMaxHeight() != null) {
						maxHeightEdit.setText(String.valueOf(currentInstance.getMaxHeight()));
					}
					else {
						maxHeightEdit.setText("");
					}
					if(currentInstance.getMaxWeight() != null) {
						maxWeightEdit.setText(String.valueOf(currentInstance.getMaxWeight()));
					}
					else {
						maxWeightEdit.setText("");
					}
					if(currentInstance.getMinHeight() != null) {
						minHeightEdit.setText(String.valueOf(currentInstance.getMinHeight()));
					}
					else {
						minHeightEdit.setText("");
					}
					if(currentInstance.getMinWeight() != null) {
						minWeightEdit.setText(String.valueOf(currentInstance.getMinWeight()));
					}
					else {
						minWeightEdit.setText("");
					}
				}
			}
		});
		registerForContextMenu(listView);
	}
}
