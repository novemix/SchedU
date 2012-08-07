/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */
package com.selagroup.schedu.activities;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.selagroup.schedu.R;

/**
 * The Class NotesActivity.
 */
public class NotesActivity extends Activity {

	private static AlertDialog.Builder AUDIO_SAVE_DIALOG;
	
	private MediaRecorder mRecorder;
	private boolean mIsRecording = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notes);
		
		EditText et = new EditText(this);
		et.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		AUDIO_SAVE_DIALOG = new Builder(this);
		AUDIO_SAVE_DIALOG.setTitle("Enter Audio Note Name");
		AUDIO_SAVE_DIALOG.setView(et);
		AUDIO_SAVE_DIALOG.setPositiveButton("Save", new AlertDialog.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		AUDIO_SAVE_DIALOG.setNegativeButton("Cancel", new AlertDialog.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});

		final String[][] stuff = new String[][] { { "1", "one" }, { "2", "two" }, { "3", "three" }, { "4", "four" }, { "5", "five" }, { "6", "six" }, { "7", "seven" }, { "8", "eight" },
		        { "9", "nine" }, { "10", "ten" }, { "11", "really long description, let's see what happens when we get really long, like now, ok?" }, { "12", "twelve" } };

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
				return row;
			}
		}
		((ListView) findViewById(R.id.notes_lv_list)).setAdapter(new MyAdapter(this, R.layout.listitem_notes, stuff));

		initListeners();
	}

	private void initListeners() {
		((ImageButton) findViewById(R.id.notes_btn_sketch)).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(NotesActivity.this, SketchNoteActivity.class));
			}
		});

		((ImageButton) findViewById(R.id.notes_btn_text)).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(NotesActivity.this, TextNoteActivity.class));
			}
		});

		((ImageButton) findViewById(R.id.notes_btn_audio)).setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (!mIsRecording) {
					mRecorder = new MediaRecorder();
					mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
					mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
					mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
					mRecorder.setOutputFile(NotesActivity.this.getFilesDir().getPath().toString() +
							File.separator + "tmp.mp3");
					try {
						mRecorder.prepare();
						mRecorder.start(); // Recording is now started
						mIsRecording = true;
						Toast.makeText(NotesActivity.this, "Recording started", Toast.LENGTH_SHORT).show();
					} catch (IllegalStateException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}

				} else {
					mRecorder.stop();
					mRecorder.reset(); // You can reuse the object by going back to setAudioSource() step
					mRecorder.release(); // Now the object cannot be reused
					AUDIO_SAVE_DIALOG.show();
					mIsRecording = false;
					Toast.makeText(NotesActivity.this, "Recording done", Toast.LENGTH_SHORT).show();
				}
			}
		});

		((ImageButton) findViewById(R.id.notes_btn_camera)).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

			}
		});
	}
}
