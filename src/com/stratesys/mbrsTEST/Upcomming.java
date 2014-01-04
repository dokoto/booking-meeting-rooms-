package com.stratesys.mbrsTEST;

import java.util.ArrayList;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.TextView;

public class Upcomming extends Fragment
{

	private ListView lv_option_menu_list;
	private Adapter_BookingList Adapter;

	private class Adapter_row
	{
		public Adapter_row(String date, String subjet, String time, String room)
		{
			this.date = date;
			this.subjet = subjet;
			this.time = time;
			this.room = room;
		}

		public String date;
		public String subjet;
		public String time;
		public String room;
		final static private String date_sep = " - ";
		final static private String time_sep = " @ ";
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.layo_upcoming, container, false);
		LoadListOfBookings(rootView, inflater);
		return rootView;
	}

	private void LoadListOfBookings(View rootView, LayoutInflater inflater)
	{
		lv_option_menu_list = (ListView) rootView.findViewById(R.id.layo_upcoming_lv_options);
		lv_option_menu_list.setTextFilterEnabled(true);
		View footerView = inflater.inflate(R.layout.layo_upcoming_search, lv_option_menu_list, false);
		lv_option_menu_list.addHeaderView(footerView);
		EditText et_search = (EditText) rootView.findViewById(R.id.layo_upcoming_search_et_search);
		et_search.addTextChangedListener(new TextWatcher()
		{

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
				Adapter.getFilter().filter(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{
			}

			@Override
			public void afterTextChanged(Editable prefix)
			{
			}

		});

		final ArrayList<Adapter_row> adapter_row = GetMyUpcomingRooms();
		Adapter = new Adapter_BookingList(rootView.getContext(), adapter_row);
		lv_option_menu_list.setAdapter(Adapter);
	}

	private ArrayList<Adapter_row> GetMyUpcomingRooms()
	{
		ArrayList<Adapter_row> adapter_row = new ArrayList<Adapter_row>();
		adapter_row.add(new Adapter_row("Tomorrow", "Board Meeting", "15:30", "Sapphire Room"));
		adapter_row.add(new Adapter_row("Today", "XPT Project : OverView", "17:30", "Gold Room"));
		adapter_row.add(new Adapter_row("Today", "Budget Control : January", "09:30", "Expensive Room"));
		adapter_row.add(new Adapter_row("26/02/2014", "Evaluation Follow Up Part 2", "12:30", "Slave Room"));
		adapter_row.add(new Adapter_row("Tomorrow", "Board Meeting", "15:30", "Sapphire Room"));
		adapter_row.add(new Adapter_row("01/02/2014", "XPT Project : Part 1", "17:30", "Gold Room"));
		adapter_row.add(new Adapter_row("01/02/2014", "Budget Control : february", "09:30", "Expensive Room"));
		adapter_row.add(new Adapter_row("22/12/2014", "Evaluation Follow Up Part 3", "12:30", "Slave Room"));
		adapter_row.add(new Adapter_row("01/03/2014", "Board Meeting", "15:30", "Sapphire Room"));
		adapter_row.add(new Adapter_row("01/03/2014", "XPT Project : Part 2", "17:30", "Gold Room"));
		adapter_row.add(new Adapter_row("01/03/2014", "Budget Control : March", "09:30", "Expensive Room"));
		adapter_row.add(new Adapter_row("20/12/2014", "Evaluation Follow Up Part 4", "12:30", "Slave Room"));
		adapter_row.add(new Adapter_row("01/04/2014", "Board Meeting", "15:30", "Sapphire Room"));
		adapter_row.add(new Adapter_row("01/04/2014", "XPT Project : Part 3", "17:30", "Gold Room"));
		adapter_row.add(new Adapter_row("01/04/2014", "Budget Control : April", "09:30", "Expensive Room"));
		adapter_row.add(new Adapter_row("10/10/2014", "Evaluation Follow Up Part 5", "12:30", "Slave Room"));
		adapter_row.add(new Adapter_row("01/05/2014", "Board Meeting", "15:30", "Sapphire Room"));
		adapter_row.add(new Adapter_row("01/05/2014", "XPT Project : Part 4", "17:30", "Gold Room"));
		adapter_row.add(new Adapter_row("01/05/2014", "Budget Control : May", "09:30", "Expensive Room"));
		adapter_row.add(new Adapter_row("27/12/2014", "Evaluation Follow Up Part 6", "12:30", "Slave Room"));

		return adapter_row;
	}

	private static class ViewHolder
	{
		public TextView tv_date;
		public TextView tv_subjet;
		public TextView tv_time;
		public TextView tv_room;
		public TextView tv_date_sep;
		public TextView tv_time_sep;
	}

	public class Adapter_BookingList extends ArrayAdapter<Adapter_row>
	{
		public final Context ContextAdapter;
		public ArrayList<Adapter_row> Rows;
		public ArrayList<Adapter_row> OrigRows;
		ViewHolder holder;

		public Adapter_BookingList(Context context, ArrayList<Adapter_row> rows)
		{
			super(context, R.layout.layo_main, rows);
			this.ContextAdapter = context;
			this.Rows = rows;
			this.OrigRows = new ArrayList<Adapter_row>();
			OrigRows.addAll(Rows);
		}

		@Override
		public Filter getFilter()
		{
			return new Filter()
			{
				@SuppressWarnings("unchecked")
				@Override
				protected void publishResults(CharSequence constraint, FilterResults results)
				{
					if (results.count == 0)
						notifyDataSetInvalidated();
					else
					{
						Rows.clear();
						Rows.addAll((ArrayList<Adapter_row>) results.values);
						notifyDataSetChanged();
					}
				}

				@Override
				protected FilterResults performFiltering(CharSequence constraint)
				{
					final FilterResults oReturn = new FilterResults();
					if (constraint == null || constraint.length() == 0)
					{
						oReturn.values = OrigRows;
						oReturn.count = OrigRows.size();
					} else
					{
						ArrayList<Adapter_row> NewRows = new ArrayList<Adapter_row>();
						Rows.clear();
						Rows.addAll(OrigRows);

						for (Adapter_row row : OrigRows)
						{
							if (row.subjet.toLowerCase().startsWith(constraint.toString().toLowerCase()))
								NewRows.add(row);
						}
						oReturn.values = NewRows;
						oReturn.count = NewRows.size();
					}
					return oReturn;
				}
			};
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
				rowView = inflater.inflate(R.layout.layo_upcoming_list_item, parent, false);
				holder = new ViewHolder();
				holder.tv_date = (TextView) rowView.findViewById(R.id.layo_main_list_item_tv_date);
				holder.tv_subjet = (TextView) rowView.findViewById(R.id.layo_main_list_item_tv_subjet);
				holder.tv_time = (TextView) rowView.findViewById(R.id.layo_main_list_item_tv_time);
				holder.tv_room = (TextView) rowView.findViewById(R.id.layo_main_list_item_tv_room);
				holder.tv_date_sep = (TextView) rowView.findViewById(R.id.layo_main_list_item_tv_date_sep);
				holder.tv_time_sep = (TextView) rowView.findViewById(R.id.layo_main_list_item_tv_time_sep);

				rowView.setTag(holder);
			}

			holder = (ViewHolder) rowView.getTag();
			if (Rows.size() > 0)
			{
				holder.tv_date.setText(Rows.get(position).date);
				holder.tv_subjet.setText(Rows.get(position).subjet);
				holder.tv_time.setText(Rows.get(position).time);
				holder.tv_room.setText(Rows.get(position).room);
				holder.tv_date_sep.setText(Adapter_row.date_sep);
				holder.tv_time_sep.setText(Adapter_row.time_sep);
			}
			notifyDataSetChanged();
			return rowView;
		}
	}

}