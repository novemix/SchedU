package com.selagroup.schedu.managers;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;

import com.selagroup.schedu.Utility;
import com.selagroup.schedu.database.DBHelper;
import com.selagroup.schedu.model.Course;
import com.selagroup.schedu.model.Term;

public class TermManager extends Manager<Term> {
	
	CourseManager mCourseManager;

	public TermManager(DBHelper iHelper) {
		super(iHelper);
	}

	public void setCourseManager(CourseManager iCourseManager) {
	    mCourseManager = iCourseManager;
    }

	@Override
	public int insert(Term iTerm) {
		if (iTerm == null) {
			return -1;
		}
		
		// If the term already exists, just update the entry
		if (get(iTerm.getID()) != null) {
			update(iTerm);
			return iTerm.getID();
		}

		open(OPEN_MODE.WRITE);
		int termID = (int) mDB.insert(DBHelper.TABLE_Term, null, iTerm.getValues());
		iTerm.setID(termID);
		close();
		return termID;
	}

	@Override
	public void delete(Term iTerm) {
		// Delete all courses for this term
		for (Course course : mCourseManager.getAllForTerm(iTerm.getID())) {
			mCourseManager.delete(course);
		}
		
		open(OPEN_MODE.WRITE);
		mDB.delete(DBHelper.TABLE_Term, DBHelper.COL_TERM_ID + "=?", new String[] { "" + iTerm.getID() });
		close();
	}

	@Override
	public void update(Term iTerm) {
		open(OPEN_MODE.WRITE);
		
		// Update term table
		mDB.update(DBHelper.TABLE_Term, iTerm.getValues(), DBHelper.COL_TERM_ID + "=?", new String[] { "" + iTerm.getID() });
		close();
	}

	@Override
	protected Term itemFromCurrentPos(Cursor iCursor) {
		int id = iCursor.getInt(iCursor.getColumnIndex(DBHelper.COL_TERM_ID));
		long startDate = iCursor.getLong(iCursor.getColumnIndex(DBHelper.COL_TERM_StartDate));
		long endDate = iCursor.getLong(iCursor.getColumnIndex(DBHelper.COL_TERM_EndDate));
		
		return new Term(id, Utility.calendarFromInt(startDate), Utility.calendarFromInt(endDate));
	}
	
	@Override
	protected String getTableName() {
		return DBHelper.TABLE_Term;
	}

	@Override
	protected String getIDColumnName() {
		return DBHelper.COL_TERM_ID;
	}
}
