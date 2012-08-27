/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */

package com.selagroup.schedu.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import android.content.ContentValues;
import android.util.Pair;

import com.selagroup.schedu.Utility;
import com.selagroup.schedu.database.DBHelper;

/**
 * The Class SchoolClass.
 */
public class Course extends ContentValueItem {
	private static final long serialVersionUID = 756404045035754144L;

	private Term mTerm;
	private String mCode;
	private String mName;
	private Instructor mInstructor;
	private List<TimePlaceBlock> mScheduleBlocks = new ArrayList<TimePlaceBlock>(2);
	private List<TimePlaceBlock> mRemovedBlocks = new LinkedList<TimePlaceBlock>();

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
		if (mScheduleBlocks.remove(iBlock)) {
			mRemovedBlocks.add(iBlock);
		}
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

	/**
	 * Gets a sorted list of blocks for a given day
	 * @param iDayIndex Day index to get blocks for (0 = Sunday, 1 = Monday, etc)
	 * @return List of blocks for the day, sorted by start time
	 */
	public List<TimePlaceBlock> getBlocksOnDay(int iDayIndex) {
		ArrayList<TimePlaceBlock> blocks = new ArrayList<TimePlaceBlock>();
		for (TimePlaceBlock block : mScheduleBlocks) {
			if (block.getDayFlagArray()[iDayIndex]) {
				blocks.add(block);
			}
		}
		Collections.sort(blocks);
		return blocks;
	}

	/**
	 * Gets the next block (and the day) for this course given a start time, will return classes currently ongoing
	 * @param iStart Start date and time
	 * @return Pair: block and the associated day
	 */
	public Pair<TimePlaceBlock, Calendar> getNextBlock(Calendar iStart) {
		TreeMap<Calendar, TimePlaceBlock> blockEndTimes = new TreeMap<Calendar, TimePlaceBlock>();
		for (TimePlaceBlock block : mScheduleBlocks) {
			for (int i = 0; i < Utility.DAYS_PER_WEEK; ++i) {
				if (block.getDayFlagArray()[i]) {
					Calendar blockEnd = (Calendar) block.getEndTime().clone();
					blockEnd.set(Calendar.YEAR, iStart.get(Calendar.YEAR));
					blockEnd.set(Calendar.WEEK_OF_YEAR, iStart.get(Calendar.WEEK_OF_YEAR));
					blockEnd.set(Calendar.DAY_OF_WEEK, i + 1);
					if (blockEnd.before(iStart)) {
						blockEnd.add(Calendar.WEEK_OF_YEAR, 1);
					}
					blockEndTimes.put(blockEnd, block);
				}
			}
		}
		if (blockEndTimes.isEmpty()) {
			return null;
		} else {
			Calendar firstKey = blockEndTimes.firstKey();
			return new Pair<TimePlaceBlock, Calendar>(blockEndTimes.get(firstKey), firstKey);
		}
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

	public List<TimePlaceBlock> getRemovedBlocks() {
		return mRemovedBlocks;
	}

	public void clearRemovedBlocks() {
		mRemovedBlocks.clear();
	}
}
