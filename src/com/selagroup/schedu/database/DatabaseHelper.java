/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */
package com.selagroup.schedu.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * The Class DatabaseHelper
 */
public class DatabaseHelper extends SQLiteOpenHelper {

	// Database name and version
	public static final String DATABASE_NAME = "SchedulerDatabase";
	public static final int DATABASE_VERSION = 1;

	// Tables
	// Term table
	public static final String TABLE_Term = "Term";
	public static final String COL_TERM_ID = "ID"; 							// Primary key
	public static final String COL_TERM_StartDate = "StartDate";
	public static final String COL_TERM_EndDate = "EndDate";
	
	// Instructor table
	public static final String TABLE_Instructor = "Instructor";
	public static final String COL_INSTRUCTOR_ID = "ID";					// Primary key
	public static final String COL_INSTRUCTOR_Name = "Name";
	public static final String COL_INSTRUCTOR_Email = "Email";
	public static final String COL_INSTRUCTOR_Phone = "Phone";

	// Class table
	public static final String TABLE_Class = "Class";
	public static final String COL_CLASS_ID = "ID"; 						// Primary key
	public static final String COL_CLASS_CourseCode = "CourseCode";
	public static final String COL_CLASS_CourseName = "CourseName";
	public static final String COL_CLASS_IsExam = "IsExam";
	public static final String COL_CLASS_InstructorID = "Instructor_ID";	// Foreign key: Instructor.ID

	// ClassesToTerm table (connecting table)
	public static final String TABLE_ClassToTerm = "ClassToTerm";
	public static final String COL_CLASS_TO_TERM_TermID = "Term_ID";		// Foreign key: Term.ID
	public static final String COL_CLASS_TO_TERM_ClassID = "Class_ID";		// Foreign key: Class.ID

	// Location table
	public static final String TABLE_Location = "Location";
	public static final String COL_LOCATION_ID = "ID";						// Primary key
	public static final String COL_LOCATION_Building = "Building";
	public static final String COL_LOCATION_Room = "Room";
	public static final String COL_LOCATION_GeoLocation = "GeoLocation";

	// TimePlaceBlock table
	public static final String TABLE_TimePlaceBlock = "TimePlaceBlock";
	public static final String COL_TIME_PLACE_BLOCK_ID = "ID";						// Primary key
	public static final String COL_TIME_PLACE_BLOCK_DayFlags = "DayFlags";
	public static final String COL_TIME_PLACE_BLOCK_StartTime = "StartTime";
	public static final String COL_TIME_PLACE_BLOCK_EndTime = "EndTime";
	public static final String COL_TIME_PLACE_BLOCK_LocationID = "Location_ID";		// Foreign key: Location.ID

	// ClassTimePlaceBlocks table (connecting table)
	public static final String TABLE_ClassTimeBlock = "ClassTimePlaceBlock";
	public static final String COL_CLASS_TIME_PLACE_BLOCK_TimePlaceBlockID = "TimePlaceBlock_ID";	// Foreign key: TimePlaceBlock.ID
	public static final String COL_CLASS_TIME_PLACE_BLOCK_ClassID = "Class_ID";						// Foreign key: Class.ID

	// OfficeTimePlaceBlocks table (connecting table)
	public static final String TABLE_OfficeTimePlaceBlock = "OfficeTimePlaceBlock";
	public static final String COL_OFFICE_TIME_PLACE_BLOCK_TimePlaceBlockID = "TimePlaceBlock_ID";	// Foreign key: TimePlaceBlock.ID
	public static final String COL_OFFICE_TIME_PLACE_BLOCK_InstructorID = "Instructor_ID";			// Foreign key: Instructor.ID

	// NoteType table
	public static final String TABLE_NoteType = "NoteType";
	public static final String COL_NOTE_TYPE_ID = "ID";						// Primary key
	public static final String COL_NOTE_TYPE_Name = "Name";
	
	// Note table
	public static final String TABLE_Note = "Note";
	public static final String COL_NOTE_ID = "ID";							// Primary key
	public static final String COL_NOTE_NoteTypeID = "NoteType_ID";			// Foreign key: NoteType.ID
	public static final String COL_NOTE_Title = "Title";
	public static final String COL_NOTE_FilePath = "FilePath";
	public static final String COL_NOTE_CreationDate = "CreationDate";

	// ClassNotes table (connecting table)
	public static final String TABLE_ClassNote = "ClassNote";
	public static final String COL_CLASS_NOTE_ClassID = "Class_ID";			// Foreign key: Class.ID
	public static final String COL_CLASS_NOTE_NoteID = "Note_ID";			// Foreign key: Note.ID

	// Assignment table
	public static final String TABLE_Assignment = "Assignment";
	public static final String COL_ASSIGNMENT_ID = "ID";					// Primary key
	public static final String COL_ASSIGNMENT_Name = "Name";
	public static final String COL_ASSIGNMENT_DueDate = "DueDate";

	// ClassAssignments table (connecting table)
	public static final String TABLE_CLASS_ASSIGNMENT = "ClassAssignment";
	public static final String COL_CLASS_ASSIGNMENT_AssignmentID = "Assignment_ID";		// Foreign key: Assignment.ID
	public static final String COL_CLASS_ASSIGNMENT_ClassID = "Class_ID";				// Foreign key: Class.ID
	
	// Table creation statements
	public static final String TABLE_CREATE_Term = "(" +
			COL_TERM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			COL_TERM_StartDate + " INTEGER, " +
			COL_TERM_EndDate + " INTEGER);";

	public static final String TABLE_CREATE_Class = "(" +
			COL_CLASS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			COL_CLASS_CourseCode + " INTEGER, " +
			COL_CLASS_CourseName + " TEXT, " +
			COL_CLASS_IsExam + " INTEGER, " +
			COL_CLASS_InstructorID + " INTEGER, " +
			"FOREIGN KEY(" + COL_CLASS_InstructorID + ") REFERENCES " + TABLE_Instructor + "(" + COL_INSTRUCTOR_ID + ");";

	public static final String TABLE_CREATE_ClassToTerm = "(" +
			COL_CLASS_TO_TERM_ClassID + " INTEGER, " +
			COL_CLASS_TO_TERM_TermID + " INTEGER, " +
			"FOREIGN KEY(" + COL_CLASS_TO_TERM_ClassID + ") REFERENCES " + TABLE_Class + "(" + COL_CLASS_ID + ")," +
			"FOREIGN KEY(" + COL_CLASS_TO_TERM_TermID + ") REFERENCES " + TABLE_Term + "(" + COL_TERM_ID + ");";

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
			COL_TIME_PLACE_BLOCK_DayFlags + " INTEGER, " +
			COL_TIME_PLACE_BLOCK_StartTime + " INTEGER, " +
			COL_TIME_PLACE_BLOCK_EndTime + " INTEGER, " +
			COL_TIME_PLACE_BLOCK_LocationID + " INTEGER, " +
			"FOREIGN KEY(" + COL_TIME_PLACE_BLOCK_LocationID + ") REFERENCES " + TABLE_Location + "(" + COL_LOCATION_ID + ");";

	public static final String TABLE_CREATE_ClassTimePlaceBlock = "(" +
			COL_CLASS_TIME_PLACE_BLOCK_ClassID + " INTEGER, " +
			COL_CLASS_TIME_PLACE_BLOCK_TimePlaceBlockID + " INTEGER, " +
			"FOREIGN KEY(" + COL_CLASS_TIME_PLACE_BLOCK_ClassID + ") REFERENCES " + TABLE_Class + "(" + COL_CLASS_ID + "), " +
			"FOREIGN KEY(" + COL_CLASS_TIME_PLACE_BLOCK_TimePlaceBlockID + ") REFERENCES " + TABLE_TimePlaceBlock + "(" + COL_TIME_PLACE_BLOCK_ID + ");";

	public static final String TABLE_CREATE_OfficeTimePlaceBlock = "(" +
			COL_OFFICE_TIME_PLACE_BLOCK_InstructorID + " INTEGER, " +
			COL_OFFICE_TIME_PLACE_BLOCK_TimePlaceBlockID + " INTEGER, " +
			"FOREIGN KEY(" + COL_OFFICE_TIME_PLACE_BLOCK_InstructorID + ") REFERENCES " + TABLE_Instructor + "(" + COL_INSTRUCTOR_ID + "), " +
			"FOREIGN KEY(" + COL_OFFICE_TIME_PLACE_BLOCK_TimePlaceBlockID + ") REFERENCES " + TABLE_TimePlaceBlock + "(" + COL_TIME_PLACE_BLOCK_ID + ");";
	
	public static final String TABLE_CREATE_Note = "(" +
			COL_NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + 
			COL_NOTE_Title + " TEXT, " + 
			COL_NOTE_NoteTypeID + " INTEGER, " + 
			COL_NOTE_FilePath + " TEXT, " + 
			"FOREIGN KEY(" + COL_NOTE_NoteTypeID + ") REFERENCES " + TABLE_NoteType + "(" + COL_NOTE_TYPE_ID + ");";
	
	public static final String TABLE_CREATE_NoteType = "(" +
			COL_NOTE_NoteTypeID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			COL_NOTE_TYPE_Name + " TEXT);";
	
	public static final String TABLE_CREATE_ClassNote = "(" +
			COL_CLASS_NOTE_ClassID + " INTEGER, " +
			COL_CLASS_NOTE_NoteID + " INTEGER, " + 
			"FOREIGN KEY(" + COL_CLASS_NOTE_ClassID + ") REFERENCES " + TABLE_Class + "(" + COL_CLASS_ID + "), " + 
			"FOREIGN KEY(" + COL_CLASS_NOTE_NoteID + ") REFERENCES " + TABLE_Note + "(" + COL_NOTE_ID + ");";
	
	public static final String TABLE_CREATE_Assignment = "(" +
			COL_ASSIGNMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			COL_ASSIGNMENT_Name + " TEXT, " + 
			COL_ASSIGNMENT_DueDate + " INTEGER;";
	
	public static final String TABLE_CREATE_ClassAssignment = "(" +
			COL_CLASS_ASSIGNMENT_AssignmentID + " INTEGER, " + 
			COL_CLASS_ASSIGNMENT_ClassID + " INTEGER, " + 
			"FOREIGN KEY(" + COL_CLASS_ASSIGNMENT_AssignmentID + ") REFERENCES " + TABLE_Assignment + "(" + COL_ASSIGNMENT_ID + "), " + 
			"FOREIGN KEY(" + COL_CLASS_ASSIGNMENT_ClassID + ") REFERENCES " + TABLE_Class + "(" + COL_CLASS_ID + ");";
	
	// All table names in the order they should be created
	public static final String[] TABLE_NAMES = new String[] { TABLE_Term, TABLE_Instructor, TABLE_Class, TABLE_ClassToTerm, 
			TABLE_Location, TABLE_TimePlaceBlock, TABLE_ClassTimeBlock, TABLE_OfficeTimePlaceBlock, TABLE_NoteType,
			TABLE_Note, TABLE_ClassNote, TABLE_Assignment, TABLE_CLASS_ASSIGNMENT };
	
	// All table creation strings in the order they should be created
	public static final String[] TABLE_CREATES = new String[] { TABLE_CREATE_Term, TABLE_CREATE_Instructor, TABLE_CREATE_Class, TABLE_CREATE_ClassToTerm, 
		TABLE_CREATE_Location, TABLE_CREATE_TimePlaceBlock, TABLE_CREATE_ClassTimePlaceBlock, TABLE_CREATE_OfficeTimePlaceBlock, TABLE_CREATE_NoteType,
		TABLE_CREATE_Note, TABLE_CREATE_ClassNote, TABLE_CREATE_Assignment, TABLE_CREATE_ClassAssignment };

	/**
	 * Instantiates a new scheduler database helper.
	 * 
	 * @param context the context
	 * @param factory the factory
	 */
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/**
	 * Creates the database
	 * 
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		for (int i = 0; i < TABLE_NAMES.length; ++i) {
			db.execSQL("create table " + TABLE_NAMES[i] + " " + TABLE_CREATES[i]);
		}
	}

	/**
	 * Drops all tables and recreates the database.
	 * 
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		for (String tableName : TABLE_NAMES) {
			db.execSQL("drop table " + tableName + ";");
		}
		onCreate(db);
	}
}
