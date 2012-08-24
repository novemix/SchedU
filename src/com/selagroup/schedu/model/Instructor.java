/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */

package com.selagroup.schedu.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.ContentValues;

import com.selagroup.schedu.database.DBHelper;

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
		mLocation = new Location(-1, "", "", "");
	}

	public void addOfficeBlock(TimePlaceBlock iBlock) {
		mOfficeHours.add(iBlock);
	}
	
	public void removeOfficeBlock(TimePlaceBlock iBlock) {
		mOfficeHours.remove(iBlock);
	}
	
	public void clearOfficeBlocks() {
		mOfficeHours.clear();
	}
	
	public List<TimePlaceBlock> getOfficeBlocks() {
		return Collections.unmodifiableList(mOfficeHours);
	}

	public ContentValues getValues() {
		ContentValues values = new ContentValues();
		values.put(DBHelper.COL_INSTRUCTOR_Name, mName);
		values.put(DBHelper.COL_INSTRUCTOR_Email, mEmail);
		values.put(DBHelper.COL_INSTRUCTOR_Phone, mPhone);
		values.put(DBHelper.COL_INSTRUCTOR_LocationID, mLocation.getID());
		return values;
	}
	
	public String getName() {
		return mName;
	}
	
	public void setName(String iName) {
		mName = iName;
	}
	
	public String getPhone() {
		return mPhone;
	}
	
	public void setPhone(String iPhone) {
		mPhone = iPhone;
	}
	
	public String getEmail() {
		return mEmail;
	}
	
	public void setEmail(String iEmail) {
		mEmail = iEmail;
	}
	
	public Location getLocation() {
		return mLocation;
	}
	
	public boolean setLocation(Location iLocation) {
		if (iLocation != null) {
			mLocation = iLocation;
			return true;
		} else {
			return false;
		}
	}
	
	public void setBuilding(String iBuilding) {
		mLocation.setBuilding(iBuilding);
	}
	
	public void setRoom(String iRoom) {
		mLocation.setRoom(iRoom);
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
