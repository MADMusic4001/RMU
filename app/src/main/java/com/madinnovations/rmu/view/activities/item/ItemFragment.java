package com.madinnovations.rmu.view.activities.item;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.controller.rxhandler.item.ItemRxHandler;
import com.madinnovations.rmu.controller.rxhandler.item.WeaponRxHandler;
import com.madinnovations.rmu.data.entities.object.Item;
import com.madinnovations.rmu.data.entities.object.Weapon;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.adapters.TwoFieldListAdapter;
import com.madinnovations.rmu.view.di.modules.ItemFragmentModule;

import java.util.Collection;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.content.ContentValues.TAG;

/**
 * Controller class for Item UI
 */
public class ItemFragment extends Fragment implements ItemPaneFragment.DataAccessInterface, TwoFieldListAdapter.GetValues<Item> {
	@Inject
	protected ItemRxHandler                itemRxHandler;
	@Inject
	protected WeaponRxHandler              weaponRxHandler;
	private   TwoFieldListAdapter<Item>    listAdapter;
	private   ListView                     listView;
	private   Item                         currentInstance = new Item();
	private   boolean                      isNew = true;
	private   ItemPaneFragment             itemPaneFragment;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
		((CampaignActivity)getActivity()).getActivityComponent().
				newItemFragmentComponent(new ItemFragmentModule(this)).injectInto(this);

		View layout = inflater.inflate(R.layout.items_fragment, container, false);

		((TextView)layout.findViewById(R.id.header_field1)).setText(R.string.label_item_name);
		((TextView)layout.findViewById(R.id.header_field2)).setText(R.string.label_item_description);

		itemPaneFragment = new ItemPaneFragment();
		itemPaneFragment.setDataAccessInterface(this);
		getFragmentManager().beginTransaction().add(R.id.item_pane_container, itemPaneFragment).commit();

		initListView(layout);

		setHasOptionsMenu(true);

		return layout;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.items_action_bar, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if(id == R.id.action_new_item) {
			currentInstance = new Item();
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
		getActivity().getMenuInflater().inflate(R.menu.item_context_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem menuItem) {
		final Item item;

		AdapterView.AdapterContextMenuInfo info =
				(AdapterView.AdapterContextMenuInfo)menuItem.getMenuInfo();

		switch (menuItem.getItemId()) {
			case R.id.context_new_item:
				currentInstance = new Item();
				isNew = true;
				copyItemToViews();
				listView.clearChoices();
				listAdapter.notifyDataSetChanged();
				return true;
			case R.id.context_delete_item:
				item = (Item)listView.getItemAtPosition(info.position);
				if(item != null) {
					deleteItem(item);
					return true;
				}
				break;
			case R.id.context_make_weapon:
				item = (Item)listView.getItemAtPosition(info.position);
				if(item != null) {
					Weapon weapon = new Weapon(item);
					weapon.setBonus((short)0);
					weapon.setTwoHanded(false);
					removeItemFromList(item);
					weaponRxHandler.save(weapon).subscribe(new Subscriber<Weapon>() {
						@Override
						public void onCompleted() {}
						@Override
						public void onError(Throwable e) {
							Log.e(TAG, "onError: Excception caught saving weapon.", e);
							Toast.makeText(ItemFragment.this.getActivity(), R.string.toast_weapon_save_failed,
									Toast.LENGTH_LONG).show();
						}
						@Override
						public void onNext(Weapon weapon) {
							Toast.makeText(ItemFragment.this.getActivity(), R.string.toast_weapon_saved,
									Toast.LENGTH_LONG).show();
						}
					});
				}
				break;
		}
		return super.onContextItemSelected(menuItem);
	}

	@Override
	public Item getItem() {
		return currentInstance;
	}

	@Override
	public void saveItem() {
		if(currentInstance.isValid()) {
			final boolean wasNew = isNew;
			isNew = false;
			itemRxHandler.save(currentInstance)
					.observeOn(AndroidSchedulers.mainThread())
					.subscribeOn(Schedulers.io())
					.subscribe(new Subscriber<Item>() {
						@Override
						public void onCompleted() {}
						@Override
						public void onError(Throwable e) {
							Log.e(TAG, "Exception saving new Item: " + currentInstance, e);
							Toast.makeText(getActivity(), R.string.toast_item_save_failed, Toast.LENGTH_SHORT).show();
						}
						@Override
						public void onNext(Item savedItem) {
							if (wasNew) {
								listAdapter.add(savedItem);
								if(savedItem == currentInstance) {
									listView.setSelection(listAdapter.getPosition(savedItem));
									listView.setItemChecked(listAdapter.getPosition(savedItem), true);
								}
								listAdapter.notifyDataSetChanged();
							}
							if(getActivity() != null) {
								Toast.makeText(getActivity(), R.string.toast_item_saved, Toast.LENGTH_SHORT).show();
								int position = listAdapter.getPosition(savedItem);
								LinearLayout v = (LinearLayout) listView.getChildAt(position - listView.getFirstVisiblePosition());
								if (v != null) {
									TextView textView = (TextView) v.findViewById(R.id.row_field1);
									textView.setText(savedItem.getName() == null ? savedItem.getItemTemplate().getName() :
											savedItem.getName());
									textView = (TextView) v.findViewById(R.id.row_field2);
									textView.setText(savedItem.getHistory());
								}
							}
						}
					});
		}
	}

	@Override
	public CharSequence getField1Value(Item item) {
		return item.getName() == null ? item.getItemTemplate().getName() : item.getName();
	}

	@Override
	public CharSequence getField2Value(Item item) {
		return item.getHistory();
	}

	private void copyItemToViews() {
		itemPaneFragment.copyItemToViews();
	}

	private void deleteItem(@NonNull final Item item) {
		itemRxHandler.deleteById(item.getId())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new Subscriber<Boolean>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e(TAG, "Exception when deleting: " + item, e);
						Toast.makeText(getActivity(), R.string.toast_item_delete_failed, Toast.LENGTH_SHORT).show();
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
								currentInstance = new Item();
								isNew = true;
							}
							copyItemToViews();
							Toast.makeText(getActivity(), R.string.toast_item_deleted, Toast.LENGTH_SHORT).show();
						}
					}
				});
	}

	private void removeItemFromList(@NonNull final Item item) {
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
			currentInstance = new Item();
			isNew = true;
		}
		copyItemToViews();
	}

	private void initListView(View layout) {
		listView = (ListView) layout.findViewById(R.id.list_view);
		listAdapter = new TwoFieldListAdapter<>(this.getActivity(), 1, 5, this);
		listView.setAdapter(listAdapter);

		itemRxHandler.getAllWithoutSubclass()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Collection<Item>>() {
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
						Log.e(TAG, "Exception caught loading all Item instances in onCreateView", e);
						Toast.makeText(ItemFragment.this.getActivity(),
								R.string.toast_items_load_failed,
								Toast.LENGTH_SHORT).show();
					}
					@Override
					public void onNext(Collection<Item> creatureCategories) {
						listAdapter.clear();
						listAdapter.addAll(creatureCategories);
						listAdapter.notifyDataSetChanged();
						String toastString;
						toastString = String.format(getString(R.string.toast_items_loaded), creatureCategories.size());
						Toast.makeText(ItemFragment.this.getActivity(), toastString, Toast.LENGTH_SHORT).show();
					}
				});

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				currentInstance = (Item)listView.getItemAtPosition(position);
				isNew = false;
				if (currentInstance == null) {
					currentInstance = new Item();
					isNew = true;
				}
				copyItemToViews();
			}
		});
		registerForContextMenu(listView);
	}
}
