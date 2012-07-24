/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */

package com.selagroup.schedu.model;

import android.content.ContentValues;

import com.selagroup.schedu.database.ContentValueItem;

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
		// TODO Auto-generated method stub
		return null;
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
