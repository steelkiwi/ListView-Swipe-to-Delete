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

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Vibrator;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.skd.swipetodelete.shake.ShakeDetectActivity;
import com.skd.swipetodelete.shake.ShakeDetectActivityListener;

/*
 * Class responsible for establishing list view gesture listener and device shake detector.
 * Shake detector is used to remove a group of marked list view items. 
 */

public class ItemListManager {
	private Context ctx;
	private ItemBaseListHandler listHandler;
	private GestureDetector swipeDetector;
	private ShakeDetectActivity shakeDetector;
	
	public ItemListManager(Context ctx, ItemBaseListHandler handler, boolean useShake) {
		this.ctx = ctx;
		this.listHandler = handler;
		
		swipeDetector = new GestureDetector(ctx, new ItemListGestureDetector(ctx.getResources().getDisplayMetrics(), listHandler));
		
		View.OnTouchListener gestureListener = new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				return swipeDetector.onTouchEvent(event);
			}
		};
		if ((listHandler != null) && (listHandler.getListView() != null)) {
			listHandler.getListView().setOnTouchListener(gestureListener);
		}
		
		if (useShake) {
			shakeDetector = new ShakeDetectActivity(ctx);
			shakeDetector.addListener(new ShakeDetectActivityListener() {
				@Override
				public void shakeDetected() {
					if ((listHandler != null) && (!listHandler.hasMarkedToRemoveItems())) {
						return;
					}
					showRemoveItemsConfirmDialog();
				}
			});
		}	
	}
	
	public void resume() {
		if (shakeDetector != null) {
			shakeDetector.onResume();
		}
	}

	public void pause() {
		if (shakeDetector != null) {
			shakeDetector.onPause();
		}
	}
	
	public void destroy() {
		ctx = null;
		listHandler = null;
		swipeDetector = null;
		shakeDetector = null;
	}
	
	private void showRemoveItemsConfirmDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setMessage("Remove all selected items?")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								if (listHandler != null) { 
									listHandler.removeAllItems();
								}
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
		Vibrator v = (Vibrator) ctx.getSystemService(Context.VIBRATOR_SERVICE);
		if (v != null) { v.vibrate(500); }
	}
}
