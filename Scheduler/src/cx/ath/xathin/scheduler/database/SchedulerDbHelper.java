package cx.ath.xathin.scheduler.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SchedulerDbHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "scheduler.db";
	private static final int DATABASE_VERSION = 1;

	public SchedulerDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		if (!db.isReadOnly()) {
			// Enable foreign key constraints
			db.execSQL("PRAGMA foreign_keys=ON;");
		}
		OfficialTable.onCreate(db);
		LeagueTable.onCreate(db);
		FieldTable.onCreate(db);
		ScheduleTable.onCreate(db);
		GameTable.onCreate(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		OfficialTable.onUpgrade(db, oldVersion, newVersion);
		LeagueTable.onUpgrade(db, oldVersion, newVersion);
		FieldTable.onUpgrade(db, oldVersion, newVersion);
		ScheduleTable.onUpgrade(db, oldVersion, newVersion);
		GameTable.onUpgrade(db, oldVersion, newVersion);
	}

}
