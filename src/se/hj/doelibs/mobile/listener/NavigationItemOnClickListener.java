/**
 * 
 */
package se.hj.doelibs.mobile.listener;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import se.hj.doelibs.mobile.MyLoansActivity;
import se.hj.doelibs.mobile.SearchActivity;
import se.hj.doelibs.mobile.SettingsActivity;


/**
 * @author Christoph
 *
 */
public class NavigationItemOnClickListener implements ListView.OnItemClickListener {

	private Context context;
	private DrawerLayout drawerLayout;
	private ListView listView;
	
	public NavigationItemOnClickListener(Context pContext, DrawerLayout pDrawerLayout, ListView pListView) {
		context = pContext;
		drawerLayout = pDrawerLayout;
		listView = pListView;
	}
	
	public void onItemClick(AdapterView parent, View view, int position, long id) {
		Intent intent = null;
		
		if(position == 1) {
			//my loans:
			intent = new Intent(context, MyLoansActivity.class);
		} else if(position == 2){
			intent = new Intent(context, SearchActivity.class);
		} else {
			intent = new Intent(context, SettingsActivity.class);
		}
		drawerLayout.closeDrawer(listView);
		context.startActivity(intent);
	}
}
