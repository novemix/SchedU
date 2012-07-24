/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */

package com.selagroup.schedu.model;


import java.util.Calendar;

import com.selagroup.schedu.database.DBHelper;

import android.content.ContentValues;

/**
 * The Class Assignment.
 */
public class Assignment extends ContentValueItem {
	private String mName;
	private Calendar mDueDate;
	private Course mCourse;
	
	public Assignment(int iID, String iName, Calendar iDueDate, Course iCourse) {
		super(iID);
		mName = iName;
		mDueDate = iDueDate;
		mCourse = iCourse;
	}

	public ContentValues getValues() {
		ContentValues values = new ContentValues();
		values.put(DBHelper.COL_ASSIGNMENT_Name, mName);
		values.put(DBHelper.COL_ASSIGNMENT_DueDate, mDueDate.getTimeInMillis());
		values.put(DBHelper.COL_ASSIGNMENT_CourseID, mCourse.getID());
		return values;
	}
}
