/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */

package com.selagroup.schedu.model;

import java.util.Calendar;

import com.selagroup.schedu.database.DBHelper;


import android.content.ContentValues;

/**
 * The Class Term.
 */
public class Term extends ContentValueItem {
	private Calendar mStartDate;
	private Calendar mEndDate;
	
	public Term(int iID, Calendar iStartDate, Calendar iEndDate) {
		super(iID);
		mStartDate = iStartDate;
		mEndDate = iEndDate;
	}

	public ContentValues getValues() {
		ContentValues values = new ContentValues();
		values.put(DBHelper.COL_TERM_StartDate, mStartDate.getTimeInMillis());
		values.put(DBHelper.COL_TERM_EndDate, mEndDate.getTimeInMillis());
		return values;
	}
	
	public Calendar getStartDate() {
		return mStartDate;
	}
	
	public Calendar getEndDate() {
		return mEndDate;
	}
}
