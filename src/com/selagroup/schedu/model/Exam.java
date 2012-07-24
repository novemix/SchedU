/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */
package com.selagroup.schedu.model;

import com.selagroup.schedu.database.DatabaseHelper;

import android.content.ContentValues;


/**
 * The Class Exam.
 */
public class Exam extends ContentValueItem {
	private String mDescription;
	private Course mCourse;
	private TimePlaceBlock mBlock;
	
	/**
	 * Instantiates a new exam.
	 */
	public Exam(int iID, String iDescription, Course iCourse, TimePlaceBlock iBlock) {
		super(iID);
		mCourse = iCourse;
		mBlock = iBlock;
		mDescription = iDescription;
	}

	public ContentValues getValues() {
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.COL_EXAM_Description, mDescription);
		values.put(DatabaseHelper.COL_EXAM_CourseID, mCourse.getID());
		values.put(DatabaseHelper.COL_EXAM_TimePlaceBlockID, mBlock.getID());
		return values;
	}
	
	public String getDescription() {
		return mDescription;
	}
	
	public Course getCourse() {
		return mCourse;
	}

	public TimePlaceBlock getBlock() {
	    return mBlock;
    }
}
