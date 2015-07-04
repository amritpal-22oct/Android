package com.example.animdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.View;

public class CustomView extends View{

	Bitmap[] bitmaps = new Bitmap[3];
	int curr=0;
	
	public CustomView(Context context) {
		super(context);
		bitmaps[0] = BitmapFactory.decodeResource(getResources(), R.drawable.ricky1);
		bitmaps[1] = BitmapFactory.decodeResource(getResources(), R.drawable.sanga1);
		bitmaps[2] = BitmapFactory.decodeResource(getResources(), R.drawable.sehwag1);

	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
			canvas.drawBitmap(bitmaps[curr], 0, 0,null);
			curr = (curr+1)%3;
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			invalidate();
	}

}
