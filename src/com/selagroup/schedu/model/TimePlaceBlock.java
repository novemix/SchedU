/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */
package com.selagroup.schedu.model;

import java.util.Calendar;

import android.content.ContentValues;

import com.selagroup.schedu.database.ContentValueItem;

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
	private static final int dayMasks[] = new int[] { 0x0001, 0x0002, 0x0004, 0x0008, 0x0010, 0x0020, 0x0040 };
	
	public TimePlaceBlock(int iID, Location iLocation, Calendar iStartTime, Calendar iEndTime, int iDayFlag) {
		super(iID);
		mLocation = iLocation;
		mStartTime = iStartTime;
		mEndTime = iEndTime;
		mDayFlag = iDayFlag;
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
	
	/**
	 * Gets an array of booleans for each day of the week (Sun - Sat)
	 * @return
	 */
	public boolean[] getDayFlagArray() {
		boolean[] flagArray = new boolean[7];
		
		for (int i = 0; i < DAYS_IN_WEEK; ++i) {
			flagArray[i] = (mDayFlag & dayMasks[i]) > 0;
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
			mDayFlag = (mDayFlag | (dayMasks[i] & (flagArray[i] ? 0xFFFF : 0x0000)));
		}
	}
}
