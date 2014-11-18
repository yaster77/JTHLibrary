package se.hj.doelibs.mobile;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import org.apache.http.HttpException;
import se.hj.doelibs.api.TitleDao;
import se.hj.doelibs.model.Title;

public class MyLoansActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    
	    View contentView = inflater.inflate(R.layout.activity_my_loans, null, false);
	    drawerLayout.addView(contentView, 0);

		new AsyncTask<Void, Void, Title>() {
			@Override
			protected Title doInBackground(Void... voids) {
				TitleDao titleDao = new TitleDao();
				Title res = null;

				try {
					res = titleDao.getById(1);
				} catch (HttpException e) {
					Log.e("adsf", "dfsa", e);
				}

				return res;
			}

			@Override
			protected void onPostExecute(Title result) {
				TextView tv = (TextView)findViewById(R.id.myLoanText);

				tv.setText("done");
			}
		}.execute();
	}
}