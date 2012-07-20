/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */

package com.selagroup.schedu.database;

import java.util.ArrayList;

import android.database.Cursor;

import com.selagroup.schedu.model.note.Note;

/**
 * The Class NoteManager.
 */
public class NoteManager extends Manager<Note> {

	/**
	 * Instantiates a new scheduler DAL.
	 * 
	 * @param dbHelper the context
	 */
	public NoteManager(DatabaseHelper iHelper) {
		super(iHelper);
	}

	@Override
	public void add(Note iNote) {
		open(OPEN_MODE.WRITE);
		mDB.insert(DatabaseHelper.TABLE_Note, null, iNote.getValues());
		close();
	}

	@Override
	public void remove(Note iNote) {
		open(OPEN_MODE.WRITE);
		mDB.delete(DatabaseHelper.TABLE_Note, DatabaseHelper.COL_NOTE_ID + "=?", new String[] { "" + iNote.getID() });
		close();
	}

	@Override
	public void update(Note iNote) {
		open(OPEN_MODE.WRITE);
		mDB.update(DatabaseHelper.TABLE_Note, iNote.getValues(), DatabaseHelper.COL_NOTE_ID + "=?", new String[] { "" + iNote.getID() });
		close();
	}

	@Override
	public ArrayList<Note> getAll() {
		ArrayList<Note> notes = new ArrayList<Note>();
		
		Cursor cursor = mDB.query(DatabaseHelper.TABLE_Note, null, null, null, null, null, null);
		if (cursor.moveToFirst()) {
//			while (cursor.moveToNext()) {
//				
//			}
		}
		
		do {
			// int type = cursor.getInt(4); // Note type
			// notes.add(object);
		} while (cursor.moveToNext());
		
		return notes;
	}

	@Override
	public Note get(int iNoteID) {
		return null;
	}

	/**
	 * Synchronizes information stored in the database with the file system.
	 * This is done to check for any files that have been deleted
	 */
	protected void syncWithFileSystem() {
		
	}
}
