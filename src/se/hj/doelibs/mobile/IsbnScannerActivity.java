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
import se.hj.doelibs.mobile.asynctask.TaskCallback;
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

		//when we read isbn numbers successfully, we save them in tmp preferences in case the display is rotated and so this activity reloaded
		if(isbnScannerTmpValues.contains(PreferencesKeys.KEY_ISBN_VERSION)
				&& isbnScannerTmpValues.contains(PreferencesKeys.KEY_ISBN_NUMBER)) {
			String isbn = isbnScannerTmpValues.getString(PreferencesKeys.KEY_ISBN_NUMBER, "");
			String format = isbnScannerTmpValues.getString(PreferencesKeys.KEY_ISBN_VERSION, "");
			handleScanResults(isbn, format);
		} else {
			//open scanner only if the user clicked on the cammera button - not in case the display was rotated
			if(getIntent().getBooleanExtra(ExtraKeys.ISBN_SCANNER_START_ZXING, false)) {
				openScanner();
			}
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

		new CheckIfIsbnExistsTask(isbn, format, new TaskCallback<Title>() {
			@Override
			public void onTaskCompleted(Title title) {
				dialog.hide();
				if (title == null) {
					showDialogNoTitleFound();
				} else {
					//redirect to action...
					Intent titleDetailsActivity = new Intent(IsbnScannerActivity.this, TitleDetailsActivity.class);
					titleDetailsActivity.putExtra(ExtraKeys.TITLE_ID, title.getTitleId());
					startActivity(titleDetailsActivity);
				}
			}

			@Override
			public void beforeTaskRun() {
				dialog.setMessage("checking if title exists in DoeLibS");
				dialog.setCancelable(false);
				dialog.show();
			}
		}).execute();
	}

	/**
	 * opens a dialog that the title is not known to the DoeLibS and asks what the user wants to do
	 */
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

	/**
	 * opens the zxing scanner app
	 */
	private void openScanner() {
		IntentIntegrator scanIntegrator = new IntentIntegrator(this);
		scanIntegrator.initiateScan();
	}



	private class CheckIfIsbnExistsTask extends AsyncTask<Void, Void, Title> {

		private String isbn;
		private TitleDao.IsbnFormat format;
		private TaskCallback<Title> callback;

		public CheckIfIsbnExistsTask(String isbn, String format, TaskCallback<Title> callback) {
			this.isbn = isbn;
			this.callback = callback;

			if(format.equals("isbn10")) {
				this.format = TitleDao.IsbnFormat.ISBN10;
			} else {
				this.format = TitleDao.IsbnFormat.ISBN13;
			}
		}

		@Override
		protected Title doInBackground(Void... params) {
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
			callback.onTaskCompleted(title);
		}

		@Override
		protected void onPreExecute() {
			callback.beforeTaskRun();
		}
	}
}
