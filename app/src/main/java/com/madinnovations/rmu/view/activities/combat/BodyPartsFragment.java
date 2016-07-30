package com.madinnovations.rmu.view.activities.combat;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
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
import com.madinnovations.rmu.controller.rxhandler.combat.BodyPartRxHandler;
import com.madinnovations.rmu.data.entities.combat.BodyPart;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.adapters.BodyPartListAdapter;
import com.madinnovations.rmu.view.di.modules.CombatFragmentModule;

import java.util.Collection;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Handles interactions with the UI for body parts.
 */
public class BodyPartsFragment extends Fragment {
	@Inject
	protected BodyPartRxHandler   bodyPartRxHandler;
	@Inject
	protected BodyPartListAdapter listAdapter;
	private ListView              listView;
	private EditText              nameEdit;
	private EditText              descriptionEdit;
	private BodyPart selectedInstance = null;
	private boolean dirty = false;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((CampaignActivity)getActivity()).getActivityComponent().
				newCombatFragmentComponent(new CombatFragmentModule(this)).injectInto(this);

		View layout = inflater.inflate(R.layout.body_parts_fragment, container, false);

		initNameEdit(layout);
		initDescriptionEdit(layout);
		initListView(layout);

		setHasOptionsMenu(true);

		bodyPartRxHandler.getAll()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Collection<BodyPart>>() {
					@Override
					public void onCompleted() {

					}

					@Override
					public void onError(Throwable e) {
						Log.e("BodyPartsFragment", "Exception caught getting all CriticalCode instances in onCreateView", e);
						Toast.makeText(BodyPartsFragment.this.getActivity(),
									   getString(R.string.toast_body_parts_load_failed),
									   Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onNext(Collection<BodyPart> bodyParts) {
						listAdapter.clear();
						listAdapter.addAll(bodyParts);
						listAdapter.notifyDataSetChanged();
						String toastString;
						toastString = String.format(getString(R.string.toast_body_parts_loaded), bodyParts.size());
						Toast.makeText(BodyPartsFragment.this.getActivity(), toastString, Toast.LENGTH_SHORT).show();
					}
				});

		return layout;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.body_parts_action_bar, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if(id == R.id.action_new_body_part) {
			BodyPart bodyPart = new BodyPart();
			bodyPart.setName(getString(R.string.default_body_part_name));
			bodyPart.setDescription(getString(R.string.default_body_part_description));
			bodyPartRxHandler.save(bodyPart)
					.observeOn(AndroidSchedulers.mainThread())
					.subscribeOn(Schedulers.io())
					.subscribe(new Subscriber<BodyPart>() {
						@Override
						public void onCompleted() {

						}

						@Override
						public void onError(Throwable e) {
							Log.e("BodyPartsFragment", "Exception saving new BodyPart in onOptionsItemSelected", e);
						}

						@Override
						public void onNext(BodyPart savedBodyPart) {
							listAdapter.add(savedBodyPart);
							nameEdit.setText(savedBodyPart.getName());
							descriptionEdit.setText(savedBodyPart.getDescription());
							selectedInstance = savedBodyPart;
						}
					});
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		getActivity().getMenuInflater().inflate(R.menu.body_part_context_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		final BodyPart bodyPart;

		AdapterView.AdapterContextMenuInfo info =
				(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

		switch (item.getItemId()) {
			case R.id.context_delete_body_part:
				bodyPart = (BodyPart)listView.getItemAtPosition(info.position);
				if(bodyPart != null) {
					bodyPartRxHandler.deleteById(bodyPart.getId())
							.observeOn(AndroidSchedulers.mainThread())
							.subscribe(new Subscriber<Boolean>() {
								@Override
								public void onCompleted() {

								}

								@Override
								public void onError(Throwable e) {
									Log.e("BodyPartFragment", "Exception when deleting: " + bodyPart, e);
									String toastString = getString(R.string.toast_body_part_delete_failed);
									Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
								}

								@Override
								public void onNext(Boolean success) {
									String toastString;

									if(success) {
										listAdapter.remove(bodyPart);
										listAdapter.notifyDataSetChanged();
										toastString = getString(R.string.toast_body_part_deleted);
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
						selectedInstance.setName(newName);
						bodyPartRxHandler.save(selectedInstance)
								.observeOn(AndroidSchedulers.mainThread())
								.subscribe(new Subscriber<BodyPart>() {
									@Override
									public void onCompleted() {
									}
									@Override
									public void onError(Throwable e) {
										Log.e("BodyPartsFragment", "Exception saving new BodyPart in initNameEdit", e);
										String toastString = getString(R.string.toast_body_part_save_failed);
										Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
									}
									@Override
									public void onNext(BodyPart savedBodyPart) {
										onSaved(savedBodyPart);
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
				dirty = true;
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
						selectedInstance.setDescription(newDescription);
						bodyPartRxHandler.save(selectedInstance)
								.observeOn(AndroidSchedulers.mainThread())
								.subscribe(new Subscriber<BodyPart>() {
									@Override
									public void onCompleted() {
									}
									@Override
									public void onError(Throwable e) {
										Log.e("BodyPartsFragment", "Exception saving new BodyPart in initDescriptionEdit", e);
										String toastString = getString(R.string.toast_body_part_save_failed);
										Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
									}
									@Override
									public void onNext(BodyPart savedBodyPart) {
										onSaved(savedBodyPart);
									}
								});
					}
				}
			}
		});
	}

	private void onSaved(BodyPart bodyPart) {
		if(getActivity() == null) {
			return;
		}

		String toastString;
		toastString = getString(R.string.toast_body_part_saved);
		Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();

		int position = listAdapter.getPosition(bodyPart);
		// Add 1 for the header row
		LinearLayout v = (LinearLayout) listView.getChildAt(position - listView.getFirstVisiblePosition() + 1);
		if (v != null) {
			TextView textView = (TextView) v.findViewById(R.id.name_view);
			if (textView != null) {
				textView.setText(bodyPart.getName());
			}
			textView = (TextView) v.findViewById(R.id.description_view);
			if (textView != null) {
				textView.setText(bodyPart.getDescription());
			}
		}
	}

	private void initListView(View layout) {
		View headerView;

		listView = (ListView) layout.findViewById(R.id.list_view);
		headerView = getActivity().getLayoutInflater().inflate(
				R.layout.name_description_header, listView, false);
		listView.addHeaderView(headerView);

		listView.setAdapter(listAdapter);

		// Clicking a row in the listView will send the user to the edit world activity
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(dirty && selectedInstance != null) {
					selectedInstance.setName(nameEdit.getText().toString());
					selectedInstance.setDescription(descriptionEdit.getText().toString());
					bodyPartRxHandler.save(selectedInstance)
							.observeOn(AndroidSchedulers.mainThread())
							.subscribe(new Subscriber<BodyPart>() {
								@Override
								public void onCompleted() {
								}
								@Override
								public void onError(Throwable e) {
									Log.e("BodyPartsFragment", "Exception saving new BodyPart in initListView", e);
									String toastString = getString(R.string.toast_body_part_save_failed);
									Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
								}
								@Override
								public void onNext(BodyPart savedBodyPart) {
									onSaved(savedBodyPart);
								}
							});
				}

				selectedInstance = (BodyPart) listView.getItemAtPosition(position);
				if (selectedInstance != null) {
					nameEdit.setText(selectedInstance.getName());
					descriptionEdit.setText(selectedInstance.getDescription());
				}
			}
		});
		registerForContextMenu(listView);
	}
}
