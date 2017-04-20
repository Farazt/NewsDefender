package ie.cm.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import java.util.List;
import ie.cm.R;
import ie.cm.activities.Base;
import ie.cm.adapters.NewsListAdapter;
import ie.cm.api.NewsApi;
import ie.cm.api.VolleyListener;
import ie.cm.models.NewsItem;
import ie.cm.utils.NewsApiALL;

/**
 * Created by ftahir on 11/04/17.
 */
public class NewsItemFragment extends Fragment implements AdapterView.OnItemClickListener,
                                                          AdapterView.OnItemLongClickListener,
                                                          AdapterView.OnClickListener,
                                                          VolleyListener
{
  protected static NewsListAdapter listAdapter;
  protected         ListView 			listView;
  protected         String              title;
  protected         TextView            titleBar;
  protected         SwipeRefreshLayout  mSwipeRefreshLayout;
  public            String                apiUrl= NewsApiALL.BBCHeadlines;
  protected         SearchView            searchView;
  ShareDialog       shareDialog;
  public NewsItemFragment() {
    // Required empty public constructor
  }

  @Override
  public void onAttach(Context context)
  {
    super.onAttach(context);
    //this.activity = (Base) context;
  }

  public static NewsItemFragment newInstance() {
    NewsItemFragment fragment = new NewsItemFragment();
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    shareDialog=new ShareDialog(this);

  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
      super.onCreateOptionsMenu(menu, inflater);
    MenuInflater inflaterM=inflater;
      inflaterM.inflate(R.menu.home,menu);
    MenuItem item=menu.findItem(R.id.menu_search);
      searchView=(SearchView) item.getActionView();
    searchLists();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View v = null;
    v = inflater.inflate(R.layout.fragment_home, container, false);
    listView = (ListView) v.findViewById(R.id.newsList);
      mSwipeRefreshLayout =   (SwipeRefreshLayout) v.findViewById(R.id.refresh_layout);
    setSwipeRefreshLayout();
    return v;
  }

  protected void setSwipeRefreshLayout()
  { //Check for swipte and add extra api calls here
    mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        if(Base.app.newsfeed.size()==0)
        {
          NewsApi.get(mSwipeRefreshLayout,apiUrl);
        }
        else{
          mSwipeRefreshLayout.setRefreshing(false);
        }
      }
    });
  }

  @Override
  public void onResume() {
    super.onResume();
    //updateUI(this);
    NewsApi.attachListener(this);
    NewsApi.get(mSwipeRefreshLayout,apiUrl);
  }
  @Override
  public void onPause() {
    super.onPause();
    NewsApi.detachListener();
  }
  @Override
  public void onStart()
  {
    super.onStart();
  }
  @Override
  public void onDetach() {
    super.onDetach();
    // mListener = null;
  }
  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    Bundle activityInfo = new Bundle();
    //ImageView myv=(ImageView) view.findViewById(R.id.share);
    //myv.setOnClickListener(this);
    NewsItem currentItem=Base.app.newsfeed.get(position);
    activityInfo.putString("Heading",currentItem.getNewsHeading());
    activityInfo.putString("Description",currentItem.getNewsDesc());
    activityInfo.putString("imageUrl",currentItem.getImageID());
    FragmentTransaction ft = getFragmentManager().beginTransaction();
    Fragment fragment = NewsDetailsFragment.newInstance(activityInfo);
    ft.replace(R.id.homeFrame, fragment);
    ft.addToBackStack(null);
    ft.commit();
  }
  @Override
  public void setList(List list) {
    Base.app.newsfeed = list;
    updateUI();
  }
  public void setTitle(String title){
    this.title=title;
  }

  public void updateUI() {

    titleBar = (TextView)getActivity().findViewById(R.id.recentAddedBarTextView);
    titleBar.setVisibility(View.VISIBLE);
    titleBar.setText(this.title);
    listAdapter = new NewsListAdapter(getActivity(), Base.app.newsfeed,this);
    listView.setAdapter (listAdapter);
    listView.setOnItemClickListener(this);
    listView.setOnItemLongClickListener(this);
    listView.setEmptyView(getActivity().findViewById(R.id.empty_list_view));
    listAdapter.notifyDataSetChanged(); // Update the adapter
  }

  @Override
  public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
    final int position=i;
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder.setMessage("Are you sure you want to save this Article?")
            .setCancelable(false)
            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int id) {
                NewsItem currentItem=Base.app.newsfeed.get(position);
                Log.i("my-tag","Current item=" + currentItem.getNewsHeading());
                Base.app.dbManager.insert(currentItem);
              }
            })
            .setNegativeButton("No", new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
              }
            });
    AlertDialog alert = builder.create();
    alert.show();
    return true;
  }

  //Share The news over social Media
  @Override
  public void onClick(final View view) {
    if(view.getTag() instanceof NewsItem){
      AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
      builder.setMessage("Are you sure you want to share this Article on Facebook?")
              .setCancelable(false)
              .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                  if(ShareDialog.canShow(ShareLinkContent.class)){
                    ShareLinkContent content = new ShareLinkContent.Builder()
                            .setContentUrl(Uri.parse(((NewsItem) view.getTag()).getUrl()))
                            .build();
                    shareDialog.show(content);
                  }
                }
              })
              .setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                  dialog.cancel();
                }
              });
      AlertDialog alert = builder.create();
      alert.show();
    }
  }

  protected void searchLists(){
    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String s) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            listAdapter.getFilter().filter(s);
            return false;
        }
    });
  }

}

