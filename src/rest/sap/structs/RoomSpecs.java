package rest.sap.structs;

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
	public String key;
	public boolean is_active;
	public String description;	

	

	public void set(String key, boolean is_active)	
	{
		this.key = key;
		this.is_active = is_active;
	}
	
	public void set(String key, boolean is_active, String description)	
	{
		this.key = key;
		this.is_active = is_active;
		this.description = description;
	}
	
	@Override
	public JSONObject toJSONObject()
	{
		JSONObject jo = new JSONObject();
		try
		{
			jo.put("KEY", this.key);
			jo.put("IS_ACTIVE", this.is_active);
			jo.put("DESCRIPTION", this.description);

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
			this.key = src.getString("key");
			this.is_active = false;
			this.description = src.getString("description");
		} catch (JSONException e)
		{
			e.printStackTrace();
			return null;
		}
		return this;
	}

}
