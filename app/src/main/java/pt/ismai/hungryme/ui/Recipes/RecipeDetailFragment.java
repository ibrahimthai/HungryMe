package pt.ismai.hungryme.ui.Recipes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import butterknife.Bind;
import pt.ismai.hungryme.HelpingClass.SQLiteOpenHelper;
import pt.ismai.hungryme.R;
import pt.ismai.hungryme.Recipe.FavoriteRecipe;
import pt.ismai.hungryme.Recipe.RecipeContent;
import pt.ismai.hungryme.ui.UI.BaseActivity;
import pt.ismai.hungryme.ui.UI.BaseFragment;
import pt.ismai.hungryme.ui.WebViewActivity;

/***
 * RecipeDetailFragment
 * Contains view for a single Recipes detail
 */
public class RecipeDetailFragment extends BaseFragment {
    public static final String MyPREFERENCES = "MyPrefs";

    public static final String ARG_ITEM_ID = "item_id";
    public static final String ARG_TITLE = "TITLE";
    private RecipeContent.RecipeItem recipeItem;
    private FloatingActionButton fab;
    private boolean hideFavoriteButton;
    SharedPreferences prefs;
    SQLiteOpenHelper dbRecipes;


    @Bind(R.id.dButton)
    Button directions;

    @Bind(R.id.label)
    TextView label;

    @Bind(R.id.author)
    TextView author;

    @Bind(R.id.backdrop)
    ImageView backdropImg;

    @Bind(R.id.card_Title)
    TextView cardTitle;

    @Bind(R.id.calories)
    TextView calories;

    @Bind(R.id.ingredients)
    TextView ingredients;

    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;

    /***
     * onCreate
     * Sets recipe item
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        hideFavoriteButton = false;
        // Gets recipe and stores it
        if (bundle.containsKey(ARG_ITEM_ID)) {
            recipeItem = RecipeContent.ITEM_MAP.get(bundle.getString(ARG_ITEM_ID));
        }
        // If send through a Bundle (in Favorites), get arguments from bundle to create recipe item
        else if (bundle.containsKey(ARG_TITLE)) {

            recipeItem = new RecipeContent.RecipeItem(
                    bundle.getString("ID"),
                    bundle.getString("PHOTO_URL"),
                    bundle.getString("TITLE"),
                    bundle.getString("AUTHOR"),
                    bundle.getString("LABEL"),
                    bundle.getString("HEALTHLABEL"),
                    bundle.getString("INGREDIENTS"),
                    bundle.getString("CALORIES"),
                    bundle.getString("URL")

            );
            hideFavoriteButton = true;
        }

        prefs = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        setHasOptionsMenu(true);
    }

    /***
     * Creates view
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return created view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Calls function in BaseFragment to set layout and bind to view
        View rootView = inflateAndBind(inflater, container, R.layout.fragment_quick_detail);

        // Set menu if activity is supposed to have menu button
        if (!((BaseActivity) getActivity()).providesActivityToolbar()) {
            ((BaseActivity) getActivity()).setToolbar((Toolbar) rootView.findViewById(R.id.toolbar));
        }

        // If recipe item exists, set information
        if (recipeItem != null) {
            // Loads image background
            loadBackdrop();
            // Set the title of the screen as the recipe name
            collapsingToolbar.setTitle(recipeItem.title);
            collapsingToolbar.setExpandedTitleTextAppearance(R.style.toolbar_text);
            // Set recipe information
            cardTitle.setText(recipeItem.title);
            author.setText(recipeItem.author);
            label.setText(recipeItem.healthlabel);

            calories.setText("Calories: " + recipeItem.calories);
            ingredients.setText(recipeItem.ingredients);

            // Create button and link it to the recipe online
            Button dButton = (Button) rootView.findViewById(R.id.dButton);
            dButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent webBrowser = new Intent(getActivity(), WebViewActivity.class);
                    webBrowser.putExtra("url", recipeItem.url);
                    startActivity(webBrowser);
                }
            });

            // Create database instance
            dbRecipes = new SQLiteOpenHelper(getActivity());

            // Add favorite button
            FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
            if (!hideFavoriteButton) {
                // Set button to add recipe to favorites by adding recipe to SQL database
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            String nameR = recipeItem.title;
                            String authorR = recipeItem.author;
                            String labelR = recipeItem.label;
                            String healthLabelR = recipeItem.healthlabel;
                            String caloriesR = recipeItem.calories;
                            String ingredientsR = recipeItem.ingredients;
                            String urlR = recipeItem.url;
                            String imageR = recipeItem.photoURL;

                            //make sure recipe is not a duplicate
                            if (checkIfAlreadyFavorited(nameR, authorR, caloriesR, ingredientsR, urlR, imageR) != true) {
                                String email = prefs.getString("EMAIL", "");
                                dbRecipes.addRecipe(email, nameR, authorR, labelR, healthLabelR, caloriesR, ingredientsR, urlR, imageR);
                                Toast.makeText(getContext(), "Added to Favorites", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getContext(), "Duplicate Recipe, Check Favorites", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
            }
            // If in Favorites, hide favorite button
            else {
                // Since the button is anchored, need to remove the anchor to make it invisible
                CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
                p.setAnchorId(View.NO_ID);
                fab.setLayoutParams(p);
                fab.setVisibility(View.GONE);
            }
        }
        return rootView;
    }


    /**
     * Checks if a recipe is already favorited before, returns True if it has been.
     * @param nameR
     * @param authorR
     * @param caloriesR
     * @param ingredientsR
     * @param urlR
     * @param imageR
     * @return Boolean value, True if is a duplicate
     */
    public boolean checkIfAlreadyFavorited(String nameR, String authorR, String caloriesR, String ingredientsR, String urlR, String imageR) {
        //Get the currently logged in user
        String email = prefs.getString("EMAIL", "");

        //Get list of all recipes
        List<FavoriteRecipe> favoriteRecipes;
        favoriteRecipes = dbRecipes.getAllRecipes(email);

        //go through list and see if the given recipe is already in the list
        for (FavoriteRecipe compareR : favoriteRecipes) {
            if (compareRecipe(nameR,authorR, caloriesR, ingredientsR, urlR, imageR, compareR) == true) {
                return true;
            }
        }

        return false;
    }
    /**
     * Compares 2 recipes, returns True if they are the same recipe
     * @param compareRecipe
     * @return Boolean True if recipes are the same
     */
    public boolean compareRecipe(String nameR, String authorR, String caloriesR, String ingredientsR, String urlR, String imageR, FavoriteRecipe compareRecipe) {
        //go through and compare each attribute except for id
        if (!nameR.equals(compareRecipe.get_name())) {
            return false;
        }
        else if (!authorR.equals(compareRecipe.get_author())) {
            return false;
        }
        else if (!caloriesR.equals(compareRecipe.getCalories())) {
            return false;
        }
        else if (!ingredientsR.equals(compareRecipe.get_ingredients())) {
            return false;
        }
        else if (!urlR.equals(compareRecipe.get_url())) {
            return false;
        }
        else if (!imageR.equals(compareRecipe.get_photoURL())) {
            return false;
        }
        return true;
    }
    /***
     * loadBackDrop
     * Loads image as a backdrop
     */
    private void loadBackdrop() {
        Glide.with(getActivity())
                .load(recipeItem.photoURL)
                .asBitmap()
                .centerCrop()
                .into(new BitmapImageViewTarget(backdropImg) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        super.setResource(resource);
                    }
                });
    }

    /***
     * onCreateOptionsMenu
     * Creates options menu
     * @param menu
     * @param inflater
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.sample_actions, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /***
     * onOptionsItemSelected
     * Change view to item selected in menu
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /***
     * newInstance
     * Creates a new instance of a recipes details
     * @param itemID
     * @return an empty recipe detail fragment
     */
    public static RecipeDetailFragment newInstance(String itemID) {
        RecipeDetailFragment fragment = new RecipeDetailFragment();
        Bundle args = new Bundle();
        args.putString(RecipeDetailFragment.ARG_ITEM_ID, itemID);
        fragment.setArguments(args);
        return fragment;
    }

    /***
     * newInstance
     * Creates a new instance of a recipes details
     * @param bundle - arguments passed in through a Bundle
     * @return an empty recipe detail fragment
     */
    public static RecipeDetailFragment newInstance(Bundle bundle) {
        RecipeDetailFragment fragment = new RecipeDetailFragment();

        fragment.setArguments(bundle);
        return fragment;
    }

    /***
     * Empty constructor
     */
    public RecipeDetailFragment() {}
}
