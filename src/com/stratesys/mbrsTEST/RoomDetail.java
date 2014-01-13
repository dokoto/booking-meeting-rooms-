package com.stratesys.mbrsTEST;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class RoomDetail extends Activity
{
	private ListView lv_features_list;
	private Adapter_FeatureList Adapter;
	private ImageButton ib_go_to_map;
	private TextView tv_address;
	private Geocoder geocoder;

	private class Adapter_row
	{
		public Adapter_row(String feature, boolean selected)
		{
			this.feature = feature;
			this.selected = selected;
		}

		public String feature;
		public boolean selected;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layo_room_detail);
		LoadFeatures();
		LoadEvents();
	}

	private void LoadEvents()
	{
		ib_go_to_map = (ImageButton) findViewById(R.id.layo_room_detail_ib_go_to_map);
		ib_go_to_map.setOnClickListener(new OnClickListener() {

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
						String uri = String.format(Locale.getDefault(), "geo:%f,%f", address.getLatitude(), address.getLongitude());
						Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
						startActivity(intent);
					}
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}});
	}
	
	private void LoadFeatures()
	{
		geocoder = new Geocoder(this, Locale.getDefault());
		tv_address = (TextView) findViewById(R.id.layo_room_detail_tv_address);
		lv_features_list = (ListView) findViewById(R.id.layo_room_detail_lv_room_features);		
		final ArrayList<Adapter_row> adapter_row = new ArrayList<Adapter_row>();
		adapter_row.add(new Adapter_row("Wi-Fi", false));
		adapter_row.add(new Adapter_row("Conference Call", false));
		adapter_row.add(new Adapter_row("Flip Chart", false));
		adapter_row.add(new Adapter_row("White Board", false));
		adapter_row.add(new Adapter_row("Projector", false));
		adapter_row.add(new Adapter_row("Catering", false));
		adapter_row.add(new Adapter_row("Coffee Machine", false));
		Adapter = new Adapter_FeatureList(this, adapter_row);
		lv_features_list.setAdapter(Adapter);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	private static class ViewHolder
	{
		public TextView tv_feature;
		public CheckBox cb_selected;
	}

	public class Adapter_FeatureList extends ArrayAdapter<Adapter_row>
	{
		public final Context ContextAdapter;
		public ArrayList<Adapter_row> Rows;
		ViewHolder holder;

		public Adapter_FeatureList(Context context, ArrayList<Adapter_row> rows)
		{
			super(context, R.layout.layo_room_detail, rows);
			this.ContextAdapter = context;
			this.Rows = rows;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			View rowView = convertView;
			if (position > Rows.size() - 1)
				return rowView;
			if (rowView == null)
			{
				LayoutInflater inflater = (LayoutInflater) ContextAdapter.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				rowView = inflater.inflate(R.layout.layo_search_list_item, parent, false);
				holder = new ViewHolder();
				holder.tv_feature = (TextView) rowView.findViewById(R.id.layo_search_list_item_tv_feature);
				holder.cb_selected = (CheckBox) rowView.findViewById(R.id.layo_search_list_item_cb_selected);

				rowView.setTag(holder);
			}

			holder = (ViewHolder) rowView.getTag();
			if (Rows.size() > 0)
			{
				holder.tv_feature.setText(Rows.get(position).feature);
				holder.cb_selected.setChecked(Rows.get(position).selected);
				holder.cb_selected.setEnabled(false);
			}
			notifyDataSetChanged();
			return rowView;
		}
	}
}
