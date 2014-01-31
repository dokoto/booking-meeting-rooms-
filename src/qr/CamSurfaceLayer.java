package qr;

import com.stratesys.mbrsTEST.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
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
	private Rect Frame;
	private static final int[] SCANNER_ALPHA = { 0, 64, 128, 192, 255, 192, 128, 64 };
	private static final long ANIMATION_DELAY = 80L;
	private static final int CURRENT_POINT_OPACITY = 0xA0;
	private static final int POINT_SIZE = 6;

	public CamSurfaceLayer(Context context, AttributeSet attrs)
	{
		super(context, attrs);
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

	public void setCamConfig(CamConfig cc)
	{
		configManager = cc;
	}

	@Override
	public void onDraw(Canvas canvas)
	{
		Frame = configManager.getFramingRect();
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
			paint.setColor(laserColor);
			paint.setAlpha(SCANNER_ALPHA[scannerAlpha]);
			scannerAlpha = (scannerAlpha + 1) % SCANNER_ALPHA.length;
			int middle = Frame.height() / 2 + Frame.top;
			canvas.drawRect(Frame.left + 2, middle - 1, Frame.right - 1, middle + 2, paint);
			postInvalidateDelayed(ANIMATION_DELAY, Frame.left - POINT_SIZE, Frame.top - POINT_SIZE, Frame.right + POINT_SIZE, Frame.bottom
					+ POINT_SIZE);
		}
	}

}
