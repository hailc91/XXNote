package com.ham.activity;


//import java.io.File;
//import java.util.ArrayList;


import com.example.hamnote.R;
import com.ham.database.*;

import android.os.Bundle;
//import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
/*import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;*/
import android.widget.TextView;
//import android.widget.Toast;

public class DetailNoteActivity extends Activity {
	private DatabaseAdapter database = new DatabaseAdapter(this);
	//private GridView gridView = null;
	//private ArrayList<NoteRecord> listNote = null;
	//private int noteNum = 0;
	private Long noteid;
	private NoteRecord note = null;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_hamnote);
        
        Intent i = getIntent();
        noteid = (Long) i.getExtras().getLong("noteid");
        Log.d("ID get", noteid.toString());
        
        database.open();
        
		createDetail();
		
		/*
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				
                Intent i = new Intent(getApplicationContext(), HAMNoteActivity.class);
                i.putExtra("note", arg2);
                startActivity(i);
			}
        });*/		
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detailnote, menu);
        return true;
    }
    
    @Override
    public void onStop()
    {
    	super.onStop();
    	database.close();
    }
    
    /*@Override
    public void onDestroy()
    {
    	database.close();
    }*/
    
    private void createDetail()	// display detail content
    {   	
    	note = database.GetNoteRecord(noteid.toString());
    	TextView tv = (TextView) findViewById(R.id.test_detail);
    	tv.setText(note.CONTENT);
    	//Toast.makeText(this, "Ok", Toast.LENGTH_SHORT).show();
    }
    
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        /*MenuItem item = menu.findItem(R.id.note_num);
        item.setTitle(Integer.toString(noteNum));*/
        return true;
    }
    
}
