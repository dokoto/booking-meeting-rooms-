package com.stratesys.mbrsTEST;

import java.util.ArrayList;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;

public class Search extends Fragment
{
	private SeekBar sb_capacity;
	private TextView tv_capacity;
	private ImageButton ib_booking_date;
	private EditText et_booking_date;
	private ImageButton ib_booking_time;
	private EditText et_booking_time;
	private ListView lv_features_list;
	private Adapter_FeatureList Adapter;

	
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{

		View rootView = inflater.inflate(R.layout.layo_search, container, false);
		sb_capacity = (SeekBar) rootView.findViewById(R.id.layo_search_sb_capacity);
		tv_capacity = (TextView) rootView.findViewById(R.id.layo_search_tv_capacity);
		ib_booking_date = (ImageButton) rootView.findViewById(R.id.layo_search_ib_booking_date);
		et_booking_date = (EditText) rootView.findViewById(R.id.layo_search_et_booking_date);
		ib_booking_time = (ImageButton) rootView.findViewById(R.id.layo_search_ib_booking_time);
		et_booking_time = (EditText) rootView.findViewById(R.id.layo_search_et_booking_time);
		sb_capacity.setMax(25);
		sb_capacity.setProgress(0);
		lv_features_list = (ListView) rootView.findViewById(R.id.layo_search_lv_room_features);
		final ArrayList<Adapter_row> adapter_row = new ArrayList<Adapter_row>();
		adapter_row.add(new Adapter_row("Wi-Fi", false));
		adapter_row.add(new Adapter_row("Conference Call", false));
		adapter_row.add(new Adapter_row("Flip Chart", false));
		adapter_row.add(new Adapter_row("White Board", false));
		adapter_row.add(new Adapter_row("Projector", false));
		adapter_row.add(new Adapter_row("Catering", false));
		adapter_row.add(new Adapter_row("Coffee Machine", false));
		Adapter = new Adapter_FeatureList(rootView.getContext(), adapter_row);
		lv_features_list.setAdapter(Adapter);

		sb_capacity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
		{

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
			{
				tv_capacity.setText("Capacity: " + String.valueOf(progress));
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar)
			{
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar)
			{
			}
		});

		ib_booking_date.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				DatePickerFragment date = new DatePickerFragment();				
				date.setCallBack(new OnDateSetListener()
				{
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
					{
						et_booking_date.setText(((dayOfMonth < 10) ? "0" : "") + String.valueOf(dayOfMonth) + "/"
								+ (((monthOfYear + 1) < 10) ? "0" : "") + String.valueOf(monthOfYear + 1) + "/" + String.valueOf(year));
					}
				});
				date.show(getFragmentManager(), "Booking Date");
			}

		});

		ib_booking_time.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				TimePickerFragmment time = new TimePickerFragmment();
				time.setCallBack(new OnTimeSetListener()
				{

					@Override
					public void onTimeSet(TimePicker view, int hourOfDay, int minute)
					{
						et_booking_time.setText(String.valueOf(hourOfDay) + ":" + String.valueOf(minute));

					}
				});
				time.show(getFragmentManager(), "Booking Time");
			}

		});

		return rootView;
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
			super(context, R.layout.layo_search, rows);
			this.ContextAdapter = context;
			this.Rows = rows;
		}


		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{			
			View rowView = convertView;
			if (position > Rows.size()-1)
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
				holder.cb_selected.setTag(Integer.valueOf(position));				 
				holder.cb_selected.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						int position = (Integer) v.getTag();
						if (Rows.get(position).selected)
							Rows.get(position).selected = false;
						else
							Rows.get(position).selected = true;
						
						notifyDataSetChanged();
					}
				});
			}
			notifyDataSetChanged();
			return rowView;
		}
	}
	
}
