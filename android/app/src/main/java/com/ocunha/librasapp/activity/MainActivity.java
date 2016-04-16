package com.ocunha.librasapp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.MenuItem;
import android.widget.TextView;

import com.ocunha.librasapp.R;
import com.ocunha.librasapp.fragment.HistoryFragment;
import com.ocunha.librasapp.fragment.ImageRecognizeFragment;
import com.ocunha.librasapp.fragment.WordListFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        Fragment fragment;
        String tag;
        if(savedInstanceState != null){
            tag = savedInstanceState.getString("tag");
            fragment = getSupportFragmentManager().getFragment(savedInstanceState, tag);
        } else {
            fragment = new WordListFragment();
            tag = "main";
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment, tag).commitAllowingStateLoss();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String tag = "content";
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);

        if(fragment == null) {
            tag = "main";
            fragment = getSupportFragmentManager().findFragmentByTag(tag);

        }
        outState.putString("tag", tag);
        getSupportFragmentManager().putFragment(outState, tag, fragment);
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

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.nav_dictionary:
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new WordListFragment(), "main").commit();
                break;
            case R.id.nav_history:
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new HistoryFragment(), "main").commit();
                break;
            case R.id.nav_search:
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new ImageRecognizeFragment(), "main").commit();
                break;

            case R.id.nav_share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                sendIntent.setType("text/plain");
                sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_message));
                startActivity(Intent.createChooser(sendIntent, getString(R.string.label_share)));
                break;
            case R.id.nav_info:
                final SpannableString s = new SpannableString(getString(R.string.about_message));
                Linkify.addLinks(s, Linkify.ALL);

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                AlertDialog dialog = builder.setIcon(R.drawable.ic_nav_header)
                        .setTitle(getString(R.string.app_name))
                        .setMessage(s)
                        .create();
                dialog.show();

                ((TextView)dialog.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
                break;

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}
