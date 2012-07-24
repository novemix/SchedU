package com.selagroup.schedu;

import com.selagroup.schedu.database.DBHelper;
import com.selagroup.schedu.managers.AssignmentManager;
import com.selagroup.schedu.managers.CourseManager;
import com.selagroup.schedu.managers.ExamManager;
import com.selagroup.schedu.managers.InstructorManager;
import com.selagroup.schedu.managers.LocationManager;
import com.selagroup.schedu.managers.NoteManager;
import com.selagroup.schedu.managers.TermManager;
import com.selagroup.schedu.managers.TimePlaceBlockManager;

import android.app.Application;

public class MyApplication extends Application {
	private DBHelper mHelper;
	private TermManager mTermManager;
	private LocationManager mLocationManager;					// Keep private, operations handled by other managers
	private TimePlaceBlockManager mTimePlaceBlockManager;		// Keep private, operations handled by other managers
	private InstructorManager mInstructorManager;
	private CourseManager mCourseManager;
	private NoteManager mNoteManager;
	private AssignmentManager mAssignmentManager;
	private ExamManager mExamManager;

	
	@Override
	public void onCreate() {
		super.onCreate();
		
		// Create database helper
		mHelper = new DBHelper(getApplicationContext());

		// Create managers to manage database access
		mTermManager = new TermManager(mHelper);
		mLocationManager = new LocationManager(mHelper);
		mTimePlaceBlockManager = new TimePlaceBlockManager(mHelper, mLocationManager);
		mInstructorManager = new InstructorManager(mHelper, mTimePlaceBlockManager);
		mCourseManager = new CourseManager(mHelper, mTermManager, mInstructorManager, mTimePlaceBlockManager);
		mNoteManager = new NoteManager(mHelper, mCourseManager);
		mAssignmentManager = new AssignmentManager(mHelper, mCourseManager);
		mExamManager = new ExamManager(mHelper, mTimePlaceBlockManager, mCourseManager);
	}
	
	public TermManager getTermManager() {
		return mTermManager;
	}
	
	public InstructorManager getInstructorManager() {
		return mInstructorManager;
	}
	
	public CourseManager getCourseManager() {
		return mCourseManager;
	}
	
	public NoteManager getNoteManager() {
		return mNoteManager;
	}
	
	public AssignmentManager getAssignmentManager() {
		return mAssignmentManager;
	}
	
	public ExamManager getExamManager() {
		return mExamManager;
	}
}
