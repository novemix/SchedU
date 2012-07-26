/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */

package com.selagroup.schedu.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import com.selagroup.schedu.database.DBHelper;

import android.content.ContentValues;

/**
 * The Class SchoolClass.
 */
public class Course extends ContentValueItem {
	private static final long serialVersionUID = 756404045035754144L;

	private Term mTerm;
	private String mCourseCode;
	private String mCourseName;
	private Instructor mInstructor;
	private List<TimePlaceBlock> mScheduleBlocks = new ArrayList<TimePlaceBlock>(2);

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

	public List<TimePlaceBlock> getScheduleBlocks() {
		return Collections.unmodifiableList(mScheduleBlocks);
	}

	public TimePlaceBlock getScheduleBlock(int iBlockID) {
		for (TimePlaceBlock block : mScheduleBlocks) {
			if (block.getID() == iBlockID) {
				return block;
			}
		}
		return null;
	}

	public ContentValues getValues() {
		ContentValues values = new ContentValues();
		values.put(DBHelper.COL_COURSE_CourseName, mCourseName);
		values.put(DBHelper.COL_COURSE_CourseCode, mCourseCode);
		values.put(DBHelper.COL_COURSE_InstructorID, mInstructor.getID());
		values.put(DBHelper.COL_COURSE_TermID, mTerm.getID());
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

	public boolean hasBlockOnDay(int iDayIndex) {
		for (TimePlaceBlock block : mScheduleBlocks) {
			if (block.getDayFlagArray()[iDayIndex]) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		if (mCourseName.equals("")) {
			return mCourseCode;
		}
		return mCourseName + "(" + mCourseCode + ")";
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof Course) {
			return mCourseCode == ((Course) other).mCourseCode && mTerm.getID() == ((Course) other).mTerm.getID();
		}
		return false;
	}
}
