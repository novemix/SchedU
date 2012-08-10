package com.selagroup.schedu.adapters;

import java.util.List;

import com.selagroup.schedu.R;
import com.selagroup.schedu.model.Course;
import com.selagroup.schedu.model.Term;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

public class CourseArrayAdapter extends ArrayAdapter<Course> {

	private Context mContext;
	private List<Course> mCourses;
	
	public CourseArrayAdapter(Context iContext, int textViewResourceId, List<Course> iCourses) {
		super(iContext, textViewResourceId, iCourses);
		mContext = iContext;
		mCourses = iCourses;
	}
	
	private static class ViewHolder {
		private TextView course_adapter_tv_select;
	}
	
	@Override
	public View getView(int position, View row, ViewGroup parent) {
		ViewHolder tmpHolder = null;

		// Only get the items from the layout once
		if (row == null) {
			LayoutInflater li = LayoutInflater.from(mContext);
			row = li.inflate(R.layout.adapter_course_select, null);
			tmpHolder = new ViewHolder();
			tmpHolder.course_adapter_tv_select = (TextView) row.findViewById(R.id.course_adapter_tv_select);
			row.setTag(tmpHolder);
		} else {
			tmpHolder = (ViewHolder) row.getTag();
		}
		final Course course = mCourses.get(position);
		final ViewHolder holder = tmpHolder;
		holder.course_adapter_tv_select.setText(course.toString());
		
		return row;
	}
}
