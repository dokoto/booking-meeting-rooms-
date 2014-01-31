package rest.sap.structs;

import org.json.JSONException;
import org.json.JSONObject;
import utils.json.JSONable;

public class StatusQuery implements JSONable
{

	public String errorType;
	public String errorMessage;
	
	public enum SAPFIELDS
	{
		TYPE, MESSAGE
	};
	
	@Override
	public JSONObject toJSONObject()
	{
		JSONObject jo = new JSONObject();
		try
		{
			jo.put(SAPFIELDS.TYPE.toString(), this.errorType);
			jo.put(SAPFIELDS.MESSAGE.toString(), this.errorMessage);

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
			this.errorType = src.getString(SAPFIELDS.TYPE.toString().toLowerCase());
			this.errorMessage = src.getString(SAPFIELDS.MESSAGE.toString().toLowerCase());

		} catch (JSONException e)
		{
			e.printStackTrace();
			return null;
		}
		return this;
	}


}
