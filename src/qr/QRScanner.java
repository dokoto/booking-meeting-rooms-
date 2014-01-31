package qr;

import android.os.Bundle;
import android.app.Activity;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.stratesys.mbrsTEST.R;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class QRScanner extends Activity implements SurfaceHolder.Callback, SensorEventListener
{

	private Camera camera;
	private SurfaceView surfaceView;
	private CamSurfaceLayer viewfinder_view;
	private SurfaceHolder surfaceHolder;
	private boolean previewing = false;
	private LayoutInflater controlInflater = null;
	private byte mBuffer[];
	private TextView result_text_capture;
	private BeepManager beepManager;
	private Button rescanQR;
	private boolean ContinueScanning = true;
	private boolean mAutoFocus = true;
	private SensorManager mSensorManager;
	private Sensor mAccel;
	private boolean mInitialized = false;
	private float mLastX = 0;
	private float mLastY = 0;
	private float mLastZ = 0;

	private ImageButton TestShot;
	private ImageView TestCapuredImage;
	private boolean TestModeActive = false;
	private CamConfig configCamManager;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.layo_qr_scanner);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		getWindow().setFormat(PixelFormat.UNKNOWN);
		surfaceView = (SurfaceView) findViewById(R.id.layo_qr_scanner_camerapreview);
		viewfinder_view = (CamSurfaceLayer) findViewById(R.id.layo_qr_scanner_sf_viewfinder_view);
		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.addCallback(this);

		controlInflater = LayoutInflater.from(getBaseContext());
		View viewControl = controlInflater.inflate(R.layout.layo_qr_scanner_overlap_controls, null);
		LayoutParams layoutParamsControl = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		this.addContentView(viewControl, layoutParamsControl);

		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mAccel = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

		configCamManager = new CamConfig(getBaseContext());
		viewfinder_view.setCamConfig(configCamManager);

		result_text_capture = (TextView) viewControl.findViewById(R.id.layo_qr_scanner_overlap_controls_tv_result_text_capture);
		rescanQR = (Button) viewControl.findViewById(R.id.layo_qr_scanner_overlap_controls_bt_rescan);
		rescanQR.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				if (TestModeActive)
				{
					rescanQR.setVisibility(View.GONE);
					TestCapuredImage.setVisibility(View.GONE);
				} else
				{
					ContinueScanning = true;
					result_text_capture.setText("");
					result_text_capture.setVisibility(View.GONE);
					rescanQR.setVisibility(View.GONE);
				}

			}
		});

		beepManager = new BeepManager(this);

		/*
		 * MODO TEST
		 */
		if (TestModeActive)
		{
			Toast.makeText(getApplicationContext(), "TEST MODE ACTIVE", Toast.LENGTH_SHORT).show();
			TestCapuredImage = (ImageView) viewControl.findViewById(R.id.layo_qr_scanner_overlap_controls_iv_test_img_cap);
			TestShot = (ImageButton) viewControl.findViewById(R.id.layo_qr_scanner_overlap_controls_ib_test_shot);
			TestShot.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View arg0)
				{
					TestCapuredImage.setImageBitmap(getPicOnLive(mBuffer, configCamManager.getFramingRectInPreview() ));
					rescanQR.setVisibility(View.VISIBLE);
					TestCapuredImage.setVisibility(View.VISIBLE);
				}
			});
		}
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		mSensorManager.unregisterListener(this);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		beepManager.updatePrefs();
		mSensorManager.registerListener(this, mAccel, SensorManager.SENSOR_DELAY_UI);
	}

	private AutoFocusCallback myAutoFocusCallback = new AutoFocusCallback()
	{

		public void onAutoFocus(boolean autoFocusSuccess, Camera arg1)
		{
			mAutoFocus = true;
		}
	};

	public void setCameraFocus(AutoFocusCallback autoFocus)
	{
		if (camera.getParameters().getFocusMode().equals(Parameters.FOCUS_MODE_AUTO)
				|| camera.getParameters().getFocusMode().equals(Parameters.FOCUS_MODE_MACRO))
		{
			camera.autoFocus(autoFocus);
		}

	}

	@Override
	public void onSensorChanged(SensorEvent event)
	{

		float x = event.values[0];
		float y = event.values[1];
		float z = event.values[2];
		if (!mInitialized)
		{
			mLastX = x;
			mLastY = y;
			mLastZ = z;
			mInitialized = true;
		}
		float deltaX = Math.abs(mLastX - x);
		float deltaY = Math.abs(mLastY - y);
		float deltaZ = Math.abs(mLastZ - z);

		if (deltaX > .5 && mAutoFocus)
		{
			mAutoFocus = false;
			setCameraFocus(myAutoFocusCallback);
		}
		if (deltaY > .5 && mAutoFocus)
		{
			mAutoFocus = false;
			setCameraFocus(myAutoFocusCallback);
		}
		if (deltaZ > .5 && mAutoFocus)
		{
			mAutoFocus = false;
			setCameraFocus(myAutoFocusCallback);
		}

		mLastX = x;
		mLastY = y;
		mLastZ = z;

	}

	private void decode(Bitmap QRbitmap)
	{

		if (QRbitmap == null)
			return;

		QRbitmap = QRbitmap.copy(Bitmap.Config.ARGB_8888, true);
		int[] intArray = new int[QRbitmap.getWidth() * QRbitmap.getHeight()];
		QRbitmap.getPixels(intArray, 0, QRbitmap.getWidth(), 0, 0, QRbitmap.getWidth(), QRbitmap.getHeight());

		LuminanceSource source = new com.google.zxing.RGBLuminanceSource(QRbitmap.getWidth(), QRbitmap.getHeight(), intArray);
		BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
		Reader reader = new MultiFormatReader();
		try
		{
			Result result = reader.decode(bitmap);
			beepManager.playBeepSoundAndVibrate();
			result_text_capture.setText(result.getText());
			result_text_capture.setVisibility(View.VISIBLE);
			rescanQR.setVisibility(View.VISIBLE);
			ContinueScanning = false;

		} catch (NotFoundException e)
		{
			result_text_capture.setVisibility(View.GONE);
			Log.w("QR DECODE", "I can't decode image, no valid QR found.");
		} catch (ChecksumException e)
		{
			result_text_capture.setVisibility(View.GONE);
			e.printStackTrace();
		} catch (FormatException e)
		{
			result_text_capture.setVisibility(View.GONE);
			e.printStackTrace();

		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
	{
		if (previewing)
		{
			camera.stopPreview();
			previewing = false;
		}

		if (camera != null)
		{
			try
			{
				camera.setPreviewDisplay(surfaceHolder);				
				camera.startPreview();
				previewing = true;
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder)
	{
		try
		{
			camera = Camera.open();
			configCamManager.initFromCameraParameters(camera);
			configCamManager.setDesiredCameraParameters(camera, true);

			updateBufferSize();
			camera.addCallbackBuffer(mBuffer);
			camera.setPreviewCallbackWithBuffer(new PreviewCallback()
			{
				public synchronized void onPreviewFrame(byte[] data, Camera c)
				{

					if (camera != null)
					{
						camera.addCallbackBuffer(mBuffer);
						// Experimental
						if (TestModeActive == false)
						{
							if (ContinueScanning)
								decode( getPicOnLive(mBuffer, configCamManager.getFramingRectInPreview() ) );
						}
					}
				}
			});

		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0)
	{
		camera.stopPreview();
		camera.release();
		camera = null;
		previewing = false;

	}

	private void updateBufferSize()
	{
		mBuffer = null;
		System.gc();
		int h = camera.getParameters().getPreviewSize().height;
		int w = camera.getParameters().getPreviewSize().width;
		int bitsPerPixel = ImageFormat.getBitsPerPixel(camera.getParameters().getPreviewFormat());
		mBuffer = new byte[w * h * bitsPerPixel / 8];
	}

	public Bitmap getPicOnLive(byte[] buffer, Rect frame)
	{
		System.gc();
		Bitmap b = null;
		Size s = camera.getParameters().getPreviewSize();
		
		YuvImage yuvimage = new YuvImage(buffer, ImageFormat.NV21, s.width, s.height, null);

		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		yuvimage.compressToJpeg(frame, 100, outStream);
		b = BitmapFactory.decodeByteArray(outStream.toByteArray(), 0, outStream.size());

		Matrix matrix = new Matrix();
		matrix.postRotate(90);
		yuvimage = null;
		outStream = null;
		System.gc();
		return Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, true);
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1)
	{
		// TODO Auto-generated method stub

	}

}
