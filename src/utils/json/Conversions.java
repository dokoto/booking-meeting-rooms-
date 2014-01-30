package utils.json;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class Conversions
{
	public static Object FromUTF8InputStream(InputStream is) throws IOException, JSONException
	{
		InputStreamReader reader = new InputStreamReader(is, "UTF-8");
		StringBuilder builder = new StringBuilder();
		char[] buf = new char[1000];
		int l = 0;
		while (l >= 0) {
		    builder.append(buf, 0, l);
		    l = reader.read(buf);
		}

		Object jsonTest = new JSONTokener(builder.toString()).nextValue();
		if (jsonTest instanceof JSONObject)
			return new JSONObject(new JSONTokener(builder.toString()));
		else if (jsonTest instanceof JSONArray)
			return new JSONArray(new JSONTokener(builder.toString()));
		else 
			return null;
			
	}
}
