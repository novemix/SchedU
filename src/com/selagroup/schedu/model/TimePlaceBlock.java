/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */
package com.selagroup.schedu.model;

import java.util.Calendar;

import com.selagroup.schedu.database.DBHelper;

import android.content.ContentValues;


/**
 * The Class TimePlaceBlock.
 */
public class TimePlaceBlock extends ContentValueItem {
	private static final int DAYS_IN_WEEK = 7;
	
	private Location mLocation;
	private Calendar mStartTime;
	private Calendar mEndTime;
	private int mDayFlag;
	
	// Masks for the days of the week: Sun, Mon, Tue, Wed, Thur, Fri, Sat, Sun
	public static final int DAY_MASKS[] = new int[] { 0x0001, 0x0002, 0x0004, 0x0008, 0x0010, 0x0020, 0x0040 };
	
	public TimePlaceBlock(int iID, Location iLocation, Calendar iStartTime, Calendar iEndTime, int iDayFlag) {
		super(iID);
		mLocation = iLocation;
		mStartTime = iStartTime;
		mEndTime = iEndTime;
		mDayFlag = iDayFlag;
	}
	
	public TimePlaceBlock(int iID, Location iLocation, Calendar iStartTime, Calendar iEndTime, boolean[] iDayFlag) {
		super(iID);
		mLocation = iLocation;
		mStartTime = iStartTime;
		mEndTime = iEndTime;
		setDayFlagArray(iDayFlag);
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
		ContentValues values = new ContentValues();
		values.put(DBHelper.COL_TIME_PLACE_BLOCK_StartTime, mStartTime.getTimeInMillis());
		values.put(DBHelper.COL_TIME_PLACE_BLOCK_EndTime, mEndTime.getTimeInMillis());
		values.put(DBHelper.COL_TIME_PLACE_BLOCK_LocationID, mLocation.getID());
		values.put(DBHelper.COL_TIME_PLACE_BLOCK_DayFlag, mDayFlag);
		return values;
	}
	
	/**
	 * Gets an array of booleans for each day of the week (Sun - Sat)
	 * @return
	 */
	public boolean[] getDayFlagArray() {
		boolean[] flagArray = new boolean[7];
		
		for (int i = 0; i < DAYS_IN_WEEK; ++i) {
			flagArray[i] = (mDayFlag & DAY_MASKS[i]) > 0;
		}
		
		return flagArray;
	}
	
	/**
	 * Sets the flag array for days of the week (Sun - Sat)
	 * @param flagArray
	 */
	public void setDayFlagArray(boolean[] flagArray) {
		mDayFlag = 0;
		for (int i = 0; i < DAYS_IN_WEEK; ++i) {
			mDayFlag = (mDayFlag | (DAY_MASKS[i] & (flagArray[i] ? 0xFFFF : 0x0000)));
		}
	}
}
