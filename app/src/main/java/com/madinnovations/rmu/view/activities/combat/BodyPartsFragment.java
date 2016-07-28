package com.madinnovations.rmu.view.activities.combat;

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
import com.madinnovations.rmu.controller.rxhandler.combat.BodyPartRxHandler;
import com.madinnovations.rmu.data.entities.combat.BodyPart;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.adapters.BodyPartListAdapter;
import com.madinnovations.rmu.view.di.modules.FragmentModule;

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
	protected BodyPartRxHandler talentCategoryRxHandler;
	@Inject
	protected BodyPartListAdapter listAdapter;
	private ListView listView;
	private EditText nameEdit;
	private EditText descriptionEdit;
	private BodyPart selectedInstance = null;
	private boolean dirty = false;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((CampaignActivity)getActivity()).getActivityComponent().
				newFragmentComponent(new FragmentModule(this)).injectInto(this);

		View layout = inflater.inflate(R.layout.body_parts_fragment, container, false);

		initNameEdit(layout);
		nameEdit = (EditText)layout.findViewById(R.id.name_edit);
		initDescriptionEdit(layout);
		descriptionEdit = (EditText)layout.findViewById(R.id.description_edit);

		initListView(layout);

		talentCategoryRxHandler.getAll()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Collection<BodyPart>>() {
					@Override
					public void onCompleted() {

					}

					@Override
					public void onError(Throwable e) {
						Log.e("BodyPartsFragment", "Exception occurred loading talent categories", e);
						Toast.makeText(BodyPartsFragment.this.getActivity(),
								getString(R.string.toast_body_parts_load_failed),
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onNext(Collection<BodyPart> bodyParts) {
						String toastString;

						toastString = String.format(getString(R.string.toast_body_parts_loaded), bodyParts.size());
						listAdapter.clear();
						listAdapter.addAll(bodyParts);
						listAdapter.notifyDataSetChanged();
						Toast.makeText(BodyPartsFragment.this.getActivity(), toastString, Toast.LENGTH_SHORT).show();
					}
				});

		setHasOptionsMenu(true);
		return layout;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		Log.e("BodyPartFragment", "Creating options menu");
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.body_parts_action_bar, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if(id == R.id.action_new_body_part) {
			BodyPart talentCategory = new BodyPart();
			talentCategory.setName(getString(R.string.default_body_part_name));
			talentCategory.setDescription(getString(R.string.default_body_part_description));
			talentCategoryRxHandler.save(talentCategory)
					.observeOn(AndroidSchedulers.mainThread())
					.subscribeOn(Schedulers.io())
					.subscribe(new Subscriber<BodyPart>() {
						@Override
						public void onCompleted() {

						}

						@Override
						public void onError(Throwable e) {

						}

						@Override
						public void onNext(BodyPart talentCategory) {
							listAdapter.add(talentCategory);
							nameEdit.setText(talentCategory.getName());
							descriptionEdit.setText(talentCategory.getDescription());
							selectedInstance = talentCategory;
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
		final BodyPart talentCategory;

		AdapterView.AdapterContextMenuInfo info =
				(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

		switch (item.getItemId()) {
			case R.id.context_delete_body_part:
				talentCategory = (BodyPart)listView.getItemAtPosition(info.position);
				if(talentCategory != null) {
					talentCategoryRxHandler.deleteById(talentCategory.getId())
							.observeOn(AndroidSchedulers.mainThread())
							.subscribe(new Subscriber<Boolean>() {
								@Override
								public void onCompleted() {

								}

								@Override
								public void onError(Throwable e) {
									Log.e("BodyPartFragment", "Exception thrown when deleting: " + talentCategory, e);
									String toastString = getString(R.string.toast_body_part_delete_failed);
									Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
								}

								@Override
								public void onNext(Boolean success) {
									String toastString;

									if(success) {
										listAdapter.remove(talentCategory);
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
				if (editable.length() == 0) {
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
						talentCategoryRxHandler.save(selectedInstance)
								.observeOn(AndroidSchedulers.mainThread())
								.subscribe(new Subscriber<BodyPart>() {
									@Override
									public void onCompleted() {
									}
									@Override
									public void onError(Throwable e) {
										String toastString = getString(R.string.toast_body_part_save_failed);
										Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
										Log.e("BodyPartFragment", "Save failed for: " + selectedInstance, e);
									}
									@Override
									public void onNext(BodyPart talentCategory) {
										onSaved(talentCategory);
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
				if (editable.length() == 0) {
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
						talentCategoryRxHandler.save(selectedInstance)
								.observeOn(AndroidSchedulers.mainThread())
								.subscribe(new Subscriber<BodyPart>() {
									@Override
									public void onCompleted() {
									}
									@Override
									public void onError(Throwable e) {
										String toastString = getString(R.string.toast_body_part_save_failed);
										Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
									}
									@Override
									public void onNext(BodyPart talentCategory) {
										onSaved(talentCategory);
									}
								});
					}
				}
			}
		});
	}

	private void onSaved(BodyPart talentCategory) {
		if(getActivity() == null) {
			return;
		}

		String toastString;
		toastString = getString(R.string.toast_body_part_saved);
		Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();

		int position = listAdapter.getPosition(talentCategory);
		// Add 1 for the header row
		LinearLayout v = (LinearLayout) listView.getChildAt(position - listView.getFirstVisiblePosition() + 1);
		if (v != null) {
			TextView textView = (TextView) v.findViewById(R.id.name_view);
			if (textView != null) {
				textView.setText(talentCategory.getName());
			}
			textView = (TextView) v.findViewById(R.id.description_view);
			if (textView != null) {
				textView.setText(talentCategory.getDescription());
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
					talentCategoryRxHandler.save(selectedInstance)
							.observeOn(AndroidSchedulers.mainThread())
							.subscribe(new Subscriber<BodyPart>() {
								@Override
								public void onCompleted() {
								}
								@Override
								public void onError(Throwable e) {
									String toastString = getString(R.string.toast_body_part_save_failed);
									Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
								}
								@Override
								public void onNext(BodyPart talentCategory) {
									onSaved(talentCategory);
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
