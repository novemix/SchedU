/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */

package com.selagroup.schedu.model.note;

import com.selagroup.schedu.model.Course;

/**
 * The Class TextNote.
 */
public class TextNote extends Note {
	private String mText = "";
	
	/**
	 * Instantiates a new text note.
	 * @param iType the i type
	 */
	public TextNote(int iID, Course iCourse, String iDescription) {
		super(iID, NOTE_TYPE.TEXT, iCourse, iDescription);
	}
	
	/**
	 * Gets the text.
	 * @return the text
	 */
	public String getText() {
		return mText;
	}
}
