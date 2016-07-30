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
 * Populates a ListView with {@link BodyPart} information
 */
public class BodyPartListAdapter extends ArrayAdapter<BodyPart> {
	private static final int LAYOUT_RESOURCE_ID = R.layout.name_description_row;
	private LayoutInflater layoutInflater;

	/**
	 * Creates a new BodyPartListAdapter instance.
	 *
	 * @param context the view {@link Context} the adapter will be attached to.
	 */
	@Inject
	public BodyPartListAdapter(Context context) {
		super(context, LAYOUT_RESOURCE_ID);
		this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView;
		ViewHolder holder;

		if (convertView == null) {
			rowView = layoutInflater.inflate(LAYOUT_RESOURCE_ID, parent, false);
			holder = new ViewHolder((TextView) rowView.findViewById(R.id.name_view),
					(TextView) rowView.findViewById(R.id.description_view));
			rowView.setTag(holder);
		}
		else {
			rowView = convertView;
			holder = (ViewHolder) convertView.getTag();
		}

		BodyPart talentCategory = getItem(position);
		holder.nameView.setText(talentCategory.getName());
		holder.descriptionView.setText(talentCategory.getDescription());
		return rowView;
	}

	private class ViewHolder {
		private TextView nameView;
		private TextView descriptionView;

		ViewHolder(TextView nameView, TextView descriptionView) {
			this.nameView = nameView;
			this.descriptionView = descriptionView;
		}
	}
}
