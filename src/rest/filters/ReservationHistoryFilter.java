package rest.filters;

import org.json.JSONException;
import org.json.JSONObject;
import utils.json.JSONable;

public class ReservationHistoryFilter implements JSONable
{

	public String userID;
	public String beginDate;
	public int number_days_ago;

	public enum SAPFIELDS
	{
		USER_ID, BEGDA, NUMBER_DAYS_AGO
	};
	
	@Override
	public JSONObject toJSONObject()
	{
		JSONObject jo = new JSONObject();
		try
		{
			jo.put(SAPFIELDS.USER_ID.toString(), this.userID);
			jo.put(SAPFIELDS.BEGDA.toString(), this.beginDate);
			jo.put(SAPFIELDS.NUMBER_DAYS_AGO.toString(), this.number_days_ago);

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
			this.userID = src.getString(SAPFIELDS.USER_ID.toString());
			this.beginDate = src.getString(SAPFIELDS.BEGDA.toString());
			this.number_days_ago = Integer.valueOf(src.getString(SAPFIELDS.NUMBER_DAYS_AGO.toString()));

		} catch (JSONException e)
		{
			e.printStackTrace();
			return null;
		}
		return this;
	}

}
