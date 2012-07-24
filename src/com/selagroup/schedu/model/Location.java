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
}
