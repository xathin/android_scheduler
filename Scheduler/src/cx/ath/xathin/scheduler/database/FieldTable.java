package cx.ath.xathin.scheduler.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class FieldTable {

	private static final String TAG = FieldTable.class.getName();
	
	public static final String TABLE_FIELD = "fields";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NAME = "name";
	
	// Create table statement
	private static final String CREATE_DATABASE = "create table "
			+ TABLE_FIELD + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_NAME
			+ " text not null)";
	
	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(CREATE_DATABASE);
	}

	public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(TAG,
				"Upgrading database from version " + oldVersion + " to "
				+ newVersion + ", which will destroy all old data.");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_FIELD);
		onCreate(db);
	}

}
