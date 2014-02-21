package com.ham.activity;

//import com.ham.hamnote.R;

import com.example.hamnote.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class HAMNoteActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hamnote);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detailnote, menu);
        return true;
    }
    
}
