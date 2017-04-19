package ie.cm.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONException;
import org.json.JSONObject;

import ie.cm.R;
import ie.cm.activities.Base;
import ie.cm.models.User;

/**
 * Created by ftahir on 14/04/17.
 */

public class SignInFragment extends Fragment {
    ProfilePictureView profilePictureView;
    LoginButton login;
    TextView fbName;
    TextView fbEmail;
    TextView fbGender;
    TextView fbdob;
    TextView titleBar;
    CallbackManager cbkManager;
    MenuItem signin;
    public SignInFragment() {
        // Required empty public constructor
    }
    public static SignInFragment newInstance() {
        SignInFragment fragment = new SignInFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Size app","Here"+ Base.app.newsfeed);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = null;
        v = inflater.inflate(R.layout.activity_login_fragment, container, false);
        cbkManager=CallbackManager.Factory.create();
        profilePictureView = (ProfilePictureView) v.findViewById(R.id.friendProfilePicture);
        fbName=(TextView) v.findViewById(R.id.loginName);
        fbEmail=(TextView) v.findViewById(R.id.emailtext);
        fbGender=(TextView) v.findViewById(R.id.gender);
        fbdob=(TextView) v.findViewById(R.id.dob);
        login =(LoginButton) v.findViewById(R.id.login_button);
        login.setFragment(this);
        titleBar = (TextView)getActivity().findViewById(R.id.recentAddedBarTextView);
        titleBar.setVisibility(v.GONE);
        fbName.setVisibility(v.GONE);
        attachLogin();
        if(Base.app.dbManager.get()!=null){
            User user=Base.app.dbManager.get();
            profilePictureView.setVisibility(v.VISIBLE);
            fbName.setVisibility(v.VISIBLE);
            profilePictureView.setProfileId(user.getFbID());
            fbName.setText(user.getFbName());
            fbEmail.setText(user.getEmail());
            fbGender.setText(user.getGender());
            fbdob.setText(user.getBirthday());
        }
        return v;
    }

    private void attachLogin(){
        AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken,
                                                       AccessToken currentAccessToken) {
                if (currentAccessToken == null) {
                    //Log.d(TAG, "onLogout catched");
                    //deleteContact();//This is my code
                    try{
                    Base.app.dbManager.deleteUser(Base.app.dbManager.get().getFbID());}
                    catch(Exception e){
                        Log.i("Delete","Deleted"+ e.toString());
                    }
                    titleBar.setText("Log In");
                    //titleBar.setText("");
                    Fragment currentFragment = getFragmentManager().findFragmentByTag("SignInFragment");
                    FragmentTransaction fragTransaction = getFragmentManager().beginTransaction();
                    fragTransaction.detach(currentFragment);
                    fragTransaction.attach(currentFragment);
                    fragTransaction.commit();

                    //fbtext.setText(Base.app.dbManager.get().getFbID());
                }
            }
        };
        login.registerCallback(cbkManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // Log.i("Login","Login Successful"+ loginResult);
                GraphRequest request1 = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                Log.v("LoginActivity Response ", response.toString());

                                try {
                                    String name = object.getString("name");
                                    String email= object.getString("email");
                                    String gender= object.getString("gender");
                                    String dob=object.getString("birthday");
                                    String id = object.getString("id");
                                    Log.i("DOB","DOB="+ dob);
                                    User userM=new User(id,name,email,gender,dob);
                                    Base.app.dbManager.insertUSER(userM);
                                    Fragment currentFragment = getFragmentManager().findFragmentByTag("SignInFragment");
                                    FragmentTransaction fragTransaction = getFragmentManager().beginTransaction();
                                    fragTransaction.detach(currentFragment);
                                    fragTransaction.attach(currentFragment);
                                    fragTransaction.commit();



                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender, birthday");
                request1.setParameters(parameters);
                request1.executeAsync();



            }

            @Override
            public void onCancel() {
                fbName.setText("Login Cancel");
                //Log.i("Login","Login Cancel");
            }

            @Override
            public void onError(FacebookException error) {
                fbName.setText("Login Error");
                //Log.i("Login","Login Successful"+ error);

            }
        });
        accessTokenTracker.startTracking();


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        cbkManager.onActivityResult(requestCode,resultCode,data);
    }
}
