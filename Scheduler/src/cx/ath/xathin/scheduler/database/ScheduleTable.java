package cx.ath.xathin.scheduler.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ScheduleTable {

	private static final String TAG = ScheduleTable.class.getName();
	
	public static final String TABLE_SCHEDULE = "schedules";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_START_DATE = "start";
	public static final String COLUMN_END_DATE = "end";
	public static final String COLUMN_TYPE = "type";
	
	// Create table statement
	private static final String CREATE_DATABASE = "create table "
			+ TABLE_SCHEDULE + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_START_DATE
			+ " integer not null, " + COLUMN_END_DATE
			+ " integer not null, " + COLUMN_TYPE
			+ " text not null)";
	
	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(CREATE_DATABASE);
	}

	public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(TAG,
				"Upgrading database from version " + oldVersion + " to "
				+ newVersion + ", which will destroy all old data.");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCHEDULE);
		onCreate(db);
	}


}
