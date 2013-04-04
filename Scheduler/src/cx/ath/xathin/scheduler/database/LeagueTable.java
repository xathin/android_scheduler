package cx.ath.xathin.scheduler.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class LeagueTable {

	private static final String TAG = LeagueTable.class.getName();
	
	public static final String TABLE_LEAGUE = "leagues";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_PAY = "pay";
	
	// Create table statement
	private static final String CREATE_DATABASE = "create table "
			+ TABLE_LEAGUE + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_NAME
			+ " text not null, " + COLUMN_PAY
			+ " real not null)";
	
	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(CREATE_DATABASE);
	}

	public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(TAG,
				"Upgrading database from version " + oldVersion + " to "
				+ newVersion + ", which will destroy all old data.");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LEAGUE);
		onCreate(db);
	}
	
}
