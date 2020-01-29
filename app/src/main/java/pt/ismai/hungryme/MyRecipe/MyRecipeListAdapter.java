package pt.ismai.hungryme.MyRecipe;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import pt.ismai.hungryme.R;

/***
 * MyListAdapter
 * Adapter used for Recipe Lists
 * Used to set view for RecipeListFragments
 */
public class MyRecipeListAdapter extends BaseAdapter {
    private Activity activity;
    ArrayList<MyRecipes> recipesList = new ArrayList<>();

    private Context context;
    private int layout;
    private ArrayList<MyRecipes> foodsList;

    public MyRecipeListAdapter(Context context, int layout, ArrayList<MyRecipes> foodsList) {
        this.context = context;
        this.layout = layout;
        this.foodsList = foodsList;
    }

    @Override
    public int getCount() {
        return foodsList.size();
    }

    @Override
    public Object getItem(int position) {
        return foodsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder{
        ImageView imageView;
        TextView recipeTitle;
        TextView recipeCalories;
        TextView recipeLabel;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        View row = view;
        ViewHolder holder = new ViewHolder();

        // If there's no view, layout used from list_item_recipe XML file (res/layout/list_item_recipe.xml)
        if (view == null) {
            view = LayoutInflater.from(activity).inflate(R.layout.list_item_recipe, viewGroup, false);
        }

        if(row == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);

            // Recipe Item Design
            holder.recipeTitle = (TextView) row.findViewById(R.id.recipe_title);
            holder.recipeCalories = (TextView) row.findViewById(R.id.recipe_calories);
            holder.recipeLabel = (TextView) row.findViewById(R.id.label);
            holder.imageView = (ImageView) row.findViewById(R.id.thumbnail);
            row.setTag(holder);
        }
        else {
            holder = (ViewHolder) row.getTag();
        }

        MyRecipes food = foodsList.get(position);

        holder.recipeTitle.setText(food.title);
        holder.recipeCalories.setText(food.calories);
        holder.recipeLabel.setText(food.label);

        byte[] foodImage = food.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(foodImage, 0, foodImage.length);
        holder.imageView.setImageBitmap(bitmap);

        return row;
    }



}
