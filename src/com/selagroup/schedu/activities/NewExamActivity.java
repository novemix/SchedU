/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */
package com.selagroup.schedu.activities;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.selagroup.schedu.R;
import com.selagroup.schedu.ScheduApplication;
import com.selagroup.schedu.managers.ExamManager;
import com.selagroup.schedu.model.Course;
import com.selagroup.schedu.model.Exam;
import com.selagroup.schedu.model.Location;
import com.selagroup.schedu.model.TimePlaceBlock;

/**
 * The Class NewExamActivity.
 */
public class NewExamActivity extends Activity {
	
	ExamManager mExamManager;
	Course mCourse;
	
	EditText new_exam_et_desc;
	EditText new_exam_et_bldg;
	EditText new_exam_et_room;
	Button new_exam_btn_date;
	Button new_exam_btn_start_time;
	Button new_exam_btn_end_time;
	Button new_exam_btn_done;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_exam);
		
		initActivity();
		
		initWidgets();
		initListeners();
	}

	private void initActivity() {
		mCourse = ((ScheduApplication) getApplication()).getCourseManager().get(getIntent().getIntExtra("courseID", -1));
		((TextView) findViewById(R.id.new_exam_tv_course_code)).setText(mCourse.getCode());
		
		mExamManager = ((ScheduApplication) getApplication()).getExamManager();
	}
	
	private void initWidgets() {
		new_exam_et_desc = (EditText) findViewById(R.id.new_exam_et_desc);
		new_exam_et_bldg = (EditText) findViewById(R.id.new_exam_et_building);
		new_exam_et_room = (EditText) findViewById(R.id.new_exam_et_room);
		new_exam_btn_date = (Button) findViewById(R.id.new_exam_btn_date);
		new_exam_btn_start_time = (Button) findViewById(R.id.new_exam_btn_start_time);
		new_exam_btn_end_time = (Button) findViewById(R.id.new_exam_btn_end_time);
		new_exam_btn_done = (Button) findViewById(R.id.new_exam_btn_done);
	}
	
	private void initListeners() {
		new_exam_btn_date.setOnClickListener(btnListener);
		new_exam_btn_start_time.setOnClickListener(btnListener);
		new_exam_btn_end_time.setOnClickListener(btnListener);
		new_exam_btn_done.setOnClickListener(btnListener);
		((Button) findViewById(R.id.new_exam_btn_cancel)).setOnClickListener(btnListener);
	}
	
	private OnClickListener btnListener = new OnClickListener() {
		
		final Calendar examDate = Calendar.getInstance();
		final Time start = new Time();
		final Time end = new Time();
		
		public void onClick(View v) {
			switch(v.getId()) {
			case R.id.new_exam_btn_date:
				(new DatePickerDialog(NewExamActivity.this, new OnDateSetListener() {
					public void onDateSet(DatePicker view, int year, int month, int day) {
						examDate.set(year, month, day);
						new_exam_btn_date.setText((new SimpleDateFormat("EEE, MMM dd, yyyy")).format(examDate.getTime()));
						new_exam_btn_date.setTag(examDate);
					}
				}, examDate.get(Calendar.YEAR), examDate.get(Calendar.MONTH), examDate.get(Calendar.DAY_OF_MONTH))).show();
				break;
			case R.id.new_exam_btn_start_time:
				(new TimePickerDialog(NewExamActivity.this, new OnTimeSetListener() {
					public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
						start.set(0, minute, hourOfDay, start.monthDay, start.month, start.year);
						new_exam_btn_start_time.setText(start.format("%I:%M %P"));
						new_exam_btn_start_time.setTag(start);
					}}, start.hour, start.minute, false)).show();
				break;
			case R.id.new_exam_btn_end_time:
				(new TimePickerDialog(NewExamActivity.this, new OnTimeSetListener() {
					public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
						end.set(0, minute, hourOfDay, end.monthDay, end.month, end.year);
						new_exam_btn_end_time.setText(end.format("%I:%M %P"));
						new_exam_btn_end_time.setTag(end);
					}}, end.hour, end.minute, false)).show();
				break;
			case R.id.new_exam_btn_done:
				addNewExam();
				finish();
				break;
			case R.id.new_exam_btn_cancel:
				finish();
				break;
			}
		}

		
	};
	
	private void addNewExam() {
		Calendar start = ((Calendar) new_exam_btn_date.getTag());
		Calendar end = Calendar.getInstance();
		int year = start.get(Calendar.YEAR);
		int month = start.get(Calendar.MONTH);
		int day = start.get(Calendar.DAY_OF_MONTH);
		
		Time start_time = (Time) new_exam_btn_start_time.getTag();
		Time end_time = (Time) new_exam_btn_end_time.getTag();
		
		start.set(year, month, day, start_time.hour, start_time.minute, 0);
		end.set(year, month, day, end_time.hour, end_time.minute, 0);
		
		Location location = new Location(-1, new_exam_et_bldg.getText().toString(), new_exam_et_room.getText().toString(), null);
		TimePlaceBlock block = new TimePlaceBlock(-1, location, start, end, 0);
		
		Exam exam = new Exam(-1, new_exam_et_desc.getText().toString(), mCourse, block);
		
		mExamManager.insert(exam);
	}
}
