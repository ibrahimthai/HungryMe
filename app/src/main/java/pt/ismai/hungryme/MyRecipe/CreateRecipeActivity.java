package pt.ismai.hungryme.MyRecipe;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

import pt.ismai.hungryme.HelpingClass.SQLMyRecipeHelper;
import pt.ismai.hungryme.R;

public class CreateRecipeActivity extends AppCompatActivity implements View.OnClickListener  {

    private static final int RESULT_LOAD_IMAGE = 1;
    ImageView usersImage;
    FloatingActionButton uploadImage;
    Button createRecipeBtn;
    EditText imageName;

    EditText title;
    EditText author;
    EditText calories;
    EditText dietaryType;
    EditText ingredients;
    EditText directions;
    ImageView foodImage;

    public static SQLMyRecipeHelper myRecipeSQLHelper;

    final int REQUEST_CODE_GALLERY = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recipe);

        init();

        myRecipeSQLHelper = new SQLMyRecipeHelper(this, "MyRecipes.sqlite", null, 1);
        myRecipeSQLHelper.queryData("CREATE TABLE IF NOT EXISTS MYRECIPES (Id INTEGER PRIMARY KEY AUTOINCREMENT, title VARCHAR, author VARCHAR, label VARCHAR, calories VARCHAR, ingredients VARCHAR, directions VARCHAR, image BLOG)");

        // Upload an image
        uploadImage = (FloatingActionButton) findViewById(R.id.addImageButton);
        uploadImage.setOnClickListener(this);

        // Create a recipe
        createRecipeBtn = (Button) findViewById(R.id.createRecipeButton);
        createRecipeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    myRecipeSQLHelper.insertData(title.getText().toString().trim(),
                            author.getText().toString().trim(),
                            dietaryType.getText().toString().trim(),
                            calories.getText().toString().trim(),
                            ingredients.getText().toString().trim(),
                            directions.getText().toString().trim(),
                            imageViewToByte(usersImage));

                    Toast.makeText(getApplicationContext(), "Added successfully!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(CreateRecipeActivity.this, MyRecipesActivity.class);
                    startActivity(intent);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    // Add algorithm to upload image here
    protected void addImage() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
    }

    // Add algorithm to create recipe here
    protected void createRecipe() {

        Toast.makeText(CreateRecipeActivity.this, "Testing", Toast.LENGTH_LONG).show();

        /*
        SQLMyRecipeHelper myRecipeSQLHelper = new SQLMyRecipeHelper(this, "myRecipe.sqlite", null, 1);
        myRecipeSQLHelper.queryData("CREATE TABLE IF NOT EXISTS MyRecipe (Id INTEGER PRIMARY KEY AUTOINCREMENT, title VARCHAR, author VARCHAR, label VARCHAR, calories INT, ingredients VARCHAR, directions VARCHAR, image BLOG");

        ActivityCompat.requestPermissions(CreateRecipeActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                REQUEST_CODE_GALLERY);


        try {
            myRecipeSQLHelper.insertData(title.getText().toString().trim(),
                    author.getText().toString().trim(),
                    dietaryType.getText().toString().trim(),
                    (Integer.parseInt(calories.getText().toString().trim())),
                    ingredients.getText().toString().trim(),
                    directions.getText().toString().trim(),
                    imageViewToByte(usersImage));

            Toast.makeText(CreateRecipeActivity.this, "Added", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, MyRecipesActivity.class);
            startActivity(intent);

        } catch (Exception e) {
            e.printStackTrace();

        }

        */


    }

    private byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {

            // Get address of the image
            Uri selectedImage = data.getData();
            // Display the image
            usersImage.setImageURI(selectedImage);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == REQUEST_CODE_GALLERY){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            }
            else {
                Toast.makeText(getApplicationContext(), "You don't have permission to access file location!", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void init(){
        title = (EditText) findViewById(R.id.myRecipeTitle);
        author = (EditText) findViewById(R.id.myRecipeAuthor);
        dietaryType = (EditText) findViewById(R.id.myRecipeLabel);
        calories = (EditText) findViewById(R.id.myRecipeCalories);
        ingredients = (EditText) findViewById(R.id.myRecipeIngredients);
        directions = (EditText) findViewById(R.id.myRecipeDirections);
        usersImage = (ImageView) findViewById(R.id.myRecipeImage);
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.addImageButton:
                addImage();
                break;
            default:
        }
    }




}
