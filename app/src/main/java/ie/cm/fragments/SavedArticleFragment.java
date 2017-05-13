package ie.cm.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import ie.cm.R;
import ie.cm.activities.Base;
import ie.cm.adapters.NewsListAdapter;
import ie.cm.api.NewsApi;
import ie.cm.api.VolleyListener;
import ie.cm.models.NewsItem;

/**
 * Created by ftahir on 14/04/17.
 *
 */

public class SavedArticleFragment extends Fragment implements AdapterView.OnItemLongClickListener,VolleyListener {

    protected static NewsListAdapter listAdapter;
    protected ListView listView;
    protected TextView backgroundtext;
    protected         String              title;
    public            boolean             favourites = false;
    protected TextView titleBar;
    List<NewsItem> cloud_news = new ArrayList<NewsItem>();

    //protected SwipeRefreshLayout mSwipeRefreshLayout;

    public SavedArticleFragment() {
        // Required empty public constructor
    }

    public static SavedArticleFragment newInstance() {
        SavedArticleFragment fragment = new SavedArticleFragment();
        return fragment;

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = null;
        v = inflater.inflate(R.layout.fragment_home, container, false);
        listView = (ListView) v.findViewById(R.id.newsList);
        backgroundtext=(TextView)v.findViewById(R.id.empty_list_view);
        Base.app.newsfeed=Base.app.dbManager.getAll();
        this.updateUI();
        return v;
    }
    public void setTitle(String title){
        this.title=title;
    }

    public void updateUI() {
        if(Base.app.newsfeed.size()==0 ){
            NewsApi.getSavedNews("/api/newsItem/");
            if(cloud_news.size()==Base.app.dbManager.getAll().size()){
                if(Base.app.dbManager.getAll().size()==0){
                backgroundtext.setText("No Articles Saved");}

            }
            else if(cloud_news.size()>0 && Base.app.dbManager.getAll().size()==0){
                for(NewsItem item:cloud_news){
                    Base.app.newsfeed.add(item);
                    Base.app.dbManager.insert(item);
                }
            }

        }

        titleBar = (TextView)getActivity().findViewById(R.id.recentAddedBarTextView);
        titleBar.setText(this.title);
        listAdapter = new NewsListAdapter(getActivity(), Base.app.newsfeed);
        listView.setAdapter (listAdapter);
        //listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        listView.setEmptyView(getActivity().findViewById(R.id.empty_list_view));
        listAdapter.notifyDataSetChanged(); // Update the adapter
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
       final int position=i;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are you sure you want to Delete this Article?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        NewsItem currentItem=Base.app.newsfeed.get(position);
                        Log.i("my-tag","Current item=" + currentItem.getNewsHeading());
                        NewsApi.delete("/api/newsItem/"+currentItem.getCloudId());
                        Base.app.dbManager.delete(currentItem.getNewsId());
                        listAdapter.newsFeed.remove(currentItem); // update adapters data
                        listAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();


        return false;
    }

    @Override
    public void setList(List list) {
        cloud_news=list;
        Log.i("News Size Cloud","Cloud"+cloud_news.size());
    }

    @Override
    public void setCloudValue(String Url, String cloudid) {

    }
}
