package smartinov.drinklist.dao;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import smartinov.drinklist.model.Drink;

public class DrinkRepository {

    private DrinkDAO drinkDAO;
    private LiveData<List<Drink>> allDrinks;

    public DrinkRepository(Application application) {
        DrinkDatabase database = DrinkDatabase.getInstance(application);
        drinkDAO = database.drinkDAO();
        allDrinks = drinkDAO.getAllDrinks();
    }

    public void insert(Drink drink) {

        new InsertDrinkAsyncTask(drinkDAO).execute(drink);
    }

    public void update(Drink drink) {
        new UpdateDrinkAsyncTask(drinkDAO).execute(drink);
    }

    public void delete(Drink drink) {
        new DeleteDrinkAsyncTask(drinkDAO).execute(drink);
    }

    public void deleteAll() {
        new DeleteAllDrinkAsyncTask(drinkDAO).execute();
    }

    public LiveData<List<Drink>> getAllDrinks() {
        return allDrinks;
    }

    private static class InsertDrinkAsyncTask extends AsyncTask<Drink, Void, Void> {
        private DrinkDAO drinkDAO;

        private InsertDrinkAsyncTask(DrinkDAO drinkDAO) {
            this.drinkDAO = drinkDAO;
        }

        @Override
        protected Void doInBackground(Drink... drinks) {
            drinkDAO.insert(drinks[0]);
            return null;
        }
    }

    private static class UpdateDrinkAsyncTask extends AsyncTask<Drink, Void, Void> {
        private DrinkDAO drinkDAO;

        private UpdateDrinkAsyncTask(DrinkDAO drinkDAO) {
            this.drinkDAO = drinkDAO;
        }

        @Override
        protected Void doInBackground(Drink... drinks) {
            drinkDAO.update(drinks[0]);
            return null;
        }
    }

    private static class DeleteDrinkAsyncTask extends AsyncTask<Drink, Void, Void> {
        private DrinkDAO drinkDAO;

        private DeleteDrinkAsyncTask(DrinkDAO drinkDAO) {
            this.drinkDAO = drinkDAO;
        }

        @Override
        protected Void doInBackground(Drink... drinks) {
            drinkDAO.delete(drinks[0]);
            return null;
        }
    }

    private static class DeleteAllDrinkAsyncTask extends AsyncTask<Void, Void, Void> {
        private DrinkDAO drinkDAO;

        private DeleteAllDrinkAsyncTask(DrinkDAO drinkDAO) {
            this.drinkDAO = drinkDAO;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            drinkDAO.deleteAll();
            return null;
        }
    }
}
