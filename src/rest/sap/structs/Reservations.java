package rest.sap.structs;

import org.json.JSONException;
import org.json.JSONObject;
import utils.json.JSONable;

public class Reservations implements JSONable
{

	public String reservationID;
	public String roomID;
	public String userID;
	public DateRange interval;
	public boolean was_canceled;
	public boolean was_abused;

	public enum SAPFIELDS
	{
		ID, MROOM_ID, USER_ID, ENDDA, ENDUZ, BEGDA, BEGUZ, WAS_CANCELED, WAS_ABUSED
	};

	public Reservations()
	{
		interval = new DateRange();
	}

	@Override
	public JSONObject toJSONObject()
	{
		JSONObject jo = new JSONObject();
		try
		{
			jo.put(SAPFIELDS.ID.toString(), this.reservationID);
			jo.put(SAPFIELDS.MROOM_ID.toString(), this.roomID);
			jo.put(SAPFIELDS.USER_ID.toString(), this.userID);

			jo.put(SAPFIELDS.ENDDA.toString(), this.interval.endDateSAP());
			jo.put(SAPFIELDS.ENDUZ.toString(), this.interval.endTimeSAP());
			jo.put(SAPFIELDS.BEGDA.toString(), this.interval.beginDateSAP());
			jo.put(SAPFIELDS.BEGUZ.toString(), this.interval.beginTimeSAP());

			jo.put(SAPFIELDS.WAS_CANCELED.toString(), this.was_canceled);
			jo.put(SAPFIELDS.WAS_ABUSED.toString(), this.was_abused);

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
			this.reservationID = src.getString(SAPFIELDS.ID.toString().toLowerCase());
			this.roomID = src.getString(SAPFIELDS.MROOM_ID.toString().toLowerCase());
			this.userID = src.getString(SAPFIELDS.USER_ID.toString().toLowerCase());

			this.interval.endDate( src.getString(SAPFIELDS.ENDDA.toString().toLowerCase()) );
			this.interval.endTime( src.getString(SAPFIELDS.ENDUZ.toString().toLowerCase()) );
			this.interval.beginDate( src.getString(SAPFIELDS.BEGDA.toString().toLowerCase()) );
			this.interval.beginTime( src.getString(SAPFIELDS.BEGUZ.toString().toLowerCase()) );

			this.was_canceled = (src.getString(SAPFIELDS.WAS_CANCELED.toString().toLowerCase()).compareToIgnoreCase("X") == 0) ? true
					: false;
			this.was_abused = (src.getString(SAPFIELDS.WAS_ABUSED.toString().toLowerCase()).compareToIgnoreCase("X") == 0) ? true : false;

		} catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
		return this;
	}

}
