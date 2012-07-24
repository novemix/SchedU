/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */
package com.selagroup.schedu.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.selagroup.schedu.R;
import com.selagroup.schedu.Sketch;

/**
 * The Class SketchNoteActivity.
 */
public class SketchNoteActivity extends Activity {
	
	Sketch sketch;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sketchnote);
		
		sketch = new Sketch(this);
		((LinearLayout) findViewById(R.id.sketchnote_ll)).addView(sketch);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("Save Note");
		menu.add("Clear");
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		String choice = (String) item.getTitle();
		if (choice.equals("Clear")) {
			sketch.clear();
		}
		if (choice.equals("Save Note")) {
			sketch.save();
		}
		return true;
	}
}
