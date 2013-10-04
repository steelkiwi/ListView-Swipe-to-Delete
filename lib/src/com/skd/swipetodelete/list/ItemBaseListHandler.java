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

import android.widget.ListView;

/*
 * Represents a base class holding callback methods for the following actions:
 * - list view item is clicked;
 * - list view item is swiped;
 * - list view item is about to be removed;
 * - a menu action is triggered on a list view item.
 */

public abstract class ItemBaseListHandler {
	private ListView list;
	
	public ItemBaseListHandler(ListView list) {
		this.list = list;
	}
	
	public void onItemClicked(int position) {
		if ((list == null) && (((ItemBaseListAdapter) list.getAdapter()) == null)) { return; }
		
		((ItemBaseListAdapter) list.getAdapter()).showItemMenu(position);
	}
	
	public void onItemSwiped(boolean isRight, int position) {
		if ((list == null) && (((ItemBaseListAdapter) list.getAdapter()) == null)) { return; }
		
		if (isRight) {
			((ItemBaseListAdapter) list.getAdapter()).markToRemove(position);
		} else {
			((ItemBaseListAdapter) list.getAdapter()).unmarkToRemove(position);
		}
	}

	public void onItemRemove(int position) {
		if ((list == null) && (((ItemBaseListAdapter) list.getAdapter()) == null)) { return; }
		
		((ItemBaseListAdapter) list.getAdapter()).remove(position);
	}

	public abstract void onItemMenuAction(int position, String action);

	public ListView getListView() {
		return list;
	}
	
	public boolean hasMarkedToRemoveItems() {
		if ((list == null) && (((ItemBaseListAdapter) list.getAdapter()) == null)) { return false; }
		
		return ((ItemBaseListAdapter) list.getAdapter()).hasMarkedToRemove();
	}
	
	public void removeAllItems() {
		if ((list == null) && (((ItemBaseListAdapter) list.getAdapter()) == null)) { return; }
		
		((ItemBaseListAdapter) list.getAdapter()).removeAll();
	}
}
