package rest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
public class RoomFilter implements JSONable
{
	public String beginDate;
	public String endDate;
	public String beginTime;
	public String endTime;
	public RoomSpecs services;
	public RoomAddrs location;

	
	public enum SAPFIELDS
	{
		BEGDA, ENDDA, BEGUZ, ENDUZ, SERVICES, LOCATION
	};
		
	@Override
	public JSONObject toJSONObject()
	{
		JSONObject jo = new JSONObject();
		try
		{
			jo.put(SAPFIELDS.BEGDA.toString(), this.beginDate);
			jo.put(SAPFIELDS.ENDDA.toString(), this.endDate);
			jo.put(SAPFIELDS.BEGUZ.toString(), this.beginTime);
			jo.put(SAPFIELDS.ENDUZ.toString(), this.endTime);
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
			this.beginDate = src.getString(SAPFIELDS.BEGDA.toString());
			this.endDate = src.getString(SAPFIELDS.ENDDA.toString());
			this.beginTime = src.getString(SAPFIELDS.BEGUZ.toString());
			this.endTime = src.getString(SAPFIELDS.ENDUZ.toString());			
			this.services.fromJSONObject( src.getJSONObject(SAPFIELDS.SERVICES.toString()) );
			this.location.fromJSONObject( src.getJSONObject(SAPFIELDS.LOCATION.toString()) );
			
		} catch (JSONException e)
		{
			e.printStackTrace();
			return null;
		}
		return this;
	}

	@Override
	public Object fromJSONObject(JSONArray src)
	{
		// TODO Auto-generated method stub
		return null;
	}
}
