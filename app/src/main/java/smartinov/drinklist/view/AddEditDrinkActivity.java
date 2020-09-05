package smartinov.drinklist.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import smartinov.drinklist.R;

public class AddEditDrinkActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextIngredients;
    private NumberPicker numberPickerAlcohol;
    private ImageView imageView;
    private Button buttonTakeImage;
    private String currentImageLocation;



    public static final String EXTRA_NAME =
            "smartinov.drinklist.view.EXTRA_NAME";
    public static final String EXTRA_INGREDIENTS =
            "smartinov.drinklist.view.EXTRA_INGREDIENTS";
    public static final String EXTRA_ALCOHOL =
            "smartinov.drinklist.view.EXTRA_ALCOHOL";
    public static final String EXTRA_ID =
            "smartinov.drinklist.view.EXTRA_ID";
    public static final String EXTRA_IMAGE =
            "smartinov.drinklist.view.EXTRA_IMAGE";
    public static final int TAKE_PICTURE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_drink);

        editTextIngredients = findViewById(R.id.edit_text_ingredients);
        editTextName = findViewById(R.id.edit_text_name);
        numberPickerAlcohol = findViewById(R.id.number_picker_alcohol);
        imageView = findViewById(R.id.edit_view_image);
        buttonTakeImage = findViewById(R.id.button_take_image);


        numberPickerAlcohol.setMinValue(0);
        numberPickerAlcohol.setMaxValue(100);
        numberPickerAlcohol.setValue(0);


        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit Drink");
            editTextName.setText(intent.getStringExtra(EXTRA_NAME));
            editTextIngredients.setText(intent.getStringExtra(EXTRA_INGREDIENTS));
            numberPickerAlcohol.setValue(intent.getIntExtra(EXTRA_ALCOHOL, 0));

        } else {
            setTitle("Add Drink");
        }

        if (intent.getStringExtra(EXTRA_IMAGE) != null) {

            try {

                String drinkImage = intent.getStringExtra(EXTRA_IMAGE);
                Picasso.get()
                        .load("file://" + drinkImage)
                        .placeholder(R.drawable.image_loader)
                        .fit()
                        .centerCrop()
                        .into(imageView);

            } catch (Exception ex) {
                Toast.makeText(this, "Could not load image", Toast.LENGTH_SHORT).show();
            }

        }

        buttonTakeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_drink_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_drink:
                saveDrink();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void saveDrink() {

        String name = editTextName.getText().toString();
        String ingredients = editTextIngredients.getText().toString();
        int alcohol = numberPickerAlcohol.getValue();

        if (name.trim().isEmpty() || ingredients.trim().isEmpty()) {
            Toast.makeText(this, "Please enter the name of the drink and its ingredients", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_NAME, name);
        data.putExtra(EXTRA_INGREDIENTS, ingredients);
        data.putExtra(EXTRA_ALCOHOL, alcohol);
        data.putExtra(EXTRA_IMAGE, currentImageLocation);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();


    }

    private void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(AddEditDrinkActivity.this.getPackageManager()) == null) {
            showImageErrorToast();
            return;
        }

        File image = null;
        try {
            image = createImageFile();
        } catch (IOException ex) {
            showImageErrorToast();
            return;
        }

        if (image == null) {
            showImageErrorToast();
            return;
        }

        Uri imageUri = FileProvider.getUriForFile(this, "smartinov.drinklist.provider", image);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(takePictureIntent, TAKE_PICTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_PICTURE && resultCode == Activity.RESULT_OK) {

            Picasso.get()
                    .load("file://" + currentImageLocation)
                    .placeholder(R.drawable.image_loader)
                    .fit().centerCrop()
                    .into(imageView);
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageName = "Drink_" + timeStamp + "_";
        File storageDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageName,
                ".jpg",
                storageDir
        );

        currentImageLocation = image.getAbsolutePath();
        return image;

    }

    private void showImageErrorToast() {
        Toast.makeText(this, "There was an error taking photo", Toast.LENGTH_LONG).show();
    }
}
