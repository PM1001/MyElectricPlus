package org.emoncms.myapps;

import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.support.v7.widget.Toolbar;


public class MainActivity extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks
{
    boolean isFullScreen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        isFullScreen = SP.getBoolean("display_fullscreen", false);

        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getFragmentManager();
        NavigationDrawerFragment mNavigationDrawerFragment;
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                fragmentManager.findFragmentById(R.id.navigation_drawer);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);

        DrawerLayout Drawer = (DrawerLayout) findViewById(R.id.drawer_layout);        // Drawer object Assigned to the view
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                Drawer);

        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this,Drawer,toolbar,R.string.openDrawer,R.string.closeDrawer);
        Drawer.setDrawerListener(mDrawerToggle); // Drawer Listener set to the Drawer toggle
        mDrawerToggle.syncState();               // Finally we set the drawer toggle sync State

        fragmentManager.beginTransaction()
                .replace(R.id.container, new MyElectricMainFragment(), getResources().getString(R.string.tag_me_fragment))
                .commit();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (isFullScreen && hasFocus)
            setFullScreen();
    }

    void setFullScreen()
    {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                            | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
        else if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
        {
            int mUIFlag = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;

            getWindow().getDecorView().setSystemUiVisibility(mUIFlag);
        }
        else
        {
            int mUIFlag = View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;

            getWindow().getDecorView().setSystemUiVisibility(mUIFlag);
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position)
    {
        switch (position) {
            case 0:
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, new MyElectricMainFragment(), getResources().getString(R.string.tag_me_fragment))
                        .commit();
                break;
            case 1:
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, new SettingsFragment(), getResources().getString(R.string.tag_settings_fragment))
                        .commit();
                break;
        }
    }

    @Override
    public void onBackPressed() {

        if (getFragmentManager().findFragmentByTag(getResources().getString(R.string.tag_me_fragment)) == null)
        {
            NavigationDrawerFragment navFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentByTag(getResources().getString(R.string.tag_navigation_fragment));
            navFragment.setSelectedItem(0);

            getFragmentManager().beginTransaction()
                    .replace(R.id.container, new MyElectricMainFragment(), getResources().getString(R.string.tag_me_fragment))
                    .commit();
        }
        else
            super.onBackPressed();
    }
}