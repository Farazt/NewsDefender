package ie.cm.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ie.cm.models.NewsItem;
import ie.cm.models.User;

public class DBManager {

	private SQLiteDatabase database;
	private DBDesigner dbHelper;

	public DBManager(Context context) {
		dbHelper = new DBDesigner(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		database.close();
	}

	public void insert(NewsItem nI) {
		ContentValues values = new ContentValues();
		values.put(DBDesigner.COLUMN_DESCRIPTION, nI.getNewsDesc());
		values.put(DBDesigner.COLUMN_HEADING, nI.getNewsHeading());
		values.put(DBDesigner.COLUMN_DATE, nI.getDate());
		values.put(DBDesigner.COLUMN_TIME, nI.getTime());
		values.put(DBDesigner.COLUMN_ImageURL, nI.getImageID());
		long insertId = database.insert(DBDesigner.TABLE_NEWS_ITEM, null,
				values);
	}
	public void insertUSER(User user){
		ContentValues values = new ContentValues();
		values.put(DBDesigner.COLUMN_FBID, user.getFbID());
		values.put(DBDesigner.COLUMN_NAME, user.getFbName());
		values.put(DBDesigner.COLUMN_EMAIL, user.getEmail());
		values.put(DBDesigner.COLUMN_GENDER, user.getGender());
		values.put(DBDesigner.COLUMN_DOB, user.getBirthday());

		long insertId = database.insert(DBDesigner.TABLE_NEWS_USER, null,
				values);
		Log.i("User Inserted","Inserted Id="+ insertId);
	}

	public void delete(int id) {

		Log.v("DB", "NewsItem deleted with id: " + id);
		database.delete(DBDesigner.TABLE_NEWS_ITEM,
				DBDesigner.COLUMN_ID + " = " + id, null);
	}



	public List<NewsItem> getAll() {
		List<NewsItem> allSavedNews = new ArrayList<NewsItem>();
		Cursor cursor = database.rawQuery("SELECT * FROM "
				+ DBDesigner.TABLE_NEWS_ITEM, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			NewsItem pojo = toNewsItem(cursor);
			allSavedNews.add(pojo);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return allSavedNews;
	}

	private NewsItem toNewsItem(Cursor cursor) {
		NewsItem nwItem = new NewsItem();
		nwItem.setNewsId(cursor.getInt(0));
		nwItem.setNewsHeading(cursor.getString(1));
		nwItem.setNewsDesc(cursor.getString(2));
		nwItem.setDate(cursor.getString(3));
		nwItem.setTime(cursor.getString(4)) ;
		nwItem.setImageID(cursor.getString(5));
		return nwItem;
	}
	public void deleteUser(String id){
		Log.v("DB", "NewsItem deleted with id: " + id);
		database.delete(DBDesigner.TABLE_NEWS_USER,
				DBDesigner.COLUMN_FBID + " = " + id, null);
	}

	public User get() {
		User user = null;
		Cursor cursor = database.rawQuery("SELECT * FROM "
				+ DBDesigner.TABLE_NEWS_USER , null);
		cursor.moveToFirst();
		if(cursor!=null && cursor.getCount()>0){
			user=new User(cursor.getString(0),cursor.getString(4),cursor.getString(1),cursor.getString(2),cursor.getString(3));
			Log.i("Cursor","Result="+ user.toString());
		}
		// Make sure to close the cursor
		cursor.close();
		return user;
	}



/*
	public List<Coffee> getFavourites() {
		List<Coffee> coffees = new ArrayList<Coffee>();
		Cursor cursor = database.rawQuery("SELECT * FROM "
				+ DBDesigner.TABLE_COFFEE + " WHERE "
				+ DBDesigner.COLUMN_FAV + " = 1", null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Coffee pojo = toCoffee(cursor);
			coffees.add(pojo);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return coffees;
	}
	
	private Coffee toCoffee(Cursor cursor) {
		Coffee pojo = new Coffee();
		pojo.coffeeId = cursor.getInt(0);
		pojo.name = cursor.getString(1);
		pojo.shop = cursor.getString(2);
		pojo.price = cursor.getDouble(3);
		pojo.rating = cursor.getDouble(4);
		pojo.favourite = (cursor.getInt(5) == 1) ? true : false;

		return pojo;
	}

	public void setupList() {
		Coffee c1 = new Coffee("Mocca Latte", "Ardkeen Stores", 4, 2.99, false);
		Coffee c2 = new Coffee("Espresso", "Tescos Stores",3.5, 1.99, true);
		Coffee c3 = new Coffee("Standard Black", "Ardkeen Stores",2.5, 1.99, true);
		Coffee c4 = new Coffee("Cappuccino", "Spar Shop",2.5, 1.49, false);

		insert(c1);
		insert(c2);
		insert(c3);
		insert(c4);
	}*/
}
