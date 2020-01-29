package pt.ismai.hungryme.MyRecipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyRecipeContent {

    public static final List<MyRecipeContent.MyRecipeItem> ITEMS = new ArrayList<>();

    public static final Map<String, MyRecipeContent.MyRecipeItem> ITEM_MAP = new HashMap<>(7);

    private static void addMyItem(MyRecipeContent.MyRecipeItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
        ITEM_MAP.put(item.author, item);
        ITEM_MAP.put(item.photoURL, item);
        ITEM_MAP.put(item.title, item);
        ITEM_MAP.put(item.label, item);
        ITEM_MAP.put(item.ingredients, item);
        ITEM_MAP.put(String.valueOf(item.calories), item);
        ITEM_MAP.put(item.directions, item);
    }

    public static class MyRecipeItem {
        public String id;
        public String photoURL;
        public String title;
        public String author;
        public String label;
        public String ingredients;
        public String calories;
        public String directions;

        public MyRecipeItem(String id, String photoURL, String title, String author, String label, String ingredients, String calories, String directions) {
            super();
            this.id = id;
            this.photoURL = photoURL;
            this.title = title;
            this.author = author;
            this.label = label;
            this.ingredients = ingredients;
            this.calories = calories;
            this.directions = directions;
        }
    }

}
