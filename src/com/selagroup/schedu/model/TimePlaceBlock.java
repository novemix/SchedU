/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */
package com.selagroup.schedu.model;

import java.util.Calendar;

import android.content.ContentValues;

import com.selagroup.schedu.database.IContentValueItem;

/**
 * The Class TimePlaceBlock.
 */
public class TimePlaceBlock implements IContentValueItem {
	private int mID;
	private Location mLocation;
	private Calendar mStartTime;		// Note: the day/month/year for this should NOT be used
	private Calendar mEndTime;
	
	public TimePlaceBlock(int iID, Location iLocation, Calendar iStartTime, Calendar iEndTime) {
		mID = iID;
		mLocation = iLocation;
		mStartTime = iStartTime;
		mEndTime = iEndTime;
	}
	
	public Location getLocation() {
		return mLocation;
	}
	
	public Calendar getStartTime() {
		return mStartTime;
	}
	
	public Calendar getEndTime() {
		return mEndTime;
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
