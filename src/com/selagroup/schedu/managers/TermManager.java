package com.selagroup.schedu.managers;

import android.database.Cursor;

import com.selagroup.schedu.Utility;
import com.selagroup.schedu.database.DatabaseHelper;
import com.selagroup.schedu.model.Term;

public class TermManager extends Manager<Term> {

	public TermManager(DatabaseHelper iHelper) {
		super(iHelper);
	}

	@Override
	public int insert(Term iTerm) {
		// If the term already exists, just update the entry
		if (get(iTerm.getID()) != null) {
			update(iTerm);
			return iTerm.getID();
		}

		open(OPEN_MODE.WRITE);
		int termID = (int) mDB.insert(DatabaseHelper.TABLE_Term, null, iTerm.getValues());
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
		mDB.update(DatabaseHelper.TABLE_Term, iTerm.getValues(), DatabaseHelper.COL_TERM_ID + "=?", new String[] { "" + iTerm.getID() });
		close();
	}

	@Override
	protected Term itemFromCurrentPos(Cursor iCursor) {
		int id = iCursor.getInt(iCursor.getColumnIndex(DatabaseHelper.COL_TERM_ID));
		long startDate = iCursor.getInt(iCursor.getColumnIndex(DatabaseHelper.COL_TERM_StartDate));
		long endDate = iCursor.getInt(iCursor.getColumnIndex(DatabaseHelper.COL_TERM_EndDate));
		
		return new Term(id, Utility.calendarFromInt(startDate), Utility.calendarFromInt(endDate));
	}
	
	@Override
	protected String getTableName() {
		return DatabaseHelper.TABLE_Term;
	}

	@Override
	protected String getIDColumnName() {
		return DatabaseHelper.COL_TERM_ID;
	}
}
