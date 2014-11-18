package se.hj.doelibs.mobile;

import android.app.Activity;
import android.content.SharedPreferences;
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
import org.apache.http.auth.UsernamePasswordCredentials;
import se.hj.doelibs.mobile.codes.PreferencesKeys;
import se.hj.doelibs.mobile.listadapter.NavigationListAdapter;
import se.hj.doelibs.mobile.listener.NavigationItemOnClickListener;

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

	/**
	 * returns the credentials of the current user.
	 *
	 * returns null if the user is not logged in
	 *
	 * @return
	 */
	protected UsernamePasswordCredentials getCredentials() {
		UsernamePasswordCredentials credentials = null;

		SharedPreferences pref = getSharedPreferences(PreferencesKeys.NAME_MAIN_SETTINGS, MODE_PRIVATE);

		if(pref.contains(PreferencesKeys.KEY_USER_USERNAME) && pref.contains(PreferencesKeys.KEY_USER_PASSWORD)) {
			credentials = new UsernamePasswordCredentials(
					pref.getString(PreferencesKeys.KEY_USER_USERNAME, ""),
					pref.getString(PreferencesKeys.KEY_USER_PASSWORD, ""));
		}

		return credentials;
	}

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

		//show username if loggedin
		UsernamePasswordCredentials credentials = getCredentials();
		if(credentials != null) {
			userNameTextView.setText(credentials.getUserPrincipal().getName());
		} else {
			userNameTextView.setText("Anonymous");
		}
		
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
