/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */
package com.selagroup.schedu.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
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
			saveDialog();
		}
		return true;
	}
	
	public void saveDialog() {
		//todo: get rid of hardcoded strings, save description in db, etc for save
		AlertDialog.Builder dlg = new AlertDialog.Builder(SketchNoteActivity.this);
		dlg.setTitle("Save Note");
		dlg.setMessage("Description:");
		final EditText input = new EditText(SketchNoteActivity.this);
		dlg.setView(input);

		dlg.setNegativeButton("Cancel", new OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// nothing
			}
		});
		dlg.setPositiveButton("Save", new OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				sketch.save();
			}
		});
		dlg.show();
	}
}
