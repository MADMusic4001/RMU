package com.madinnovations.rmu.view.activities.play;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.data.entities.character.Character;
import com.madinnovations.rmu.data.entities.creature.Creature;
import com.madinnovations.rmu.data.entities.play.EncounterRoundInfo;
import com.madinnovations.rmu.view.utils.RandomUtils;

/**
 * Fragment to handle interaction with the Resolve Action dialog UI.
 */
@SuppressWarnings("unused")
public class ResolveAttackDialog extends DialogFragment {
	private static final String TAG = "ResolveAttackDialog";
	public static final String COMBAT_INFO_ARG_KEY = "combatInfo";
	public static final String CHARACTER_ARG_KEY = "character";
	public static final String CREATURE_ARG_KEY = "creature";
	private EncounterRoundInfo encounterRoundInfo;
	private Character                   character = null;
	private Creature                    creature  = null;
	private ResolveAttackDialogListener listener;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		LayoutInflater inflater = getActivity().getLayoutInflater();

		encounterRoundInfo = (EncounterRoundInfo) getArguments().getSerializable(COMBAT_INFO_ARG_KEY);
		character = (Character)getArguments().getSerializable(CHARACTER_ARG_KEY);
		creature = (Creature)getArguments().getSerializable(CREATURE_ARG_KEY);

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		View contentView = inflater.inflate(R.layout.resolve_attack_dialog, null);

		TextView nameView = (TextView)contentView.findViewById(R.id.name_view);
		if(character != null) {
			nameView.setText(character.getFullName());
		}
		else if(creature != null) {
			nameView.setText(creature.getCreatureVariety().getName());
		}

		TextView targetView = (TextView)contentView.findViewById(R.id.target_view);
		if(encounterRoundInfo.getSelectedOpponent() != null && encounterRoundInfo.getSelectedOpponent() instanceof Character) {
			targetView.setText(((Character) encounterRoundInfo.getSelectedOpponent()).getKnownAs());
		}
		else if(encounterRoundInfo.getSelectedOpponent() != null && encounterRoundInfo.getSelectedOpponent() instanceof Creature) {
			targetView.setText(((Creature) encounterRoundInfo.getSelectedOpponent()).getCreatureVariety().getName());
		}

		EditText attackRollEdit = (EditText)contentView.findViewById((R.id.attack_roll_edit));
		attackRollEdit.setText(String.valueOf(RandomUtils.roll1d100OE()));

		builder.setTitle(R.string.title_initiative)
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

	public boolean copyViewsToItems() {
		boolean result = false;

		encounterRoundInfo.setActionInProgress(null);
		result = true;

		return result;
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
