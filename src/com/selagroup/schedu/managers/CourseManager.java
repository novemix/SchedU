/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */
package com.selagroup.schedu.managers;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;

import com.selagroup.schedu.database.DBHelper;
import com.selagroup.schedu.model.Course;
import com.selagroup.schedu.model.Instructor;
import com.selagroup.schedu.model.Term;
import com.selagroup.schedu.model.TimePlaceBlock;

/**
 * The Class CourseManager.
 */
public class CourseManager extends Manager<Course> {
	private TermManager mTermManager;
	private InstructorManager mInstructorManager;
	private TimePlaceBlockManager mTimePlaceBlockManager;

	/**
	 * Instantiates a new course manager.
	 * 
	 * @param iHelper the helper
	 * @param iTermManager the term manager
	 * @param iInstructorManager the instructor manager
	 * @param iTimePlaceBlockManager the time place block manager
	 */
	public CourseManager(DBHelper iHelper, TermManager iTermManager, InstructorManager iInstructorManager, TimePlaceBlockManager iTimePlaceBlockManager) {
		super(iHelper);
		mTermManager = iTermManager;
		mInstructorManager = iInstructorManager;
		mTimePlaceBlockManager = iTimePlaceBlockManager;
	}

	public ArrayList<Course> getAllForInstructor(int iInstructorID) {
		ArrayList<Course> courses = new ArrayList<Course>();

		open(OPEN_MODE.READ);
		Cursor cursor = mDB.rawQuery("SELECT " + DBHelper.COL_COURSE_ALL_COL + " FROM " +
				DBHelper.TABLE_Course + " INNER JOIN " + DBHelper.TABLE_Instructor + " ON " +
				DBHelper.TABLE_Instructor + "." + DBHelper.COL_INSTRUCTOR_ID + " = " +
				DBHelper.TABLE_Course + "." + DBHelper.COL_COURSE_InstructorID +
				" WHERE " + DBHelper.TABLE_Instructor + "." + DBHelper.COL_INSTRUCTOR_ID + " = ?",
				new String[] { "" + iInstructorID });
		if (cursor.moveToFirst()) {
			do {
				courses.add(itemFromCurrentPos(cursor));
			} while (cursor.moveToNext());
		}
		cursor.close();
		close();

		return courses;
	}

	public ArrayList<Course> getAllForTerm(int iTermID) {
		ArrayList<Course> courses = new ArrayList<Course>();

		// Open the database, query for all courses matching the termID, and add them to the list
		open(OPEN_MODE.READ);
		Cursor cursor = mDB.query(DBHelper.TABLE_Course, null, DBHelper.COL_COURSE_TermID + "=?", new String[] { "" + iTermID }, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				courses.add(itemFromCurrentPos(cursor));
			} while (cursor.moveToNext());
		}
		cursor.close();
		close();

		return courses;
	}

	@Override
	public int insert(Course iCourse) {
		if (iCourse == null) {
			return -1;
		}

		// If the course already exists, just update the entry
		if (get(iCourse.getID()) != null) {
			update(iCourse);
			return iCourse.getID();
		}

		// Insert or update the instructor for this course
		Instructor instructor = iCourse.getInstructor();
		if (instructor != null) {
			instructor.setID(mInstructorManager.insert(instructor));
		}

		// Insert course to Course table
		open(OPEN_MODE.WRITE);
		int courseID = (int) mDB.insert(DBHelper.TABLE_Course, null, iCourse.getValues());
		iCourse.setID(courseID);
		close();

		// Insert or update schedule time blocks
		ContentValues courseToScheduleBlock = null;
		for (TimePlaceBlock block : iCourse.getScheduleBlocks()) {
			block.setID(mTimePlaceBlockManager.insert(block));

			courseToScheduleBlock = new ContentValues();
			courseToScheduleBlock.put(DBHelper.COL_COURSE_TIME_PLACE_BLOCK_CourseID, courseID);
			courseToScheduleBlock.put(DBHelper.COL_COURSE_TIME_PLACE_BLOCK_TimePlaceBlockID, block.getID());

			open(OPEN_MODE.WRITE);
			mDB.insert(DBHelper.TABLE_CourseTimePlaceBlock, null, courseToScheduleBlock);
			close();
		}

		return (int) courseID;
	}

	@Override
	public void delete(Course iCourse) {
		// Delete course from course table
		open(OPEN_MODE.WRITE);
		mDB.delete(DBHelper.TABLE_Course, DBHelper.COL_COURSE_ID + "=?", new String[] { "" + iCourse.getID() });
		close();

		// Delete Schedule TimePlaceBlocks
		for (TimePlaceBlock block : iCourse.getScheduleBlocks()) {
			mTimePlaceBlockManager.delete(block);

			open(OPEN_MODE.WRITE);
			mDB.delete(DBHelper.TABLE_CourseTimePlaceBlock, DBHelper.COL_COURSE_TIME_PLACE_BLOCK_CourseID + "=? AND " +
					DBHelper.COL_COURSE_TIME_PLACE_BLOCK_TimePlaceBlockID + "=?",
					new String[] { "" + iCourse.getID(), "" + block.getID() });
			close();
		}
	}

	@Override
	public void update(Course iCourse) {
		int courseID = iCourse.getID();

		// Update instructor
		mInstructorManager.update(iCourse.getInstructor());

		// Update course table
		open(OPEN_MODE.WRITE);
		mDB.update(DBHelper.TABLE_Course, iCourse.getValues(), DBHelper.COL_COURSE_ID + "=?", new String[] { "" + iCourse.getID() });
		close();

		// Insert or update schedule time blocks
		ContentValues courseScheduleBlock = null;
		for (TimePlaceBlock block : iCourse.getScheduleBlocks()) {
			block.setID(mTimePlaceBlockManager.insert(block));

			courseScheduleBlock = new ContentValues();
			courseScheduleBlock.put(DBHelper.COL_COURSE_TIME_PLACE_BLOCK_CourseID, courseID);
			courseScheduleBlock.put(DBHelper.COL_COURSE_TIME_PLACE_BLOCK_TimePlaceBlockID, block.getID());

			open(OPEN_MODE.WRITE);
			try {
				mDB.insertOrThrow(DBHelper.TABLE_CourseTimePlaceBlock, null, courseScheduleBlock);
			} catch (SQLiteConstraintException e) {
				// record already exists in table
			}
			close();
		}

		// Delete removed blocks
		for (TimePlaceBlock block : iCourse.getRemovedBlocks()) {
			mTimePlaceBlockManager.delete(block);
			open(OPEN_MODE.WRITE);

			courseScheduleBlock = new ContentValues();
			courseScheduleBlock.put(DBHelper.COL_COURSE_TIME_PLACE_BLOCK_CourseID, courseID);
			courseScheduleBlock.put(DBHelper.COL_COURSE_TIME_PLACE_BLOCK_TimePlaceBlockID, block.getID());

			mDB.delete(DBHelper.TABLE_CourseTimePlaceBlock, DBHelper.COL_COURSE_TIME_PLACE_BLOCK_CourseID +
					"=? AND " + DBHelper.COL_COURSE_TIME_PLACE_BLOCK_TimePlaceBlockID + "=?",
					new String[] { "" + courseID, "" + block.getID() });
			close();
		}
		iCourse.clearRemovedBlocks();
	}

	@Override
	protected Course itemFromCurrentPos(Cursor iCursor) {
		// Get course data
		int courseID = iCursor.getInt(iCursor.getColumnIndex(DBHelper.COL_COURSE_ID));
		String courseCode = iCursor.getString(iCursor.getColumnIndex(DBHelper.COL_COURSE_CourseCode));
		String courseName = iCursor.getString(iCursor.getColumnIndex(DBHelper.COL_COURSE_CourseName));
		int instructorID = iCursor.getInt(iCursor.getColumnIndex(DBHelper.COL_COURSE_InstructorID));
		int termID = iCursor.getInt(iCursor.getColumnIndex(DBHelper.COL_COURSE_TermID));

		// Lookup instructor from ID
		Instructor instructor = mInstructorManager.get(instructorID);

		// Lookup term
		Term term = mTermManager.get(termID);

		Course newCourse = new Course(courseID, term, courseCode, courseName, instructor);

		// Get block IDs for the course schedule
		open(OPEN_MODE.READ);
		Cursor scheduleBlockCursor = mDB.query(DBHelper.TABLE_CourseTimePlaceBlock, new String[] { DBHelper.COL_COURSE_TIME_PLACE_BLOCK_TimePlaceBlockID },
				DBHelper.COL_COURSE_TIME_PLACE_BLOCK_CourseID + "=?", new String[] { "" + courseID }, null, null, null);
		// Add schedule blocks
		if (scheduleBlockCursor.moveToFirst()) {
			do {
				int blockID = scheduleBlockCursor.getInt(0);	// Only column, the block IDs
				TimePlaceBlock block = mTimePlaceBlockManager.get(blockID);
				newCourse.addScheduleBlock(block);
			} while (scheduleBlockCursor.moveToNext());
		}
		scheduleBlockCursor.close();
		close();

		return newCourse;
	}

	@Override
	protected String getTableName() {
		return DBHelper.TABLE_Course;
	}

	@Override
	protected String getIDColumnName() {
		return DBHelper.COL_COURSE_ID;
	}
}
