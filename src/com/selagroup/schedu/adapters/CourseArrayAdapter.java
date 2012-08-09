package com.selagroup.schedu.adapters;

import java.util.List;

import com.selagroup.schedu.model.Course;

import android.content.Context;
import android.widget.ArrayAdapter;

public class CourseArrayAdapter extends ArrayAdapter<Course> {

	public CourseArrayAdapter(Context iContext, int textViewResourceId, List<Course> iCourses) {
		super(iContext, textViewResourceId, iCourses);
		
	}

}
