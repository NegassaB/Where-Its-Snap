package com.negassagisila.whereitssnap;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {
    private ListView mNavDrawerList;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;

    private void switchFragment(int position) {

        Fragment fragment = null;
        String fragmentID = "";
        switch (position) {
            case 0:
                fragmentID = "TITLES";
                Bundle args = new Bundle();
                args.putString("Tag", "_NO_TAG");
                fragment = new TitlesFragment();
                fragment.setArguments(args);
                break;

            case 1:
                fragmentID = "TAGS";
                fragment = new TagsFragment();
                break;

            case 2:
                fragmentID = "CAPTURE";
                fragment = new CaptureFragment();
                break;

            default:
                break;
        }

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragmentHolder, fragment, fragmentID).commit();

        //close the drawer
        mDrawerLayout.closeDrawer(mNavDrawerList);
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open,
                R.string.drawer_close) {

            //called when the drawer is opened
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                assert getSupportActionBar() != null;
                getSupportActionBar().setTitle("Make a Selection");

                //triggers call to onPrepareOptionsMenu()
                invalidateOptionsMenu();
            }

            //called when the drawer is closed
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

                getSupportActionBar().setTitle(mActivityTitle);

                //triggers call to onPrepareOptionsMenu()
                invalidateOptionsMenu();
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        //mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {

        //close if drawer is open
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {

            //drawer is open so close it
            mDrawerLayout.closeDrawer(mNavDrawerList);
        } else {
            //go back to TitlesFragment
            //quit if already at TitlesFragment
            Fragment f = getFragmentManager().findFragmentById(R.id.fragmentHolder);
            if (f instanceof TitlesFragment) {
                finish();
                System.exit(0);
            } else {
                switchFragment(0);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection simplifiableIfStatement
        if (id == R.string.action_settings) {
            return true;
        }

        //activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavDrawerList = findViewById(R.id.navList);
        mDrawerLayout = findViewById(R.id.drawerLayout);
        mActivityTitle = getTitle().toString();

        //initialize an array with our titles from strings.xml
        String[] navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        //initialize our ArrayAdapter
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, navMenuTitles);
        //set the adapter to the ListView
        mNavDrawerList.setAdapter(mAdapter);

        setupDrawer();

        assert getSupportActionBar() != null;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mNavDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switchFragment(position);
            }
        });

        switchFragment(0);
    }
}
