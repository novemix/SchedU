/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */

package com.selagroup.schedu.model;

import android.content.ContentValues;

import com.selagroup.schedu.database.IContentValueItem;

/**
 * The Class Instructor.
 */
public class Instructor implements IContentValueItem {
	private int mID = -1;
	private String mName;
	
	public Instructor(String iName) {
		mName = iName;
	}

	public ContentValues getValues() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getID() {
		return mID;
	}

	public void setID(int iID) {
		mID = iID;
	}
}
