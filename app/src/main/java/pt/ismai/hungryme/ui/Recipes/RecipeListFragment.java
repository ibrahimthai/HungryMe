package pt.ismai.hungryme.ui.Recipes;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import pt.ismai.hungryme.HelpingClass.JSONParser;
import pt.ismai.hungryme.HelpingClass.MyListAdapter;
import pt.ismai.hungryme.Recipe.RecipeContent;

/***
 * RecipeListFragment
 * It's a single list element in a Recipe list (A recipe in a list of many)
 */
public class RecipeListFragment extends ListFragment {

    private Callback callback = dummyCallback;

    /***
     * Sets the interface
     */
    public interface Callback {
        void onItemSelected(String id);
    }

    /***
     * Creates a dummy callback for testing
     */
    private static final Callback dummyCallback = new Callback() {
        @Override
        public void onItemSelected(String id) {
        }
    };

    /***
     * onCreate
     * Initializes list elements using searched string
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get searched string
        String search = super.getActivity().getIntent().getStringExtra("search");
        // Create a list using adapter
        MyListAdapter adapter = new MyListAdapter(getActivity());
        // Fetch recipes from API using search text
        JSONParser rad = new JSONParser(adapter);
        rad.search = search;
        rad.execute();
        setListAdapter(adapter);
        setHasOptionsMenu(true);
    }

    /***
     * onListItemClick
     * Handles scenario where item in list is clicked
     * @param l
     * @param v
     * @param position
     * @param id
     */
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        // Call subsequent function for selected recipe
        callback.onItemSelected(RecipeContent.ITEMS.get(position).id);
    }

    /***
     * onAttach
     * Attaches to context
     * @param context
     */
    @TargetApi(23)
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onAttachToContext(context);
    }

    /***
     * onAttach
     * Attaches to activity
     * @param activity
     */
    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onAttachToContext(activity);
        }
    }

    /***
     * onAttachToContext
     * Sets callback when attaching to context
     * @param context
     */
    protected void onAttachToContext(Context context) {
        if (!(context instanceof Callback)) {
            throw new IllegalStateException("Activity must implement callback interface.");
        }
        callback = (Callback) context;
    }

}
