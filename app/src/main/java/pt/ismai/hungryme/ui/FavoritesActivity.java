package pt.ismai.hungryme.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import pt.ismai.hungryme.Recipe.FavoriteRecipe;
import pt.ismai.hungryme.HelpingClass.FavoriteListAdapter;
import pt.ismai.hungryme.HelpingClass.SQLiteOpenHelper;
import pt.ismai.hungryme.R;
import pt.ismai.hungryme.ui.Recipes.RecipeDetailActivity;
import pt.ismai.hungryme.ui.UI.BaseActivity;

/**
FavoritesActivity Displays a list of recipes that the user has previously saved to their favorites
Called whenever the app is opened up and when the "Favorites" category is clicked on.
The user is then able to click on one of the recipes in the list and is displaye more info about the selected recipe
 */
public class FavoritesActivity extends BaseActivity {
    public static final String MyPREFERENCES = "MyPrefs";
    private SharedPreferences prefs;

    ListView lv;
    ArrayList<FavoriteRecipe> recipes = new ArrayList<>(); //the list of recipes that have been favorited

    /**
     * onCreate it sets up the view of the list of recipes
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseActivity.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_favorites);
        setupToolbar();

        SQLiteOpenHelper db = new SQLiteOpenHelper(this);

        lv = (ListView) findViewById(R.id.lv);

        //Get the currently logged-in user
        prefs = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String email = prefs.getString("EMAIL", "");

        //this makes the call to the database and gets all of the recipes
        recipes = (ArrayList<FavoriteRecipe>) db.getAllRecipes(email);

        //sets up the action that will be performed when a recipe in the list is clicked
        FavoriteListAdapter favoriteListAdapter = new FavoriteListAdapter(this,R.layout.list_item_recipe, recipes);
        lv.setAdapter(favoriteListAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            //go to the actual recipe on click (when you're already inside of the recipe) i.e. click on VIEW DIRECTIONS button
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get recipe information
                FavoriteRecipe recipe = recipes.get(position);
                Intent details = new Intent(getBaseContext(), RecipeDetailActivity.class); //make view the web browser view

                // Pass recipe information to RecipeDetailActivity
                Bundle extras = new Bundle();
                extras.putString("ID", Integer.toString(recipe.get_id()));
                extras.putString("PHOTO_URL", recipe.get_photoURL());
                extras.putString("TITLE", recipe.get_name());
                extras.putString("AUTHOR", recipe.get_author());
                extras.putString("LABEL", recipe.get_label());
                extras.putString("INGREDIENTS", recipe.get_ingredients());
                extras.putString("CALORIES", recipe.getCalories());
                extras.putString("URL", recipe.get_url());
                extras.putString("HEALTHLABEL", recipe.get_healthLabel());

                details.putExtras(extras);
                startActivity(details);
            }
        });

    }

    /*
     * General classes for stuff like clicking on home button etc.
     */

    /**
     * Sets the title to favourites on the page
     */
    private void setupToolbar() {
        final ActionBar ab = getActionBarToolbar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Favorites");
    }

    /**
     * Actions to be done when something is selected.
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                openDrawer();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getSelfNavDrawerItem() {
        return R.id.nav_fast;
    }

    @Override
    public boolean providesActivityToolbar() {
        return true;
    }

}
