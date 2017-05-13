package ie.cm.activities;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;

import ie.cm.R;
import ie.cm.main.NewsDefender;

public class Base extends AppCompatActivity {

	public static NewsDefender app = NewsDefender.getInstance();
	public static ProgressDialog dialog;
	/* Client used to interact with Google APIs. */
	public static GoogleApiClient mGoogleApiClient;
	public static final int RC_SIGN_IN = 9001;
	public static boolean signedIn = false;
	public static String googleToken;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app = (NewsDefender) getApplication();
		FacebookSdk.sdkInitialize(getApplicationContext());
	}

	protected void goToActivity(Activity current,
								Class<? extends Activity> activityClass,
								Bundle bundle) {
		Intent newActivity = new Intent(current, activityClass);
		if (bundle != null) newActivity.putExtras(bundle);
		current.startActivity(newActivity);
	}

	public void openInfoDialog(Activity current) {
		Dialog dialog = new Dialog(current);
		dialog.setTitle("About New Defender");
		dialog.setContentView(R.layout.info);
		TextView currentVersion = (TextView) dialog
				.findViewById(R.id.versionTextView);

		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	public void menuInfo(MenuItem m) {
		openInfoDialog(this);
	}

	public void menuHome(MenuItem m)
    {
        goToActivity(this, Home.class, null);
    }
	public void logout(MenuItem item) {

		Log.v("News Defender","Logging out from: " + mGoogleApiClient);
		Base.signedIn = mGoogleApiClient.isConnected();

		if (Base.signedIn) {
			Log.v("News Defender","Logging out from: " + mGoogleApiClient);
			Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
			mGoogleApiClient.disconnect();
			Base.googleToken = "";
			Base.signedIn = mGoogleApiClient.isConnected();
			mGoogleApiClient.connect();
			Log.v("News Defender","googleClient Connected: " + Base.signedIn);
			Toast.makeText(this, "Signed out of Google", Toast.LENGTH_LONG).show();
			Log.v("News Defender", "News Defender App Terminated");
		}


	}

}
