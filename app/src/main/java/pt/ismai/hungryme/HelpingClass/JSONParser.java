package pt.ismai.hungryme.HelpingClass;

import android.os.AsyncTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import pt.ismai.hungryme.Recipe.HttpHandler;
import pt.ismai.hungryme.Recipe.RecipeContent;

/***
 * JSONParser class
 * Handles all the API interactions (using JSON)
 */
public class JSONParser extends AsyncTask<Void, Void, Void> {

    public String jsonUrl = "https://api.edamam.com/"; //Api Keys and Connection URL
    public String searchStart = "search?q=";
    public String search = "healthy";
    private String apiKey = "&app_id=d0e5d5bd&app_key=e6bf3f8ea4091e2b45d932587d7591fd";
    public String results = "&from=0&to=";
    public int resultsNum = 35;

    private MyListAdapter a;

    /***
     * Constructor
     * @param adapter
     */
    public JSONParser(MyListAdapter adapter) {
        a = adapter;
    }

    /***
     * doInBackground
     * Fetches data from the API in the background
     * @param arg0
     */
    protected Void doInBackground(Void... arg0) {
        // Initialize json string (has all the information to send request)
        String json = jsonUrl + searchStart + search + apiKey + results + resultsNum;
        // Http handler to send json data
        HttpHandler sh = new HttpHandler();
        // Make the API call and store result in jsonStr
        String jsonStr = sh.makeServiceCall(json);

        if (jsonStr != null) {
            try {
                // Parse returned data into a JSON object
                JSONObject jsonObj = new JSONObject(jsonStr);

                // Get the recipes returned
                JSONArray recipes = jsonObj.getJSONArray("hits");

                // For every recipe, extract the information from the JSON string
                for (int i = 0; i < recipes.length(); i++) {
                    JSONObject c = recipes.getJSONObject(i);

                    JSONObject recipe = (JSONObject) c.get("recipe");
                    String id = String.valueOf(i);
                    String photoURL = recipe.getString("image");
                    String url = recipe.getString("url");
                    String title = recipe.getString("label");
                    String author = recipe.getString("source");
                    String label = "";
                    Integer calories = Math.round(recipe.getInt("calories"));
                    String caloriesString = String.valueOf(calories);
                    JSONArray dietlabels = recipe.getJSONArray("dietLabels");
                    for (int j = 0; j < dietlabels.length(); j++) {
                            label += dietlabels.get(j).toString() + " ";
                    }

                    String ingredients = "";
                    JSONArray ingredientsArray = recipe.getJSONArray("ingredientLines");
                    for (int k = 0; k < ingredientsArray.length(); k++) {
                        ingredients += "- " + ingredientsArray.get(k).toString() + ";\n";
                    }
                    String healthlabel = "";
                    JSONArray healthlabels = recipe.getJSONArray("healthLabels");
                    for (int j = 0; j < healthlabels.length(); j++) {
                        healthlabel += healthlabels.get(j).toString() + " ";
                    }

                    // Create RecipeContent object to store the recipe that was just created
                    // *** This will be replaces in the future as this call should be in the RecipeContent class ***
                    RecipeContent.RecipeItem item = new RecipeContent.RecipeItem(id, photoURL, title, author, label, healthlabel, ingredients, caloriesString, url);
                    RecipeContent.ITEMS.add(item);
                    RecipeContent.ITEM_MAP.put(item.id, item);
                }
            } catch (final JSONException e) {}
        } else {}
        return null;
    }

    /***
     * onPostExecute
     * After making the call, notify client that data changed so that program will update the dataset
     * @param aVoid
     */
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        a.notifyDataSetChanged();
    }
}