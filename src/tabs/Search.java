package tabs;

import java.util.Date;

import com.stratesys.mbrsTEST.R;

import rest.filters.AvailableRoomsFilter;
import rest.sap.methods.AvailableRooms;
import rest.sap.methods.AvailableServices;
import rest.sap.structs.Room;
import rest.sap.structs.RoomSpecs;
import utils.time.DatePickerFragment;
import utils.time.TimePickerFragmment;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
	private Button bt_search;
	private LinearLayout ll_features_list;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{

		View rootView = InitVars(inflater, container, savedInstanceState);
		InitEvents();

		return rootView;
	}

	private View InitVars(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{

		View rootView = inflater.inflate(R.layout.layo_search, container, false);
		bt_search = (Button) rootView.findViewById(R.id.layo_search_bt_search);
		ll_features_list = (LinearLayout) rootView.findViewById(R.id.layo_search_ll_room_features);
		sb_capacity = (SeekBar) rootView.findViewById(R.id.layo_search_sb_capacity);
		tv_capacity = (TextView) rootView.findViewById(R.id.layo_search_tv_capacity);
		ib_booking_date = (ImageButton) rootView.findViewById(R.id.layo_search_ib_booking_date);
		et_booking_date = (EditText) rootView.findViewById(R.id.layo_search_et_booking_date);
		ib_booking_time = (ImageButton) rootView.findViewById(R.id.layo_search_ib_booking_time);
		et_booking_time = (EditText) rootView.findViewById(R.id.layo_search_et_booking_time);

		sb_capacity.setMax(25);
		sb_capacity.setProgress(2);

		getServicesDescriptions(rootView, inflater, container);

		return rootView;
	}

	private void InitEvents()
	{
		bt_search.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				executeSearch();
			}
		});

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
						et_booking_time.setText(((hourOfDay < 10) ? "0" : "") + String.valueOf(hourOfDay) + ":"
								+ ((minute < 10) ? "0" : "") + String.valueOf(minute));
					}
				});
				time.show(getFragmentManager(), "Booking Time");
			}

		});
	}

	private void getServicesDescriptions(final View rootView, final LayoutInflater inflater, final ViewGroup container)
	{
		AvailableServices QueryServices = new AvailableServices();
		QueryServices.get(new AvailableServices.CallBackResultQuery()
		{
			@Override
			public void onQueryHasFinished(RoomSpecs[] services)
			{
				int i = 0;
				for (RoomSpecs service : services)
				{
					View v_features = inflater.inflate(R.layout.layo_search_list_item, container, false);
					TextView tv_feature = (TextView) v_features.findViewById(R.id.layo_search_list_item_tv_feature);
					tv_feature.setText(service.description);
					LinearLayout ll_item = new LinearLayout(getActivity());
					ll_item.addView(v_features);
					ll_features_list.addView(ll_item, i++);
				}
			}
		});
	}

	private void executeSearch()
	{
		try
		{
			AvailableRoomsFilter filter = new AvailableRoomsFilter();

			filter.interval.beginDate("01/01/2014");
			filter.interval.endDate("01/01/2015");
			filter.interval.beginTime("01:00:00");
			filter.interval.endTime("01:00:00");

			filter.location.country = "Spain";
			filter.location.city = "Madrid";
			filter.location.building = "Norte 2";
			filter.location.floor = "2º";
			filter.location.roomID = "201";

			filter.services.set("HAS_CONFCALL", true);
			filter.services.set("HAS_VIDCALL", true);
			filter.services.set("HAS_INTERNET", true);
			filter.services.set("HAS_FLIPCHART", true);
			filter.services.set("HAS_WHITEBOARD", true);

			AvailableRooms QueryAvailableRooms = new AvailableRooms();
			QueryAvailableRooms.get(filter, new AvailableRooms.CallBackResultQuery()
			{
				@Override
				public void onQueryHasFinished(Room[] rooms)
				{
				}
			});
		} catch (Exception e)
		{
			e.printStackTrace();
		}

	}
}
