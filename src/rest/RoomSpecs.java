package rest;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import utils.json.JSONable;

/**
 * SAP STRUCT NAME : Z62_MROOMS_SPECS
 * <p>
 * 
 * @param dynamic
 *            The fields dynamically loaded from SAP. Are booleans.
 */
public class RoomSpecs implements JSONable
{
	private HashMap<String, Boolean> values;
	private HashMap<String, String> descriptions;

	public RoomSpecs()
	{
		values = new HashMap<String, Boolean>();
		descriptions = new HashMap<String, String>();
	}

	public RoomSpecs(JSONObject src)
	{
		values = new HashMap<String, Boolean>();
		this.fromJSONObject(src);
	}

	public HashMap<String, Boolean> GetValues()
	{
		return values;
	}

	public HashMap<String, String> GetDescriptions()
	{
		return descriptions;
	}

	public void Set(String key, boolean value)
	{
		values.put(key, value);
	}

	@Override
	public JSONObject toJSONObject()
	{
		JSONObject jo = new JSONObject();
		try
		{
			for (Map.Entry<String, Boolean> entry : values.entrySet())
				jo.put(entry.getKey(), ((entry.getValue()) ? "X" : ""));

		} catch (JSONException e)
		{
			e.printStackTrace();
			return null;
		}
		return jo;
	}

	/**
	 * JSON expected : [{"key":"xxxx", "description":"xxxx"},{"key":"xxxxx", "description":"xxxx"}]
	 */
	@Override
	public Object fromJSONObject(JSONObject src)
	{
		return null;
	}

	@Override
	public Object fromJSONObject(JSONArray src)
	{
		try
		{
			for (int i = 0; i < src.length(); i++)
			{
				JSONObject item = src.getJSONObject(i);
				values.put(item.getString("key"), false);
				descriptions.put(item.getString("key"), item.getString("description"));
			}

		} catch (JSONException e)
		{
			e.printStackTrace();
			return null;
		}
		return this;
	}
}
