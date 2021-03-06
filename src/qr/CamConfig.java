package qr;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * A class which deals with reading, parsing, and setting the camera parameters
 * which are used to configure the camera hardware.
 */
final class CamConfig
{
	public enum FrontLightMode
	{

		/** Always on. */
		ON,
		/** On only when ambient light is low. */
		AUTO,
		/** Always off. */
		OFF;

		private static FrontLightMode parse(String modeString)
		{
			return modeString == null ? OFF : valueOf(modeString);
		}

		public static FrontLightMode readPref(SharedPreferences sharedPrefs)
		{
			return parse(sharedPrefs.getString(KEY_FRONT_LIGHT_MODE, null));
		}

	}

	private static final String TAG = "CameraConfiguration";
	public static final String KEY_FRONT_LIGHT_MODE = "preferences_front_light_mode";
	public static final String KEY_AUTO_FOCUS = "preferences_auto_focus";
	public static final String KEY_DISABLE_CONTINUOUS_FOCUS = "preferences_disable_continuous_focus";
	public static final String KEY_INVERT_SCAN = "preferences_invert_scan";

	// This is bigger than the size of a small screen, which is still supported.
	// The routine
	// below will still select the default (presumably 320x240) size for these.
	// This prevents
	// accidental selection of very low resolution on some devices.
	private static final int MIN_PREVIEW_PIXELS = 480 * 320; // normal screen
	private static final double MAX_ASPECT_DISTORTION = 0.15;
	private static final int MIN_FRAME_WIDTH = 240;
	private static final int MIN_FRAME_HEIGHT = 240;
	private static final int MAX_FRAME_WIDTH = 1200; // = 5/8 * 1920
	private static final int MAX_FRAME_HEIGHT = 675; // = 5/8 * 1080

	private final Context context;
	private Point screenResolution;
	private Point cameraResolution;
	private Rect framingRectInPreview;
	private Rect framingRect;
	private Camera camera;

	CamConfig(Context context)
	{
		this.context = context;
	}

	/**
	 * Reads, one time, values from the camera that are needed by the app.
	 */
	void initFromCameraParameters(Camera camera)
	{
		this.camera = camera;
		Camera.Parameters parameters = this.camera.getParameters();
		WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		Point theScreenResolution = new Point();
		display.getSize(theScreenResolution);
		screenResolution = theScreenResolution;
		Log.i(TAG, "Screen resolution: " + screenResolution);
		cameraResolution = findBestPreviewSizeValue(parameters, screenResolution);
		Log.i(TAG, "Camera resolution: " + cameraResolution);
	}

	void setDesiredCameraParameters(Camera camera, boolean safeMode)
	{
		Camera.Parameters parameters = camera.getParameters();

		if (parameters == null)
		{
			Log.w(TAG, "Device error: no camera parameters are available. Proceeding without configuration.");
			return;
		}

		Log.i(TAG, "Initial camera parameters: " + parameters.flatten());

		if (safeMode)
		{
			Log.w(TAG, "In camera config safe mode -- most settings will not be honored");
		}

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

		initializeTorch(parameters, prefs, safeMode);

		String focusMode = null;
		if (prefs.getBoolean(KEY_AUTO_FOCUS, true))
		{
			if (safeMode || prefs.getBoolean(KEY_DISABLE_CONTINUOUS_FOCUS, false))
			{
				focusMode = findSettableValue(parameters.getSupportedFocusModes(), Camera.Parameters.FOCUS_MODE_AUTO);
			} else
			{
				focusMode = findSettableValue(parameters.getSupportedFocusModes(), Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE,
						Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO, Camera.Parameters.FOCUS_MODE_AUTO);
			}
		}
		// Maybe selected auto-focus but not available, so fall through here:
		if (!safeMode && focusMode == null)
		{
			focusMode = findSettableValue(parameters.getSupportedFocusModes(), Camera.Parameters.FOCUS_MODE_MACRO,
					Camera.Parameters.FOCUS_MODE_EDOF);
		}
		if (focusMode != null)
		{
			parameters.setFocusMode(focusMode);
		}

		if (prefs.getBoolean(KEY_INVERT_SCAN, false))
		{
			String colorMode = findSettableValue(parameters.getSupportedColorEffects(), Camera.Parameters.EFFECT_NEGATIVE);
			if (colorMode != null)
			{
				parameters.setColorEffect(colorMode);
			}
		}

		parameters.setPreviewSize(cameraResolution.x, cameraResolution.y);
		camera.setParameters(parameters);

		Camera.Parameters afterParameters = camera.getParameters();
		Camera.Size afterSize = afterParameters.getPreviewSize();
		if (afterSize != null && (cameraResolution.x != afterSize.width || cameraResolution.y != afterSize.height))
		{
			Log.w(TAG, "Camera said it supported preview size " + cameraResolution.x + 'x' + cameraResolution.y
					+ ", but after setting it, preview size is " + afterSize.width + 'x' + afterSize.height);
			cameraResolution.x = afterSize.width;
			cameraResolution.y = afterSize.height;
		}
	}

	Point getCameraResolution()
	{
		return cameraResolution;
	}

	Point getScreenResolution()
	{
		return screenResolution;
	}

	boolean getTorchState(Camera camera)
	{
		if (camera != null)
		{
			Camera.Parameters parameters = camera.getParameters();
			if (parameters != null)
			{
				String flashMode = camera.getParameters().getFlashMode();
				return flashMode != null
						&& (Camera.Parameters.FLASH_MODE_ON.equals(flashMode) || Camera.Parameters.FLASH_MODE_TORCH.equals(flashMode));
			}
		}
		return false;
	}

	void setTorch(Camera camera, boolean newSetting)
	{
		Camera.Parameters parameters = camera.getParameters();
		doSetTorch(parameters, newSetting, false);
		camera.setParameters(parameters);
	}

	private void initializeTorch(Camera.Parameters parameters, SharedPreferences prefs, boolean safeMode)
	{
		boolean currentSetting = FrontLightMode.readPref(prefs) == FrontLightMode.ON;
		doSetTorch(parameters, currentSetting, safeMode);
	}

	private void doSetTorch(Camera.Parameters parameters, boolean newSetting, boolean safeMode)
	{
		String flashMode;
		if (newSetting)
		{
			flashMode = findSettableValue(parameters.getSupportedFlashModes(), Camera.Parameters.FLASH_MODE_TORCH,
					Camera.Parameters.FLASH_MODE_ON);
		} else
		{
			flashMode = findSettableValue(parameters.getSupportedFlashModes(), Camera.Parameters.FLASH_MODE_OFF);
		}
		if (flashMode != null)
		{
			parameters.setFlashMode(flashMode);
		}
	}

	private Point findBestPreviewSizeValue(Camera.Parameters parameters, Point screenResolution)
	{

		List<Camera.Size> rawSupportedSizes = parameters.getSupportedPreviewSizes();
		if (rawSupportedSizes == null)
		{
			Log.w(TAG, "Device returned no supported preview sizes; using default");
			Camera.Size defaultSize = parameters.getPreviewSize();
			return new Point(defaultSize.width, defaultSize.height);
		}

		// Sort by size, descending
		List<Camera.Size> supportedPreviewSizes = new ArrayList<Camera.Size>(rawSupportedSizes);
		Collections.sort(supportedPreviewSizes, new Comparator<Camera.Size>()
		{
			@Override
			public int compare(Camera.Size a, Camera.Size b)
			{
				int aPixels = a.height * a.width;
				int bPixels = b.height * b.width;
				if (bPixels < aPixels)
				{
					return -1;
				}
				if (bPixels > aPixels)
				{
					return 1;
				}
				return 0;
			}
		});

		if (Log.isLoggable(TAG, Log.INFO))
		{
			StringBuilder previewSizesString = new StringBuilder();
			for (Camera.Size supportedPreviewSize : supportedPreviewSizes)
			{
				previewSizesString.append(supportedPreviewSize.width).append('x').append(supportedPreviewSize.height).append(' ');
			}
			Log.i(TAG, "Supported preview sizes: " + previewSizesString);
		}

		double screenAspectRatio = (double) screenResolution.x / (double) screenResolution.y;

		// Remove sizes that are unsuitable
		Iterator<Camera.Size> it = supportedPreviewSizes.iterator();
		while (it.hasNext())
		{
			Camera.Size supportedPreviewSize = it.next();
			int realWidth = supportedPreviewSize.width;
			int realHeight = supportedPreviewSize.height;
			if (realWidth * realHeight < MIN_PREVIEW_PIXELS)
			{
				it.remove();
				continue;
			}

			boolean isCandidatePortrait = realWidth < realHeight;
			int maybeFlippedWidth = isCandidatePortrait ? realHeight : realWidth;
			int maybeFlippedHeight = isCandidatePortrait ? realWidth : realHeight;
			double aspectRatio = (double) maybeFlippedWidth / (double) maybeFlippedHeight;
			double distortion = Math.abs(aspectRatio - screenAspectRatio);
			if (distortion > MAX_ASPECT_DISTORTION)
			{
				it.remove();
				continue;
			}

			if (maybeFlippedWidth == screenResolution.x && maybeFlippedHeight == screenResolution.y)
			{
				Point exactPoint = new Point(realWidth, realHeight);
				Log.i(TAG, "Found preview size exactly matching screen size: " + exactPoint);
				return exactPoint;
			}
		}

		// If no exact match, use largest preview size. This was not a great
		// idea on older devices because
		// of the additional computation needed. We're likely to get here on
		// newer Android 4+ devices, where
		// the CPU is much more powerful.
		if (!supportedPreviewSizes.isEmpty())
		{
			Camera.Size largestPreview = supportedPreviewSizes.get(0);
			Point largestSize = new Point(largestPreview.width, largestPreview.height);
			Log.i(TAG, "Using largest suitable preview size: " + largestSize);
			return largestSize;
		}

		// If there is nothing at all suitable, return current preview size
		Camera.Size defaultPreview = parameters.getPreviewSize();
		Point defaultSize = new Point(defaultPreview.width, defaultPreview.height);
		Log.i(TAG, "No suitable preview sizes, using default: " + defaultSize);
		return defaultSize;
	}

	private static String findSettableValue(Collection<String> supportedValues, String... desiredValues)
	{
		Log.i(TAG, "Supported values: " + supportedValues);
		String result = null;
		if (supportedValues != null)
		{
			for (String desiredValue : desiredValues)
			{
				if (supportedValues.contains(desiredValue))
				{
					result = desiredValue;
					break;
				}
			}
		}
		Log.i(TAG, "Settable value: " + result);
		return result;
	}

	public synchronized Rect getFramingRect()
	{
		if (framingRect == null)
		{
			if (camera == null)
			{
				return null;
			}
			Point screenResolution = this.getScreenResolution();
			if (screenResolution == null)
			{
				// Called early, before init even finished
				return null;
			}

			int width = findDesiredDimensionInRange(screenResolution.x, MIN_FRAME_WIDTH, MAX_FRAME_WIDTH);
			int height = findDesiredDimensionInRange(screenResolution.y, MIN_FRAME_HEIGHT, MAX_FRAME_HEIGHT);

			int leftOffset = (screenResolution.x - width) / 2;
			int topOffset = (screenResolution.y - height) / 2;
			framingRect = new Rect(leftOffset, topOffset, leftOffset + width, topOffset + height);
			Log.d(TAG, "Calculated framing rect: " + framingRect);
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

	public synchronized Rect getFramingRectInPreview()
	{
		if (framingRectInPreview == null)
		{
			Rect framingRect = getFramingRect();
			if (framingRect == null)
			{
				return null;
			}
			Rect rect = new Rect(framingRect);
			Point cameraResolution = this.getCameraResolution();
			Point screenResolution = this.getScreenResolution();
			if (cameraResolution == null || screenResolution == null)
			{
				// Called early, before init even finished
				return null;
			}
			rect.left = rect.left * cameraResolution.x / screenResolution.x;
			rect.right = rect.right * cameraResolution.x / screenResolution.x;
			rect.top = rect.top * cameraResolution.y / screenResolution.y;
			rect.bottom = rect.bottom * cameraResolution.y / screenResolution.y;
			framingRectInPreview = rect;
		}
		return framingRectInPreview;
	}

}
