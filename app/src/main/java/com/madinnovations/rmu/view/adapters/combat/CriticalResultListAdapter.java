package com.madinnovations.rmu.view.adapters.combat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.data.entities.combat.CriticalResult;

import javax.inject.Inject;

/**
 * Populates a ListView with {@link CriticalResult} information
 */
public class CriticalResultListAdapter extends ArrayAdapter<CriticalResult> {
	private static final int LAYOUT_RESOURCE_ID = R.layout.range_description_row;
	private LayoutInflater layoutInflater;

	/**
	 * Creates a new CriticalResultListAdapter instance.
	 *
	 * @param context the view {@link Context} the adapter will be attached to.
	 */
	@Inject
	public CriticalResultListAdapter(Context context) {
		super(context, LAYOUT_RESOURCE_ID);
		this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView;
		ViewHolder holder;

		if (convertView == null) {
			rowView = layoutInflater.inflate(LAYOUT_RESOURCE_ID, parent, false);
			holder = new ViewHolder((TextView) rowView.findViewById(R.id.range_view),
					(TextView) rowView.findViewById(R.id.severity_code_view),
					(TextView) rowView.findViewById(R.id.description_view));
			rowView.setTag(holder);
		}
		else {
			rowView = convertView;
			holder = (ViewHolder) convertView.getTag();
		}

		CriticalResult item = getItem(position);
		holder.rangeView.setText(String.format(getContext().getString(R.string.min_max_roll_value),
				item.getMinRoll(), item.getMaxRoll()));
		holder.severityCodeView.setText(String.valueOf(item.getSeverityCode()));
		holder.descriptionView.setText(item.getDescription());
		return rowView;
	}

	private class ViewHolder {
		private TextView rangeView;
		private TextView severityCodeView;
		private TextView descriptionView;

		ViewHolder(TextView rangeView, TextView severityCodeView, TextView descriptionView) {
			this.rangeView = rangeView;
			this.severityCodeView = severityCodeView;
			this.descriptionView = descriptionView;
		}
	}
}
