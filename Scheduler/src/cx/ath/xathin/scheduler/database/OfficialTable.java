package cx.ath.xathin.scheduler.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class OfficialTable {
	
	private static final String TAG = OfficialTable.class.getName();
	
	public static final String TABLE_OFFICIAL = "officials";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_FIRST_NAME = "first_name";
	public static final String COLUMN_LAST_NAME = "last_name";
	public static final String COLUMN_PHONE1 = "phone1";
	public static final String COLUMN_PHONE2 = "phone2";
	public static final String COLUMN_LEVEL = "level";
	public static final String COLUMN_MIN_PAY = "min_pay";
	
	// Create database table statement
	private static final String CREATE_DATABASE = "create table "
			+ TABLE_OFFICIAL + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_FIRST_NAME
			+ " text not null" + COLUMN_LAST_NAME
			+ " text not null" + COLUMN_PHONE1
			+ " text not null" + COLUMN_PHONE2
			+ " text" + COLUMN_LEVEL
			+ " integer" + COLUMN_MIN_PAY
			+ " real);";
	
	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(CREATE_DATABASE);
	}

	public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(TAG,
				"Upgrading database from version " + oldVersion + " to "
				+ newVersion + ", which will destroy all old data.");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_OFFICIAL);
		onCreate(db);
	}

}
