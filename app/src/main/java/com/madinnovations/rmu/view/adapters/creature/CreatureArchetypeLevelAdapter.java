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
package com.madinnovations.rmu.view.adapters.creature;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.data.entities.creature.CreatureArchetypeLevel;
import com.madinnovations.rmu.view.utils.EditTextUtils;

/**
 * Populates a ListView with {@link CreatureArchetypeLevel} information
 */
public class CreatureArchetypeLevelAdapter extends ArrayAdapter<CreatureArchetypeLevel> {
	private static final int LAYOUT_RESOURCE_ID = R.layout.creature_archetypes_level_row;
	private Context context;
	private LayoutInflater layoutInflater;
	private CreatureArchetypeLevelCallbacks callbacks;

	/**
	 * Creates a new RacialStatBonusListAdapter instance.
	 *
	 * @param context the view {@link Context} the adapter will be attached to.
	 */
	public CreatureArchetypeLevelAdapter(Context context, CreatureArchetypeLevelCallbacks callbacks) {
		super(context, LAYOUT_RESOURCE_ID);
		this.context = context;
		this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.callbacks = callbacks;
	}

	@NonNull
	@Override
	public View getView(int position, View convertView, @NonNull ViewGroup parent) {
		View rowView;
		ViewHolder holder;

		if (convertView == null) {
			rowView = layoutInflater.inflate(LAYOUT_RESOURCE_ID, parent, false);
			holder = new ViewHolder((CheckBox) rowView.findViewById(R.id.select_range_check_box),
									(TextView) rowView.findViewById(R.id.level_view),
									(EditText) rowView.findViewById(R.id.attack_edit),
									(EditText) rowView.findViewById(R.id.attack2_edit),
									(EditText) rowView.findViewById(R.id.db_edit),
									(EditText) rowView.findViewById(R.id.body_dev_edit),
									(EditText) rowView.findViewById(R.id.prime_skill_edit),
									(EditText) rowView.findViewById(R.id.secondary_skill_edit),
									(EditText) rowView.findViewById(R.id.power_dev_edit),
									(EditText) rowView.findViewById(R.id.spells_edit),
									(EditText) rowView.findViewById(R.id.talent_dp_edit),
									(EditText) rowView.findViewById(R.id.agility_edit),
									(EditText) rowView.findViewById(R.id.constitution_stat_edit),
									(EditText) rowView.findViewById(R.id.constitution_edit),
									(EditText) rowView.findViewById(R.id.empathy_edit),
									(EditText) rowView.findViewById(R.id.intuition_edit),
									(EditText) rowView.findViewById(R.id.memory_edit),
									(EditText) rowView.findViewById(R.id.presence_edit),
									(EditText) rowView.findViewById(R.id.quickness_edit),
									(EditText) rowView.findViewById(R.id.reasoning_edit),
									(EditText) rowView.findViewById(R.id.self_discipline_edit),
									(EditText) rowView.findViewById(R.id.strength_edit));
			rowView.setTag(holder);
		}
		else {
			rowView = convertView;
			holder = (ViewHolder) convertView.getTag();
		}

		CreatureArchetypeLevel creatureArchetypeLevel = getItem(position);
		if(creatureArchetypeLevel != null) {
			holder.currentInstance = creatureArchetypeLevel;
			holder.selectRangeCheckBox.setChecked(callbacks.isSelected(creatureArchetypeLevel.getLevel()));
			holder.levelView.setText(String.valueOf(creatureArchetypeLevel.getLevel()));
			holder.attackEdit.setText(String.valueOf(creatureArchetypeLevel.getAttack()));
			holder.attack2Edit.setText(String.valueOf(creatureArchetypeLevel.getAttack2()));
			holder.defensiveBonusEdit.setText(String.valueOf(creatureArchetypeLevel.getDefensiveBonus()));
			holder.bodyDevelopmentEdit.setText(String.valueOf(creatureArchetypeLevel.getBodyDevelopment()));
			holder.primeSkillEdit.setText(String.valueOf(creatureArchetypeLevel.getPrimeSkill()));
			holder.secondarySkillEdit.setText(String.valueOf(creatureArchetypeLevel.getSecondarySkill()));
			holder.powerDevelopmentEdit.setText(String.valueOf(creatureArchetypeLevel.getPowerDevelopment()));
			holder.spellsEdit.setText(String.valueOf(creatureArchetypeLevel.getSpells()));
			holder.talentDPEdit.setText(String.valueOf(creatureArchetypeLevel.getTalentDP()));
			holder.agilityEdit.setText(String.valueOf(creatureArchetypeLevel.getAgility()));
			holder.constitutionStatEdit.setText(String.valueOf(creatureArchetypeLevel.getConstitutionStat()));
			holder.constitutionEdit.setText(String.valueOf(creatureArchetypeLevel.getConstitution()));
			holder.empathyEdit.setText(String.valueOf(creatureArchetypeLevel.getEmpathy()));
			holder.intuitionEdit.setText(String.valueOf(creatureArchetypeLevel.getIntuition()));
			holder.memoryEdit.setText(String.valueOf(creatureArchetypeLevel.getMemory()));
			holder.presenceEdit.setText(String.valueOf(creatureArchetypeLevel.getPresence()));
			holder.quicknessEdit.setText(String.valueOf(creatureArchetypeLevel.getQuickness()));
			holder.reasoningEdit.setText(String.valueOf(creatureArchetypeLevel.getReasoning()));
			holder.selfDisciplineEdit.setText(String.valueOf(creatureArchetypeLevel.getSelfDiscipline()));
			holder.strengthEdit.setText(String.valueOf(creatureArchetypeLevel.getStrength()));
		}

		return rowView;
	}

	private class ViewHolder implements EditTextUtils.ValuesCallback {
		private CreatureArchetypeLevel currentInstance;
		private CheckBox selectRangeCheckBox;
		private TextView levelView;
		private EditText attackEdit;
		private EditText attack2Edit;
		private EditText defensiveBonusEdit;
		private EditText bodyDevelopmentEdit;
		private EditText primeSkillEdit;
		private EditText secondarySkillEdit;
		private EditText powerDevelopmentEdit;
		private EditText spellsEdit;
		private EditText talentDPEdit;
		private EditText agilityEdit;
		private EditText constitutionStatEdit;
		private EditText constitutionEdit;
		private EditText empathyEdit;
		private EditText intuitionEdit;
		private EditText memoryEdit;
		private EditText presenceEdit;
		private EditText quicknessEdit;
		private EditText reasoningEdit;
		private EditText selfDisciplineEdit;
		private EditText strengthEdit;

		public ViewHolder(CheckBox selectRangeCheckBox, TextView levelView, EditText attackEdit,
						  EditText attack2Edit, EditText defensiveBonusEdit, EditText bodyDevelopmentEdit,
						  EditText primeSkillEdit, EditText secondarySkillEdit, EditText powerDevelopmentEdit,
						  EditText spellsEdit, EditText talentDPEdit, EditText agilityEdit, EditText constitutionStatEdit,
						  EditText constitutionEdit, EditText empathyEdit, EditText intuitionEdit, EditText memoryEdit,
						  EditText presenceEdit, EditText quicknessEdit, EditText reasoningEdit,
						  EditText selfDisciplineEdit, EditText strengthEdit) {
			this.selectRangeCheckBox = selectRangeCheckBox;
			this.levelView = levelView;
			this.attackEdit = attackEdit;
			this.attack2Edit = attack2Edit;
			this.defensiveBonusEdit = defensiveBonusEdit;
			this.bodyDevelopmentEdit = bodyDevelopmentEdit;
			this.primeSkillEdit = primeSkillEdit;
			this.secondarySkillEdit = secondarySkillEdit;
			this.powerDevelopmentEdit = powerDevelopmentEdit;
			this.spellsEdit = spellsEdit;
			this.talentDPEdit = talentDPEdit;
			this.agilityEdit = agilityEdit;
			this.constitutionStatEdit = constitutionStatEdit;
			this.constitutionEdit = constitutionEdit;
			this.empathyEdit = empathyEdit;
			this.intuitionEdit = intuitionEdit;
			this.memoryEdit = memoryEdit;
			this.presenceEdit = presenceEdit;
			this.quicknessEdit = quicknessEdit;
			this.reasoningEdit = reasoningEdit;
			this.selfDisciplineEdit = selfDisciplineEdit;
			this.strengthEdit = strengthEdit;

			initSelectRangeCheckBox();
			EditTextUtils.initEdit(attackEdit, context, this, R.id.attack_edit, 0);
			EditTextUtils.initEdit(attack2Edit, context, this, R.id.attack2_edit, 0);
			EditTextUtils.initEdit(defensiveBonusEdit, context, this, R.id.db_edit, 0);
			EditTextUtils.initEdit(bodyDevelopmentEdit, context, this, R.id.body_dev_edit, 0);
			EditTextUtils.initEdit(primeSkillEdit, context, this, R.id.prime_skill_edit, 0);
			EditTextUtils.initEdit(secondarySkillEdit, context, this, R.id.secondary_skill_edit, 0);
			EditTextUtils.initEdit(powerDevelopmentEdit, context, this, R.id.power_dev_edit, 0);
			EditTextUtils.initEdit(spellsEdit, context, this, R.id.spells_edit, 0);
			EditTextUtils.initEdit(talentDPEdit, context, this, R.id.talent_dp_edit, 0);
			EditTextUtils.initEdit(agilityEdit, context, this, R.id.agility_edit, 0);
			EditTextUtils.initEdit(constitutionStatEdit, context, this, R.id.constitution_stat_edit, 0);
			EditTextUtils.initEdit(constitutionEdit, context, this, R.id.constitution_edit, 0);
			EditTextUtils.initEdit(empathyEdit, context, this, R.id.empathy_edit, 0);
			EditTextUtils.initEdit(intuitionEdit, context, this, R.id.intuition_edit, 0);
			EditTextUtils.initEdit(memoryEdit, context, this, R.id.memory_edit, 0);
			EditTextUtils.initEdit(presenceEdit, context, this, R.id.presence_edit, 0);
			EditTextUtils.initEdit(quicknessEdit, context, this, R.id.quickness_edit, 0);
			EditTextUtils.initEdit(reasoningEdit, context, this, R.id.reasoning_edit, 0);
			EditTextUtils.initEdit(selfDisciplineEdit, context, this, R.id.self_discipline_edit, 0);
			EditTextUtils.initEdit(strengthEdit, context, this, R.id.strength_edit, 0);
		}

		private void initSelectRangeCheckBox() {
			selectRangeCheckBox.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					callbacks.toggleSelection(currentInstance.getLevel(), selectRangeCheckBox.isChecked());
				}
			});
		}

		@Override
		public String getValueForEditText(@IdRes int editTextId) {
			String result = null;

			switch (editTextId) {
				case R.id.attack_edit:
					result = String.valueOf(currentInstance.getAttack());
					break;
				case R.id.attack2_edit:
					result = String.valueOf(currentInstance.getAttack2());
					break;
				case R.id.db_edit:
					result = String.valueOf(currentInstance.getDefensiveBonus());
					break;
				case R.id.body_dev_edit:
					result = String.valueOf(currentInstance.getBodyDevelopment());
					break;
				case R.id.prime_skill_edit:
					result = String.valueOf(currentInstance.getPrimeSkill());
					break;
				case R.id.secondary_skill_edit:
					result = String.valueOf(currentInstance.getSecondarySkill());
					break;
				case R.id.power_dev_edit:
					result = String.valueOf(currentInstance.getPowerDevelopment());
					break;
				case R.id.spells_edit:
					result = String.valueOf(currentInstance.getSpells());
					break;
				case R.id.talent_dp_edit:
					result = String.valueOf(currentInstance.getTalentDP());
					break;
				case R.id.agility_edit:
					result = String.valueOf(currentInstance.getAgility());
					break;
				case R.id.constitution_stat_edit:
					result = String.valueOf(currentInstance.getConstitutionStat());
					break;
				case R.id.constitution_edit:
					result = String.valueOf(currentInstance.getConstitution());
					break;
				case R.id.empathy_edit:
					result = String.valueOf(currentInstance.getEmpathy());
					break;
				case R.id.intuition_edit:
					result = String.valueOf(currentInstance.getIntuition());
					break;
				case R.id.memory_edit:
					result = String.valueOf(currentInstance.getMemory());
					break;
				case R.id.presence_edit:
					result = String.valueOf(currentInstance.getPresence());
					break;
				case R.id.quickness_edit:
					result = String.valueOf(currentInstance.getQuickness());
					break;
				case R.id.reasoning_edit:
					result = String.valueOf(currentInstance.getReasoning());
					break;
				case R.id.self_discipline_edit:
					result = String.valueOf(currentInstance.getSelfDiscipline());
					break;
				case R.id.strength_edit:
					result = String.valueOf(currentInstance.getStrength());
					break;
			}

			return result;
		}

		@Override
		public void setValueFromEditText(@IdRes int editTextId, String newString) {
			if(newString != null && !newString.isEmpty()) {
				short newShort = Short.valueOf(newString);
				switch (editTextId) {
					case R.id.attack_edit:
						currentInstance.setAttack(newShort);
						break;
					case R.id.attack2_edit:
						currentInstance.setAttack2(newShort);
						break;
					case R.id.db_edit:
						currentInstance.setDefensiveBonus(newShort);
						break;
					case R.id.body_dev_edit:
						currentInstance.setBodyDevelopment(newShort);
						break;
					case R.id.prime_skill_edit:
						currentInstance.setPrimeSkill(newShort);
						break;
					case R.id.secondary_skill_edit:
						currentInstance.setSecondarySkill(newShort);
						break;
					case R.id.power_dev_edit:
						currentInstance.setPowerDevelopment(newShort);
						break;
					case R.id.spells_edit:
						currentInstance.setSpells(newShort);
						break;
					case R.id.talent_dp_edit:
						currentInstance.setTalentDP(newShort);
						break;
					case R.id.agility_edit:
						currentInstance.setAgility(newShort);
						break;
					case R.id.constitution_stat_edit:
						currentInstance.setConstitutionStat(newShort);
						break;
					case R.id.constitution_edit:
						currentInstance.setConstitution(newShort);
						break;
					case R.id.empathy_edit:
						currentInstance.setEmpathy(newShort);
						break;
					case R.id.intuition_edit:
						currentInstance.setIntuition(newShort);
						break;
					case R.id.memory_edit:
						currentInstance.setMemory(newShort);
						break;
					case R.id.presence_edit:
						currentInstance.setPresence(newShort);
						break;
					case R.id.quickness_edit:
						currentInstance.setQuickness(newShort);
						break;
					case R.id.reasoning_edit:
						currentInstance.setReasoning(newShort);
						break;
					case R.id.self_discipline_edit:
						currentInstance.setSelfDiscipline(newShort);
						break;
					case R.id.strength_edit:
						currentInstance.setStrength(newShort);
						break;
				}
				callbacks.save();
			}
		}

		@Override
		public boolean performLongClick(@IdRes int editTextId) {
			return false;
		}
	}

	public interface CreatureArchetypeLevelCallbacks {
		/**
		 * Saves the CreatureArchetype
		 */
		void save();

		/**
		 * Toggles the selected status of a level row.
		 *
		 * @param level  the level of the row whose selection status changed
		 * @param selected  true if the level is selected, otherwise false
		 */
		void toggleSelection(short level, boolean selected);

		/**
		 * Checks if the given level is selected.
		 *
		 * @param level  the level to check
		 * @return  true if the level is selected, otherwise false.
		 */
		boolean isSelected(short level);
	}
}
