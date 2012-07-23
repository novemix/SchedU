package com.selagroup.schedu.managers;

import android.database.Cursor;

import com.selagroup.schedu.database.DatabaseHelper;
import com.selagroup.schedu.managers.Manager.OPEN_MODE;
import com.selagroup.schedu.model.Instructor;

public class InstructorManager extends Manager<Instructor> {

	public InstructorManager(DatabaseHelper iHelper) {
		super(iHelper);
	}

	@Override
	public int insert(Instructor iInstructor) {
		// If the note already exists, just update the entry
		if (get(iInstructor.getID()) != null) {
			update(iInstructor);
			return iInstructor.getID();
		}

		open(OPEN_MODE.WRITE);
		long instructorID = mDB.insert(DatabaseHelper.TABLE_Instructor, null, iInstructor.getValues());
		close();
		return (int) instructorID;
	}

	@Override
	public void delete(Instructor iInstructor) {
		// Don't allow deletes for now
		// If implemented, would need to update all classes taught by this instructor with NULLs
	}

	@Override
	public void update(Instructor iInstructor) {
		open(OPEN_MODE.WRITE);
		mDB.update(DatabaseHelper.TABLE_Instructor, iInstructor.getValues(), DatabaseHelper.COL_INSTRUCTOR_ID + "=?", new String[] { "" + iInstructor.getID() });
		close();
	}

	@Override
	protected Instructor itemFromCurrentPos(Cursor iCursor) {
		// TODO Auto-generated method stub
		return null;
	}

}
