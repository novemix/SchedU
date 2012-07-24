/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */
package com.selagroup.schedu.managers;

import android.database.Cursor;

import com.selagroup.schedu.database.DatabaseHelper;
import com.selagroup.schedu.model.Exam;

/**
 * The Class ExamManager.
 */
public class ExamManager extends Manager<Exam> {
	private TimePlaceBlockManager mTimePlaceBlockManager;
	
	public ExamManager(DatabaseHelper iHelper, TimePlaceBlockManager iTimePlaceBlockManager) {
		super(iHelper);
		mTimePlaceBlockManager = iTimePlaceBlockManager;
	}

	@Override
	public int insert(Exam iExam) {
		// If the exam already exists, just update the entry
		if (get(iExam.getID()) != null) {
			update(iExam);
			return iExam.getID();
		}

		open(OPEN_MODE.WRITE);
		int examID = (int) mDB.insert(DatabaseHelper.TABLE_Exam, null, iExam.getValues());
		iExam.setID(examID);
		
		// TODO: Also insert the TimePlaceBlock for this exam
		
		close();
		return examID;
	}

	@Override
	public void delete(Exam iExam) {
		open(OPEN_MODE.WRITE);
		mDB.delete(DatabaseHelper.TABLE_Exam, DatabaseHelper.COL_EXAM_ID + "=?", new String[] { "" + iExam.getID() });
		close();
		
		// TODO: Also delete the TimePlaceBlock for this exam
	}

	@Override
	public void update(Exam iExam) {
		open(OPEN_MODE.WRITE);
		mDB.update(DatabaseHelper.TABLE_Exam, iExam.getValues(), DatabaseHelper.COL_INSTRUCTOR_ID + "=?", new String[] { "" + iExam.getID() });
		close();
		
		// TODO: Also update the TimePlaceBlock for this exam
	}

	@Override
	protected Exam itemFromCurrentPos(Cursor iCursor) {
		// TODO Auto-generated method stub
		return null;
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
