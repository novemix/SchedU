/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */

package com.selagroup.schedu.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.selagroup.schedu.R;

/**
 * The Class TermActivity.
 */
public class TermActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_term);
		
		startActivity(new Intent(this, AddCourseActivity.class));
	}
}
