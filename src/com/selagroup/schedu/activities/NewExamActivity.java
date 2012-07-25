/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */
package com.selagroup.schedu.activities;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.selagroup.schedu.R;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;

/**
 * The Class NewExamActivity.
 */
public class NewExamActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_exam);
		
		final Button dateButton = (Button) findViewById(R.id.new_exam_btn_date);
		final Calendar examDate = Calendar.getInstance();
		final OnDateSetListener dateListener = new OnDateSetListener() {
			
			public void onDateSet(DatePicker view, int year, int month, int day) {
				examDate.set(year, month, day);
				dateButton.setText((new SimpleDateFormat("EEE, MMMM dd, yyyy")).format(examDate.getTime()));
			}
		};
		
		OnClickListener btnListener = new OnClickListener() {
			
			public void onClick(View v) {
				switch(v.getId()) {
				case R.id.new_exam_btn_date:
					(new DatePickerDialog(NewExamActivity.this, dateListener, 2012, 6, 25)).show();
					break;
				case R.id.new_exam_btn_start_time:
					(new TimePickerDialog(NewExamActivity.this, null, 0, 0, false)).show();
					break;
				case R.id.new_exam_btn_end_time:
					(new TimePickerDialog(NewExamActivity.this, null, 0, 0, false)).show();
					break;
				}
			}
		};
		dateButton.setOnClickListener(btnListener);
		((Button) findViewById(R.id.new_exam_btn_start_time)).setOnClickListener(btnListener);
		((Button) findViewById(R.id.new_exam_btn_end_time)).setOnClickListener(btnListener);
	}
}
