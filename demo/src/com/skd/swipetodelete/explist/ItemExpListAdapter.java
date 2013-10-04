package com.skd.swipetodelete.explist;

import java.util.ArrayList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skd.swipetodelete.Item;
import com.skd.swipetodelete.R;
import com.skd.swipetodelete.menu.MenuItemDesc;

/*
 * Represents an expandable list view adapter class inherited from ItemBaseListAdapter.
 * Override getGroupView() and getChildView() methods to set a custom content, super getGroupView() and getChildView() will do the rest.
 */

public class ItemExpListAdapter extends ItemBaseExpListAdapter {

	public ItemExpListAdapter(int itemLayoutID, ArrayList<MenuItemDesc> menuItems, ItemBaseExpListHandler handler) {
		super(itemLayoutID, menuItems, handler);
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		super.getGroupView(groupPosition, isExpanded, convertView, parent);
		
		LinearLayout groupView = null;
		
		String group = groupData.get(groupPosition);
		
		if (convertView == null) {
			groupView = new LinearLayout(parent.getContext());
			LayoutInflater li = LayoutInflater.from(parent.getContext());
			li.inflate(R.layout.group, groupView);
		}
		else {
			groupView = (LinearLayout)convertView;
		}
		
		TextView text = (TextView) groupView.findViewById(R.id.text);
		text.setText(group);
		
		ExpandableListView eLV = (ExpandableListView) parent;
	    eLV.expandGroup(groupPosition);
		
		return groupView;
	}
	
	@Override
	public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, final ViewGroup parent) {
		View view = super.getChildView(groupPosition, childPosition, isLastChild, convertView, parent);
		
		Item dt = (Item) data.get(groupPosition).get(childPosition);
			
		TextView text = (TextView) view.findViewById(R.id.text);
		text.setText(dt.getText());
		
		return view;
	}
	
	//actions ***********************************************************************************
	
	public void edit(int groupPosition, int childPosition, String text) {
		if (text.length() <= 0) { return; }
		((Item) this.data.get(groupPosition).get(childPosition)).setText(text);
		notifyDataSetChanged();
	}
}
