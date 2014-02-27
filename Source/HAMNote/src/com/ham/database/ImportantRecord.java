package com.ham.database;

public class ImportantRecord {
	public String ID;
	public String TIMEALERT;
	public String SOUND;
	public int CODE;
	
	public ImportantRecord(String id, String time, String sound,int code){
		this.ID =id;
		this.TIMEALERT = time;
		this.SOUND = sound;
		this.CODE = code;
	}
	
}
