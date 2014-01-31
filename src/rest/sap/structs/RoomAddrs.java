package rest.sap.structs;

import org.json.JSONException;
import org.json.JSONObject;

import utils.json.JSONable;

/**
 * SAP STRUCT NAME : Z62_MROOMS_ADDRS
 * <p>
 * 
 * @param country
 *            String MAX(80) Free text
 * @param city
 *            String MAX(80) Free text
 * @param building
 *            String MAX(80) Free text
 * @param floor
 *            String MAX(80) Free text
 * @param roomID
 *            String MAX(10) Numeric
 */
public class RoomAddrs implements JSONable
{
	public String country;
	public String city;
	public String building;
	public String floor;
	public String roomID;

	public enum SAPFIELDS
	{
		COUNTRY, CITY, BUILDING, FLOOR, ROOMNR
	};

	@Override
	public JSONObject toJSONObject()
	{
		JSONObject jo = new JSONObject();
		try
		{
			jo.put(SAPFIELDS.COUNTRY.toString(), this.country);
			jo.put(SAPFIELDS.CITY.toString(), this.city);
			jo.put(SAPFIELDS.BUILDING.toString(), this.building);
			jo.put(SAPFIELDS.FLOOR.toString(), this.floor);
			jo.put(SAPFIELDS.ROOMNR.toString(), this.roomID);
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
			this.country = src.getString(SAPFIELDS.COUNTRY.toString());
			this.city = src.getString(SAPFIELDS.CITY.toString());
			this.building = src.getString(SAPFIELDS.BUILDING.toString());
			this.floor = src.getString(SAPFIELDS.FLOOR.toString());
			this.roomID = src.getString(SAPFIELDS.ROOMNR.toString());
		} catch (JSONException e)
		{
			e.printStackTrace();
			return null;
		}
		return this;
	}

}
