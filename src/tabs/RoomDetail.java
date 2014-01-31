package tabs;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.stratesys.mbrsTEST.R;

import maps.MapDisplay;


import rest.sap.methods.AvailableServices;
import rest.sap.structs.RoomSpecs;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RoomDetail extends Activity
{
	private ImageButton ib_go_to_map;
	private TextView tv_address;
	private Geocoder geocoder;
	private LinearLayout ll_features_list;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layo_room_detail);
		LoadVars();
		LoadFeatures();
		LoadEvents();
	}

	private void LoadVars()
	{
		ib_go_to_map = (ImageButton) findViewById(R.id.layo_room_detail_ib_go_to_map);
		tv_address = (TextView) findViewById(R.id.layo_room_detail_tv_address);
		ll_features_list = (LinearLayout) findViewById(R.id.layo_room_detail_ll_room_features);
	}

	private void LoadEvents()
	{

		ib_go_to_map.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				List<Address> locationList;
				try
				{
					locationList = geocoder.getFromLocationName(tv_address.getText().toString(), 10);
					if (locationList != null)
					{
						Address address = locationList.get(0);
						Intent intent = new Intent(getBaseContext(), MapDisplay.class);
						intent.putExtra("lat", address.getLatitude());
						intent.putExtra("lng", address.getLongitude());
						startActivity(intent);
					}
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}


	private void LoadFeatures()
	{
		geocoder = new Geocoder(this, Locale.getDefault());
		AvailableServices QueryServices = new AvailableServices();		
		QueryServices.get(new AvailableServices.CallBackResultQuery()
		{
			@Override
			public void onQueryHasFinished(RoomSpecs[] services)
			{
				int i = 0;
				ll_features_list.removeAllViews();
				for (RoomSpecs service : services)
				{
					View v_features = View.inflate(RoomDetail.this, R.layout.layo_search_list_item, null);
					TextView tv_feature = (TextView) v_features.findViewById(R.id.layo_search_list_item_tv_feature);
					tv_feature.setText(service.description);
					LinearLayout ll = new LinearLayout(RoomDetail.this);
					ll.addView(v_features);
					ll_features_list.addView(ll, i++);
				}
			}
		});
	}
}
