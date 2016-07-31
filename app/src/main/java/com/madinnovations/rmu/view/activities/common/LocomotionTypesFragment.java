package com.madinnovations.rmu.view.activities.common;

import android.annotation.SuppressLint;
import android.app.Fragment;
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
import com.madinnovations.rmu.controller.rxhandler.common.LocomotionTypeRxHandler;
import com.madinnovations.rmu.data.entities.common.LocomotionType;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.adapters.common.LocomotionTypeListAdapter;
import com.madinnovations.rmu.view.di.modules.CommonFragmentModule;

import java.util.Collection;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Handles interactions with the UI for locomotion types.
 */
public class LocomotionTypesFragment extends Fragment {
	@Inject
	protected LocomotionTypeRxHandler locomotionTypeRxHandler;
	@Inject
	protected LocomotionTypeListAdapter listAdapter;
	private ListView listView;
	private EditText nameEdit;
	private EditText descriptionEdit;
	private EditText defaultRateEdit;
	private LocomotionType selectedInstance = null;
	private boolean dirty = false;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((CampaignActivity)getActivity()).getActivityComponent().
				newCommonFragmentComponent(new CommonFragmentModule(this)).injectInto(this);

		View layout = inflater.inflate(R.layout.locomotion_types_fragment, container, false);

		initNameEdit(layout);
		initDescriptionEdit(layout);
		initDefaultRateEdit(layout);

		initListView(layout);

		setHasOptionsMenu(true);

		locomotionTypeRxHandler.getAll()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Collection<LocomotionType>>() {
					@Override
					public void onCompleted() {

					}

					@Override
					public void onError(Throwable e) {
						Log.e("LocomotionTypesFragment", "Exception caught getting all LocomotionType instances in onCreateView", e);
						Toast.makeText(LocomotionTypesFragment.this.getActivity(),
									   getString(R.string.toast_locomotion_types_load_failed),
									   Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onNext(Collection<LocomotionType> locomotionTypes) {
						listAdapter.clear();
						listAdapter.addAll(locomotionTypes);
						listAdapter.notifyDataSetChanged();
						String toastString;
						toastString = String.format(getString(R.string.toast_locomotion_types_loaded), locomotionTypes.size());
						Toast.makeText(LocomotionTypesFragment.this.getActivity(), toastString, Toast.LENGTH_SHORT).show();
					}
				});

		return layout;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.locomotion_types_action_bar, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if(id == R.id.action_new_locomotion_type) {
			LocomotionType locomotionType = new LocomotionType();
			locomotionType.setDefaultRate(Short.parseShort(getString(R.string.default_locomotion_type_default_rate)));
			locomotionType.setName(getString(R.string.default_locomotion_type_name));
			locomotionType.setDescription(getString(R.string.default_locomotion_type_description));
			locomotionTypeRxHandler.save(locomotionType)
					.observeOn(AndroidSchedulers.mainThread())
					.subscribeOn(Schedulers.io())
					.subscribe(new Subscriber<LocomotionType>() {
						@Override
						public void onCompleted() {

						}

						@Override
						public void onError(Throwable e) {
							Log.e("LocomotionTypesFragment", "Exception saving LocomotionType in onOptionsItemSelected", e);
							Toast.makeText(LocomotionTypesFragment.this.getActivity(),
									getString(R.string.toast_locomotion_type_save_failed), Toast.LENGTH_SHORT).show();
						}

						@SuppressLint("SetTextI18n")
						@Override
						public void onNext(LocomotionType locomotionType) {
							listAdapter.add(locomotionType);
							defaultRateEdit.setText(Short.toString(locomotionType.getDefaultRate()));
							nameEdit.setText(locomotionType.getName());
							descriptionEdit.setText(locomotionType.getDescription());
							selectedInstance = locomotionType;
						}
					});
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		getActivity().getMenuInflater().inflate(R.menu.locomotion_type_context_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		final LocomotionType locomotionType;

		AdapterView.AdapterContextMenuInfo info =
				(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

		switch (item.getItemId()) {
			case R.id.context_delete_locomotion_type:
				locomotionType = (LocomotionType)listView.getItemAtPosition(info.position);
				if(locomotionType != null) {
					locomotionTypeRxHandler.deleteById(locomotionType.getId())
							.observeOn(AndroidSchedulers.mainThread())
							.subscribe(new Subscriber<Boolean>() {
								@Override
								public void onCompleted() {

								}

								@Override
								public void onError(Throwable e) {
									Log.e("LocomotionTypesFragment", "Exception when deleting: " + locomotionType, e);
									String toastString = getString(R.string.toast_locomotion_type_delete_failed);
									Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
								}

								@Override
								public void onNext(Boolean success) {
									String toastString;

									if(success) {
										listAdapter.remove(locomotionType);
										listAdapter.notifyDataSetChanged();
										toastString = getString(R.string.toast_locomotion_type_deleted);
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

	private void initDefaultRateEdit(View layout) {
		defaultRateEdit = (EditText)layout.findViewById(R.id.default_rate_edit);
		defaultRateEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length() == 0 && defaultRateEdit != null) {
					defaultRateEdit.setError(getString(R.string.validation_default_rate_required));
				}
				else if (selectedInstance != null && Short.parseShort(editable.toString()) != selectedInstance.getDefaultRate()) {
					dirty = true;
				}
			}
		});
		defaultRateEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					final short newDefaultRate = Short.parseShort(defaultRateEdit.getText().toString());
					if (selectedInstance != null && newDefaultRate != selectedInstance.getDefaultRate()) {
						dirty = true;
						selectedInstance.setDefaultRate(newDefaultRate);
						locomotionTypeRxHandler.save(selectedInstance)
							.observeOn(AndroidSchedulers.mainThread())
							.subscribe(new Subscriber<LocomotionType>() {
								@Override
								public void onCompleted() {
								}
								@Override
								public void onError(Throwable e) {
									Log.e("LocomotionTypesFragment", "Exception saving LocomotionType in initDefaultRateEdit", e);
									String toastString = getString(R.string.toast_locomotion_type_save_failed);
									Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
								}
								@Override
								public void onNext(LocomotionType locomotionType) {
									onSaved(locomotionType);
								}
							});
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
				else if (selectedInstance != null && !editable.toString().equals(selectedInstance.getName())) {
					dirty = true;
				}
			}
		});
		nameEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					final String newName = nameEdit.getText().toString();
					if (selectedInstance != null && !newName.equals(selectedInstance.getName())) {
						dirty = true;
						selectedInstance.setName(newName);
						locomotionTypeRxHandler.save(selectedInstance)
							.observeOn(AndroidSchedulers.mainThread())
							.subscribe(new Subscriber<LocomotionType>() {
								@Override
								public void onCompleted() {
								}
								@Override
								public void onError(Throwable e) {
									Log.e("LocomotionTypesFragment", "Exception saving LocomotionType in initNameEdit", e);
									String toastString = getString(R.string.toast_locomotion_type_save_failed);
									Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
								}
								@Override
								public void onNext(LocomotionType locomotionType) {
									onSaved(locomotionType);
								}
							});
					}
				}
			}
		});
	}

	private void initDescriptionEdit(View layout) {
		descriptionEdit = (EditText)layout.findViewById(R.id.description_edit);
		descriptionEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length() == 0 && descriptionEdit != null) {
					descriptionEdit.setError(getString(R.string.validation_description_required));
				}
				else if (selectedInstance != null && !editable.toString().equals(selectedInstance.getDescription())) {
					dirty = true;
				}
			}
		});
		descriptionEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					final String newDescription = descriptionEdit.getText().toString();
					if (selectedInstance != null && !newDescription.equals(selectedInstance.getDescription())) {
						dirty = true;
						selectedInstance.setDescription(newDescription);
						locomotionTypeRxHandler.save(selectedInstance)
							.observeOn(AndroidSchedulers.mainThread())
							.subscribe(new Subscriber<LocomotionType>() {
								@Override
								public void onCompleted() {
								}
								@Override
								public void onError(Throwable e) {
									Log.e("LocomotionTypesFragment",
										  "Exception saving LocomotionType in initDescriptionEdit", e);
									String toastString = getString(R.string.toast_locomotion_type_save_failed);
									Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
								}
								@Override
								public void onNext(LocomotionType locomotionType) {
									onSaved(locomotionType);
								}
							});
					}
				}
			}
		});
	}

	private void onSaved(LocomotionType locomotionType) {
		if(getActivity() == null) {
			return;
		}
		String toastString;
		toastString = getString(R.string.toast_locomotion_type_saved);
		Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();

		int position = listAdapter.getPosition(locomotionType);
		LinearLayout v = (LinearLayout) listView.getChildAt(position - listView.getFirstVisiblePosition());
		if (v != null) {
			TextView textView = (TextView) v.findViewById(R.id.name_view);
			if (textView != null) {
				textView.setText(locomotionType.getName());
			}
			textView = (TextView) v.findViewById(R.id.description_view);
			if (textView != null) {
				textView.setText(locomotionType.getDescription());
			}
		}
	}

	private void initListView(View layout) {
		listView = (ListView) layout.findViewById(R.id.list_view);

		listView.setAdapter(listAdapter);

		// Clicking a row in the listView will send the user to the edit world activity
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@SuppressLint("SetTextI18n")
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(dirty && selectedInstance != null) {
					selectedInstance.setDefaultRate(Short.parseShort(defaultRateEdit.getText().toString()));
					selectedInstance.setName(nameEdit.getText().toString());
					selectedInstance.setDescription(descriptionEdit.getText().toString());
					locomotionTypeRxHandler.save(selectedInstance)
							.observeOn(AndroidSchedulers.mainThread())
							.subscribe(new Subscriber<LocomotionType>() {
								@Override
								public void onCompleted() {
								}
								@Override
								public void onError(Throwable e) {
									Log.e("LocomotionTypesFragment", "Exception saving LocomotionType in initListView", e);
									String toastString = getString(R.string.toast_locomotion_type_save_failed);
									Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
								}
								@Override
								public void onNext(LocomotionType locomotionType) {
									onSaved(locomotionType);
								}
							});
					dirty = false;
				}

				selectedInstance = (LocomotionType) listView.getItemAtPosition(position);
				if (selectedInstance != null) {
					defaultRateEdit.setText(Short.toString(selectedInstance.getDefaultRate()));
					nameEdit.setText(selectedInstance.getName());
					descriptionEdit.setText(selectedInstance.getDescription());
				}
			}
		});
		registerForContextMenu(listView);
	}
}
