package ie.cm.activities;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import ie.cm.R;
import ie.cm.fragments.NewsItemFragment;
import ie.cm.fragments.SavedArticleFragment;
import ie.cm.fragments.SignInFragment;
import ie.cm.utils.NewsApiALL;

public class Home extends Base
        implements NavigationView.OnNavigationItemSelectedListener {
    public static NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        dialog = new ProgressDialog(this,1);


        FragmentTransaction ft = getFragmentManager().beginTransaction();
        NewsItemFragment fragment = NewsItemFragment.newInstance();
        fragment.setTitle("Headlines");
        ft.replace(R.id.homeFrame, fragment);
        ft.commit();

    }



    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        // http://stackoverflow.com/questions/32944798/switch-between-fragments-with-onnavigationitemselected-in-new-navigation-drawer
        int id = item.getItemId();
        Fragment fragment;
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if(id == R.id.sign_in){
            fragment = SignInFragment.newInstance();
            ft.replace(R.id.homeFrame, fragment,"SignInFragment");
            ft.addToBackStack(null);
            ft.commit();

        }
        else if (id == R.id.nav_headlines) {
            fragment = NewsItemFragment.newInstance();
            Base.app.newsfeed.clear();
            ((NewsItemFragment)fragment).setTitle("Headlines");
            ft.replace(R.id.homeFrame, fragment);
            ft.addToBackStack(null);
            ft.commit();
        } else if (id == R.id.nav_sports_news) {
            fragment = NewsItemFragment.newInstance();
            Base.app.newsfeed.clear();
            ((NewsItemFragment)fragment).setTitle("Sports News");
            ((NewsItemFragment)fragment).apiUrl= NewsApiALL.SportsUrl;
            ft.replace(R.id.homeFrame, fragment);
            ft.addToBackStack(null);
            ft.commit();

        } else if (id == R.id.nav_tech_news) {
            fragment = NewsItemFragment.newInstance();
            Base.app.newsfeed.clear();
            ((NewsItemFragment)fragment).setTitle("Tech News");
            ((NewsItemFragment)fragment).apiUrl= NewsApiALL.techCrunchUrl;
            ft.replace(R.id.homeFrame, fragment);
            ft.addToBackStack(null);
            ft.commit();

        } else if (id == R.id.nav_nature) {
            fragment = NewsItemFragment.newInstance();
            Base.app.newsfeed.clear();
            ((NewsItemFragment)fragment).setTitle("National Geographic");
            ((NewsItemFragment)fragment).apiUrl= NewsApiALL.nationalGeographicUrl;
            ft.replace(R.id.homeFrame, fragment);
            ft.addToBackStack(null);
            ft.commit();

        } else if (id == R.id.wall_street) {
            fragment = NewsItemFragment.newInstance();
            Base.app.newsfeed.clear();
            ((NewsItemFragment)fragment).setTitle("WSJ");
            ((NewsItemFragment)fragment).apiUrl= NewsApiALL.wallStreetJournalUrl;
            ft.replace(R.id.homeFrame, fragment);
            ft.addToBackStack(null);
            ft.commit();
        }
        else if (id == R.id.nav_reddit) {
            fragment = NewsItemFragment.newInstance();
            Base.app.newsfeed.clear();
            ((NewsItemFragment)fragment).setTitle("Reddit");
            ((NewsItemFragment)fragment).apiUrl= NewsApiALL.redditUrl;
            ft.replace(R.id.homeFrame, fragment);
            ft.addToBackStack(null);
            ft.commit();
        }
        else if (id == R.id.nav_financial) {
            fragment = NewsItemFragment.newInstance();
            Base.app.newsfeed.clear();
            ((NewsItemFragment)fragment).setTitle("Financial Times");
            ((NewsItemFragment)fragment).apiUrl= NewsApiALL.financialTimes;
            ft.replace(R.id.homeFrame, fragment);
            ft.addToBackStack(null);
            ft.commit();
        }
        else if (id == R.id.nav_economist) {
            fragment = NewsItemFragment.newInstance();
            Base.app.newsfeed.clear();
            ((NewsItemFragment)fragment).setTitle("The Economist");
            ((NewsItemFragment)fragment).apiUrl= NewsApiALL.economistUrl;
            ft.replace(R.id.homeFrame, fragment);
            ft.addToBackStack(null);
            ft.commit();
        }
        else if (id == R.id.nav_save) {
             fragment = SavedArticleFragment.newInstance();
             Base.app.newsfeed.clear();
            ((SavedArticleFragment)fragment).setTitle("Saved Articles");
            ft.replace(R.id.homeFrame, fragment);
            ft.addToBackStack(null);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
