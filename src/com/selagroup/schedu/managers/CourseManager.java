package com.selagroup.schedu.managers;

import android.content.ContentValues;
import android.database.Cursor;

import com.selagroup.schedu.database.DatabaseHelper;
import com.selagroup.schedu.model.Course;
import com.selagroup.schedu.model.Instructor;
import com.selagroup.schedu.model.TimePlaceBlock;

public class CourseManager extends Manager<Course> {
	private InstructorManager mInstructorManager;
	private TimePlaceBlockManager mTimePlaceBlockManager;

	public CourseManager(DatabaseHelper iHelper, InstructorManager iInstructorManager, TimePlaceBlockManager iTimePlaceBlockManager) {
		super(iHelper);
		mInstructorManager = iInstructorManager;
		mTimePlaceBlockManager = iTimePlaceBlockManager;
	}

	@Override
	public int insert(Course iCourse) {
		// If the course already exists, just update the entry
		if (get(iCourse.getID()) != null) {
			update(iCourse);
			return iCourse.getID();
		}

		open(OPEN_MODE.WRITE);

		// Insert course to Course table
		int courseID = (int) mDB.insert(DatabaseHelper.TABLE_Course, null, iCourse.getValues());
		iCourse.setID(courseID);

		// Insert course/term listing to CourseToTerm table
		ContentValues courseToTermValues = new ContentValues();
		courseToTermValues.put(DatabaseHelper.COL_COURSE_TO_TERM_CourseID, iCourse.getID());
		courseToTermValues.put(DatabaseHelper.COL_COURSE_TO_TERM_TermID, iCourse.getTerm().getID());
		mDB.insert(DatabaseHelper.TABLE_CourseToTerm, null, courseToTermValues);

		// Insert or update time blocks
		ContentValues courseToBlock = null;
		for (TimePlaceBlock block : iCourse.getBlocks()) {
			block.setID(mTimePlaceBlockManager.insert(block));

			courseToBlock = new ContentValues();
			courseToBlock.put(DatabaseHelper.COL_COURSE_TIME_PLACE_BLOCK_CourseID, courseID);
			courseToBlock.put(DatabaseHelper.COL_COURSE_TIME_PLACE_BLOCK_TimePlaceBlockID, block.getID());
			mDB.insert(DatabaseHelper.TABLE_CourseTimeBlock, null, courseToBlock);
		}

		// Insert or update the instructor for this course
		Instructor instructor = iCourse.getInstructor();
		instructor.setID(mInstructorManager.insert(instructor));

		close();
		return (int) courseID;
	}

	@Override
	public void delete(Course iCourse) {
		open(OPEN_MODE.WRITE);

		// Delete course from course table
		mDB.delete(DatabaseHelper.TABLE_Course, DatabaseHelper.COL_COURSE_ID + "=?", new String[] { "" + iCourse.getID() });

		// Delete course/term listing from CourseToTerm table
		mDB.delete(DatabaseHelper.TABLE_CourseToTerm, DatabaseHelper.COL_COURSE_TO_TERM_CourseID + "=? AND " +
				DatabaseHelper.COL_COURSE_TO_TERM_TermID + "=?",
				new String[] { "" + iCourse.getID(), "" + iCourse.getTerm().getID() });

		// Delete TimePlaceBlocks
		for (TimePlaceBlock block : iCourse.getBlocks()) {
			mTimePlaceBlockManager.delete(block);

			mDB.delete(DatabaseHelper.TABLE_CourseTimeBlock, DatabaseHelper.COL_COURSE_TIME_PLACE_BLOCK_CourseID + "=? AND " +
					DatabaseHelper.COL_COURSE_TIME_PLACE_BLOCK_TimePlaceBlockID + "=?",
					new String[] { "" + iCourse.getID(), "" + block.getID() });
		}
		
		// TODO: Also, delete all notes, exams, and assignments for this course.

		close();
	}

	@Override
	public void update(Course iCourse) {
		int courseID = iCourse.getID();
		open(OPEN_MODE.WRITE);

		// Update course table
		mDB.update(DatabaseHelper.TABLE_Course, iCourse.getValues(), DatabaseHelper.COL_COURSE_ID + "=?", new String[] { "" + iCourse.getID() });

		// Insert or update time blocks
		ContentValues courseToBlock = null;
		for (TimePlaceBlock block : iCourse.getBlocks()) {
			mTimePlaceBlockManager.insert(block);

			courseToBlock = new ContentValues();
			courseToBlock.put(DatabaseHelper.COL_COURSE_TIME_PLACE_BLOCK_CourseID, courseID);
			courseToBlock.put(DatabaseHelper.COL_COURSE_TIME_PLACE_BLOCK_TimePlaceBlockID, block.getID());
			mDB.insert(DatabaseHelper.TABLE_CourseTimeBlock, null, courseToBlock);
		}

		// Update instructor
		mInstructorManager.update(iCourse.getInstructor());

		close();
	}

	@Override
	protected Course itemFromCurrentPos(Cursor iCursor) {
		// Get course data
		int courseID = iCursor.getInt(iCursor.getColumnIndex(DatabaseHelper.COL_COURSE_ID));
		String courseCode = iCursor.getString(iCursor.getColumnIndex(DatabaseHelper.COL_COURSE_CourseCode));
		String courseName = iCursor.getString(iCursor.getColumnIndex(DatabaseHelper.COL_COURSE_CourseName));
		int instructorID = iCursor.getInt(iCursor.getColumnIndex(DatabaseHelper.COL_COURSE_InstructorID));

		// Lookup instructor from ID
		Instructor instructor = mInstructorManager.get(instructorID);

		Course newCourse = new Course(courseID, courseCode, courseName, instructor);

		// Get block IDs for this course
		Cursor blockCursor = mDB.query(DatabaseHelper.TABLE_CourseTimeBlock, new String[] { DatabaseHelper.COL_COURSE_TIME_PLACE_BLOCK_TimePlaceBlockID },
				DatabaseHelper.COL_COURSE_TIME_PLACE_BLOCK_CourseID + "=?", new String[] { "" + courseID }, null, null, null);

		// Lookup blocks from IDs, get them, and add them to the course
		if (blockCursor.moveToFirst()) {
			do {
				int blockID = blockCursor.getInt(0);	// Only column, the block IDs
				TimePlaceBlock block = mTimePlaceBlockManager.get(blockID);
				newCourse.addBlock(block);
			} while (blockCursor.moveToNext());
		}

		return newCourse;
	}
	
	@Override
	protected String getTableName() {
		return DatabaseHelper.TABLE_Course;
	}

	@Override
	protected String getIDColumnName() {
		return DatabaseHelper.COL_COURSE_ID;
	}
}
