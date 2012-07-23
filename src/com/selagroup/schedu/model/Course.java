/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */

package com.selagroup.schedu.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.ContentValues;

import com.selagroup.schedu.database.IContentValueItem;

/**
 * The Class SchoolClass.
 */
public class Course implements IContentValueItem {
	private int mID;
	private String mCourseCode;
	private String mCourseName;
	private Instructor mInstructor;
	private List<TimePlaceBlock> mCourseScheduleBlocks = new ArrayList<TimePlaceBlock>(2);

	/**
	 * Instantiates a new course.
	 *
	 * @param iCourseID the course id
	 * @param iCourseCode the course code
	 * @param iCourseName the course name
	 * @param iInstructor the instructor
	 */
	public Course(int iID, String iCourseCode, String iCourseName, Instructor iInstructor) {
		mID = iID;
		mCourseCode = iCourseCode;
		mCourseName = iCourseName;
		mInstructor = iInstructor;
	}
	
	public void addBlock(TimePlaceBlock iBlock) {
		mCourseScheduleBlocks.add(iBlock);
	}
	
	public void removeBlock(TimePlaceBlock iBlock) {
		mCourseScheduleBlocks.remove(iBlock);
	}
	
	public List<TimePlaceBlock> getBlocks() {
		return Collections.unmodifiableList(mCourseScheduleBlocks);
	}

	public ContentValues getValues() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getID() {
		return mID;
	}

	public Term getTerm() {
		// TODO Auto-generated method stub
		return null;
	}

	public Instructor getInstructor() {
		// TODO Auto-generated method stub
		return null;
	}
}

