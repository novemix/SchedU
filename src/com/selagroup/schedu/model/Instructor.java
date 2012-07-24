/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */

package com.selagroup.schedu.model;

import com.selagroup.schedu.database.DatabaseHelper;

import android.content.ContentValues;


/**
 * The Class Instructor.
 */
public class Instructor extends ContentValueItem {
	private String mName;
	private String mPhone;
	private String mEmail;
	
	public Instructor(int iID, String iName, String iPhone, String iEmail) {
		super(iID);
		mName = iName;
		mPhone = iPhone;
		mEmail = iEmail;
	}

	public ContentValues getValues() {
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.COL_INSTRUCTOR_Name, mName);
		values.put(DatabaseHelper.COL_INSTRUCTOR_Email, mEmail);
		values.put(DatabaseHelper.COL_INSTRUCTOR_Phone, mPhone);
		return values;
	}
	
	public String getName() {
		return mName;
	}
	
	public String getPhone() {
		return mPhone;
	}
	
	public String getEmail() {
		return mEmail;
	}
}
