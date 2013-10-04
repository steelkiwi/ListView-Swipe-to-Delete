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

package com.skd.swipetodelete.menu;

/*
 * A description class for list item's menu cell used by MenuItemBuilder to construct an image view which represents a menu cell. 
 */

public class MenuItemDesc {
	private String action;
	private int iconDrawableID;
	
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public int getIcon() {
		return iconDrawableID;
	}
	public void setIcon(int iconDrawableID) {
		this.iconDrawableID = iconDrawableID;
	}
}
