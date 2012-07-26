/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */

package com.selagroup.schedu.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.selagroup.schedu.database.DBHelper;

import android.content.ContentValues;

/**
 * The Class Instructor.
 */
public class Instructor extends ContentValueItem {
    private static final long serialVersionUID = 2064735598532482711L;
    
	private String mName;
	private String mPhone;
	private String mEmail;
	private Location mLocation;
	private ArrayList<TimePlaceBlock> mOfficeHours = new ArrayList<TimePlaceBlock>();
	
	public Instructor(int iID, String iName, String iPhone, String iEmail) {
		super(iID);
		mName = iName;
		mPhone = iPhone;
		mEmail = iEmail;
	}

	public void addOfficeBlock(TimePlaceBlock iBlock) {
		mOfficeHours.add(iBlock);
	}
	
	public void removeOfficeBlock(TimePlaceBlock iBlock) {
		mOfficeHours.remove(iBlock);
	}
	
	public List<TimePlaceBlock> getOfficeBlocks() {
		return Collections.unmodifiableList(mOfficeHours);
	}

	public ContentValues getValues() {
		ContentValues values = new ContentValues();
		values.put(DBHelper.COL_INSTRUCTOR_Name, mName);
		values.put(DBHelper.COL_INSTRUCTOR_Email, mEmail);
		values.put(DBHelper.COL_INSTRUCTOR_Phone, mPhone);
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
	
	public Location getLocation() {
		return mLocation;
	}
	
	@Override
	public String toString() {
		return mName;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof Instructor) {
			return mName.equalsIgnoreCase(((Instructor) other).mName);
		}
		return false;
	}
}
