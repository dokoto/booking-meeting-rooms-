package rest.sap.structs;

import org.json.JSONException;
import org.json.JSONObject;
import utils.json.JSONable;

/**
 * @param roomID
 *            String MAX(10) Numeric
 * @param name
 *            String MAX(80) Free text
 * @param Features
 *            RoomSpecsFilter Object
 * @param Location
 *            RoomAddrsFilter Object
 * 
 */
public class Room implements JSONable
{
	public String roomID;
	public String name;
	public RoomSpecs services;
	public RoomAddrs location;

	public enum SAPFIELDS
	{
		ROOMNR, NAME, SERVICES, LOCATION
	};

	public Room()
	{
		services = new RoomSpecs();
		location = new RoomAddrs();
	}

	public static Room findByID(Room[] datas, String ID)
	{
		for(Room item : datas)
		{
			if (item.name.compareTo(ID) == 0)
				return item;
		}
		
		return new Room();
	}
	
	@Override
	public JSONObject toJSONObject()
	{
		JSONObject jo = new JSONObject();
		try
		{
			jo.put(SAPFIELDS.ROOMNR.toString(), this.roomID);
			jo.put(SAPFIELDS.NAME.toString(), this.name);
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
			this.roomID = src.getString(SAPFIELDS.ROOMNR.toString().toLowerCase());
			this.name = src.getString(SAPFIELDS.NAME.toString().toLowerCase());
			this.services.fromJSONObject(src.getJSONObject(SAPFIELDS.SERVICES.toString().toLowerCase()));
			this.location.fromJSONObject(src.getJSONObject(SAPFIELDS.LOCATION.toString().toLowerCase()));

		} catch (JSONException e)
		{
			e.printStackTrace();
			return null;
		}
		return this;
	}

}
