/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */

package com.selagroup.schedu.activities;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.selagroup.schedu.MyApplication;
import com.selagroup.schedu.R;
import com.selagroup.schedu.managers.LocationManager;
import com.selagroup.schedu.model.Location;
import com.selagroup.schedu.model.TimePlaceBlock;

/**
 * The Class AddTimeActivity.
 */
public class AddTimeActivity extends Activity {

	// Views
	private Button addtime_btn_add;
	private Button addtime_btn_cancel;

	private CheckBox addtime_cb_sun;
	private CheckBox addtime_cb_mon;
	private CheckBox addtime_cb_tue;
	private CheckBox addtime_cb_wed;
	private CheckBox addtime_cb_thu;
	private CheckBox addtime_cb_fri;
	private CheckBox addtime_cb_sat;

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
		addtime_btn_cancel = (Button) findViewById(R.id.addtime_btn_cancel);

		addtime_cb_sun = (CheckBox) findViewById(R.id.addtime_cb_sun);
		addtime_cb_mon = (CheckBox) findViewById(R.id.addtime_cb_mon);
		addtime_cb_tue = (CheckBox) findViewById(R.id.addtime_cb_tue);
		addtime_cb_wed = (CheckBox) findViewById(R.id.addtime_cb_wed);
		addtime_cb_thu = (CheckBox) findViewById(R.id.addtime_cb_thu);
		addtime_cb_fri = (CheckBox) findViewById(R.id.addtime_cb_fri);
		addtime_cb_sat = (CheckBox) findViewById(R.id.addtime_cb_sat);

		addtime_et_building = (EditText) findViewById(R.id.addtime_et_building);
		addtime_et_room = (EditText) findViewById(R.id.addtime_et_room);
		
		addtime_btn_start = (Button) findViewById(R.id.addtime_btn_start);
		addtime_btn_end = (Button) findViewById(R.id.addtime_btn_end);

		initListeners();
	}

	private void initListeners() {
		addtime_btn_start.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
		addtime_btn_end.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		addtime_btn_add.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent returnIntent = new Intent();
				setResult(RESULT_OK, returnIntent);

				Location location = new Location(-1, addtime_et_building.getText().toString(), addtime_et_room.getText().toString(), "");
				Calendar startTime = Calendar.getInstance();
				Calendar endTime = Calendar.getInstance();
				
				// startTime.set(Calendar.HOUR, addtime_sp_start_hour.getSelectedItemPosition() * HOUR_PER_);
				// TimePlaceBlock block = new TimePlaceBlock(-1, location, startTime, endTime, dayFlag);

				finish();
			}
		});

		addtime_btn_cancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent returnIntent = new Intent();
				setResult(RESULT_CANCELED, returnIntent);
				finish();
			}
		});
	}

	private void showStartTimeDialog(Calendar initDate) {
		if (initDate == null) {
			initDate = Calendar.getInstance();
		}
		// Set up start date dialog
		DatePickerDialog startDateDialog = new DatePickerDialog(this, new OnDateSetListener() {
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Calendar startDate = Calendar.getInstance();
				startDate.set(Calendar.YEAR, year);
				startDate.set(Calendar.MONTH, monthOfYear);
				startDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

				if (mStartTime != null && startDate.after(mEndTime)) {
					Toast.makeText(AddTimeActivity.this, "Class cannot start after it ends!", Toast.LENGTH_SHORT).show();
				} else {
//					mSelectedTerm.setStartDate(startDate);
//					mTermAdapter.notifyDataSetChanged();
//					updateButtonText(mSelectedTerm);
				}
			}
		}, initDate.get(Calendar.YEAR), initDate.get(Calendar.MONTH), initDate.get(Calendar.DAY_OF_MONTH));
		startDateDialog.setTitle("Set Term Start Date");
		startDateDialog.show();
	}

	private void showEndTimeDialog(Calendar initDate) {
		if (initDate == null) {
			initDate = Calendar.getInstance();
		}
		// Set up end date dialog
		DatePickerDialog endDateDialog = new DatePickerDialog(this, new OnDateSetListener() {
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Calendar endDate = Calendar.getInstance();
				endDate.set(Calendar.YEAR, year);
				endDate.set(Calendar.MONTH, monthOfYear);
				endDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

				if (mEndTime != null && mEndTime.before(mStartTime)) {
					Toast.makeText(AddTimeActivity.this, "Class cannot end before it starts!", Toast.LENGTH_SHORT).show();
				} else {
//					mSelectedTerm.setEndDate(endDate);
//					mTermAdapter.notifyDataSetChanged();
//					updateButtonText(mSelectedTerm);
				}
			}
		}, initDate.get(Calendar.YEAR), initDate.get(Calendar.MONTH), initDate.get(Calendar.DAY_OF_MONTH));
		endDateDialog.setTitle("Set Term End Date");
		endDateDialog.show();
	}
}