package smartinov.drinklist.dao;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import smartinov.drinklist.model.Drink;

@Database(entities = Drink.class, version = 3, exportSchema = false)
public abstract class DrinkDatabase extends RoomDatabase {

    private static DrinkDatabase instance;

    public abstract DrinkDAO drinkDAO();

    public static synchronized DrinkDatabase getInstance(Context context){

        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    DrinkDatabase.class, "drinks_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }

        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDBAsyncTask(instance).execute();
        }
    };

    private static class PopulateDBAsyncTask extends AsyncTask<Void, Void, Void>{

        private DrinkDAO drinkDAO;

        private PopulateDBAsyncTask(DrinkDatabase database){
            drinkDAO = database.drinkDAO();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            drinkDAO.insert(new Drink("Coffee", "water, coffee, milk", 0));
            drinkDAO.insert(new Drink("Fanta", "carbonized water, sugar, orange", 0));
            drinkDAO.insert(new Drink("Beer", "water, hops, barley", 5));
            return null;
        }

    }

}
