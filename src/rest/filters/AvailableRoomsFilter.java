package rest.filters;

import org.json.JSONException;
import org.json.JSONObject;

import rest.sap.structs.DateRange;
import rest.sap.structs.RoomAddrs;
import rest.sap.structs.RoomSpecs;

import utils.json.JSONable;

/**
 * SAP INTERFACE NAME : GET_AVAILABLE_MEETING_ROOMS
 * <p>
 * 
 * @param beginDate
 *            String MAX(8) YYYYMMDD
 * @param endDate
 *            String MAX8) YYYYMMDD
 * @param beginTime
 *            String MAX8) HHMMSS
 * @param endTime
 *            String MAX(6) HHMMSS
 * @param Features
 *            RoomSpecsFilter Object
 * @param Location
 *            RoomAddrsFilter Object
 * 
 * 
 */
public class AvailableRoomsFilter implements JSONable
{
	public DateRange interval;
	public RoomSpecs services;
	public RoomAddrs location;

	public enum SAPFIELDS
	{
		BEGDA, ENDDA, BEGUZ, ENDUZ, SERVICES, LOCATION
	};

	public AvailableRoomsFilter()
	{
		interval = new DateRange();
		services = new RoomSpecs();
		location = new RoomAddrs();
	}

	@Override
	public JSONObject toJSONObject()
	{
		JSONObject jo = new JSONObject();
		try
		{
			jo.put(SAPFIELDS.BEGDA.toString(), this.interval.beginDateSAP());
			jo.put(SAPFIELDS.ENDDA.toString(), this.interval.endDateSAP());
			jo.put(SAPFIELDS.BEGUZ.toString(), this.interval.beginTimeSAP());
			jo.put(SAPFIELDS.ENDUZ.toString(), this.interval.endTimeSAP());
			jo.put(SAPFIELDS.SERVICES.toString(), this.services.toJSONObject());
			jo.put(SAPFIELDS.LOCATION.toString(), this.location.toJSONObject());

		} catch (JSONException e)
		{
			e.printStackTrace();
			return null;
		}
		return jo;
	}

	@Override
	public Object fromJSONObject(JSONObject src)
	{
		try
		{
			this.interval.beginDate( src.getString(SAPFIELDS.BEGDA.toString()) );
			this.interval.endDate( src.getString(SAPFIELDS.ENDDA.toString()) );
			this.interval.beginTime( src.getString(SAPFIELDS.BEGUZ.toString()) );
			this.interval.endTime( src.getString(SAPFIELDS.ENDUZ.toString()) );
			this.services.fromJSONObject(src.getJSONObject(SAPFIELDS.SERVICES.toString()));
			this.location.fromJSONObject(src.getJSONObject(SAPFIELDS.LOCATION.toString()));

		} catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
		return this;
	}

}
