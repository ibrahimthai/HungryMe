package pt.ismai.hungryme.ui.Recipes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import pt.ismai.hungryme.R;
import pt.ismai.hungryme.Recipe.RecipeContent;
import pt.ismai.hungryme.ui.UI.BaseActivity;

/***
 * RecipesActivity
 * Handles interactions with Recipes section after a search
 */
public class RecipesActivity extends BaseActivity implements RecipeListFragment.Callback {
    private boolean twoPaneMode;
    MaterialSearchView searchView;

    /***
     * onCreate
     * Initializes view
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Set theme (set in settings)
        BaseActivity.onActivityCreateSetTheme(this);

        // Set view to activity_list layout (res/layout/activity_list.xml)
        setContentView(R.layout.activity_list);
        // Setup menu
        setupToolbar();

        // Setup search view
        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
            }

            @Override
            public void onSearchViewClosed() {
            }
        });

        if (isTwoPaneLayoutUsed()) {
            twoPaneMode = true;
            enableActiveItemState();
        }
        if (savedInstanceState == null && twoPaneMode) {
            setupDetailFragment();
        }
    }

    /***
     * onItemSelected
     * Called when a recipe is selected from search results
     * @param id
     */
    @Override
    public void onItemSelected(String id) {

        /*if in twoPaneMode, get recipe details and replace current screen
        else, get extra details and show those
         */
        if (twoPaneMode) {
            RecipeDetailFragment fragment = RecipeDetailFragment.newInstance(id);
            getFragmentManager().beginTransaction().replace(R.id.recipe_detail_container, fragment).commit();
        } else {
            Intent detailIntent = new Intent(this, RecipeDetailActivity.class);
            detailIntent.putExtra(RecipeDetailFragment.ARG_ITEM_ID, id);
            startActivity(detailIntent);
        }
    }

    /***
     * setupToolbar
     * Setup menu
     */
    private void setupToolbar() {
        final ActionBar ab = getActionBarToolbar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        // Set the top left to menu
        ab.setDisplayHomeAsUpEnabled(true);
        // Set title of screen
        String title = getIntent().getStringExtra("title");
        getSupportActionBar().setTitle(title);

    }

    /***
     * setupDetailFragment
     * Sets up recipe details
     */
    private void setupDetailFragment() {
        // Get recipe details and set them to details screen
        RecipeDetailFragment fragment =  RecipeDetailFragment.newInstance(RecipeContent.ITEMS.get(0).id);
        getFragmentManager().beginTransaction().replace(R.id.recipe_detail_container, fragment).commit();
    }

    /***
     * enableActiveItemState
     * Enables active item state (used in two pane mode)
     */
    private void enableActiveItemState() {
        RecipeListFragment fragmentById = (RecipeListFragment) getFragmentManager().findFragmentById(R.id.recipe_list);
        fragmentById.getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    /***
     * isTwoPaneLayoutUsed
     * Returns whether a two pane layout is used
     * @return true = two pane layout used. False = two pane layout not used
     */
    private boolean isTwoPaneLayoutUsed() {
        return findViewById(R.id.recipe_detail_container) != null;
    }

    /***
     * onCreateOptionsMenu
     * Initializes menu
     * @param menu
     * @return true on create
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sample_actions, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }

    /***
     * onOptionsItemSelected
     * Changes view on menu item that's selected
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Open menu
                openDrawer();
                return true;
        }
        // Switch view to menu item selected
        return super.onOptionsItemSelected(item);
    }

    /***
     * getSelfNavDrawerItem
     * Returns where on the list the menu was hit to get to this page
     * @return the position on the menu
     */
    @Override
    protected int getSelfNavDrawerItem() {
        return 0;
    }

    /***
     * providesActivityToolbar
     * Function used to determine whether screen has a menu button
     * @return true - the menu button is required for this page
     */
    @Override
    public boolean providesActivityToolbar() {
        return true;
    }
}
