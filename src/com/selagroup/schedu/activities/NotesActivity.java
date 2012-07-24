/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */
package com.selagroup.schedu.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ImageButton;
import com.selagroup.schedu.R;

/**
 * The Class NotesActivity.
 */
public class NotesActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notes);
		
		((ImageButton) findViewById(R.id.notes_btn_sketch)).setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				startActivity(new Intent(NotesActivity.this, SketchNoteActivity.class));
			}
		});
				
		final String[][] stuff = new String[][] {{"1","one"},{"2","two"},{"3","three"},{"4","four"},{"5","five"},
				{"6","six"},{"7","seven"},{"8","eight"},{"9","nine"},{"10","ten"},
				{"11","really long description, let's see what happens when we get really long, like now, ok?"},
				{"12","twelve"}};
		
		final class MyAdapter extends ArrayAdapter<String[]> {

			String[][] arr;
			Activity ctx;
			
			public MyAdapter(Context context, int textViewResourceId, String[][] objects) {
				super(context, textViewResourceId, objects);
				ctx = (Activity) context;
				arr = objects;
			}

			@Override
			public View getView(int position, View row, ViewGroup parent) {
				if (row == null) {
					LayoutInflater li = ctx.getLayoutInflater();
					row = li.inflate(R.layout.listitem_notes, null);
				}
				((TextView) row.findViewById(R.id.notes_list_date)).setText(arr[position][0]);
				((TextView) row.findViewById(R.id.notes_list_title)).setText(arr[position][1]);
				//((ImageView) row.findViewById(R.id.mylistitem_iv)).setImageResource(people.get(position).getFlag());
				return row;
			}
		}
		((ListView) findViewById(R.id.notes_lv_list)).setAdapter(new MyAdapter(this, R.layout.listitem_notes, stuff));
	}
}
