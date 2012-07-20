/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */

package com.selagroup.schedu.model.note;

import com.selagroup.schedu.model.Course;

import android.content.ContentValues;
import android.graphics.Bitmap;

/**
 * The Class SketchNote.
 */
public class SketchNote extends Note {
	private Bitmap mSketch;

	/**
	 * Instantiates a new sketch note.
	 * @param iType the i type
	 */
	public SketchNote(int iID, Course iCourse, String iDescription, int iWidth, int iHeight) {
		super(iID, NOTE_TYPE.SKETCH, iCourse, iDescription);
		mSketch = Bitmap.createBitmap(iWidth, iHeight, Bitmap.Config.ARGB_8888);
	}
	
	public Bitmap getSketch() {
		return mSketch;
	}

	public ContentValues getValues() {
		// TODO Auto-generated method stub
		return null;
	}
}
