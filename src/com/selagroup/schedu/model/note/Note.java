/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */

package com.selagroup.schedu.model.note;

import java.util.Calendar;

/**
 * The Class Note.
 */
public abstract class Note {
	
	// Type of note
	public static enum NOTE_TYPE { TEXT, SKETCH, PHOTO, AUDIO };
	
	protected NOTE_TYPE mType;				// Type of this note
	private String mTitle;					// Title for this note
	private Calendar mDateCreated;			// Date this note was created
	
	/**
	 * Creates a new note and stores the current time as the creation time
	 * @param iType The type of note this is
	 */
	protected Note(NOTE_TYPE iType) {
		mType = iType;
		mDateCreated = Calendar.getInstance();
	}

	/**
	 * @return The note type
	 */
	public NOTE_TYPE getType() {
		return mType;
	}
	
	/**
	 * @return The note title
	 */
	public String getTitle() {
		return mTitle;
	}
	
	/**
	 * @param iTitle The new note title
	 */
	public void setTitle(String iTitle) {
		mTitle = iTitle;
	}
	
	/**
	 * @return The date this note was created
	 */
	public Calendar getDateCreated() {
		return mDateCreated;
	}
}
