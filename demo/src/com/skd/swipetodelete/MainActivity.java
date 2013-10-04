package com.skd.swipetodelete;

import com.skd.swipetodelete.explist.MainExpListActivity;
import com.skd.swipetodelete.list.MainListActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/*
 * Main activity class. 
 */

public class MainActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		((Button) findViewById(R.id.listDemoBtn)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				launchListViewDemo();
			}
		});
		
		((Button) findViewById(R.id.expListDemoBtn)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				launchExpandableListViewDemo();
			}
		});
	}
	
	private void launchListViewDemo() {
		Intent i = new Intent(MainActivity.this, MainListActivity.class);
		startActivity(i);
	}
	
	private void launchExpandableListViewDemo() {
		Intent i = new Intent(MainActivity.this, MainExpListActivity.class);
		startActivity(i);
	}
}
