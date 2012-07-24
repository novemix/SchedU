package com.selagroup.schedu.database;

import android.content.ContentValues;

public abstract class ContentValueItem {
	private int mID;
	
	public ContentValueItem(int iID) {
		mID = iID;
	}
	
	public final int getID() {
		return mID;
	}
	
	public final void setID(int iID) {
		mID = iID;
	}
	
	public abstract ContentValues getValues();
}
