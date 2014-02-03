package rest.sap.methods;

import android.net.Uri;

public abstract class REST
{
	protected static final String HOST = "demo.stratesys-ts.com";
	protected static final String PROTOCOL = "http";
	protected static final int PORT = 8030;
	protected static final String PATH_RESOURCE = "/prc62/roomreservation/%s/%s.%s";
	//protected static final String PATH_NO_RESOURCE = "/prc62/roomreservation/%s";
	
	protected static final String JSON_TAG_ROOMS = "rooms";
	protected static final String JSON_TAG_RESERVATIONS = "reservations";
	protected static final String JSON_TAG_STATUS_QUERY = "status_query";
	protected static final String JSON_TAG_SERVICES = "services";
	protected static final String JSON_TAG_LOCATION = "location";

	protected interface CallBackResultQuery
	{
	}
	
	protected void get() {}
	protected void put() {}
	protected void delete() {}

	protected String BuildUriPOST(String rest_name, String query_id)
	{
		String path = String.format(PATH_RESOURCE, rest_name, query_id, "JSON");
		String host = PROTOCOL + "://" + HOST + ":" + String.valueOf(PORT);
		Uri.Builder builderURL = Uri.parse(host).buildUpon();
		builderURL.path(path);

		return builderURL.build().toString();
	}
}
