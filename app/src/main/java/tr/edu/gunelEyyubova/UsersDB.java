package tr.edu.gunelEyyubova;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

public class UsersDB extends SQLiteOpenHelper {
    public static final String DBNAME = "USERS.db";
    public UsersDB(Context context) {
        super(context, "USERS.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        MyDB.execSQL("CREATE TABLE users(username VARCHAR PRIMARY KEY, password VARCHAR,firstname VARCHAR,lastname VARCHAR,birthdate VARCHAR," +
                "phonenumber VARCHAR,image BLOB)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int i, int i1) {
        MyDB.execSQL("DROP TABLE IF EXISTS users");
    }

    public Boolean insertData(String user_name, String pass_word, String first_name, String last_name,String phone_number, String birth_date,
                              byte[] image_bytes){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put("username", user_name);
        contentValues.put("password", pass_word);
        contentValues.put("firstname", first_name);
        contentValues.put("lastname", last_name);
        contentValues.put("birthdate", birth_date);
        contentValues.put("phonenumber", phone_number);
        contentValues.put("image", image_bytes);
        long result = MyDB.insert("users", null, contentValues);

        if(result==-1) return false;

        else return true;

    }

    public Boolean checkUsername(String username) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("SELECT * FROM users WHERE username = ?", new String[]{username});
        if (cursor.getCount() > 0)  return true;
        else return false;

    }

    public Boolean checkUsernamePassword(String username, String password){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("SELECT * FROM users WHERE username = ? AND  password = ?", new String[] {username,password});

        if(cursor.getCount()>0) return true;

        else return false;

    }







}