package se.hj.doelibs.mobile;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

/**
 * activity which is shown if the no internet connection could be established
 *
 * @author Christoph
 */
public class NoConnectionActivity extends BaseActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View contentView = inflater.inflate(R.layout.activity_no_connection, null, false);
		drawerLayout.addView(contentView, 0);
	}
}