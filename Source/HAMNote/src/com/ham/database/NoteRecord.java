package com.ham.database;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NoteRecord {
	public String ID;
	public String TITLE;
	public String CONTENT;
	public String IMAGE;
	public String DATE;
	public int THEMEID;
	public int ISIMPORTANT;
	
	@SuppressLint("SimpleDateFormat")
	public NoteRecord(String title, String content, String img,String date ,int theme, int isImportant){
		String timeStamp = new SimpleDateFormat("yyMMddhhmmssSSSSSS").format(Calendar.getInstance().getTime());
		this.ID = timeStamp;
		this.TITLE = title;
		this.CONTENT = content;
		this.IMAGE = img;
		this.DATE = date;
		this.THEMEID = theme;
		this.ISIMPORTANT = isImportant;
	}
	public NoteRecord(String ID,String title ,String content, String img,String date ,int theme, int isImportant){
		this.ID = ID;
		this.TITLE = title;
		this.CONTENT = content;
		this.IMAGE = img;
		this.DATE = date;
		this.THEMEID = theme;
		this.ISIMPORTANT = isImportant;
	}

}
