package pt.ismai.hungryme.HelpingClass;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

public class SQLMyRecipeHelper extends SQLiteOpenHelper {

    // Users own recipes database
    public static final String MY_DB_NAME = "MyRecipes.db";
    public static final int DB_VERSION = 1;
    public static final String MY_RECIPES_TABLE = "myRecipes";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USER = "user";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_AUTHOR = "author";
    public static final String COLUMN_LABEL = "label";
    public static final String COLUMN_CALORIES = "calories";
    public static final String COLUMN_INGREDIENTS = "ingredients";
    public static final String COLUMN_DIRECTIONS = "directions";
    public static final String COLUMN_IMAGE = "image";




    // Constructor
    public SQLMyRecipeHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public void queryData(String sql) {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }

    public void insertData(String title, String author, String label, String calories, String ingredients, String directions, byte[] image) {

        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO MYRECIPES VALUES (NULL, ?, ?, ?, ?, ?, ?, ?)";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1, title);
        statement.bindString(2, author);
        statement.bindString(3, label);
        statement.bindString(4, calories);
        statement.bindString(5, ingredients);
        statement.bindString(6, directions);
        statement.bindBlob(7, image);

        statement.executeInsert();
    }

    public void updateData(String title, String author, String label, String calories, String ingredients, String directions, byte[] image, int id) {
        SQLiteDatabase database = getWritableDatabase();

        String sql = "UPDATE MYRECIPE SET title = ?, author = ?, label = ?, calories = ?, ingredients = ?, directions = ?, image = ? WHERE id = ?";
        SQLiteStatement statement = database.compileStatement(sql);

        statement.bindString(1, title);
        statement.bindString(2, author);
        statement.bindString(3, label);
        statement.bindString(4, calories);
        statement.bindString(5, ingredients);
        statement.bindString(6, directions);
        statement.bindBlob(7, image);
        statement.bindDouble(8, (double)id);

        statement.execute();
        database.close();
    }

    public  void deleteData(int id) {
        SQLiteDatabase database = getWritableDatabase();

        String sql = "DELETE FROM MYRECIPES WHERE id = ?";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindDouble(1, (double)id);

        statement.execute();
        database.close();
    }

    public Cursor getData(String sql){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, null);
    }

    public Cursor allData(){
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery("select * from MYRECIPES", null);
        return cursor;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
















    // Users recipe list columns
    /*
    public static final String CREATE_My_TABLE_RECIPES = "CREATE TABLE " + MY_RECIPES_TABLE + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_USER + " TEXT,"
            + COLUMN_TITLE + " TEXT,"
            + COLUMN_AUTHOR + " TEXT,"
            + COLUMN_LABEL + " TEXT,"
            + COLUMN_CALORIES + " TEXT,"
            + COLUMN_INGREDIENTS + " TEXT,"
            + COLUMN_DIRECTIONS + " TEXT,"
            + COLUMN_IMAGE + " TEXT);";
    */




}
