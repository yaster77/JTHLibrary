package se.hj.doelibs.mobile;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import org.apache.http.HttpException;
import se.hj.doelibs.api.TitleDao;
import se.hj.doelibs.mobile.codes.ExtraKeys;
import se.hj.doelibs.mobile.codes.PreferencesKeys;
import se.hj.doelibs.model.Title;

public class IsbnScannerActivity extends BaseActivity {

	private SharedPreferences isbnScannerTmpValues;
	private SharedPreferences.Editor isbnScannerTmpValuesEditor;
	private TextView tv;

	@Override
	public void onCreate(Bundle state) {
		super.onCreate(state);

		//setup navigation
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View contentView = inflater.inflate(R.layout.activity_isbn_scanner, null, false);
		drawerLayout.addView(contentView, 0);

		//set fields
		tv = (TextView)findViewById(R.id.isbn_scanner_tv);

		//setup SharedPreferences
		isbnScannerTmpValues = getSharedPreferences(PreferencesKeys.NAME_TMP_VALUES, MODE_PRIVATE);
		isbnScannerTmpValuesEditor = isbnScannerTmpValues.edit();

		//open scanner only if the user clicked on the cammera button - not in case the display was rotated
		if(getIntent().getBooleanExtra(ExtraKeys.ISBN_SCANNER_START_ZXING, false)) {
			openScanner();
		}

		//when we read isbn numbers successfully, we save them in tmp preferences in case the display is rotated and so this activity reloaded
		if(isbnScannerTmpValues.contains(PreferencesKeys.KEY_ISBN_VERSION)
				&& isbnScannerTmpValues.contains(PreferencesKeys.KEY_ISBN_NUMBER)) {
			String isbn = isbnScannerTmpValues.getString(PreferencesKeys.KEY_ISBN_NUMBER, "");
			String format = isbnScannerTmpValues.getString(PreferencesKeys.KEY_ISBN_VERSION, "");
			handleScanResults(isbn, format);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
		if (scanningResult != null && scanningResult.getContents() != null && scanningResult.getFormatName() != null) {
			String isbn = scanningResult.getContents();
			String format = scanningResult.getFormatName();

			//save received values in tmp
			isbnScannerTmpValuesEditor.putString(PreferencesKeys.KEY_ISBN_NUMBER, isbn);
			isbnScannerTmpValuesEditor.putString(PreferencesKeys.KEY_ISBN_VERSION, format);
			isbnScannerTmpValuesEditor.commit();

			handleScanResults(isbn, format);
		} else {
			Toast.makeText(getApplicationContext(), "No scan data received!", Toast.LENGTH_SHORT).show();

			tv.setText("try again");
		}
	}

	/**
	 * handles the result of the scanner
	 *
	 * @param isbn
	 * @param format
	 */
	private void handleScanResults(String isbn, String format) {
		if(format.equals("EAN_13") || format.equals("EAN_8")) {
			Toast.makeText(getApplicationContext(), isbn + "(" + format + ")", Toast.LENGTH_LONG).show();

			//check isbn in another thread
			checkIsbn(isbn, format);
		} else {
			Toast.makeText(getApplicationContext(), "only ISBN10 and ISBN13 supported", Toast.LENGTH_SHORT).show();
		}

		//remove values from shared preferences again because they are processed
		isbnScannerTmpValuesEditor.remove(PreferencesKeys.KEY_ISBN_NUMBER);
		isbnScannerTmpValuesEditor.remove(PreferencesKeys.KEY_ISBN_VERSION);
		isbnScannerTmpValuesEditor.commit();
	}

	/**
	 * checks if the given isbn exists in doelibs. if so it opens the title details page otherwise it shows a message
	 *
	 * @param isbn
	 * @param format
	 */
	private void checkIsbn(String isbn, String format) {
		final ProgressDialog dialog = new ProgressDialog(IsbnScannerActivity.this);

		new AsyncTask<String, Void, Title>() {
			@Override
			protected Title doInBackground(String... params) {
				String isbn = params[0];
				//tv.setText(isbn);

				TitleDao.IsbnFormat format;
				if(params[1].equals("isbn10")) {
					format = TitleDao.IsbnFormat.ISBN10;
				} else {
					format = TitleDao.IsbnFormat.ISBN13;
				}

				TitleDao titleDao = new TitleDao(getCredentials());
				Title title = null;
				try {
					title = titleDao.getByIsbn(isbn, format);
				} catch (HttpException e) {
					Log.d("IsbnScannerActivity", "could not load titleinformation by ISBN", e);
				}

				return title;
			}

			@Override
			protected void onPostExecute(Title title) {
				dialog.dismiss();

				if (title == null) {
					showDialogNoTitleFound();
				} else {
					//redirect to action...
					tv.setText("redirect to titledetails view...");
				}
			}

			@Override
			protected void onPreExecute() {
				dialog.setMessage("checking if title exists in DoeLibS");
				dialog.setCancelable(false);
				dialog.show();
			}
		}.execute(new String[]{isbn, format});
	}

	private void showDialogNoTitleFound() {
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.dialog_no_isbn_on_scanning_found)
				.setPositiveButton(R.string.btn_isbn_scanner_more_scanning, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						openScanner();
					}
				})
				.setNegativeButton(R.string.btn_isbn_scanner_no_more_scanning, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// User cancelled the dialog --> load search page
						Intent searchActivity = new Intent(IsbnScannerActivity.this, SearchActivity.class);
						startActivity(searchActivity);
					}
				});

		builder.create().show();
	}

	private void openScanner() {
		IntentIntegrator scanIntegrator = new IntentIntegrator(this);
		scanIntegrator.initiateScan();
	}
}
