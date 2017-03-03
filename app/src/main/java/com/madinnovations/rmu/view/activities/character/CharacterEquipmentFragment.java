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
package com.madinnovations.rmu.view.activities.character;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.controller.rxhandler.common.SkillRxHandler;
import com.madinnovations.rmu.controller.rxhandler.item.ItemRxHandler;
import com.madinnovations.rmu.controller.rxhandler.spell.RealmRxHandler;
import com.madinnovations.rmu.data.entities.character.Character;
import com.madinnovations.rmu.data.entities.object.Item;
import com.madinnovations.rmu.data.entities.object.Slot;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.di.modules.CharacterFragmentModule;
import com.madinnovations.rmu.view.utils.SpinnerUtils;

import javax.inject.Inject;

/**
 * Handles interactions with the UI for character creation.
 */
public class CharacterEquipmentFragment extends Fragment implements SpinnerUtils.ValuesCallback {
	@SuppressWarnings("unused")
	private static final String TAG = "CharacterEquipmentFrag";
	@Inject
	protected SkillRxHandler     skillRxHandler;
	@Inject
	protected RealmRxHandler     realmRxHandler;
	@Inject
	protected ItemRxHandler      itemRxHandler;
	private   CharactersFragment charactersFragment;
	private   SpinnerUtils<Item> headSlotSpinnerUtils;
	private   SpinnerUtils<Item> mainHandSlotSpinnerUtils;
	private   SpinnerUtils<Item> offHandSlotSpinnerUtils;
	private   SpinnerUtils<Item> handsSlotSpinnerUtils;
	private   SpinnerUtils<Item> chestSlotSpinnerUtils;
	private   SpinnerUtils<Item> armsSlotSpinnerUtils;
	private   SpinnerUtils<Item> legsSlotSpinnerUtils;
	private   SpinnerUtils<Item> feetSlotSpinnerUtils;
	private   SpinnerUtils<Item> backSlotSpinnerUtils;
	private   SpinnerUtils<Item> shirtSlotSpinnerUtils;
	private   SpinnerUtils<Item> pantsSlotSpinnerUtils;
	private   SpinnerUtils<Item> backpackSlotSpinnerUtils;
	private   Item               headSlotItem     = new Item();
	private   Item               mainHandSlotItem = new Item();
	private   Item               offhandSlotItem  = new Item();
	private   Item               chestSlotItem    = new Item();
	private   Item               armsSlotItem     = new Item();
	private   Item               handsSlotItem    = new Item();
	private   Item               legsSlotItem     = new Item();
	private   Item               feetSlotItem     = new Item();
	private   Item               backSlotItem     = new Item();
	private   Item               shirtSlotItem    = new Item();
	private   Item               pantsSlotItem    = new Item();
	private   Item               backpackSlotItem = new Item();

	/**
	 * Creates new CharacterEquipmentFragment instance.
	 *
	 * @param charactersFragment  the CharactersFragment instance this fragment is attached to.
	 * @return the new instance.
	 */
	public static CharacterEquipmentFragment newInstance(CharactersFragment charactersFragment) {
		CharacterEquipmentFragment fragment = new CharacterEquipmentFragment();
		fragment.charactersFragment = charactersFragment;
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if(charactersFragment == null) {
			return null;
		}

		((CampaignActivity)getActivity()).getActivityComponent().
				newCharacterFragmentComponent(new CharacterFragmentModule(this)).injectInto(this);

		View layout = inflater.inflate(R.layout.character_equipment_page, container, false);

		headSlotItem.setName(getString(R.string.head_slot_label));
		mainHandSlotItem.setName(getString(R.string.main_hand_slot_label));
		offhandSlotItem.setName(getString(R.string.offhand_slot_label));
		chestSlotItem.setName(getString(R.string.chest_slot_label));
		armsSlotItem.setName(getString(R.string.arms_slot_label));
		handsSlotItem.setName(getString(R.string.hands_slot_label));
		legsSlotItem.setName(getString(R.string.legs_slot_label));
		feetSlotItem.setName(getString(R.string.feet_slot_label));
		backSlotItem.setName(getString(R.string.back_slot_label));
		shirtSlotItem.setName(getString(R.string.shirt_slot_label));
		pantsSlotItem.setName(getString(R.string.pants_slot_label));
		backpackSlotItem.setName(getString(R.string.backpack_slot_label));

		headSlotSpinnerUtils = new SpinnerUtils<>();
		headSlotSpinnerUtils.initSpinner(layout, getActivity(), itemRxHandler.getAllForSlot(Slot.HEAD), this,
										 R.id.head_slot_spinner, headSlotItem);
		mainHandSlotSpinnerUtils = new SpinnerUtils<>();
		mainHandSlotSpinnerUtils.initSpinner(layout, getActivity(), itemRxHandler.getAllForSlot(Slot.MAIN_HAND), this,
											 R.id.main_hand_slot_spinner, mainHandSlotItem);
		offHandSlotSpinnerUtils = new SpinnerUtils<>();
		offHandSlotSpinnerUtils.initSpinner(layout, getActivity(), itemRxHandler.getAllForSlot(Slot.OFF_HAND), this,
											R.id.off_hand_slot_spinner, offhandSlotItem);
		handsSlotSpinnerUtils = new SpinnerUtils<>();
		handsSlotSpinnerUtils.initSpinner(layout, getActivity(), itemRxHandler.getAllForSlot(Slot.HANDS), this,
										  R.id.hands_slot_spinner, handsSlotItem);
		chestSlotSpinnerUtils = new SpinnerUtils<>();
		chestSlotSpinnerUtils.initSpinner(layout, getActivity(), itemRxHandler.getAllForSlot(Slot.CHEST), this,
										  R.id.chest_slot_spinner, chestSlotItem);
		armsSlotSpinnerUtils = new SpinnerUtils<>();
		armsSlotSpinnerUtils.initSpinner(layout, getActivity(), itemRxHandler.getAllForSlot(Slot.ARMS), this,
										 R.id.arms_slot_spinner, armsSlotItem);
		legsSlotSpinnerUtils = new SpinnerUtils<>();
		legsSlotSpinnerUtils.initSpinner(layout, getActivity(), itemRxHandler.getAllForSlot(Slot.LEGS), this,
										 R.id.legs_slot_spinner, legsSlotItem);
		feetSlotSpinnerUtils = new SpinnerUtils<>();
		feetSlotSpinnerUtils.initSpinner(layout, getActivity(), itemRxHandler.getAllForSlot(Slot.FEET), this,
										 R.id.feet_slot_spinner, feetSlotItem);
		backSlotSpinnerUtils = new SpinnerUtils<>();
		backSlotSpinnerUtils.initSpinner(layout, getActivity(), itemRxHandler.getAllForSlot(Slot.BACK), this,
										 R.id.back_slot_spinner, backSlotItem);
		shirtSlotSpinnerUtils = new SpinnerUtils<>();
		shirtSlotSpinnerUtils.initSpinner(layout, getActivity(), itemRxHandler.getAllForSlot(Slot.SHIRT), this,
										  R.id.shirt_slot_spinner, shirtSlotItem);
		pantsSlotSpinnerUtils = new SpinnerUtils<>();
		pantsSlotSpinnerUtils.initSpinner(layout, getActivity(), itemRxHandler.getAllForSlot(Slot.PANTS), this,
										  R.id.pants_slot_spinner, pantsSlotItem);
		backpackSlotSpinnerUtils = new SpinnerUtils<>();
		backpackSlotSpinnerUtils.initSpinner(layout, getActivity(), itemRxHandler.getAllForSlot(Slot.BACKPACK), this,
											 R.id.backpack_slot_spinner, backpackSlotItem);

		return layout;
	}

	@Override
	public void onResume() {
		super.onResume();
		headSlotSpinnerUtils.onResume(getActivity());
		mainHandSlotSpinnerUtils.onResume(getActivity());
		offHandSlotSpinnerUtils.onResume(getActivity());
		handsSlotSpinnerUtils.onResume(getActivity());
		chestSlotSpinnerUtils.onResume(getActivity());
		armsSlotSpinnerUtils.onResume(getActivity());
		legsSlotSpinnerUtils.onResume(getActivity());
		feetSlotSpinnerUtils.onResume(getActivity());
		backSlotSpinnerUtils.onResume(getActivity());
		shirtSlotSpinnerUtils.onResume(getActivity());
		pantsSlotSpinnerUtils.onResume(getActivity());
		backpackSlotSpinnerUtils.onResume(getActivity());
		copyItemToViews();
	}

	@Override
	public Object getValueForSpinner(@IdRes int spinnerId) {
		Object result = null;

		switch(spinnerId) {
			case R.id.head_slot_spinner:
				result = charactersFragment.getCurrentInstance().getHeadItem();
				break;
			case R.id.main_hand_slot_spinner:
				result = charactersFragment.getCurrentInstance().getMainHandItem();
				break;
			case R.id.off_hand_slot_spinner:
				result = charactersFragment.getCurrentInstance().getOffhandItem();
				break;
			case R.id.hands_slot_spinner:
				result = charactersFragment.getCurrentInstance().getHandsItem();
				break;
			case R.id.chest_slot_spinner:
				result = charactersFragment.getCurrentInstance().getChestItem();
				break;
			case R.id.arms_slot_spinner:
				result = charactersFragment.getCurrentInstance().getArmsItem();
				break;
			case R.id.legs_slot_spinner:
				result = charactersFragment.getCurrentInstance().getLegsItem();
				break;
			case R.id.feet_slot_spinner:
				result = charactersFragment.getCurrentInstance().getFeetItem();
				break;
			case R.id.back_slot_spinner:
				result = charactersFragment.getCurrentInstance().getBackItem();
				break;
			case R.id.shirt_slot_spinner:
				result = charactersFragment.getCurrentInstance().getShirtItem();
				break;
			case R.id.pants_slot_spinner:
				result = charactersFragment.getCurrentInstance().getPantsItem();
				break;
			case R.id.backpack_slot_spinner:
				result = charactersFragment.getCurrentInstance().getBackpackItem();
				break;
		}

		return result;
	}

	@Override
	public void setValueFromSpinner(@IdRes int spinnerId, Object newItem) {
		switch(spinnerId) {
			case R.id.head_slot_spinner:
				charactersFragment.getCurrentInstance().setHeadItem((Item)newItem);
				charactersFragment.saveItem();
				break;
			case R.id.main_hand_slot_spinner:
				charactersFragment.getCurrentInstance().setMainHandItem((Item)newItem);
				charactersFragment.saveItem();
				break;
			case R.id.off_hand_slot_spinner:
				charactersFragment.getCurrentInstance().setOffhandItem((Item)newItem);
				charactersFragment.saveItem();
				break;
			case R.id.hands_slot_spinner:
				charactersFragment.getCurrentInstance().setHandsItem((Item)newItem);
				charactersFragment.saveItem();
				break;
			case R.id.chest_slot_spinner:
				charactersFragment.getCurrentInstance().setChestItem((Item)newItem);
				charactersFragment.saveItem();
				break;
			case R.id.arms_slot_spinner:
				charactersFragment.getCurrentInstance().setArmsItem((Item)newItem);
				charactersFragment.saveItem();
				break;
			case R.id.legs_slot_spinner:
				charactersFragment.getCurrentInstance().setLegsItem((Item)newItem);
				charactersFragment.saveItem();
				break;
			case R.id.feet_slot_spinner:
				charactersFragment.getCurrentInstance().setFeetItem((Item)newItem);
				charactersFragment.saveItem();
				break;
			case R.id.back_slot_spinner:
				charactersFragment.getCurrentInstance().setBackItem((Item)newItem);
				charactersFragment.saveItem();
				break;
			case R.id.shirt_slot_spinner:
				charactersFragment.getCurrentInstance().setShirtItem((Item)newItem);
				charactersFragment.saveItem();
				break;
			case R.id.pants_slot_spinner:
				charactersFragment.getCurrentInstance().setPantsItem((Item)newItem);
				charactersFragment.saveItem();
				break;
			case R.id.backpack_slot_spinner:
				charactersFragment.getCurrentInstance().setBackpackItem((Item)newItem);
				charactersFragment.saveItem();
				break;
		}
	}

	@Override
	public void observerCompleted(@IdRes int spinnerId) {

	}

	public void copyItemToViews() {
		if(charactersFragment != null) {
			Character character = charactersFragment.getCurrentInstance();

			headSlotSpinnerUtils.setSelection(character.getHeadItem());
			mainHandSlotSpinnerUtils.setSelection(character.getMainHandItem());
			offHandSlotSpinnerUtils.setSelection(character.getOffhandItem());
			handsSlotSpinnerUtils.setSelection(character.getHandsItem());
			chestSlotSpinnerUtils.setSelection(character.getChestItem());
			armsSlotSpinnerUtils.setSelection(character.getArmsItem());
			legsSlotSpinnerUtils.setSelection(character.getLegsItem());
			feetSlotSpinnerUtils.setSelection(character.getFeetItem());
			backSlotSpinnerUtils.setSelection(character.getBackItem());
			shirtSlotSpinnerUtils.setSelection(character.getShirtItem());
			pantsSlotSpinnerUtils.setSelection(character.getPantsItem());
			backpackSlotSpinnerUtils.setSelection(character.getBackpackItem());
		}
	}
}
