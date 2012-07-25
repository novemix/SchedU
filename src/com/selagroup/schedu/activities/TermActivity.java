/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */

package com.selagroup.schedu.activities;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.selagroup.schedu.MyApplication;
//import com.selagroup.schedu.MyDatabaseTest;
import com.selagroup.schedu.R;
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
	
	// Adapters
	private ArrayAdapter<Term> mTermAdapter;
	
	// Data
	private List<Term> mTerms;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_term);

		mTermManager = ((MyApplication) getApplication()).getTermManager();
		Calendar start = Calendar.getInstance();
		start.set(Calendar.YEAR, 2012);
		start.set(Calendar.MONTH, Calendar.JANUARY);
		start.set(Calendar.DAY_OF_MONTH, 4);
		
		Calendar end = Calendar.getInstance();
		end.set(Calendar.YEAR, 2012);
		end.set(Calendar.MONTH, Calendar.MARCH);
		end.set(Calendar.DAY_OF_MONTH, 25);
		
		Log.i("Test", "ms time: " + start.getTimeInMillis());
		Log.i("Test", "ms time: " + end.getTimeInMillis());
		
		mTermManager.insert(new Term(-1, start, end));
		
		mTerms = mTermManager.getAll();
		
		Log.i("Test", "ms time: " + mTerms.get(0).getStartDate().getTimeInMillis());
		Log.i("Test", "ms time: " + mTerms.get(0).getEndDate().getTimeInMillis());

		initWidgets();
		
		((Button) findViewById(R.id.term_btn_ok)).setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				startActivity(new Intent(TermActivity.this, AddCourseActivity.class));
			}
		});
		
	}

	private void initWidgets() {
		term_sp_select = (Spinner) findViewById(R.id.term_sp_select);
		mTermAdapter = new ArrayAdapter<Term>(this, android.R.layout.simple_dropdown_item_1line, mTerms);
		term_sp_select.setAdapter(mTermAdapter);
	}
}
