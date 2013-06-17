package com.example.web_app;

import java.util.List;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.AvoidXfermode.Mode;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.Shape;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.BaseAdapter;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class ImageAdapter extends BaseAdapter implements ListAdapter {

	private Context mContext;
	
	private List<Drawable> profile_pics;
	
	public ImageAdapter(Context c,	List<Drawable> profile_pics) {
		mContext = c;
		this.profile_pics = profile_pics;
		
	}
	
	@Override
	public int getCount() {
		return profile_pics.size();
	}

	@Override
	public Object getItem(int position) {
		return profile_pics.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

        ImageView imageView;
        ShapeDrawable s = new ShapeDrawable(new OvalShape());
        
        if (convertView == null) {  // if it's not recycled, initialize some attributes
        
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(400, 400));
        	imageView.setImageDrawable(s);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setBackground(profile_pics.get(position));
        return imageView;
	}
	
}
	

