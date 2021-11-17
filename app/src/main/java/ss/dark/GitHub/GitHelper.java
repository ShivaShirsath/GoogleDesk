package ss.dark.GitHub;

import android.content.Context;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONObject;

public class GitHelper {
	public static Context context = null;
	public static JSONObject obj ;
	public static JSONArray array ;
	public GitHelper(Context context) {
		this.context = context;

	}
	public static JSONObject getJSONobject(String user) {
		Volley.newRequestQueue(context).add(
			new JsonObjectRequest(
				Request.Method.GET,
				"https://api.github.com/users/" + user,
				null,
				new Response.Listener<JSONObject>(){
					@Override
					public void onResponse(JSONObject response) {
						try {
							obj = response ;
						} catch (Exception e) {
							Toast.makeText(context, "JSON() :" + e.getMessage(), Toast.LENGTH_LONG).show();
						}
					}
				},
				new Response.ErrorListener(){
					@Override
					public void onErrorResponse(VolleyError e) {
						Toast.makeText(context, "JSON(error) :" + e.getMessage(), Toast.LENGTH_LONG).show();
					}
				}
			)
		);
		return obj;
	}
	public static JSONArray getJSONarray(String url) {
		Volley.newRequestQueue(context).add(
			new JsonArrayRequest(
				Request.Method.GET,
				url,
				null,
				new Response.Listener<JSONArray>(){
					@Override
					public void onResponse(JSONArray response) {
						try {
							array = response ;
						} catch (Exception e) {
							Toast.makeText(context, "JSON[] :" + e.getMessage(), Toast.LENGTH_LONG).show();
						}
					}
				},
				new Response.ErrorListener(){
					@Override
					public void onErrorResponse(VolleyError e) {
						Toast.makeText(context, "JSON[error] :" + e.getMessage(), Toast.LENGTH_LONG).show();
					}
				}
			)
		);
		return array ;
	}
}
