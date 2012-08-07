package com.selagroup.schedu.adapters;

import java.util.Calendar;
import java.util.List;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.selagroup.schedu.R;
import com.selagroup.schedu.model.Term;

public class TermArrayAdapter extends ArrayAdapter<Term> {
	private Context mContext;
	private List<Term> mTerms;
	private int mEditIndex = -1;
	private boolean mEditMode = false;
	private static AlertDialog.Builder mDeleteAlert;
	private AlertDialog mDeleteAlertDialog;

	private static class ViewHolder {
		private TextView term_adapter_tv_select;
		private Button term_btn_delete;
		private Button term_btn_start;
		private Button term_btn_end;
	}

	public TermArrayAdapter(Context iContext, int textViewResourceId, List<Term> iTerms) {
		super(iContext, textViewResourceId, iTerms);
		mContext = iContext;
		mTerms = iTerms;
		mDeleteAlert = new AlertDialog.Builder(mContext);
	}

	public void setEditMode(boolean iEditMode) {
		mEditMode = iEditMode;
	}

	public void setEditIndex(int iEditIndex) {
		mEditIndex = iEditIndex;
	}

	@Override
	public View getView(int position, View row, ViewGroup parent) {
		ViewHolder holder = null;

		// Only get the items from the layout once
		if (row == null) {
			LayoutInflater li = LayoutInflater.from(mContext);
			row = li.inflate(R.layout.adapter_term_select, null);
			holder = new ViewHolder();
			holder.term_adapter_tv_select = (TextView) row.findViewById(R.id.term_adapter_tv_select);
			holder.term_btn_delete = (Button) row.findViewById(R.id.term_btn_delete);
			holder.term_btn_start = (Button) row.findViewById(R.id.term_btn_start);
			holder.term_btn_end = (Button) row.findViewById(R.id.term_btn_end);
			row.setTag(holder);
		} else {
			holder = (ViewHolder) row.getTag();
		}

		// Get the item
		final Term term = mTerms.get(position);
		if (term != null) {
			boolean showEdit = mEditIndex == position;
			holder.term_adapter_tv_select.setText(term.toString());
			holder.term_btn_delete.setTag(term);
			holder.term_btn_delete.setVisibility(mEditMode ? View.VISIBLE : View.GONE);
			holder.term_btn_start.setVisibility(mEditMode && showEdit ? View.VISIBLE : View.GONE);
			holder.term_btn_end.setVisibility(mEditMode && showEdit ? View.VISIBLE : View.GONE);

			if (mEditMode) {
				holder.term_btn_delete.setOnClickListener(new OnClickListener() {
					public void onClick(View view) {
						final Term term = (Term) view.getTag();
						mDeleteAlert.setTitle("Are you sure you want to delete this term?");
						mDeleteAlert.setPositiveButton("Delete", new AlertDialog.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								TermArrayAdapter.this.remove(term);
								if (TermArrayAdapter.this.isEmpty()) {
									TermArrayAdapter.this.add(null);
								}
							}
						});
						mDeleteAlert.setNegativeButton("Cancel", new AlertDialog.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								mDeleteAlertDialog.dismiss();
							}
						});
						mDeleteAlertDialog = mDeleteAlert.create();
						mDeleteAlertDialog.show();
					}
				});
				if (showEdit) {
					holder.term_btn_start.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							showStartDateDialog(term.getStartDate(), term);
						}
					});
					holder.term_btn_end.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							showEndDateDialog(term.getEndDate(), term);
						}
					});
				}
			}
		}
		else {
			holder.term_adapter_tv_select.setText("No terms. Create a term first.");
			holder.term_btn_delete.setVisibility(View.GONE);
			holder.term_btn_start.setVisibility(View.GONE);
			holder.term_btn_end.setVisibility(View.GONE);
		}

		return row;
	}

	@Override
	public View getDropDownView(int position, View row, ViewGroup parent) {
		ViewHolder holder = null;

		// Only get the items from the layout once
		if (row == null) {
			LayoutInflater li = LayoutInflater.from(mContext);
			row = li.inflate(R.layout.adapter_term_select, null);
			holder = new ViewHolder();
			holder.term_adapter_tv_select = (TextView) row.findViewById(R.id.term_adapter_tv_select);
			row.setTag(holder);
		} else {
			holder = (ViewHolder) row.getTag();
		}

		// Get the item
		Term term = mTerms.get(position);
		String displayStr = null;
		if (term != null) {
			displayStr = term.toString();
		} else {
			displayStr = "Add New Term";
		}
		holder.term_adapter_tv_select.setText(displayStr);

		return row;
	}

	private void showStartDateDialog(Calendar initDate, final Term iTerm) {
		if (initDate == null) {
			initDate = Calendar.getInstance();
		}
		// Set up start date dialog
		DatePickerDialog startDateDialog = new DatePickerDialog(mContext, new OnDateSetListener() {
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
				iTerm.setStartDate(newDate);
				notifyDataSetChanged();
			}
		}, initDate.get(Calendar.YEAR), initDate.get(Calendar.MONTH), initDate.get(Calendar.DAY_OF_MONTH));
		startDateDialog.setTitle("Set Term Start Date");
		startDateDialog.show();
	}

	private void showEndDateDialog(Calendar initDate, final Term iTerm) {
		if (initDate == null) {
			initDate = Calendar.getInstance();
		}
		// Set up end date dialog
		DatePickerDialog endDateDialog = new DatePickerDialog(mContext, new OnDateSetListener() {
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
				iTerm.setEndDate(newDate);
				notifyDataSetChanged();
			}
		}, initDate.get(Calendar.YEAR), initDate.get(Calendar.MONTH), initDate.get(Calendar.DAY_OF_MONTH));
		endDateDialog.setTitle("Set Term End Date");
		endDateDialog.show();
	}
}
