package com.selagroup.schedu.managers;

import android.content.ContentValues;
import android.database.Cursor;

import com.selagroup.schedu.database.DatabaseHelper;
import com.selagroup.schedu.model.Course;
import com.selagroup.schedu.model.Instructor;
import com.selagroup.schedu.model.Term;
import com.selagroup.schedu.model.TimePlaceBlock;

public class CourseManager extends Manager<Course> {
	private TermManager mTermManager;
	private InstructorManager mInstructorManager;
	private TimePlaceBlockManager mTimePlaceBlockManager;

	public CourseManager(DatabaseHelper iHelper, TermManager iTermManager, InstructorManager iInstructorManager, TimePlaceBlockManager iTimePlaceBlockManager) {
		super(iHelper);
		mTermManager = iTermManager;
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

		// Insert or update the instructor for this course
		Instructor instructor = iCourse.getInstructor();
		instructor.setID(mInstructorManager.insert(instructor));
		
		// Insert course to Course table
		open(OPEN_MODE.WRITE);
		int courseID = (int) mDB.insert(DatabaseHelper.TABLE_Course, null, iCourse.getValues());
		iCourse.setID(courseID);
		close();

		// Insert or update schedule time blocks
		ContentValues courseToScheduleBlock = null;
		for (TimePlaceBlock block : iCourse.getScheduleBlocks()) {
			block.setID(mTimePlaceBlockManager.insert(block));

			courseToScheduleBlock = new ContentValues();
			courseToScheduleBlock.put(DatabaseHelper.COL_COURSE_TIME_PLACE_BLOCK_CourseID, courseID);
			courseToScheduleBlock.put(DatabaseHelper.COL_COURSE_TIME_PLACE_BLOCK_TimePlaceBlockID, block.getID());
			
			open(OPEN_MODE.WRITE);
			mDB.insert(DatabaseHelper.TABLE_CourseTimeBlock, null, courseToScheduleBlock);
			close();
		}
		
		// Insert or update office time blocks
		ContentValues courseToOfficeBlock = null;
		for (TimePlaceBlock block : iCourse.getOfficeBlocks()) {
			block.setID(mTimePlaceBlockManager.insert(block));
			
			courseToOfficeBlock = new ContentValues();
			courseToOfficeBlock.put(DatabaseHelper.COL_OFFICE_TIME_PLACE_BLOCK_CourseID, courseID);
			courseToOfficeBlock.put(DatabaseHelper.COL_OFFICE_TIME_PLACE_BLOCK_TimePlaceBlockID, block.getID());
			
			open(OPEN_MODE.WRITE);
			mDB.insert(DatabaseHelper.TABLE_OfficeTimePlaceBlock, null, courseToOfficeBlock);
			close();
		}
		
		return (int) courseID;
	}

	@Override
	public void delete(Course iCourse) {
		// Delete course from course table
		open(OPEN_MODE.WRITE);
		mDB.delete(DatabaseHelper.TABLE_Course, DatabaseHelper.COL_COURSE_ID + "=?", new String[] { "" + iCourse.getID() });
		close();
		
		// Delete Schedule TimePlaceBlocks
		for (TimePlaceBlock block : iCourse.getScheduleBlocks()) {
			mTimePlaceBlockManager.delete(block);

			open(OPEN_MODE.WRITE);
			mDB.delete(DatabaseHelper.TABLE_CourseTimeBlock, DatabaseHelper.COL_COURSE_TIME_PLACE_BLOCK_CourseID + "=? AND " +
					DatabaseHelper.COL_COURSE_TIME_PLACE_BLOCK_TimePlaceBlockID + "=?",
					new String[] { "" + iCourse.getID(), "" + block.getID() });
			close();
		}
		
		// Delete Office TimePlaceBlocks
		for (TimePlaceBlock block : iCourse.getOfficeBlocks()) {
			mTimePlaceBlockManager.delete(block);

			open(OPEN_MODE.WRITE);
			mDB.delete(DatabaseHelper.TABLE_OfficeTimePlaceBlock, DatabaseHelper.COL_OFFICE_TIME_PLACE_BLOCK_CourseID + "=? AND " +
					DatabaseHelper.COL_OFFICE_TIME_PLACE_BLOCK_TimePlaceBlockID + "=?",
					new String[] { "" + iCourse.getID(), "" + block.getID() });
			close();
		}
		
		// TODO: Also, delete all notes, exams, and assignments for this course?
	}

	@Override
	public void update(Course iCourse) {
		int courseID = iCourse.getID();

		// Update instructor
		mInstructorManager.update(iCourse.getInstructor());
		
		// Update course table
		open(OPEN_MODE.WRITE);
		mDB.update(DatabaseHelper.TABLE_Course, iCourse.getValues(), DatabaseHelper.COL_COURSE_ID + "=?", new String[] { "" + iCourse.getID() });
		close();
		
		// Insert or update schedule time blocks
		ContentValues courseScheduleBlock = null;
		for (TimePlaceBlock block : iCourse.getScheduleBlocks()) {
			block.setID(mTimePlaceBlockManager.insert(block));

			courseScheduleBlock = new ContentValues();
			courseScheduleBlock.put(DatabaseHelper.COL_COURSE_TIME_PLACE_BLOCK_CourseID, courseID);
			courseScheduleBlock.put(DatabaseHelper.COL_COURSE_TIME_PLACE_BLOCK_TimePlaceBlockID, block.getID());
			
			open(OPEN_MODE.WRITE);
			mDB.insert(DatabaseHelper.TABLE_CourseTimeBlock, null, courseScheduleBlock);
			close();
		}
		
		// Insert or update schedule time blocks
		ContentValues officeScheduleBlock = null;
		for (TimePlaceBlock block : iCourse.getOfficeBlocks()) {
			mTimePlaceBlockManager.insert(block);

			officeScheduleBlock = new ContentValues();
			officeScheduleBlock.put(DatabaseHelper.COL_OFFICE_TIME_PLACE_BLOCK_CourseID, courseID);
			officeScheduleBlock.put(DatabaseHelper.COL_OFFICE_TIME_PLACE_BLOCK_TimePlaceBlockID, block.getID());
			
			open(OPEN_MODE.WRITE);
			mDB.insert(DatabaseHelper.TABLE_OfficeTimePlaceBlock, null, officeScheduleBlock);
			close();
		}
	}

	@Override
	protected Course itemFromCurrentPos(Cursor iCursor) {
		// Get course data
		int courseID = iCursor.getInt(iCursor.getColumnIndex(DatabaseHelper.COL_COURSE_ID));
		String courseCode = iCursor.getString(iCursor.getColumnIndex(DatabaseHelper.COL_COURSE_CourseCode));
		String courseName = iCursor.getString(iCursor.getColumnIndex(DatabaseHelper.COL_COURSE_CourseName));
		int instructorID = iCursor.getInt(iCursor.getColumnIndex(DatabaseHelper.COL_COURSE_InstructorID));
		int termID = iCursor.getInt(iCursor.getColumnIndex(DatabaseHelper.COL_COURSE_TermID));

		// Lookup instructor from ID
		Instructor instructor = mInstructorManager.get(instructorID);
		
		// Lookup term
		Term term = mTermManager.get(termID);

		Course newCourse = new Course(courseID, term, courseCode, courseName, instructor);

		// Get block IDs for the course schedule
		open(OPEN_MODE.READ);
		Cursor scheduleBlockCursor = mDB.query(DatabaseHelper.TABLE_CourseTimeBlock, new String[] { DatabaseHelper.COL_COURSE_TIME_PLACE_BLOCK_TimePlaceBlockID },
				DatabaseHelper.COL_COURSE_TIME_PLACE_BLOCK_CourseID + "=?", new String[] { "" + courseID }, null, null, null);
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
		
		// Get block IDs for the office hours
		open(OPEN_MODE.READ);
		Cursor officeBlockCursor = mDB.query(DatabaseHelper.TABLE_OfficeTimePlaceBlock, new String[] { DatabaseHelper.COL_OFFICE_TIME_PLACE_BLOCK_TimePlaceBlockID },
				DatabaseHelper.COL_OFFICE_TIME_PLACE_BLOCK_CourseID + "=?", new String[] { "" + courseID }, null, null, null);
		// Add office blocks
		if (officeBlockCursor.moveToFirst()) {
			do {
				int blockID = officeBlockCursor.getInt(0);	// Only column, the block IDs
				TimePlaceBlock block = mTimePlaceBlockManager.get(blockID);
				newCourse.addOfficeBlock(block);
			} while (officeBlockCursor.moveToNext());
		}
		officeBlockCursor.close();
		close();

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
