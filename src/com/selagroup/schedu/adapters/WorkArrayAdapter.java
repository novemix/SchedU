package com.selagroup.schedu.adapters;

import java.util.Calendar;
import java.util.List;

import com.selagroup.schedu.R;
import com.selagroup.schedu.model.Assignment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class WorkArrayAdapter extends ArrayAdapter<Assignment> {

	private Context mContext;
	private List<Assignment> mWorkList;
	
	public interface WorkEditListener {
		public void onWorkEdit(Assignment iWork);
		public void onWorkDelete(Assignment iWork);
	}
	
	public WorkArrayAdapter(Context iContext, int textViewResourceId, List<Assignment> iWorkList) {
		super(iContext, textViewResourceId, iWorkList);
		mContext = iContext;
		mWorkList = iWorkList;
	}

	public View getView(int position, View row, ViewGroup parent) {
		if (row == null) {
			LayoutInflater li = LayoutInflater.from(mContext);
			row = li.inflate(R.layout.adapter_work_select, null);
		}
		row.findViewById(R.id.work_adapter_btn_due).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Calendar now = Calendar.getInstance();
				DatePickerDialog dlg = new DatePickerDialog(mContext, null,
						now.get(Calendar.YEAR),
						now.get(Calendar.MONTH),
						now.get(Calendar.DAY_OF_MONTH));
				dlg.show();
			}
		});
		return row;
	}
}
