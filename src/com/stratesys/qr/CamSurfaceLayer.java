package com.stratesys.qr;

import com.stratesys.mbrsTEST.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class CamSurfaceLayer extends View
{
	private CamConfig configManager;
	private final Paint paint;
	private Bitmap resultBitmap;
	private final int maskColor;
	private final int resultColor;
	private final int laserColor;
	private int scannerAlpha;
	private Rect framingRect;
	private Rect Frame;
	private static final int MIN_FRAME_WIDTH = 240;
	private static final int MIN_FRAME_HEIGHT = 240;
	private static final int MAX_FRAME_WIDTH = 1200; // = 5/8 * 1920
	private static final int MAX_FRAME_HEIGHT = 675; // = 5/8 * 1080
	private static final int[] SCANNER_ALPHA = { 0, 64, 128, 192, 255, 192, 128, 64 };
	private static final long ANIMATION_DELAY = 80L;
	private static final int CURRENT_POINT_OPACITY = 0xA0;
	private static final int POINT_SIZE = 6;

	public CamSurfaceLayer(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		configManager = new CamConfig(context);
		configManager.initFromCameraParameters();
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		Resources resources = getResources();
		maskColor = resources.getColor(R.color.viewfinder_mask);
		resultColor = resources.getColor(R.color.result_view);
		laserColor = resources.getColor(R.color.viewfinder_laser);
		scannerAlpha = 0;
	}

	public Rect getFrame()
	{
		return Frame;
	}
	

	@Override
	public void onDraw(Canvas canvas)
	{
		Frame = getFramingRect();
		int width = canvas.getWidth();
		int height = canvas.getHeight();
		
		paint.setColor(resultBitmap != null ? resultColor : maskColor);
		canvas.drawRect(0, 0, width, Frame.top, paint);
	    canvas.drawRect(0, Frame.top, Frame.left, Frame.bottom + 1, paint);
	    canvas.drawRect(Frame.right + 1, Frame.top, width, Frame.bottom + 1, paint);
	    canvas.drawRect(0, Frame.bottom + 1, width, height, paint);
		
		if (resultBitmap != null)
		{
			// Draw the opaque result bitmap over the scanning rectangle
			paint.setAlpha(CURRENT_POINT_OPACITY);
			canvas.drawBitmap(resultBitmap, null, Frame, paint);
		} else
		{

			// Draw a red "laser scanner" line through the middle to show
			// decoding is active
			paint.setColor(laserColor);
			paint.setAlpha(SCANNER_ALPHA[scannerAlpha]);
			scannerAlpha = (scannerAlpha + 1) % SCANNER_ALPHA.length;
			int middle = Frame.height() / 2 + Frame.top;
			canvas.drawRect(Frame.left + 2, middle - 1, Frame.right - 1, middle + 2, paint);
			// Request another update at the animation interval, but only
			// repaint the laser line,
			// not the entire viewfinder mask.
			postInvalidateDelayed(ANIMATION_DELAY, Frame.left - POINT_SIZE, Frame.top - POINT_SIZE, Frame.right + POINT_SIZE, Frame.bottom + POINT_SIZE);
		}
	}

	public synchronized Rect getFramingRect()
	{
		if (framingRect == null)
		{
			Point screenResolution = configManager.getScreenResolution();
			if (screenResolution == null)
			{
				return null;
			}

			int width = findDesiredDimensionInRange(screenResolution.x, MIN_FRAME_WIDTH, MAX_FRAME_WIDTH);
			int height = findDesiredDimensionInRange(screenResolution.y, MIN_FRAME_HEIGHT, MAX_FRAME_HEIGHT);

			int leftOffset = (screenResolution.x - width) / 2;
			int topOffset = (screenResolution.y - height) / 2;
			framingRect = new Rect(leftOffset, topOffset, leftOffset + width, topOffset + height);
		}
		return framingRect;
	}


	private static int findDesiredDimensionInRange(int resolution, int hardMin, int hardMax)
	{
		int dim = 5 * resolution / 8; // Target 5/8 of each dimension
		if (dim < hardMin)
		{
			return hardMin;
		}
		if (dim > hardMax)
		{
			return hardMax;
		}
		return dim;
	}

}
