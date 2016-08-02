package com.madinnovations.rmu.view.adapters.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.data.entities.common.Stat;

import javax.inject.Inject;

/**
 * Populates a ListView with {@link Stat} information
 */
public class StatListAdapter extends ArrayAdapter<Stat> {
	private static final int LAYOUT_RESOURCE_ID = R.layout.stat_list_row;
	private LayoutInflater layoutInflater;

	/**
	 * Creates a new StatListAdapter instance.
	 *
	 * @param context the view {@link Context} the adapter will be attached to.
	 */
	@Inject
	public StatListAdapter(Context context) {
		super(context, LAYOUT_RESOURCE_ID);
		this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView;
		ViewHolder holder;

		if (convertView == null) {
			rowView = layoutInflater.inflate(LAYOUT_RESOURCE_ID, parent, false);
			holder = new ViewHolder((TextView) rowView.findViewById(R.id.abbreviation_view),
					(TextView) rowView.findViewById(R.id.name_view),
					(TextView) rowView.findViewById(R.id.description_view));
			rowView.setTag(holder);
		}
		else {
			rowView = convertView;
			holder = (ViewHolder) convertView.getTag();
		}

		Stat stat = getItem(position);
		holder.abbreviationView.setText(stat.getAbbreviation());
		holder.nameView.setText(stat.getName());
		holder.descriptionView.setText(stat.getDescription());
		return rowView;
	}

	private class ViewHolder {
		private TextView abbreviationView;
		private TextView nameView;
		private TextView descriptionView;

		ViewHolder(TextView abbreviationView, TextView nameView, TextView descriptionView) {
			this.abbreviationView = abbreviationView;
			this.nameView = nameView;
			this.descriptionView = descriptionView;
		}
	}
}