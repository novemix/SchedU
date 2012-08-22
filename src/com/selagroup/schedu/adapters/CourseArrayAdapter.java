package com.selagroup.schedu.adapters;

import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.selagroup.schedu.R;
import com.selagroup.schedu.activities.AddCourseActivity;
import com.selagroup.schedu.activities.CourseActivity;
import com.selagroup.schedu.model.Course;
import com.selagroup.schedu.model.TimePlaceBlock;

public class CourseArrayAdapter extends ArrayAdapter<Course> {

	private Context mContext;
	private List<Course> mCourses;

	public CourseArrayAdapter(Context iContext, int textViewResourceId, List<Course> iCourses) {
		super(iContext, textViewResourceId, iCourses);
		mContext = iContext;
		mCourses = iCourses;
	}

	private static class ViewHolder {
		private TextView course_adapter_tv_code;
		private TextView course_adapter_tv_name;
		private Button course_adapter_btn_edit;
	}

	@Override
	public View getView(int position, View row, ViewGroup parent) {
		ViewHolder holder = null;

		// Only get the items from the layout once
		if (row == null) {
			LayoutInflater li = LayoutInflater.from(mContext);
			row = li.inflate(R.layout.adapter_course_select, null);
			holder = new ViewHolder();
			holder.course_adapter_tv_code = (TextView) row.findViewById(R.id.course_adapter_tv_code);
			holder.course_adapter_tv_name = (TextView) row.findViewById(R.id.course_adapter_tv_name);
			holder.course_adapter_btn_edit = (Button) row.findViewById(R.id.course_adapter_btn_edit);
			holder.course_adapter_btn_edit.setFocusable(false);
			holder.course_adapter_btn_edit.setFocusableInTouchMode(false);
			row.setTag(holder);
		} else {
			holder = (ViewHolder) row.getTag();
		}
		final Course course = mCourses.get(position);
		final ViewHolder holderFinalRef = holder;
		row.setBackgroundResource(R.drawable.list_block_selector);

		holderFinalRef.course_adapter_tv_code.setVisibility(View.VISIBLE);
		holderFinalRef.course_adapter_tv_name.setVisibility(View.VISIBLE);
		if (course != null) {
			holderFinalRef.course_adapter_btn_edit.setVisibility(View.VISIBLE);
			holderFinalRef.course_adapter_tv_code.setText(course.getCode());
			holderFinalRef.course_adapter_tv_name.setText(course.getName());

			holderFinalRef.course_adapter_btn_edit.setOnClickListener(new OnClickListener() {
				public void onClick(View view) {
					Intent editCourseIntent = new Intent(mContext, AddCourseActivity.class);
					editCourseIntent.putExtra("edit", true);
					editCourseIntent.putExtra("course", course);
					mContext.startActivity(editCourseIntent);
				}
			});
		} else {
			holderFinalRef.course_adapter_btn_edit.setVisibility(View.GONE);
			holderFinalRef.course_adapter_tv_code.setText("No courses for this term.");
			holderFinalRef.course_adapter_tv_name.setText("Please add a course.");
		}

		row.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				if (course != null) {
					Intent showCourseIntent = new Intent(mContext, CourseActivity.class);

					Calendar day = Calendar.getInstance();
					Pair<TimePlaceBlock, Calendar> nextBlockPair = course.getNextBlock(day);

					if (nextBlockPair == null) {
						showCourseIntent.putExtra("courseID", course.getID());
						showCourseIntent.putExtra("blockID", -1);
						showCourseIntent.putExtra("day", Calendar.getInstance());
					} else {
						showCourseIntent.putExtra("courseID", course.getID());
						showCourseIntent.putExtra("blockID", nextBlockPair.first.getID());
						showCourseIntent.putExtra("day", nextBlockPair.second);
					}
					mContext.startActivity(showCourseIntent);
				} else {
					mContext.startActivity(new Intent(mContext, AddCourseActivity.class));
				}
			}
		});

		return row;
	}
}
