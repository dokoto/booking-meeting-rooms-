package tabs;

import com.stratesys.mbrsTEST.R;

import qr.QRScanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class QRscan extends Fragment
{
	private ImageButton ib_take_picture;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		//View rootView = inflater.inflate(R.layout.layo_qrscan, container, false);
		View rootView = inflater.inflate(R.layout.layo_qrscan, container, false);
		ib_take_picture = (ImageButton) rootView.findViewById(R.id.layo_qrscan_ib_take_picture);
		ib_take_picture.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				//Take a Picture
				//Intent intent = new Intent(getActivity(), TakePicture.class);
				//startActivityForResult(intent, 0);
				
				// QR Scan
				//Intent intent = new Intent(getActivity(), QRDecoder.class);
				Intent intent = new Intent(getActivity(), QRScanner.class);
				startActivityForResult(intent, 0);
			}
		});

		return rootView;
	}
}
