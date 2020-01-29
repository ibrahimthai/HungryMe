package pt.ismai.hungryme.HelpingClass;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import pt.ismai.hungryme.Recipe.FavoriteRecipe;

/***
 * SQLiteOpenHelper
 * Helper class to interact with an SQLite database
 * Used to store favorites
 */
public class SQLiteOpenHelper extends android.database.sqlite.SQLiteOpenHelper {
    public static final String TAG = SQLiteOpenHelper.class.getSimpleName();

    public static final String DB_NAME = "favoritesRecipes.db";
    public static final int DB_VERSION = 1;
    public static final String RECIPES_TABLE = "recipes";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_USER = "user";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_AUTHOR = "author";
    public static final String COLUMN_LABEL = "label";
    public static final String COLUMN_HEALTHLABEL = "healthLabel";
    public static final String COLUMN_CALORIES = "calories";
    public static final String COLUMN_INGREDIENTS = "ingredients";
    public static final String COLUMN_URL = "url";
    public static final String COLUMN_IMAGE = "image";

    public static final String CREATE_TABLE_RECIPES = "CREATE TABLE " + RECIPES_TABLE + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_USER + " TEXT,"
            + COLUMN_NAME + " TEXT,"
            + COLUMN_AUTHOR + " TEXT,"
            + COLUMN_LABEL + " TEXT,"
            + COLUMN_HEALTHLABEL + " TEXT,"
            + COLUMN_CALORIES + " TEXT,"
            + COLUMN_INGREDIENTS + " TEXT,"
            + COLUMN_URL + " TEXT,"
            + COLUMN_IMAGE + " TEXT);";

    /***
     * Constructor
     * @param context
     */
    public SQLiteOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    /***
     * onCreate
     * Creates database instance
     * @param dbR
     */
    @Override
    public void onCreate(SQLiteDatabase dbR) {
        dbR.execSQL(CREATE_TABLE_RECIPES);
    }

    /***
     * Creates new database on upgrade
     * @param dbR
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase dbR, int oldVersion, int newVersion) {
        dbR.execSQL("DROP TABLE IF EXISTS " + RECIPES_TABLE);
        onCreate(dbR);
    }

    /**
     * Resets the database
     */
    public void resetDb() {
        SQLiteDatabase dbR = this.getWritableDatabase();
        onUpgrade(dbR, 0, 0);
    }

    /***
     * Adds favorited recipe to database
     * @param name
     * @param email
     * @param author
     * @param calories
     * @param ingredients
     * @param url
     * @param image
     * @param label
     */

    public void addRecipe(String email, String name, String author, String label, String healthLabel, String calories, String ingredients, String url, String image) {
        // Opens database instance
        SQLiteDatabase dbR = this.getWritableDatabase();

        // Fills values for recipe
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_USER, email);
        values.put(COLUMN_AUTHOR, author);
        values.put(COLUMN_LABEL, label);
        values.put(COLUMN_HEALTHLABEL, healthLabel);
        values.put(COLUMN_CALORIES, calories);
        values.put(COLUMN_INGREDIENTS, ingredients);
        values.put(COLUMN_URL, url);
        values.put(COLUMN_IMAGE, image);
        values.put(COLUMN_LABEL, label);
        // Inserts recipe into database
        long id = dbR.insert(RECIPES_TABLE, null, values);
        // Closes database instance
        dbR.close();
        // Logs adding of recipe
        Log.d(TAG, "Recipe Added" + id);
    }

    /***
     * getAllRecipes
     * Fetches all favorited recipes from database
     * @return
     */
    public List<FavoriteRecipe> getAllRecipes(String email) {
        ArrayList<FavoriteRecipe> recipesList = new ArrayList<>();

        // Create SQL query and select only favourite recipes for the current user
        String selectQuery = "SELECT  * FROM " + RECIPES_TABLE
                + " WHERE " + COLUMN_USER + "='" + email + "';";


        // Open database instance
        SQLiteDatabase db = this.getWritableDatabase();
        // Send query and move "cursor" to the top of the results
        Cursor cursor = db.rawQuery(selectQuery, null);
        // If there are results, loop through all results
        if (cursor.moveToFirst()) {
            do {
                // Create new recipe object and add to it using information from query result
                FavoriteRecipe recipe = new FavoriteRecipe();
                recipe.set_id(Integer.parseInt(cursor.getString(0)));
                recipe.set_name(cursor.getString(2));
                recipe.set_author(cursor.getString(3));
                recipe.set_label(cursor.getString(4));
                recipe.set_healthLabel(cursor.getString(5));
                recipe.setCalories(cursor.getString(6));
                recipe.set_ingredients(cursor.getString(7));
                recipe.set_url(cursor.getString(8));
                recipe.set_photoURL(cursor.getString(9));
                // Add recipe to list
                recipesList.add(recipe);
            } while (cursor.moveToNext());
        }
        return recipesList;
    }
}