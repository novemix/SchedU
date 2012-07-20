package com.selagroup.schedu.database;

import java.util.ArrayList;

import com.selagroup.schedu.model.note.Note;

public class NoteManager extends Manager<Note> {
	private ArrayList<Note> mAllNotes = new ArrayList<Note>();
	// private ArrayList<Note> mClassNoteCache = null;

	/**
	 * Instantiates a new scheduler DAL.
	 *
	 * @param dbHelper the context
	 */
	public NoteManager(DatabaseHelper dbHelper) {
		super(dbHelper);
	}
	
	@Override
	public void add(Note iNote) {
		open(null);
	}
	
	@Override
	public void remove(Note iNote) {
		
	}
	
	@Override
	public void update(Note iNote) {
		
	}
	
	@Override
	public ArrayList<Note> getAll() {
		return mAllNotes;
	}
	
	@Override
	public Note get(int iNoteID) {
		return null;
	}
	
	/**
	 * Synchronizes information stored in the database with the file system.
	 * This should be done when starting the app to check for any files that have been deleted
	 */
	public void syncWithFileSystem() {
		
	}
}
