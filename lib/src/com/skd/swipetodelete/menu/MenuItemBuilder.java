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

import android.content.Context;
import android.widget.ImageView;

import com.skd.swipetodelete.R;
import com.skd.swipetodelete.utils.DimenUtils;

/*
 * A builder class helping to construct an image view which represents a menu cell from its description.
 */

public final class MenuItemBuilder {
	private static final int MENU_ITEM_PADDING = 12;
	
	private MenuItemDesc itemDesc;
	
	public MenuItemBuilder setItemDesc(MenuItemDesc itemDesc) {
		this.itemDesc = itemDesc;
		return this;
	}
	
	public ImageView build(Context ctx) {
		String action = itemDesc.getAction();
		if (action == null || action.equals("")) {
			throw new IllegalArgumentException("No menu action specified!");
		}
		int iconDrawableID = itemDesc.getIcon();
		if (iconDrawableID <= 0) {
			throw new IllegalArgumentException("No menu icon specified!");
		}
		
		ImageView item = new ImageView(ctx);
		item.setTag(action);
		item.setImageResource(iconDrawableID);
		item.setContentDescription(ctx.getString(R.string.icon));
		int padding = DimenUtils.getPixelsFromDP(ctx.getResources(), MENU_ITEM_PADDING);
		item.setPadding(padding, padding, padding, padding);
		return item;
	}
}
