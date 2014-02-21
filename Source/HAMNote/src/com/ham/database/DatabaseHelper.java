package com.ham.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{
	//Table Note: Column: ID, TITLE, CONTENT, IMAGE, DATE, THEMEID, ISIMPORTANT
	private static final String TBL_NOTE = "NOTETBL ";
	private static final String NOTE_ID ="ID INTEGER PRIMARY KEY NOT NULL";
	private static final String NOTE_TITLE ="TITLE TEXT";
	private static final String NOTE_CONTENT ="CONTENT TEXT";
	private static final String NOTE_IMAGE ="IMAGE TEXT";
	private static final String NOTE_DATE ="DATE TEXT";
	private static final String NOTE_THEMEID ="THEMEID INTEGER";
	private static final String NOTE_ISIMPORTANT ="ISIMPORTANT INTEGER CHECK(ISIMPORTANT=0 OR ISIMPORTANT = 1)";
	private static final String CONSTRAINT1 = "FOREIGN KEY(THEMEID) REFERENCES THEMETBL(ID) ON DELETE CASCADE ";
	
	private static final String CREATE_TBL_NOTE = "CREATE TABLE " + TBL_NOTE + "( " + NOTE_ID + ", " + NOTE_TITLE + ", " + 
	NOTE_CONTENT+", " + NOTE_IMAGE + ", " + NOTE_DATE + ", " + NOTE_THEMEID + ", " + NOTE_ISIMPORTANT + ", "+ 
			CONSTRAINT1 + ")";
	
	
	//Table Theme: Column: ID, BACKGROUND, FONT, SIZE
	private static final String TBL_THEME = "THEMETBL";
	private static final String THEME_ID = "ID INTEGER PRIMARY KEY NOT NULL";
	private static final String THEME_BACKGROUND = "BACKGROUND TEXT";
	private static final String THEME_FONT = "FONT TEXT";
	private static final String THEME_SIZE = "SIZE INTEGER";
	
	private static final String CREATE_TBL_THEME = "CREATE TABLE " + TBL_THEME + "( "+ THEME_ID + ", "
			+ THEME_BACKGROUND + ", " + THEME_FONT + ", " + THEME_SIZE + " )" ;
	
	//Table Important: Column: ID, TIMEALERT, SOUND
	private static final String TBL_IMPORTANT = "IMPORTANTTBL";
	private static final String IMPORTANT_ID = "ID INTEGER PRIMARY KEY NOT NULL ";
	private static final String IMPORTANT_TIMEALERT = "TIMEALERT TEXT ";
	private static final String IMPORTANT_SOUND = "SOUND TEXT ";
	private static final String CONSTRAINT2 = "FOREIGN KEY(ID) REFERENCES NOTETBL(ID) ON DELETE CASCADE ";
	
	private static final String CREATE_TBL_IMPORTANT = "CREATE TABLE " + TBL_IMPORTANT + "( "+IMPORTANT_ID + ", "
			+ IMPORTANT_TIMEALERT + ", " + IMPORTANT_SOUND +", " + CONSTRAINT2 + " )";
	
	
	public DatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TBL_THEME);
		db.execSQL(CREATE_TBL_NOTE);
		db.execSQL(CREATE_TBL_IMPORTANT);
		
	}
	private void dropTable(SQLiteDatabase db ,String tblName){
		String sql = "DROP TABLE IF EXISTS " + tblName;
		db.execSQL(sql);
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		dropTable(db,TBL_THEME);
		dropTable(db,TBL_NOTE);
		dropTable(db,TBL_IMPORTANT);
		onCreate(db);
		
	}
	
}
