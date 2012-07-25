/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */
package com.selagroup.schedu.activities;

import com.selagroup.schedu.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/**
 * The Class CourseAssignActivity.
 */
public class CourseAssignActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_course_assign);
		
		final Dialog dlgNewAssignment = new Dialog(this);
		//dlgNewAssignment.setTitle(R.string.dialog_new_assignment_title);
		dlgNewAssignment.requestWindowFeature(Window.FEATURE_NO_TITLE); 
		dlgNewAssignment.setContentView(R.layout.dialog_new_assignment);
		((Button) findViewById(R.id.course_assign_btn_new_assignment)).setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				dlgNewAssignment.show();
			}
		});
		
		((Button) findViewById(R.id.course_assign_btn_new_exam)).setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				startActivity(new Intent(CourseAssignActivity.this, NewExamActivity.class));
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
					row = li.inflate(R.layout.listitem_course_assign, null);
				}
				((TextView) row.findViewById(R.id.course_assign_list_due_date)).setText(arr[position][0]);
				((TextView) row.findViewById(R.id.course_assign_list_description)).setText(arr[position][1]);
				return row;
			}
		}
		((ListView) findViewById(R.id.course_assign_lv_assignments)).setAdapter(new MyAdapter(this, R.layout.listitem_course_assign, stuff));
		((ListView) findViewById(R.id.course_assign_lv_exams)).setAdapter(new MyAdapter(this, R.layout.listitem_course_assign, stuff));
	}
}
