package com.ham.database;

public class NoteRecord {
	public int ID;
	public String TITLE;
	public String CONTENT;
	public String IMAGE;
	public String DATE;
	public int THEMEID;
	public int ISIMPORTANT;
	
	public NoteRecord(int id, String title, String content, String img,String date ,int theme, int isImportant){
		this.ID = id;
		this.TITLE = title;
		this.CONTENT = content;
		this.IMAGE = img;
		this.DATE = date;
		this.THEMEID = theme;
		this.ISIMPORTANT = isImportant;
	}

}
