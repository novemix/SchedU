/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */

package com.selagroup.schedu.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.content.ContentValues;

import com.selagroup.schedu.database.DBHelper;

/**
 * The Class Term.
 */
public class Term extends ContentValueItem {
    private static final long serialVersionUID = 7962826214043942640L;
    
	private static final DateFormat mFormat = new SimpleDateFormat("MMM dd, yyyy");
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
	
	public String getStartDateString() {
		return (mStartDate != null ? mFormat.format(mStartDate.getTime()) : "[start date]");
	}

	public void setStartDate(Calendar iStartDate) {
	    mStartDate = iStartDate;
    }
	
	public Calendar getEndDate() {
		return mEndDate;
	}
	
	public String getEndDateString() {
		return (mEndDate != null ? mFormat.format(mEndDate.getTime()) : "[end date]");
	}
	
	public void setEndDate(Calendar iEndDate) {
		mEndDate = iEndDate;
    }
	
	@Override
	public String toString() {
		return getStartDateString() + " - " + getEndDateString();
	}
}
