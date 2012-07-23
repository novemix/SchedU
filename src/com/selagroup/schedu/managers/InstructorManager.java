package com.selagroup.schedu.managers;

import android.database.Cursor;

import com.selagroup.schedu.database.DatabaseHelper;
import com.selagroup.schedu.model.Instructor;

public class InstructorManager extends Manager<Instructor> {

	public InstructorManager(DatabaseHelper iHelper) {
		super(iHelper);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int insert(Instructor iItem) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void delete(Instructor iItem) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Instructor iItem) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Instructor itemFromCurrentPos(Cursor iCursor) {
		// TODO Auto-generated method stub
		return null;
	}

}
