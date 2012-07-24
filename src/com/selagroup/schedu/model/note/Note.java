/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */

package com.selagroup.schedu.model.note;

import java.util.Calendar;

import android.content.ContentValues;

import com.selagroup.schedu.database.DBHelper;
import com.selagroup.schedu.model.ContentValueItem;
import com.selagroup.schedu.model.Course;

/**
 * The Class Note.
 */
public abstract class Note extends ContentValueItem {

	// Type of note
	public static enum NOTE_TYPE {
		TEXT, SKETCH, PHOTO, AUDIO;
		public static final NOTE_TYPE[] VALUES_ARRAY = NOTE_TYPE.values();
	};

	protected NOTE_TYPE mType;				// Type of this note
	private Course mCourse;					// The course this note is for
	private String mDescription;			// Title for this note
	private String mFilePath;				// File path for the data stored in this note
	private Calendar mDateCreated;			// Date this note was created

	public Note(int iID, NOTE_TYPE iType, Course iCourse, String iDescription) {
		super(iID);
		mType = iType;
		mCourse = iCourse;
		mDescription = iDescription;
		mFilePath = "";						// Should initialize with some file path that makes sense for the Android
		mDateCreated = Calendar.getInstance();
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

	@Override
	public ContentValues getValues() {
		ContentValues values = new ContentValues();
		values.put(DBHelper.COL_NOTE_Title, mDescription);
		values.put(DBHelper.COL_NOTE_CreationDate, mDateCreated.getTimeInMillis());
		values.put(DBHelper.COL_NOTE_NoteTypeEnum, mType.ordinal());
		values.put(DBHelper.COL_NOTE_FilePath, mFilePath);
		values.put(DBHelper.COL_NOTE_CourseID, mCourse.getID());
		return values;
	}
}
