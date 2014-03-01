package com.ham.activity;

import com.example.hamnote.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

public class ImageShowActivity extends Activity {
	private String imagePath;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imageshow);        
        
        Intent i = getIntent();
        imagePath = (String) i.getExtras().getString("imagePath");
        
        ImageView iView = (ImageView) findViewById(R.id.image_show);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
		iView.setImageBitmap(BitmapFactory.decodeFile(imagePath, options));		
    }
	
	@Override
    public void onStop()
    {
    	super.onStop();
    }

}
