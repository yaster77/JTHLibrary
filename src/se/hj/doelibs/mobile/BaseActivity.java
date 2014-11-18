package se.hj.doelibs.mobile;

import se.hj.doelibs.mobile.listadapter.NavigationListAdapter;
import se.hj.doelibs.mobile.listener.NavigationItemOnClickListener;
import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Base activity to provide basic attributes (currently logged in user, ...) and
 * the navigation drawer.
 * 
 * @author Christoph
 *
 */
public abstract class BaseActivity extends Activity {

	protected DrawerLayout drawerLayout;
	protected ListView drawerListView;
	@SuppressWarnings("deprecation")
	protected ActionBarDrawerToggle actionBarDrawerToggle;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base);

		// get ListView for the navigation
		drawerListView = (ListView) findViewById(R.id.left_drawer);

		//set the header of the list view
		LayoutInflater inflater = getLayoutInflater();
		ViewGroup drawerHeader = (ViewGroup) inflater.inflate(R.layout.navigation_header, drawerListView, false);
		drawerListView.addHeaderView(drawerHeader, null, false);
		
		TextView userNameTextView = (TextView)findViewById(R.id.username);
		userNameTextView.setText("Max Mustermann");
		
		// Set the adapter for the list view
		ListAdapter listAdapter = new NavigationListAdapter(this);
		drawerListView.setAdapter(listAdapter);

		// get the layout of the navigation
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		
		// add functionality to show the navigation with click on the nav icon
		actionBarDrawerToggle = new ActionBarDrawerToggle(this,
			drawerLayout,
			R.drawable.ic_navbar, /* nav drawer icon */
			R.string.drawer_open, /* "open drawer" description */
			R.string.drawer_close /* "close drawer" description */
		);

		// Set actionBarDrawerToggle as the DrawerListener
		drawerLayout.setDrawerListener(actionBarDrawerToggle);

		getActionBar().setDisplayHomeAsUpEnabled(true);

		//add listener for list view
		drawerListView.setOnItemClickListener(new NavigationItemOnClickListener(this, drawerLayout, drawerListView));
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		actionBarDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred. 
		actionBarDrawerToggle.syncState();
	}
}
