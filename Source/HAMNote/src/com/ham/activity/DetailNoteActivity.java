package com.ham.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

import com.example.hamnote.R;
import com.ham.database.*;

import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.ham.dialog.DialogHandle;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class DetailNoteActivity extends Activity {
	private DatabaseAdapter database = new DatabaseAdapter(this);
	Random randomGenerator = new Random();
	private Long noteid = null;
	private EditText content;
	private EditText title;
	private TextView titleRemain;
	private NoteRecord note = null;
	private int maxLength_title = 40;
	private Menu detailMenu;
	private boolean importantTurnedOn = false;
	private boolean chooseTimer = false;
	private boolean chooseSong = false;
	private DialogHandle dialogHandle = new DialogHandle(this);
	private String[] imagePath;
	// contain current images path
	private String imageRaw = "";
	private final String IMAGERAW_STORAGE = "updatedImgRaw";
	private GridView grid;
	private boolean addImageCalled;
	private int screenWidth;
	private int screenHeight;
	private List<String> deleteImageList = new ArrayList<String>();
	private List<String> unSaveImages = new ArrayList<String>();
	private boolean saveClicked = false;

	private int isUpdate = 0;
	private String themeStyle = "", fontStyle = "";
	private int fontSize = 20;
	private RelativeLayout layout;

	private static int RESULT_LOAD_IMAGE = 1;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_hamnote);

		content = (EditText) findViewById(R.id.detail_content);
		title = (EditText) findViewById(R.id.detail_title);
		titleRemain = (TextView) findViewById(R.id.detail_title_remain_character);
		grid = (GridView) findViewById(R.id.detail_gridImage);
		layout = (RelativeLayout) findViewById(R.id.detail_layout);

		// Get screen size
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		screenWidth = size.x;
		screenHeight = size.y;

		Intent i = getIntent();
		isUpdate = (Integer) i.getExtras().getInt("update");
		if (isUpdate == 1) {
			noteid = (Long) i.getExtras().getLong("noteid");
		} else {
			themeStyle = (String) i.getExtras().getString("themestyle");
			fontStyle = (String) i.getExtras().getString("fontstyle");
			fontSize = (Integer) i.getExtras().getInt("fontsize");
		}
		database.open();

		createDetail();
		addImageCalled = false;
		if(savedInstanceState!=null){
			imageRaw = savedInstanceState.getString(IMAGERAW_STORAGE);
		}
		createImageGrid();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.detailnote, menu);
		this.detailMenu = menu;
		return true;
	}	

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString(IMAGERAW_STORAGE, imageRaw);
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (addImageCalled) {
			database.open();
			createImageGrid();
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		database.close();		
	}	

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(!saveClicked && unSaveImages.size()>0) {
			String[] iArr = new String[unSaveImages.size()];
			unSaveImages.toArray(iArr);
			for(String p: iArr){
				if (p != "") {
					String ip = p;	
					String contextFilesPath = this.getFilesDir().getPath();
					ip = ip.replace(contextFilesPath + "/", "");
					this.deleteFile(ip);					
				}
			}
		}
	}

	private void InitGui() {
		// Declare font
		Typeface tf1 = Typeface.createFromAsset(getAssets(),
				"fonts/valentina.ttf");
		Typeface tf2 = Typeface.createFromAsset(getAssets(),
				"fonts/cariardreams.ttf");
		Typeface tf3 = Typeface.createFromAsset(getAssets(),
				"fonts/optimaregular.ttf");		
		
		content.setBackgroundColor(Color.WHITE);
		content.getBackground().setAlpha(75);
		content.setMaxHeight(400);
		title.setBackgroundColor(Color.WHITE);
		title.getBackground().setAlpha(95);
		title.setWidth(screenWidth - 150);
		// Layout
		if (themeStyle.equals("Spring")) {
			layout.setBackgroundResource(R.drawable.spring);
		} else if (themeStyle.equals("Summer")) {
			layout.setBackgroundResource(R.drawable.summer);
		} else if (themeStyle.equals("Autumn")) {
			layout.setBackgroundResource(R.drawable.autumn);
		} else {
			layout.setBackgroundResource(R.drawable.winter);
		}

		// Font style
		if (fontStyle.equals("Valentina font")) {
			title.setTypeface(tf1, Typeface.BOLD);
			content.setTypeface(tf1, Typeface.BOLD_ITALIC);
		} else if (fontStyle.equals("Optima font")) {
			title.setTypeface(tf3, Typeface.BOLD);
			content.setTypeface(tf3, Typeface.BOLD_ITALIC);
		} else {
			title.setTypeface(tf2, Typeface.BOLD);
			content.setTypeface(tf2, Typeface.BOLD_ITALIC);
		}
		// Font size
		title.setTextSize(fontSize + 2);
		content.setTextSize(fontSize);
	}

	private void createDetail() // display detail content
	{
		if (isUpdate == 1) {
			note = database.GetNoteRecord(noteid.toString());
			title.setText(note.TITLE);
			content.setText(note.CONTENT);
			imageRaw = note.IMAGE;
			importantTurnedOn = (note.ISIMPORTANT > 0) ? true : false;
			titleRemain.setText(Integer.toString(maxLength_title
					- note.TITLE.length()));
			ThemeRecord theme = database.GetThemeRecord(note.THEMEID);
			if (theme != null) {
				themeStyle = theme.BACKGROUND;
				fontStyle = theme.FONT;
				fontSize = theme.SIZE;
			}
		}
		InitGui();
		title.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				titleRemain.setText(Integer.toString(maxLength_title
						- title.getText().toString().length()));
			}
		});
	}

	private void SetAlert(ImportantRecord r) {
		Intent i = new Intent("com.ham.activity.AlertActivity");
		i.putExtra("SongID", Integer.valueOf(r.SOUND));
		i.putExtra("CODE", r.CODE);
		PendingIntent operation = PendingIntent.getActivity(getBaseContext(),
				r.CODE, i, Intent.FLAG_ACTIVITY_NEW_TASK);
		String date = r.TIMEALERT;
		int day = Integer.valueOf(date.substring(0, 2));
		int month = Integer.valueOf(date.substring(3, 5));
		int year = Integer.valueOf(date.substring(6, 10));
		int hour = Integer.valueOf(date.substring(11, 13));
		int minute = Integer.valueOf(date.substring(14, 16));
		GregorianCalendar calendar = new GregorianCalendar(year,month-1,day,hour, minute);
		long timeBase = calendar.getTimeInMillis();
		AlarmManager alarmManager = (AlarmManager)getSystemService("alarm");
		alarmManager.set(AlarmManager.RTC_WAKEUP, timeBase, operation);
	}

	@SuppressLint("SimpleDateFormat")
	private void saveNote() {
		// retrieve current note info
		chooseTimer = dialogHandle.chooseTime;
		chooseSong = dialogHandle.chooseSong;
		String nID = "";
		String nTitle = "";
		String nContent = "";
		String nImage = "";
		String nDate = "";
		int nThemeID = 0;
		int nIsImportant = 0;

		String timeStamp = new SimpleDateFormat("yyyyMMddhhmmss")
				.format(Calendar.getInstance().getTime());
		if (isUpdate == 1)
			nID = noteid.toString();
		nTitle = title.getText().toString();
		nContent = content.getText().toString();
		nImage = imageRaw;
		nDate = timeStamp;
		nIsImportant = (importantTurnedOn == true) ? 1 : 0;
		nThemeID = randomGenerator.nextInt(2000);

		NoteRecord nNote = new NoteRecord(nID, nTitle, nContent, nImage, nDate,
				nThemeID, nIsImportant);
		NoteRecord mNote = new NoteRecord(nTitle, nContent, nImage, nDate,
				nThemeID, nIsImportant);
		if (isUpdate == 1) {
			database.UpdateToNoteTbl(noteid.toString(), nNote);
		} else {
			database.InsertToNoteTbl(mNote);
			database.InsertCompleteThemeTbl(new ThemeRecord(nThemeID,
					themeStyle, fontStyle, fontSize));
		}
		// Set if it important
		if (importantTurnedOn && isUpdate == 1) {
			ImportantRecord ir = database.GetImportantRecord(noteid.toString());
			if (ir == null) {
				int id = randomGenerator.nextInt(10000);
				if (chooseTimer)
					database.InsertToImportantTbl(new ImportantRecord(noteid
							.toString(), dialogHandle.getTime(), Integer
							.toString(dialogHandle.getSong()), id));
				else {
					Calendar cal = Calendar.getInstance();
					cal.add(Calendar.DATE, 7);
					String time = new SimpleDateFormat("dd/MM/yyyy-hh:mm")
							.format(cal.getTime());
					database.InsertToImportantTbl(new ImportantRecord(noteid
							.toString(), time, Integer.toString(dialogHandle
							.getSong()), id));
				}
				ImportantRecord ir1 = database.GetImportantRecord(noteid.toString());
				SetAlert(ir1);
			} else {
				if(chooseTimer){
					database.UpdateToImportantTbl(ir.ID,"TIMEALERT", dialogHandle.getTime());
				}
				if(chooseSong)
				{
					database.UpdateToImportantTbl(ir.ID,"SOUND", Integer.toString(dialogHandle.getSong()));
				}
				ImportantRecord ir1 = database.GetImportantRecord(noteid.toString());
				SetAlert(ir1);
			}

		}
		if (importantTurnedOn && isUpdate != 1) {
			int id = randomGenerator.nextInt(10000);
			if (chooseTimer)
				database.InsertToImportantTbl(new ImportantRecord(mNote.ID,
						dialogHandle.getTime(), Integer.toString(dialogHandle
								.getSong()), id));
			else {
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DATE, 7);
				String time = new SimpleDateFormat("dd/MM/yyyy-hh:mm")
						.format(cal.getTime());
				database.InsertToImportantTbl(new ImportantRecord(mNote.ID,
						time, Integer.toString(dialogHandle.getSong()), id));
			}
			ImportantRecord ir = database.GetImportantRecord(mNote.ID);
			SetAlert(ir);

		}
		if (!importantTurnedOn && isUpdate == 1) {
			ImportantRecord ir = database.GetImportantRecord(nNote.ID);
			if (ir != null) {
				Intent k = new Intent("com.ham.activity.AlertActivity");
				PendingIntent operation = PendingIntent.getActivity(
						getBaseContext(), ir.CODE, k,
						Intent.FLAG_ACTIVITY_NEW_TASK);
				AlarmManager alarmManager = (AlarmManager) getBaseContext()
						.getSystemService(ALARM_SERVICE);
				alarmManager.cancel(operation);
				database.DeleteImportantRecord(ir.ID);
			}
		}
		chooseTimer = false;
		chooseSong = false;
		
		// delete images
		
		if(deleteImageList.size()>0) {
			String[] iArr = new String[deleteImageList.size()];
			deleteImageList.toArray(iArr);
			for(String p: iArr){
				if (p != "") {
					String ip = p;	
					String contextFilesPath = this.getFilesDir().getPath();
					ip = ip.replace(contextFilesPath + "/", "");
					this.deleteFile(ip);
				}
			}
		}
		
		saveClicked = true;
	}

	public void onButtonClicked(View v) {
		if (v.getId() == R.id.detail_saveButton) {
			saveNote();
			database.close();
			/*
			 * Intent i = new Intent(getBaseContext(),HAMNoteActivity.class);
			 * startActivity(i);
			 */
			this.finish();
		}
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		if (importantTurnedOn) {
			menu.findItem(R.id.detailmenu_star).setIcon(
					R.drawable.star_yellow_36x36);
			menu.findItem(R.id.detailmenu_timer).setIcon(
					R.drawable.timer_icon_green_36x36);
			menu.findItem(R.id.detailmenu_music).setIcon(
					R.drawable.music_icon_green_36x36);
		}
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
			if (importantTurnedOn) {
				MenuItem iMusic = detailMenu.findItem(R.id.detailmenu_music);
				MenuItem iTimer = detailMenu.findItem(R.id.detailmenu_timer);
				iMusic.setIcon(R.drawable.music_icon_grey_36x36);
				iTimer.setIcon(R.drawable.timer_icon_grey_36x36);
				item.setIcon(R.drawable.star_grey_36x36);
			} else {
				MenuItem iMusic = detailMenu.findItem(R.id.detailmenu_music);
				MenuItem iTimer = detailMenu.findItem(R.id.detailmenu_timer);
				iMusic.setIcon(R.drawable.music_icon_green_36x36);
				iTimer.setIcon(R.drawable.timer_icon_green_36x36);
				item.setIcon(R.drawable.star_yellow_36x36);
			}
			importantTurnedOn = !importantTurnedOn;
			break;

		case R.id.detailmenu_music:
			if (importantTurnedOn)
				showDialog(dialogHandle.SONG_DIAGLOG_ID);
			break;

		case R.id.detailmenu_timer:
			if (importantTurnedOn) {
				showDialog(dialogHandle.DATETIME_DIALOG_ID);
			}
			break;

		case R.id.detailmenu_lock:
			Toast.makeText(getBaseContext(),
					"Please buy PRO version to unlock this function ^^",
					Toast.LENGTH_LONG).show();
			break;

		case R.id.detailmenu_addimage:
			addImageCalled = true;
			database.close();
			Intent i = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(i, RESULT_LOAD_IMAGE);
			break;

		default:
			database.close();	
			if(unSaveImages.size()>0) {
				String[] iArr = new String[unSaveImages.size()];
				unSaveImages.toArray(iArr);
				for(String p: iArr){
					if (p != "") {
						String ip = p;	
						String contextFilesPath = this.getFilesDir().getPath();
						ip = ip.replace(contextFilesPath + "/", "");
						this.deleteFile(ip);
					}
				}
			}
			this.finish();
			break;
		}
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
				&& null != data) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };

			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String picturePath = cursor.getString(columnIndex);
			cursor.close();

			if (imageRaw.length() == 0) {
				imageRaw = picturePath;
			} else {
				imageRaw = imageRaw + ":" + picturePath;
			}

		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, v.getId(), 0, "Remove");
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getTitle() == "Remove") {
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
					.getMenuInfo();
			imageRaw = imageRaw.replace(imagePath[info.position], "");
			if (imageRaw.startsWith(":")) {
				imageRaw = imageRaw.substring(1);
			} else if (imageRaw.endsWith(":")) {
				imageRaw = imageRaw.substring(0, imageRaw.length() - 1);
			} else {
				imageRaw = imageRaw.replace("::", ":");
			}
			
			deleteImageList.add(imagePath[info.position]);
			createImageGrid();
		}
		return true;
	}

	private void createImageGrid() {
		if (imageRaw.length() > 0) {
			imagePath = imageRaw.split(":");

			String contextFilesPath = this.getFilesDir().getPath();
			for (int i = 0; i < imagePath.length; i++) {
				if (!imagePath[i].startsWith(contextFilesPath)) // not in
																// internal
																// store
				{
					// Bitmap resizedImage = resizeImage(imagePath[i],
					// screenWidth, screenHeight);
					String newPath = writeFileToInternalStorage(this,
							imagePath[i], screenWidth, screenHeight);
					if (newPath.length() > 0) { imagePath[i] = newPath; unSaveImages.add(newPath); }					
				}
			}

			imageRaw = imagePath[0];
			if (imagePath.length > 1) {
				for (int j = 1; j < imagePath.length; j++)
					imageRaw = imageRaw + ":" + imagePath[j];
			}

			grid.setAdapter(new ImageAdapter(this, imagePath));
			this.registerForContextMenu(grid);
			grid.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					String img = imagePath[(int) arg3];
					final BitmapFactory.Options options = new BitmapFactory.Options();
					options.inSampleSize = 1;
					Bitmap bmImg = BitmapFactory.decodeFile(img, options);
					try {						
						ExifInterface exif = new ExifInterface(img);
						int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);						
						switch(orientation){
							case ExifInterface.ORIENTATION_ROTATE_90:
								bmImg = ccw(bmImg, 90);
								break;
							case ExifInterface.ORIENTATION_ROTATE_270:
								bmImg = ccw(bmImg, 270);
								break;
							case ExifInterface.ORIENTATION_ROTATE_180:
								bmImg = ccw(bmImg, 180);
								break;
							default:
								break;
						}						
					} catch (IOException e) {
						// e.printStackTrace();
					}
					// Create dialog
					dialogHandle.setImage(bmImg);
					dialogHandle.DialogProcess(dialogHandle.IMAGE_DIAGLOG_ID).show();
				}
			});

		} else {
			//imagePath = imageRaw.split(":");
			grid.setAdapter(new ImageAdapter(this, new String[0]));
		}

	}
	
	private Bitmap ccw(Bitmap b, int r)
	{
		Matrix m = new Matrix();
		m.postRotate(r);
		int w = b.getWidth();
		int h = b.getHeight();
		return Bitmap.createBitmap(b, 0, 0, w, h, m, true);
	}

	private String writeFileToInternalStorage(Context context, String path,
			int w, int h) {
		try {
			int inSampleSize = 1;
			// First decode with inJustDecodeBounds=true to check dimensions
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(path, options);
			int bitmapHeight = options.outHeight;
			int bitmapWidth = options.outWidth;

			while (bitmapHeight / (2 * inSampleSize) > h
					&& bitmapWidth / (2 * inSampleSize) > w) {
				inSampleSize *= 2;
			}

			options.inSampleSize = inSampleSize;
			options.inJustDecodeBounds = false;
			String fileName = Long.toString(System.currentTimeMillis())
					+ ".jpeg";
			FileOutputStream fos = openFileOutput(fileName,
					Context.MODE_PRIVATE);
			Bitmap bm = BitmapFactory.decodeFile(path, options);
			bm.compress(Bitmap.CompressFormat.JPEG, 30, fos);
			File cacheDir = context.getFilesDir();
			String newpath = cacheDir.getPath() + "/" + fileName;
			
			// set exifinterface for new image
			try {
				ExifInterface oldExif = new ExifInterface(path);
				ExifInterface newExif = new ExifInterface(newpath);
				int oldOrientation = oldExif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
				newExif.setAttribute(ExifInterface.TAG_ORIENTATION, Integer.toString(oldOrientation));
				newExif.saveAttributes();
			} catch (IOException e) {
				// e.printStackTrace();
			}	
			return newpath;
		} catch (FileNotFoundException e) {
			// e.printStackTrace();
		}
		return "";
	}

}
