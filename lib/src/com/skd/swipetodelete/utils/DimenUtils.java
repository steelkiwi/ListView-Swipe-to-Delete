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

package com.skd.swipetodelete.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;

public class DimenUtils {
	public static int getViewHeight(Context ctx, View view) {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        view.measure(metrics.widthPixels, metrics.heightPixels);
        return view.getMeasuredHeight();
	}
	
	public static int getScreenWidth(Resources r) {
		return r.getDisplayMetrics().widthPixels;
	}
	
	public static int getPixelsFromDP(Resources r, int dpVal) {
    	return ((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, r.getDisplayMetrics()));
    }
}
