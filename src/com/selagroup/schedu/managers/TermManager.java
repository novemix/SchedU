package com.selagroup.schedu.managers;

import com.selagroup.schedu.database.DatabaseHelper;
import com.selagroup.schedu.model.Term;

import android.database.Cursor;

public class TermManager extends Manager<Term> {

	public TermManager(DatabaseHelper iHelper) {
		super(iHelper);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int insert(Term iItem) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void delete(Term iItem) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Term iItem) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Term itemFromCurrentPos(Cursor iCursor) {
		// TODO Auto-generated method stub
		return null;
	}

}
