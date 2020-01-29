package pt.ismai.hungryme.MyRecipe;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

import pt.ismai.hungryme.HelpingClass.SQLMyRecipeHelper;
import pt.ismai.hungryme.R;
import pt.ismai.hungryme.ui.UI.BaseActivity;


public class MyRecipesActivity extends BaseActivity implements View.OnClickListener {

    public static final String MyPREFERENCES = "MyPrefs";
    private SharedPreferences prefs;
    private FloatingActionButton addRecipeButton;

    ListView myRecipeListView;
    ArrayList<MyRecipes> recipes; //the list of recipes that the user has created
    MyRecipeListAdapter adapter = null;

    // Create the database of the users recipes
    SQLMyRecipeHelper myRecipes;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recipes);
        BaseActivity.onActivityCreateSetTheme(this);
        setupToolbar();

        //Get the currently logged-in user
        prefs = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String email = prefs.getString("EMAIL", "");

        // A button where the user wants to create a new recipe
        addRecipeButton = (FloatingActionButton) findViewById(R.id.addRecipeButton);
        addRecipeButton.setOnClickListener(this);




        // Load all users recipes from the database
        myRecipeListView = (ListView) findViewById(R.id.myRecipeList);
        recipes = new ArrayList<>();
        adapter = new MyRecipeListAdapter(this, R.layout.list_item_recipe, recipes);
        myRecipeListView.setAdapter(adapter);

        // Cursor cursor = MainActivity.sqLiteHelper.getData("SELECT * FROM FOOD");

        // Get all data from the SQL database











        //Cursor cursor = CreateRecipeActivity.myRecipeSQLHelper.getData("SELECT * FROM MYRECIPES");
        //recipes.clear();

        /*
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String title = cursor.getString(1);
            String author = cursor.getString(2);
            String label = cursor.getString(3);
            String calories = cursor.getString(4);
            String ingredients = cursor.getString(5);
            String directions = cursor.getString(6);
            byte[] image = cursor.getBlob(7);

            // Add data into array list
            recipes.add(new MyRecipes(title, author, label, calories, ingredients, directions, image, id));
        }

        adapter.notifyDataSetChanged();


        myRecipeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {





            }
        });
        */



    }




    /**
     * Sets the title to favourites on the page
     */
    private void setupToolbar() {
        final ActionBar ab = getActionBarToolbar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Recipes");
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
        return R.id.nav_my_recipes;
    }

    @Override
    public boolean providesActivityToolbar() {
        return true;
    }

    // Function to go create a new recipe
    protected void goCreateRecipe() {
        Intent intent = new Intent(this, CreateRecipeActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.addRecipeButton:
                goCreateRecipe();
                break;
            default:
        }
    }




}
