package ie.cm.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ie.cm.R;
import ie.cm.activities.Base;
import ie.cm.api.NewsApi;
import ie.cm.api.VolleyListener;

/**
 * Created by ftahir on 11/04/17.
 */

public class NewsDetailsFragment extends Fragment implements VolleyListener {

    TextView titleBar,titleName,titleShop;


    String newDescription;
    String imageUrl;
    String newsHeading;

    private OnFragmentInteractionListener mListener;

    public NewsDetailsFragment() {
        // Required empty public constructor
    }

    public static NewsDetailsFragment newInstance(Bundle coffeeBundle) {
        NewsDetailsFragment fragment = new NewsDetailsFragment();
        fragment.setArguments(coffeeBundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            newDescription = getArguments().getString("Description");
            imageUrl = getArguments().getString("imageUrl");
            newsHeading = getArguments().getString("Heading");
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void toggle(View v);
        void update(View v);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_news_details, container, false);
        ImageView newsImage=(ImageView) v.findViewById(R.id.newsImageFromApi);
        TextView  description=(TextView) v.findViewById(R.id.newsDetails);
        TextView heading=(TextView) v.findViewById(R.id.heading);
        heading.setText(newsHeading);
        description.setText(newDescription);
        Picasso.with(getActivity()).load(imageUrl).into(newsImage);
        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();

        //NewsApi.attachListener(this);
        //NewsApi.get();
        //titleBar = (TextView) getActivity().findViewById(R.id.recentAddedBarTextView);
        //titleBar.setText(R.string.updateACoffeeLbl);
    }

    @Override
    public void onPause() {
        super.onPause();
        NewsApi.detachListener();
    }

    @Override
    public void setList(List list) {
        Base.app.newsfeed = list;
    }




}


