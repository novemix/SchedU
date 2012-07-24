/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */
package com.selagroup.schedu.managers;

import android.database.Cursor;

import com.selagroup.schedu.database.DatabaseHelper;
import com.selagroup.schedu.model.Instructor;

/**
 * The Class InstructorManager.
 */
public class InstructorManager extends Manager<Instructor> {
	
	public InstructorManager(DatabaseHelper iHelper) {
		super(iHelper);
	}

	@Override
	public int insert(Instructor iInstructor) {
		// If the instructor already exists, just update the entry
		if (get(iInstructor.getID()) != null) {
			update(iInstructor);
			return iInstructor.getID();
		}

		open(OPEN_MODE.WRITE);
		int instructorID = (int) mDB.insert(DatabaseHelper.TABLE_Instructor, null, iInstructor.getValues());
		iInstructor.setID(instructorID);
		
		close();
		return instructorID;
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
		int id = iCursor.getInt(iCursor.getColumnIndex(DatabaseHelper.COL_INSTRUCTOR_ID));
		String name = iCursor.getString(iCursor.getColumnIndex(DatabaseHelper.COL_INSTRUCTOR_Name));
		String email = iCursor.getString(iCursor.getColumnIndex(DatabaseHelper.COL_INSTRUCTOR_Email));
		String phone = iCursor.getString(iCursor.getColumnIndex(DatabaseHelper.COL_INSTRUCTOR_Phone));
		
		return new Instructor(id, name, phone, email);
	}
	
	@Override
	protected String getTableName() {
		return DatabaseHelper.TABLE_Instructor;
	}

	@Override
	protected String getIDColumnName() {
		return DatabaseHelper.COL_INSTRUCTOR_ID;
	}
}
