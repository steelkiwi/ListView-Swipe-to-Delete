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

package com.skd.swipetodelete;

/*
 * Represents a parent class for a custom data class used in list adapter.
 * Holds helper flags used in list animations.
 */

public abstract class ItemBase {
	protected boolean isMarkedToDelete;
	protected boolean needDeleteAnim;
	protected boolean isMarkedToShowMenu;
	protected boolean needMenuAnim;
	
	public ItemBase() {
		this.isMarkedToDelete = false;
		this.needDeleteAnim   = false;
		this.isMarkedToShowMenu = false;
		this.needMenuAnim       = false;
	}
	
	public boolean isMarkedToDelete() {
		return isMarkedToDelete;
	}
	public void setMarkedToDelete(boolean isMarkedToDelete) {
		this.isMarkedToDelete = isMarkedToDelete;
	}
	public boolean isNeedDeleteAnim() {
		return needDeleteAnim;
	}
	public void setNeedDeleteAnim(boolean needDeleteAnim) {
		this.needDeleteAnim = needDeleteAnim;
	}
	public boolean isMarkedToShowMenu() {
		return isMarkedToShowMenu;
	}
	public void setMarkedToShowMenu(boolean isMarkedToShowMenu) {
		this.isMarkedToShowMenu = isMarkedToShowMenu;
	}
	public boolean isNeedMenuAnim() {
		return needMenuAnim;
	}
	public void setNeedMenuAnim(boolean needMenuAnim) {
		this.needMenuAnim = needMenuAnim;
	}
}
