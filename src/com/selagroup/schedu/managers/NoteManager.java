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
import com.selagroup.schedu.model.note.AudioNote;
import com.selagroup.schedu.model.note.Note;
import com.selagroup.schedu.model.note.PhotoNote;
import com.selagroup.schedu.model.note.SketchNote;
import com.selagroup.schedu.model.note.TextNote;

/**
 * The Class NoteManager.
 */
public class NoteManager extends Manager<Note> {
	private CourseManager mCourseManager;

	/**
	 * Instantiates a new note manager.
	 * 
	 * @param iHelper the helper
	 * @param iCourseManager the course manager that will manage courses associated with these notes
	 */
	public NoteManager(DBHelper iHelper, CourseManager iCourseManager) {
		super(iHelper);
		mCourseManager = iCourseManager;
		syncWithFileSystem();
	}
	
	public List<Note> getAllForCourse(int iCourseID) {
		List<Note> notes = new ArrayList<Note>();
		
		// Open the database, query for all notes matching the courseID, and add them to the list
		open(OPEN_MODE.READ);
		Cursor cursor = mDB.query(DBHelper.TABLE_Note, null, DBHelper.COL_NOTE_CourseID + "=?", new String[]{ "" + iCourseID }, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				notes.add(itemFromCurrentPos(cursor));
			} while (cursor.moveToNext());
		}
		close();
		
		return notes;
	}

	@Override
	public int insert(Note iNote) {
		// If the note already exists, just update the entry
		if (get(iNote.getID()) != null) {
			update(iNote);
			return iNote.getID();
		}

		open(OPEN_MODE.WRITE);
		int noteID = (int) mDB.insert(DBHelper.TABLE_Note, null, iNote.getValues());
		iNote.setID(noteID);
		close();
		return noteID;
	}

	@Override
	public void delete(Note iNote) {
		open(OPEN_MODE.WRITE);
		mDB.delete(DBHelper.TABLE_Note, DBHelper.COL_NOTE_ID + "=?", new String[] { "" + iNote.getID() });
		close();
	}

	@Override
	public void update(Note iNote) {
		open(OPEN_MODE.WRITE);
		mDB.update(DBHelper.TABLE_Note, iNote.getValues(), DBHelper.COL_NOTE_ID + "=?", new String[] { "" + iNote.getID() });
		close();
	}

	@Override
	protected Note itemFromCurrentPos(Cursor iCursor) {
		// Get note data
		int noteID = iCursor.getInt(iCursor.getColumnIndex(DBHelper.COL_NOTE_ID));
		String title = iCursor.getString(iCursor.getColumnIndex(DBHelper.COL_NOTE_Title));
		int courseID = iCursor.getInt(iCursor.getColumnIndex(DBHelper.COL_NOTE_CourseID));
		int typeIndex = iCursor.getInt(iCursor.getColumnIndex(DBHelper.COL_NOTE_NoteTypeEnum));

		// Convert type to an integer
		Note.NOTE_TYPE type = Note.NOTE_TYPE.VALUES_ARRAY[typeIndex];
		Note newNote = null;

		// Get the course from the courseID
		Course course = mCourseManager.get(courseID);

		// Create a new note
		switch (type) {
		case TEXT:
			newNote = new TextNote(noteID, course, title);
			break;
		case SKETCH:
			newNote = new SketchNote(noteID, course, title, 100, 100);
			break;
		case AUDIO:
			newNote = new AudioNote(noteID, course, title);
			break;
		case PHOTO:
			newNote = new PhotoNote(noteID, course, title);
			break;
		}

		return newNote;
	}
	
	@Override
	protected String getTableName() {
		return DBHelper.TABLE_Note;
	}

	@Override
	protected String getIDColumnName() {
		return DBHelper.COL_NOTE_ID;
	}

	/**
	 * Synchronizes information stored in the database with the file system. This is done to check for any files that have been deleted
	 */
	private void syncWithFileSystem() {

	}
}
