/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */
package com.selagroup.schedu.managers;

import android.database.Cursor;

import com.selagroup.schedu.database.DatabaseHelper;
import com.selagroup.schedu.model.Location;

/**
 * The Class LocationManager.
 */
public class LocationManager extends Manager<Location> {

	public LocationManager(DatabaseHelper iHelper) {
		super(iHelper);
	}

	@Override
	public int insert(Location iItem) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void delete(Location iItem) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Location iItem) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Location itemFromCurrentPos(Cursor iCursor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getTableName() {
		return DatabaseHelper.TABLE_Location;
	}

	@Override
	protected String getIDColumnName() {
		return DatabaseHelper.COL_LOCATION_ID;
	}
}
