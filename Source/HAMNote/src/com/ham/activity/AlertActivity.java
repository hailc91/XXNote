package com.ham.activity;

import com.ham.dialog.DialogAlert;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class AlertActivity extends FragmentActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);	
		DialogAlert alert = new DialogAlert();
		int songID,CODE;
		Intent i = getIntent();
		songID = i.getExtras().getInt("SongID");
		CODE = i.getExtras().getInt("CODE");
		Bundle args = new Bundle();
		args.putInt("SongID", songID);
		args.putInt("CODE", CODE);
		alert.setArguments(args);
		alert.show(getSupportFragmentManager(),"Demo");	
	}

}
