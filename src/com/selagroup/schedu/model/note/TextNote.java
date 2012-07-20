/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */

package com.selagroup.schedu.model.note;

import android.content.ContentValues;

/**
 * The Class TextNote.
 */
public class TextNote extends Note {
	private String mText = "";
	
	/**
	 * Instantiates a new text note.
	 * @param iType the i type
	 */
	protected TextNote() {
		super(NOTE_TYPE.TEXT);
	}
	
	/**
	 * Gets the text.
	 * @return the text
	 */
	public String getText() {
		return mText;
	}

	public ContentValues getValues() {
		// TODO Auto-generated method stub
		return null;
	}
}
