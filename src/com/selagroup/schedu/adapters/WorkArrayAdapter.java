package com.selagroup.schedu.adapters;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import com.selagroup.schedu.R;
import com.selagroup.schedu.ScheduApplication;
import com.selagroup.schedu.model.Assignment;

import android.app.Application;
import android.app.DatePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class WorkArrayAdapter extends ArrayAdapter<Assignment> {
	
	public interface WorkEditListener {
		public void onWorkEdit(Assignment iWorkItem);
		public void onWorkDelete(Assignment iWorkItem);
	}
	
	private Context mContext;
	private List<Assignment> mWorkList;
	private WorkEditListener mEditListener;
	
	public WorkArrayAdapter(Context iContext, int textViewResourceId, List<Assignment> iWorkList, WorkEditListener iEditListener) {
		super(iContext, textViewResourceId, iWorkList);
		mContext = iContext;
		mWorkList = iWorkList;
		mEditListener = iEditListener;
	}

	public View getView(int position, View row, ViewGroup parent) {
		if (row == null) {
			LayoutInflater li = LayoutInflater.from(mContext);
			row = li.inflate(R.layout.adapter_work_select, null);
		}
		final Assignment workItem = mWorkList.get(position);
		((TextView) row.findViewById(R.id.work_adapter_tv_due)).setText((new SimpleDateFormat("MM/dd")).format(workItem.getDueDate().getTime()));
		((TextView) row.findViewById(R.id.work_adapter_tv_desc)).setText(workItem.getName());
		((ImageButton) row.findViewById(R.id.work_adapter_btn_delete)).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				mEditListener.onWorkDelete(workItem);
			}
		});
		return row;
	}
}
