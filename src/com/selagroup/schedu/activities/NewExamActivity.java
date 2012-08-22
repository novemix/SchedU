/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */
package com.selagroup.schedu.activities;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
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
	
	private Boolean editExam = false;
	private ExamManager mExamManager;
	private Course mCourse;
	private Exam exam;
	
	private EditText new_exam_et_desc;
	private EditText new_exam_et_bldg;
	private EditText new_exam_et_room;
	private Button new_exam_btn_date;
	private Button new_exam_btn_start_time;
	private Button new_exam_btn_end_time;
	private Button new_exam_btn_done;
	private ImageButton new_exam_btn_delete;
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM dd, yyyy");
	private SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
	
	private Calendar start;
	private Calendar end;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_exam);
		
		initActivity();
		
		initWidgets();
		setValues();
		initListeners();
	}
	
	@Override
	public void onBackPressed() {
		cancel();
	}
	
	private void initActivity() {
		mExamManager = ((ScheduApplication) getApplication()).getExamManager();
		
		Intent intent = getIntent();
		int courseID = intent.getIntExtra("courseID", -1);
		if (courseID != -1) {
			mCourse = ((ScheduApplication) getApplication()).getCourseManager().get(courseID);
		} else {
			setTitle(R.string.new_exam_edit_title);
			editExam = true;
			exam = mExamManager.get(intent.getIntExtra("examID", -1));
			mCourse = exam.getCourse();
		}
		
		((TextView) findViewById(R.id.new_exam_tv_course_code)).setText(mCourse.getCode());
	}
	
	private void initWidgets() {
		new_exam_et_desc = (EditText) findViewById(R.id.new_exam_et_desc);
		new_exam_et_bldg = (EditText) findViewById(R.id.new_exam_et_building);
		new_exam_et_room = (EditText) findViewById(R.id.new_exam_et_room);
		new_exam_btn_date = (Button) findViewById(R.id.new_exam_btn_date);
		new_exam_btn_start_time = (Button) findViewById(R.id.new_exam_btn_start_time);
		new_exam_btn_end_time = (Button) findViewById(R.id.new_exam_btn_end_time);
		new_exam_btn_done = (Button) findViewById(R.id.new_exam_btn_done);
		if (editExam) {
			new_exam_btn_delete = (ImageButton) findViewById(R.id.new_exam_btn_delete);
			new_exam_btn_delete.setVisibility(View.VISIBLE);
		}
	}
	
	private void setValues() {
		TimePlaceBlock block = (editExam ? exam.getBlock() : mCourse.getScheduleBlocks().get(0));
		Location location = block.getLocation();
		new_exam_et_bldg.setText(location.getBuilding());
		new_exam_et_room.setText(location.getRoom());
		if (editExam) {
			new_exam_et_desc.setText(exam.getDescription());
			start = block.getStartTime();
			end = block.getEndTime();
			new_exam_btn_date.setText(dateFormat.format(start.getTime()));
			new_exam_btn_date.setTag(start);
			new_exam_btn_start_time.setText(timeFormat.format(start.getTime()).toLowerCase());
			new_exam_btn_start_time.setTag(start);
			new_exam_btn_end_time.setText(timeFormat.format(end.getTime()).toLowerCase());
			new_exam_btn_end_time.setTag(end);
		} else {
			start = Calendar.getInstance();
			end = Calendar.getInstance();
		}
	}
	
	private void initListeners() {
		new_exam_btn_date.setOnClickListener(btnListener);
		new_exam_btn_start_time.setOnClickListener(btnListener);
		new_exam_btn_end_time.setOnClickListener(btnListener);
		new_exam_btn_done.setOnClickListener(btnListener);
		((Button) findViewById(R.id.new_exam_btn_cancel)).setOnClickListener(btnListener);
		if (editExam) {
			new_exam_btn_delete.setOnClickListener(btnListener);
		}
	}
	
	private boolean validate() {
		return ( ! "".equals(new_exam_et_desc.getText().toString())
				&& new_exam_btn_date.getTag() != null
				&& new_exam_btn_start_time.getTag() != null
				&& new_exam_btn_end_time.getTag() != null
				&& start.compareTo(end) == -1);
	}
	
	private OnClickListener btnListener = new OnClickListener() {
		
		public void onClick(View v) {
			switch(v.getId()) {
			case R.id.new_exam_btn_date:
				(new DatePickerDialog(NewExamActivity.this, new OnDateSetListener() {
					public void onDateSet(DatePicker view, int year, int month, int day) {
						start.set(year, month, day);
						end.set(year, month, day);
						new_exam_btn_date.setText(dateFormat.format(start.getTime()));
						new_exam_btn_date.setTag(start);
					}
				}, start.get(Calendar.YEAR), start.get(Calendar.MONTH), start.get(Calendar.DAY_OF_MONTH))).show();
				break;
			case R.id.new_exam_btn_start_time:
				(new TimePickerDialog(NewExamActivity.this, new OnTimeSetListener() {
					public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
						start.set(start.get(Calendar.YEAR), start.get(Calendar.MONTH), start.get(Calendar.DAY_OF_MONTH)
								, hourOfDay, minute, 0);
						new_exam_btn_start_time.setText(timeFormat.format(start.getTime()));
						new_exam_btn_start_time.setTag(start);
					}}, start.get(Calendar.HOUR_OF_DAY), start.get(Calendar.MINUTE), false)).show();
				break;
			case R.id.new_exam_btn_end_time:
				(new TimePickerDialog(NewExamActivity.this, new OnTimeSetListener() {
					public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
						end.set(end.get(Calendar.YEAR), end.get(Calendar.MONTH), end.get(Calendar.DAY_OF_MONTH)
								, hourOfDay, minute, 0);
						new_exam_btn_end_time.setText(timeFormat.format(end.getTime()));
						new_exam_btn_end_time.setTag(end);
					}}, end.get(Calendar.HOUR_OF_DAY), end.get(Calendar.MINUTE), false)).show();
				break;
			case R.id.new_exam_btn_delete:
				(new AlertDialog.Builder(NewExamActivity.this))
				.setMessage(R.string.new_exam_dialog_delete_text)
				.setPositiveButton(R.string.new_exam_dialog_delete_confirm_btn_label, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						mExamManager.delete(exam);
						setResult(RESULT_OK);
						finish();
					}
				})
				.setNegativeButton(R.string.new_exam_dialog_delete_cancel_btn_label, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				})
				.show();
				break;
			case R.id.new_exam_btn_done:
				if (validate()) {
					addNewExam();
					setResult(RESULT_OK);
					finish();
				} else {
					(new AlertDialog.Builder(NewExamActivity.this))
					.setMessage(R.string.new_exam_dialog_validate_text)
					.setPositiveButton(R.string.new_exam_dialog_validate_OK_btn_label, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					})
					.show();
				}
				break;
			case R.id.new_exam_btn_cancel:
				cancel();
				break;
			}
		}
	};
	
	private void addNewExam() {
		if (!editExam) {
			Location location = new Location(-1, new_exam_et_bldg.getText().toString(), new_exam_et_room.getText().toString(), null);
			TimePlaceBlock block = new TimePlaceBlock(-1, location, start, end, 0);

			Exam newExam = new Exam(-1, new_exam_et_desc.getText().toString(), mCourse, block);

			mExamManager.insert(newExam);
		} else {
			exam.setDescription(new_exam_et_desc.getText().toString());
			Location location = exam.getBlock().getLocation();
			location.setBuilding(new_exam_et_bldg.getText().toString());
			location.setRoom(new_exam_et_room.getText().toString());
			TimePlaceBlock block = exam.getBlock();
			block.setStartTime(start);
			block.setEndTime(end);
			mExamManager.insert(exam);
		}
	}
	
	private void cancel() {
		setResult(RESULT_CANCELED);
		finish();
	}
}
