package com.madinnovations.rmu.view.activities.play;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.controller.rxhandler.combat.CriticalResultRxHandler;
import com.madinnovations.rmu.data.entities.character.Character;
import com.madinnovations.rmu.data.entities.combat.Attack;
import com.madinnovations.rmu.data.entities.combat.CriticalResult;
import com.madinnovations.rmu.data.entities.combat.DamageResult;
import com.madinnovations.rmu.data.entities.combat.DamageTable;
import com.madinnovations.rmu.data.entities.common.Specialization;
import com.madinnovations.rmu.data.entities.creature.Creature;
import com.madinnovations.rmu.data.entities.object.Weapon;
import com.madinnovations.rmu.data.entities.object.WeaponTemplate;
import com.madinnovations.rmu.data.entities.play.EncounterRoundInfo;
import com.madinnovations.rmu.data.entities.play.EncounterSetup;
import com.madinnovations.rmu.view.utils.EditTextUtils;
import com.madinnovations.rmu.view.utils.RandomUtils;

import java.util.Collection;

import rx.Subscriber;

/**
 * Fragment to handle interaction with the Resolve Action dialog UI.
 */
@SuppressWarnings("unused")
public class ResolveAttackDialog extends DialogFragment implements EditTextUtils.ValuesCallback {
	private static final String TAG = "ResolveAttackDialog";
	public static final String CRITICAL_RESULT_RX_HANDLER_ARG_KEY = "criticalResultRxHandler";
	public static final String ENCOUNTER_SETUP_ARG_KEY = "encounterSetup";
	public static final String COMBAT_INFO_ARG_KEY = "combatInfo";
	public static final String CHARACTER_ARG_KEY = "character";
	public static final String CREATURE_ARG_KEY = "creature";
	private CriticalResultRxHandler criticalResultRxHandler;
	private EncounterSetup encounterSetup;
	private EncounterRoundInfo encounterRoundInfo;
	private Character                   character = null;
	private Creature                    creature  = null;
	private ResolveAttackDialogListener listener;
	private EditText attackRollEdit;
	private EditText hitsEdit;
	private EditText criticalEdit;
	private EditText criticalRollEdit;
	private EditText criticalResultEdit;
	private short attackRoll = 1;
	private short criticalRoll = 1;
	private DamageResult damageResult;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		LayoutInflater inflater = getActivity().getLayoutInflater();

		criticalResultRxHandler = (CriticalResultRxHandler)getArguments().getSerializable(CRITICAL_RESULT_RX_HANDLER_ARG_KEY);
		encounterSetup = (EncounterSetup)getArguments().getSerializable(ENCOUNTER_SETUP_ARG_KEY);
		encounterRoundInfo = (EncounterRoundInfo) getArguments().getSerializable(COMBAT_INFO_ARG_KEY);
		character = (Character)getArguments().getSerializable(CHARACTER_ARG_KEY);
		creature = (Creature)getArguments().getSerializable(CREATURE_ARG_KEY);

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		View contentView = inflater.inflate(R.layout.resolve_attack_dialog, null);
		attackRollEdit = EditTextUtils.initEdit(contentView, getActivity(), this, R.id.attack_roll_edit, 0);
		hitsEdit = (EditText)contentView.findViewById(R.id.hits_text);
		criticalEdit = (EditText)contentView.findViewById(R.id.critical_edit);
		criticalRollEdit = EditTextUtils.initEdit(contentView, getActivity(), this, R.id.critical_roll_edit, 0);
		criticalResultEdit = (EditText)contentView.findViewById(R.id.critical_result_edit);

		TextView nameView = (TextView)contentView.findViewById(R.id.name_view);
		if(character != null) {
			nameView.setText(character.getFullName());
		}
		else if(creature != null) {
			nameView.setText(creature.getCreatureVariety().getName());
		}

		TextView targetView = (TextView)contentView.findViewById(R.id.target_view);
		EncounterRoundInfo opponentInfo = getOpponentInfo();
		if(encounterRoundInfo.getSelectedOpponent() != null && encounterRoundInfo.getSelectedOpponent() instanceof Character) {
			targetView.setText(((Character) encounterRoundInfo.getSelectedOpponent()).getKnownAs());
		}
		else if(encounterRoundInfo.getSelectedOpponent() != null && encounterRoundInfo.getSelectedOpponent() instanceof Creature) {
			targetView.setText(((Creature) encounterRoundInfo.getSelectedOpponent()).getCreatureVariety().getName());
		}

		if(opponentInfo != null) {
			attackRoll = RandomUtils.roll1d100UOE();
			setDamageResults(attackRoll, opponentInfo);
		}

		builder.setTitle(R.string.title_resolve_attack)
				.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						listener.onResolveAttackOk(ResolveAttackDialog.this);
					}
				})
				.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						listener.onResolveAttackCancel(ResolveAttackDialog.this);
					}
				})
				.setView(contentView);
		return builder.create();
	}

	@Override
	public String getValueForEditText(@IdRes int editTextId) {
		String result = null;
		switch (editTextId) {
			case R.id.attack_roll_edit:
				result = String.valueOf(attackRoll);
				break;
			case R.id.critical_roll_edit:
				result = String.valueOf(criticalRoll);
				break;
		}

		return result;
	}

	@Override
	public void setValueFromEditText(@IdRes int editTextId, String newString) {
		switch (editTextId) {
			case R.id.attack_roll_edit:
				if(newString != null && !newString.isEmpty()) {
					attackRoll = Short.valueOf(newString);
					EncounterRoundInfo opponentInfo = getOpponentInfo();
					if(opponentInfo != null) {
						setDamageResults(attackRoll, opponentInfo);
					}
				}
				break;
			case R.id.critical_roll_edit:
				if(newString != null && !newString.isEmpty()) {
					criticalRoll = Short.valueOf(newString);
					setCriticalResults(criticalRoll, damageResult);
				}
				break;
		}
	}

	@Override
	public boolean performLongClick(@IdRes int editTextId) {
		return false;
	}

	public boolean copyViewsToItems() {
		encounterRoundInfo.setActionInProgress(null);
		return true;
	}

	private EncounterRoundInfo getOpponentInfo() {
		EncounterRoundInfo opponentInfo;
		//noinspection SuspiciousMethodCalls
		opponentInfo = encounterSetup.getEnemyCombatInfo().get(encounterRoundInfo.getSelectedOpponent());
		if(opponentInfo == null) {
			//noinspection SuspiciousMethodCalls
			opponentInfo = encounterSetup.getCharacterCombatInfo().get(encounterRoundInfo.getSelectedOpponent());
		}

		return opponentInfo;
	}

	private void setDamageResults(short attackRoll, EncounterRoundInfo opponentInfo) {
		Object attack = encounterRoundInfo.getSelectedAttack();
		Log.d(TAG, "setDamageResults: attack = " + attack);
		Weapon weapon = null;
		Attack creatureAttack = null;
		Specialization specialization = null;
		DamageTable damageTable = null;
		short fumbleRange = 1;
		if(attack != null) {
			if(attack instanceof Weapon) {
				weapon = (Weapon)attack;
				damageTable = ((WeaponTemplate)weapon.getItemTemplate()).getDamageTable();
				fumbleRange = ((WeaponTemplate)weapon.getItemTemplate()).getFumble();
			}
			else if(attack instanceof Attack) {
				creatureAttack = (Attack)attack;
				damageTable = creatureAttack.getDamageTable();
				fumbleRange = creatureAttack.getFumble();
			}
			else if(attack instanceof Specialization) {
				specialization = (Specialization)attack;
				// TODO: Get DamageTable for attack specialization and fumble range
			}
			Log.d(TAG, "setDamageResults: damageTable = " + damageTable);
			Log.d(TAG, "setDamageResults: fumbleRange = " + fumbleRange);
		}
		short offensiveBonus = encounterRoundInfo.getOffensiveBonus(opponentInfo, weapon, creatureAttack, specialization);
		Log.d(TAG, "setDamageResults: offensiveBonus = " + offensiveBonus);
		short defensiveBonus = opponentInfo.getDefensiveBonus(encounterRoundInfo);
		Log.d(TAG, "setDamageResults: defensiveBonus = " + defensiveBonus);
		if(attackRoll >= 1 && attackRoll <= fumbleRange) {
			attackRollEdit.setText(String.format(getString(R.string.fumbled), attackRoll));
		}
		else {
			attackRollEdit.setText(String.valueOf(attackRoll));
			if(damageTable != null) {
				damageResult = damageTable.getResult(opponentInfo.getCombatant().getArmorType(),
																		(short)(attackRoll + offensiveBonus - defensiveBonus));
				Log.d(TAG, "setDamageResults: damageResult = " + damageResult);
				if(damageResult != null) {
					hitsEdit.setText(String.valueOf(damageResult.getHits()));
					if (damageResult.getCriticalType() != null && damageResult.getCriticalSeverity() != null) {
						criticalRoll = RandomUtils.roll1d100();
						setCriticalResults(criticalRoll, damageResult);
					}
					else {
						criticalEdit.setText("NA");
					}
				}
				else {
					hitsEdit.setText("0");
					criticalEdit.setText("NA");
				}
			}
		}
	}

	private void setCriticalResults(final short criticalRoll, final DamageResult damageResult) {
		if(damageResult != null && damageResult.getCriticalSeverity() != null && damageResult.getCriticalType() != null) {
			criticalEdit.setText(String.valueOf(damageResult.getCriticalSeverity() + damageResult.getCriticalType().toString()));
			criticalRollEdit.setText(String.valueOf(criticalRoll));
			criticalResultRxHandler.getCriticalResultsForCriticalType(damageResult.getCriticalType())
					.subscribe(new Subscriber<Collection<CriticalResult>>() {
						@Override
						public void onCompleted() {}
						@Override
						public void onError(Throwable e) {
							Log.e(TAG, "onError: Exception caught getting CriticalResult instances", e);
						}
						@Override
						public void onNext(Collection<CriticalResult> criticalResults) {
							for (CriticalResult criticalResult : criticalResults) {
								if (criticalResult.getSeverityCode() == damageResult.getCriticalSeverity() &&
										criticalRoll >= criticalResult.getMinRoll() &&
										criticalRoll <= criticalResult.getMaxRoll()) {
									criticalResultEdit.setText(criticalResult.getResultText());
									break;
								}
							}
						}
					});
		}
	}

	public ResolveAttackDialog.ResolveAttackDialogListener getListener() {
		return listener;
	}
	public void setListener(ResolveAttackDialog.ResolveAttackDialogListener listener) {
		this.listener = listener;
	}
	public Character getCharacter() {
		return character;
	}
	public Creature getCreature() {
		return creature;
	}
	public EncounterRoundInfo getEncounterRoundInfo() {
		return encounterRoundInfo;
	}

	public interface ResolveAttackDialogListener {
		/**
		 * Callback method called when the user clicks the OK button
		 *
		 * @param dialog  the dialog instance executing the callback
		 */
		void onResolveAttackOk(DialogFragment dialog);

		/**
		 * Callback method called when the user clicks the Cancel button
		 *
		 * @param dialog  the dialog instance executing the callback
		 */
		void onResolveAttackCancel(DialogFragment dialog);
	}
}
