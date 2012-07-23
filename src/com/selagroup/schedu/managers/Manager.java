/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */

package com.selagroup.schedu.managers;

import java.util.ArrayList;

import com.selagroup.schedu.database.DatabaseHelper;
import com.selagroup.schedu.database.IContentValueItem;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * The Class Manager.
 */
public abstract class Manager<T extends IContentValueItem> {

	protected SQLiteDatabase mDB = null;
	private DatabaseHelper mHelper = null;
	protected ArrayList<T> mItemsTmp = new ArrayList<T>();

	public static enum OPEN_MODE {
		READ, WRITE
	};
	
	/**
	 * Instantiates a new manager.
	 * @param iContext the i context
	 */
	public Manager(DatabaseHelper iHelper) {
		mHelper = iHelper;
	}
	
	/**
	 * Inserts a new item into the database
	 * @param iItem the new item to add
	 * @return the ID of the item added
	 */
	public abstract int insert(T iItem);
	
	/**
	 * Deletes the item with the same ID as iItem from the database
	 * @param iItem the item to delete
	 */
	public abstract void delete(T iItem);
	
	/**
	 * Updates the item in the database with the same ID as iItem to match the properties of iItem
	 * @param iItem the item to update
	 */
	public abstract void update(T iItem);
	
	/**
	 * Reads an item from a cursor returned by a query
	 * @param iCursor the cursor, set to the correct position
	 * @return the newly constructed item
	 */
	protected abstract T itemFromCurrentPos(Cursor iCursor);
	
	/**
	 * Gets the item from a query based on its ID
	 * @param iItemID the item ID
	 * @return the item
	 */
	public T get(int iItemID) {
		T item = null;
		open(OPEN_MODE.READ);
		Cursor cursor = mDB.query(DatabaseHelper.TABLE_Note, null, DatabaseHelper.COL_NOTE_ID + "=?", new String[] { "" + iItemID }, null, null, null);
		if (cursor.moveToFirst()) {
			item = itemFromCurrentPos(cursor);
		}
		close();
		return item;
	}
	
	/**
	 * Gets all items of type T from database
	 * @return all items in an ArrayList
	 */
	public ArrayList<T> getAll() {
		mItemsTmp.clear();
		open(OPEN_MODE.READ);
		
		// Loop query for all notes and loop through the cursor, adding notes
		Cursor cursor = mDB.query(DatabaseHelper.TABLE_Note, null, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				T itemToAdd = itemFromCurrentPos(cursor);
				if (itemToAdd != null) {
					mItemsTmp.add(itemToAdd);
				}
			} while (cursor.moveToNext());
		}
		
		close();
		
		return mItemsTmp;
	}

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
