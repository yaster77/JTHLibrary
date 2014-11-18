package se.hj.doelibs.mobile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class IsbnScannerActivity extends BaseActivity {//implements me.dm7.barcodescanner.zbar.ZBarScannerView.ResultHandler{

	@Override
	public void onCreate(Bundle state) {
		super.onCreate(state);

		IntentIntegrator scanIntegrator = new IntentIntegrator(this);
		scanIntegrator.initiateScan();
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
		if (scanningResult != null) {
			String isbn = scanningResult.getContents();
			String format = scanningResult.getFormatName();
			
			Toast.makeText(getApplicationContext(), isbn + "(" + format + ")", Toast.LENGTH_LONG).show();
			
			//check for title and load new intent...
		} else {
			Toast.makeText(getApplicationContext(), "No scan data received!", Toast.LENGTH_SHORT).show();
		}
	}
}
