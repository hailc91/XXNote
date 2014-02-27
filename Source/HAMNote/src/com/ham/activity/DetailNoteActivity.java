package com.ham.activity;


//import java.io.File;
//import java.util.ArrayList;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

import com.example.hamnote.R;
import com.ham.database.*;

import android.os.Bundle;
import android.annotation.SuppressLint;
//import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.ham.dialog.DialogHandle;
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
	Random randomGenerator = new Random();
	private Long noteid;
	private EditText content;
	private EditText title;
	private TextView titleRemain;
	private NoteRecord note = null;
	private int maxLength_title = 40;
	private Menu detailMenu;
	private boolean importantTurnedOn = false;
	private boolean chooseTimer = false;
	private  DialogHandle dialogHandle = new DialogHandle(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_hamnote);
        
        content = (EditText) findViewById(R.id.detail_content);
        title = (EditText) findViewById(R.id.detail_title);
        titleRemain = (TextView) findViewById(R.id.detail_title_remain_character);
        
        
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
        this.detailMenu = menu;
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
    	
    	title.setText(note.TITLE);
    	content.setText(note.CONTENT);    	
    	
    	titleRemain.setText(Integer.toString(maxLength_title - note.TITLE.length()));
    	title.addTextChangedListener(new TextWatcher(){

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				titleRemain.setText(Integer.toString(maxLength_title - title.getText().toString().length()));				
			}
    		
    	});
    	
    	//Toast.makeText(this, "Ok", Toast.LENGTH_SHORT).show();
    }
    private void SetAlert(int ID){
    	Intent i = new Intent("com.ham.activity.AlertActivity");
    	i.putExtra("SongID", dialogHandle.getSong());
    	i.putExtra("CODE", ID);
		PendingIntent operation = PendingIntent.getActivity(getBaseContext(), ID, i, Intent.FLAG_ACTIVITY_NEW_TASK);
		AlarmManager alarmManager = (AlarmManager) getBaseContext().getSystemService(ALARM_SERVICE);
		GregorianCalendar calendar;
		long alarm_time;
		if(chooseTimer == true){
			calendar = new GregorianCalendar(dialogHandle.vYear,dialogHandle.vMonth-1,dialogHandle.vDay, dialogHandle.vHour, dialogHandle.vMinute);
			alarm_time = calendar.getTimeInMillis();
		}
		else{
			alarm_time = System.currentTimeMillis() + 604800000; //Default 1 week
		}
        alarmManager.set(AlarmManager.RTC_WAKEUP  , alarm_time , operation);
        Toast.makeText(getBaseContext(), dialogHandle.getTime(),Toast.LENGTH_SHORT).show();
        Toast.makeText(getBaseContext(), "Alarm is set successfully",Toast.LENGTH_SHORT).show();
    }
    @SuppressLint("SimpleDateFormat")
	private void saveNote()
    {
    	// update current note info
    	//String nID = "";
    	String nTitle = "";
    	String nContent = "";
    	String nImage = "";
    	String nDate = "";
    	int nThemeID = 0;
    	int nIsImportant = 0;
    	
    	String timeStamp = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
    	
    	nTitle = title.getText().toString();
    	nContent = content.getText().toString();    	
    	nDate = timeStamp; 	
    	
    	NoteRecord nNote = new NoteRecord(nTitle, nContent, nImage, nDate, nThemeID, nIsImportant);
    	
    	database.UpdateToNoteTbl(noteid.toString(), nNote);
    	//Set if it important
    	if(importantTurnedOn){
    		int id = randomGenerator.nextInt(10000);
    		if(chooseTimer)
    			database.InsertToImportantTbl(new ImportantRecord(noteid.toString(),dialogHandle.getTime(), Integer.toString(dialogHandle.getSong()),id));
    		else
    		{
    			Calendar cal = Calendar.getInstance();
    			cal.add(Calendar.DATE, 7);
    			String time = new SimpleDateFormat("dd/MM/yyyy-hh:mm").format(cal.getTime());
    			database.InsertToImportantTbl(new ImportantRecord(noteid.toString(),time, Integer.toString(dialogHandle.getSong()),id));
    		}
    		SetAlert(id);
    		chooseTimer = false;
    		
    	}
    }
    
    public void onButtonClicked(View v)
	{
		if(v.getId() == R.id.detail_saveButton)
		{
			saveNote();		
			
			Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show();
			database.close();
			this.finish();
		}
	}
    
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        /*MenuItem item = menu.findItem(R.id.note_num);
        item.setTitle(Integer.toString(noteNum));*/
        return true;
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        return dialogHandle.DialogProcess(id);
    }
    @SuppressWarnings("deprecation")
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
 
        switch (item.getItemId()) {
 
        case R.id.detailmenu_star:
        	if(importantTurnedOn)
        	{
        		MenuItem iMusic = detailMenu.findItem(R.id.detailmenu_music);
            	MenuItem iTimer = detailMenu.findItem(R.id.detailmenu_timer);
            	iMusic.setIcon(R.drawable.music_icon_grey_36x36);
            	iTimer.setIcon(R.drawable.timer_icon_grey_36x36);
            	item.setIcon(R.drawable.star_grey_36x36);  
        	}
        	else
        	{
        		MenuItem iMusic = detailMenu.findItem(R.id.detailmenu_music);
            	MenuItem iTimer = detailMenu.findItem(R.id.detailmenu_timer);
            	iMusic.setIcon(R.drawable.music_icon_green_36x36);
            	iTimer.setIcon(R.drawable.timer_icon_green_36x36);
            	item.setIcon(R.drawable.star_yellow_36x36);            	
        	}
        	importantTurnedOn = !importantTurnedOn;
            break;
            
        case R.id.detailmenu_music:
        	showDialog(dialogHandle.SONG_DIAGLOG_ID);
            break;
            
        case R.id.detailmenu_timer:
        	chooseTimer = true;
        	showDialog(dialogHandle.DATETIME_DIALOG_ID);
            break;
            
        default:
        	break;        
        }
        return true;
    }
}
