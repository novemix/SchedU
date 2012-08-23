package com.selagroup.schedu.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.selagroup.schedu.R;
import com.selagroup.schedu.model.TimePlaceBlock;

public class CourseBlockAdapter extends ArrayAdapter<TimePlaceBlock> {
	public interface BlockDeleteListener {
		public void onDelete(TimePlaceBlock iBlock);
	}

	private BlockDeleteListener mDeleteListener;

	private Context mContext;
	private List<TimePlaceBlock> mBlocks;

	public CourseBlockAdapter(Context iContext, int textViewResourceId, List<TimePlaceBlock> iBlocks, BlockDeleteListener iDeleteListener) {
		super(iContext, textViewResourceId, iBlocks);
		mContext = iContext;
		mBlocks = iBlocks;
		mDeleteListener = iDeleteListener;
	}

	private static class ViewHolder {
		private TextView addcourse_tv_blockInfo;
		private ImageView addcourse_btn_blockDelete;
	}

	@Override
	public View getView(int position, View row, ViewGroup parent) {
		ViewHolder tmpHolder = null;

		// Only get the items from the layout once
		if (row == null) {
			LayoutInflater li = LayoutInflater.from(mContext);
			row = li.inflate(R.layout.adapter_addcourse_block, null);
			tmpHolder = new ViewHolder();
			tmpHolder.addcourse_tv_blockInfo = (TextView) row.findViewById(R.id.addcourse_tv_blockInfo);
			tmpHolder.addcourse_btn_blockDelete = (ImageView) row.findViewById(R.id.addcourse_btn_blockDelete);
			row.setTag(tmpHolder);
		} else {
			tmpHolder = (ViewHolder) row.getTag();
		}
		final TimePlaceBlock block = mBlocks.get(position);
		final ViewHolder holder = tmpHolder;

		if (block != null) {
			holder.addcourse_tv_blockInfo.setText(block.toString());

			holder.addcourse_btn_blockDelete.setOnClickListener(new OnClickListener() {
				public void onClick(View view) {
					if (mDeleteListener != null) {
						mDeleteListener.onDelete(block);
					}
				}
			});
		}
		else {
			holder.addcourse_tv_blockInfo.setText("No times for this course.");
			holder.addcourse_btn_blockDelete.setVisibility(View.GONE);
		}

		return row;
	}
}
