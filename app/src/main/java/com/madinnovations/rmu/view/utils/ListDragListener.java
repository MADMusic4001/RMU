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
package com.madinnovations.rmu.view.utils;

import android.content.ClipData;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.view.DragEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.data.entities.common.SkillCost;
import com.madinnovations.rmu.data.entities.common.Specialization;

/**
 * Generic OnDragListener implementation
 */
public class ListDragListener implements View.OnDragListener{
	public static final String DRAG_ADD_COST = "add-skill-cost";
	public static final String DRAG_REMOVE_COST = "remove-skill-cost";
	private Resources resources;
	private ListView targetList;
	private ArrayAdapter<Specialization> adapter;
	private Drawable targetShape = ResourcesCompat.getDrawable(resources, R.drawable.drag_target_background, null);
	private Drawable hoverShape  = ResourcesCompat.getDrawable(resources, R.drawable.drag_hover_background, null);
	private Drawable normalShape = targetList.getBackground();

	public ListDragListener(Resources resources, ListView targetList, ArrayAdapter<Specialization> adapter) {
		this.resources = resources;
		this.targetList = targetList;
		this.adapter = adapter;
	}

	@Override
	public boolean onDrag(View v, DragEvent event) {
		final int action = event.getAction();

		switch(action) {
			case DragEvent.ACTION_DRAG_STARTED:
				if(event.getClipDescription() != null && DRAG_ADD_COST.equals(event.getClipDescription().getLabel())) {
					v.setBackground(targetShape);
					v.invalidate();
					break;
				}
				return false;
			case DragEvent.ACTION_DRAG_ENTERED:
				if(event.getClipDescription() != null && DRAG_ADD_COST.equals(event.getClipDescription().getLabel())) {
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
				if(event.getClipDescription() != null && DRAG_ADD_COST.equals(event.getClipDescription().getLabel())) {
					v.setBackground(targetShape);
					v.invalidate();
				}
				else {
					return false;
				}
				break;
			case DragEvent.ACTION_DROP:
				if(event.getClipDescription() != null && DRAG_ADD_COST.equals(event.getClipDescription().getLabel())) {
					boolean changed = false;
					for(int i = 0; i < event.getClipData().getItemCount(); i++) {
						ClipData.Item item = event.getClipData().getItemAt(i);
						// We just send specialization ID but since that is the only field used in the Specialization.equals method we can
						// create a temporary attack and set its id field then use the new Specialization to find the position of the actual
						// Specialization instance in the adapter
						int specializationId = Integer.valueOf(item.getText().toString());
						Specialization dummySpec = new Specialization(specializationId);
						int position = adapter.getPosition(dummySpec);
						if(position != -1) {
							Specialization specialization = adapter.getItem(position);
							SkillCost skillCost = new SkillCost();
						}
					}
					if(changed) {
					}
					v.setBackground(normalShape);
					v.invalidate();
				}
				else {
					return false;
				}
				break;
			case DragEvent.ACTION_DRAG_ENDED:
				v.setBackground(normalShape);
				v.invalidate();
				break;
		}

		return true;
	}
}
