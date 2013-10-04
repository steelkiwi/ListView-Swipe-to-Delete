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

package com.skd.swipetodelete.explist;

import android.widget.ExpandableListView;

/*
 * Represents a base class holding callback methods for the following actions:
 * - expandable list view item is clicked;
 * - expandable list view item is swiped;
 * - expandable list view item is about to be removed;
 * - a menu action is triggered on an expandable list view item.
 */

public abstract class ItemBaseExpListHandler {
	private ExpandableListView list;
	
	public ItemBaseExpListHandler(ExpandableListView list) {
		this.list = list;
	}
	
	public void onGroupClicked(int groupPosition) {}
	
	public void onItemClicked(int groupPosition, int childPosition) {
		if ((list == null) && (((ItemBaseExpListAdapter) list.getExpandableListAdapter()) == null)) { return; }
		
		((ItemBaseExpListAdapter) list.getExpandableListAdapter()).showItemMenu(groupPosition, childPosition);
	}
	
	public void onItemSwiped(boolean isRight, int groupPosition, int childPosition) {
		if ((list == null) && (((ItemBaseExpListAdapter) list.getExpandableListAdapter()) == null)) { return; }
		
		if (isRight) {
			((ItemBaseExpListAdapter) list.getExpandableListAdapter()).markToRemove(groupPosition, childPosition);
		} else {
			((ItemBaseExpListAdapter) list.getExpandableListAdapter()).unmarkToRemove(groupPosition, childPosition);
		}
		((ItemBaseExpListAdapter) list.getExpandableListAdapter()).notifyDataSetChanged();
	}

	public void onItemRemove(int groupPosition, int childPosition) {
		if ((list == null) && (((ItemBaseExpListAdapter) list.getExpandableListAdapter()) == null)) { return; }
		
		((ItemBaseExpListAdapter) list.getExpandableListAdapter()).remove(groupPosition, childPosition);
		((ItemBaseExpListAdapter) list.getExpandableListAdapter()).notifyDataSetChanged();
	}
	
	public abstract void onItemMenuAction(int groupPosition, int childPosition, String action);

	public ExpandableListView getListView() {
		return list;
	}
	
	public boolean hasMarkedToRemoveItems() {
		if ((list == null) && (((ItemBaseExpListAdapter) list.getExpandableListAdapter()) == null)) { return false; }
		
		return ((ItemBaseExpListAdapter) list.getExpandableListAdapter()).hasMarkedToRemove();
	}
	
	public void removeAllItems() {
		if ((list == null) && (((ItemBaseExpListAdapter) list.getExpandableListAdapter()) == null)) { return; }
		
		((ItemBaseExpListAdapter) list.getExpandableListAdapter()).removeAll();
	}
}
