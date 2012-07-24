package com.selagroup.schedu;

import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.selagroup.schedu.database.DBHelper;
import com.selagroup.schedu.managers.*;
import com.selagroup.schedu.model.*;
import com.selagroup.schedu.model.note.*;

public class MyDatabaseTest {
	public MyDatabaseTest(Context appContext) {
		DBHelper helper = new DBHelper(appContext);
		
		// Setup managers
		TermManager termManager = new TermManager(helper);
		LocationManager locationManager = new LocationManager(helper);
		TimePlaceBlockManager timePlaceBlockManager = new TimePlaceBlockManager(helper, locationManager);
		InstructorManager instructorManager = new InstructorManager(helper, timePlaceBlockManager);
		CourseManager courseManager = new CourseManager(helper, termManager, instructorManager, timePlaceBlockManager);
		NoteManager noteManager = new NoteManager(helper, courseManager);
		AssignmentManager assignmentManager = new AssignmentManager(helper, courseManager);
		ExamManager examManager = new ExamManager(helper, timePlaceBlockManager, courseManager);
		
		// New term
		Term term = new Term(-1, Calendar.getInstance(), Calendar.getInstance());
		
		// New locations for classes/offices/exams
		Location classLoc1 = new Location(-1, "Building 1", "Room 1", "Class location 1");
		Location classLoc2 = new Location(-1, "Building 2", "Room 2", "Class location 2");
		Location officeLoc1 = new Location(-1, "Building 1", "Room 3", "Office location 1");
		Location officeLoc2 = new Location(-1, "Building 2", "Room 4", "Office location 2");
		Location examLoc1 = new Location(-1, "Building 1", "The dungeon", "Exam room location");
		
		// New TimePlaceBlocks for classes/offices/exams
		boolean[] dayFlag = new boolean[] { false, true, false, true, false, false, false}; // Mon, Wed
		TimePlaceBlock classBlock1 = new TimePlaceBlock(-1, classLoc1, Calendar.getInstance(), Calendar.getInstance(), dayFlag);
		TimePlaceBlock classBlock2 = new TimePlaceBlock(-1, classLoc2, Calendar.getInstance(), Calendar.getInstance(), dayFlag);
		TimePlaceBlock officeBlock1 = new TimePlaceBlock(-1, officeLoc1, Calendar.getInstance(), Calendar.getInstance(), dayFlag);
		TimePlaceBlock officeBlock2 = new TimePlaceBlock(-1, officeLoc2, Calendar.getInstance(), Calendar.getInstance(), dayFlag);
		TimePlaceBlock examBlock1 = new TimePlaceBlock(-1, examLoc1, Calendar.getInstance(), Calendar.getInstance(), dayFlag);
		
		// New instructors
		Instructor instructor1 = new Instructor(-1, "Mr. Chemistry", "123-456-7890", "chem@uw.edu");
		instructor1.addOfficeBlock(officeBlock1);
		Instructor instructor2 = new Instructor(-1, "Mr. Biology", "123-456-9999", "bio@uw.edu");
		instructor2.addOfficeBlock(officeBlock2);
		
		// New courses, add blocks
		Course chem101 = new Course(-1, term, "Chem 101", "Introductory Chemistry", instructor1);
		chem101.addScheduleBlock(classBlock1);
		Course bio101 = new Course(-1, term, "Bio 101", "Introductory Biology", instructor2);
		bio101.addScheduleBlock(classBlock2);
		
		// New notes
		Note note1 = new TextNote(-1, chem101, "Chem intro");
		Note note2 = new TextNote(-1, bio101, "Bio intro");
		
		// New assignments
		Assignment assignment1 = new Assignment(-1, "Read chapter 1", Calendar.getInstance(), chem101);
		
		// New exams
		Exam exam1 = new Exam(-1, "Quiz 1", bio101, examBlock1);
		
		// Insert data
		Log.i("Test", "Finished constructors, now doing inserts...");
		Log.i("Test", "Inserting term.");
		termManager.insert(term);
		Log.i("Test", "Inserting bio 101 course.");
		courseManager.insert(bio101);
		Log.i("Test", "Inserting chem 101 course.");
		courseManager.insert(chem101);
		Log.i("Test", "Inserting note1.");
		noteManager.insert(note1);
		Log.i("Test", "Inserting note2.");
		noteManager.insert(note2);
		Log.i("Test", "Inserting assignment1.");
		assignmentManager.insert(assignment1);
		Log.i("Test", "Inserting exam1.");
		examManager.insert(exam1);
		
		Log.i("Test", "Getting all courses for Wed");
		List<Course> courses = courseManager.getAllForTermAndDay(term.getID(), TimePlaceBlock.DAY_MASKS[3]);
		
		for (Course course : courses) {
			Log.i("Test", "Course: " + course.getCourseName() + " is on Wed.");
		}
	}
}
