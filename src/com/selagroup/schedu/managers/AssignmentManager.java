package com.selagroup.schedu.managers;

import android.database.Cursor;

import com.selagroup.schedu.database.DatabaseHelper;
import com.selagroup.schedu.model.Assignment;

public class AssignmentManager extends Manager<Assignment> {

	public AssignmentManager(DatabaseHelper iHelper) {
		super(iHelper);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int insert(Assignment iItem) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void delete(Assignment iItem) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Assignment iItem) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Assignment itemFromCurrentPos(Cursor iCursor) {
		// TODO Auto-generated method stub
		return null;
	}
}
