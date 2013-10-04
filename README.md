ListView Swipe to Delete Library
=======================================

ListView Swipe to Delete is an Android library which offers a list view navigation mode similar to Any.do application.

You can:
* swipe from left to right to mark list item as completed (completed items are moved to the bottom of the list)
* swipe from right to left to undo completing
* press "X" button to remove completed list item from a list
* shake device to remove all completed list items from a list
* tap upon list item to reveal a context menu.

The library supports both ListView and ExpandableListView navigations.
See /demo folder for example how to use.

How to use
----------

1. Create own XML layout for a list view item.
   The library will later attach it to its XML templates for:
   * item with hidden context menu
   * completed item.

2. Define item's context menu if needed.
   It can be edit or share item's content, for instance. Menu is initially hidden and is shown if item is tapped.
   For every menu item you need to specify icon and action:
```java
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
```
   Received collection of menu items descriptions will later be used to create menu ImageViews.

3. In activity's onCreate() method get a reference to a list view:
```java
list = (ListView) findViewById(android.R.id.list);
```

4. Then, you need to create a class to handle menu items actions and pass a list view reference to it:
```java
listHandler = new ItemListHandler(list);
...
public class ItemListHandler extends ItemBaseListHandler {

	    public ItemListHandler(ListView list) {
		    super(list);
	    }

	    @Override
	    public void onItemMenuAction(int position, String action) {
		    if (action.equals(getString(R.string.editAction))) {
		    	//TODO do smth
		    }
		    else if (action.equals(getString(R.string.shareAction))) {
		    	//TODO do smth
		    }
	    }
}
```

5. Create list adapter.
   You need to pass:
   * an ID of your list item's content XML layout
   * a list of menu items descriptions
   * a list handler
```java
listAdapter = new ItemListAdapter(R.layout.item, menuItems, listHandler);
fillAdapter();
list.setAdapter(listAdapter);
```
   Then you need to override adapter's getView() method to set list item's data:
```java
@Override
public View getView(final int position, View convertView, ViewGroup parent) {
	        View view = super.getView(position, convertView, parent); //parent adapter is responsible for view creation and list animations
	
	        Item dt = (Item) data.get(position);
		
	        TextView text = (TextView) view.findViewById(R.id.text);
	        text.setText(dt.getText());
	
	        return view;
}
```

6. Create list manager class responsible for swipe and shake detection:
```java
listManager = new ItemListManager(this, listHandler, true); //the 3rd parameter in constructor enables shake detection
```

TO DO
-----

* add animation when context menu appears for a tapped list item.
