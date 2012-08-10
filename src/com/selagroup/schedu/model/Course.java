/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */

package com.selagroup.schedu.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.ContentValues;

import com.selagroup.schedu.database.DBHelper;

/**
 * The Class SchoolClass.
 */
public class Course extends ContentValueItem {
	private static final long serialVersionUID = 756404045035754144L;
	
	private static final int DAYS_IN_WEEK = 7;

	private Term mTerm;
	private String mCode;
	private String mName;
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
		mCode = iCourseCode;
		mName = iCourseName;
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
		values.put(DBHelper.COL_COURSE_CourseName, mName);
		values.put(DBHelper.COL_COURSE_CourseCode, mCode);
		if (mInstructor != null) {
			values.put(DBHelper.COL_COURSE_InstructorID, mInstructor.getID());
		}
		values.put(DBHelper.COL_COURSE_TermID, mTerm.getID());
		return values;
	}

	public Term getTerm() {
		return mTerm;
	}

	public String getCode() {
		return mCode;
	}
	
	public void setCode(String iCode) {
		mCode = iCode;
	}

	public String getName() {
		return mName;
	}
	
	public void setName(String iName) {
		mName = iName;
	}

	public Instructor getInstructor() {
		return mInstructor;
	}
	
	public void setInstructor(Instructor iInstructor) {
		mInstructor = iInstructor;
	}

	public List<TimePlaceBlock> getBlocksOnDay(int iDayIndex) {
		ArrayList<TimePlaceBlock> blocks = new ArrayList<TimePlaceBlock>(2);
		for (TimePlaceBlock block : mScheduleBlocks) {
			if (block.getDayFlagArray()[iDayIndex]) {
				blocks.add(block);
			}
		}
		return blocks;
	}

	@Override
	public String toString() {
		if (mName.equals("")) {
			return mCode;
		}
		return mName + " (" + mCode + ")";
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof Course) {
			return mCode == ((Course) other).mCode && mTerm.getID() == ((Course) other).mTerm.getID();
		}
		return false;
	}
}
