/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */
package com.selagroup.schedu.activities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.selagroup.schedu.R;
import com.selagroup.schedu.ScheduApplication;
import com.selagroup.schedu.Utility;
import com.selagroup.schedu.adapters.WorkArrayAdapter;
import com.selagroup.schedu.adapters.WorkArrayAdapter.WorkEditListener;
import com.selagroup.schedu.managers.AssignmentManager;
import com.selagroup.schedu.model.Assignment;
import com.selagroup.schedu.model.Course;

/**
 * The Class CourseWorkActivity.
 */
public class CourseWorkActivity extends ListActivity {
	
	AssignmentManager mWorkManager;
	WorkArrayAdapter mWorkAdapter;
	List<Assignment> mWorkList;
	Course mCourse;
	Boolean mAddMode = false;
	
	Button course_work_btn_add;
	LinearLayout course_work_ll_add;
	Button course_work_btn_due;
	EditText course_work_et_desc;
	ImageButton course_work_btn_cancel;
	
	private WorkEditListener mWorkEditListener = new WorkEditListener() {
		public void onWorkDelete(final Assignment iWorkItem) {
			AlertDialog.Builder dlg = new AlertDialog.Builder(CourseWorkActivity.this);
			String msg = getResources().getString(R.string.course_work_delete_dialog_text) + "\n" + iWorkItem.getName();
			dlg
				.setTitle(R.string.course_work_delete_dialog_confirm_label)
				.setMessage(msg)
				.setPositiveButton(R.string.course_work_delete_dialog_confirm_label,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								mWorkList.remove(iWorkItem);
								mWorkManager.delete(iWorkItem);
								mWorkAdapter.notifyDataSetChanged();
							}
						})
				.setNegativeButton(R.string.course_work_cancel_btn_label,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						})
				.show();
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_course_work);
		
		initActivity();
		
		initWidgets();
		initListeners();
	}
	
	@Override
	public void onBackPressed() {
		if (mAddMode) {
			cancelAdd();
		}
		else {
			super.onBackPressed();
		}
	}
	
	protected void initActivity() {
		mCourse = ((ScheduApplication) getApplication()).getCourseManager().get(getIntent().getIntExtra("courseID", -1));
		((TextView) findViewById(R.id.course_work_course_code)).setText(mCourse.getCode());

		mWorkManager = ((ScheduApplication) getApplication()).getAssignmentManager();
		mWorkList = mWorkManager.getAllForCourse(mCourse.getID());
		mWorkAdapter = new WorkArrayAdapter(CourseWorkActivity.this, R.layout.adapter_work_select, mWorkList, mWorkEditListener);
		setListAdapter(mWorkAdapter);
	}
	
	protected void initWidgets() {
		course_work_btn_add = (Button) findViewById(R.id.course_work_btn_add);
		course_work_ll_add = (LinearLayout) findViewById(R.id.course_work_ll_add);
		course_work_btn_due = (Button) findViewById(R.id.course_work_btn_due);
		course_work_et_desc = (EditText) findViewById(R.id.course_work_et_desc);
		course_work_btn_cancel = (ImageButton) findViewById(R.id.course_work_btn_cancel);
	}
	
	protected void initListeners() {
		course_work_btn_add.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (mAddMode) { // User tapped Done
					if (validateInputs()) { // input is valid
						Utility.hideSoftKeyboard(CourseWorkActivity.this, course_work_et_desc.getWindowToken());
						course_work_btn_add.setText(R.string.course_work_add_btn_label);
						course_work_ll_add.setVisibility(View.GONE);
						mAddMode = false;
						Assignment newWorkItem = new Assignment(-1, course_work_et_desc.getText().toString(), (Calendar) course_work_btn_due.getTag(), mCourse);
						mWorkManager.insert(newWorkItem);
						mWorkList.clear();
						mWorkList.addAll(mWorkManager.getAllForCourse(mCourse.getID()));
						resetInputs();
					}
					else { // input not valid
						AlertDialog.Builder dlg = new AlertDialog.Builder(CourseWorkActivity.this);
						dlg
							.setTitle(R.string.course_work_invalid_dialog_title)
							.setMessage(R.string.course_work_invalid_dialog_text)
							.setPositiveButton(R.string.course_work_invalid_dialog_OK_btn_label,
									new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int which) {
											dialog.dismiss();
										}
									})
							.show();
					}
				}
				else { // User tapped Add
					mAddMode = true;
					course_work_btn_add.setText(R.string.course_work_done_btn_label);
					course_work_ll_add.setVisibility(View.VISIBLE);
				}
			}
		});
		
		course_work_btn_cancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				cancelAdd();
			}
		});
		
		course_work_btn_due.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Calendar now = Calendar.getInstance();
				DatePickerDialog dlg = new DatePickerDialog(CourseWorkActivity.this, new OnDateSetListener() {
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
						Calendar dueDate = Calendar.getInstance();
						dueDate.set(year, monthOfYear, dayOfMonth);
						course_work_btn_due.setTag(dueDate);
						course_work_btn_due.setText((new SimpleDateFormat("M/dd")).format(dueDate.getTime()));
					}
				}, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
				dlg.show();
			}
		});
	}
	
	protected void cancelAdd() {
		Utility.hideSoftKeyboard(CourseWorkActivity.this, course_work_et_desc.getWindowToken());
		resetInputs();
		course_work_btn_add.setText(R.string.course_work_add_btn_label);
		course_work_ll_add.setVisibility(View.GONE);
		mAddMode = false;
	}
	
	protected boolean validateInputs() {
		if ("".equals(course_work_et_desc.getText().toString()) || course_work_btn_due.getTag() == null) {
			return false;
		}
		else {
			return true;
		}
	}
	
	protected void resetInputs() {
		course_work_btn_due.setText(R.string.course_work_due_btn_label);
		course_work_et_desc.setText("");
	}
	
	/**
	 * Context menu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Utility.buildOptionsMenu(CourseWorkActivity.this, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (Utility.handleOptionsMenuSelection(CourseWorkActivity.this, item)) {
			return true;
		}
		else {
			return super.onOptionsItemSelected(item);
		}
	}
	/*
	private void mockup() {
		final Dialog dlgNewAssignment = new Dialog(this);
		//dlgNewAssignment.setTitle(R.string.dialog_new_assignment_title);
		dlgNewAssignment.requestWindowFeature(Window.FEATURE_NO_TITLE);
 		dlgNewAssignment.setContentView(R.layout.dialog_work);
 		dlgNewAssignment.findViewById(R.id.dialog_work_btn_cancel).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dlgNewAssignment.dismiss();
			}
		});
		((ImageButton) findViewById(R.id.course_work_btn_add)).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dlgNewAssignment.show();
			}
		});
	}
	*/
}
