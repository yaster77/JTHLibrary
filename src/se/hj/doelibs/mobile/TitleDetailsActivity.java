package se.hj.doelibs.mobile;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import org.apache.http.HttpException;
import se.hj.doelibs.api.TitleDao;
import se.hj.doelibs.mobile.codes.ExtraKeys;
import se.hj.doelibs.mobile.utils.ListUtils;
import se.hj.doelibs.model.Loanable;
import se.hj.doelibs.model.Title;

import java.util.List;

/**
 * @author Christoph
 */
public class TitleDetailsActivity extends BaseActivity {
	private Title title;

	private TextView tv_title;
	private TextView tv_edition;

	private TextView tv_categories;
	private TextView tv_publisher;
	private TextView tv_authors;

	private List<Loanable> loanables;
	private ListView lv_loanables;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_title_details);

		//setup navigation
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View contentView = inflater.inflate(R.layout.activity_isbn_scanner, null, false);
		drawerLayout.addView(contentView, 0);

		tv_title = (TextView)findViewById(R.id.tv_titledetails_titlename);
		tv_edition = (TextView)findViewById(R.id.tv_titledetails_edition);
		tv_categories = (TextView)findViewById(R.id.tv_titledetails_categories);
		tv_publisher = (TextView)findViewById(R.id.tv_titledetails_publisher);
		tv_authors = (TextView)findViewById(R.id.tv_titledetails_authors);

		Intent intent = getIntent();
		title = (Title) intent.getSerializableExtra(ExtraKeys.TITLE_OBJECT);

		setupView();
	}

	/**
	 * loads all data into the viewfields. If any data of the title object is missing (default author, loanables, etc) it loads them
	 */
	private void setupView(){
		setupNativeData();
		setupDetailData();
	}

	/**
	 * puts all the data from the given title object into the view
	 */
	private void setupNativeData() {
		tv_title.setText(title.getBookTitle());
		tv_edition.setText("Edition " + title.getEditionNumber());
		tv_publisher.setText(title.getPublisher().getName() + "(" + title.getEditionYear() + ")");
	}

	/**
	 * loads more detailed information about the title (author, loanables, etc) and puts it into the view
	 */
	private void setupDetailData() {
		new AsyncTask<Integer, Void, Title>() {
			@Override
			protected Title doInBackground(Integer... titleIds) {
				TitleDao titleDao = new TitleDao(getCredentials());
				Title extendedTitle = null;

				try {
					extendedTitle = titleDao.getById(titleIds[0]);
				} catch (HttpException e) {
					Log.e("TitleDetails", "could not load more details from title", e);
				}

				return extendedTitle;
			}

			@Override
			protected void onPostExecute(Title extendedTitle) {
				//now put all the extended titleinformation into the view

				tv_categories.setText(ListUtils.implode(extendedTitle.getTopics(), " ,"));

				//add authors and editors in one textview:
				String authors = ListUtils.implode(extendedTitle.getAuthors(), ", ");
				String editors = ListUtils.implode(extendedTitle.getEditors(), " (Editor), ");
				if(extendedTitle.getEditors().size() > 0) {
					editors += " (Editor)";
				}
				String fullAuthorList = authors + ((authors.length()>0 && editors.length() > 0)?", ":"") + editors;
				tv_authors.setText(fullAuthorList);

				loanables = extendedTitle.getLoanables();

				//actionBarProgressBar.setVisibility(View.GONE);
				setProgressBarIndeterminateVisibility(false);
			}

			@Override
			protected void onPreExecute() {
				//actionBarProgressBar.setVisibility(View.VISIBLE);
				setProgressBarIndeterminateVisibility(true);
			}
		}.execute(title.getTitleId());
	}
}