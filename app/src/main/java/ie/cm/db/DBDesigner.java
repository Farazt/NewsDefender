package ie.cm.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBDesigner extends SQLiteOpenHelper
{
	public static final String TABLE_NEWS_ITEM = "table_news_items";
	public static final String COLUMN_ID = "newsItemId";
	public static final String COLUMN_HEADING = "newsHeading";
	public static final String COLUMN_DESCRIPTION = "newsDescription";
	public static final String COLUMN_DATE = "date";
	public static final String COLUMN_TIME = "time";
	public static final String COLUMN_ImageURL = "imageUrl";
	
	private static final String DATABASE_NAME = "newsItem.db";
	private static final int DATABASE_VERSION = 5;

	//DB Fiedlds for User Table
	public	 static final String TABLE_NEWS_USER="table_user_table";
	public static final String COLUMN_FBID="id";
	public static final String COLUMN_NAME="name";
	public static final String COLUMN_EMAIL="email";
	public static final String COLUMN_GENDER="gender";
	public static final String COLUMN_DOB="dob";
	//USER TABLE
	private static final String DATABASE_CREATE_TABLE_USER = "create table "
			+ TABLE_NEWS_USER + "( " + COLUMN_FBID + " text, "
			+ COLUMN_EMAIL + " text,"
			+ COLUMN_GENDER + " text,"
			+ COLUMN_DOB + " text,"
			+ COLUMN_NAME+ " text);" ; //SQLite doesn't support boolean types


	// Database creation sql statement
	private static final String DATABASE_CREATE_TABLE_NEWS = "create table "
			+ TABLE_NEWS_ITEM + "( " + COLUMN_ID + " integer primary key autoincrement, "
			+ COLUMN_HEADING + " text not null,"
			+ COLUMN_DESCRIPTION + " text not null,"
			+ COLUMN_DATE + " text,"
			+ COLUMN_TIME + " text not null,"
			+ COLUMN_ImageURL + " text not null);"; //SQLite doesn't support boolean types
		
	public DBDesigner(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE_TABLE_NEWS);
		database.execSQL(DATABASE_CREATE_TABLE_USER);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(DBDesigner.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NEWS_ITEM);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NEWS_USER);
		onCreate(db);
	}

}


