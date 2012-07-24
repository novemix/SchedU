/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */
package com.selagroup.schedu.model;

import android.content.ContentValues;

import com.selagroup.schedu.database.ContentValueItem;

/**
 * The Class Exam.
 */
public class Exam extends ContentValueItem {
	private Course mCourse;
	private TimePlaceBlock mBlock;
	
	/**
	 * Instantiates a new exam.
	 */
	public Exam(int iID, Course iCourse, TimePlaceBlock iBlock) {
		super(iID);
		mCourse = iCourse;
		mBlock = iBlock;
	}

	public ContentValues getValues() {
		// TODO Auto-generated method stub
		return null;
	}
}
