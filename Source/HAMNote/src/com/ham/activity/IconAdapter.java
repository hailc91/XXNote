package com.ham.activity;

import com.example.hamnote.R;
import java.util.ArrayList;
import com.ham.database.*;

//import android.R;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class IconAdapter extends BaseAdapter
{
	private Context context;
	private ArrayList<NoteRecord> data;
	public IconAdapter(Context c, ArrayList<NoteRecord> listNote)
	{
		context = c;
		data = listNote;
	}
	
	@Override
	public int getCount() { return data.size();	}
	
	@Override
	public NoteRecord getItem(int pos) { return data.get(pos);	}
	
	@Override
	public long getItemId(int pos) { return Long.valueOf(getItem(pos).ID).longValue(); }
	
	@Override
	public View getView(int pos, View convertView, ViewGroup parent)
	{		
		TextView iconNote = new TextView(context);
		String date = getItem(pos).DATE;
		String yyyy = date.substring(0, 4);
		String mm = date.substring(4, 6);
		String dd = date.substring(6);
		date = dd + "-" + mm + "-" + yyyy;
		iconNote.setText(date + "\n" + getItem(pos).TITLE);
		iconNote.setPadding(20, 5, 5, 5);
		iconNote.setBackgroundResource(R.drawable.note_container_icon_200x100);
		
		return iconNote;
	}

}
