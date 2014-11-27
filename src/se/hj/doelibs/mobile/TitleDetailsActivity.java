package se.hj.doelibs.mobile;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import se.hj.doelibs.mobile.codes.ExtraKeys;

/**
 * Activity to show title details.
 * This activity will only be called on small devices or from the ISBN scanner. Otherwise it has the "splitscreen"
 *
 * @author Christoph
 */
public class TitleDetailsActivity extends BaseActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//setup navigation
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View contentView = inflater.inflate(R.layout.activity_title_details, null, false);
		drawerLayout.addView(contentView, 0);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			int titleId = extras.getInt(ExtraKeys.TITLE_ID, -1);

			if(titleId != -1) {
				TitleDetailsFragment detailFragment = (TitleDetailsFragment) getFragmentManager().findFragmentById(R.id.detailFragment);
				detailFragment.setTitleId(titleId);
				detailFragment.setupView();
			}
		}
	}
}