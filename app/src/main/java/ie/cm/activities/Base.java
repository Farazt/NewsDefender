package ie.cm.activities;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.facebook.FacebookSdk;

import ie.cm.R;
import ie.cm.main.NewsDefender;


public class Base extends AppCompatActivity {

	public static NewsDefender app = NewsDefender.getInstance();
	public static ProgressDialog dialog;

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
		currentVersion.setText("5.0");
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}
	public void menuInfo(MenuItem m)
    {
        openInfoDialog(this);
    }

	public void menuHome(MenuItem m)
    {
        goToActivity(this, Home.class, null);
    }

}
