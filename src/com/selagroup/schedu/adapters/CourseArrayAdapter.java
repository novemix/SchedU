package com.selagroup.schedu.adapters;

import java.util.Calendar;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
	private static AlertDialog.Builder mDeleteAlert;
	private AlertDialog mDeleteAlertDialog;

	public interface CourseDeleteListener {
		public void onDelete(Course iCourse);
	}

	private CourseDeleteListener mDeleteListener;

	private Context mContext;
	private List<Course> mCourses;

	public CourseArrayAdapter(Context iContext, int textViewResourceId, List<Course> iCourses, CourseDeleteListener iDeleteListener) {
		super(iContext, textViewResourceId, iCourses);
		mContext = iContext;
		mCourses = iCourses;
		mDeleteListener = iDeleteListener;

		mDeleteAlert = new AlertDialog.Builder(mContext);
		mDeleteAlert.setTitle("Warning!");
		mDeleteAlert.setMessage("You will lose all data associated with this course! Are you sure you want to delete this course?");
		mDeleteAlert.setNegativeButton("Cancel", new AlertDialog.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				mDeleteAlertDialog.dismiss();
			}
		});
	}

	private static class ViewHolder {
		private TextView course_adapter_tv_code;
		private TextView course_adapter_tv_name;
		private Button course_adapter_btn_delete;
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
			holder.course_adapter_btn_delete = (Button) row.findViewById(R.id.course_adapter_btn_delete);
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
			holderFinalRef.course_adapter_btn_delete.setVisibility(View.VISIBLE);
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

			holderFinalRef.course_adapter_btn_delete.setVisibility(View.VISIBLE);
			holderFinalRef.course_adapter_btn_delete.setOnClickListener(new OnClickListener() {
				public void onClick(View view) {
					mDeleteAlert.setPositiveButton("Delete", new AlertDialog.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							if (mDeleteListener != null) {
								mDeleteListener.onDelete(course);
							}
						}
					});
					mDeleteAlertDialog = mDeleteAlert.create();
					mDeleteAlertDialog.show();
				}
			});
		} else {
			holderFinalRef.course_adapter_btn_edit.setVisibility(View.GONE);
			holderFinalRef.course_adapter_btn_delete.setVisibility(View.GONE);
			holderFinalRef.course_adapter_tv_code.setText("No courses for this term.");
			holderFinalRef.course_adapter_tv_name.setText("Please add a course.");
		}

		row.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				Intent showCourseIntent = new Intent(mContext, CourseActivity.class);

				// TODO: need to get the next time/day for this course
				Calendar day = null;
				TimePlaceBlock nextBlock = course.getScheduleBlocks().get(0);

				showCourseIntent.putExtra("courseID", course.getID());
				showCourseIntent.putExtra("blockID", nextBlock.getID());
				showCourseIntent.putExtra("day", Calendar.getInstance());
				mContext.startActivity(showCourseIntent);
			}
		});

		return row;
	}
}
