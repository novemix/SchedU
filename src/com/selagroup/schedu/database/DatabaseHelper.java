/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */
package com.selagroup.schedu.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * The Course DatabaseHelper
 */
public class DatabaseHelper extends SQLiteOpenHelper {

	// Database name and version
	public static final String DATABASE_NAME = "schedu.db";
	public static final int DATABASE_VERSION = 1;

	// Tables
	// Term table
	public static final String TABLE_Term = "Term";
	public static final String COL_TERM_ID = "_id"; 						// Primary key
	public static final String COL_TERM_StartDate = "StartDate";
	public static final String COL_TERM_EndDate = "EndDate";

	// Instructor table
	public static final String TABLE_Instructor = "Instructor";
	public static final String COL_INSTRUCTOR_ID = "_id";					// Primary key
	public static final String COL_INSTRUCTOR_Name = "Name";
	public static final String COL_INSTRUCTOR_Email = "Email";
	public static final String COL_INSTRUCTOR_Phone = "Phone";

	// Course table
	public static final String TABLE_Course = "Course";
	public static final String COL_COURSE_ID = "_id"; 						// Primary key
	public static final String COL_COURSE_CourseCode = "CourseCode";
	public static final String COL_COURSE_CourseName = "CourseName";
	public static final String COL_COURSE_InstructorID = "Instructor_ID";	// Foreign key: Instructor.ID
	public static final String COL_COURSE_TermID = "Term_ID";				// Foreign key: Term.ID

	// Location table
	public static final String TABLE_Location = "Location";
	public static final String COL_LOCATION_ID = "_id";						// Primary key
	public static final String COL_LOCATION_Building = "Building";
	public static final String COL_LOCATION_Room = "Room";
	public static final String COL_LOCATION_GeoLocation = "GeoLocation";

	// TimePlaceBlock table
	public static final String TABLE_TimePlaceBlock = "TimePlaceBlock";
	public static final String COL_TIME_PLACE_BLOCK_ID = "_id";						// Primary key
	public static final String COL_TIME_PLACE_BLOCK_DayFlag = "DayFlag";
	public static final String COL_TIME_PLACE_BLOCK_StartTime = "StartTime";
	public static final String COL_TIME_PLACE_BLOCK_EndTime = "EndTime";
	public static final String COL_TIME_PLACE_BLOCK_LocationID = "Location_ID";		// Foreign key: Location.ID

	// CourseTimePlaceBlocks table (connecting table)
	public static final String TABLE_CourseTimeBlock = "CourseTimePlaceBlock";
	public static final String COL_COURSE_TIME_PLACE_BLOCK_TimePlaceBlockID = "TimePlaceBlock_ID";		// Foreign key: TimePlaceBlock.ID
	public static final String COL_COURSE_TIME_PLACE_BLOCK_CourseID = "Course_ID";						// Foreign key: Course.ID

	// OfficeTimePlaceBlocks table (connecting table)
	public static final String TABLE_OfficeTimePlaceBlock = "OfficeTimePlaceBlock";
	public static final String COL_OFFICE_TIME_PLACE_BLOCK_TimePlaceBlockID = "TimePlaceBlock_ID";	// Foreign key: TimePlaceBlock.ID
	public static final String COL_OFFICE_TIME_PLACE_BLOCK_CourseID = "Course_ID";					// Foreign key: Course.ID

	// Note table
	public static final String TABLE_Note = "Note";
	public static final String COL_NOTE_ID = "_id";								// Primary key
	public static final String COL_NOTE_Title = "Title";
	public static final String COL_NOTE_FilePath = "FilePath";
	public static final String COL_NOTE_CreationDate = "CreationDate";
	public static final String COL_NOTE_NoteTypeEnum = "NoteTypeEnum";			// TEXT = 0, SKETCH = 1, PHOTO = 2, AUDIO = 3
	public static final String COL_NOTE_CourseID = "Course_ID";					// Foreign key: Course.ID

	// Assignment table
	public static final String TABLE_Assignment = "Assignment";
	public static final String COL_ASSIGNMENT_ID = "_id";						// Primary key
	public static final String COL_ASSIGNMENT_Name = "Name";
	public static final String COL_ASSIGNMENT_DueDate = "DueDate";
	public static final String COL_ASSIGNMENT_CourseID = "Course_ID";			// Foreign key: Course.ID

	// Exam table
	public static final String TABLE_Exam = "Exam";
	public static final String COL_EXAM_ID = "_id";
	public static final String COL_EXAM_CourseID = "Course_ID";
	public static final String COL_EXAM_TimePlaceBlockID = "TimePlaceBlock_ID";
	public static final String COL_EXAM_Description = "Description";

	// Table creation statements
	public static final String TABLE_CREATE_Term = "(" +
			COL_TERM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			COL_TERM_StartDate + " INTEGER, " +
			COL_TERM_EndDate + " INTEGER);";

	public static final String TABLE_CREATE_Course = "(" +
			COL_COURSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			COL_COURSE_CourseCode + " INTEGER, " +
			COL_COURSE_CourseName + " TEXT, " +
			COL_COURSE_InstructorID + " INTEGER, " +
			COL_COURSE_TermID + " INTEGER, " +
			"FOREIGN KEY(" + COL_COURSE_InstructorID + ") REFERENCES " + TABLE_Instructor + "(" + COL_INSTRUCTOR_ID + "), " + 
			"FOREIGN KEY(" + COL_COURSE_TermID + ") REFERENCES " + TABLE_Term + "(" + COL_TERM_ID + "));";

	public static final String TABLE_CREATE_Instructor = "(" +
			COL_INSTRUCTOR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			COL_INSTRUCTOR_Name + " TEXT, " +
			COL_INSTRUCTOR_Email + " TEXT, " +
			COL_INSTRUCTOR_Phone + " TEXT);";

	public static final String TABLE_CREATE_Location = "(" +
			COL_LOCATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			COL_LOCATION_Building + " TEXT, " +
			COL_LOCATION_Room + " TEXT, " +
			COL_LOCATION_GeoLocation + " TEXT);";

	public static final String TABLE_CREATE_TimePlaceBlock = "(" +
			COL_TIME_PLACE_BLOCK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			COL_TIME_PLACE_BLOCK_DayFlag + " INTEGER, " +
			COL_TIME_PLACE_BLOCK_StartTime + " INTEGER, " +
			COL_TIME_PLACE_BLOCK_EndTime + " INTEGER, " +
			COL_TIME_PLACE_BLOCK_LocationID + " INTEGER, " +
			"FOREIGN KEY(" + COL_TIME_PLACE_BLOCK_LocationID + ") REFERENCES " + TABLE_Location + "(" + COL_LOCATION_ID + "));";

	public static final String TABLE_CREATE_CourseTimePlaceBlock = "(" +
			COL_COURSE_TIME_PLACE_BLOCK_CourseID + " INTEGER, " +
			COL_COURSE_TIME_PLACE_BLOCK_TimePlaceBlockID + " INTEGER, " +
			"FOREIGN KEY(" + COL_COURSE_TIME_PLACE_BLOCK_CourseID + ") REFERENCES " + TABLE_Course + "(" + COL_COURSE_ID + "), " +
			"FOREIGN KEY(" + COL_COURSE_TIME_PLACE_BLOCK_TimePlaceBlockID + ") REFERENCES " + TABLE_TimePlaceBlock + "(" + COL_TIME_PLACE_BLOCK_ID + "), " + 
			"PRIMARY KEY(" + COL_COURSE_TIME_PLACE_BLOCK_CourseID + ", " + COL_COURSE_TIME_PLACE_BLOCK_TimePlaceBlockID + "));";

	public static final String TABLE_CREATE_OfficeTimePlaceBlock = "(" +
			COL_OFFICE_TIME_PLACE_BLOCK_CourseID + " INTEGER, " +
			COL_OFFICE_TIME_PLACE_BLOCK_TimePlaceBlockID + " INTEGER, " +
			"FOREIGN KEY(" + COL_OFFICE_TIME_PLACE_BLOCK_CourseID + ") REFERENCES " + TABLE_Course + "(" + COL_COURSE_ID + "), " +
			"FOREIGN KEY(" + COL_OFFICE_TIME_PLACE_BLOCK_TimePlaceBlockID + ") REFERENCES " + TABLE_TimePlaceBlock + "(" + COL_TIME_PLACE_BLOCK_ID + "), " + 
			"PRIMARY KEY(" + COL_OFFICE_TIME_PLACE_BLOCK_CourseID + ", " + COL_OFFICE_TIME_PLACE_BLOCK_TimePlaceBlockID + "));";

	public static final String TABLE_CREATE_Note = "(" +
			COL_NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			COL_NOTE_Title + " TEXT, " +
			COL_NOTE_FilePath + " TEXT, " +
			COL_NOTE_NoteTypeEnum + " INTEGER, " +
			COL_NOTE_CreationDate + " INTEGER, " +
			COL_NOTE_CourseID + " INTEGER, " +
			"FOREIGN KEY(" + COL_NOTE_CourseID + ") REFERENCES " + TABLE_Course + "(" + COL_COURSE_ID + "));";

	public static final String TABLE_CREATE_Assignment = "(" +
			COL_ASSIGNMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			COL_ASSIGNMENT_Name + " TEXT, " +
			COL_ASSIGNMENT_DueDate + " INTEGER, " +
			COL_ASSIGNMENT_CourseID + " INTEGER, " +
			"FOREIGN KEY(" + COL_ASSIGNMENT_CourseID + ") REFERENCES " + TABLE_Course + "(" + COL_COURSE_ID + "));";

	public static final String TABLE_CREATE_Exam = "(" +
			COL_EXAM_ID + " INTEGER, " +
			COL_EXAM_Description + " TEXT, " +
			COL_EXAM_CourseID + " INTEGER, " +
			COL_EXAM_TimePlaceBlockID + " INTEGER, " +
			"FOREIGN KEY(" + COL_EXAM_CourseID + ") REFERENCES " + TABLE_Course + "(" + COL_COURSE_ID + "), " +
			"FOREIGN KEY(" + COL_EXAM_TimePlaceBlockID + ") REFERENCES " + TABLE_TimePlaceBlock + "(" + COL_TIME_PLACE_BLOCK_ID + "))";

	// All table names in the order they should be created
	public static final String[] TABLE_NAMES = new String[] { TABLE_Term, TABLE_Instructor, TABLE_Course,
			TABLE_Location, TABLE_TimePlaceBlock, TABLE_CourseTimeBlock, TABLE_OfficeTimePlaceBlock,
			TABLE_Note, TABLE_Assignment, TABLE_Exam };

	// All table creation strings in the order they should be created
	public static final String[] TABLE_CREATES = new String[] { TABLE_CREATE_Term, TABLE_CREATE_Instructor, TABLE_CREATE_Course,
			TABLE_CREATE_Location, TABLE_CREATE_TimePlaceBlock, TABLE_CREATE_CourseTimePlaceBlock, TABLE_CREATE_OfficeTimePlaceBlock,
			TABLE_CREATE_Note, TABLE_CREATE_Assignment, TABLE_CREATE_Exam };

	/**
	 * Instantiates a new scheduler database helper.
	 * 
	 * @param context the context
	 * @param factory the factory
	 */
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		Log.i("Test", "Helper created");
	}

	/**
	 * Creates the database
	 * 
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.i("Test", "DB Created");
		for (int i = 0; i < TABLE_NAMES.length; ++i) {
			Log.i("Test", "Adding table: " + TABLE_NAMES[i]);
			db.execSQL("CREATE TABLE " + TABLE_NAMES[i] + " " + TABLE_CREATES[i]);
			Log.i("Test", "Added table: " + TABLE_NAMES[i]);
		}
		//db.execSQL("CREATE TABLE Term (_id INTEGER);");
	}

	/**
	 * Drops all tables and recreates the database.
	 * 
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i("Test", "Upgraded");
		for (String tableName : TABLE_NAMES) {
			db.execSQL("drop table " + tableName + ";");
		}
		onCreate(db);
	}
	
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i("Test", "Downgraded");
		for (String tableName : TABLE_NAMES) {
			db.execSQL("drop table " + tableName + ";");
		}
		onCreate(db);
	}
}
