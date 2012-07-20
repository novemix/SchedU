/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */

package com.selagroup.schedu.activities;

import com.selagroup.schedu.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
/**
 * The Class AddCourseActivity.
 */
public class AddCourseActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addcourse);
		
		((Button) findViewById(R.id.addcourse_btn_add_time)).setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				myDialog d = new myDialog(AddCourseActivity.this);
				d.show();
				
			}
		});
	}
	
	public class myDialog extends Dialog {

		public myDialog(Context context) {
			super(context);
			setContentView(R.layout.dialog_addtime);
		}
		
	}
}
