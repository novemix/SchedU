/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */
package com.selagroup.schedu.activities;



import com.selagroup.schedu.R;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * The Class PreferencesActivity.
 */
public class PreferencesActivity extends PreferenceActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preferences);
		addPreferencesFromResource(R.xml.preferences);
	}
}
