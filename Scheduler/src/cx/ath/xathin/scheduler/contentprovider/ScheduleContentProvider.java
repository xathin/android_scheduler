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
import cx.ath.xathin.scheduler.database.ScheduleTable;
import cx.ath.xathin.scheduler.database.SchedulerDbHelper;

public class ScheduleContentProvider extends ContentProvider {

	// database
	private SchedulerDbHelper database;

	// Used for the UriMatcher
	private static final int SCHEDULES = 310;
	private static final int SCHEDULE_ID = 320;

	private static final String AUTHORITY = "cx.ath.xathin.scheduler.contentprovider";

	private static final String BASE_PATH = "schedules";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
			+ "/" + BASE_PATH);

	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
			+ "/schedules";
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
			+ "/schedule";

	private static final UriMatcher sURIMatcher = new UriMatcher(
			UriMatcher.NO_MATCH);
	static {
		sURIMatcher.addURI(AUTHORITY, BASE_PATH, SCHEDULES);
		sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", SCHEDULE_ID);
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
		case SCHEDULES:
			rowsDeleted = sqlDB.delete(ScheduleTable.TABLE_SCHEDULE, selection,
					selectionArgs);
			break;
		case SCHEDULE_ID:
			String id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsDeleted = sqlDB.delete(ScheduleTable.TABLE_SCHEDULE,
						ScheduleTable.COLUMN_ID + "=" + id, null);
			} else {
				rowsDeleted = sqlDB.delete(ScheduleTable.TABLE_SCHEDULE,
						ScheduleTable.COLUMN_ID + "=" + id + " and "
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
		case SCHEDULES:
			id = sqlDB.insert(ScheduleTable.TABLE_SCHEDULE, null, values);
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
		queryBuilder.setTables(ScheduleTable.TABLE_SCHEDULE);

		int uriType = sURIMatcher.match(uri);
		switch (uriType) {
		case SCHEDULES:
			break;
		case SCHEDULE_ID:
			// Adding the ID to the original query
			queryBuilder.appendWhere(ScheduleTable.COLUMN_ID + "="
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
		String[] available = { ScheduleTable.COLUMN_START_DATE,
				ScheduleTable.COLUMN_END_DATE, ScheduleTable.COLUMN_TYPE,
				ScheduleTable.COLUMN_ID };
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
		case SCHEDULES:
			rowsUpdated = sqlDB.update(ScheduleTable.TABLE_SCHEDULE, values,
					selection, selectionArgs);
			break;
		case SCHEDULE_ID:
			String id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsUpdated = sqlDB.update(ScheduleTable.TABLE_SCHEDULE,
						values, ScheduleTable.COLUMN_ID + "=" + id, null);
			} else {
				rowsUpdated = sqlDB.update(ScheduleTable.TABLE_SCHEDULE,
						values, ScheduleTable.COLUMN_ID + "=" + id + " and "
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
