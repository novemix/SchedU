package com.selagroup.schedu.adapters;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.selagroup.schedu.R;
import com.selagroup.schedu.model.Exam;
import com.selagroup.schedu.model.Location;

public class ExamArrayAdapter extends ArrayAdapter<Exam> {
	
	public interface ExamEditListener {
		public void onExamEdit(Exam iExam);
	}
	
	private Context mContext;
	private List<Exam> mExamList;
	private ExamEditListener mEditListener;
	
	public ExamArrayAdapter(Context iContext, int textViewResourceId, List<Exam> iExamList, ExamEditListener iEditListener) {
		super(iContext, textViewResourceId, iExamList);
		mContext = iContext;
		mExamList = iExamList;
		mEditListener = iEditListener;
	}
	
	public View getView(int position, View row, ViewGroup parent) {
		if (row == null) {
			LayoutInflater li = LayoutInflater.from(mContext);
			row = li.inflate(R.layout.adapter_exam_select, null);
		}
		final Exam exam = mExamList.get(position);
		((TextView) row.findViewById(R.id.exam_adapter_tv_date)).setText((new SimpleDateFormat("M/dd")).format(exam.getBlock().getStartTime().getTime()));
		((TextView) row.findViewById(R.id.exam_adapter_tv_desc)).setText(exam.getDescription());
		Location location = exam.getBlock().getLocation();
		String building = location.getBuilding();
		String room = location.getRoom();
		String separator = "".equals(building) || "".equals(room) ? "" : ", ";
		String location_text = building + separator + room;
		((TextView) row.findViewById(R.id.exam_adapter_tv_location)).setText(location_text);
		SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
		Calendar start = exam.getBlock().getStartTime();
		Calendar end = exam.getBlock().getEndTime();
		String time_text = timeFormat.format(start.getTime()).toLowerCase() + " - " + timeFormat.format(end.getTime()).toLowerCase();
		((TextView) row.findViewById(R.id.exam_adapter_tv_time)).setText(time_text);
		
		((Button) row.findViewById(R.id.exam_adapter_btn_edit)).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				mEditListener.onExamEdit(exam);
			}
		});
		return row;
	}
}
