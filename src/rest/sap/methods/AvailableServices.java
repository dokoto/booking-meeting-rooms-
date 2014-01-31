package rest.sap.methods;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import rest.sap.structs.RoomSpecs;
import utils.json.Conversions;
import android.os.AsyncTask;

public final class AvailableServices extends Query
{
	private static String QUERY_ID = "02"; 
	/**
	 * 02: GET_ROOM_SERVICES
	 */
	public interface CallBackResultQuery
	{
		public void onQueryHasFinished(RoomSpecs[] services);
	}
	
	public void get(final CallBackResultQuery callBack)
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
				JSONArray items = null;
				RoomSpecs[] services = null;

				@Override
				protected void onPreExecute()
				{
					try
					{
						httpclient = new DefaultHttpClient();
						httppost = new HttpPost(BuildUriPOST("query", QUERY_ID));
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
							root = (JSONObject) Conversions.FromUTF8InputStream(responseEntity.getContent());
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
							items = root.getJSONArray(JSON_TAG_SERVICES);
							services = new RoomSpecs[items.length()];
							for (int i = 0; i < items.length(); i++)
							{
								services[i] = new RoomSpecs();
								services[i].fromJSONObject(items.getJSONObject(i));
							}
							
						
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
}
