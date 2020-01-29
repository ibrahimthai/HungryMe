package pt.ismai.hungryme.MyRecipe;

public class MyRecipes {
    int id;
    String title;
    String author;
    String label;
    String calories;
    String ingredients;
    String directions;
    byte[] image;

    public MyRecipes(String title, String author, String label, String calories, String ingredients, String directions, byte[] image, int id) {
        this.title = title;
        this.author = author;
        this.label = label;
        this.calories = calories;
        this.ingredients = ingredients;
        this.directions = directions;
        this.image = image;
        this.id = id;

    }

    // ID
    public int getID() {
        return id;
    }
    public void setID(int id) {
        this.id = id;
    }

    // Title
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    // Author
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }

    // Label
    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }

    // Calories
    public String getCalories() {
        return calories;
    }
    public void setCalories(String calories) {
        this.calories = calories;
    }

    // Ingredients
    public String getIngredients() {
        return ingredients;
    }
    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    // Direction
    public String getDirections() {
        return directions;
    }
    public void setDirections(String directions) {
        this.directions = directions;
    }

    // Food Image
    public byte[] getImage() { return image; }
    public void setImage(byte[] image) { this.image = image; }


}
