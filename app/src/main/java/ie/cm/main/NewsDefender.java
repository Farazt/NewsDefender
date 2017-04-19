package ie.cm.main;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import java.util.ArrayList;
import java.util.List;
import ie.cm.db.DBManager;
import ie.cm.models.NewsItem;

public class NewsDefender extends Application
{   public DBManager dbManager=new DBManager(this);
    private RequestQueue mRequestQueue;
    private static NewsDefender mInstance;
    public List <NewsItem>  newsfeed = new ArrayList<NewsItem>();
    public static final String TAG = NewsDefender.class.getName();
    public static Bundle fbParameters;
    @Override
    public void onCreate() {
        super.onCreate();
        Log.v("News Defender", "News Defender App Started");
        mInstance = this;
        fbParameters=new Bundle();
        mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        dbManager.open();
    }

    public static synchronized NewsDefender getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    public <T> void add(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancel() {
        mRequestQueue.cancelAll(TAG);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        dbManager.close();
    }
}