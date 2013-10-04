package com.skd.swipetodelete.list;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;

import com.skd.swipetodelete.Item;
import com.skd.swipetodelete.R;
import com.skd.swipetodelete.menu.MenuItemDesc;

/*
 * Main activity class for list view animations demo.
 * The following are established:
 * - list view;
 * - list adapter inherited from ItemBaseListAdapter;
 * - list handler inherited from ItemBaseListHandler which overrides a callback method when menu item is triggered;
 * - list manager which is responsible for swipe and shake detection (the 3rd parameter in constructor enables shake detection).
 * Mark as removed list view items are placed at the bottom of the list view.
 * Two sample menu actions are implemented: edit list view item and share. 
 */

public class MainListActivity extends Activity {
	private ListView list;
	private ItemListAdapter listAdapter;
	private ItemListHandler listHandler;
	private ItemListManager listManager;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_list);

		list = (ListView) findViewById(android.R.id.list);
		
		listHandler = new ItemListHandler(list);
		
		ArrayList<MenuItemDesc> menuItems = fillMenu();
		
		listAdapter = new ItemListAdapter(R.layout.item, menuItems, listHandler);
		fillAdapter();
		list.setAdapter(listAdapter);

		listManager = new ItemListManager(this, listHandler, true);
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
		for (int i=0; i<8; i++) {
			listAdapter.add(new Item((i + 1), "Item " + (i + 1)));
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
	
	public class ItemListHandler extends ItemBaseListHandler {

		public ItemListHandler(ListView list) {
			super(list);
		}

		@Override
		public void onItemMenuAction(int position, String action) {
			if (action.equals(getString(R.string.editAction))) {
				showEditItemConfirmDialog(position);
			}
			else if (action.equals(getString(R.string.shareAction))) {
				showShareItemDialog(position);
			}
		}
	}
	
	private void showEditItemConfirmDialog(final int position) {
		final EditText itemText = new EditText(this); 
		
		AlertDialog.Builder builder = new AlertDialog.Builder(MainListActivity.this);
		builder.setMessage("Edit item?")
				.setCancelable(false)
				.setView(itemText)
				.setPositiveButton("Save changes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								listAdapter.edit(position, itemText.getText().toString());
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
	
	private void showShareItemDialog(int position) {
		Intent i = new Intent();
		i.setAction(Intent.ACTION_SEND);
		i.putExtra(Intent.EXTRA_TEXT, ((Item) listAdapter.getItem(position)).getText());
		i.setType("text/plain");
		startActivity(i);
	}
}
