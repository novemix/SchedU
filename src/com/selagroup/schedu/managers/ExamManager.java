/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */
package com.selagroup.schedu.managers;

import android.database.Cursor;

import com.selagroup.schedu.database.DatabaseHelper;
import com.selagroup.schedu.model.Course;
import com.selagroup.schedu.model.Exam;
import com.selagroup.schedu.model.TimePlaceBlock;

/**
 * The Class ExamManager.
 */
public class ExamManager extends Manager<Exam> {
	private TimePlaceBlockManager mTimePlaceBlockManager;
	private CourseManager mCourseManager;
	
	public ExamManager(DatabaseHelper iHelper, TimePlaceBlockManager iTimePlaceBlockManager, CourseManager iCourseManager) {
		super(iHelper);
		mTimePlaceBlockManager = iTimePlaceBlockManager;
		mCourseManager = iCourseManager;
	}

	@Override
	public int insert(Exam iExam) {
		// If the exam already exists, just update the entry
		if (get(iExam.getID()) != null) {
			update(iExam);
			return iExam.getID();
		}
		
		// Insert/update the TimePlaceBlock for this exam
		mTimePlaceBlockManager.insert(iExam.getBlock());

		open(OPEN_MODE.WRITE);
		int examID = (int) mDB.insert(DatabaseHelper.TABLE_Exam, null, iExam.getValues());
		iExam.setID(examID);
		
		close();
		return examID;
	}

	@Override
	public void delete(Exam iExam) {
		// Delete the TimePlaceBlock for this exam
		mTimePlaceBlockManager.insert(iExam.getBlock());
		
		open(OPEN_MODE.WRITE);
		mDB.delete(DatabaseHelper.TABLE_Exam, DatabaseHelper.COL_EXAM_ID + "=?", new String[] { "" + iExam.getID() });
		close();
	}

	@Override
	public void update(Exam iExam) {
		// Update the TimePlaceBlock for this exam
		mTimePlaceBlockManager.update(iExam.getBlock());
		
		open(OPEN_MODE.WRITE);
		mDB.update(DatabaseHelper.TABLE_Exam, iExam.getValues(), DatabaseHelper.COL_INSTRUCTOR_ID + "=?", new String[] { "" + iExam.getID() });
		close();
	}

	@Override
	protected Exam itemFromCurrentPos(Cursor iCursor) {
		int id = iCursor.getInt(iCursor.getColumnIndex(DatabaseHelper.COL_EXAM_ID));
		int courseID = iCursor.getInt(iCursor.getColumnIndex(DatabaseHelper.COL_EXAM_CourseID));
		int blockID = iCursor.getInt(iCursor.getColumnIndex(DatabaseHelper.COL_EXAM_TimePlaceBlockID));
		String description = iCursor.getString(iCursor.getColumnIndex(DatabaseHelper.COL_EXAM_Description));
		
		TimePlaceBlock block = mTimePlaceBlockManager.get(blockID);
		Course course = mCourseManager.get(courseID);
		
		return new Exam(id, description, course, block);
	}

	@Override
	protected String getTableName() {
		return DatabaseHelper.TABLE_Exam;
	}

	@Override
	protected String getIDColumnName() {
		return DatabaseHelper.COL_EXAM_ID;
	}
}
