package se.hj.doelibs.mobile;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import org.apache.http.HttpException;
import se.hj.doelibs.api.TitleDao;
import se.hj.doelibs.mobile.codes.ExtraKeys;
import se.hj.doelibs.model.Title;

import java.util.ArrayList;
import java.util.List;


public class SearchResultActivity extends BaseActivity {

    private ListView _list;
    private ArrayList<SearchResultItem> _data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View contentView = inflater.inflate(R.layout.activity_searchresult, null, false);
        drawerLayout.addView(contentView, 0);

        Intent intent = getIntent();
        final String searchTerm = intent.getStringExtra("Term");
        final String topics = intent.getStringExtra("Topics");

        final TitleDao titleDao = new TitleDao(getCredentials());
        _list = (ListView)findViewById(R.id.searchResultList);
        _data = new ArrayList<SearchResultItem>();


        _list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SearchResultItem clicked = (SearchResultItem)_list.getItemAtPosition(position);
                Intent titleDetailsActivity = new Intent(SearchResultActivity.this, TitleDetailsActivity.class);
                titleDetailsActivity.putExtra(ExtraKeys.TITLE_ID, clicked.titleId);
                startActivity(titleDetailsActivity);
            }
        });



        new AsyncTask<Void, Void, List<Title>>() {

            @Override
            protected List<Title> doInBackground(Void... params) {
                List<Title> response = null;
                try {
                    response = titleDao.searchTitle(searchTerm, topics);
                } catch (HttpException e) {
                    e.printStackTrace();
                }

                return response;
            }

            @Override
            protected void onPostExecute(List<Title> titles) {
                for (Title title : titles)
                {
                    SearchResultItem item = new SearchResultItem();
                    item.title = title.getBookTitle();
                    item.titleId = title.getTitleId();
                    _data.add(item);
                }

                if(_data.isEmpty()){
                    Toast emptyToast = Toast.makeText(SearchResultActivity.this, R.string.search_result_none, Toast.LENGTH_LONG);
                    emptyToast.show();
                }
                _list.setAdapter(new ArrayAdapter<SearchResultItem>(SearchResultActivity.this, android.R.layout.simple_list_item_1, _data));
            }
        }.execute();


    }




}
