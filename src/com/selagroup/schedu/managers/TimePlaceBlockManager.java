package com.selagroup.schedu.managers;

import android.database.Cursor;

import com.selagroup.schedu.database.DatabaseHelper;
import com.selagroup.schedu.model.TimePlaceBlock;

public class TimePlaceBlockManager extends Manager<TimePlaceBlock> {

	public TimePlaceBlockManager(DatabaseHelper iHelper) {
		super(iHelper);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int insert(TimePlaceBlock iItem) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void delete(TimePlaceBlock iItem) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(TimePlaceBlock iItem) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected TimePlaceBlock itemFromCurrentPos(Cursor iCursor) {
		// TODO Auto-generated method stub
		return null;
	}
}
