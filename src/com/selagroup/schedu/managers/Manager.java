/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */

package com.selagroup.schedu.managers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.selagroup.schedu.database.DatabaseHelper;
import com.selagroup.schedu.model.ContentValueItem;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * The Class Manager.
 */
public abstract class Manager<T extends ContentValueItem> {

	protected SQLiteDatabase mDB = null;
	protected ArrayList<T> mItemsTmp = new ArrayList<T>();
	protected DatabaseHelper mHelper = null;

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
	 * Tries to insert a new item into the database.
	 * If the item already exists, it performs an update.
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
	 * Reads an item from a cursor returned by a query.
	 * Precondition: the database must be opened for reading.
	 * @param iCursor the cursor, set to the correct position
	 * @return the newly constructed item
	 */
	protected abstract T itemFromCurrentPos(Cursor iCursor);
	
	/**
	 * Gets the item from a query based on its ID
	 * @param iItemID the item ID
	 * @return the item
	 */
	public final T get(int iItemID) {
		T item = null;
		open(OPEN_MODE.READ);
		Cursor cursor = mDB.query(getTableName(), null, getIDColumnName() + "=?", new String[] { "" + iItemID }, null, null, null);
		if (cursor.moveToFirst()) {
			item = itemFromCurrentPos(cursor);
		}
		cursor.close();
		close();
		return item;
	}
	
	/**
	 * Gets all items of type T from database
	 * @return all items in an ArrayList
	 */
	public final List<T> getAll() {
		mItemsTmp.clear();
		open(OPEN_MODE.READ);
		
		// Loop query for all items and loop through the cursor, adding items to the list
		T itemToAdd = null;
		Cursor cursor = mDB.query(getTableName(), null, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				itemToAdd = itemFromCurrentPos(cursor);
				if (itemToAdd != null) {
					mItemsTmp.add(itemToAdd);
				}
			} while (cursor.moveToNext());
		}
		cursor.close();
		close();
		
		return Collections.unmodifiableList(mItemsTmp);
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
	
	protected abstract String getTableName();
	protected abstract String getIDColumnName();
}
