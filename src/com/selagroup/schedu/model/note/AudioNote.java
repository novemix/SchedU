/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */

package com.selagroup.schedu.model.note;

import com.selagroup.schedu.model.Course;

import android.content.ContentValues;

/**
 * The Class AudioNote.
 */
public class AudioNote extends Note {
	public AudioNote(int iID, Course iCourse, String iDescription) {
		super(iID, NOTE_TYPE.AUDIO, iCourse, iDescription);
	}

	public ContentValues getValues() {
		// TODO Auto-generated method stub
		return null;
	}
}
