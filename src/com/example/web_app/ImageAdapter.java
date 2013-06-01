package com.example.web_app;

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
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.Shape;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.BaseAdapter;

public class ImageAdapter extends BaseAdapter implements ListAdapter {

	private Context mContext;
	
	private Integer[] mThumbIds = {
			R.drawable.rob,
			R.drawable.andrew,
			R.drawable.tom,
			R.drawable.james,
			R.drawable.jean,
			R.drawable.matt
	};
	
	public ImageAdapter(Context c) {
		mContext = c;
	}
	
	@Override
	public int getCount() {
		return mThumbIds.length;
	}

	@Override
	public Object getItem(int position) {
		return mThumbIds[position];
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
            imageView.setLayoutParams(new GridView.LayoutParams(600, 600));
        	imageView.setImageDrawable(s);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        //    imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mThumbIds[position]);
        return imageView;
	}
	
}
	

