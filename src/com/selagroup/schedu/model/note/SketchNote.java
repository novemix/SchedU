/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */

package com.selagroup.schedu.model.note;

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
	protected SketchNote(NOTE_TYPE iType, int iWidth, int iHeight) {
		super(iType);
		mSketch = Bitmap.createBitmap(iWidth, iHeight, Bitmap.Config.ARGB_8888);
	}
	
	public Bitmap getSketch() {
		return mSketch;
	}
}
