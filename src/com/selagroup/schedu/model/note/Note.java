/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */

package com.selagroup.schedu.model.note;

import java.util.Calendar;

import com.selagroup.schedu.database.IContentValueItem;
import com.selagroup.schedu.model.Course;

/**
 * The Class Note.
 */
public abstract class Note implements IContentValueItem {
	
	// Type of note
	public static enum NOTE_TYPE { TEXT, SKETCH, PHOTO, AUDIO };
	
	private int mID;						// Note ID
	protected NOTE_TYPE mType;				// Type of this note
	private Course mCourse;					// The course this note is for
	private String mDescription;			// Title for this note
	private String mFilePath;				// File path for the data stored in this note
	private Calendar mDateCreated;			// Date this note was created
	
	public Note(int iID, NOTE_TYPE iType, Course iCourse, String iDescription) {
		mID = iID;
		mType = iType;
		mCourse = iCourse;
		mDescription = iDescription;
		mFilePath = "";						// Should initialize with some file path that makes sense for the Android
		mDateCreated = Calendar.getInstance();
	}
	
	/**
	 * @return The note ID
	 */
	public int getID() {
		return mID;
	}

	/**
	 * @return The note type
	 */
	public NOTE_TYPE getType() {
		return mType;
	}
	
	/**
	 * Gets the course.
	 * @return the course
	 */
	public Course getCourse() {
		return mCourse;
	}
	
	/**
	 * @return The note description
	 */
	public String getDescription() {
		return mDescription;
	}
	
	/**
	 * @param iDescription The new note description
	 */
	public void setDescription(String iDescription) {
		mDescription = iDescription;
	}
	
	/**
	 * @return the file path
	 */
	public String getFilePath() {
		return mFilePath;
	}
	
	/**
	 * @return The date this note was created
	 */
	public Calendar getDateCreated() {
		return mDateCreated;
	}
}
