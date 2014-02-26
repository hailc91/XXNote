package com.ham.database;

import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseAdapter {
	private static final String DATABASE_NAME = "NoteDatabase.db";
	private static final int DATABASE_VERSION = 1;
	
	private SQLiteDatabase database;
	private final Context context;
	private DatabaseHelper dbHelper;
	
	public DatabaseAdapter(Context c){
		context =c;
		dbHelper = new DatabaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	public void Init(){
		//Insert complete default theme
		InsertCompleteThemeTbl(new ThemeRecord(33, "bg1.jpg", "Arial", 12));
		
		// Use for test only, DELETE these lines later
		NoteRecord note0 = new NoteRecord("1", "Ngủ trưa", "Ngủ từ 12h tới 22h tối", "Image: link 0", "20140224", 33, 0);
		NoteRecord note1 = new NoteRecord("2", "Hẹn hò", "Cafe đứng uống, không được ngồi :3", "", "20140220", 1, 0);
		NoteRecord note2 = new NoteRecord("3", "Đi ngủ", "Không xác định giờ thức giấc", "", "20140223", 2, 1);
		NoteRecord note3 = new NoteRecord("4", "Về quê", "100km", "", "20140220", 2, 1);
		NoteRecord note4 = new NoteRecord("5", "Cúp học môn Mo...., gặp girl xinh @@!", "^___^", "", "20140224", 2, 1);
		InsertToNoteTbl(note0);
		InsertToNoteTbl(note1);
		InsertToNoteTbl(note2);		
		InsertToNoteTbl(note3);
		InsertToNoteTbl(note4);	
	}
	
	public DatabaseAdapter open() throws SQLException{
		database = dbHelper.getWritableDatabase();
		return this;
	}
	
	public void close(){
		database.close();
	}
	/*Theme Table*/
	//Theme just create new, not need to update template
	public void InsertCompleteThemeTbl(ThemeRecord r){
		ContentValues values = new ContentValues();
		values.put("ID", r.ID);
		values.put("BACKGROUND", r.BACKGROUND);
		values.put("FONT", r.FONT);
		values.put("SIZE", r.SIZE);
		database.insert("THEMETBL", null, values);
	}
	public ThemeRecord GetThemeRecord(int ID){
		Cursor c = database.query("THEMETBL", null, "ID=?",new String[]{Integer.toString(ID)} , null, null,null);
		c.moveToFirst();
		if(c.isAfterLast() == false){
			return new ThemeRecord(c.getInt(0), c.getString(1), c.getString(2), c.getInt(3));
		}
		return null;
	}
	public void DeleteThemeRecord(int ID){
		database.delete("THEMETBL", "ID=?", new String[]{Integer.toString(ID)});
	}
	/* End Theme table*/
	
	
	/*Note Table*/
	public void InsertToNoteTbl(NoteRecord r){
		ContentValues values = new ContentValues();
		values.put("ID", r.ID);
		values.put("TITLE", r.TITLE);
		values.put("CONTENT", r.CONTENT);
		values.put("IMAGE", r.IMAGE);
		values.put("DATE", r.DATE);
		values.put("THEMEID", r.THEMEID);
		values.put("ISIMPORTANT", r.ISIMPORTANT);
		database.insert("NOTETBL", null, values);
	}
	//Use with value type: Text
	public void UpdateToNoteTbl(String ID, String ColumnName, String newValue ){
		ContentValues values = new ContentValues();
		values.put(ColumnName, newValue);
		database.update("NOTETBL", values, "ID=?", new String[]{ID});
	}
	//Use with value type: integer
	public void UpdateToNoteTbl(String ID, String ColumnName, int newValue ){
		ContentValues values = new ContentValues();
		values.put(ColumnName, newValue);
		database.update("NOTETBL", values, "ID=?", new String[]{ID});
	}
	//Use to update all data
	public void UpdateToNoteTbl(String ID,NoteRecord r ){
		ContentValues values = new ContentValues();
		values.put("ID", r.ID);
		values.put("TITLE", r.TITLE);
		values.put("CONTENT", r.CONTENT);
		values.put("IMAGE", r.IMAGE);
		values.put("DATE", r.DATE);
		values.put("THEMEID", r.THEMEID);
		values.put("ISIMPORTANT", r.ISIMPORTANT);
		database.update("NOTETBL", values, "ID=?", new String[]{ID});
	}
	//Delete by ID
	public void DeleteRecordFromNoteTbl(String ID){
		database.delete("NOTETBL", "ID=?", new String[]{ID});
	}
	//Get all from Note and store to list SORT by date
	public ArrayList<NoteRecord> GetListNote(){
		ArrayList<NoteRecord> result = new ArrayList<NoteRecord>();
		Cursor c= database.query("NOTETBL", null, null, null, null, null,"DATE");
		c.moveToFirst();
		while(c.isAfterLast() == false){
			int THEMEID, ISIMPORTANT;
			String TITLE, CONTENT, IMAGE, DATE, ID;
			ID = c.getString(0);
			TITLE = c.getString(1);
			CONTENT = c.getString(2);
			IMAGE = c.getString(3);
			DATE = c.getString(4);
			THEMEID = c.getInt(5);
			ISIMPORTANT = c.getInt(6);
			result.add(new NoteRecord(ID, TITLE, CONTENT, IMAGE, DATE, THEMEID, ISIMPORTANT));
			c.moveToNext();
		}
		return result;
	}
	//Get 1 note record
	public NoteRecord GetNoteRecord(String ID){
		Cursor c= database.query("NOTETBL", null, "ID=?",new String[]{ID} , null, null,null);
		c.moveToFirst();
		if(c.isAfterLast() == false){
			return new NoteRecord(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getInt(5), c.getInt(6));
		}
		//not safe
		return null;
	}
	/* End Note table*/
	
	/* Important table*/
	public void InsertToImportantTbl(ImportantRecord r){
		ContentValues values = new ContentValues();
		values.put("ID", r.ID);
		values.put("TIMEALERT", r.TIMEALERT);
		values.put("SOUND", r.SOUND);
		database.insert("IMPORTANTTBL", null, values);
	}
	public void UpdateToImportantTbl(String ID, String colName, String newValue){
		ContentValues values = new ContentValues();
		values.put(colName, newValue);
		database.update("IMPORTANTTBL", values, "ID=?", new String[]{ID});
	}
	public void DeleteImportantRecord(String ID){
		database.delete("IMPORTANTTBL", "ID=?", new String[]{ID});
	}
	public ImportantRecord GetImportantRecord(String ID){
		Cursor c= database.query("IMPORTANTTBL", null, "ID=?",new String[]{ID} , null, null,null);
		c.moveToFirst();
		if(c.isAfterLast() == false){
			return new ImportantRecord(c.getString(0),c.getString(1),c.getString(2));
		}
		//not safe
		return null;
	}
	/* End Important table */
}
