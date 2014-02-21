package com.ham.database;

public class ThemeRecord {
	public int ID;
	public String BACKGROUND;
	public String FONT;
	public int SIZE;
	
	public ThemeRecord(int id, String background, String font, int size){
		this.ID = id;
		this.BACKGROUND = background;
		this.FONT = font;
		this.SIZE =size;
	}

}
