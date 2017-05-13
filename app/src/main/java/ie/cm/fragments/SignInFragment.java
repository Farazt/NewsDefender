package ie.cm.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import ie.cm.R;
import ie.cm.activities.Base;
import ie.cm.activities.Home;
import ie.cm.models.User;

/**
 * Created by ftahir on 14/04/17.
 */

public class SignInFragment extends Fragment implements SignInButton.OnClickListener,
                                            GoogleApiClient.OnConnectionFailedListener{
    ProfilePictureView profilePictureView;
    LoginButton login;
    TextView fbName;
    TextView fbEmail;
    TextView fbGender;
    TextView fbdob;
    TextView titleBar;
    SignInButton signInButton_Google;
    MenuItem signin;
    private static final String TAG = "SignInActivity";


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
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
            Base.mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage((FragmentActivity) getActivity() /* FragmentActivity */, new GoogleApiClient.OnConnectionFailedListener() {
                   @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Log.i("Connection Failed","="+connectionResult.toString());
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = null;
        v = inflater.inflate(R.layout.activity_login_fragment, container, false);
        profilePictureView = (ProfilePictureView) v.findViewById(R.id.friendProfilePicture);
        signInButton_Google =(SignInButton) v.findViewById(R.id.sign_in_button);
        fbName=(TextView) v.findViewById(R.id.loginName);
        fbEmail=(TextView) v.findViewById(R.id.emailtext);
        fbGender=(TextView) v.findViewById(R.id.gender);
        fbdob=(TextView) v.findViewById(R.id.dob);
        login =(LoginButton) v.findViewById(R.id.login_button);
        login.setPublishPermissions("publish_actions");
        signInButton_Google.setOnClickListener(this);

        login.setFragment(this);
        try{
            if(!Base.app.dbManager.get().getFbName().isEmpty()){
                updateMenuTitles(Base.app.dbManager.get().getFbName());
            }}catch (Exception e){
            updateMenuTitles("Log in");
        }
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

    private void updateMenuTitles(String title) {
        Menu menu= Home.navigationView.getMenu();
        MenuItem menuItem = menu.findItem(R.id.sign_in);
        menuItem.setTitle(title);
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
                        titleBar.setText("Log In");
                        //titleBar.setText("");
                        Fragment currentFragment = getFragmentManager().findFragmentByTag("SignInFragment");
                        FragmentTransaction fragTransaction = getFragmentManager().beginTransaction();
                        fragTransaction.detach(currentFragment);
                        fragTransaction.attach(currentFragment);
                        fragTransaction.commit();
                    }
                }
            }
        };
        login.registerCallback(Base.app.cbkManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // Log.i("Login","Login Successful"+ loginResult);
                GraphRequest request = GraphRequest.newMeRequest(
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
                request.setParameters(parameters);
                request.executeAsync();
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
        if (requestCode == Base.RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        else{
        Base.app.cbkManager.onActivityResult(requestCode,resultCode,data);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            // ...
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(Base.mGoogleApiClient);
        startActivityForResult(signInIntent, Base.RC_SIGN_IN);
    }
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            Log.i("Google-SignIn","="+ acct.toString());
            Base.signedIn=true;
            Toast.makeText(getActivity(), "Signed In", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getActivity(), Home.class);
            SignInFragment.this.startActivity(intent);
            // acct.getDisplayName()

        } else {
            // Signed out, show unauthenticated UI.
        }
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i("Google-SignIn-Failed","="+connectionResult.toString());
    }
}
