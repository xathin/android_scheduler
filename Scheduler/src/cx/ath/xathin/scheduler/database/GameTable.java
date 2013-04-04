package cx.ath.xathin.scheduler.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class GameTable {

	private static final String TAG = GameTable.class.getName();
	
	public static final String TABLE_GAME = "games";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_FIELD_ID = "field_id";
	public static final String COLUMN_OFFICIAL1_ID = "official1_id";
	public static final String COLUMN_OFFICIAL2_ID = "official2_id";
	public static final String COLUMN_DATETIME = "date_time";
	public static final String COLUMN_SCHEDULE_ID = "schedule_id";
	
	// Create table statement
	private static final String CREATE_DATABASE = "create table "
			+ TABLE_GAME + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_DATETIME
			+ " integer not null, "	+ COLUMN_FIELD_ID
			+ " integer references " + FieldTable.TABLE_FIELD + ", " + COLUMN_OFFICIAL1_ID
			+ " integer references " + OfficialTable.TABLE_OFFICIAL + ", " + COLUMN_OFFICIAL2_ID
			+ " integer references " + OfficialTable.TABLE_OFFICIAL + ", " + COLUMN_SCHEDULE_ID
			+ " integer references " + ScheduleTable.TABLE_SCHEDULE + ");";
			
	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(CREATE_DATABASE);
	}

	public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(TAG,
				"Upgrading database from version " + oldVersion + " to "
				+ newVersion + ", which will destroy all old data.");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_GAME);
		onCreate(db);
	}
	
}
