/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */
package com.selagroup.schedu.managers;

import android.database.Cursor;

import com.selagroup.schedu.database.DBHelper;
import com.selagroup.schedu.model.Location;

/**
 * The Class LocationManager.
 */
public class LocationManager extends Manager<Location> {

	public LocationManager(DBHelper iHelper) {
		super(iHelper);
	}

	@Override
	public int insert(Location iLocation) {
		if (iLocation == null) {
			return -1;
		}
		
		// If the location already exists, just update the entry
		if (get(iLocation.getID()) != null) {
			update(iLocation);
			return iLocation.getID();
		}

		open(OPEN_MODE.WRITE);
		int locationID = (int) mDB.insert(DBHelper.TABLE_Location, null, iLocation.getValues());
		iLocation.setID(locationID);
		
		close();
		return locationID;
	}

	@Override
	public void delete(Location iLocation) {
		open(OPEN_MODE.WRITE);
		mDB.delete(DBHelper.TABLE_Location, DBHelper.COL_LOCATION_ID + "=?", new String[] { "" + iLocation.getID() });
		close();
	}

	@Override
	public void update(Location iLocation) {
		open(OPEN_MODE.WRITE);
		mDB.update(DBHelper.TABLE_Location, iLocation.getValues(), DBHelper.COL_LOCATION_ID + "=?", new String[] { "" + iLocation.getID() });
		close();
	}

	@Override
	protected Location itemFromCurrentPos(Cursor iCursor) {
		// Get location data
		int locationID = iCursor.getInt(iCursor.getColumnIndex(DBHelper.COL_LOCATION_ID));
		String building = iCursor.getString(iCursor.getColumnIndex(DBHelper.COL_LOCATION_Building));
		String room = iCursor.getString(iCursor.getColumnIndex(DBHelper.COL_LOCATION_Room));
		String geoLocation = iCursor.getString(iCursor.getColumnIndex(DBHelper.COL_LOCATION_GeoLocation));
		
		return new Location(locationID, building, room, geoLocation);
	}

	@Override
	protected String getTableName() {
		return DBHelper.TABLE_Location;
	}

	@Override
	protected String getIDColumnName() {
		return DBHelper.COL_LOCATION_ID;
	}
}
