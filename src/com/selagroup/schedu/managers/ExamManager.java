/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */
package com.selagroup.schedu.managers;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;

import com.selagroup.schedu.database.DBHelper;
import com.selagroup.schedu.model.Course;
import com.selagroup.schedu.model.Exam;
import com.selagroup.schedu.model.TimePlaceBlock;

/**
 * The Class ExamManager.
 */
public class ExamManager extends Manager<Exam> {
	private TimePlaceBlockManager mTimePlaceBlockManager;
	private CourseManager mCourseManager;
	
	public ExamManager(DBHelper iHelper, TimePlaceBlockManager iTimePlaceBlockManager, CourseManager iCourseManager) {
		super(iHelper);
		mTimePlaceBlockManager = iTimePlaceBlockManager;
		mCourseManager = iCourseManager;
	}
	
	public List<Exam> getAllForCourse(int iCourseID) {
		List<Exam> exams = new ArrayList<Exam>();
		
		// Open the database, query for all exams matching the courseID, and add them to the list
		open(OPEN_MODE.READ);
		Cursor cursor = mDB.query(DBHelper.TABLE_Exam, null, DBHelper.COL_EXAM_CourseID + "=?", new String[]{ "" + iCourseID }, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				exams.add(itemFromCurrentPos(cursor));
			} while (cursor.moveToNext());
		}
		close();
		
		return exams;
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
		int examID = (int) mDB.insert(DBHelper.TABLE_Exam, null, iExam.getValues());
		iExam.setID(examID);
		
		close();
		return examID;
	}

	@Override
	public void delete(Exam iExam) {
		// Delete the TimePlaceBlock for this exam
		mTimePlaceBlockManager.insert(iExam.getBlock());
		
		open(OPEN_MODE.WRITE);
		mDB.delete(DBHelper.TABLE_Exam, DBHelper.COL_EXAM_ID + "=?", new String[] { "" + iExam.getID() });
		close();
	}

	@Override
	public void update(Exam iExam) {
		// Update the TimePlaceBlock for this exam
		mTimePlaceBlockManager.update(iExam.getBlock());
		
		open(OPEN_MODE.WRITE);
		mDB.update(DBHelper.TABLE_Exam, iExam.getValues(), DBHelper.COL_INSTRUCTOR_ID + "=?", new String[] { "" + iExam.getID() });
		close();
	}

	@Override
	protected Exam itemFromCurrentPos(Cursor iCursor) {
		int id = iCursor.getInt(iCursor.getColumnIndex(DBHelper.COL_EXAM_ID));
		int courseID = iCursor.getInt(iCursor.getColumnIndex(DBHelper.COL_EXAM_CourseID));
		int blockID = iCursor.getInt(iCursor.getColumnIndex(DBHelper.COL_EXAM_TimePlaceBlockID));
		String description = iCursor.getString(iCursor.getColumnIndex(DBHelper.COL_EXAM_Description));
		
		TimePlaceBlock block = mTimePlaceBlockManager.get(blockID);
		Course course = mCourseManager.get(courseID);
		
		return new Exam(id, description, course, block);
	}

	@Override
	protected String getTableName() {
		return DBHelper.TABLE_Exam;
	}

	@Override
	protected String getIDColumnName() {
		return DBHelper.COL_EXAM_ID;
	}
}
