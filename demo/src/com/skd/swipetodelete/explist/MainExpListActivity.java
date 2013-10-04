package com.skd.swipetodelete.explist;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ExpandableListView;

import com.skd.swipetodelete.Item;
import com.skd.swipetodelete.ItemBase;
import com.skd.swipetodelete.R;
import com.skd.swipetodelete.menu.MenuItemDesc;

/*
 * Main activity class for expandable list view animations demo.
 * The following are established:
 * - list view;
 * - list adapter inherited from ItemBaseListAdapter;
 * - list handler inherited from ItemBaseListHandler which overrides a callback method when menu item is triggered;
 * - list manager which is responsible for swipe and shake detection (the 3rd parameter in constructor enables shake detection).
 * Mark as removed child items are placed at the bottom of the list's group.
 * Two sample menu actions are implemented: edit list view item and share. 
 */

public class MainExpListActivity extends Activity {
	private ExpandableListView list;
	private ItemExpListAdapter listAdapter;
	private ItemExpListHandler listHandler;
	private ItemExpListManager listManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_explist);

		list = (ExpandableListView) findViewById(android.R.id.list);
		
		listHandler = new ItemExpListHandler(list);
		
		ArrayList<MenuItemDesc> menuItems = fillMenu();
		
		listAdapter = new ItemExpListAdapter(R.layout.item, menuItems, listHandler);
		fillAdapter();
		list.setAdapter(listAdapter);

		listManager = new ItemExpListManager(this, listHandler, true);
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (listManager != null) {
			listManager.pause();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (listManager != null) {
			listManager.resume();
		}
	}
	
	@Override
	protected void onDestroy() {
		if (listManager != null) {
			listManager.destroy();
		}
		super.onDestroy();
	}
	
	private void fillAdapter() {
		for (int i=0; i<2; i++) {
			listAdapter.addGroup("Group " + (i + 1));
			ArrayList<ItemBase> items = new ArrayList<ItemBase>();
			for (int j=0; j<3; j++) {
				items.add(new Item((j + 1), "Item " + (j + 1)));
			}
			listAdapter.addChild(items);
		}
	}

	private ArrayList<MenuItemDesc> fillMenu() {
		ArrayList<MenuItemDesc> items = new ArrayList<MenuItemDesc>();
		
		MenuItemDesc itemEdit = new MenuItemDesc();
		itemEdit.setAction(getString(R.string.editAction));
		itemEdit.setIcon(R.drawable.btn_edit);
		items.add(itemEdit);
		
		MenuItemDesc itemShare = new MenuItemDesc();
		itemShare.setAction(getString(R.string.shareAction));
		itemShare.setIcon(R.drawable.btn_share);
		items.add(itemShare);
	
		return items;
	}

	public class ItemExpListHandler extends ItemBaseExpListHandler {

		public ItemExpListHandler(ExpandableListView list) {
			super(list);
		}

		@Override
		public void onItemMenuAction(int groupPosition, int childPosition, String action) {
			if (action.equals(getString(R.string.editAction))) {
				showEditItemConfirmDialog(groupPosition, childPosition);
			}
			else if (action.equals(getString(R.string.shareAction))) {
				showShareItemDialog(groupPosition, childPosition);
			}
		}
	}
	
	private void showEditItemConfirmDialog(final int groupPosition, final int childPosition) {
		final EditText itemText = new EditText(this); 
		
		AlertDialog.Builder builder = new AlertDialog.Builder(MainExpListActivity.this);
		builder.setMessage("Edit task?")
				.setCancelable(false)
				.setView(itemText)
				.setPositiveButton("Save changes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								listAdapter.edit(groupPosition, childPosition, itemText.getText().toString());
							}
						})
				.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	private void showShareItemDialog(int groupPosition, int childPosition) {
		Intent i = new Intent();
		i.setAction(Intent.ACTION_SEND);
		i.putExtra(Intent.EXTRA_TEXT, ((Item) listAdapter.getChild(groupPosition, childPosition)).getText());
		i.setType("text/plain");
		startActivity(i);
	}
}
