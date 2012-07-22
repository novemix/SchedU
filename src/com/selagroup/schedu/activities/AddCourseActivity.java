/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */

package com.selagroup.schedu.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.selagroup.schedu.R;
/**
 * The Class AddCourseActivity.
 */
public class AddCourseActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addcourse);
		
		String[] foo = new String[] {"one", "two", "three", "four", "five", "six", "seven"};
		ListView lv = (ListView) findViewById(R.id.addcourse_lv_schedule);
		lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, foo));
		
		((Button) findViewById(R.id.addcourse_btn_add_time)).setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				startActivity(new Intent(AddCourseActivity.this, AddTimeActivity.class));
			}
		});
		
		startActivity(new Intent(this, CalendarActivity.class));
	}
	
//	public class myDialog extends Dialog {
//
//		public myDialog(Context context) {
//			super(context);
//			requestWindowFeature(Window.FEATURE_NO_TITLE);
//			setContentView(R.layout.dialog_addtime);
//		}
//		
//	}
}
