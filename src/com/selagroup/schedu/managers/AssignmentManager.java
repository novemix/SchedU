package com.selagroup.schedu.managers;

import java.util.Calendar;

import android.database.Cursor;

import com.selagroup.schedu.database.DatabaseHelper;
import com.selagroup.schedu.model.Assignment;
import com.selagroup.schedu.model.Course;

public class AssignmentManager extends Manager<Assignment> {
	private CourseManager mCourseManager;

	public AssignmentManager(DatabaseHelper iHelper, CourseManager iCourseManager) {
		super(iHelper);
		mCourseManager = iCourseManager;
	}

	@Override
	public int insert(Assignment iAssignment) {
		// If the assignment already exists, just update the entry
		if (get(iAssignment.getID()) != null) {
			update(iAssignment);
			return iAssignment.getID();
		}

		open(OPEN_MODE.WRITE);
		int assignmentID = (int) mDB.insert(DatabaseHelper.TABLE_Assignment, null, iAssignment.getValues());
		iAssignment.setID(assignmentID);
		
		close();
		return assignmentID;
	}

	@Override
	public void delete(Assignment iAssignment) {
		open(OPEN_MODE.WRITE);
		mDB.delete(DatabaseHelper.TABLE_Assignment, DatabaseHelper.COL_ASSIGNMENT_ID + "=?", new String[] { "" + iAssignment.getID() });
		close();
	}

	@Override
	public void update(Assignment iAssignment) {
		open(OPEN_MODE.WRITE);
		mDB.update(DatabaseHelper.TABLE_Assignment, iAssignment.getValues(), DatabaseHelper.COL_ASSIGNMENT_ID + "=?", new String[] { "" + iAssignment.getID() });
		close();
	}

	@Override
	protected Assignment itemFromCurrentPos(Cursor iCursor) {
		// Get assignment data
		int assignmentID = iCursor.getInt(iCursor.getColumnIndex(DatabaseHelper.COL_ASSIGNMENT_ID));
		String name = iCursor.getString(iCursor.getColumnIndex(DatabaseHelper.COL_ASSIGNMENT_Name));
		int dueDateMillis = iCursor.getInt(iCursor.getColumnIndex(DatabaseHelper.COL_ASSIGNMENT_DueDate));
		int courseID = iCursor.getInt(iCursor.getColumnIndex(DatabaseHelper.COL_ASSIGNMENT_CourseID));
		
		Calendar dueDate = Calendar.getInstance();
		dueDate.setTimeInMillis(dueDateMillis);
		Course course = mCourseManager.get(courseID);
		
		return new Assignment(assignmentID, name, dueDate, course);
	}
	
	@Override
	protected String getTableName() {
		return DatabaseHelper.TABLE_Assignment;
	}

	@Override
	protected String getIDColumnName() {
		return DatabaseHelper.COL_ASSIGNMENT_ID;
	}
}
