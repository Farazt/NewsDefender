package ie.cm.main;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.facebook.CallbackManager;
import java.util.ArrayList;
import java.util.List;
import ie.cm.db.DBManager;
import ie.cm.models.NewsItem;
//import com.kinvey.android.Client;


public class NewsDefender extends Application
{
    public DBManager dbManager=new DBManager(this);
    private RequestQueue mRequestQueue;
    private static NewsDefender mInstance;
    public List <NewsItem>  newsfeed = new ArrayList<NewsItem>();
    public static final String TAG = NewsDefender.class.getName();
    public static Bundle fbParameters;
    public static CallbackManager cbkManager;
   // final Client mKinveyClient = new Client.Builder("kid_r1Kmbnggb",
     //       "34f740edbc4b4fec811bc91aaa36cdc0",
    //        this.getApplicationContext()).build();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v("News Defender", "News Defender App Started");
        mInstance = this;
        fbParameters=new Bundle();
        cbkManager=CallbackManager.Factory.create();
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