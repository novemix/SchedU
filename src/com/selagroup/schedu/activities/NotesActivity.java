/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */
package com.selagroup.schedu.activities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaRecorder;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.selagroup.schedu.R;
import com.selagroup.schedu.ScheduApplication;
import com.selagroup.schedu.Utility;
import com.selagroup.schedu.managers.NoteManager;
import com.selagroup.schedu.model.note.Note;

/**
 * The Class NotesActivity.
 */
public class NotesActivity extends Activity {

	private static AlertDialog.Builder AUDIO_SAVE_DIALOG;

	private MediaRecorder mRecorder;
	private boolean mIsRecording = false;

	private ImageButton notes_btn_text;
	private ImageButton notes_btn_audio;
	private ImageButton notes_btn_camera;

	private NoteManager mNoteManager;
	private List<Note> mNotes;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notes);

		mNoteManager = ((ScheduApplication) getApplication()).getNoteManager();
		mNotes = mNoteManager.getAll();

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

		initWidgets();
		initListeners();
	}

	/**
	 * Context menu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Utility.buildOptionsMenu(NotesActivity.this, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (Utility.handleOptionsMenuSelection(NotesActivity.this, item)) {
			return true;
		}
		else {
			return super.onOptionsItemSelected(item);
		}
	}

	private void initWidgets() {
		notes_btn_text = ((ImageButton) findViewById(R.id.notes_btn_text));
		notes_btn_audio = ((ImageButton) findViewById(R.id.notes_btn_audio));
		notes_btn_camera = ((ImageButton) findViewById(R.id.notes_btn_camera));

		final EditText audioFileName = new EditText(this);
		audioFileName.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		AUDIO_SAVE_DIALOG = new Builder(this);
		AUDIO_SAVE_DIALOG.setTitle("Enter Audio Note Name");
		AUDIO_SAVE_DIALOG.setView(audioFileName);
		AUDIO_SAVE_DIALOG.setPositiveButton("Save", new AlertDialog.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// Save audio
				// audioFileName
				File saveDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PODCASTS);
				if (saveDir.canWrite()) {
					File saveFile = new File(saveDir, audioFileName.getText().toString());

					try {
						// Make sure the Pictures directory exists.
						saveDir.mkdirs();

						// Very simple code to copy a picture from the application's
						// resource into the external file. Note that this code does
						// no error checking, and assumes the picture is small (does not
						// try to copy it in chunks). Note that if external storage is
						// not currently mounted this will silently fail.
						InputStream is = getResources().openRawResource(R.drawable.bg_splash);
						OutputStream os = new FileOutputStream(saveFile);
						byte[] data = new byte[is.available()];
						is.read(data);
						os.write(data);
						is.close();
						os.close();
						
						// Tell the media scanner about the new file so that it is
						// immediately available to the user.
						MediaScannerConnection.scanFile(NotesActivity.this,
								new String[] { saveFile.toString() }, null,
								new MediaScannerConnection.OnScanCompletedListener() {
									public void onScanCompleted(String path, Uri uri) {
										Log.i("ExternalStorage", "Scanned " + path + ":");
										Log.i("ExternalStorage", "-> uri=" + uri);
									}
								});
					} catch (IOException e) {
						// Unable to create file, likely because external storage is
						// not currently mounted.
						Log.w("ExternalStorage", "Error writing " + saveFile, e);
					}

				}
				// MediaStore.Images.Media.insertImage(cr, source, title, description)
			}
		});
		AUDIO_SAVE_DIALOG.setNegativeButton("Cancel", new AlertDialog.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// Delete tmp file

			}
		});
	}

	private void initListeners() {

		notes_btn_text.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(NotesActivity.this, TextNoteActivity.class));
			}
		});

		notes_btn_audio.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				handleRecord();
			}
		});

		notes_btn_camera.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

			}
		});
	}

	private void handleRecord() {
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
}
