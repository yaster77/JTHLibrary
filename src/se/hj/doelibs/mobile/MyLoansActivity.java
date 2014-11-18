package se.hj.doelibs.mobile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

public class MyLoansActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    
	    View contentView = inflater.inflate(R.layout.activity_my_loans, null, false);
	    drawerLayout.addView(contentView, 0);

		//check if user is logged in
		if(getCredentials() == null) {
			Log.d("MyLoans", "user is not logged in");
			Intent loginActivity = new Intent(this, LoginActivity.class);
			startActivity(loginActivity);
		}
	}
}