/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */

package com.selagroup.schedu.model.note;

/**
 * The Class TextNote.
 */
public class TextNote extends Note {
	private String mText = "";
	
	/**
	 * Instantiates a new text note.
	 * @param iType the i type
	 */
	protected TextNote(NOTE_TYPE iType) {
		super(iType);
	}
	
	/**
	 * Gets the text.
	 * @return the text
	 */
	public String getText() {
		return mText;
	}
}
