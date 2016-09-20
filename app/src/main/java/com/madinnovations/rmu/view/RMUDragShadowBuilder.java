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
package com.madinnovations.rmu.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import java.util.List;

/**
 * Class to create a drag shadow that outlines a group of View instances. The View instances can have gaps between them.
 */
public class RMUDragShadowBuilder extends View.DragShadowBuilder {
	private Drawable[] shadows;
	private List<View> views;

	public RMUDragShadowBuilder(List<View> views) {
		this.views = views;
		shadows = new Drawable[views.size()];
		for(int i = 0 ; i < shadows.length; i ++) {
			shadows[i] = new ColorDrawable(Color.LTGRAY);
		}
	}

	@Override
	public void onProvideShadowMetrics (Point size, Point touch) {
		int topLeftX = Integer.MAX_VALUE, topLeftY = Integer.MAX_VALUE;
		int bottomRightX = 0, bottomRightY = 0;
		int width, height;

		for(int i = 0; i < views.size(); i++) {
			shadows[i].setBounds(views.get(i).getLeft(), views.get(i).getTop(), views.get(i).getRight(), views.get(i).getBottom());
			if(views.get(i).getLeft() < topLeftX) {
				topLeftX = views.get(i).getLeft();
			}
			if(views.get(i).getTop() < topLeftY) {
				topLeftY = views.get(i).getTop();
			}
			if(views.get(i).getRight() > bottomRightX) {
				bottomRightX = views.get(i).getRight();
			}
			if(views.get(i).getBottom() > bottomRightY) {
				bottomRightY = views.get(i).getBottom();
			}
		}

		width = bottomRightX - topLeftX;
		height = bottomRightY - topLeftY;
		size.set(width, height);
		touch.set(width / 2, height / 2);
	}

	@Override
	public void onDrawShadow(Canvas canvas) {
		for(Drawable shadow : shadows) {
			shadow.draw(canvas);
		}
	}
}
