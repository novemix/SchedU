package com.selagroup.schedu.managers;

import java.util.ArrayList;

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
		open(OPEN_MODE.WRITE);

		// Insert course to Course table
		long courseID = mDB.insert(DatabaseHelper.TABLE_Course, null, iCourse.getValues());

		// Insert course/term listing to CourseToTerm table
		ContentValues courseToTermValues = new ContentValues();
		courseToTermValues.put(DatabaseHelper.COL_COURSE_TO_TERM_CourseID, iCourse.getID());
		courseToTermValues.put(DatabaseHelper.COL_COURSE_TO_TERM_TermID, iCourse.getTerm().getID());
		mDB.insert(DatabaseHelper.TABLE_CourseToTerm, null, courseToTermValues);
		
		// Insert new time blocks for this course
		for (TimePlaceBlock block : iCourse.getBlocks()) {
			block.setID(mTimePlaceBlockManager.insert(block));
		}
		
		// Insert instructor for this course into Instructor table
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
		
		close();
	}

	@Override
	public void update(Course iCourse) {
		open(OPEN_MODE.WRITE);
		
		// Update course table
		mDB.update(DatabaseHelper.TABLE_Course, iCourse.getValues(), DatabaseHelper.COL_COURSE_ID + "=?", new String[] { "" + iCourse.getID() });
		
		// Update time blocks
		
		// Update instructor
		
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

		return new Course(courseID, courseCode, courseName, instructor);
	}
	
	@Override
	public Course get(int iCourseID) {
		Course course = super.get(iCourseID);
		
		// Add time blocks
		
		// Add instructor
		
		return course;
	}
	
	@Override
	public ArrayList<Course> getAll() {
		ArrayList<Course> courses = super.getAll();
		
		// Add time blocks for each course
		
		// Add instructor for each course
		
		return courses;
	}
}
