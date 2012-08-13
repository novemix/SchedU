package com.selagroup.schedu.adapters;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.selagroup.schedu.R;
import com.selagroup.schedu.model.Course;

public class CourseArrayAdapter extends ArrayAdapter<Course> {
	private static AlertDialog.Builder mDeleteAlert;
	private AlertDialog mDeleteAlertDialog;

	public interface CourseDeleteListener {
		public void onDelete(Course iCourse);
	}

	private CourseDeleteListener mDeleteListener;

	private Context mContext;
	private List<Course> mCourses;

	private boolean mEditEnabled;

	public CourseArrayAdapter(Context iContext, int textViewResourceId, List<Course> iCourses, CourseDeleteListener iDeleteListener) {
		super(iContext, textViewResourceId, iCourses);
		mContext = iContext;
		mCourses = iCourses;
		mDeleteListener = iDeleteListener;
		mDeleteAlert = new AlertDialog.Builder(mContext);
	}

	private static class ViewHolder {
		private TextView course_adapter_tv_code;
		private TextView course_adapter_tv_name;
		private Button course_adapter_btn_delete;
	}

	public void setEditEnabled(boolean iEditEnabled) {
		mEditEnabled = iEditEnabled;
	}

	@Override
	public View getView(int position, View row, ViewGroup parent) {
		ViewHolder tmpHolder = null;

		// Only get the items from the layout once
		if (row == null) {
			LayoutInflater li = LayoutInflater.from(mContext);
			row = li.inflate(R.layout.adapter_course_select, null);
			tmpHolder = new ViewHolder();
			tmpHolder.course_adapter_tv_code = (TextView) row.findViewById(R.id.course_adapter_tv_code);
			tmpHolder.course_adapter_tv_name = (TextView) row.findViewById(R.id.course_adapter_tv_name);
			tmpHolder.course_adapter_btn_delete = (Button) row.findViewById(R.id.course_adapter_btn_delete);
			row.setTag(tmpHolder);
		} else {
			tmpHolder = (ViewHolder) row.getTag();
		}
		final Course course = mCourses.get(position);
		final ViewHolder holder = tmpHolder;
		row.setBackgroundResource(R.drawable.term_block);

		if (course != null) {
			holder.course_adapter_tv_code.setText(course.getCode());
			holder.course_adapter_tv_name.setText(course.getName());

			// If edit enabled, show delete button
			if (mEditEnabled) {
				holder.course_adapter_btn_delete.setVisibility(View.VISIBLE);
				holder.course_adapter_btn_delete.setOnClickListener(new OnClickListener() {
					public void onClick(View view) {
						mDeleteAlert.setTitle("Warning!");
						mDeleteAlert.setMessage("You will lose all data associated with this course! Are you sure you want to delete this course?");
						mDeleteAlert.setPositiveButton("Delete", new AlertDialog.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								if (mDeleteListener != null) {
									mDeleteListener.onDelete(course);
								}
							}
						});
						mDeleteAlert.setNegativeButton("Cancel", new AlertDialog.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								mDeleteAlertDialog.dismiss();
							}
						});
						mDeleteAlertDialog = mDeleteAlert.create();
						mDeleteAlertDialog.show();
					}
				});
			}
			else {
				holder.course_adapter_btn_delete.setVisibility(View.GONE);
			}
		}
		else {
			holder.course_adapter_tv_code.setText("No courses for this term.");
			holder.course_adapter_tv_name.setText("Please add another course.");
		}

		return row;
	}
}
