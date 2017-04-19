package ie.cm.api;

import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ie.cm.activities.Base;
import ie.cm.models.NewsItem;



public class NewsApi {


    private static       VolleyListener vListener;
    private static final String TAG = "News Defender";

    public static void attachListener(VolleyListener fragment)
    {
        vListener = fragment;
    }
    public static void detachListener()
    {
        vListener  = null;
    }

    private static void showDialog(String message) {
        Base.dialog.setMessage(message);
        if (!Base.dialog.isShowing())
                Base.dialog.show();
    }
    private static void hideDialog() {

        if (Base.dialog.isShowing())
            Base.dialog.dismiss();
    }

    public static void get( final SwipeRefreshLayout mSwipeRefreshLayout, final String url){
        //RequestQueue queue= Volley.newRequestQueue(this);
        //RequestQueue queue=Base.app.getRequestQueue();
        JsonObjectRequest myReq=new JsonObjectRequest(Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray newsItems=response.getJSONArray("articles");

                            for(int i=0 ;i<newsItems.length();i++){
                                JSONObject temp= newsItems.getJSONObject(i);
                                String title=temp.getString("title");

                                String description=temp.getString("description");
                                String publishedAt=temp.getString("publishedAt");
                                String url=temp.getString("url");
                                String urlToImage=temp.getString("urlToImage");
                                String author=temp.getString("author");
                                Base.app.newsfeed.add(new NewsItem(title,description,publishedAt, publishedAt,url,urlToImage));
                            }
                            vListener.setList(Base.app.newsfeed);
                            mSwipeRefreshLayout.setRefreshing(false);
                            hideDialog();
                        } catch (JSONException e) {
                            Log.i("tag","Response"+e);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Error handling
                        System.out.println("Something went wrong!");
                        mSwipeRefreshLayout.setRefreshing(false);
                        error.printStackTrace();
                    }
                });
        myReq.setRetryPolicy(new DefaultRetryPolicy(
                10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        Base.app.add(myReq);
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////////

}