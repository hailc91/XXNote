package com.ham.activity;


import java.io.File;
import java.util.ArrayList;


import com.example.hamnote.R;
import com.ham.database.*;
import com.ham.dialog.DialogHandle;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
//import android.widget.Toast;

public class HAMNoteActivity extends Activity {

	@SuppressLint("SdCardPath")
	private File dbFile=new File("/data/data/com.example.hamnote/databases/NoteDatabase.db");
	private DatabaseAdapter database = new DatabaseAdapter(this);
	private GridView gridView = null;
	private ArrayList<NoteRecord> listNote = null;
	private int noteNum = 0;
	private boolean gridViewCalled;
	private DialogHandle dialogHandle = new DialogHandle(this);
	private Menu hamMenu;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hamnote);
        
		if(!dbFile.exists()){
			database.open();
			database.Init();
			
			// move database.close() to onDestroy()/onStop()
			//database.close();
		} else {
			// delete & re-create database - use for testing
			//dbFile.delete();
			database.open();
			//database.Init();
			
		}		
		
		gridViewCalled = true;
		createGridView();	
		
    }
    @SuppressWarnings("deprecation")
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        case R.id.note_delete:
        	gridViewCalled = false;
        	Intent k = new Intent (getBaseContext(),DeleteNoteActivity.class);
        	startActivity(k);
        	return true;
        case R.id.note_add:
        	gridViewCalled = false;
        	showDialog(dialogHandle.THEME_ID);
            return true;
        case R.id.note_about:
        	dialogHandle.DialogProcess(dialogHandle.ABOUT).show();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.hamnote, menu);
        this.hamMenu = menu;
        return true;
    }
    
    @Override
    protected void onResume()
    {
    	super.onResume();
    	if(!gridViewCalled) {
    		database.open();
    		createGridView();
    		hamMenu.findItem(R.id.note_num).setTitle(Integer.toString(noteNum));
    	}
    }
    
    @Override
    public void onStop()
    {
    	super.onStop();
    	database.close();
    }
    
    private void createGridView()	// display notes on grid view
    {  	
    	gridView = (GridView) findViewById(R.id.gridView);
    	listNote = database.GetListNote();
		noteNum = listNote.size();
    	gridView.setAdapter(new IconAdapter(this, listNote));    
    	
    	gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				database.close();
				gridViewCalled = false;
				Intent i = new Intent(getBaseContext(), DetailNoteActivity.class);
                i.putExtra("noteid", arg3);
                i.putExtra("update", 1);
                startActivity(i);
			}
        }); 	
    	
    }
    
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem item = menu.findItem(R.id.note_num);
        item.setTitle(Integer.toString(noteNum));
        return true;
    }  
    @Override
    protected Dialog onCreateDialog(int id) {
        return dialogHandle.DialogProcess(id);
    }
      
}
