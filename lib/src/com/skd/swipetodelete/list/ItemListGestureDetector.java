/*
 * Copyright (C) 2013 Steelkiwi Development, Julia Zudikova
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * 		http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.skd.swipetodelete.list;

import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.GestureDetector.SimpleOnGestureListener;

/*
 * Represents a class detecting gestures on a list view item:
 * - single tap -> opens item menu;
 * - swipe from left to right -> marks item as removed;
 * - swipe from right to left -> undoes item's removal.
 */

public class ItemListGestureDetector extends SimpleOnGestureListener {
	private int REL_SWIPE_MIN_DISTANCE;
	private int REL_SWIPE_MAX_OFF_PATH;
	private int REL_SWIPE_THRESHOLD_VELOCITY;
	
	private int temp_position = -1;

	private ItemBaseListHandler itemListHandler;
	
	public ItemListGestureDetector(DisplayMetrics dm, ItemBaseListHandler itemListHandler) {
		REL_SWIPE_MIN_DISTANCE = (int) (120.0f * dm.densityDpi / 160.0f + 0.5);
		REL_SWIPE_MAX_OFF_PATH = (int) (250.0f * dm.densityDpi / 160.0f + 0.5);
		REL_SWIPE_THRESHOLD_VELOCITY = (int) (200.0f * dm.densityDpi / 160.0f + 0.5);
		this.itemListHandler = itemListHandler;
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		int pos = itemListHandler.getListView().pointToPosition((int) e.getX(), (int) e.getY());
		if (pos >= 0) {
			itemListHandler.onItemClicked(pos);
		}	
		return true;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		temp_position = itemListHandler.getListView().pointToPosition((int) e.getX(), (int) e.getY());
		return super.onDown(e);
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		if (Math.abs(e1.getY() - e2.getY()) > REL_SWIPE_MAX_OFF_PATH) { return false; }
		if ((e1.getX() - e2.getX() > REL_SWIPE_MIN_DISTANCE) &&
			(Math.abs(velocityX) > REL_SWIPE_THRESHOLD_VELOCITY)) {
			int pos = itemListHandler.getListView().pointToPosition((int) e1.getX(), (int) e2.getY());
			if (pos >= 0 && temp_position == pos) { 
				itemListHandler.onItemSwiped(false, pos); 
			}
		}
		else if ((e2.getX() - e1.getX() > REL_SWIPE_MIN_DISTANCE) && 
				 (Math.abs(velocityX) > REL_SWIPE_THRESHOLD_VELOCITY)) {
			int pos = itemListHandler.getListView().pointToPosition((int) e1.getX(), (int) e2.getY());
			if (pos >= 0 && temp_position == pos) {
				itemListHandler.onItemSwiped(true, pos);
			}
		}
		return false;
	}
}