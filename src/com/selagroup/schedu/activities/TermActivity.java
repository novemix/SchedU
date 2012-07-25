/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */

package com.selagroup.schedu.activities;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.selagroup.schedu.MyApplication;
import com.selagroup.schedu.R;
import com.selagroup.schedu.adapters.TermArrayAdapter;
import com.selagroup.schedu.managers.TermManager;
import com.selagroup.schedu.model.Term;

/**
 * The Class TermActivity.
 */
public class TermActivity extends Activity {

	// Managers
	private TermManager mTermManager;

	// Views
	private Spinner term_sp_select;
	private Button term_btn_start;
	private Button term_btn_end;
	private Button term_btn_ok;

	// Adapters
	private TermArrayAdapter mTermAdapter;

	// Data
	private List<Term> mTerms;
	private List<Integer> mNewTermIndices = new LinkedList<Integer>();
	private Term mNewTerm = null;
	private Term mSelectedTerm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_term);

		mTermManager = ((MyApplication) getApplication()).getTermManager();

		mTerms = mTermManager.getAll();
		mTerms.add(0, null);

		initWidgets();
	}

	private void initWidgets() {
		// Get views from xml resource
		term_sp_select = (Spinner) findViewById(R.id.term_sp_select);
		term_btn_start = (Button) findViewById(R.id.term_btn_start);
		term_btn_end = (Button) findViewById(R.id.term_btn_end);
		term_btn_ok = (Button) findViewById(R.id.term_btn_ok);

		// Set up spinner adapter
		mTermAdapter = new TermArrayAdapter(this, R.layout.adapter_term_select, mTerms);
		mTermAdapter.setDropDownViewResource(R.layout.adapter_term_select);
		term_sp_select.setAdapter(mTermAdapter);

		// Select the first term that has already been entered, if there are any
		if (mTerms.size() > 1) {
			term_sp_select.setSelection(1);
		}

		term_sp_select.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

				// Check for "Add New Term" selection, marked by a null entry in mTerms
				int selectionIndex = pos;
				mSelectedTerm = mTerms.get(pos);
				if (mSelectedTerm == null) {
					// Create and add a new term to the list
					mNewTerm = new Term(-1, null, null);
					mTerms.add(mNewTerm);

					// Select the newly added term
					selectionIndex = mTerms.size() - 1;
					term_sp_select.setSelection(selectionIndex);
					mSelectedTerm = mNewTerm;

					// Add term index
					mNewTermIndices.add(selectionIndex);
				}

				// Update button text based on selected term
				updateButtonText(mTerms.get(selectionIndex));
			}

			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		term_btn_start.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				showStartDateDialog(mSelectedTerm.getStartDate());
			}
		});

		term_btn_end.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				showEndDateDialog(mSelectedTerm.getEndDate());
			}
		});

		term_btn_ok.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				// Add new terms
				for (Integer index : mNewTermIndices) {
					Term addedTerm = mTerms.get(index);
					if (termIsValid(addedTerm)) {
						mTermManager.insert(addedTerm);
						Toast.makeText(TermActivity.this, "Added new term: " + addedTerm, Toast.LENGTH_SHORT).show();
					}
				}

				// Update terms
				for (Term term : mTerms) {
					if (termIsValid(term)) {
						mTermManager.update(term);
					}
				}

				// Move to next activity if a valid term is selected
				if (termIsValid(mSelectedTerm)) {
					startActivity(new Intent(TermActivity.this, AddCourseActivity.class));
				}
				else {
					Toast.makeText(TermActivity.this, "The selected term must have a start date and an end date.", Toast.LENGTH_LONG).show();
				}
			}
		});
	}
	
	private void showStartDateDialog(Calendar initDate) {
		// Set up start date dialog
		DatePickerDialog startDateDialog = new DatePickerDialog(this, new OnDateSetListener() {
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Calendar endDate = mSelectedTerm.getEndDate();
				Calendar startDate = Calendar.getInstance();
				startDate.set(Calendar.YEAR, year);
				startDate.set(Calendar.MONTH, monthOfYear);
				startDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

				if (endDate != null && startDate.after(endDate)) {
					Toast.makeText(TermActivity.this, "Term cannot start after it ends!", Toast.LENGTH_SHORT).show();
				} else {
					mSelectedTerm.setStartDate(startDate);
					mTermAdapter.notifyDataSetChanged();
					updateButtonText(mSelectedTerm);
				}
			}
		}, initDate.get(Calendar.YEAR), initDate.get(Calendar.MONTH), initDate.get(Calendar.DAY_OF_MONTH));
		startDateDialog.setTitle("Set Term Start Date");
		startDateDialog.show();
	}
	
	private void showEndDateDialog(Calendar initDate) {
		// Set up end date dialog
		DatePickerDialog endDateDialog = new DatePickerDialog(this, new OnDateSetListener() {
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Calendar startDate = mSelectedTerm.getStartDate();
				Calendar endDate = Calendar.getInstance();
				endDate.set(Calendar.YEAR, year);
				endDate.set(Calendar.MONTH, monthOfYear);
				endDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

				if (startDate != null && endDate.before(startDate)) {
					Toast.makeText(TermActivity.this, "Term cannot end before it starts!", Toast.LENGTH_SHORT).show();
				} else {
					mSelectedTerm.setEndDate(endDate);
					mTermAdapter.notifyDataSetChanged();
					updateButtonText(mSelectedTerm);
				}
			}
		}, initDate.get(Calendar.YEAR), initDate.get(Calendar.MONTH), initDate.get(Calendar.DAY_OF_MONTH));
		endDateDialog.setTitle("Set Term End Date");
		endDateDialog.show();
	}

	/**
	 * Updates the start date and end date buttons with the correct text for the selected term
	 * @param selectedTerm The currently selected term
	 */
	private void updateButtonText(Term selectedTerm) {
		if (selectedTerm.getStartDate() == null) {
			term_btn_start.setText("Select Start");
		} else {
			term_btn_start.setText(selectedTerm.getStartDateString());
		}
		if (selectedTerm.getEndDate() == null) {
			term_btn_end.setText("Select End");
		} else {
			term_btn_end.setText(selectedTerm.getEndDateString());
		}
	}

	private boolean termIsValid(Term term) {
		return term != null && term.getStartDate() != null && term.getEndDate() != null;
	}
}
