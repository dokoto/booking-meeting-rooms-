/**
 * 
 */
package com.stratesys.mbrsTEST;

import java.util.ArrayList;
import com.stratesys.mbrsTEST.Search.Adapter_FeatureList;
import com.stratesys.mbrsTEST.Upcomming.Adapter_BookingList;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

public class RoomDetail extends Activity
{
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
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layo_room_detail);
		LoadFeatures();
	}

	
	private void LoadFeatures()
	{
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
