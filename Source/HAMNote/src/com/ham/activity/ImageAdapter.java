package com.ham.activity;

import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
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
		ImageView iView = new ImageView(context);
		BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4; 
		Bitmap bm = BitmapFactory.decodeFile(getItem(pos), options);
		try{		       
	        // rotate image if it is rotated
	        ExifInterface exif = new ExifInterface(getItem(pos));
			int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);			
			switch(orientation){
				case ExifInterface.ORIENTATION_ROTATE_90:
					bm = ccw(bm, 90);
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					bm = ccw(bm, 270);
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					bm = ccw(bm, 180);
					break;
				default:
					break;
			}   	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Drawable d = new BitmapDrawable(context.getResources(), bm);		
		iView.setLayoutParams(new GridView.LayoutParams(parent.getWidth()/3-vSpace*2, parent.getWidth()/3-vSpace*2));
		iView.setImageDrawable(d);		
		iView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		iView.setAlpha((float)0.85);
		return iView;
	}
	
	private Bitmap ccw(Bitmap b, int r)
	{
		Matrix m = new Matrix();
		m.postRotate(r);
		int w = b.getWidth();
		int h = b.getHeight();
		return Bitmap.createBitmap(b, 0, 0, w, h, m, true);
	}
}
