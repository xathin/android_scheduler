package cx.ath.xathin.scheduler;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.SimpleCursorAdapter;
import cx.ath.xathin.scheduler.contentprovider.OfficialContentProvider;
import cx.ath.xathin.scheduler.database.OfficialTable;

public class OfficialListviewActivity extends ListActivity implements
		LoaderManager.LoaderCallbacks<Cursor> {

	private SimpleCursorAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.official_activity);
		// getSupportLoaderManager().initLoader(LOADER_ID, null, this);
		this.getListView().setDividerHeight(2);
		fillData();
	}

	private void fillData() {
		// Fields from database
		// Must contain '_id' column for adapter to work
		String[] from = new String[] { OfficialTable.COLUMN_FIRST_NAME,
				OfficialTable.COLUMN_LAST_NAME, OfficialTable.COLUMN_LEVEL,
				OfficialTable.COLUMN_PHONE1 };
		// Fields on the UI which we map
		int[] to = new int[] { R.id.official_first_name_list,
				R.id.official_last_name_list, R.id.official_level_list,
				R.id.official_phone1_list };

		getLoaderManager().initLoader(0, null, this);
		adapter = new SimpleCursorAdapter(this, R.layout.official_activity,
				null, from, to, 0);

		setListAdapter(adapter);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		String[] projection = { OfficialTable.COLUMN_ID,
				OfficialTable.COLUMN_FIRST_NAME,
				OfficialTable.COLUMN_LAST_NAME, OfficialTable.COLUMN_LEVEL,
				OfficialTable.COLUMN_PHONE1 };
		CursorLoader cursorLoader = new CursorLoader(this,
				OfficialContentProvider.CONTENT_URI, projection, null, null,
				null);
		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		adapter.swapCursor(data);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		adapter.swapCursor(null);
	}

}
