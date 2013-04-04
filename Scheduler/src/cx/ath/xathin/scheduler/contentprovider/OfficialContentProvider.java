package cx.ath.xathin.scheduler.contentprovider;

import java.util.Arrays;
import java.util.HashSet;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import cx.ath.xathin.scheduler.database.OfficialTable;
import cx.ath.xathin.scheduler.database.SchedulerDbHelper;

public class OfficialContentProvider extends ContentProvider {

	// database
	private SchedulerDbHelper database;

	// Used for the UriMatcher
	private static final int OFFICIALS = 110;
	private static final int OFFICIAL_ID = 120;

	private static final String AUTHORITY = "cx.ath.xathin.scheduler.contentprovider";

	private static final String BASE_PATH = "officials";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
			+ "/" + BASE_PATH);

	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
			+ "/officials";
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
			+ "/official";

	private static final UriMatcher sURIMatcher = new UriMatcher(
			UriMatcher.NO_MATCH);
	static {
		sURIMatcher.addURI(AUTHORITY, BASE_PATH, OFFICIALS);
		sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", OFFICIAL_ID);
	}

	@Override
	public boolean onCreate() {
		database = new SchedulerDbHelper(getContext());
		return false;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = database.getWritableDatabase();
		int rowsDeleted = 0;
		switch (uriType) {
		case OFFICIALS:
			rowsDeleted = sqlDB.delete(OfficialTable.TABLE_OFFICIAL, selection,
					selectionArgs);
			break;
		case OFFICIAL_ID:
			String id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsDeleted = sqlDB.delete(OfficialTable.TABLE_OFFICIAL,
						OfficialTable.COLUMN_ID + "=" + id, null);
			} else {
				rowsDeleted = sqlDB.delete(OfficialTable.TABLE_OFFICIAL,
						OfficialTable.COLUMN_ID + "=" + id + " and "
								+ selection, selectionArgs);
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsDeleted;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = database.getWritableDatabase();
		long id = 0;
		switch (uriType) {
		case OFFICIALS:
			id = sqlDB.insert(OfficialTable.TABLE_OFFICIAL, null, values);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return Uri.parse(BASE_PATH + "/" + id);
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		// Using SQLiteQueryBuilder instead of query() method
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

		// Check if the caller has requested a column which does not exists
		checkColumns(projection);

		// Set the table
		queryBuilder.setTables(OfficialTable.TABLE_OFFICIAL);

		int uriType = sURIMatcher.match(uri);
		switch (uriType) {
		case OFFICIALS:
			break;
		case OFFICIAL_ID:
			// Adding the ID to the original query
			queryBuilder.appendWhere(OfficialTable.COLUMN_ID + "="
					+ uri.getLastPathSegment());
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}

		SQLiteDatabase db = database.getWritableDatabase();
		Cursor cursor = queryBuilder.query(db, projection, selection,
				selectionArgs, null, null, sortOrder);
		// Make sure that potential listeners are getting notified
		cursor.setNotificationUri(getContext().getContentResolver(), uri);

		return cursor;
	}

	private void checkColumns(String[] projection) {
		String[] available = { OfficialTable.COLUMN_FIRST_NAME,
				OfficialTable.COLUMN_LAST_NAME, OfficialTable.COLUMN_PHONE1,
				OfficialTable.COLUMN_PHONE2, OfficialTable.COLUMN_LEVEL,
				OfficialTable.COLUMN_MIN_PAY, OfficialTable.COLUMN_ID };
		if (projection != null) {
			HashSet<String> requestedColumns = new HashSet<String>(
					Arrays.asList(projection));
			HashSet<String> availableColumns = new HashSet<String>(
					Arrays.asList(available));
			// Check if all columns which are requested are available
			if (!availableColumns.containsAll(requestedColumns)) {
				throw new IllegalArgumentException(
						"Unknown columns in projection");
			}
		}
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = database.getWritableDatabase();
		int rowsUpdated = 0;
		switch (uriType) {
		case OFFICIALS:
			rowsUpdated = sqlDB.update(OfficialTable.TABLE_OFFICIAL, values,
					selection, selectionArgs);
			break;
		case OFFICIAL_ID:
			String id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsUpdated = sqlDB.update(OfficialTable.TABLE_OFFICIAL,
						values, OfficialTable.COLUMN_ID + "=" + id, null);
			} else {
				rowsUpdated = sqlDB.update(OfficialTable.TABLE_OFFICIAL,
						values, OfficialTable.COLUMN_ID + "=" + id + " and "
								+ selection, selectionArgs);
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsUpdated;
	}

}
