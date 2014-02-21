package com.ham.activity;


import java.io.File;


import com.example.hamnote.R;
import com.ham.database.*;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.Menu;

public class HAMNoteActivity extends Activity {

	@SuppressLint("SdCardPath")
	private File dbFile=new File("/data/data/com.example.hamnote/databases/NoteDatabase.db");
	private DatabaseAdapter database = new DatabaseAdapter(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hamnote);
		if(!dbFile.exists()){
			database.open();
			database.Init();
			database.close();
		}
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detailnote, menu);
        return true;
    }
    
}
