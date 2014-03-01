package com.ham.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter
{
	private Context context;
	private String[] imagePath;
	private int vSpace = 8;
	
	public ImageAdapter(Context c, String[] iPath)
	{
		context = c;
		imagePath = iPath;	
	}
	
	@Override
	public int getCount() { return imagePath.length;	}
	
	@Override
	public String getItem(int pos) { return imagePath[pos];	}
	
	@Override
	public long getItemId(int pos) { return pos; }
	
	@Override
	public View getView(int pos, View convertView, ViewGroup parent)
	{					
		/*ImageView iView;
		BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 12;
        
		if (convertView == null) { // if it's not recycled, initialize some attributes
			iView = new ImageView(context);					
			iView.setLayoutParams(new GridView.LayoutParams(parent.getWidth()/3-vSpace*2, parent.getWidth()/3-vSpace*2));
			iView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		} else {
			iView = (ImageView) convertView;
        }
		
		iView.setImageBitmap(BitmapFactory.decodeFile(getItem(pos), options));
		return iView;*/
		
		BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        Bitmap bm = BitmapFactory.decodeFile(getItem(pos), options);
        Drawable d = new BitmapDrawable(context.getResources(), bm);
		ImageView iView = new ImageView(context);
		iView.setLayoutParams(new GridView.LayoutParams(parent.getWidth()/3-vSpace*2, parent.getWidth()/3-vSpace*2));
		//iView.setPadding(4, 4, 4, 4);
		iView.setImageDrawable(d);		
		iView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		iView.setAlpha((float)0.85);
		return iView;
	}

}
