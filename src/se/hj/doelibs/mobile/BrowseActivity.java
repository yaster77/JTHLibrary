package se.hj.doelibs.mobile;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import org.apache.http.HttpException;
import se.hj.doelibs.api.TitleDao;
import se.hj.doelibs.mobile.asynctask.TaskCallback;
import se.hj.doelibs.mobile.codes.ExtraKeys;
import se.hj.doelibs.mobile.listadapter.SearchResultListAdapter;
import se.hj.doelibs.mobile.listener.OnTitleItemSelectedListener;
import se.hj.doelibs.model.Title;

import java.util.List;

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
		String searchTerm = intent.getStringExtra("Term");
		String topics = intent.getStringExtra("Topics");

		_list = (ListView)findViewById(R.id.searchResultList);
		_list.setBackgroundColor(0);

		_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Title clicked = (Title)_list.getItemAtPosition(position);
				onTitleItemSelected(clicked.getTitleId());
			}
		});

		//do the search
		new SearchTitleAsyncTask(searchTerm, topics, new TaskCallback<List<Title>>() {
			@Override
			public void onTaskCompleted(List<Title> titles) {
				if(titles.isEmpty()){
					Toast emptyToast = Toast.makeText(BrowseActivity.this, R.string.search_result_none, Toast.LENGTH_LONG);
					emptyToast.show();
				}
				_list.setAdapter(new SearchResultListAdapter(BrowseActivity.this, android.R.layout.simple_list_item_1, titles));
			}
		}).execute();
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


	/**
	 * class which does the search for a title in background
	 */
	private class SearchTitleAsyncTask extends AsyncTask<Void, Void, List<Title>> {

		private TitleDao titleDao;
		private String searchTerm;
		private String topics;
		private TaskCallback<List<Title>> callback;

		public SearchTitleAsyncTask(String searchTerm, String topics, TaskCallback<List<Title>> callback) {
			this.titleDao = new TitleDao(getCredentials());
			this.searchTerm = searchTerm;
			this.topics = topics;
			this.callback = callback;
		}

		@Override
		protected List<Title> doInBackground(Void... params) {
			List<Title> response = null;
			try {
				response = this.titleDao.searchTitle(this.searchTerm, this.topics);
			} catch (HttpException e) {
				e.printStackTrace();
			}

			return response;
		}

		@Override
		protected void onPostExecute(List<Title> titles) {
			callback.onTaskCompleted(titles);
		}
	}
}