/**
 * Copyright (C) 2016 MadInnovations
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.madinnovations.rmu.view.adapters.character;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.data.entities.character.SkillCostEntry;

/**
 * Adapter for allowing user to assign costs to skills.
 */
public class AssignableCostsAdapter extends ArrayAdapter<SkillCostEntry> {
	private static final int LAYOUT_RESOURCE_ID = R.layout.list_edit_skill_costs_row;
	private static final String DRAG_COST = "drag-cost";
	private LayoutInflater layoutInflater;

	/**
	 * Creates a new AssignableCostsAdapter instance
	 *
	 * @param context the view {@link Context} the adapter will be attached to.
	 */
	public AssignableCostsAdapter(Context context) {
		super(context, LAYOUT_RESOURCE_ID);
		this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@NonNull
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView;
		ViewHolder holder;

		if (convertView == null) {
			rowView = layoutInflater.inflate(LAYOUT_RESOURCE_ID, parent, false);
			holder = new ViewHolder((TextView)rowView.findViewById(R.id.name_view),
									(LinearLayout)rowView.findViewById(R.id.costs_group),
									(TextView)rowView.findViewById(R.id.initial_cost_view),
									(TextView)rowView.findViewById(R.id.additional_cost_view));
			rowView.setTag(holder);
		}
		else {
			rowView = convertView;
			holder = (ViewHolder)convertView.getTag();
		}

		SkillCostEntry skillCostEntry = getItem(position);
		if(skillCostEntry != null && skillCostEntry.getSkill() != null) {
			holder.nameView.setText(skillCostEntry.getSkill().getName());
			holder.initialCostView.setText(String.valueOf(skillCostEntry.getSkillCost().getFirstCost()));
			holder.additionalCostView.setText(String.valueOf(skillCostEntry.getSkillCost().getAdditionalCost()));
		}

		return rowView;
	}

	private class ViewHolder {
		SkillCostEntry entry;
		TextView nameView;
		LinearLayout costsGroup;
		TextView initialCostView;
		TextView additionalCostView;

		public ViewHolder(final TextView nameView, final LinearLayout costsGroup, final TextView initialCostView,
						  TextView additionalCostView) {
			this.nameView = nameView;
			this.costsGroup = costsGroup;
			this.initialCostView = initialCostView;
			this.additionalCostView = additionalCostView;

			this.nameView.setOnLongClickListener(new View.OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					Log.d("RMU", "In onLongClick");
					ClipData dragData = null;

					int position = getPosition(entry);
					ClipData.Item clipDataItem = new ClipData.Item(String.valueOf(position));
					dragData = new ClipData(DRAG_COST, new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, clipDataItem);
					View.DragShadowBuilder myShadow = new View.DragShadowBuilder(nameView);

					if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
						v.startDragAndDrop(dragData, myShadow, null, 0);
						Log.d("RMU", "Started drag");
					}
					else {
						//noinspection deprecation
						v.startDrag(dragData, myShadow, null, 0);
						Log.d("RMU", "Started drag");
					}
					return false;
				}
			});
			this.costsGroup.setOnDragListener(new View.OnDragListener() {
				private Drawable targetShape = ResourcesCompat.getDrawable(getContext().getResources(),
						R.drawable.drag_target_background,
						null);
				private Drawable hoverShape  = ResourcesCompat.getDrawable(getContext().getResources(),
						R.drawable.drag_hover_background,
						null);

				@Override
				public boolean onDrag(View v, DragEvent event) {
					final int action = event.getAction();

					switch(action) {
						case DragEvent.ACTION_DRAG_STARTED:
							if(event.getClipDescription() != null && DRAG_COST.equals(event.getClipDescription().getLabel())) {
								v.setBackground(targetShape);
								v.invalidate();
								break;
							}
							return false;
						case DragEvent.ACTION_DRAG_ENTERED:
							if(event.getClipDescription() != null && DRAG_COST.equals(event.getClipDescription().getLabel())) {
								v.setBackground(hoverShape);
								v.invalidate();
							}
							else {
								return false;
							}
							break;
						case DragEvent.ACTION_DRAG_LOCATION:
							break;
						case DragEvent.ACTION_DRAG_EXITED:
							if(event.getClipDescription() != null && DRAG_COST.equals(event.getClipDescription().getLabel())) {
								v.setBackground(targetShape);
								v.invalidate();
							}
							else {
								return false;
							}
							break;
						case DragEvent.ACTION_DROP:
							if(event.getClipDescription() != null && DRAG_COST.equals(event.getClipDescription().getLabel())) {
								boolean changed = false;
								for(int i = 0; i < event.getClipData().getItemCount(); i++) {
									ClipData.Item item = event.getClipData().getItemAt(i);
									// We just send skill name but since that is the only field used in the  Attack.equals method we
									// can create a temporary attack and set its id field then use the new Attack to find the position
									// of the actual Attack instance in the adapter
									int position = Integer.valueOf(item.getText().toString());
									if(position != -1) {
										SkillCostEntry entry = AssignableCostsAdapter.this.getItem(position);
										if(entry != null) {

										}
//								if (attackBonusesListAdapter.getPosition(attackBonus) == -1) {
//									attackBonusesListAdapter.add(attackBonus);
//									varietiesFragment.getCurrentInstance().getAttackBonusesMap().put(attack, (short) 0);
//									changed = true;
//								}
									}
								}
//						if(changed) {
//							varietiesFragment.saveItem();
//							attackBonusesListAdapter.notifyDataSetChanged();
//						}
//						v.setBackground(normalShape);
								v.invalidate();
							}
							else {
								return false;
							}
							break;
						case DragEvent.ACTION_DRAG_ENDED:
//					v.setBackground(normalShape);
							v.invalidate();
							break;
					}

					return true;
				}
			});
		}
	}

	protected class AssignableCostDragListener implements View.OnDragListener {
		private Drawable targetShape = ResourcesCompat.getDrawable(getContext().getResources(),
																   R.drawable.drag_target_background,
																   null);
		private Drawable hoverShape  = ResourcesCompat.getDrawable(getContext().getResources(),
																   R.drawable.drag_hover_background,
																   null);

		@Override
		public boolean onDrag(View v, DragEvent event) {
			final int action = event.getAction();

			switch(action) {
				case DragEvent.ACTION_DRAG_STARTED:
					if(event.getClipDescription() != null && DRAG_COST.equals(event.getClipDescription().getLabel())) {
						v.setBackground(targetShape);
						v.invalidate();
						break;
					}
					return false;
				case DragEvent.ACTION_DRAG_ENTERED:
					if(event.getClipDescription() != null && DRAG_COST.equals(event.getClipDescription().getLabel())) {
						v.setBackground(hoverShape);
						v.invalidate();
					}
					else {
						return false;
					}
					break;
				case DragEvent.ACTION_DRAG_LOCATION:
					break;
				case DragEvent.ACTION_DRAG_EXITED:
					if(event.getClipDescription() != null && DRAG_COST.equals(event.getClipDescription().getLabel())) {
						v.setBackground(targetShape);
						v.invalidate();
					}
					else {
						return false;
					}
					break;
				case DragEvent.ACTION_DROP:
					if(event.getClipDescription() != null && DRAG_COST.equals(event.getClipDescription().getLabel())) {
						boolean changed = false;
						for(int i = 0; i < event.getClipData().getItemCount(); i++) {
							ClipData.Item item = event.getClipData().getItemAt(i);
							// We just send skill name but since that is the only field used in the  Attack.equals method we
							// can create a temporary attack and set its id field then use the new Attack to find the position
							// of the actual Attack instance in the adapter
							int position = Integer.valueOf(item.getText().toString());
							if(position != -1) {
								SkillCostEntry entry = AssignableCostsAdapter.this.getItem(position);
								if(entry != null) {

								}
//								if (attackBonusesListAdapter.getPosition(attackBonus) == -1) {
//									attackBonusesListAdapter.add(attackBonus);
//									varietiesFragment.getCurrentInstance().getAttackBonusesMap().put(attack, (short) 0);
//									changed = true;
//								}
							}
						}
//						if(changed) {
//							varietiesFragment.saveItem();
//							attackBonusesListAdapter.notifyDataSetChanged();
//						}
//						v.setBackground(normalShape);
						v.invalidate();
					}
					else {
						return false;
					}
					break;
				case DragEvent.ACTION_DRAG_ENDED:
//					v.setBackground(normalShape);
					v.invalidate();
					break;
			}

			return true;
		}
	}
}
