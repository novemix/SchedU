/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */
package com.selagroup.schedu.managers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import android.database.Cursor;
import android.util.SparseArray;

import com.selagroup.schedu.Utility;
import com.selagroup.schedu.database.DBHelper;
import com.selagroup.schedu.model.Course;
import com.selagroup.schedu.model.Location;
import com.selagroup.schedu.model.TimePlaceBlock;

/**
 * The Class TimePlaceBlockManager.
 */
public class TimePlaceBlockManager extends Manager<TimePlaceBlock> {

	private LocationManager mLocationManager;

	public TimePlaceBlockManager(DBHelper iHelper, LocationManager iLocationManager) {
		super(iHelper);
		mLocationManager = iLocationManager;
	}

	@Override
	public int insert(TimePlaceBlock iBlock) {
		if (iBlock == null) {
			return -1;
		}

		// If the term already exists, just update the entry
		if (get(iBlock.getID()) != null) {
			update(iBlock);
			return iBlock.getID();
		}
		Location location = iBlock.getLocation();
		location.setID(mLocationManager.insert(location));

		open(OPEN_MODE.WRITE);
		int blockID = (int) mDB.insert(DBHelper.TABLE_TimePlaceBlock, null, iBlock.getValues());
		iBlock.setID(blockID);
		close();
		return blockID;
	}

	@Override
	public void delete(TimePlaceBlock iBlock) {
		mLocationManager.delete(iBlock.getLocation());

		open(OPEN_MODE.WRITE);
		mDB.delete(DBHelper.TABLE_TimePlaceBlock, DBHelper.COL_TIME_PLACE_BLOCK_ID + "=?", new String[] { "" + iBlock.getID() });
		close();
	}

	@Override
	public void update(TimePlaceBlock iBlock) {
		mLocationManager.update(iBlock.getLocation());

		open(OPEN_MODE.WRITE);
		mDB.update(DBHelper.TABLE_TimePlaceBlock, iBlock.getValues(), DBHelper.COL_TIME_PLACE_BLOCK_ID + "=?", new String[] { "" + iBlock.getID() });
		close();
	}

	@Override
	protected TimePlaceBlock itemFromCurrentPos(Cursor iCursor) {
		int id = iCursor.getInt(iCursor.getColumnIndex(DBHelper.COL_TIME_PLACE_BLOCK_ID));
		long startTime = iCursor.getLong(iCursor.getColumnIndex(DBHelper.COL_TIME_PLACE_BLOCK_StartTime));
		long endTime = iCursor.getLong(iCursor.getColumnIndex(DBHelper.COL_TIME_PLACE_BLOCK_EndTime));
		int dayFlag = iCursor.getInt(iCursor.getColumnIndex(DBHelper.COL_TIME_PLACE_BLOCK_DayFlag));
		int locationID = iCursor.getInt(iCursor.getColumnIndex(DBHelper.COL_TIME_PLACE_BLOCK_LocationID));

		Location location = mLocationManager.get(locationID);

		return new TimePlaceBlock(id, location, Utility.calendarFromInt(startTime), Utility.calendarFromInt(endTime), dayFlag);
	}

	@Override
	protected String getTableName() {
		return DBHelper.TABLE_TimePlaceBlock;
	}

	@Override
	protected String getIDColumnName() {
		return DBHelper.COL_TIME_PLACE_BLOCK_ID;
	}

	/**
	 * Get the next TimePlaceBlock from the current time and for any course or for all courses
	 * @param iNow The current time
	 * @param iCourse The course to get the next block for, or null for any course
	 * @param oBlock Output, the next block from the current time
	 * @param oDay The day the next block occurs on
	 * @param oCourseID The ID of the course associated with the block returned
	 */
	public void nextBlock(Calendar iNow, Course iCourse, TimePlaceBlock oBlock, Calendar oDay, Integer oCourseID) {
		String selectedCourseIDs = "*";
		if (iCourse != null) {
			selectedCourseIDs = "" + iCourse.getID();
		}

		SparseArray<Integer> blockToCourse = new SparseArray<Integer>();
		ArrayList<TimePlaceBlock> blocks = new ArrayList<TimePlaceBlock>();
		TreeMap<Calendar, TimePlaceBlock> startTimes = new TreeMap<Calendar, TimePlaceBlock>();

		// Get all time blocks for the associated course(s)
		open(OPEN_MODE.READ);
		Cursor cursor = mDB.rawQuery("SELECT " + DBHelper.COL_TIME_PLACE_BLOCK_ALL_COL + ", " + DBHelper.TABLE_CourseTimePlaceBlock + "." + DBHelper.COL_COURSE_TIME_PLACE_BLOCK_CourseID + " FROM "
		        + DBHelper.TABLE_TimePlaceBlock + " INNER JOIN " + DBHelper.TABLE_CourseTimePlaceBlock + " ON " + DBHelper.TABLE_CourseTimePlaceBlock + "."
		        + DBHelper.COL_COURSE_TIME_PLACE_BLOCK_TimePlaceBlockID + " = " + DBHelper.TABLE_TimePlaceBlock + "." + DBHelper.COL_TIME_PLACE_BLOCK_ID + " WHERE ("
		        + DBHelper.TABLE_CourseTimePlaceBlock + "." + DBHelper.COL_COURSE_TIME_PLACE_BLOCK_CourseID + " = ?)", new String[] { "" + selectedCourseIDs });

		if (cursor.moveToFirst()) {
			do {
				int courseID = cursor.getInt(cursor.getColumnIndex(DBHelper.TABLE_CourseTimePlaceBlock + "." + DBHelper.COL_COURSE_TIME_PLACE_BLOCK_CourseID));
				TimePlaceBlock block = itemFromCurrentPos(cursor);

				blocks.add(itemFromCurrentPos(cursor));
				blockToCourse.put(block.getID(), courseID);
			} while (cursor.moveToNext());
		}
		cursor.close();
		close();

		// Add start times for each block on each day
		Calendar startTime;
		for (TimePlaceBlock block : blocks) {
			startTime = (Calendar) block.getStartTime().clone();
			startTime.set(iNow.get(Calendar.YEAR), iNow.get(Calendar.MONTH), iNow.get(Calendar.DAY_OF_MONTH));
			boolean[] days = block.getDayFlagArray();
			startTime.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
			
			// Loop through the days storing the start time for each day
			for (int dayIndex = 0; dayIndex < 7; ++dayIndex, startTime.add(Calendar.DAY_OF_WEEK, 1)) {
				if (days[dayIndex]) {
					startTimes.put((Calendar) startTime.clone(), block);
				}
			}
		}

		// Find the closest course block and return the data for it
		SortedMap<Calendar, TimePlaceBlock> tmpMap = startTimes.tailMap(iNow);
		oDay = tmpMap.firstKey();
		oBlock = tmpMap.get(oDay);
		oCourseID = blockToCourse.get(oBlock.getID());
	}
}
