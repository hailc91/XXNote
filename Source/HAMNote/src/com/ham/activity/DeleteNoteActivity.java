package com.ham.activity;

import com.example.hamnote.R;
import com.ham.database.DatabaseAdapter;
import com.ham.database.ImportantRecord;
import com.ham.database.NoteRecord;

import java.util.ArrayList;
import java.util.List;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
//import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;

public class DeleteNoteActivity extends ListActivity {
	
	private DatabaseAdapter database = new DatabaseAdapter(this);
	ArrayList<NoteRecord> listNote;
	final Context context = this;

	/** Items entered by the user is stored in this ArrayList variable */
    private List<Model> lists = new ArrayList<Model>();
    private List<String> list = new ArrayList<String>();
    /** Declaring an ArrayAdapter to set items to ListView */
    ArrayAdapter<String> adapter;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {

	    super.onCreate(savedInstanceState);
	    
	    
	 
	    /*ArrayAdapter<Model> adapters = new InteractiveArrayAdapter(this, list);
	    setListAdapter(adapters);*/
	
	    /** Setting a custom layout for the list activity */
	    setContentView(R.layout.testdeletenote);
	
	    /** Reference to the add button of the layout main.xml */
	    /*Button btn = (Button) findViewById(R.id.btnAdd);*/
	    
	    /** Reference to the delete button of the layout main.xml */
	    Button btnDel = (Button) findViewById(R.id.btnDel);
	
	    /** Defining the ArrayAdapter to set items to ListView */
	    adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, list);
	
	    /** Defining a click event listener for the button "Add" */
	    /*OnClickListener listener = new OnClickListener() {
	                    @Override
	                    public void onClick(View v) {
	                            EditText edit = (EditText) findViewById(R.id.txtItem);
	                            list.add(edit.getText().toString());
	                            edit.setText("");
	                            adapter.notifyDataSetChanged();
	                    }
	            };*/
	    database.open();
	    listNote = database.GetListNote();
	    database.close();
	    for(int i =0;i<listNote.size();++i){
	    	lists.add(get(listNote.get(i).TITLE, listNote.get(i).ID));
	    	list.add(listNote.get(i).TITLE);
            adapter.notifyDataSetChanged();
	    }
	            
	    /** Defining a click event listener for the button "Delete" */
	    OnClickListener listenerDel = new OnClickListener() {
	    	@Override
	    	public void onClick(View v) {
	    		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
	    				context);
	    		// set title
				alertDialogBuilder.setTitle("Alert ");
				// set dialog message
				alertDialogBuilder
					.setMessage("Do you want delete your note?")
					.setCancelable(false)
					.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							// if this button is clicked, close
							// current activity
							/** Getting the checked items from the listview */
				    		SparseBooleanArray checkedItemPositions = getListView().getCheckedItemPositions();
				    		int itemCount = getListView().getCount();
				    		int result = -1;
				    		for(int i=itemCount; i >= 0; i--){
				    			if(checkedItemPositions.get(i)){	    				
				    				adapter.remove(lists.get(i).getName());
				    				database.open();
				    				NoteRecord r= database.GetNoteRecord(lists.get(i).getid());
				    				result = database.DeleteRecordFromNoteTbl(lists.get(i).getid());
				    				if(r!=null){
				    					database.DeleteThemeRecord(r.THEMEID);
				    					if(r.ISIMPORTANT == 1){
				    						ImportantRecord ir = database.GetImportantRecord(r.ID);
				    						Intent k = new Intent("com.ham.activity.AlertActivity");
				    						PendingIntent operation = PendingIntent.getActivity(getBaseContext(), ir.CODE, k, Intent.FLAG_ACTIVITY_NEW_TASK);
				    						AlarmManager alarmManager = (AlarmManager) getBaseContext().getSystemService(ALARM_SERVICE);
				    						alarmManager.cancel(operation);
				    						database.DeleteImportantRecord(r.ID);
				    					}
				    				}
				        			database.close();
				        			
				    			}
				    		}	
				    		Log.d("Success", Integer.toString(result));
				    		checkedItemPositions.clear();
				    	    adapter.notifyDataSetChanged();
				    	    /*Intent k = new Intent (getBaseContext(),HAMNoteActivity.class);
				        	startActivity(k);*/

						}
					  })
					.setNegativeButton("No",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							// if this button is clicked, just close
							// the dialog box and do nothing
							dialog.cancel();
						}
					});
	    			    	   
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
 
				// show it
				alertDialog.show();
	    	}
	    };            
	
	    /** Setting the event listener for the add button */
	    /*btn.setOnClickListener(listener);*/
	    
	    /** Setting the event listener for the delete button */
	    btnDel.setOnClickListener(listenerDel);    
	
	    /** Setting the adapter to the ListView */
	    setListAdapter(adapter);
    }
    private Model get(String name, String id) {
        return new Model(name,id);
      }
    
}
