/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */
package com.selagroup.schedu.database;

import com.selagroup.schedu.model.note.Note;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SchedulerDAL {
	
	// Reference to to the database helper
	private DatabaseHelper mHelper = null;
	private SQLiteDatabase mDB;

	/**
	 * Instantiates a new scheduler DAL.
	 *
	 * @param iContext the context
	 */
	public SchedulerDAL(Context iContext) {
		mHelper = new DatabaseHelper(iContext);
	}
	
	// Example, not to be used yet
	public void addNote(Note iNote) {
		open();
		ContentValues values = null;
		mDB.insert(DatabaseHelper.TABLE_Note, null, values);
		close();
	}
	
	/**
	 * Synchronizes information stored in the database with the file system.
	 * This should be done when starting the app to check for any files that have been deleted
	 */
	public void syncWithFileSystem() {
		
	}

	/**
	 * Open the database for writing
	 */
	private void open() {
		mDB = mHelper.getWritableDatabase();
		if (mDB == null) {
			Log.i("DB", "null");
		}
	}

	/**
	 * Close the database
	 */
	private void close() {
		if (mDB != null) {
			mDB.close();
		}
	}
}
