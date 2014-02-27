package com.ham.dialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.ham.database.*;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class RestartReceiver extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent intent) {
		ArrayList<ImportantRecord> list;
		DatabaseAdapter database = new DatabaseAdapter(context);
		database.open();
		list = database.GetAllImportant();
		
		for(int j=0;j<list.size();j++){
			String date = list.get(j).TIMEALERT;
			int day = Integer.valueOf(date.substring(0, 2));
			int month = Integer.valueOf(date.substring(3, 5));
			int year = Integer.valueOf(date.substring(6, 10));
			int hour = Integer.valueOf(date.substring(11, 13));
			int minute = Integer.valueOf(date.substring(14, 16));
			GregorianCalendar calendar = new GregorianCalendar(year,month-1,day,hour, minute);
			long timeBase = calendar.getTimeInMillis();
			long currentTime = System.currentTimeMillis();
			if(timeBase>currentTime){
				Intent i = new Intent("com.ham.activity.AlertActivity");
				i.putExtra("SongID", Integer.valueOf(list.get(j).SOUND));
				PendingIntent operation = PendingIntent.getActivity(context, list.get(j).CODE,i, Intent.FLAG_ACTIVITY_NEW_TASK);
				AlarmManager alarmManager = (AlarmManager) context.getSystemService("alarm");
				alarmManager.set(AlarmManager.RTC_WAKEUP  , timeBase , operation);
			}
			else{
				database.DeleteImportantRecord(list.get(j).ID);
			}
		}
		database.close();
		
	}
}
