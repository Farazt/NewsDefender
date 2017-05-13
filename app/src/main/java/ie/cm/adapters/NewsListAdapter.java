package ie.cm.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ie.cm.R;
import ie.cm.activities.Base;
import ie.cm.models.NewsItem;



public class NewsListAdapter extends ArrayAdapter<NewsItem> {
  private Context context;
  private OnClickListener shareListener;
  public List<NewsItem> newsFeed;
  public NewsListAdapter(Context context,
                         List<NewsItem> coffeeList) {
    super(context, R.layout.news_item, coffeeList);
    this.newsFeed=coffeeList;
    this.context=context;
  }
  public NewsListAdapter(Context context,
                         List<NewsItem> coffeeList, OnClickListener shareListener) {
    super(context, R.layout.news_item, coffeeList);
    this.newsFeed=coffeeList;
    this.context=context;
    this.shareListener=shareListener;
  }
  @Override
  public View getView(int position, View convertView, ViewGroup parent){
    LayoutInflater inflater = (LayoutInflater) context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    if(convertView==null){
        convertView=inflater.inflate(R.layout.news_item, parent,false);
      }
      ImageView imgShare = (ImageView) convertView.findViewById(R.id.share);
      imgShare.setTag(Base.app.newsfeed.get(position));
      imgShare.setOnClickListener(shareListener);
      NewsItem currentItem=newsFeed.get(position);
      ImageView newsImage=(ImageView) convertView.findViewById(R.id.leftIco);
      TextView heading=(TextView) convertView.findViewById(R.id.heading);
      TextView desc=(TextView) convertView.findViewById(R.id.desc);
      heading.setText(currentItem.getNewsHeading());
      desc.setText(currentItem.getNewsDescSmall());
      Picasso.with(this.context).load(currentItem.getImageID()).into(newsImage);
      return convertView;

    }
  }







