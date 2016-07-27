package com.madinnovations.rmu.view.activities.common;

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
import com.madinnovations.rmu.controller.rxhandler.common.StatRxHandler;
import com.madinnovations.rmu.data.entities.common.Stat;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.adapters.StatListAdapter;
import com.madinnovations.rmu.view.di.modules.FragmentModule;

import java.util.Collection;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by madanle on 7/27/16.
 */
public class StatsFragment extends Fragment {
	@Inject
	protected StatRxHandler statRxHandler;
	@Inject
	protected StatListAdapter listAdapter;
	private ListView listView;
	private EditText abbreviationEdit;
	private EditText nameEdit;
	private EditText descriptionEdit;
	private Stat selectedInstance = null;
	private boolean dirty = false;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((CampaignActivity)getActivity()).getActivityComponent().
				newFragmentComponent(new FragmentModule(this)).injectInto(this);

		View layout = inflater.inflate(R.layout.stats_fragment, container, false);

		initAbbreviationEdit(layout);
		initNameEdit(layout);
		initDescriptionEdit(layout);

		initListView(layout);

		statRxHandler.getAll()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Collection<Stat>>() {
					@Override
					public void onCompleted() {

					}

					@Override
					public void onError(Throwable e) {
						Log.e("StatFragment", "Exception occured loading talent categories", e);
					}

					@Override
					public void onNext(Collection<Stat> talentCategories) {
						String toastString;

						toastString = String.format(getString(R.string.toast_talent_categories_loaded), talentCategories.size());
						listAdapter.clear();
						listAdapter.addAll(talentCategories);
						listAdapter.notifyDataSetChanged();
						Toast.makeText(StatsFragment.this.getActivity(), toastString, Toast.LENGTH_SHORT).show();
					}
				});

		setHasOptionsMenu(true);
		return layout;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		Log.e("StatFragment", "Creating options menu");
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.stats_action_bar, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if(id == R.id.action_new_stat) {
			Stat stat = new Stat();
			stat.setName(getString(R.string.default_stat_name));
			stat.setDescription(getString(R.string.default_stat_description));
			statRxHandler.save(stat)
					.observeOn(AndroidSchedulers.mainThread())
					.subscribeOn(Schedulers.io())
					.subscribe(new Subscriber<Stat>() {
						@Override
						public void onCompleted() {

						}

						@Override
						public void onError(Throwable e) {

						}

						@Override
						public void onNext(Stat stat) {
							listAdapter.add(stat);
							nameEdit.setText(stat.getName());
							descriptionEdit.setText(stat.getDescription());
							selectedInstance = stat;
						}
					});
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		getActivity().getMenuInflater().inflate(R.menu.stats_context_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		final Stat stat;

		AdapterView.AdapterContextMenuInfo info =
				(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

		switch (item.getItemId()) {
			case R.id.context_delete_stat:
				stat = (Stat)listView.getItemAtPosition(info.position);
				if(stat != null) {
					statRxHandler.deleteById(stat.getId())
							.observeOn(AndroidSchedulers.mainThread())
							.subscribe(new Subscriber<Boolean>() {
								@Override
								public void onCompleted() {

								}

								@Override
								public void onError(Throwable e) {
									Log.e("StatFragment", "Exception thrown when deleting: " + stat, e);
									String toastString = getString(R.string.toast_stat_delete_failed);
									Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
								}

								@Override
								public void onNext(Boolean success) {
									String toastString;

									if(success) {
										int position = listAdapter.getPosition(stat);
										listAdapter.remove(stat);
										listAdapter.notifyDataSetChanged();
										toastString = getString(R.string.toast_stat_deleted);
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

	private void initAbbreviationEdit(View layout) {
		abbreviationEdit = (EditText)layout.findViewById(R.id.abbreviation_edit);
		abbreviationEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				dirty = true;
				if (editable.length() == 0) {
					abbreviationEdit.setError(getString(R.string.validation_name_required));
				}
			}
		});
		nameEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					final String newAbbreviation = nameEdit.getText().toString();
					if (selectedInstance != null && !selectedInstance.getAbbreviation().equals(newAbbreviation)) {
						selectedInstance.setAbbreviation(newAbbreviation);
						statRxHandler.save(selectedInstance)
								.observeOn(AndroidSchedulers.mainThread())
								.subscribe(new Subscriber<Stat>() {
									@Override
									public void onCompleted() {
									}
									@Override
									public void onError(Throwable e) {
										String toastString = getString(R.string.toast_stat_save_failed);
										Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
										Log.e("StatFragment", "Save failed for: " + selectedInstance, e);
									}
									@Override
									public void onNext(Stat stat) {
										onSaved(stat);
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
				dirty = true;
				if (editable.length() == 0) {
					nameEdit.setError(getString(R.string.validation_name_required));
				}
			}
		});
		nameEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					final String newName = nameEdit.getText().toString();
					if (selectedInstance != null && !selectedInstance.getName().equals(newName)) {
						selectedInstance.setName(newName);
						statRxHandler.save(selectedInstance)
								.observeOn(AndroidSchedulers.mainThread())
								.subscribe(new Subscriber<Stat>() {
									@Override
									public void onCompleted() {
									}
									@Override
									public void onError(Throwable e) {
										String toastString = getString(R.string.toast_stat_save_failed);
										Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
										Log.e("StatFragment", "Save failed for: " + selectedInstance, e);
									}
									@Override
									public void onNext(Stat stat) {
										onSaved(stat);
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
			}
		});
		descriptionEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					final String newDescription = descriptionEdit.getText().toString();
					if (selectedInstance != null && !selectedInstance.getDescription().equals(newDescription)) {
						selectedInstance.setDescription(newDescription);
						statRxHandler.save(selectedInstance)
								.observeOn(AndroidSchedulers.mainThread())
								.subscribe(new Subscriber<Stat>() {
									@Override
									public void onCompleted() {
									}
									@Override
									public void onError(Throwable e) {
										String toastString = getString(R.string.toast_stat_save_failed);
										Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
									}
									@Override
									public void onNext(Stat stat) {
										onSaved(stat);
									}
								});
					}
				}
			}
		});
	}

	private void onSaved(Stat stat) {
		String toastString;
		toastString = getString(R.string.toast_stat_saved);
		Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();

		int position = listAdapter.getPosition(stat);
		// Add 1 for the header row
		LinearLayout v = (LinearLayout) listView.getChildAt(position - listView.getFirstVisiblePosition() + 1);
		if (v != null) {
			TextView textView = (TextView) v.findViewById(R.id.abbreviation_view);
			if (textView != null) {
				textView.setText(stat.getAbbreviation());
			}
			textView = (TextView) v.findViewById(R.id.name_view);
			if (textView != null) {
				textView.setText(stat.getName());
			}
			textView = (TextView) v.findViewById(R.id.description_view);
			if (textView != null) {
				textView.setText(stat.getDescription());
			}
		}
	}

	private void initListView(View layout) {
		View headerView;

		listView = (ListView) layout.findViewById(R.id.list_view);
//		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		headerView = getActivity().getLayoutInflater().inflate(
				R.layout.stat_list_header, listView, false);
		listView.addHeaderView(headerView);

		listView.setAdapter(listAdapter);

		// Clicking a row in the listView will send the user to the edit world activity
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(dirty) {
					selectedInstance.setAbbreviation(abbreviationEdit.getText().toString());
					selectedInstance.setName(nameEdit.getText().toString());
					selectedInstance.setDescription(descriptionEdit.getText().toString());
					statRxHandler.save(selectedInstance)
							.observeOn(AndroidSchedulers.mainThread())
							.subscribe(new Subscriber<Stat>() {
								@Override
								public void onCompleted() {
								}
								@Override
								public void onError(Throwable e) {
									String toastString = getString(R.string.toast_stat_save_failed);
									Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
								}
								@Override
								public void onNext(Stat stat) {
									onSaved(stat);
								}
							});
				}

				selectedInstance = (Stat) listView.getItemAtPosition(position);
				if (selectedInstance != null) {
					nameEdit.setText(selectedInstance.getName());
					descriptionEdit.setText(selectedInstance.getDescription());
				}
			}
		});
		registerForContextMenu(listView);
	}
}
