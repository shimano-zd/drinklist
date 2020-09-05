package smartinov.drinklist.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import smartinov.drinklist.R;
import smartinov.drinklist.model.Drink;
import smartinov.drinklist.view.adapter.DrinkAdapter;
import smartinov.drinklist.view.adapter.DrinkClickListener;
import smartinov.drinklist.viewmodel.DrinkViewModel;

public class MainActivity extends AppCompatActivity {

    private DrinkViewModel viewModel;
    private static final int ADD_DRINK_REQUEST = 1;
    private static final int EDIT_DRINK_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton buttonAddDrink = findViewById(R.id.button_add_drink);
        buttonAddDrink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddEditDrinkActivity.class);
                startActivityForResult(intent, ADD_DRINK_REQUEST);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final DrinkAdapter adapter = new DrinkAdapter();
        recyclerView.setAdapter(adapter);

        viewModel = ViewModelProviders.of(this).get(DrinkViewModel.class);
        viewModel.getAllDrinks().observe(this, new Observer<List<Drink>>() {
            @Override
            public void onChanged(List<Drink> drinks) {

                adapter.submitList(drinks);

            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            // delete items when swiping from the main list
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                viewModel.delete(adapter.getNoteAtPosition(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Drink deleted!", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnDrinkClickListener(new DrinkClickListener() {
            @Override
            public void OnDrinkClicked(Drink drink) {
                Intent intent = new Intent(MainActivity.this, AddEditDrinkActivity.class);
                intent.putExtra(AddEditDrinkActivity.EXTRA_NAME, drink.getName());
                intent.putExtra(AddEditDrinkActivity.EXTRA_INGREDIENTS, drink.getIngredients());
                intent.putExtra(AddEditDrinkActivity.EXTRA_ALCOHOL, drink.getAlcoholPercentage());
                intent.putExtra(AddEditDrinkActivity.EXTRA_ID, drink.getId());
                intent.putExtra(AddEditDrinkActivity.EXTRA_IMAGE, drink.getImageLocation());

                startActivityForResult(intent, EDIT_DRINK_REQUEST);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_DRINK_REQUEST && resultCode == RESULT_OK) {
            String name = data.getStringExtra(AddEditDrinkActivity.EXTRA_NAME);
            String ingredients = data.getStringExtra(AddEditDrinkActivity.EXTRA_INGREDIENTS);
            int alc = data.getIntExtra(AddEditDrinkActivity.EXTRA_ALCOHOL, 0);
            String imagePath = data.getStringExtra(AddEditDrinkActivity.EXTRA_IMAGE);

            Drink drink = new Drink(name, ingredients, alc);
            drink.setImageLocation(imagePath);
            viewModel.insert(drink);


            Toast.makeText(this, "Drink saved!", Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_DRINK_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddEditDrinkActivity.EXTRA_ID, -1);
            if(id == -1){
                Toast.makeText(this, "Drink cannot be updated...", Toast.LENGTH_SHORT).show();
            }

            String name = data.getStringExtra(AddEditDrinkActivity.EXTRA_NAME);
            String ingredients = data.getStringExtra(AddEditDrinkActivity.EXTRA_INGREDIENTS);
            int alc = data.getIntExtra(AddEditDrinkActivity.EXTRA_ALCOHOL, 0);
            String imagePath = data.getStringExtra(AddEditDrinkActivity.EXTRA_IMAGE);

            Drink drink = new Drink(name, ingredients, alc);
            drink.setId(id);
            drink.setImageLocation(imagePath);

            viewModel.update(drink);
            Toast.makeText(this, "Updated!", Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.delete_all_drinks:
                viewModel.deleteAllDrinks();
                Toast.makeText(this, "All drinks deleted!", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }
}
