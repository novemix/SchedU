/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */

package com.selagroup.schedu.activities;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;

import com.selagroup.schedu.AlarmSystem;
import com.selagroup.schedu.R;
import com.selagroup.schedu.ScheduApplication;
import com.selagroup.schedu.Utility;
import com.selagroup.schedu.adapters.TermArrayAdapter;
import com.selagroup.schedu.adapters.TermArrayAdapter.TermEditListener;
import com.selagroup.schedu.adapters.TermArrayAdapter.TermSelectListener;
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
	private ImageView term_btn_add;

	// Data
	private boolean mAddMode = false;
	private Term mNewTerm = null;
	private List<Term> mTerms;

	// Comparator
	private static final Comparator<Term> mTermComparator = new Comparator<Term>() {
		// -1 = lhs < rhs, 1 = lhs > rhs
		public int compare(Term lhs, Term rhs) {
			if (lhs == null || lhs.getStartDate() == null || lhs.getEndDate() == null) {
				return -1;
			}
			if (rhs == null || rhs.getStartDate() == null || rhs.getEndDate() == null) {
				return 1;
			}
			int retVal = rhs.getStartDate().compareTo(lhs.getStartDate());
			return retVal;
		}
	};

	// Listeners
	private TermEditListener mTermEditListener = new TermEditListener() {
		public void onTermEdit(Term iTerm) {
			if (mAddMode) {
				term_btn_add.setImageResource(termIsValid(iTerm) ? R.drawable.done_layer_list : R.drawable.layer_list_cancel);
				term_btn_add.invalidate();
			}
			if (termIsValid(iTerm)) {
				mTermManager.update(iTerm);
				Collections.sort(mTerms, mTermComparator);

				ScheduApplication app = (ScheduApplication) getApplication();
				Term currentTerm = Utility.getCurrentTerm(mTerms, Calendar.getInstance());
				AlarmSystem alarmSys = app.getAlarmSystem();
				if (currentTerm != null) {
					alarmSys.scheduleEventsForDay(app.getCourseManager().getAllForTerm(currentTerm.getID()), Calendar.getInstance(), true);
				} else {
					alarmSys.clearAlarms();
				}
			}
		}

		public void onTermDelete(Term iTerm) {
			mTerms.remove(iTerm);
			if (mTerms.isEmpty()) {
				mTerms.add(null);
			}
			mTermManager.delete(iTerm);
			mTermAdapter.notifyDataSetChanged();
			onTermEdit(iTerm);
			term_btn_add.setEnabled(true);
		}
	};

	private TermSelectListener mTermSelectListener = new TermSelectListener() {
		public void onTermSelect(Term iTerm) {
			ScheduApplication app = (ScheduApplication) getApplication();
			Intent addCourseIntent = new Intent(TermActivity.this, AllCoursesActivity.class);
			app.setCurrentTerm(iTerm);
			startActivity(addCourseIntent);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_term);

		mTermManager = ((ScheduApplication) getApplication()).getTermManager();

		mTerms = mTermManager.getAll();
		if (mTerms.isEmpty()) {
			mTerms.add(null);
		}
		Collections.sort(mTerms, mTermComparator);

		initWidgets();
	}

	@Override
	public void onBackPressed() {
		if (mAddMode) {
			cancelAdd();
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(getResources().getString(R.string.menu_item_preferences));
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getTitle().equals(getResources().getString(R.string.menu_item_preferences))) {
			startActivity(new Intent(TermActivity.this, ScheduPreferences.class));
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}

	private void initWidgets() {
		term_btn_add = (ImageView) findViewById(R.id.term_btn_add);

		term_btn_add.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (!mAddMode) {
					if (mTerms.contains(null)) {
						mTerms.clear();
					}

					mAddMode = true;
					mTermAdapter.setAddMode(true);
					term_btn_add.setImageResource(R.drawable.layer_list_cancel);

					mNewTerm = new Term(-1, null, null);
					mTerms.add(0, mNewTerm);

					mTermAdapter.setEditIndex(mTerms.indexOf(mNewTerm));
					mTermAdapter.notifyDataSetChanged();
					mTermEditListener.onTermEdit(mNewTerm);
				} else {
					// Valid term, insert (Done button pressed)
					if (termIsValid(mNewTerm)) {
						addTerm();
					}
					// Invalid term, remove from list (Cancel button pressed)
					else {
						cancelAdd();
					}
				}
			}
		});

		// Set up spinner adapter
		mTermAdapter = new TermArrayAdapter(this, R.layout.adapter_term_select, mTerms, mTermEditListener, mTermSelectListener);

		mTermAdapter.setDropDownViewResource(R.layout.adapter_term_select);
		setListAdapter(mTermAdapter);
	}

	/**
	 * Checks if a Term is valid: can be inserted into the database and used as a Term
	 * @param term the term to check
	 * @return true if a valid term
	 */
	private boolean termIsValid(Term term) {
		return term != null && term.getStartDate() != null && term.getEndDate() != null;
	}
	
	private void cancelAdd() {
		mAddMode = false;
		mTermAdapter.setAddMode(false);
		mTermAdapter.setEditIndex(-1);
		
		mTermAdapter.remove(mNewTerm);
		mNewTerm = null;
		if (mTermAdapter.isEmpty()) {
			mTerms.add(null);
		}
		term_btn_add.setImageResource(R.drawable.layer_list_add);
		term_btn_add.invalidate();
		mTermAdapter.notifyDataSetChanged();
	}
	
	private void addTerm() {
		mAddMode = false;
		mTermAdapter.setAddMode(false);
		mTermAdapter.setEditIndex(-1);
		
		mTermManager.insert(mNewTerm);
		mTermEditListener.onTermEdit(mNewTerm);

		// Sort terms by start date
		Collections.sort(mTerms, mTermComparator);
		term_btn_add.setImageResource(R.drawable.layer_list_add);
		term_btn_add.invalidate();
		mTermAdapter.notifyDataSetChanged();
	}
	
	@Override
	public void onAttachedToWindow() {
	    super.onAttachedToWindow();
	    Window window = getWindow();
	    window.setFormat(PixelFormat.RGBA_8888);
	}
}
