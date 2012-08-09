/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */

package com.selagroup.schedu.activities;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ToggleButton;

import com.selagroup.schedu.R;
import com.selagroup.schedu.ScheduApplication;
import com.selagroup.schedu.adapters.TermArrayAdapter;
import com.selagroup.schedu.managers.TermManager;
import com.selagroup.schedu.model.Term;

/**
 * The Class TermActivity.
 */
public class TermActivity extends ListActivity {

	// Managers
	private TermManager mTermManager;

	// Adapters
	private TermArrayAdapter mTermAdapter;

	// Widgets
	private ToggleButton term_btn_edit;
	private ImageButton term_btn_add;

	// Data
	private boolean mEditMode = false;
	private List<Term> mTerms;
	private Term mNewTerm = null;
	private Term mSelectedTerm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_term);

		mTermManager = ((ScheduApplication) getApplication()).getTermManager();

		mTerms = mTermManager.getAll();
		if (mTerms.isEmpty()) {
			mTerms.add(null);
		}

		initWidgets();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("xPreferences");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getTitle().equals("xPreferences")) {
			startActivity(new Intent(TermActivity.this, ScheduPreferences.class));
			return true;
		}
		else {
			return super.onOptionsItemSelected(item);
		}
	}

	private void initWidgets() {
		term_btn_edit = (ToggleButton) findViewById(R.id.term_btn_edit);
		term_btn_add = (ImageButton) findViewById(R.id.term_btn_add);

		term_btn_edit.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				mEditMode = !mEditMode;
				mTermAdapter.setEditMode(mEditMode);
				mTermAdapter.notifyDataSetChanged();
			}
		});

		term_btn_add.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (mTerms.contains(null)) {
					mTerms.clear();
				}
				mNewTerm = new Term(-1, null, null);
				mTerms.add(0, mNewTerm);
				mTermAdapter.notifyDataSetChanged();
				mEditMode = true;
				term_btn_edit.setChecked(mEditMode);
				mTermAdapter.setEditMode(mEditMode);
				mTermAdapter.setEditIndex(mTerms.indexOf(mNewTerm));
				mTermAdapter.notifyDataSetChanged();
			}
		});

		// Set up spinner adapter
		mTermAdapter = new TermArrayAdapter(this, R.layout.adapter_term_select, mTerms);
		mTermAdapter.setDropDownViewResource(R.layout.adapter_term_select);
		setListAdapter(mTermAdapter);

		getListView().setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
				if (getListView().getItemAtPosition(pos) != null) {
					if (mEditMode) {
						mTermAdapter.setEditIndex(pos);
						mTermAdapter.notifyDataSetChanged();
					}
					else {
						mSelectedTerm = mTermAdapter.getItem(pos);
						Intent addCourseIntent = new Intent(TermActivity.this, AddCourseActivity.class);
						((ScheduApplication) getApplication()).setCurrentTerm(mSelectedTerm);
						addCourseIntent.putExtra("fromTerm", true);
						startActivity(addCourseIntent);
					}
				}
			}
		});
	}

	/**
	 * Updates the start date and end date buttons with the correct text for the selected term
	 * @param selectedTerm The currently selected term
	 */
	private void updateButtonText(Term selectedTerm) {
	}

	/**
	 * Checks if a Term is valid: can be inserted into the database and used as a Term
	 * @param term the term to check
	 * @return true if a valid term
	 */
	private boolean termIsValid(Term term) {
		return term != null && term.getStartDate() != null && term.getEndDate() != null;
	}
}
