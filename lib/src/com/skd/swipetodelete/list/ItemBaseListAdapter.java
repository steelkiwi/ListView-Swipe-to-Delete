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

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.skd.swipetodelete.ItemBase;
import com.skd.swipetodelete.R;
import com.skd.swipetodelete.menu.MenuItemBuilder;
import com.skd.swipetodelete.menu.MenuItemDesc;
import com.skd.swipetodelete.utils.DimenUtils;

/*
 * Represents a list view adapter class responsible for list view animations and actions.
 * It uses provided list item content layout and templates:
 * - for general item with hidden menu;
 * - for item marked to be removed. 
 * Inherit this class and override getView() method only to set custom content.
 */

public class ItemBaseListAdapter extends BaseAdapter {
	private static final int ITEM_MENU_ANIM_DURATION  = 500;
	private static final int REMOVE_BTN_ANIM_DURATION = 200;
	private static final int CROSS_LINE_ANIM_DURATION = 400;
	
	private static final String GENERAL_ITEM_LAYOUT_TAG = "general";
	private static final String CROSSED_ITEM_LAYOUT_TAG = "crossed";
	
	protected ArrayList<ItemBase> data;
	private ArrayList<MenuItemDesc> menuItemsDesc;
	private ItemBaseListHandler handler;
	private int itemLayoutID = 0;
	private int menuLayoutHeight = 0;
	
	public ItemBaseListAdapter(int itemLayoutID, ArrayList<MenuItemDesc> menuItemsDesc, ItemBaseListHandler handler) {
		this.data = new ArrayList<ItemBase>();
		this.menuItemsDesc = menuItemsDesc;
		this.handler   = handler;
		this.itemLayoutID = itemLayoutID;
	}

	public void set(ArrayList<ItemBase> dt) {
		this.data.addAll(dt);
	}
	
	public void add(ItemBase dt) {
		this.data.add(dt);
	}
	
	public void clear() {
		if (this.data != null) {
			this.data.clear();
			this.data = null;
		}
	}
	
	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		ItemBase dt = data.get(position);
		
		if (dt.isMarkedToDelete()) {
			view = getItemMarkedView(position, convertView, parent);
		}
		else {
			view = getItemWithMenuView(position, convertView, parent);
		}
		
		view.clearAnimation();
		return view;
	}
	
	//item with menu view ***********************************************************************
	
	private LinearLayout getItemWithMenuView(final int position, View convertView, ViewGroup parent) {
		LinearLayout view = null;
		ItemBase dt = data.get(position);
		
		if ((convertView == null) || (convertView.getTag().equals(CROSSED_ITEM_LAYOUT_TAG))) {
			view = createItemWithMenuContentView(parent.getContext(), R.layout.item_with_menu);
		}
		else {
			view = (LinearLayout) convertView;
		}
		view.setTag(GENERAL_ITEM_LAYOUT_TAG);
		
		final LinearLayout menuLayout = (LinearLayout) view.findViewById(R.id.menuLayout);
		if (menuLayoutHeight <= 0) {
			menuLayoutHeight = DimenUtils.getViewHeight(parent.getContext(), menuLayout);
		}
		setMenuLayoutHeight(menuLayout, 0);
		if (dt.isMarkedToShowMenu()) {
			setMenuLayoutHeight(menuLayout, menuLayoutHeight);
/*			if (dt.isNeedMenuAnim()) {
				//TODO start menu layout animation
			}
			else {
				setMenuLayoutHeight(menuLayout, menuLayoutHeight);
			}*/
		}
		
		if (menuLayout.getChildCount() > 0) {
			for (int i=0; i<menuLayout.getChildCount(); i++) {
				final ImageView item = (ImageView) menuLayout.getChildAt(i);
				menuLayout.getChildAt(i).setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						handler.onItemMenuAction(position, item.getTag().toString());
					}
				});
			}
		}
		
		return view;
	}
	
	private LinearLayout createItemWithMenuContentView(Context ctx, int parentLayoutID) {
		LinearLayout view = new LinearLayout(ctx);
		view.setOrientation(LinearLayout.VERTICAL);
		LayoutInflater li = LayoutInflater.from(ctx);
		li.inflate(R.layout.item_with_menu, view);
		
		View contentView = li.inflate(itemLayoutID, null);
		view.addView(contentView, 0);
		
		if ((menuItemsDesc != null) && (menuItemsDesc.size() > 0)) {
			attachMenuItems(ctx, (LinearLayout) view.findViewById(R.id.menuLayout));
		}
		
		return view;
	}
	
	private void attachMenuItems(Context ctx, LinearLayout menuLayout) {
		menuLayout.setOrientation(LinearLayout.HORIZONTAL);
		ArrayList<ImageView> items = fillMenu(ctx);
		for (int i=0; i<items.size(); i++) {
			menuLayout.addView(items.get(i));
		}
	}
	
	private ArrayList<ImageView> fillMenu(Context ctx) {
		ArrayList<ImageView> items = new ArrayList<ImageView>();
		MenuItemBuilder builder = new MenuItemBuilder();
		
		for (int i=0; i<menuItemsDesc.size(); i++) {
			builder.setItemDesc(menuItemsDesc.get(i));
			items.add(builder.build(ctx));
		}
		
		return items;
	}
	
	private void setMenuLayoutHeight(LinearLayout menuLayout, int newHeight) {
		LinearLayout.LayoutParams la = (LinearLayout.LayoutParams)(menuLayout.getLayoutParams());
		la.height = newHeight;
		menuLayout.setLayoutParams(la);
	}
	
	//marked item view **************************************************************************
	
	private FrameLayout getItemMarkedView(final int position, View convertView, ViewGroup parent) {
		FrameLayout view = null;
		ItemBase dt = data.get(position);
		
		if ((convertView == null) || (convertView.getTag().equals(GENERAL_ITEM_LAYOUT_TAG))) {
			view = createItemMarkedContentView(parent.getContext(), R.layout.item_marked);
		}
		else {
			view = (FrameLayout) convertView;
		}
		view.setTag(CROSSED_ITEM_LAYOUT_TAG);
		
		View lineView = (View) view.findViewById(R.id.line);
		
		ImageView deleteBtn = (ImageView) view.findViewById(R.id.deleteBtn);
		deleteBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (handler != null) {
					handler.onItemRemove(position);
				}
			}
		});
		
		if (dt.isNeedDeleteAnim()) {
			cross(position, lineView, deleteBtn, view.getChildAt(0), parent.getResources());
		}
		else {
			setCrossStyle(view.getChildAt(0), true);
		}
		
		return view;
	}
	
	private FrameLayout createItemMarkedContentView(Context ctx, int parentLayoutID) {
		FrameLayout view = new FrameLayout(ctx);
		LayoutInflater li = LayoutInflater.from(ctx);
		li.inflate(R.layout.item_marked, view);
		
		View contentView = li.inflate(itemLayoutID, null);
		view.addView(contentView, 0);
		
		return view;
	}
	
	//animations ********************************************************************************
	
	private void cross(final int position, final View lineView, final ImageView deleteBtn, final View contentView, Resources r) {
		final ItemBase dt = data.get(position);
		
		Animation lineViewAnimation = new ScaleAnimation(0f, 1f, 1f, 1f, 0, 0);
		lineViewAnimation.setDuration(CROSS_LINE_ANIM_DURATION);
		lineViewAnimation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {}
			
			@Override
			public void onAnimationRepeat(Animation animation) {}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				lineView.clearAnimation();
				setCrossStyle(contentView, true);
				moveItemToBottom(position);
				dt.setNeedDeleteAnim(false);
			}
		});
		
		Animation deleteBtnAnimation = new TranslateAnimation(DimenUtils.getScreenWidth(r), deleteBtn.getLeft(), 0, 0);
		deleteBtnAnimation.setDuration(REMOVE_BTN_ANIM_DURATION);
		deleteBtnAnimation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {}
			
			@Override
			public void onAnimationRepeat(Animation animation) {}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				deleteBtn.clearAnimation();
			}
		});
		
		lineView.startAnimation(lineViewAnimation);
		deleteBtn.startAnimation(deleteBtnAnimation);
	}
	
	private void moveItemToBottom(final int position) {
		final AbsListView view = handler.getListView();
		
        int localIndex = position - view.getFirstVisiblePosition();

        int childCount = view.getChildCount() - 1; 
        if (localIndex > 0) {
        	childCount -= (position - view.getFirstVisiblePosition());
        }

        int moveDelta = 0;
        for (int i = 1, max = childCount; i <= max; i++) {
            View v = view.getChildAt(localIndex + i);
            moveDelta += v.getHeight();
        }

        final View viewToMove = view.getChildAt(localIndex);
        int collapseDelta = viewToMove.getHeight();

        Animation viewAnimation = new TranslateAnimation(0, 0, 0, moveDelta);
        viewAnimation.setDuration(ITEM_MENU_ANIM_DURATION);
        viewAnimation.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationRepeat(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
            	swapItemsBottom(position);
            }
        });

        for (int i = 1, max = childCount; i <= max; i++) {
            View v = view.getChildAt(localIndex + i);
            Animation collAnim = new TranslateAnimation(0, 0, 0, -collapseDelta);
            collAnim.setDuration(ITEM_MENU_ANIM_DURATION);
            v.startAnimation(collAnim);
        }

        viewToMove.startAnimation(viewAnimation);
	}
	
	private void swapItemsBottom(int position) {
		ItemBase item = this.data.get(position);
		this.data.remove(position);
		this.data.add(item);
		notifyDataSetChanged();
	}
	
	private void swapItemsTop(int position) {
		ItemBase item = this.data.get(position);
		this.data.remove(position);
		this.data.add(0, item);
		notifyDataSetChanged();
	}
	
	private void setCrossStyle(View contentView, boolean cross) {
		if (contentView == null) { return; }
		if (cross) {
			contentView.setAlpha(0.5f);
		}
		else {
			contentView.setAlpha(1f);
		}
	}
	
	//actions ***********************************************************************************
	
	public void showItemMenu(int position) {
		if (!this.data.get(position).isMarkedToDelete()) {
			this.data.get(position).setMarkedToShowMenu(!this.data.get(position).isMarkedToShowMenu());
			this.data.get(position).setNeedMenuAnim(this.data.get(position).isMarkedToShowMenu());
		}
		resetNeedShowMenu(position);
		notifyDataSetChanged();
	}
	
	private void resetNeedShowMenu(int position) {
		for (int i=0; i<this.data.size(); i++) {
			if (i == position) { continue; }
			this.data.get(i).setMarkedToShowMenu(false);
			this.data.get(i).setNeedMenuAnim(false);
		}
	}
	
	public void markToRemove(int position) {
		resetNeedShowMenu(-1);
		if (!this.data.get(position).isMarkedToDelete()) {
			this.data.get(position).setMarkedToDelete(true);
			this.data.get(position).setNeedDeleteAnim(true);
		}	
		notifyDataSetChanged();
	}
	
	public void unmarkToRemove(int position) {
		resetNeedShowMenu(-1);
		if (this.data.get(position).isMarkedToDelete()) {
			this.data.get(position).setMarkedToDelete(false);
			this.data.get(position).setNeedDeleteAnim(false);
			swapItemsTop(position);
		}	
		notifyDataSetChanged();
	}
	
	public void remove(int position) {
		resetNeedShowMenu(-1);
		this.data.remove(position);
		notifyDataSetChanged();
	}
	
	public void removeAll() {
		resetNeedShowMenu(-1);
		for (int i=0; i<this.data.size(); i++) {
			if (this.data.get(i).isMarkedToDelete()) {
				this.data.remove(i);
				i--;
			}
		}
		notifyDataSetChanged();
	}
	
	public boolean hasMarkedToRemove() {
		for (int i=0; i<this.data.size(); i++) {
			if (this.data.get(i).isMarkedToDelete()) {
				return true;
			}
		}
		return false;
	}
}