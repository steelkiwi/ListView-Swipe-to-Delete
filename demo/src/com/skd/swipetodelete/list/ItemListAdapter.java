package com.skd.swipetodelete.list;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.skd.swipetodelete.Item;
import com.skd.swipetodelete.R;
import com.skd.swipetodelete.menu.MenuItemDesc;

/*
 * Represents a list view adapter class inherited from ItemBaseListAdapter.
 * Override getView() method to set a custom content, super getView() will do the rest.
 */

public class ItemListAdapter extends ItemBaseListAdapter {

	public ItemListAdapter(int itemLayoutID, ArrayList<MenuItemDesc> menuItems, ItemBaseListHandler handler) {
		super(itemLayoutID, menuItems, handler);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);
		
		Item dt = (Item) data.get(position);
			
		TextView text = (TextView) view.findViewById(R.id.text);
		text.setText(dt.getText());
		
		return view;
	}
	
	//actions ***********************************************************************************
	
	public void edit(int position, String text) {
		if (text.length() <= 0) { return; }
		((Item) this.data.get(position)).setText(text);
		notifyDataSetChanged();
	}
}
