package com.selagroup.schedu.managers;

import android.database.Cursor;

import com.selagroup.schedu.Utility;
import com.selagroup.schedu.database.DBHelper;
import com.selagroup.schedu.model.Term;

public class TermManager extends Manager<Term> {

	public TermManager(DBHelper iHelper) {
		super(iHelper);
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
		// Don't allow deletes for now
		// If implemented, would need to update all classes in this term
		// open(OPEN_MODE.WRITE);
		// mDB.delete(DatabaseHelper.TABLE_Term, DatabaseHelper.COL_TERM_ID + "=?", new String[] { "" + iTerm.getID() });
		// close();
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
