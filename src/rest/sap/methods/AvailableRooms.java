package rest.sap.methods;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import rest.filters.AvailableRoomsFilter;
import rest.sap.structs.Room;
import utils.json.Conversions;
import android.os.AsyncTask;

public final class AvailableRooms extends REST
{
	private static String QUERY_ID = "01"; 
	/**
	 * 01: GET_AVAILABLE_MEETING_ROOMS
	 */
	public interface CallBackResultQuery
	{
		public void onQueryHasFinished(Room[] rooms);
	}
	
	public void get(final AvailableRoomsFilter filter, final CallBackResultQuery callBack)
	{
		try
		{

			AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>()
			{

				HttpClient httpclient = null;
				HttpPost httppost = null;
				HttpResponse response2level = null;
				HttpEntity responseEntity = null;
				JSONObject root = null;
				Room[] rooms = null;
				JSONArray items = null;
				
				@Override
				protected void onPreExecute()
				{
					try
					{
						httpclient = new DefaultHttpClient();
						httppost = new HttpPost(BuildUriPOST("query", QUERY_ID));
						httppost.setEntity(new StringEntity(filter.toJSONObject().toString()));
						httppost.setHeader("Content-Type", "application/json");
						httppost.setHeader("Accept", "application/json");
						//httppost.addHeader("Authorization", Base64.encodeToString("STRMAS:alfaro02".getBytes(), Base64.NO_WRAP));
					} catch (Exception e)
					{
						e.printStackTrace();
					}
				}

				@Override
				protected Void doInBackground(Void... arg0)
				{
					try
					{
						if (httpclient != null)
						{
							response2level = httpclient.execute(httppost);
							responseEntity = response2level.getEntity();
							root = (JSONObject)Conversions.FromUTF8InputStream(responseEntity.getContent());
						}
					} catch (Exception e)
					{
						e.printStackTrace();
					}
					return null;
				}

				@Override
				protected void onPostExecute(Void result)
				{
					if (responseEntity != null)
					{
						try
						{							
							/*
							 * FALTA LA GESTION DE ERRORES
							 */
							items = root.getJSONArray(JSON_TAG_ROOMS);
							rooms = new Room[items.length()];
							for (int i = 0; i < items.length(); i++)
							{
								rooms[i] = new Room();
								rooms[i].fromJSONObject(items.getJSONObject(i));
							}

							callBack.onQueryHasFinished(rooms);

						} catch (Exception e)
						{
							e.printStackTrace();
						}

					}
				}
			};
			task.execute((Void[]) null);

		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
}
