package pt.ismai.hungryme.LoginAndRegister;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

/**
 * This class creates an SQL database that will store all the user's account information and credentials
 */
public class DbHelper extends SQLiteOpenHelper {
        // Initializes a name for the database file
        public static final String TAG = DbHelper.class.getSimpleName();
        public static final String DB_NAME = "myapp.db";
        public static final int DB_VERSION = 1;

        // Column names for the SQL database
        public static final String USER_TABLE = "users";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_PASS = "password";

        // SQL command to create a table containing account information
        public static final String CREATE_TABLE_USERS = "CREATE TABLE " + USER_TABLE + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_EMAIL + " TEXT,"
            + COLUMN_PASS + " TEXT);";

        public DbHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE_USERS);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
            onCreate(db);
        }

    /**
     * Adds the users credentials to the database in order for the user create an account.
     * @param email* The email address to add.
     * @param password* The password.
     */
    public void addUser(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASS, password);

        long id = db.insert(USER_TABLE, null, values);
        db.close();

        Log.d(TAG, "user inserted" + id);
    }

    /**
     * Gets the users credentials from the database in order for the user to login.
     * @param email* The email address.
     * @param pass* The password.
     */
    public boolean getUser(String email, String pass){
        String selectQuery = "select * from  " + USER_TABLE + " where " +
                COLUMN_EMAIL + " = " + "'"+email+"'" + " and " + COLUMN_PASS + " = " + "'"+pass+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            return true;
        }
        cursor.close();
        db.close();

        return false;
    }

    /**
     * Gets the users email address to check if their account exists in the database
     * @param email* The email address.
     */
    public boolean getEmail(String email){
        String selectQuery = "select * from  " + USER_TABLE + " where " + COLUMN_EMAIL + " = " + "'"+email+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            return true;
        }
        cursor.close();
        db.close();

        return false;
    }

    /**
     * Changes password for user account
     * @param email* The email address.
     * @param password* The password.
     */
    public void changePassword(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "UPDATE " + USER_TABLE + " SET " + COLUMN_PASS + " = '" + password + "' WHERE " + COLUMN_EMAIL + " = '" + email + "';";
        db.execSQL(query);
        db.close();

        Log.d(TAG, "password updated for " + email);
    }
}
