/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */
package com.selagroup.schedu.activities;

import com.selagroup.schedu.R;
import com.selagroup.schedu.Utility;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

/**
 * The Class CourseExamsActivity.
 */
public class CourseExamsActivity extends ListActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_course_exams);
		
		mockup();
	}
	
	/**
	 * Context menu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Utility.buildOptionsMenu(CourseExamsActivity.this, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (Utility.handleOptionsMenuSelection(CourseExamsActivity.this, item)) {
			return true;
		}
		else {
			return super.onOptionsItemSelected(item);
		}
	}
	
	private void mockup() {
		((ImageButton) findViewById(R.id.course_exams_btn_add)).setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				startActivity(new Intent(CourseExamsActivity.this, NewExamActivity.class));
			}
		});
		
	}
}
