package com.madinnovations.rmu.view.adapters.combat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.data.entities.combat.BodyPart;

import javax.inject.Inject;

/**
 * Populates a Spinner with {@link BodyPart} information
 */
public class BodyPartSpinnerAdapter extends ArrayAdapter<BodyPart> {
	private static final int LAYOUT_RESOURCE_ID = R.layout.name_row;
	private LayoutInflater layoutInflater;

	/**
	 * Creates a new BodyPartSpinnerAdapter instance.
	 *
	 * @param context the view {@link Context} the adapter will be attached to.
	 */
	@Inject
	public BodyPartSpinnerAdapter(Context context) {
		super(context, LAYOUT_RESOURCE_ID);
		this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		return getView(position, convertView, parent);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView;
		ViewHolder holder;

		if (convertView == null) {
			rowView = layoutInflater.inflate(LAYOUT_RESOURCE_ID, parent, false);
			holder = new ViewHolder((TextView) rowView.findViewById(R.id.name_view));
			rowView.setTag(holder);
		}
		else {
			rowView = convertView;
			holder = (ViewHolder) convertView.getTag();
		}

		BodyPart bodyPart = getItem(position);
		holder.nameView.setText(bodyPart.getName());
		return rowView;
	}

	private class ViewHolder {
		private TextView nameView;

		ViewHolder(TextView nameView) {
			this.nameView = nameView;
		}
	}
}
