package com.selagroup.schedu.managers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.database.Cursor;

import com.selagroup.schedu.database.DBHelper;
import com.selagroup.schedu.model.Assignment;
import com.selagroup.schedu.model.Course;

public class AssignmentManager extends Manager<Assignment> {
	private CourseManager mCourseManager;

	public AssignmentManager(DBHelper iHelper, CourseManager iCourseManager) {
		super(iHelper);
		mCourseManager = iCourseManager;
	}
	
	public List<Assignment> getAllForCourse(int iCourseID) {
		List<Assignment> assignments = new ArrayList<Assignment>();
		
		// Open the database, query for all assignments matching the courseID, and add them to the list
		open(OPEN_MODE.READ);
		Cursor cursor = mDB.query(DBHelper.TABLE_Assignment, null, DBHelper.COL_ASSIGNMENT_CourseID + "=?", new String[]{ "" + iCourseID }, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				assignments.add(itemFromCurrentPos(cursor));
			} while (cursor.moveToNext());
		}
		close();
		
		return assignments;
	}

	@Override
	public int insert(Assignment iAssignment) {
		// If the assignment already exists, just update the entry
		if (get(iAssignment.getID()) != null) {
			update(iAssignment);
			return iAssignment.getID();
		}

		open(OPEN_MODE.WRITE);
		int assignmentID = (int) mDB.insert(DBHelper.TABLE_Assignment, null, iAssignment.getValues());
		iAssignment.setID(assignmentID);
		
		close();
		return assignmentID;
	}

	@Override
	public void delete(Assignment iAssignment) {
		open(OPEN_MODE.WRITE);
		mDB.delete(DBHelper.TABLE_Assignment, DBHelper.COL_ASSIGNMENT_ID + "=?", new String[] { "" + iAssignment.getID() });
		close();
	}

	@Override
	public void update(Assignment iAssignment) {
		open(OPEN_MODE.WRITE);
		mDB.update(DBHelper.TABLE_Assignment, iAssignment.getValues(), DBHelper.COL_ASSIGNMENT_ID + "=?", new String[] { "" + iAssignment.getID() });
		close();
	}

	@Override
	protected Assignment itemFromCurrentPos(Cursor iCursor) {
		// Get assignment data
		int assignmentID = iCursor.getInt(iCursor.getColumnIndex(DBHelper.COL_ASSIGNMENT_ID));
		String name = iCursor.getString(iCursor.getColumnIndex(DBHelper.COL_ASSIGNMENT_Name));
		int dueDateMillis = iCursor.getInt(iCursor.getColumnIndex(DBHelper.COL_ASSIGNMENT_DueDate));
		int courseID = iCursor.getInt(iCursor.getColumnIndex(DBHelper.COL_ASSIGNMENT_CourseID));
		
		Calendar dueDate = Calendar.getInstance();
		dueDate.setTimeInMillis(dueDateMillis);
		Course course = mCourseManager.get(courseID);
		
		return new Assignment(assignmentID, name, dueDate, course);
	}
	
	@Override
	protected String getTableName() {
		return DBHelper.TABLE_Assignment;
	}

	@Override
	protected String getIDColumnName() {
		return DBHelper.COL_ASSIGNMENT_ID;
	}
}
