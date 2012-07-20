/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */

package com.selagroup.schedu.database;

import java.util.ArrayList;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * The Class Manager.
 */
public abstract class Manager<T extends IContentValueItem> {

	protected SQLiteDatabase mDB = null;
	private DatabaseHelper mHelper = null;

	public static enum OPEN_MODE {
		READ, WRITE
	};
	
	/**
	 * Instantiates a new manager.
	 * @param iContext the i context
	 */
	public Manager(DatabaseHelper iHelper) {
		mHelper = iHelper;
		syncWithFileSystem();
	}
	
	public abstract void add(T iItem);
	
	public abstract void remove(T iItem);
	
	public abstract void update(T iItem);
	
	public abstract ArrayList<T> getAll();
	
	public abstract T get(int iItemID);
	
	protected abstract void syncWithFileSystem();

	/**
	 * Open the database for reading or writing
	 * @return true if the operation succeeded
	 */
	public boolean open(OPEN_MODE mode) {
		if (mode == OPEN_MODE.WRITE) {
			mDB = mHelper.getWritableDatabase();
			return true;
		}
		else if (mode == OPEN_MODE.READ) {
			mDB = mHelper.getReadableDatabase();
			return true;
		}
		if (mDB == null) {
			Log.i("DB", "null");
		}
		return false;
	}

	/**
	 * Close the database, if it's open
	 */
	protected void close() {
		if (mDB != null) {
			mDB.close();
			mDB = null;
		}
	}
}
