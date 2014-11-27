package se.hj.doelibs.mobile;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import se.hj.doelibs.api.TopicDao;
import se.hj.doelibs.model.Topic;
import java.util.List;


public class SearchActivity extends BaseActivity {

	private EditText et;
	private MultiAutoCompleteTextView mactv;

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
			Intent searchResult = new Intent(SearchActivity.this, BrowseActivity.class);
			searchResult.putExtra("Term", et.getText().toString());
			searchResult.putExtra("Topics", mactvText);
			startActivity(searchResult);
		}
	}
}
