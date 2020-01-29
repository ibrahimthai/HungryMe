package pt.ismai.hungryme.ui.Recipes;

import android.content.Intent;
import android.os.Bundle;

import pt.ismai.hungryme.R;
import pt.ismai.hungryme.Recipe.RecipeContent;
import pt.ismai.hungryme.ui.UI.BaseActivity;

/***
 * RecipeDetailActivity
 * Responsible for interacting with recipe details
 */
public class RecipeDetailActivity extends BaseActivity {

    private String TAG = RecipeContent.class.getSimpleName();

    /***
     * Creates instance of RecipeDetailActivity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Sets theme (as dictated in settings)
        BaseActivity.onActivityCreateSetTheme(this);
        // Sets layout as per activity_detail XML file (res/layout/activity_detail.xml)
        setContentView(R.layout.activity_detail);

        // Sets the menu button as a back button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        Intent intent = getIntent();

        RecipeDetailFragment fragment;
        // Gets recipe details from the item selected and creates a new instance

        // If item_id in arguments, accessed through browsing. Item_id is all that's necessary
        if (intent.getStringExtra(RecipeDetailFragment.ARG_ITEM_ID) != null)
            fragment = RecipeDetailFragment.newInstance(intent.getStringExtra(RecipeDetailFragment.ARG_ITEM_ID));
        // If item_id not in arguments, accessed through favorites. Get arguments from bundle
        else
            fragment = RecipeDetailFragment.newInstance(intent.getExtras());
        // Sets instance
        getFragmentManager().beginTransaction().replace(R.id.recipe_detail_container, fragment).commit();
    }

    /***
     * providesActivityToolbar
     * Sets whether or not the menu is accessible from this activity
     * @return false as the menu button is changed to a back button
     */
    @Override
    public boolean providesActivityToolbar() {
        return false;
    }

}
