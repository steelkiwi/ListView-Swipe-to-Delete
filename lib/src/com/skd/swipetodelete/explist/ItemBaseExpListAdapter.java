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
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.skd.swipetodelete.ItemBase;
import com.skd.swipetodelete.R;
import com.skd.swipetodelete.menu.MenuItemBuilder;
import com.skd.swipetodelete.menu.MenuItemDesc;
import com.skd.swipetodelete.utils.DimenUtils;

/*
 * Represents an expandable list view adapter class responsible for expandable list view animations and actions.
 * It uses provided child item content layout and templates:
 * - for general child item with hidden menu;
 * - for child item marked to be removed. 
 * Inherit this class and override getGroupView() and getChildView() methods only to set custom content.
 */

public class ItemBaseExpListAdapter extends BaseExpandableListAdapter {
	private static final int ITEM_MENU_ANIM_DURATION  = 500;
	private static final int REMOVE_BTN_ANIM_DURATION = 200;
	private static final int CROSS_LINE_ANIM_DURATION = 400;
	
	private static final String GENERAL_ITEM_LAYOUT_TAG = "general";
	private static final String CROSSED_ITEM_LAYOUT_TAG = "crossed";
	
	protected ArrayList<String> groupData;
	protected ArrayList<ArrayList<ItemBase>> data;
	private ArrayList<MenuItemDesc> menuItemsDesc;
	private ItemBaseExpListHandler handler;
	private int itemLayoutID = 0;
	private int menuLayoutHeight = 0;
	
	public ItemBaseExpListAdapter(int itemLayoutID, ArrayList<MenuItemDesc> menuItemsDesc, ItemBaseExpListHandler handler) {
		this.groupData = new ArrayList<String>();
		this.data = new ArrayList<ArrayList<ItemBase>>();
		this.menuItemsDesc = menuItemsDesc;
		this.handler = handler;
		this.itemLayoutID = itemLayoutID;
	}
	
	//groups ************************************************************************************
	
	public void setGroup(ArrayList<String> groupDt) {
		this.groupData.addAll(groupDt);
	}
	
	public void addGroup(String groupDt) {
		this.groupData.add(groupDt);
	}

	@Override
	public int getGroupCount() {
		return this.groupData.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}
	
	@Override
	public Object getGroup(int groupPosition) {
		return this.groupData.get(groupPosition);
	}
	
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		return null;
	}
	
	//children **********************************************************************************
	
	public void addChild(ArrayList<ItemBase> dt) {
		this.data.add(dt);
	}
	
	@Override
	public int getChildrenCount(int groupPosition) {
		return this.data.get(groupPosition).size();
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}
	
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return this.data.get(groupPosition).get(childPosition);
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, final ViewGroup parent) {
		View view = null;
		ItemBase dt = data.get(groupPosition).get(childPosition);
		
		if (dt.isMarkedToDelete()) {
			view = getItemMarkedView(groupPosition, childPosition, convertView, parent);
		}
		else {
			view = getItemWithMenuView(groupPosition, childPosition, convertView, parent);
		}
		
		view.clearAnimation();
		return view;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
	
	//item with menu view ***********************************************************************
	
	private LinearLayout getItemWithMenuView(final int groupPosition, final int childPosition, View convertView, ViewGroup parent) {
		LinearLayout view = null;
		ItemBase dt = data.get(groupPosition).get(childPosition);
		
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
						handler.onItemMenuAction(groupPosition, childPosition, item.getTag().toString());
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
	
	private FrameLayout getItemMarkedView(final int groupPosition, final int childPosition, View convertView, ViewGroup parent) {
		FrameLayout view = null;
		ItemBase dt = data.get(groupPosition).get(childPosition);
		
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
					handler.onItemRemove(groupPosition, childPosition);
				}
			}
		});
		
		if (dt.isNeedDeleteAnim()) {
			cross(groupPosition, childPosition, lineView, deleteBtn, view.getChildAt(0), parent.getResources());
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

	private void cross(final int groupPosition, final int childPosition, final View lineView, final ImageView deleteBtn, final View contentView, Resources r) {
		final ItemBase dt = data.get(groupPosition).get(childPosition);
		
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
				moveItemToBottom(groupPosition, childPosition);
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
	
	private void moveItemToBottom(final int groupPosition, final int childPosition) {
		final AbsListView view = handler.getListView();
		
		long groupPackedPosition = ExpandableListView.getPackedPositionForGroup(groupPosition);
		int groupFlatPosition = handler.getListView().getFlatListPosition(groupPackedPosition);
		
		long childPackedPosition = ExpandableListView.getPackedPositionForChild(groupPosition, childPosition);
		int childFlatPosition = handler.getListView().getFlatListPosition(childPackedPosition);
		
        int localIndex = childFlatPosition - view.getFirstVisiblePosition();

        int childCount = this.data.get(groupPosition).size();
        if (localIndex > 0) {
        	childCount -= (childFlatPosition - groupFlatPosition);
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
            	swapItemsBottom(groupPosition, childPosition);
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
	
	private void swapItemsBottom(int groupPosition, int childPosition) {
		ItemBase item = this.data.get(groupPosition).get(childPosition);
		this.data.get(groupPosition).remove(childPosition);
		this.data.get(groupPosition).add(item);
		notifyDataSetChanged();
	}
	
	private void swapItemsTop(int groupPosition, int childPosition) {
		ItemBase item = this.data.get(groupPosition).get(childPosition);
		this.data.get(groupPosition).remove(childPosition);
		this.data.get(groupPosition).add(0, item);
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
	
	public void showItemMenu(int groupPosition, int childPosition) {
		if (!this.data.get(groupPosition).get(childPosition).isMarkedToDelete()) {
			this.data.get(groupPosition).get(childPosition).setMarkedToShowMenu(!this.data.get(groupPosition).get(childPosition).isMarkedToShowMenu());
			this.data.get(groupPosition).get(childPosition).setNeedMenuAnim(this.data.get(groupPosition).get(childPosition).isMarkedToShowMenu());
		}
		resetNeedShowMenu(groupPosition, childPosition);
		notifyDataSetChanged();
	}
	
	private void resetNeedShowMenu(int groupPosition, int childPosition) {
		for (int i=0; i<this.data.size(); i++) {
			for (int j=0; j<this.data.get(i).size(); j++) {
				if ((i == groupPosition) && (j == childPosition)) { continue; }
				this.data.get(i).get(j).setMarkedToShowMenu(false);
				this.data.get(i).get(j).setNeedMenuAnim(false);
			}	
		}
	}
	
	public void markToRemove(int groupPosition, int childPosition) {
		resetNeedShowMenu(-1, -1);
		if (!this.data.get(groupPosition).get(childPosition).isMarkedToDelete()) {
			this.data.get(groupPosition).get(childPosition).setMarkedToDelete(true);
			this.data.get(groupPosition).get(childPosition).setNeedDeleteAnim(true);
		}	
	}
	
	public void unmarkToRemove(int groupPosition, int childPosition) {
		resetNeedShowMenu(-1, -1);
		if (this.data.get(groupPosition).get(childPosition).isMarkedToDelete()) {
			this.data.get(groupPosition).get(childPosition).setMarkedToDelete(false);
			this.data.get(groupPosition).get(childPosition).setNeedDeleteAnim(false);
			swapItemsTop(groupPosition, childPosition);
		}	
	}
	
	public void remove(int groupPosition, int childPosition) {
		resetNeedShowMenu(-1, -1);
		this.data.get(groupPosition).remove(childPosition);
		notifyDataSetChanged();
	}
	
	public void removeAll() {
		resetNeedShowMenu(-1, -1);
		for (int i=0; i<this.data.size(); i++) {
			for (int j=0; j<this.data.get(i).size(); j++) {
				if (this.data.get(i).get(j).isMarkedToDelete()) {
					this.data.get(i).remove(j);
					j--;
				}
			}
		}
		notifyDataSetChanged();
	}
	
	public boolean hasMarkedToRemove() {
		for (int i=0; i<this.data.size(); i++) {
			for (int j=0; j<this.data.get(i).size(); j++) {
				if (this.data.get(i).get(j).isMarkedToDelete()) {
					return true;
				}
			}
		}
		return false;
	}
}
