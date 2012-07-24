/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */

package com.selagroup.schedu.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.selagroup.schedu.database.DatabaseHelper;

import android.content.ContentValues;

/**
 * The Class SchoolClass.
 */
public class Course extends ContentValueItem {
	private Term mTerm;
	private String mCourseCode;
	private String mCourseName;
	private Instructor mInstructor;
	private List<TimePlaceBlock> mScheduleBlocks = new ArrayList<TimePlaceBlock>(2);
	private List<TimePlaceBlock> mOfficeBlocks = new ArrayList<TimePlaceBlock>(2);

	/**
	 * Instantiates a new course.
	 * 
	 * @param iCourseID the course id
	 * @param iCourseCode the course code
	 * @param iCourseName the course name
	 * @param iInstructor the instructor
	 */
	public Course(int iID, Term iTerm, String iCourseCode, String iCourseName, Instructor iInstructor) {
		super(iID);
		mTerm = iTerm;
		mCourseCode = iCourseCode;
		mCourseName = iCourseName;
		mInstructor = iInstructor;
	}

	public void addScheduleBlock(TimePlaceBlock iBlock) {
		mScheduleBlocks.add(iBlock);
	}

	public void removeScheduleBlock(TimePlaceBlock iBlock) {
		mScheduleBlocks.remove(iBlock);
	}

	public void addOfficeBlock(TimePlaceBlock iBlock) {
		mOfficeBlocks.add(iBlock);
	}

	public void removeOfficeBlock(TimePlaceBlock iBlock) {
		mOfficeBlocks.remove(iBlock);
	}

	public List<TimePlaceBlock> getScheduleBlocks() {
		return Collections.unmodifiableList(mScheduleBlocks);
	}

	public List<TimePlaceBlock> getOfficeBlocks() {
		return Collections.unmodifiableList(mOfficeBlocks);
	}

	public ContentValues getValues() {
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.COL_COURSE_CourseName, mCourseName);
		values.put(DatabaseHelper.COL_COURSE_CourseCode, mCourseCode);
		values.put(DatabaseHelper.COL_COURSE_InstructorID, mInstructor.getID());
		values.put(DatabaseHelper.COL_COURSE_TermID, mTerm.getID());
		return values;
	}

	public Term getTerm() {
		return mTerm;
	}

	public String getCourseCode() {
		return mCourseCode;
	}

	public String getCourseName() {
		return mCourseName;
	}

	public Instructor getInstructor() {
		return mInstructor;
	}
}
