package pt.ismai.hungryme.HelpingClass;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import pt.ismai.hungryme.R;
import pt.ismai.hungryme.Recipe.RecipeContent;

/***
 * MyListAdapter
 * Adapter used for Recipe Lists
 * Used to set view for RecipeListFragments
 */
public class MyListAdapter extends BaseAdapter {
    private Activity activity;

    /***
     * Constructor
     * @param baseActivity
     */
    public MyListAdapter(Activity baseActivity) {
        RecipeContent.ITEMS.clear();
        activity = baseActivity;
    }

    /***
     * getCount
     * Gets the number of recipes
     * @return Number of recipes
     */
    @Override
    public int getCount() {
        return RecipeContent.ITEMS.size();
    }

    /***
     * getItem
     * Gets the Recipe at a certain position
     * @param position
     * @return recipe at position
     */
    @Override
    public Object getItem(int position) {
        return RecipeContent.ITEMS.get(position);
    }

    /***
     * getItemId
     * Gets the id for a recipe at a certain position
     * @param position
     * @return recipe id at position
     */
    @Override
    public long getItemId(int position) {
        return RecipeContent.ITEMS.get(position).id.hashCode();
    }

    /***
     * getView
     * Creates the view for a single recipe
     * @param position
     * @param convertView
     * @param container
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup container) {
        // If there's no view, layout used from list_item_recipe XML file (res/layout/list_item_recipe.xml)
        if (convertView == null) {
            convertView = LayoutInflater.from(activity).inflate(R.layout.list_item_recipe, container, false);
        }

        // Get the recipe item at the position and fills the data for the view from the recipe information
        final RecipeContent.RecipeItem item = (RecipeContent.RecipeItem) getItem(position);
        ((TextView) convertView.findViewById(R.id.recipe_title)).setText(item.title);
        ((TextView) convertView.findViewById(R.id.recipe_calories)).setText(item.calories + " kcal");

        // Checks to see if the label for the recipe is empty, if so sets the visibility to false
        if (item.label.equals("")){
            ((TextView) convertView.findViewById(R.id.label)).setText("");
            (convertView.findViewById(R.id.label)).setVisibility(View.INVISIBLE);
        } else {
            ((TextView) convertView.findViewById(R.id.label)).setText(item.label + " ");
            (convertView.findViewById(R.id.label)).setVisibility(View.VISIBLE); //THIS LINE COST ME 4 HOURS OF DEV TIME -.-"
        }

        final ImageView img = (ImageView) convertView.findViewById(R.id.thumbnail);
        // Loads and draws the image associated with the recipe
        Glide.with(activity)
                .load(item.photoURL)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(activity.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        img.setImageDrawable(circularBitmapDrawable);
                    }
                });


        return convertView;
    }
}
