package cx.ath.xathin.scheduler;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cx.ath.xathin.scheduler.contentprovider.OfficialContentProvider;
import cx.ath.xathin.scheduler.database.OfficialTable;

public class OfficialDetailActivity extends Activity {

	private EditText mFirstName;
	private EditText mLastName;
	private EditText mPhone1;
	private EditText mPhone2;
	private EditText mLevel;
	private EditText mMinPay;

	private Uri officialUri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.official_activity);
		mFirstName = (EditText) findViewById(R.id.official_edit_first_name);
		mLastName = (EditText) findViewById(R.id.official_edit_last_name);
		mPhone1 = (EditText) findViewById(R.id.official_edit_phone1);
		mPhone2 = (EditText) findViewById(R.id.official_edit_phone2);
		mLevel = (EditText) findViewById(R.id.official_edit_level);
		mMinPay = (EditText) findViewById(R.id.official_edit_min_pay);
		Button confirmButton = (Button) findViewById(R.id.official_confirm_button);

		Bundle extras = getIntent().getExtras();

		// Check from the saved instance
		officialUri = (savedInstanceState == null) ? null
				: (Uri) savedInstanceState
						.getParcelable(OfficialContentProvider.CONTENT_ITEM_TYPE);

		// Or passed from the other activity
		if (extras != null) {
			officialUri = extras
					.getParcelable(OfficialContentProvider.CONTENT_ITEM_TYPE);

			fillData(officialUri);
		}

		confirmButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (TextUtils.isEmpty(mFirstName.getText().toString())
						|| TextUtils.isEmpty(mLastName.getText().toString())
						|| TextUtils.isEmpty(mPhone1.getText().toString())) {
					makeToast();
				} else {
					setResult(RESULT_OK);
					finish();
				}
			}
		});
	}

	private void fillData(Uri uri) {
		String[] projection = { OfficialTable.COLUMN_FIRST_NAME,
				OfficialTable.COLUMN_LAST_NAME, OfficialTable.COLUMN_LEVEL,
				OfficialTable.COLUMN_PHONE1 };
		Cursor cursor = getContentResolver().query(uri, projection, null, null,
				null);
		if (cursor != null) {
			cursor.moveToFirst();
			mFirstName.setText(cursor.getString(cursor
					.getColumnIndexOrThrow(OfficialTable.COLUMN_FIRST_NAME)));
			mLastName.setText(cursor.getString(cursor
					.getColumnIndexOrThrow(OfficialTable.COLUMN_LAST_NAME)));
			mPhone1.setText(cursor.getString(cursor
					.getColumnIndexOrThrow(OfficialTable.COLUMN_PHONE1)));
			mPhone2.setText(cursor.getString(cursor
					.getColumnIndexOrThrow(OfficialTable.COLUMN_PHONE2)));
			mLevel.setText(cursor.getInt(cursor
					.getColumnIndexOrThrow(OfficialTable.COLUMN_LEVEL)));
			mMinPay.setText(Double.toString(cursor.getDouble(cursor
					.getColumnIndexOrThrow(OfficialTable.COLUMN_MIN_PAY))));

			cursor.close();
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		saveState();
		outState.putParcelable(OfficialContentProvider.CONTENT_ITEM_TYPE,
				officialUri);
	}

	@Override
	protected void onPause() {
		super.onPause();
		saveState();
	}

	private void saveState() {
		String firstName = mFirstName.getText().toString();
		String lastName = mLastName.getText().toString();
		String phone1 = mPhone1.getText().toString();
		String phone2 = mPhone2.getText().toString();
		int level = Integer.parseInt(mLevel.getText().toString());
		double minPay = Double.parseDouble(mMinPay.getText().toString());

		if (firstName.length() == 0 && lastName.length() == 0
				&& phone1.length() == 0) {
			return;
		}

		ContentValues values = new ContentValues();
		values.put(OfficialTable.COLUMN_FIRST_NAME, firstName);
		values.put(OfficialTable.COLUMN_LAST_NAME, lastName);
		values.put(OfficialTable.COLUMN_PHONE1, phone1);
		values.put(OfficialTable.COLUMN_PHONE2, phone2);
		values.put(OfficialTable.COLUMN_LEVEL, level);
		values.put(OfficialTable.COLUMN_MIN_PAY, minPay);

		if (officialUri == null) {
			// new official
			officialUri = getContentResolver().insert(
					OfficialContentProvider.CONTENT_URI, values);
		} else {
			// update official
			getContentResolver().update(officialUri, values, null, null);
		}
	}

	private void makeToast() {
		Toast.makeText(OfficialDetailActivity.this,
				"Please enter the required information.", Toast.LENGTH_LONG)
				.show();
	}
}
