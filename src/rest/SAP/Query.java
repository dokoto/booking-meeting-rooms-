package rest.SAP;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.net.Uri;
import android.os.AsyncTask;

import rest.Room;
import rest.RoomFilter;
import rest.RoomSpecs;
import utils.json.Conversions;

public class Query
{
	private static final String HOST = "demo.stratesys-ts.com";
	private static final String PROTOCOL = "http";
	private static final int PORT = 8030;
	private static final String PATH_RESOURCE = "/prc62/roomreservation/%s/%s.%s";

	//	private static final String PATH_NO_RESOURCE = "/prc62/roomreservation/%s";

	/**
	 * 01: GET_AVAILABLE_MEETING_ROOMS
	 */
	public interface CallBackResultQuery01
	{
		public void onQueryHasFinished(Room[] rooms);
	}

	/**
	 * 02: GET_ROOM_SERVICES
	 */
	public interface CallBackResultQuery02
	{
		public void onQueryHasFinished(RoomSpecs services);
	}

	public void get_available_meeting_rooms(final RoomFilter filter, final CallBackResultQuery01 callBack)
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

				@Override
				protected void onPreExecute()
				{
					try
					{
						httpclient = new DefaultHttpClient();
						httppost = new HttpPost(BuildUriPOST("query", "01"));
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
							JSONArray items = root.getJSONArray("meeting_rooms");
							Room[] rooms = new Room[items.length()];
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

	public void get_room_services(final CallBackResultQuery02 callBack)
	{
		try
		{

			AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>()
			{

				HttpClient httpclient = null;
				HttpPost httppost = null;
				HttpResponse response2level = null;
				HttpEntity responseEntity = null;
				JSONArray root = null;

				@Override
				protected void onPreExecute()
				{
					try
					{
						httpclient = new DefaultHttpClient();
						httppost = new HttpPost(BuildUriPOST("query", "02"));
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
							root = (JSONArray)Conversions.FromUTF8InputStream(responseEntity.getContent());
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
							RoomSpecs services = new RoomSpecs();
							services.fromJSONObject(root);
							callBack.onQueryHasFinished(services);

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

	private String BuildUriPOST(String rest_name, String query_id)
	{
		String path = String.format(PATH_RESOURCE, rest_name, query_id, "JSON");
		String host = PROTOCOL + "://" + HOST + ":" + String.valueOf(PORT);
		Uri.Builder builderURL = Uri.parse(host).buildUpon();
		builderURL.path(path);

		return builderURL.build().toString();
	}
}
