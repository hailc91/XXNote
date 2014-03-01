package com.ham.dialog;


import java.util.Calendar;

import com.example.hamnote.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TimePicker;


public class DialogHandle {
	public final int DATETIME_DIALOG_ID = 0;
	public final int SONG_DIAGLOG_ID = 1;
	public final int IMAGE_DIAGLOG_ID = 2;
	public Bitmap BITMAP_IMAGE;
	private Dialog dialog;
	
	Context context;
	Calendar c = Calendar.getInstance();
	public int vYear, vMonth, vDay;
	public int vHour, vMinute;
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
			
		case IMAGE_DIAGLOG_ID:
			dialog = new Dialog(context);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.imageshow);
			ImageView image_frame = (ImageView) dialog.findViewById(R.id.image_show);
			Drawable d = new BitmapDrawable(context.getResources(),BITMAP_IMAGE);
			//image_frame.setBackground(d);
			image_frame.setImageDrawable(d);
			dialog.getWindow().setBackgroundDrawable(null);
			image_frame.setOnClickListener(new View.OnClickListener() {				
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
