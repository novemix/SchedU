package com.selagroup.schedu.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.selagroup.schedu.R;
import com.selagroup.schedu.model.Term;

public class TermArrayAdapter extends ArrayAdapter<Term> {
	private Context mContext;
	private List<Term> mTerms;

	private static class ViewHolder {
		TextView term_adapter_tv_select;
	}

	public TermArrayAdapter(Context iContext, int textViewResourceId, List<Term> iTerms) {
		super(iContext, textViewResourceId, iTerms);
		mContext = iContext;
		mTerms = iTerms;
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
			displayStr = " - No Term Selected -";
		}
		holder.term_adapter_tv_select.setText(displayStr);

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
}
