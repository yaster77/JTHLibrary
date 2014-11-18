package se.hj.doelibs.mobile;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

public class SearchActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

	    super.onCreate(savedInstanceState);
	    LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    
	    View contentView = inflater.inflate(R.layout.activity_search, null, false);
	    drawerLayout.addView(contentView, 0); 
	}
}
