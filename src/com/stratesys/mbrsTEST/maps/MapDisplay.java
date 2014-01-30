package com.stratesys.mbrsTEST.maps;

import com.stratesys.mbrsTEST.R;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.view.WindowManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapDisplay extends FragmentActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.layo_map);

		// Receiving latitude from MainActivity screen
		double latitude = getIntent().getDoubleExtra("lat", 0);

		// Receiving longitude from MainActivity screen
		double longitude = getIntent().getDoubleExtra("lng", 0);

		LatLng position = new LatLng(latitude, longitude);

		// Instantiating MarkerOptions class
		MarkerOptions options = new MarkerOptions();

		// Setting position for the MarkerOptions
		options.position(position);

		// Setting title for the MarkerOptions
		options.title("It's Here");

		// Setting snippet for the MarkerOptions
		options.snippet("Latitude:" + latitude + ",Longitude:" + longitude);

		// Getting Reference to SupportMapFragment of activity_map.xml
		SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

		// Getting reference to google map
		GoogleMap googleMap = fm.getMap();

		// Adding Marker on the Google Map
		googleMap.addMarker(options);

		CameraPosition cameraPosition = new CameraPosition.Builder().target(position).zoom(12).build();
		googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

	}
}
