/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */

package com.selagroup.schedu.model.note;

import com.selagroup.schedu.model.Course;

/**
 * The Class AudioNote.
 */
public class AudioNote extends Note {
    private static final long serialVersionUID = 3416308504921113800L;

	public AudioNote(int iID, Course iCourse, String iDescription) {
		super(iID, NOTE_TYPE.AUDIO, iCourse, iDescription);
	}
}
