/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */

package com.selagroup.schedu.model.note;

import com.selagroup.schedu.model.Course;

import android.content.ContentValues;

/**
 * The Class PhotoNote.
 */
public class PhotoNote extends Note {
	public PhotoNote(int iID, Course iCourse, String iDescription) {
		super(iID, NOTE_TYPE.PHOTO, iCourse, iDescription);
	}

	public ContentValues getValues() {
		// TODO Auto-generated method stub
		return null;
	}
}
