/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */

package com.selagroup.schedu.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.selagroup.schedu.R;
import com.selagroup.schedu.database.DatabaseHelper;
import com.selagroup.schedu.database.NoteManager;

/**
 * The Class TermActivity.
 */
public class TermActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_term);
		
	
		DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
		NoteManager mgr1 = new NoteManager(dbHelper);
		NoteManager mgr2 = new NoteManager(dbHelper);
		
		startActivity(new Intent(this, AddCourseActivity.class));
	}
}
