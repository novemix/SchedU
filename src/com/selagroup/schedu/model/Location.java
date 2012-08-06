/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */

package com.selagroup.schedu.model;

import com.selagroup.schedu.database.DBHelper;

import android.content.ContentValues;


/**
 * The Class Location.
 */
public class Location extends ContentValueItem {
    private static final long serialVersionUID = 2953150752015996296L;
    
	private String mBuilding;
	private String mRoom;
	private String mGeoLocation;
	
	public Location(int iID, String iBuilding, String iRoom, String iGeoLocation) {
		super(iID);
		mBuilding = iBuilding;
		mRoom = iRoom;
		mGeoLocation = iGeoLocation;
	}

	public ContentValues getValues() {
		ContentValues values = new ContentValues();
		values.put(DBHelper.COL_LOCATION_Building, mBuilding);
		values.put(DBHelper.COL_LOCATION_Room, mRoom);
		values.put(DBHelper.COL_LOCATION_GeoLocation, mGeoLocation);
		return values;
	}

	public String getBuilding() {
		return mBuilding;
	}
	
	public void setBuilding(String iBuilding) {
		mBuilding = iBuilding;
	}
	
	public String getRoom() {
		return mRoom;
	}
	
	public void setRoom(String iRoom) {
		mRoom = iRoom;
	}
	
	@Override
	public String toString() {
		String retString = ((mBuilding == null) || mBuilding.equals("")) ?  "" : mBuilding;
		retString += ((mRoom == null) || mRoom.equals("")) ? "" : " \u2014 " + mRoom;

		return retString;
	}
}
