package se.hj.doelibs.mobile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import se.hj.doelibs.mobile.codes.ExtraKeys;
import se.hj.doelibs.mobile.listadapter.SearchResultListAdapter;
import se.hj.doelibs.mobile.listener.OnTitleItemSelectedListener;
import se.hj.doelibs.model.Title;

import java.util.ArrayList;

/**
 * activity which shows on large screens the "splitscreen" feature to borowse through the search results.
 * on small devices it will show the searchresults and open the title details in a new activity.
 *
 * @author Elias
 * @author Christoph
 */
public class BrowseActivity extends BaseActivity implements OnTitleItemSelectedListener {

	private ListView _list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View contentView = inflater.inflate(R.layout.activity_browse, null, false);
		drawerLayout.addView(contentView, 0);

		Intent intent = getIntent();
		ArrayList<Title> titles = (ArrayList<Title>) intent.getSerializableExtra(ExtraKeys.TITLE_SEARCH_RESULTS);

		_list = (ListView)findViewById(R.id.searchResultList);
		_list.setBackgroundColor(0);

		_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Title clicked = (Title)_list.getItemAtPosition(position);
				onTitleItemSelected(clicked.getTitleId());
			}
		});
		_list.setAdapter(new SearchResultListAdapter(BrowseActivity.this, android.R.layout.simple_list_item_1, titles));
	}

	@Override
	public void onTitleItemSelected(int titleId) {
		TitleDetailsFragment fragment = (TitleDetailsFragment) getFragmentManager().findFragmentById(R.id.detailFragment);

		if (fragment != null && fragment.isInLayout()) {
			fragment.setTitleId(titleId);
			fragment.setupView();
		} else {
			//fragment was not found in current layout --> it is not on a large device --> start activity with title details
			Intent titleDetailsActivity = new Intent(this, TitleDetailsActivity.class);
			titleDetailsActivity.putExtra(ExtraKeys.TITLE_ID, titleId);
			startActivity(titleDetailsActivity);
		}
	}
}