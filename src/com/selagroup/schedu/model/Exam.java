/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */
package com.selagroup.schedu.model;

import java.text.SimpleDateFormat;

import android.content.ContentValues;

import com.selagroup.schedu.database.DBHelper;

/**
 * The Class Exam.
 */
public class Exam extends ContentValueItem {
	private static final long serialVersionUID = 1706713919453127768L;
	private static final SimpleDateFormat EXAM_TIME_FORMAT = new SimpleDateFormat("h:mma");

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
		values.put(DBHelper.COL_EXAM_Description, mDescription);
		values.put(DBHelper.COL_EXAM_CourseID, mCourse.getID());
		values.put(DBHelper.COL_EXAM_TimePlaceBlockID, mBlock.getID());
		return values;
	}

	public String getDescription() {
		return mDescription;
	}

	public void setDescription(String iDescription) {
		mDescription = iDescription;
	}

	public Course getCourse() {
		return mCourse;
	}

	public TimePlaceBlock getBlock() {
		return mBlock;
	}

	@Override
	public String toString() {
		return "Exam for " + mCourse.getCode() + "\n" + mDescription + "\n" +
				(EXAM_TIME_FORMAT.format(mBlock.getStartTime().getTime()) + " - " +
				EXAM_TIME_FORMAT.format(mBlock.getEndTime().getTime())).toLowerCase();
	}
}
