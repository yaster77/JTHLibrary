package se.hj.doelibs.mobile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;
import org.apache.http.HttpException;
import se.hj.doelibs.api.TitleDao;
import se.hj.doelibs.api.TopicDao;
import se.hj.doelibs.mobile.asynctask.TaskCallback;
import se.hj.doelibs.mobile.codes.ExtraKeys;
import se.hj.doelibs.mobile.utils.ProgressDialogUtils;
import se.hj.doelibs.model.Title;
import se.hj.doelibs.model.Topic;

import java.util.ArrayList;
import java.util.List;


public class SearchActivity extends BaseActivity {

	private EditText et;
	private MultiAutoCompleteTextView mactv;
	private ProgressDialog searchDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

	    super.onCreate(savedInstanceState);
	    LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    
	    View contentView = inflater.inflate(R.layout.activity_search, null, false);
	    drawerLayout.addView(contentView, 0);

		mactv = (MultiAutoCompleteTextView)findViewById(R.id.topicMultiAutoComplete);
		final TopicDao topicDao = new TopicDao(getCredentials());
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line);
		et = (EditText)findViewById(R.id.searchEditText);

		new AsyncTask<Void, Void, List<Topic>>() {

			@Override
			protected List<Topic> doInBackground(Void... params) {
				return  topicDao.getAll();
			}

			@Override
			protected void onPostExecute(List<Topic> topics) {
				for (Topic topic : topics)
				{
					adapter.add(topic.getName());
				}
			}
		}.execute();


		mactv.setAdapter(adapter);
		mactv.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
	}


	public void searchClick(View v){
		String mactvText = mactv.getText().toString().trim();
		if(mactvText.length() > 0){
			mactvText = mactvText.replace(", ", ",");
			if(mactvText.charAt(mactvText.length()-1) == ','){
				mactvText = mactvText.substring(0, mactvText.length() - 1);
			}
		}
		String etText = et.getText().toString();

		if(etText.equals("") && mactvText.equals("")){
			Toast toast = Toast.makeText(this, R.string.search_field_required, Toast.LENGTH_LONG);
			toast.show();
		}
		else {
			//do search
			new SearchTitleAsyncTask(et.getText().toString(), mactvText, new TaskCallback<List<Title>>() {

				@Override
				public void beforeTaskRun() {
					searchDialog = new ProgressDialog(SearchActivity.this);
					searchDialog.setMessage(getResources().getText(R.string.searching));
					searchDialog.setCancelable(false);
					searchDialog.show();
				}

				@Override
				public void onTaskCompleted(List<Title> titles) {
					ProgressDialogUtils.dismissQuitely(searchDialog);

					if(titles.isEmpty()){
						Toast emptyToast = Toast.makeText(SearchActivity.this, R.string.search_result_none, Toast.LENGTH_LONG);
						emptyToast.show();
					} else {
						//start browse activity

						Intent searchResult = new Intent(SearchActivity.this, BrowseActivity.class);
						searchResult.putExtra(ExtraKeys.TITLE_SEARCH_RESULTS, new ArrayList<Title>(titles));
						startActivity(searchResult);
					}
				}
			}).execute();
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
		protected void onPreExecute() {
			callback.beforeTaskRun();
		}

		@Override
		protected void onPostExecute(List<Title> titles) {
			callback.onTaskCompleted(titles);
		}
	}
}
