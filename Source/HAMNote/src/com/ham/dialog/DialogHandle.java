package com.ham.dialog;


import java.util.Calendar;

import com.example.hamnote.R;
import com.ham.activity.DetailNoteActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;


public class DialogHandle {
	public final int DATETIME_DIALOG_ID = 0;
	public final int SONG_DIAGLOG_ID = 1;
	public final int IMAGE_DIAGLOG_ID = 2;
	public final int THEME_ID = 3;
	public final int ABOUT = 4;
	public Bitmap BITMAP_IMAGE;
	private Dialog dialog;
	
	Context context;
	Calendar c = Calendar.getInstance();
	public int vYear, vMonth, vDay;
	public int vHour, vMinute;
	public String themeStyle;
	public String fontStyle;
	public int fontSize;
	public boolean isOk = false;
	private final CharSequence[] listSong = {"Beautiful in white","Because you live","Chờ mưa mang em về"
			,"Em của ngày hôm qua","Song for love","What are words"};
	private int songName;
	AlertDialog.Builder builder;
	public DialogHandle(Context c){
		this.context = c;
	};
	public int getSong(){
		return songName;
	}
	public String getTime(){
		String result = "";
		if(vDay<10){
			result+="0"+Integer.toString(vDay);
		}
		else
			result+=Integer.toString(vDay);
		result+="/";
		
		if(vMonth<10){
			result+="0"+Integer.toString(vMonth);
		}
		else
			result+=Integer.toString(vMonth);
		result+="/";
		
		if(vYear<10){
			result+="0"+Integer.toString(vYear);
		}
		else
			result+=Integer.toString(vYear);
		result+="-";
		
		if(vHour<10){
			result+="0"+Integer.toString(vHour);
		}
		else
			result+=vHour;
		result+=":";
		
		if(vMinute<10){
			result+="0"+Integer.toString(vMinute);
		}
		else
			result+=Integer.toString(vMinute);
		
		return result;
	}
	
	public void setImage(Bitmap i)
	{
		BITMAP_IMAGE = i;
	}
	
	public Dialog DialogProcess(int ID) {
		switch(ID){
		case DATETIME_DIALOG_ID:
			Button btnOK;
			Button btnCancel;
			// datePicker;
			dialog = new Dialog(context);
			dialog.setTitle("Choose your time to alert");
			dialog.setContentView(R.layout.datetimedialog);
			
			btnOK = (Button)dialog.findViewById(R.id.btnOK);
			btnCancel = (Button)dialog.findViewById(R.id.btnCancel);
			final DatePicker datePicker = (DatePicker)dialog.findViewById(R.id.dPicker);
			
			final TimePicker timePicker = (TimePicker)dialog.findViewById(R.id.tPicker);
			
			btnOK.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					vDay = datePicker.getDayOfMonth();
					vMonth = datePicker.getMonth()+1;
					vYear = datePicker.getYear();
					vHour = timePicker.getCurrentHour();
					vMinute = timePicker.getCurrentMinute();
					dialog.dismiss();
					
				}
			});
			btnCancel.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			
			return dialog;
		case SONG_DIAGLOG_ID:
			builder = new AlertDialog.Builder(context);
			builder.setTitle("Choose sound Alert");
			builder.setItems(listSong, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					songName = which;
					//Toast.makeText(context, which,Toast.LENGTH_SHORT);
				}
			});
			return builder.create();
		case THEME_ID:
			Button btnCreate;
			Button btnCancel1;
			final Spinner themeSpinner;
			Spinner fontStyleSpinner;
			Spinner fontSizeSpinner;
			
			dialog = new Dialog(context);
			dialog.setTitle("Setting your theme");
			dialog.setContentView(R.layout.choosethemedialog);
			btnCreate =(Button)dialog.findViewById(R.id.btnCreate);
			themeSpinner = (Spinner)dialog.findViewById(R.id.ChooseTheme);
			fontStyleSpinner = (Spinner)dialog.findViewById(R.id.ChooseFont);
			fontSizeSpinner = (Spinner)dialog.findViewById(R.id.ChooseSize);
			btnCreate.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Log.d("Get string", themeStyle+" "+fontStyle+" "+fontSize);
					dialog.dismiss();
					Intent i = new Intent(context, DetailNoteActivity.class);
		            i.putExtra("update", Long.valueOf(0));
		            i.putExtra("themestyle", themeStyle);
		            i.putExtra("fontstyle", fontStyle);
		            i.putExtra("fontsize", fontSize);
		            context.startActivity(i);
				}
			});
			btnCancel1 = (Button) dialog.findViewById(R.id.btnCancel1);
			btnCancel1.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					
				}
			});
			
			themeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int pos, long id) {
					// TODO Auto-generated method stub
					themeStyle = parent.getItemAtPosition(pos).toString();
					
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					// TODO Auto-generated method stub
					themeStyle = parent.getItemAtPosition(0).toString();
				}
			});
			
			fontStyleSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int pos, long id) {
					// TODO Auto-generated method stub
					fontStyle = parent.getItemAtPosition(pos).toString();
					
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					// TODO Auto-generated method stub
					fontStyle = parent.getItemAtPosition(0).toString();
				}
			});
			
			fontSizeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int pos, long id) {
					// TODO Auto-generated method stub
					fontSize = Integer.valueOf(parent.getItemAtPosition(pos).toString());
					
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					// TODO Auto-generated method stub
					fontSize = Integer.valueOf(parent.getItemAtPosition(0).toString());
				}
			});
			return dialog;
		case IMAGE_DIAGLOG_ID:
			dialog = new Dialog(context);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.imageshow);
			ImageView image_frame = (ImageView) dialog.findViewById(R.id.image_show);
			if(BITMAP_IMAGE!=null)
			{
				Drawable d = new BitmapDrawable(context.getResources(),BITMAP_IMAGE);
				image_frame.setImageDrawable(d);
				dialog.getWindow().setBackgroundDrawable(null);
			}			
			image_frame.setOnClickListener(new View.OnClickListener() {				
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			return dialog;
			
		case ABOUT:
			dialog = new Dialog(context);
			dialog.setTitle("AHM Group");
			dialog.setContentView(R.layout.imageshow);
			ImageView image_about = (ImageView) dialog.findViewById(R.id.image_show);
			image_about.setImageResource(R.drawable.ahm);
			image_about.setOnClickListener(new View.OnClickListener() {				
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			return dialog;
		}
		return null;
	}

}
