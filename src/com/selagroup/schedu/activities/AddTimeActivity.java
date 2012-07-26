/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */

package com.selagroup.schedu.activities;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.selagroup.schedu.R;
import com.selagroup.schedu.model.Location;
import com.selagroup.schedu.model.TimePlaceBlock;

/**
 * The Class AddTimeActivity.
 */
public class AddTimeActivity extends Activity {
	
	// Misc.
	private static final int DAYS_IN_WEEK = 7;
	private static final SimpleDateFormat sTimeFormat = new SimpleDateFormat("h:mm a");

	// Views
	private Button addtime_btn_add;

	private CheckBox[] mDayCheckboxes = new CheckBox[DAYS_IN_WEEK];

	private EditText addtime_et_building;
	private EditText addtime_et_room;

	private Button addtime_btn_start;
	private Button addtime_btn_end;

	// Data
	private Calendar mStartTime;
	private Calendar mEndTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addtime);

		initWidgets();
	}

	private void initWidgets() {
		addtime_btn_add = (Button) findViewById(R.id.addtime_btn_add);

		mDayCheckboxes[0] = (CheckBox) findViewById(R.id.addtime_cb_sun);
		mDayCheckboxes[1] = (CheckBox) findViewById(R.id.addtime_cb_mon);
		mDayCheckboxes[2] = (CheckBox) findViewById(R.id.addtime_cb_tue);
		mDayCheckboxes[3] = (CheckBox) findViewById(R.id.addtime_cb_wed);
		mDayCheckboxes[4] = (CheckBox) findViewById(R.id.addtime_cb_thu);
		mDayCheckboxes[5] = (CheckBox) findViewById(R.id.addtime_cb_fri);
		mDayCheckboxes[6] = (CheckBox) findViewById(R.id.addtime_cb_sat);

		addtime_et_building = (EditText) findViewById(R.id.addtime_et_building);
		addtime_et_room = (EditText) findViewById(R.id.addtime_et_room);

		addtime_btn_start = (Button) findViewById(R.id.addtime_btn_start);
		addtime_btn_end = (Button) findViewById(R.id.addtime_btn_end);

		initListeners();
	}

	private void initListeners() {
		addtime_btn_start.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showStartTimeDialog(mStartTime);
			}
		});

		addtime_btn_end.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showEndTimeDialog(mEndTime);
			}
		});

		addtime_btn_add.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent returnIntent = new Intent();
				setResult(RESULT_OK, returnIntent);

				Location location = new Location(-1, addtime_et_building.getText().toString(), addtime_et_room.getText().toString(), "");
				boolean validBlock = false;
				boolean[] dayFlag = new boolean[DAYS_IN_WEEK];
				for (int i = 0; i < DAYS_IN_WEEK; ++i) {
					dayFlag[i] = mDayCheckboxes[i].isChecked();
					validBlock |= dayFlag[i];
				}
				validBlock &= (mStartTime != null) && (mEndTime != null);
				
				// If a valid block has been filled out, return to the add course activity
				if (validBlock) {
					TimePlaceBlock block = new TimePlaceBlock(-1, location, mStartTime, mEndTime, dayFlag);
					returnIntent.putExtra("block", block);

					finish();
				}
				else {
					Toast.makeText(AddTimeActivity.this, "Please select a start time, an end time, and at least one day of the week.", Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	@Override
	public void onBackPressed() {
		Intent returnIntent = new Intent();
		setResult(RESULT_CANCELED, returnIntent);
		finish();
		super.onBackPressed();
	}
	
	private void showStartTimeDialog(Calendar initDate) {
		if (initDate == null) {
			initDate = Calendar.getInstance();
		}

		// Make sure date is consistent, so that the start/end times can be compared
		initDate.set(Calendar.YEAR, 2000);
		initDate.set(Calendar.MONTH, Calendar.JANUARY);
		initDate.set(Calendar.DAY_OF_MONTH, 1);

		TimePickerDialog startTimeDialog = new TimePickerDialog(this, new OnTimeSetListener() {
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				Calendar startTime = Calendar.getInstance();
				startTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
				startTime.set(Calendar.MINUTE, minute);

				if (startTime.after(mEndTime)) {
					Toast.makeText(AddTimeActivity.this, "Class cannot start after it ends!", Toast.LENGTH_SHORT).show();
				}
				else {
					mStartTime = startTime;
					updateButtonText();
				}
			}
		}, initDate.get(Calendar.HOUR_OF_DAY), initDate.get(Calendar.MINUTE), false);
		startTimeDialog.setTitle("Set Start Time");
		startTimeDialog.show();
	}

	private void showEndTimeDialog(Calendar initDate) {
		if (initDate == null) {
			initDate = Calendar.getInstance();
		}

		// Make sure date is consistent, so that the start/end times can be compared
		initDate.set(Calendar.YEAR, 2000);
		initDate.set(Calendar.MONTH, Calendar.JANUARY);
		initDate.set(Calendar.DAY_OF_MONTH, 1);

		TimePickerDialog endTimeDialog = new TimePickerDialog(this, new OnTimeSetListener() {
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				Calendar endTime = Calendar.getInstance();
				endTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
				endTime.set(Calendar.MINUTE, minute);

				if (endTime.before(mStartTime)) {
					Toast.makeText(AddTimeActivity.this, "Class cannot end before it starts!", Toast.LENGTH_SHORT).show();
				}
				else {
					mEndTime = endTime;
					updateButtonText();
				}
			}
		}, initDate.get(Calendar.HOUR_OF_DAY), initDate.get(Calendar.MINUTE), false);
		endTimeDialog.setTitle("Set End Time");
		endTimeDialog.show();
	}
	
	private void updateButtonText() {
		if (mStartTime != null) {
			addtime_btn_start.setText(sTimeFormat.format(mStartTime.getTime()));
		}
		if (mEndTime != null) {
			addtime_btn_end.setText(sTimeFormat.format(mEndTime.getTime()));
		}
	}
}