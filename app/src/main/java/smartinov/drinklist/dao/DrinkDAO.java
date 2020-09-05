package smartinov.drinklist.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import smartinov.drinklist.model.Drink;

@Dao
public interface DrinkDAO {

    @Insert
    void insert(Drink drink);

    @Update
    void update(Drink drink);

    @Delete
    void delete(Drink drink);

    @Query("DELETE FROM drinks_table")
    void deleteAll();

    @Query("SELECT * FROM DRINKS_TABLE ORDER BY name ASC")
    LiveData<List<Drink>> getAllDrinks();
}
