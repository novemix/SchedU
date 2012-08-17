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
import com.selagroup.schedu.model.Term;

import android.app.Application;

public class ScheduApplication extends Application {
	
	// Database helper
	private DBHelper mHelper;
	
	// Managers for content stored in the database
	private TermManager mTermManager;
	private LocationManager mLocationManager;					// Keep private, operations handled by other managers
	private TimePlaceBlockManager mTimePlaceBlockManager;
	private InstructorManager mInstructorManager;
	private CourseManager mCourseManager;
	private NoteManager mNoteManager;
	private AssignmentManager mAssignmentManager;
	private ExamManager mExamManager;

	// Alarm system
	private AlarmSystem mAlarmSystem;
	
	// The current term
	private Term mCurrentTerm;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		// Create database helper
		mHelper = new DBHelper(getApplicationContext());

		// Create managers to manage database access
		mTermManager = new TermManager(mHelper);
		mLocationManager = new LocationManager(mHelper);
		mTimePlaceBlockManager = new TimePlaceBlockManager(mHelper, mLocationManager);
		mInstructorManager = new InstructorManager(mHelper, mLocationManager, mTimePlaceBlockManager);
		mCourseManager = new CourseManager(mHelper, mTermManager, mInstructorManager, mTimePlaceBlockManager);
		mTermManager.setCourseManager(mCourseManager);
		mNoteManager = new NoteManager(mHelper, mCourseManager);
		mAssignmentManager = new AssignmentManager(mHelper, mCourseManager);
		mExamManager = new ExamManager(mHelper, mTimePlaceBlockManager, mCourseManager);
		
		// Create alarm manager
		mAlarmSystem = new AlarmSystem(getApplicationContext());
	}
	
	// Getters for database access managers
	public TermManager getTermManager() {
		return mTermManager;
	}
	
	public InstructorManager getInstructorManager() {
		return mInstructorManager;
	}
	
	public CourseManager getCourseManager() {
		return mCourseManager;
	}
	
	public TimePlaceBlockManager getTimePlaceBlockManager() {
		return mTimePlaceBlockManager;
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
	
	// Alarm system
	public AlarmSystem getAlarmSystem() {
		return mAlarmSystem;
	}
	
	// Term get/set
	public Term getCurrentTerm() {
		return mCurrentTerm;
	}
	
	public void setCurrentTerm(Term iTerm) {
		mCurrentTerm = iTerm;
	}
}
