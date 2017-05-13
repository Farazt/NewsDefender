package ie.cm.api;

import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private static final String hostURL="http://87.44.18.206:8080";

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
        showDialog("Downloading News.....");
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
                        System.out.println("Unable to Fectch News!!!. Please Connect To Internet");
                        showDialog("Unable to Fectch News!!!. Please Connect To Internet");
                        mSwipeRefreshLayout.setRefreshing(false);
                        error.printStackTrace();
                        hideDialog();
                    }
                });
        myReq.setRetryPolicy(new DefaultRetryPolicy(
                10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        Base.app.add(myReq);
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////
    public static void post(String url, final NewsItem newsItem) {
        Log.v(TAG, "POSTing to : " + url);
        showDialog("Inserting a NewsItem to MongoDB...");
        Type objType = new TypeToken<NewsItem>(){}.getType();
        String json = new Gson().toJson(newsItem, objType);

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest gsonRequest = new JsonObjectRequest( Request.Method.POST,hostURL+url,

                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideDialog();
                        Log.v(TAG, "insert NewsItem " + response.toString());
                        try {
                            String date= response.getString("date");
                            String imageID=response.getString("imageID");
                            String url_cloud=response.getString("url");
                            String cloud_id=response.getString("_id");
                            Log.i("url","="+url_cloud);
                            vListener.setCloudValue(url_cloud,cloud_id);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.v(TAG, "Unable to insert new Coffee");
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        gsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
           Base.app.add(gsonRequest);

    }

    public static void delete(String url) {
        Log.v(TAG, "DELETEing from " + url);

        // Request a string response
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, hostURL + url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Result handling
                        Log.v(TAG, "Successfully Deleted " + response);
                        //vListener.updateUI((Fragment)vListener);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                // Error handling
                error.printStackTrace();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        // Add the request to the queue
        Base.app.add(stringRequest);
    }

    public static void getSavedNews(final String url){
        showDialog("Downloading News.....");
        JsonObjectRequest myReq=new JsonObjectRequest(Request.Method.GET,
                hostURL+url,
                null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray newsItems=response.getJSONArray("articles");
                            List<NewsItem> result = null;

                            for(int i=0 ;i<newsItems.length();i++){
                                JSONObject temp= newsItems.getJSONObject(i);
                                String title=temp.getString("newsHeading");
                                String description=temp.getString("newsDesc");
                                String publishedAt=temp.getString("date");
                                String url=temp.getString("url");
                                String urlToImage=temp.getString("imageID");
                                String author=temp.getString("author");
                                result.add(new NewsItem(title,description,publishedAt, publishedAt,url,urlToImage));
                            }
                            vListener.setList(result);
                           // mSwipeRefreshLayout.setRefreshing(false);
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
                        System.out.println("Unable to Fectch News!!!. Please Connect To Internet");
                        showDialog("Unable to Fectch News!!!. Please Connect To Internet");
                       // mSwipeRefreshLayout.setRefreshing(false);
                        error.printStackTrace();
                        hideDialog();
                    }
                });
        myReq.setRetryPolicy(new DefaultRetryPolicy(
                30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        Base.app.add(myReq);
    }





}