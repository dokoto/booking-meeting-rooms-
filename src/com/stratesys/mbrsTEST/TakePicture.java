package com.stratesys.mbrsTEST;

import java.io.File;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;

public class TakePicture extends Activity
{
	private static String STORE_PATH;
	private boolean is_taken;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		STORE_PATH = Environment.getExternalStorageDirectory() + "/qr_shot.jpg";
		StarCamera();
	}

	private void StarCamera()
	{
		File file = new File(STORE_PATH);
		Uri outputFileUri = Uri.fromFile(file);

		Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

		startActivityForResult(intent, 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		Log.i("MakeMachine", "resultCode: " + resultCode);
		switch (resultCode)
		{
		case 0:
			Log.i("MakeMachine", "User cancelled");
			this.finish();
			break;

		case -1:
			is_taken = true;
			this.finish();
			break;
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		outState.putBoolean("PHOTO_TAKEN", is_taken);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState)
	{
		Log.i("MakeMachine", "onRestoreInstanceState()");
		if (savedInstanceState.getBoolean("PHOTO_TAKEN"))
		{
			is_taken = true;
		}
	}

}
