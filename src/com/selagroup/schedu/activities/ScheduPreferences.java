/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */
package com.selagroup.schedu.activities;

import java.util.Map;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.media.AudioManager;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

import com.selagroup.schedu.R;

/**
 * The Class PreferencesActivity.
 */
public class ScheduPreferences extends PreferenceActivity implements OnSharedPreferenceChangeListener {
	public static final String PREF_KEY_SILENT = "autoSilence";
	public static final String PREF_KEY_COURSE_REMIND = "remindCourse";
	public static final String PREF_KEY_COURSE_REMIND_TIME = "courseLeadTime";
	public static final String PREF_KEY_ASSSIGN_REMIND = "remindAssign";
	public static final String PREF_KEY_ASSSIGN_REMIND_TIME1 = "assignLeadTime1";
	public static final String PREF_KEY_ASSSIGN_REMIND_TIME2 = "assignLeadTime2";
	public static final String PREF_KEY_HIGHLIGHT_RED = "highlightRed";
	public static final String PREF_KEY_HIGHLIGHT_YELLOW = "highlightYellow";

	private SharedPreferences sharedPref;
	private Map<String, ?> mAllPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preferences);
		addPreferencesFromResource(R.xml.preferences);

		sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
	}

	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

		mAllPreferences = sharedPref.getAll();

		AudioManager audioMgr = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (key.equals(PREF_KEY_SILENT)) {
			if ((Boolean) mAllPreferences.get(PREF_KEY_SILENT)) {
				// audioMgr.setRingerMode(AudioManager.RINGER_MODE_SILENT);
				Log.i("Test", "Ringer mode: silent");
			} else {
				// audioMgr.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
				Log.i("Test", "Ringer mode: normal");
			}
		} else if (key.equals(PREF_KEY_COURSE_REMIND)) {

		} else if (key.equals(PREF_KEY_COURSE_REMIND_TIME)) {

		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
	}
}
